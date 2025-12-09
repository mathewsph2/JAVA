/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/* ---------------------------------------------------------------------------- */
// Problemas:
// - Se o Administrador estornar duas vezes o mesmo registro;
// - Se fizer o estorno do estorno;
/* ---------------------------------------------------------------------------- */


package org.matheus.sibov.service;

import org.matheus.sibov.dao.CarteiraOperacoesDAO;
import java.math.BigDecimal;

public class ServiceEstorno {

    private final CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();

    public void estornarOperacao(Object[] operacao) {
        // IDs internos vindos do listarOperacoes()
        int idUsuario = (Integer) operacao[1];
        Integer idAtivo = (Integer) operacao[2];

        // Campos visíveis
        String tipoOriginal = (String) operacao[6];
        BigDecimal valorOperacao = (BigDecimal) operacao[7];
        Integer quantidade = (Integer) operacao[8];
        BigDecimal valorUnitario = (BigDecimal) operacao[9];
        BigDecimal saldoApos = (BigDecimal) operacao[13];

        // Regras de inversão
        switch (tipoOriginal) {
            case "DEPOSITO":
                dao.registrarOperacao(idUsuario, null, "ESTORNO",
                        null, null, valorOperacao,
                        saldoApos.subtract(valorOperacao),
                        null, "Estorno de depósito");
                break;

            case "SAQUE":
                dao.registrarOperacao(idUsuario, null, "ESTORNO",
                        null, null, valorOperacao,
                        saldoApos.add(valorOperacao),
                        null, "Estorno de saque");
                break;

            case "COMPRA":
                dao.registrarOperacao(idUsuario, idAtivo, "ESTORNO",
                        quantidade, valorUnitario, valorOperacao,
                        saldoApos.add(valorOperacao),
                        null, "Estorno de compra");
                break;

            case "VENDA":
                dao.registrarOperacao(idUsuario, idAtivo, "ESTORNO",
                        quantidade, valorUnitario, valorOperacao,
                        saldoApos.subtract(valorOperacao),
                        null, "Estorno de venda");
                break;

            default:
                throw new IllegalArgumentException("Tipo de operação não suportado para estorno: " + tipoOriginal);
        }
    }
}
