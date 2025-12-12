/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* ---------------------------------------------------------------------------- */
// Representa a entidade corretora no sistema.
//
/* ----------------------------------------------------------------------------*/

package org.matheus.sibov.model;

import java.math.BigDecimal;

public class Corretora {
    private int id;
    private String nome;
    private BigDecimal taxaFixa;
    private BigDecimal taxaPercentual;
    private String observacoes;

    // Construtores
    public Corretora() {}

    public Corretora(int id, String nome, BigDecimal taxaFixa, BigDecimal taxaPercentual, String observacoes) {
        this.id = id;
        this.nome = nome;
        this.taxaFixa = taxaFixa;
        this.taxaPercentual = taxaPercentual;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getTaxaFixa() { return taxaFixa; }
    public void setTaxaFixa(BigDecimal taxaFixa) { this.taxaFixa = taxaFixa; }

    public BigDecimal getTaxaPercentual() { return taxaPercentual; }
    public void setTaxaPercentual(BigDecimal taxaPercentual) { this.taxaPercentual = taxaPercentual; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    @Override
    public String toString() {
        return "Corretora{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", taxaFixa=" + taxaFixa +
                ", taxaPercentual=" + taxaPercentual +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}
