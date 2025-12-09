/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.sibov.service;

import org.matheus.sibov.dao.UsuarioDAO;

public class ContaService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /* --- Deposito----------------------------------------------------------- */
    public boolean depositar(int idConta, double valor) {

        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        return usuarioDAO.depositar(idConta, valor);
    }

    /* ------------------------------------------------------------------------ */

 /*--- Saque (somente da conta do usuário logado) ---------------------------*/
    public boolean sacar(int idUsuarioLogado, int idContaDestino, double valor) {

        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }

        // impede saque de outras contas
        if (idUsuarioLogado != idContaDestino) {
            throw new SecurityException("Você só pode sacar da sua própria conta.");
        }

        return usuarioDAO.sacar(idContaDestino, valor);
    }

    /*--------------------------------------------------------------------------*/

    // Consultar saldo
    /*--- Consulta de saldo ----------------------------------------------------*/
    public double consultarSaldo(int idUsuario) {
        return usuarioDAO.buscarSaldoInvestidor(idUsuario);
    }
    /*--------------------------------------------------------------------------*/
}
