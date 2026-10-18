"""Autenticação demonstrativa usando o padrão GoF Factory Method.

O banco de usuários, o canal de e-mail e os provedores sociais são simulações
para fins acadêmicos.
"""

from __future__ import annotations

import hashlib
import hmac
import secrets
import threading
import time
from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Protocol

from singleton_sessao import GerenciadorSessao

MENSAGEM_GENERICA = "E-mail ou senha inválidos."
MENSAGEM_CODIGO = "Se o e-mail estiver cadastrado, você receberá um código válido por 15 minutos."
MAX_TENTATIVAS = 5
BLOQUEIO_SEGUNDOS = 15 * 60
VALIDADE_CODIGO_SEGUNDOS = 15 * 60
MAX_TENTATIVAS_CODIGO = 5
ITERACOES_PBKDF2 = 600_000


def normalizar_email(email: str) -> str:
    """Normaliza endereços para evitar diferenças por espaços e maiúsculas."""
    return email.strip().casefold()


def gerar_hash(senha: str, salt: bytes) -> bytes:
    """Deriva um hash de senha com PBKDF2-HMAC-SHA256.

    Em produção, avalie Argon2id ou os parâmetros atuais recomendados pela
    OWASP, considerando também a capacidade do ambiente de execução.
    """
    return hashlib.pbkdf2_hmac(
        "sha256",
        senha.encode("utf-8"),
        salt,
        ITERACOES_PBKDF2,
    )


class CanalEmail(Protocol):
    def enviar_codigo(self, email: str, codigo: str) -> None:
        """Entrega o código ao destinatário. A implementação real deve enviar e-mail."""
        ...


class CanalEmailSimulado:
    """Caixa de e-mail falsa para demonstração; substitua em produção.

    O código pode ser consultado pelo teste local, mas nunca deve ser devolvido
    por uma rota pública da aplicação.
    """

    def __init__(self) -> None:
        self._mensagens: dict[str, str] = {}
        self._lock = threading.Lock()

    def enviar_codigo(self, email: str, codigo: str) -> None:
        with self._lock:
            self._mensagens[normalizar_email(email)] = codigo
        print(f"  [E-MAIL SIMULADO] Código enviado para {email}.")

    def ler_codigo_para_teste(self, email: str) -> str | None:
        """Método exclusivo do adaptador de demonstração, não de produção."""
        with self._lock:
            return self._mensagens.get(normalizar_email(email))


class BancoUsuarios:
    """Repositório em memória que simula um banco de dados."""

    def __init__(self) -> None:
        self.usuarios: dict[str, dict] = {}
        self.falhas: dict[str, dict] = {}
        self.codigos: dict[str, dict] = {}
        self.lock = threading.RLock()
        # Em produção, este segredo deve vir de um gerenciador de segredos e
        # ser compatível com a estratégia de persistência escolhida.
        self._segredo_codigos = secrets.token_bytes(32)
        self._salt_falso = secrets.token_bytes(16)
        self._hash_falso = gerar_hash("senha-falsa-para-tempo-constante", self._salt_falso)

    def cadastrar(self, email: str, senha: str, nome: str) -> None:
        email_normalizado = normalizar_email(email)
        if not email_normalizado or not senha:
            raise ValueError("E-mail e senha são obrigatórios.")

        salt = secrets.token_bytes(16)
        registro = {
            "nome": nome,
            "salt": salt,
            "hash": gerar_hash(senha, salt),
        }
        with self.lock:
            if email_normalizado in self.usuarios:
                raise ValueError("Já existe um usuário com esse e-mail.")
            self.usuarios[email_normalizado] = registro

    def buscar(self, email: str) -> dict | None:
        with self.lock:
            return self.usuarios.get(normalizar_email(email))

    def usuario_existe(self, email: str) -> bool:
        with self.lock:
            return normalizar_email(email) in self.usuarios

    def hash_codigo(self, email: str, codigo: str) -> bytes:
        mensagem = f"{normalizar_email(email)}:{codigo}".encode("utf-8")
        return hmac.new(self._segredo_codigos, mensagem, hashlib.sha256).digest()


@dataclass
class ResultadoAutenticacao:
    sucesso: bool
    mensagem: str
    email: str | None = None
    token_sessao: str | None = None


# Simulação local dos tokens que um provedor OAuth reconheceria.
# Isto NÃO valida tokens OAuth reais e não deve ser usado em produção.
TOKENS_VALIDOS = {
    "google": {"tok-google-123": "ana@gmail.com"},
    "facebook": {"tok-facebook-456": "bruno@hotmail.com"},
    "apple": {"tok-apple-789": "carla@icloud.com"},
}


# ---------------------------------------------------------------------------
# PRODUCT
# ---------------------------------------------------------------------------
class Autenticador(ABC):
    def __init__(self, banco: BancoUsuarios) -> None:
        self.banco = banco

    @abstractmethod
    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        """Autentica as credenciais informadas."""
        raise NotImplementedError


# ---------------------------------------------------------------------------
# CONCRETE PRODUCTS
# ---------------------------------------------------------------------------
class AutenticadorLocal(Autenticador):
    """Login com e-mail e senha, com bloqueio temporário após falhas consecutivas."""

    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        email = normalizar_email(str(credenciais.get("email", "")))
        senha = str(credenciais.get("senha", ""))
        if not email or not senha:
            return ResultadoAutenticacao(False, MENSAGEM_GENERICA)

        agora = time.time()
        # A verificação e atualização do contador são sincronizadas. O hash é
        # calculado dentro da trava nesta simulação para manter atômica a regra
        # de bloqueio; um sistema de produção deve usar transações/limitação
        # de taxa distribuída adequada ao banco e à infraestrutura.
        with self.banco.lock:
            falha = self.banco.falhas.setdefault(
                email, {"qtd": 0, "bloqueado_ate": 0.0}
            )

            if falha["bloqueado_ate"] > agora:
                # Resposta externa genérica para não confirmar existência da conta.
                return ResultadoAutenticacao(False, MENSAGEM_GENERICA)

            usuario = self.banco.usuarios.get(email)
            if usuario is None:
                salt = self.banco._salt_falso
                hash_armazenado = self.banco._hash_falso
            else:
                salt = usuario["salt"]
                hash_armazenado = usuario["hash"]

            hash_informado = gerar_hash(senha, salt)
            senha_ok = hmac.compare_digest(hash_informado, hash_armazenado)
            autenticado = usuario is not None and senha_ok

            if not autenticado:
                falha["qtd"] += 1
                if falha["qtd"] >= MAX_TENTATIVAS:
                    falha["bloqueado_ate"] = agora + BLOQUEIO_SEGUNDOS
                    falha["qtd"] = 0
                return ResultadoAutenticacao(False, MENSAGEM_GENERICA)

            # Credenciais válidas: zera o contador de falhas do usuário.
            falha["qtd"] = 0
            falha["bloqueado_ate"] = 0.0

        return ResultadoAutenticacao(True, "Login local efetuado.", email)


class AutenticadorSocial(Autenticador):
    """Base comum para a simulação de login social."""

    provedor = ""

    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        token = str(credenciais.get("token", ""))
        email = TOKENS_VALIDOS.get(self.provedor, {}).get(token)
        if email is None:
            return ResultadoAutenticacao(
                False,
                f"Não foi possível autenticar com {self.provedor.title()}.",
            )
        return ResultadoAutenticacao(
            True,
            f"Login via {self.provedor.title()} efetuado (simulação).",
            normalizar_email(email),
        )


class AutenticadorGoogle(AutenticadorSocial):
    provedor = "google"


class AutenticadorFacebook(AutenticadorSocial):
    provedor = "facebook"


class AutenticadorApple(AutenticadorSocial):
    provedor = "apple"


class AutenticadorCodigoEmail(Autenticador):
    """Código de seis dígitos, de uso único e validade de 15 minutos."""

    def __init__(self, banco: BancoUsuarios, canal_email: CanalEmail) -> None:
        super().__init__(banco)
        self.canal_email = canal_email

    def enviar_codigo(self, email: str) -> str:
        """Solicita o envio do código e sempre devolve uma mensagem neutra.

        O código nunca é retornado ao chamador. A classe de e-mail simulada
        permite lê-lo apenas para demonstrar o fluxo no script local.
        """
        email_normalizado = normalizar_email(email)
        if not email_normalizado:
            return MENSAGEM_CODIGO

        # A resposta pública é idêntica exista ou não uma conta para o endereço.
        if not self.banco.usuario_existe(email_normalizado):
            return MENSAGEM_CODIGO

        codigo = f"{secrets.randbelow(1_000_000):06d}"
        agora = time.time()
        registro = {
            "hash": self.banco.hash_codigo(email_normalizado, codigo),
            "expira_em": agora + VALIDADE_CODIGO_SEGUNDOS,
            "tentativas": 0,
        }

        with self.banco.lock:
            self.banco.codigos[email_normalizado] = registro

        try:
            self.canal_email.enviar_codigo(email_normalizado, codigo)
        except Exception:
            # Não deixe um código utilizável caso o envio falhe.
            with self.banco.lock:
                self.banco.codigos.pop(email_normalizado, None)
            # Em produção, registre detalhes técnicos em log protegido.

        return MENSAGEM_CODIGO

    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        email = normalizar_email(str(credenciais.get("email", "")))
        codigo = str(credenciais.get("codigo", ""))
        if not email or not codigo:
            return ResultadoAutenticacao(False, "Código inválido ou expirado.")

        agora = time.time()
        hash_informado = self.banco.hash_codigo(email, codigo)

        # A validação e a remoção acontecem em uma única seção crítica. Uma
        # tentativa errada não consome o código correto; uma tentativa válida
        # o consome exatamente uma vez, mesmo sob chamadas concorrentes.
        with self.banco.lock:
            pendente = self.banco.codigos.get(email)
            if pendente is None:
                return ResultadoAutenticacao(False, "Código inválido ou expirado.")

            if pendente["expira_em"] <= agora:
                self.banco.codigos.pop(email, None)
                return ResultadoAutenticacao(False, "Código inválido ou expirado.")

            if pendente["tentativas"] >= MAX_TENTATIVAS_CODIGO:
                self.banco.codigos.pop(email, None)
                return ResultadoAutenticacao(False, "Código inválido ou expirado.")

            if not hmac.compare_digest(hash_informado, pendente["hash"]):
                pendente["tentativas"] += 1
                if pendente["tentativas"] >= MAX_TENTATIVAS_CODIGO:
                    self.banco.codigos.pop(email, None)
                return ResultadoAutenticacao(False, "Código inválido ou expirado.")

            # Consumo atômico: não será possível reutilizar o mesmo código.
            self.banco.codigos.pop(email, None)

        return ResultadoAutenticacao(
            True,
            "Login por código de uso único efetuado.",
            email,
        )


# ---------------------------------------------------------------------------
# CREATOR: declara o Factory Method e usa o produto pela abstração
# ---------------------------------------------------------------------------
class ServicoLogin(ABC):
    def __init__(
        self,
        banco: BancoUsuarios,
        canal_email: CanalEmail | None = None,
    ) -> None:
        self.banco = banco
        self.canal_email = canal_email

    @abstractmethod
    def criar_autenticador(self) -> Autenticador:
        """Factory Method: cada subclasse decide qual autenticador criar."""
        raise NotImplementedError

    def realizar_login(self, credenciais: dict) -> ResultadoAutenticacao:
        autenticador = self.criar_autenticador()
        resultado = autenticador.autenticar(credenciais)

        if resultado.sucesso and resultado.email:
            resultado.token_sessao = self._iniciar_sessao(resultado.email)

        return resultado

    def _iniciar_sessao(self, email: str) -> str:
        """Registra a sessão no gerenciador Singleton compartilhado neste processo."""
        token = GerenciadorSessao().criar_sessao(email)
        # Não registre o token completo ou parcial: trate-o como segredo.
        print(f"  [sessão] Sessão iniciada para {email}.")
        return token


# ---------------------------------------------------------------------------
# CONCRETE CREATORS
# ---------------------------------------------------------------------------
class LoginLocal(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorLocal(self.banco)


class LoginGoogle(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorGoogle(self.banco)


class LoginFacebook(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorFacebook(self.banco)


class LoginApple(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorApple(self.banco)


class LoginCodigoEmail(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        if self.canal_email is None:
            raise RuntimeError(
                "LoginCodigoEmail precisa de um canal de e-mail configurado."
            )
        return AutenticadorCodigoEmail(self.banco, self.canal_email)


BOTOES_LOGIN = {
    "local": LoginLocal,
    "google": LoginGoogle,
    "facebook": LoginFacebook,
    "apple": LoginApple,
    "codigo_email": LoginCodigoEmail,
}


def tentar(titulo: str, servico: ServicoLogin, credenciais: dict) -> ResultadoAutenticacao:
    print(f"\n>> {titulo}")
    resultado = servico.realizar_login(credenciais)
    estado = "OK" if resultado.sucesso else "FALHA"
    print(f"  {estado}: {resultado.mensagem}")
    return resultado


if __name__ == "__main__":
    banco = BancoUsuarios()
    banco.cadastrar("rafaela@hotmail.com", "Senha@123", "Rafaela")
    canal_email = CanalEmailSimulado()

    local = BOTOES_LOGIN["local"](banco)

    print("=== 1. Login local ===")
    tentar(
        "Senha correta",
        local,
        {"email": "rafaela@hotmail.com", "senha": "Senha@123"},
    )
    tentar(
        "E-mail que não existe",
        local,
        {"email": "nao_existe@gmail.com", "senha": "x"},
    )
    tentar(
        "Senha errada (mensagem genérica)",
        local,
        {"email": "rafaela@hotmail.com", "senha": "errada"},
    )

    print("\n=== 2. Bloqueio após 5 tentativas erradas ===")
    # Houve uma tentativa errada para esta conta acima; as próximas quatro
    # completam cinco tentativas consecutivas após o login válido inicial.
    for numero in range(2, 6):
        tentar(
            f"Senha errada #{numero}",
            local,
            {"email": "rafaela@hotmail.com", "senha": "errada"},
        )
    tentar(
        "Senha correta, mas conta temporariamente bloqueada",
        local,
        {"email": "rafaela@hotmail.com", "senha": "Senha@123"},
    )

    print("\n=== 3. Login social simulado ===")
    tentar(
        "Google com token de teste válido",
        BOTOES_LOGIN["google"](banco),
        {"token": "tok-google-123"},
    )
    tentar(
        "Facebook com token inválido",
        BOTOES_LOGIN["facebook"](banco),
        {"token": "falso"},
    )
    tentar(
        "Apple com token de teste válido",
        BOTOES_LOGIN["apple"](banco),
        {"token": "tok-apple-789"},
    )

    print("\n=== 4. Código de uso único por e-mail ===")
    servico_codigo = BOTOES_LOGIN["codigo_email"](banco, canal_email)
    autenticador_codigo = servico_codigo.criar_autenticador()
    mensagem = autenticador_codigo.enviar_codigo("rafaela@hotmail.com")
    print(f"  Resposta ao pedido: {mensagem}")

    # A leitura do código é exclusiva da caixa de e-mail simulada para este teste.
    codigo = canal_email.ler_codigo_para_teste("rafaela@hotmail.com")
    if codigo is not None:
        tentar(
            "Código correto",
            servico_codigo,
            {"email": "rafaela@hotmail.com", "codigo": codigo},
        )
        tentar(
            "Reutilizar o mesmo código",
            servico_codigo,
            {"email": "rafaela@hotmail.com", "codigo": codigo},
        )

    print("\n=== 5. Sessões ativas no Singleton ===")
    print(
        "Sessões de rafaela@hotmail.com: "
        f"{len(GerenciadorSessao().sessoes_ativas('rafaela@hotmail.com'))}"
    )
