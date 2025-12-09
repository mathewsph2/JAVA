/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.sibov.model;

import java.math.BigDecimal;

public class FII extends Ativo {

    public FII() {
        super();
        this.tipo = TipoAtivo.FII;
    }

    // Construtor sem valorCota (mantido para compatibilidade)
    public FII(String ticker, String segmento, String observacoes) {
        super(ticker, TipoAtivo.FII, segmento, observacoes);
    }

    // NOVO CONSTRUTOR com valorCota
    public FII(String ticker, String segmento, String observacoes, BigDecimal valorCota) {
        super(ticker, TipoAtivo.FII, segmento, observacoes, valorCota);
    }

    // Construtor completo com ID
    public FII(int id, String ticker, String segmento, String observacoes, BigDecimal valorCota) {
        super(id, ticker, TipoAtivo.FII, segmento, observacoes, valorCota);
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("FII: " + ticker + " | Segmento: " + segmento
                + " | Observações: " + observacoes + " | Valor Cota: " + valorCota);
    }
}
