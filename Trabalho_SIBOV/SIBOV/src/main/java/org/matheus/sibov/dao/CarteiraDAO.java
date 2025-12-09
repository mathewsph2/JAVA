/* ---------------------------------------------------------------------------- */
// Acesso à Carteira do investidor 
// Trás o saldo do investidor 
/* ---------------------------------------------------------------------------- */
package org.matheus.sibov.dao;

import org.matheus.sibov.model.Carteira;
import org.matheus.ConnectionFactory.ConnectionFactory;

import java.sql.*;
import java.math.BigDecimal;

public class CarteiraDAO {

    /*--- Selecionar insvestidores pelo ID do investidor -----------------------*/
    public Carteira buscarPorInvestidor(int idInvestidor) {
        String sql = "SELECT * FROM carteira WHERE id_investidor = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInvestidor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Carteira(
                        rs.getInt("id"),
                        rs.getInt("id_investidor"),
                        rs.getBigDecimal("saldo")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*--------------------------------------------------------------------------*/
 
   
 /*--- Atualizar saldo do insvestidor pelo ID do investidor -----------------*/
    public boolean atualizarSaldo(int idInvestidor, BigDecimal novoSaldo) {
        String sql = "UPDATE carteira SET saldo = ? WHERE id_investidor = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, novoSaldo);
            stmt.setInt(2, idInvestidor);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    /*--------------------------------------------------------------------------*/
}
