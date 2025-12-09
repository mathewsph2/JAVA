/* ---------------------------------------------------------------------------- */
// Relacionado ao saldo do investidor
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.sibov.model;

import java.math.BigDecimal;

public class Carteira {

    private int id;
    private int idInvestidor;
    private BigDecimal saldo;

    public Carteira() {
    }

    public Carteira(int id, int idInvestidor, BigDecimal saldo) {
        this.id = id;
        this.idInvestidor = idInvestidor;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public int getIdInvestidor() {
        return idInvestidor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
