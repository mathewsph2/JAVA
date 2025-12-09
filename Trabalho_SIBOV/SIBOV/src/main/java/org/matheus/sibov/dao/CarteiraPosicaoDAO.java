/* ---------------------------------------------------------------------------- */
// Acesso ao banco de dados - Inner Join entre o ativo e a carteita_ativos no BD
// Trás os dados da posição da carteira.
/* ---------------------------------------------------------------------------- */


package org.matheus.sibov.dao;

import org.matheus.ConnectionFactory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class CarteiraPosicaoDAO {

    public List<Object[]> buscarPosicaoCarteira(int idUsuario) {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT " +
                     "a.ticker, " +
                     "a.tipo, " +
                     "ca.quantidade_total, " +
                     "ca.preco_medio, " +
                     "ca.valor_investido, " +
                     "(ca.quantidade_total * a.valor_cota) as valor_mercado " +
                     "FROM carteira_ativos ca " +
                     "INNER JOIN ativo a ON ca.id_ativo = a.id " +
                     "WHERE ca.id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] linha = new Object[6];
                linha[0] = rs.getString("ticker");
                linha[1] = rs.getString("tipo");
                linha[2] = rs.getInt("quantidade_total");
                linha[3] = rs.getBigDecimal("preco_medio");
                linha[4] = rs.getBigDecimal("valor_investido");
                linha[5] = rs.getBigDecimal("valor_mercado");
                
                resultados.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar posição da carteira: " + e.getMessage());
        }

        return resultados;
    }
}