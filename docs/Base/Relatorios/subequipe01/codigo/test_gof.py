"""Testes automatizados dos padrões Factory Method e Singleton.

Executar com:
    python -m unittest -v test_gof.py

Os testes usam apenas a biblioteca padrão unittest. O custo do PBKDF2 é
reduzido durante a suíte para acelerar os testes; isso NÃO altera a configuração
da implementação executada normalmente.
"""

from __future__ import annotations

import time
import unittest
from concurrent.futures import ThreadPoolExecutor
from unittest.mock import patch
from uuid import uuid4

import factory_method_autenticacao as auth
import singleton_sessao as sess


class TestFactoryMethod(unittest.TestCase):
    def setUp(self) -> None:
        # O número reduzido de iterações é exclusivo do ambiente de teste.
        # A implementação normal continua usando o valor de produção definido
        # em factory_method_autenticacao.py.
        self.pbkdf2_patcher = patch.object(auth, "ITERACOES_PBKDF2", 1_000)
        self.pbkdf2_patcher.start()
        self.addCleanup(self.pbkdf2_patcher.stop)

        self.banco = auth.BancoUsuarios()
        self.email = f"teste-{uuid4().hex}@example.com"
        self.senha = "Senha-Teste-123!"
        self.banco.cadastrar(self.email, self.senha, "Usuário de teste")
        self.canal = auth.CanalEmailSimulado()
        self.gerenciador = sess.GerenciadorSessao()

        self.addCleanup(self.gerenciador.encerrar_todas, self.email)
        self.addCleanup(self.gerenciador.encerrar_todas, "ana@gmail.com")
        self.addCleanup(self.gerenciador.encerrar_todas, "bruno@hotmail.com")
        self.addCleanup(self.gerenciador.encerrar_todas, "carla@icloud.com")

    def test_factory_method_cria_autenticadores_corretos(self) -> None:
        criadores = {
            "local": (auth.LoginLocal(self.banco), auth.AutenticadorLocal),
            "google": (auth.LoginGoogle(self.banco), auth.AutenticadorGoogle),
            "facebook": (auth.LoginFacebook(self.banco), auth.AutenticadorFacebook),
            "apple": (auth.LoginApple(self.banco), auth.AutenticadorApple),
            "codigo_email": (
                auth.LoginCodigoEmail(self.banco, self.canal),
                auth.AutenticadorCodigoEmail,
            ),
        }

        for nome, (criador, tipo_esperado) in criadores.items():
            with self.subTest(modalidade=nome):
                self.assertIsInstance(criador.criar_autenticador(), tipo_esperado)

    def test_login_local_valido_cria_sessao(self) -> None:
        servico = auth.LoginLocal(self.banco)
        resultado = servico.realizar_login(
            {"email": self.email, "senha": self.senha}
        )

        self.assertTrue(resultado.sucesso)
        self.assertIsNotNone(resultado.token_sessao)
        self.assertTrue(
            self.gerenciador.validar_sessao(
                self.email, resultado.token_sessao, renovar=False
            )
        )

    def test_email_inexistente_e_senha_errada_usam_mensagem_generica(self) -> None:
        autenticador = auth.AutenticadorLocal(self.banco)
        resultado_inexistente = autenticador.autenticar(
            {"email": "nao-existe@example.com", "senha": "qualquer"}
        )
        resultado_senha_errada = autenticador.autenticar(
            {"email": self.email, "senha": "senha-incorreta"}
        )

        self.assertFalse(resultado_inexistente.sucesso)
        self.assertFalse(resultado_senha_errada.sucesso)
        self.assertEqual(resultado_inexistente.mensagem, auth.MENSAGEM_GENERICA)
        self.assertEqual(resultado_senha_errada.mensagem, auth.MENSAGEM_GENERICA)
        self.assertEqual(resultado_inexistente.mensagem, resultado_senha_errada.mensagem)

    def test_bloqueia_apos_cinco_falhas_e_recusa_senha_correta(self) -> None:
        autenticador = auth.AutenticadorLocal(self.banco)

        for _ in range(auth.MAX_TENTATIVAS):
            resultado = autenticador.autenticar(
                {"email": self.email, "senha": "senha-incorreta"}
            )
            self.assertFalse(resultado.sucesso)

        registro = self.banco.falhas[self.email]
        self.assertGreater(registro["bloqueado_ate"], time.time())

        resultado_correto_durante_bloqueio = autenticador.autenticar(
            {"email": self.email, "senha": self.senha}
        )
        self.assertFalse(resultado_correto_durante_bloqueio.sucesso)
        self.assertEqual(
            resultado_correto_durante_bloqueio.mensagem,
            auth.MENSAGEM_GENERICA,
        )

    def test_login_social_simulado_aceita_token_valido(self) -> None:
        resultado = auth.LoginGoogle(self.banco).realizar_login(
            {"token": "tok-google-123"}
        )

        self.assertTrue(resultado.sucesso)
        self.assertEqual(resultado.email, "ana@gmail.com")
        self.assertIsNotNone(resultado.token_sessao)

    def test_login_social_simulado_recusa_token_invalido(self) -> None:
        resultado = auth.LoginFacebook(self.banco).realizar_login(
            {"token": "token-inexistente"}
        )

        self.assertFalse(resultado.sucesso)
        self.assertIsNone(resultado.token_sessao)

    def test_codigo_email_resposta_neutra_para_email_nao_cadastrado(self) -> None:
        autenticador = auth.AutenticadorCodigoEmail(self.banco, self.canal)
        resposta = autenticador.enviar_codigo("nao-existe@example.com")

        self.assertEqual(resposta, auth.MENSAGEM_CODIGO)
        self.assertIsNone(self.canal.ler_codigo_para_teste("nao-existe@example.com"))

    def test_codigo_correto_e_de_uso_unico_e_tentativa_errada_nao_o_consume(self) -> None:
        autenticador = auth.AutenticadorCodigoEmail(self.banco, self.canal)
        resposta = autenticador.enviar_codigo(self.email)
        self.assertEqual(resposta, auth.MENSAGEM_CODIGO)
        codigo = self.canal.ler_codigo_para_teste(self.email)
        self.assertIsNotNone(codigo)

        codigo_errado = "000000" if codigo != "000000" else "000001"
        resultado_errado = autenticador.autenticar(
            {"email": self.email, "codigo": codigo_errado}
        )
        self.assertFalse(resultado_errado.sucesso)

        resultado_correto = autenticador.autenticar(
            {"email": self.email, "codigo": codigo}
        )
        self.assertTrue(resultado_correto.sucesso)
        self.assertEqual(resultado_correto.email, self.email)

        resultado_reutilizado = autenticador.autenticar(
            {"email": self.email, "codigo": codigo}
        )
        self.assertFalse(resultado_reutilizado.sucesso)

    def test_codigo_expirado_e_recusado(self) -> None:
        autenticador = auth.AutenticadorCodigoEmail(self.banco, self.canal)
        with patch.object(auth, "VALIDADE_CODIGO_SEGUNDOS", -1):
            autenticador.enviar_codigo(self.email)
        codigo = self.canal.ler_codigo_para_teste(self.email)
        self.assertIsNotNone(codigo)

        resultado = autenticador.autenticar(
            {"email": self.email, "codigo": codigo}
        )
        self.assertFalse(resultado.sucesso)
        self.assertEqual(resultado.mensagem, "Código inválido ou expirado.")

    def test_limite_de_tentativas_remove_codigo(self) -> None:
        autenticador = auth.AutenticadorCodigoEmail(self.banco, self.canal)
        autenticador.enviar_codigo(self.email)
        codigo = self.canal.ler_codigo_para_teste(self.email)
        self.assertIsNotNone(codigo)
        codigo_errado = "000000" if codigo != "000000" else "000001"

        for _ in range(auth.MAX_TENTATIVAS_CODIGO):
            resultado = autenticador.autenticar(
                {"email": self.email, "codigo": codigo_errado}
            )
            self.assertFalse(resultado.sucesso)

        self.assertNotIn(self.email, self.banco.codigos)
        self.assertFalse(
            autenticador.autenticar({"email": self.email, "codigo": codigo}).sucesso
        )

    def test_apenas_uma_de_varias_tentativas_concorrentes_consume_codigo(self) -> None:
        autenticador = auth.AutenticadorCodigoEmail(self.banco, self.canal)
        autenticador.enviar_codigo(self.email)
        codigo = self.canal.ler_codigo_para_teste(self.email)
        self.assertIsNotNone(codigo)

        def consumir() -> bool:
            return autenticador.autenticar(
                {"email": self.email, "codigo": codigo}
            ).sucesso

        with ThreadPoolExecutor(max_workers=8) as executor:
            resultados = list(executor.map(lambda _: consumir(), range(8)))

        self.assertEqual(sum(resultados), 1)


class TestSingleton(unittest.TestCase):
    def setUp(self) -> None:
        self.gerenciador = sess.GerenciadorSessao()
        self.email = f"sessao-{uuid4().hex}@example.com"
        self.gerenciador.encerrar_todas(self.email)
        self.addCleanup(self.gerenciador.encerrar_todas, self.email)

    def test_duas_chamadas_retornam_mesma_instancia(self) -> None:
        self.assertIs(sess.GerenciadorSessao(), sess.GerenciadorSessao())

    def test_criar_validar_e_encerrar_sessao(self) -> None:
        token = self.gerenciador.criar_sessao(self.email)
        self.assertTrue(self.gerenciador.validar_sessao(self.email, token, renovar=False))
        self.assertTrue(self.gerenciador.encerrar_sessao(self.email, token))
        self.assertFalse(self.gerenciador.validar_sessao(self.email, token))

    def test_encerrar_todas_remove_sessoes_do_usuario(self) -> None:
        token1 = self.gerenciador.criar_sessao(self.email)
        token2 = self.gerenciador.criar_sessao(self.email)
        self.assertEqual(len(self.gerenciador.sessoes_ativas(self.email)), 2)

        self.gerenciador.encerrar_todas(self.email)

        self.assertEqual(self.gerenciador.sessoes_ativas(self.email), [])
        self.assertFalse(self.gerenciador.validar_sessao(self.email, token1))
        self.assertFalse(self.gerenciador.validar_sessao(self.email, token2))

    def test_sessoes_ativas_nao_expoe_tokens(self) -> None:
        token = self.gerenciador.criar_sessao(self.email, dispositivo="Navegador de teste")
        metadados = self.gerenciador.sessoes_ativas(self.email)

        self.assertEqual(len(metadados), 1)
        self.assertNotIn("token", metadados[0])
        self.assertNotIn(token, repr(metadados))
        self.assertEqual(metadados[0]["dispositivo"], "Navegador de teste")

    def test_sessao_expira_apos_timeout_configurado(self) -> None:
        with patch.object(sess, "TEMPO_INATIVIDADE_SEGUNDOS", 0.02):
            token = self.gerenciador.criar_sessao(self.email)
            time.sleep(0.05)
            self.assertFalse(
                self.gerenciador.validar_sessao(self.email, token, renovar=False)
            )

    def test_criacao_concorrente_gera_tokens_distintos_e_consistentes(self) -> None:
        quantidade = 40
        with ThreadPoolExecutor(max_workers=8) as executor:
            tokens = list(
                executor.map(
                    lambda _: self.gerenciador.criar_sessao(self.email),
                    range(quantidade),
                )
            )

        self.assertEqual(len(tokens), quantidade)
        self.assertEqual(len(set(tokens)), quantidade)
        self.assertEqual(len(self.gerenciador.sessoes_ativas(self.email)), quantidade)

    def test_email_vazio_nao_cria_sessao(self) -> None:
        with self.assertRaises(ValueError):
            self.gerenciador.criar_sessao("   ")


if __name__ == "__main__":
    unittest.main(verbosity=2)
