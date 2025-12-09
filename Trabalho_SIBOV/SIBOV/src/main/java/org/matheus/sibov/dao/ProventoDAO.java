/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package org.matheus.sibov.dao;

import org.matheus.ConnectionFactory.ConnectionFactory;
import org.matheus.sibov.model.Provento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProventoDAO {

    public void salvar(Provento provento) {
        String sql = "INSERT INTO proventos (id_ativo, tipo, valor_por_cota, data_com, data_ex, data_pagamento, status, observacoes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, provento.getIdAtivo());
            stmt.setString(2, provento.getTipo().name());
            stmt.setBigDecimal(3, provento.getValorPorCota());
            stmt.setDate(4, java.sql.Date.valueOf(provento.getDataCom()));
            stmt.setDate(5, java.sql.Date.valueOf(provento.getDataEx()));
            stmt.setDate(6, java.sql.Date.valueOf(provento.getDataPagamento()));
            stmt.setString(7, provento.getStatus().name());
            stmt.setString(8, provento.getObservacoes());

            stmt.executeUpdate();
            System.out.println("✅ Provento cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar provento: " + e.getMessage());
        }
    }
    
    
    
/* ---------------------------------------------------------------------------- */
// Criado esse método para trazer na tela VisaoAdm Proventos, 
// na aba "Listar ativos com proventos" para trazer os proventos cadastrados,
// trazendo tcket e valor_cota da tabela ATIVOS e demais campos da tabela PROVENTOS
/* ---------------------------------------------------------------------------- */
    
    public List<Object[]> listarAtivosComProventos() {
    List<Object[]> lista = new ArrayList<>();
    String sql = "SELECT a.ticker, a.valor_cota, p.tipo, p.valor_por_cota, p.data_pagamento, p.status " +
                 "FROM ativo a JOIN proventos p ON a.id = p.id_ativo";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Object[] linha = new Object[6];
            linha[0] = rs.getString("ticker");
            linha[1] = rs.getBigDecimal("valor_cota");
            linha[2] = rs.getString("tipo");
            linha[3] = rs.getBigDecimal("valor_por_cota");
            linha[4] = rs.getDate("data_pagamento");
            linha[5] = rs.getString("status");
            lista.add(linha);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao listar ativos com proventos: " + e.getMessage());
    }

    return lista;
}
    
    
    
    
    /* ---------------------------------------------------------------------------- */
// Fazendo Join entre proventos, ativos e Carteira ativos  
// para trazer o relatório de proventos pendentes e pagos
// para o usuario. Nesse caso , apenas os com status de AGENDADO. 
/* ---------------------------------------------------------------------------- */
    
    
   public List<Object[]> buscarProventosAgendadosPorUsuario(int idUsuario) {
    List<Object[]> lista = new ArrayList<>();
    String sql = "SELECT a.id AS id_ativo, a.ticker, a.tipo, a.valor_cota, " +
                 "ca.quantidade_total, ca.valor_investido, " +
                 "p.tipo AS tipo_provento, p.valor_por_cota, p.data_pagamento, p.status " +
                 "FROM proventos p " +
                 "JOIN ativo a ON a.id = p.id_ativo " +
                 "JOIN carteira_ativos ca ON ca.id_ativo = a.id " +
                 "WHERE p.status = 'AGENDADO' AND ca.id_usuario = ? " +
                 "ORDER BY p.data_pagamento ASC";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Object[] linha = new Object[10];
            linha[0] = rs.getInt("id_ativo");
            linha[1] = rs.getString("ticker");
            linha[2] = rs.getString("tipo");
            linha[3] = rs.getBigDecimal("valor_cota");
            linha[4] = rs.getInt("quantidade_total");
            linha[5] = rs.getBigDecimal("valor_investido");
            linha[6] = rs.getString("tipo_provento");
            linha[7] = rs.getBigDecimal("valor_por_cota");
            linha[8] = rs.getDate("data_pagamento");
            linha[9] = rs.getString("status");

            lista.add(linha);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao buscar proventos agendados: " + e.getMessage());
    }

    return lista;
}
   
   
   
   
   
    /* ---------------------------------------------------------------------------- */
// Fazendo Join entre proventos, ativos e Carteira ativos  
// para trazer o relatório de proventos pendentes e pagos
// para o usuario. Nesse caso , apenas os com status de PAGO. 
/* ---------------------------------------------------------------------------- */
    
    
   public List<Object[]> buscarProventosPagosPorUsuario(int idUsuario) {
    List<Object[]> lista = new ArrayList<>();
    String sql = "SELECT a.id AS id_ativo, a.ticker, a.tipo, a.valor_cota, " +
                 "ca.quantidade_total, ca.valor_investido, " +
                 "p.tipo AS tipo_provento, p.valor_por_cota, p.data_pagamento, p.status " +
                 "FROM proventos p " +
                 "JOIN ativo a ON a.id = p.id_ativo " +
                 "JOIN carteira_ativos ca ON ca.id_ativo = a.id " +
                 "WHERE p.status = 'PAGO' AND ca.id_usuario = ? " +
                 "ORDER BY p.data_pagamento ASC";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Object[] linha = new Object[10];
            linha[0] = rs.getInt("id_ativo");
            linha[1] = rs.getString("ticker");
            linha[2] = rs.getString("tipo");
            linha[3] = rs.getBigDecimal("valor_cota");
            linha[4] = rs.getInt("quantidade_total");
            linha[5] = rs.getBigDecimal("valor_investido");
            linha[6] = rs.getString("tipo_provento");
            linha[7] = rs.getBigDecimal("valor_por_cota");
            linha[8] = rs.getDate("data_pagamento");
            linha[9] = rs.getString("status");

            lista.add(linha);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao buscar proventos agendados: " + e.getMessage());
    }

    return lista;
}

   
   
   /* ------------------------------------------------------------------------
 * Listar tickers distintos de ativos com proventos PAGOS para um usuário
   
   Usando em VisaoInvestidorProventosAgendados.java no combobox
   Usando em VisaoInvestidorProventosPagos.java
   
 * ------------------------------------------------------------------------ */
public List<String> listarTickersProventosPagosPorUsuario(int idUsuario) {
    List<String> tickers = new ArrayList<>();
    String sql = "SELECT DISTINCT a.ticker " +
                 "FROM proventos p " +
                 "JOIN ativo a ON a.id = p.id_ativo " +
                 "JOIN carteira_ativos ca ON ca.id_ativo = a.id " +
                 "WHERE p.status = 'PAGO' AND ca.id_usuario = ? " +
                 "ORDER BY a.ticker";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            tickers.add(rs.getString("ticker"));
        }

    } catch (SQLException e) {
        System.err.println("Erro ao listar tickers de proventos pagos: " + e.getMessage());
    }

    return tickers;
}




/* ------------------------------------------------------------------------
 * Listar tickers distintos de ativos com proventos AGENDADOS para um usuário

     Usando em VisaoInvestidorProventosAgendados.java no combobox
   Usando em VisaoInvestidorProventosPagos.java

 * ------------------------------------------------------------------------ */
public List<String> listarTickersProventosAgendadosPorUsuario(int idUsuario) {
    List<String> tickers = new ArrayList<>();
    String sql = "SELECT DISTINCT a.ticker " +
                 "FROM proventos p " +
                 "JOIN ativo a ON a.id = p.id_ativo " +
                 "JOIN carteira_ativos ca ON ca.id_ativo = a.id " +
                 "WHERE p.status = 'AGENDADO' AND ca.id_usuario = ? " +
                 "ORDER BY a.ticker";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            tickers.add(rs.getString("ticker"));
        }

    } catch (SQLException e) {
        System.err.println("Erro ao listar tickers de proventos Agendados: " + e.getMessage());
    }

    return tickers;
}

    
    

    
    
}
