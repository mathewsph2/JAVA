/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */

package org.matheus.sibov.model;

import java.math.BigDecimal;

public class Acao extends Ativo {
    public Acao() {
        super();
        this.tipo = TipoAtivo.ACAO;
    }

    // Construtor sem valorCota (mantido para compatibilidade)
    public Acao(String ticker, String segmento, String observacoes) {
        super(ticker, TipoAtivo.ACAO, segmento, observacoes);
    }

    // NOVO CONSTRUTOR com valorCota
    public Acao(String ticker, String segmento, String observacoes, BigDecimal valorCota) {
        super(ticker, TipoAtivo.ACAO, segmento, observacoes, valorCota);
    }

    // Construtor completo com ID
    public Acao(int id, String ticker, String segmento, String observacoes, BigDecimal valorCota) {
        super(id, ticker, TipoAtivo.ACAO, segmento, observacoes, valorCota);
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Ação: " + ticker + " | Segmento: " + segmento + 
                         " | Observações: " + observacoes + " | Valor Cota: " + valorCota);
    }
}