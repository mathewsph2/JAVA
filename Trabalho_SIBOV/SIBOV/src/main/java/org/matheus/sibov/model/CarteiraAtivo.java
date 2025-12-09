/* ---------------------------------------------------------------------------- */
// Relacionado aos ativos do investidor
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.sibov.model;

import java.math.BigDecimal;

public class CarteiraAtivo {

    private int id;
    private int idUsuario;
    private int idAtivo;
    private int quantidadeTotal;
    private BigDecimal precoMedio;
    private BigDecimal valorInvestido;
    private BigDecimal ultimoValorCota;

    public CarteiraAtivo(int id, int idUsuario, int idAtivo, int quantidadeTotal,
            BigDecimal precoMedio, BigDecimal valorInvestido, BigDecimal ultimoValorCota) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtivo = idAtivo;
        this.quantidadeTotal = quantidadeTotal;
        this.precoMedio = precoMedio;
        this.valorInvestido = valorInvestido;
        this.ultimoValorCota = ultimoValorCota;
    }

    public CarteiraAtivo(int idUsuario, int idAtivo, int quantidadeTotal,
            BigDecimal precoMedio, BigDecimal valorInvestido, BigDecimal ultimoValorCota) {
        this.idUsuario = idUsuario;
        this.idAtivo = idAtivo;
        this.quantidadeTotal = quantidadeTotal;
        this.precoMedio = precoMedio;
        this.valorInvestido = valorInvestido;
        this.ultimoValorCota = ultimoValorCota;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public BigDecimal getPrecoMedio() {
        return precoMedio;
    }

    public BigDecimal getValorInvestido() {
        return valorInvestido;
    }

    public BigDecimal getUltimoValorCota() {
        return ultimoValorCota;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public void setPrecoMedio(BigDecimal precoMedio) {
        this.precoMedio = precoMedio;
    }

    public void setValorInvestido(BigDecimal valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public void setUltimoValorCota(BigDecimal ultimoValorCota) {
        this.ultimoValorCota = ultimoValorCota;
    }
}
