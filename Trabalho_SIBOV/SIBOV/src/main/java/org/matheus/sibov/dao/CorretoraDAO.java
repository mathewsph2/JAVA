/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.matheus.sibov.dao;



import org.matheus.ConnectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import org.matheus.sibov.model.Corretora;

public class CorretoraDAO {

    // Inserir nova corretora
    public void inserir(Corretora c) {
        String sql = "INSERT INTO corretoras (nome, taxa_fixa, taxa_percentual, observacoes) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNome());
            stmt.setBigDecimal(2, c.getTaxaFixa());
            stmt.setBigDecimal(3, c.getTaxaPercentual());
            stmt.setString(4, c.getObservacoes());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir corretora: " + e.getMessage());
        }
    }

    // Listar todas as corretoras
    public List<Corretora> listarTodas() {
        List<Corretora> lista = new ArrayList<>();
        String sql = "SELECT * FROM corretoras";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Corretora c = new Corretora();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTaxaFixa(rs.getBigDecimal("taxa_fixa"));
                c.setTaxaPercentual(rs.getBigDecimal("taxa_percentual"));
                c.setObservacoes(rs.getString("observacoes"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar corretoras: " + e.getMessage());
        }
        return lista;
    }

    // Buscar corretora por ID
    public Corretora buscarPorId(int id) {
        String sql = "SELECT * FROM corretoras WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Corretora(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getBigDecimal("taxa_fixa"),
                        rs.getBigDecimal("taxa_percentual"),
                        rs.getString("observacoes")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar corretora: " + e.getMessage());
        }
        return null;
    }

    // Atualizar corretora
    public void atualizar(Corretora c) {
        String sql = "UPDATE corretoras SET nome=?, taxa_fixa=?, taxa_percentual=?, observacoes=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNome());
            stmt.setBigDecimal(2, c.getTaxaFixa());
            stmt.setBigDecimal(3, c.getTaxaPercentual());
            stmt.setString(4, c.getObservacoes());
            stmt.setInt(5, c.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar corretora: " + e.getMessage());
        }
    }

    // Excluir corretora
    public void excluir(int id) {
        String sql = "DELETE FROM corretoras WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir corretora: " + e.getMessage());
        }
    }
    
    
    
    // Buscar corretora por nome - Para o combo box da funcao extra
public Corretora buscarPorNome(String nome) {
    String sql = "SELECT * FROM corretoras WHERE nome = ?";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nome);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Corretora c = new Corretora();
            c.setId(rs.getInt("id"));
            c.setNome(rs.getString("nome"));
            c.setTaxaFixa(rs.getBigDecimal("taxa_fixa"));
            c.setTaxaPercentual(rs.getBigDecimal("taxa_percentual"));
            c.setObservacoes(rs.getString("observacoes"));
            return c;
        }
    } catch (SQLException e) {
        System.out.println("Erro ao buscar corretora por nome: " + e.getMessage());
    }
    return null;
}

    
    
}

