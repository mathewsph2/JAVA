/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/* ---------------------------------------------------------------------------- */
// 
// DATACOM - último dia com direito
// DATAEX - primeiro dia sem direito
/* ---------------------------------------------------------------------------- */


package org.matheus.sibov.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Provento {

    private int id;
    private int idAtivo;
    private TipoProvento tipo;
    private BigDecimal valorPorCota;
    private LocalDate dataCom;
    private LocalDate dataEx;
    private LocalDate dataPagamento;
    private StatusProvento status;
    private String observacoes;

    // Construtor vazio
    public Provento() {}

    // Construtor completo
    public Provento(int id, int idAtivo, TipoProvento tipo, BigDecimal valorPorCota,
                    LocalDate dataCom, LocalDate dataEx, LocalDate dataPagamento,
                    StatusProvento status, String observacoes) {
        this.id = id;
        this.idAtivo = idAtivo;
        this.tipo = tipo;
        this.valorPorCota = valorPorCota;
        this.dataCom = dataCom;
        this.dataEx = dataEx;
        this.dataPagamento = dataPagamento;
        this.status = status;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int idAtivo) {
        this.idAtivo = idAtivo;
    }

    public TipoProvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoProvento tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorPorCota() {
        return valorPorCota;
    }

    public void setValorPorCota(BigDecimal valorPorCota) {
        this.valorPorCota = valorPorCota;
    }

    public LocalDate getDataCom() {
        return dataCom;
    }

    public void setDataCom(LocalDate dataCom) {
        this.dataCom = dataCom;
    }

    public LocalDate getDataEx() {
        return dataEx;
    }

    public void setDataEx(LocalDate dataEx) {
        this.dataEx = dataEx;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public StatusProvento getStatus() {
        return status;
    }

    public void setStatus(StatusProvento status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
