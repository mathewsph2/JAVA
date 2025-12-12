/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.matheus.sibov.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CarteiraOperacoes {

    private long id;
    private int idUsuario;
    private int idAtivo;
    private TipoOperacao tipoOperacao;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private Timestamp dataOperacao;
    private String observacoes;
    private int idCorretora;
    private BigDecimal saldoApos;
    private BigDecimal valorOperacao;

    // Construtor vazio
    public CarteiraOperacoes() {}

    // Construtor completo
    public CarteiraOperacoes(long id, int idUsuario, int idAtivo, TipoOperacao tipoOperacao,
                             int quantidade, BigDecimal valorUnitario, BigDecimal valorTotal,
                             Timestamp dataOperacao, String observacoes, int idCorretora,
                             BigDecimal saldoApos, BigDecimal valorOperacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtivo = idAtivo;
        this.tipoOperacao = tipoOperacao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.dataOperacao = dataOperacao;
        this.observacoes = observacoes;
        this.idCorretora = idCorretora;
        this.saldoApos = saldoApos;
        this.valorOperacao = valorOperacao;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int idAtivo) {
        this.idAtivo = idAtivo;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Timestamp getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(Timestamp dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getIdCorretora() {
        return idCorretora;
    }

    public void setIdCorretora(int idCorretora) {
        this.idCorretora = idCorretora;
    }

    public BigDecimal getSaldoApos() {
        return saldoApos;
    }

    public void setSaldoApos(BigDecimal saldoApos) {
        this.saldoApos = saldoApos;
    }

    public BigDecimal getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(BigDecimal valorOperacao) {
        this.valorOperacao = valorOperacao;
    }
}
