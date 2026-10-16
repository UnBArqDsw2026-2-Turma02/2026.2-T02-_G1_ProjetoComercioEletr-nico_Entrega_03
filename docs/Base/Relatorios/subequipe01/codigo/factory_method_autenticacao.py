"""
Padrão GoF Criacional: Factory Method
Contexto: Fluxo A - Gestão de usuário, autenticação e perfil (Subequipe 01)

Estrutura do padrão:
  Product           -> Autenticador
  ConcreteProduct   -> AutenticadorLocal, AutenticadorGoogle, AutenticadorFacebook,
                       AutenticadorApple, AutenticadorCodigoEmail
  Creator           -> ServicoLogin (declara o factory method criar_autenticador())
  ConcreteCreator   -> LoginLocal, LoginGoogle, LoginFacebook, LoginApple, LoginCodigoEmail

Regras de segurança baseadas na análise da Decathlon (ver documentação):
  - Mensagem de erro genérica (evita enumeração de usuários)
  - Bloqueio temporário após 5 tentativas erradas (a Decathlon não bloqueou)
  - Senha armazenada com hash + salt (nunca em texto puro)
  - Código de uso único por e-mail, válido por 15 minutos e usado só uma vez

Integração com o Singleton: ao autenticar, a sessão é registrada no
GerenciadorSessao (ver singleton_sessao.py).
"""
from __future__ import annotations

import hashlib
import hmac
import secrets
import time
from abc import ABC, abstractmethod
from dataclasses import dataclass

from singleton_sessao import GerenciadorSessao

MENSAGEM_GENERICA = "E-mail ou senha inválidos."
MAX_TENTATIVAS = 5
BLOQUEIO_SEGUNDOS = 15 * 60
VALIDADE_CODIGO_SEGUNDOS = 15 * 60


# ---------------------------------------------------------------------------
# Infraestrutura simulada (no projeto real seria o banco de dados)
# ---------------------------------------------------------------------------
def gerar_hash(senha: str, salt: bytes) -> bytes:
    return hashlib.pbkdf2_hmac("sha256", senha.encode(), salt, 100_000)


class BancoUsuarios:
    def __init__(self):
        self.usuarios: dict[str, dict] = {}
        self.falhas: dict[str, dict] = {}    # tentativas por e-mail (existindo ou não)
        self.codigos: dict[str, dict] = {}   # códigos de uso único pendentes

    def cadastrar(self, email: str, senha: str, nome: str) -> None:
        salt = secrets.token_bytes(16)
        self.usuarios[email.lower()] = {"nome": nome, "salt": salt, "hash": gerar_hash(senha, salt)}

    def buscar(self, email: str) -> dict | None:
        return self.usuarios.get(email.lower())


# Tokens que o "provedor OAuth" reconheceria (simulação da API externa)
TOKENS_VALIDOS = {
    "google": {"tok-google-123": "ana@gmail.com"},
    "facebook": {"tok-facebook-456": "bruno@hotmail.com"},
    "apple": {"tok-apple-789": "carla@icloud.com"},
}


@dataclass
class ResultadoAutenticacao:
    sucesso: bool
    mensagem: str
    email: str | None = None
    token_sessao: str | None = None


# ---------------------------------------------------------------------------
# PRODUCT
# ---------------------------------------------------------------------------
class Autenticador(ABC):
    def __init__(self, banco: BancoUsuarios):
        self.banco = banco

    @abstractmethod
    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        ...


# ---------------------------------------------------------------------------
# CONCRETE PRODUCTS
# ---------------------------------------------------------------------------
class AutenticadorLocal(Autenticador):
    """Login com e-mail e senha."""

    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        email = credenciais.get("email", "").lower()
        senha = credenciais.get("senha", "")
        registro = self.banco.falhas.setdefault(email, {"qtd": 0, "bloqueado_ate": 0.0})

        # Bloqueio vale até para e-mails inexistentes, para não revelar quem existe
        if registro["bloqueado_ate"] > time.time():
            return ResultadoAutenticacao(False, "Muitas tentativas. Tente novamente em 15 minutos.")

        usuario = self.banco.buscar(email)
        senha_ok = usuario is not None and hmac.compare_digest(
            gerar_hash(senha, usuario["salt"]), usuario["hash"]
        )

        if not senha_ok:
            registro["qtd"] += 1
            if registro["qtd"] >= MAX_TENTATIVAS:
                registro["bloqueado_ate"] = time.time() + BLOQUEIO_SEGUNDOS
                registro["qtd"] = 0
            # Mesma mensagem para e-mail inexistente e senha errada
            return ResultadoAutenticacao(False, MENSAGEM_GENERICA)

        registro["qtd"] = 0
        return ResultadoAutenticacao(True, "Login local efetuado.", email)


class AutenticadorSocial(Autenticador):
    """Base comum para login social (OAuth). Cada provedor define seu nome."""
    provedor = ""

    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        token = credenciais.get("token", "")
        email = TOKENS_VALIDOS.get(self.provedor, {}).get(token)
        if email is None:
            return ResultadoAutenticacao(False, f"Não foi possível autenticar com {self.provedor.title()}.")
        return ResultadoAutenticacao(True, f"Login via {self.provedor.title()} efetuado.", email)


class AutenticadorGoogle(AutenticadorSocial):
    provedor = "google"


class AutenticadorFacebook(AutenticadorSocial):
    provedor = "facebook"


class AutenticadorApple(AutenticadorSocial):
    provedor = "apple"


class AutenticadorCodigoEmail(Autenticador):
    """'Obtenha uma senha de uso único': código de 6 dígitos enviado por e-mail."""

    def enviar_codigo(self, email: str) -> str | None:
        email = email.lower()
        print("  Se o e-mail estiver cadastrado, você receberá um código.")  # resposta neutra
        if self.banco.buscar(email) is None:
            return None
        codigo = f"{secrets.randbelow(1_000_000):06d}"
        self.banco.codigos[email] = {"codigo": codigo, "expira": time.time() + VALIDADE_CODIGO_SEGUNDOS}
        return codigo  # simulação: no sistema real, o código só iria para o e-mail

    def autenticar(self, credenciais: dict) -> ResultadoAutenticacao:
        email = credenciais.get("email", "").lower()
        codigo = credenciais.get("codigo", "")
        pendente = self.banco.codigos.pop(email, None)  # pop = código só pode ser usado uma vez

        if (pendente is None
                or pendente["expira"] < time.time()
                or not hmac.compare_digest(pendente["codigo"], codigo)):
            return ResultadoAutenticacao(False, "Código inválido ou expirado.")
        return ResultadoAutenticacao(True, "Login por código de uso único efetuado.", email)


# ---------------------------------------------------------------------------
# CREATOR: declara o factory method e usa o produto sem saber qual é
# ---------------------------------------------------------------------------
class ServicoLogin(ABC):
    def __init__(self, banco: BancoUsuarios):
        self.banco = banco

    @abstractmethod
    def criar_autenticador(self) -> Autenticador:
        """FACTORY METHOD: cada subclasse decide qual autenticador criar."""

    def realizar_login(self, credenciais: dict) -> ResultadoAutenticacao:
        autenticador = self.criar_autenticador()
        resultado = autenticador.autenticar(credenciais)
        if resultado.sucesso:
            resultado.token_sessao = self._iniciar_sessao(resultado.email)
        return resultado

    def _iniciar_sessao(self, email: str) -> str:
        # Singleton: todos os tipos de login registram a sessão no mesmo gerenciador
        token = GerenciadorSessao().criar_sessao(email)
        print(f"  [sessão] Sessão iniciada para {email} (token {token[:8]}...)")
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
    def criar_autenticador(self) -> AutenticadorCodigoEmail:
        return AutenticadorCodigoEmail(self.banco)


# ---------------------------------------------------------------------------
# CLIENTE: cada botão da tela de login corresponde a um ConcreteCreator
# ---------------------------------------------------------------------------
BOTOES_LOGIN = {
    "local": LoginLocal,
    "google": LoginGoogle,
    "facebook": LoginFacebook,
    "apple": LoginApple,
    "codigo_email": LoginCodigoEmail,
}


def tentar(titulo: str, servico: ServicoLogin, credenciais: dict) -> None:
    print(f"\n>> {titulo}")
    resultado = servico.realizar_login(credenciais)
    print(f"  {'OK' if resultado.sucesso else 'FALHA'}: {resultado.mensagem}")


if __name__ == "__main__":
    banco = BancoUsuarios()
    banco.cadastrar("rafaela@hotmail.com", "Senha@123", "Rafaela")

    local = BOTOES_LOGIN["local"](banco)

    print("=== 1. Login local ===")
    tentar("Senha correta", local, {"email": "rafaela@hotmail.com", "senha": "Senha@123"})
    tentar("E-mail que não existe", local, {"email": "nao_existe@gmail.com", "senha": "x"})
    tentar("Senha errada (mesma mensagem: sem enumeração)", local,
           {"email": "rafaela@hotmail.com", "senha": "errada"})

    print("\n=== 2. Bloqueio após 5 tentativas ===")
    for i in range(2, 6):
        tentar(f"Senha errada #{i}", local, {"email": "rafaela@hotmail.com", "senha": "errada"})
    tentar("Senha correta, mas conta bloqueada", local,
           {"email": "rafaela@hotmail.com", "senha": "Senha@123"})

    print("\n=== 3. Login social ===")
    tentar("Google com token válido", BOTOES_LOGIN["google"](banco), {"token": "tok-google-123"})
    tentar("Facebook com token inválido", BOTOES_LOGIN["facebook"](banco), {"token": "falso"})
    tentar("Apple com token válido", BOTOES_LOGIN["apple"](banco), {"token": "tok-apple-789"})

    print("\n=== 4. Senha de uso único por e-mail ===")
    servico_codigo = BOTOES_LOGIN["codigo_email"](banco)
    codigo = servico_codigo.criar_autenticador().enviar_codigo("rafaela@hotmail.com")
    print(f"  (simulação) código recebido no e-mail: {codigo}")
    tentar("Código correto", servico_codigo, {"email": "rafaela@hotmail.com", "codigo": codigo})
    tentar("Reusar o mesmo código", servico_codigo, {"email": "rafaela@hotmail.com", "codigo": codigo})

    print("\n=== 5. Sessões registradas no Singleton ===")
    print(f"  Dispositivos de rafaela@hotmail.com: {GerenciadorSessao().dispositivos_conectados('rafaela@hotmail.com')}")