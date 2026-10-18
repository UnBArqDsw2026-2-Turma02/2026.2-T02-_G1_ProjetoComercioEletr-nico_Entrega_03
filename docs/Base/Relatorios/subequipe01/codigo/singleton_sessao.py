"""Gerenciador de sessões usando o padrão GoF Singleton.

Esta implementação é didática e mantém o estado apenas na memória deste processo.
Em produção, sessões compartilhadas entre múltiplos workers/servidores devem usar
armazenamento compartilhado (por exemplo, Redis ou um banco de dados).
"""

from __future__ import annotations

import secrets
import threading
import time
from typing import Any


TEMPO_INATIVIDADE_SEGUNDOS = 30 * 60


class GerenciadorSessao:
    """Mantém uma única instância por processo Python e gerencia sessões ativas."""

    _instancia: GerenciadorSessao | None = None
    _lock_instancia = threading.Lock()

    def __new__(cls) -> GerenciadorSessao:
        # A trava protege somente a criação da instância.
        with cls._lock_instancia:
            if cls._instancia is None:
                instancia = super().__new__(cls)
                instancia._sessoes = {}  # email -> token -> metadados da sessão
                instancia._lock_sessoes = threading.RLock()
                cls._instancia = instancia
        return cls._instancia

    @staticmethod
    def _normalizar_email(email: str) -> str:
        return email.strip().casefold()

    def criar_sessao(
        self,
        email: str,
        dispositivo: str = "dispositivo desconhecido",
    ) -> str:
        """Cria uma sessão com token criptograficamente aleatório.

        O prazo de inatividade é renovado quando validar_sessao() confirma uso
        válido da sessão. O token deve ser tratado como segredo pelo chamador.
        """
        email_normalizado = self._normalizar_email(email)
        if not email_normalizado:
            raise ValueError("O e-mail é obrigatório para criar uma sessão.")

        agora = time.time()
        token = secrets.token_urlsafe(32)
        with self._lock_sessoes:
            self._remover_expiradas_usuario(email_normalizado, agora)
            self._sessoes.setdefault(email_normalizado, {})[token] = {
                "criada_em": agora,
                "ultima_atividade": agora,
                "expira_em": agora + TEMPO_INATIVIDADE_SEGUNDOS,
                "dispositivo": dispositivo,
            }
        return token

    def validar_sessao(self, email: str, token: str, renovar: bool = True) -> bool:
        """Valida a sessão no servidor e, por padrão, renova o timeout de inatividade."""
        email_normalizado = self._normalizar_email(email)
        agora = time.time()

        with self._lock_sessoes:
            sessoes_usuario = self._sessoes.get(email_normalizado)
            if not sessoes_usuario:
                return False

            dados = sessoes_usuario.get(token)
            if dados is None:
                return False

            if dados["expira_em"] <= agora:
                del sessoes_usuario[token]
                if not sessoes_usuario:
                    self._sessoes.pop(email_normalizado, None)
                return False

            if renovar:
                dados["ultima_atividade"] = agora
                dados["expira_em"] = agora + TEMPO_INATIVIDADE_SEGUNDOS
            return True

    def sessoes_ativas(self, email: str) -> list[dict[str, Any]]:
        """Lista metadados não sensíveis das sessões ativas, sem expor tokens."""
        email_normalizado = self._normalizar_email(email)
        agora = time.time()
        with self._lock_sessoes:
            self._remover_expiradas_usuario(email_normalizado, agora)
            sessoes_usuario = self._sessoes.get(email_normalizado, {})
            return [
                {
                    "criada_em": dados["criada_em"],
                    "ultima_atividade": dados["ultima_atividade"],
                    "expira_em": dados["expira_em"],
                    "dispositivo": dados["dispositivo"],
                }
                for dados in sessoes_usuario.values()
            ]

    def dispositivos_conectados(self, email: str) -> list[dict[str, Any]]:
        """Mantido como nome descritivo para compatibilidade com o exemplo anterior.

        Retorna metadados das sessões, não os tokens nem uma identificação
        comprovada de dispositivos físicos.
        """
        return self.sessoes_ativas(email)

    def encerrar_sessao(self, email: str, token: str) -> bool:
        """Invalida uma única sessão (logout)."""
        email_normalizado = self._normalizar_email(email)
        with self._lock_sessoes:
            sessoes_usuario = self._sessoes.get(email_normalizado)
            if not sessoes_usuario or token not in sessoes_usuario:
                return False

            del sessoes_usuario[token]
            if not sessoes_usuario:
                self._sessoes.pop(email_normalizado, None)
            return True

    def encerrar_todas(self, email: str) -> None:
        """Invalida todas as sessões do usuário em relação a esta instância."""
        email_normalizado = self._normalizar_email(email)
        with self._lock_sessoes:
            self._sessoes.pop(email_normalizado, None)

    def _remover_expiradas_usuario(self, email: str, agora: float) -> None:
        """Método interno. Deve ser chamado com _lock_sessoes adquirido."""
        sessoes_usuario = self._sessoes.get(email)
        if not sessoes_usuario:
            return

        expiradas = [
            token
            for token, dados in sessoes_usuario.items()
            if dados["expira_em"] <= agora
        ]
        for token in expiradas:
            del sessoes_usuario[token]

        if not sessoes_usuario:
            self._sessoes.pop(email, None)


if __name__ == "__main__":
    # Demonstração manual do Singleton e das operações de sessão.
    gerenciador1 = GerenciadorSessao()
    gerenciador2 = GerenciadorSessao()

    print("=== Teste de instância Singleton ===")
    print(f"Gerenciador 1 (ID): {id(gerenciador1)}")
    print(f"Gerenciador 2 (ID): {id(gerenciador2)}")
    print(f"São a mesma instância? {'Sim' if gerenciador1 is gerenciador2 else 'Não'}")

    print("\n=== Teste de sessões ===")
    email_teste = "usuario@exemplo.com"
    token1 = gerenciador1.criar_sessao(email_teste, dispositivo="Navegador de teste")
    token2 = gerenciador2.criar_sessao(email_teste, dispositivo="Segundo dispositivo")
    print(f"Total de sessões ativas: {len(gerenciador1.sessoes_ativas(email_teste))}")
    print(f"Primeira sessão válida? {gerenciador2.validar_sessao(email_teste, token1)}")

    print("\nEncerrando uma sessão...")
    print(f"Logout realizado? {gerenciador2.encerrar_sessao(email_teste, token1)}")
    print(f"Sessões restantes: {len(gerenciador1.sessoes_ativas(email_teste))}")

    print("\nEncerrando todas as sessões...")
    gerenciador2.encerrar_todas(email_teste)
    print(f"Sessões restantes: {len(gerenciador1.sessoes_ativas(email_teste))}")
    print(f"Segunda sessão válida? {gerenciador1.validar_sessao(email_teste, token2)}")
