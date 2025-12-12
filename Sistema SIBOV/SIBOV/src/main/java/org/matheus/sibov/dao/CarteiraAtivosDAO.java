/* ---------------------------------------------------------------------------- */
// Acesso à Carteira do investidor 
// Trás os ativos do investidor 
/* ---------------------------------------------------------------------------- */



package org.matheus.sibov.dao;

import org.matheus.ConnectionFactory.ConnectionFactory;
import org.matheus.sibov.model.CarteiraAtivo;

import java.sql.*;
import java.math.BigDecimal;



public class CarteiraAtivosDAO {

    
    
    /*---  Buscar ativo específico da carteira do usuário  ---------------------*/
    public CarteiraAtivo buscar(int idUsuario, int idAtivo) {

        String sql = "SELECT * FROM carteira_ativos WHERE id_usuario = ? AND id_ativo = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idAtivo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new CarteiraAtivo(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_ativo"),
                        rs.getInt("quantidade_total"),
                        rs.getBigDecimal("preco_medio"),
                        rs.getBigDecimal("valor_investido"),
                        rs.getBigDecimal("ultimo_valor_cota")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ativo na carteira: " + e.getMessage());
        }

        return null;
    }
    /*--------------------------------------------------------------------------*/
    

    /*--- Inserir novo ativo na carteira  --------------------------------------*/ 
    public void inserir(CarteiraAtivo c) {

        String sql = "INSERT INTO carteira_ativos (id_usuario, id_ativo, quantidade_total, preco_medio, valor_investido, ultimo_valor_cota) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getIdUsuario());
            stmt.setInt(2, c.getIdAtivo());
            stmt.setInt(3, c.getQuantidadeTotal());
            stmt.setBigDecimal(4, c.getPrecoMedio());
            stmt.setBigDecimal(5, c.getValorInvestido());
            stmt.setBigDecimal(6, c.getUltimoValorCota());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir ativo na carteira: " + e.getMessage());
        }
    }

    
    
    /*--- Atualizar ativo já existente (compra complementar) -------------------*/ 
    public void atualizar(CarteiraAtivo c) {

        String sql = "UPDATE carteira_ativos "
                + "SET quantidade_total = ?, preco_medio = ?, valor_investido = ?, ultimo_valor_cota = ? "
                + "WHERE id_usuario = ? AND id_ativo = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getQuantidadeTotal());
            stmt.setBigDecimal(2, c.getPrecoMedio());
            stmt.setBigDecimal(3, c.getValorInvestido());
            stmt.setBigDecimal(4, c.getUltimoValorCota());
            stmt.setInt(5, c.getIdUsuario());
            stmt.setInt(6, c.getIdAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar ativo na carteira: " + e.getMessage());
        }
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    /*--- Remover ativo da carteira do usuário quando chegar em zero unidades --*/
public boolean deletar(int idUsuario, int idAtivo) {
    String sql = "DELETE FROM carteira_ativos WHERE id_usuario = ? AND id_ativo = ?";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        stmt.setInt(2, idAtivo);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Erro ao deletar ativo da carteira: " + e.getMessage());
    }

    return false;
}
   /*--------------------------------------------------------------------------*/
    
    
}
