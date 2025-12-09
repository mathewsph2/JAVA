/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */

package org.matheus.sibov.model;

import java.math.BigDecimal;

public abstract class Ativo {
    protected int id;
    protected String ticker;
    protected TipoAtivo tipo;
    protected String segmento;
    protected String observacoes;
    protected BigDecimal valorCota; 

    // Construtor vazio
    public Ativo() {}

    // Construtor completo (incluindo valorCota)
    public Ativo(int id, String ticker, TipoAtivo tipo, String segmento, String observacoes, BigDecimal valorCota) {
        this.id = id;
        this.ticker = ticker;
        this.tipo = tipo;
        this.segmento = segmento;
        this.observacoes = observacoes;
        this.valorCota = valorCota;
    }

    // Construtor sem ID (para inserções no banco)
    public Ativo(String ticker, TipoAtivo tipo, String segmento, String observacoes, BigDecimal valorCota) {
        this.ticker = ticker;
        this.tipo = tipo;
        this.segmento = segmento;
        this.observacoes = observacoes;
        this.valorCota = valorCota;
    }

    // Construtor sem valorCota (opcional)
    public Ativo(String ticker, TipoAtivo tipo, String segmento, String observacoes) {
        this.ticker = ticker;
        this.tipo = tipo;
        this.segmento = segmento;
        this.observacoes = observacoes;
        this.valorCota = null; // valor indefinido
    }

    // Getters
    public int getId() { return id; }
    public String getTicker() { return ticker; }
    public TipoAtivo getTipo() { return tipo; }
    public String getSegmento() { return segmento; }
    public String getObservacoes() { return observacoes; }
    public BigDecimal getValorCota() { return valorCota; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public void setTipo(TipoAtivo tipo) { this.tipo = tipo; }
    public void setSegmento(String segmento) { this.segmento = segmento; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public void setValorCota(BigDecimal valorCota) { this.valorCota = valorCota; }

    // Método obrigatório
    public abstract void exibirDetalhes();
}
