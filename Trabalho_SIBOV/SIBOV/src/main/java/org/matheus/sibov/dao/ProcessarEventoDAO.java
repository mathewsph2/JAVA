/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.matheus.sibov.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.matheus.ConnectionFactory.ConnectionFactory;
import org.matheus.sibov.model.Carteira;

/**
 *
 * @author mathe
 */
public class ProcessarEventoDAO {
    
    
    
    
    // Listar todos os eventos de proventos para processamento
    public List<Object[]> listarEventosParaProcessamento() {
        List<Object[]> lista = new ArrayList<>();
        
        String sql = "SELECT p.id AS id_operacao, a.ticker, a.tipo AS tipo_ativo, a.segmento, " +
             "p.tipo AS tipo_provento, p.valor_por_cota, p.data_com, p.data_ex, " +
             "p.data_pagamento, p.status, ca.quantidade_total, " +
             "(ca.quantidade_total * p.valor_por_cota) AS valor_total_estimado " +
             "FROM proventos p " +
             "JOIN ativo a ON p.id_ativo = a.id " +
             "JOIN carteira_ativos ca ON ca.id_ativo = a.id " +
             "WHERE p.status = 'AGENDADO' " +   // <-- aqui o filtro
             "ORDER BY p.data_pagamento DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] linha = new Object[12];
                linha[0] = rs.getInt("id_operacao");
                linha[1] = rs.getString("ticker");
                linha[2] = rs.getString("tipo_ativo");
                linha[3] = rs.getString("segmento");
                linha[4] = rs.getString("tipo_provento");
                linha[5] = rs.getBigDecimal("valor_por_cota");
                linha[6] = rs.getDate("data_com");
                linha[7] = rs.getDate("data_ex");
                linha[8] = rs.getDate("data_pagamento");
                linha[9] = rs.getString("status");
                linha[10] = rs.getInt("quantidade_total");
                linha[11] = rs.getBigDecimal("valor_total_estimado");

                lista.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
        }

        return lista;
    }
    
    
    
    
    // Listar todos os eventos de proventos para processamento
    public List<Object[]> listarEventosProcessados() {
        List<Object[]> lista = new ArrayList<>();
        
        String sql = "SELECT p.id AS id_operacao, a.ticker, a.tipo AS tipo_ativo, a.segmento, " +
             "p.tipo AS tipo_provento, p.valor_por_cota, p.data_com, p.data_ex, " +
             "p.data_pagamento, p.status, ca.quantidade_total, " +
             "(ca.quantidade_total * p.valor_por_cota) AS valor_total_estimado " +
             "FROM proventos p " +
             "JOIN ativo a ON p.id_ativo = a.id " +
             "JOIN carteira_ativos ca ON ca.id_ativo = a.id " +
             "WHERE p.status = 'PAGO' " +   
             "ORDER BY p.data_pagamento DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] linha = new Object[12];
                linha[0] = rs.getInt("id_operacao");
                linha[1] = rs.getString("ticker");
                linha[2] = rs.getString("tipo_ativo");
                linha[3] = rs.getString("segmento");
                linha[4] = rs.getString("tipo_provento");
                linha[5] = rs.getBigDecimal("valor_por_cota");
                linha[6] = rs.getDate("data_com");
                linha[7] = rs.getDate("data_ex");
                linha[8] = rs.getDate("data_pagamento");
                linha[9] = rs.getString("status");
                linha[10] = rs.getInt("quantidade_total");
                linha[11] = rs.getBigDecimal("valor_total_estimado");

                lista.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
        }

        return lista;
    }
    
    
    
    
     // Processar Eventos: Mudar O STATUS de AGENDADO para PAGO:
    public void processarEvento(int idEvento) {
    String sql = "UPDATE proventos SET status = 'PAGO' WHERE id = ?";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idEvento);
        stmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("Erro ao processar evento: " + e.getMessage());
    }
}

    
    
    
     // Processar Eventos e mudar saldo
    public void processarEventoComCredito(int idEvento) {
    String sql = "SELECT p.id_ativo, p.valor_por_cota, p.data_pagamento, ca.id_usuario, ca.quantidade_total " +
                 "FROM proventos p " +
                 "JOIN carteira_ativos ca ON ca.id_ativo = p.id_ativo " +
                 "WHERE p.id = ?";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idEvento);
        ResultSet rs = stmt.executeQuery();

        CarteiraDAO carteiraDAO = new CarteiraDAO();

        while (rs.next()) {
            int idAtivo = rs.getInt("id_ativo");
            BigDecimal valorPorCota = rs.getBigDecimal("valor_por_cota");
            Date dataPagamento = rs.getDate("data_pagamento");
            int idUsuario = rs.getInt("id_usuario");
            int quantidade = rs.getInt("quantidade_total");

            BigDecimal valorTotal = valorPorCota.multiply(BigDecimal.valueOf(quantidade));

            // Atualizar saldo na carteira
            Carteira carteira = carteiraDAO.buscarPorInvestidor(idUsuario);
            if (carteira != null) {
                BigDecimal novoSaldo = carteira.getSaldo().add(valorTotal);
                carteiraDAO.atualizarSaldo(idUsuario, novoSaldo);

                // Registrar no histórico
                String insertSql = "INSERT INTO carteira_operacoes (id_usuario, id_ativo, tipo_operacao, quantidade, valor_unitario, valor_total, data_operacao, observacoes, id_corretora, saldo_apos, valor_operacao) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, idUsuario);
                    insertStmt.setInt(2, idAtivo);
                    insertStmt.setString(3, "PROVENTO");
                    insertStmt.setBigDecimal(4, BigDecimal.valueOf(quantidade));
                    insertStmt.setBigDecimal(5, valorPorCota);
                    insertStmt.setBigDecimal(6, valorTotal);
                   insertStmt.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
                    insertStmt.setString(8, "Provento recebido automaticamente");
                    insertStmt.setNull(9, java.sql.Types.INTEGER); // id_corretora
                    insertStmt.setBigDecimal(10, novoSaldo);
                    insertStmt.setBigDecimal(11, valorTotal);

                    insertStmt.executeUpdate();
                }
            }
        }

        // Atualizar status do evento
        processarEvento(idEvento);

    } catch (SQLException e) {
        System.err.println("Erro ao processar evento com crédito: " + e.getMessage());
    }
}

    
    
    
    
    
}
