/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.matheus.sibov.service;

import java.math.BigDecimal;
import org.matheus.sibov.model.Corretora;

public class CorretoraService {

    public void validar(Corretora c) throws IllegalArgumentException {
        if (c.getNome() == null || c.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da corretora é obrigatório.");
        }

        if (c.getTaxaFixa().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa fixa não pode ser negativa.");
        }

        if (c.getTaxaPercentual().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa percentual não pode ser negativa.");
        }
    }
}
