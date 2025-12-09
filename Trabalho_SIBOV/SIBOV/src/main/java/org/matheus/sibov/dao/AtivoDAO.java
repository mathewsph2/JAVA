/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */




package org.matheus.sibov.dao;

import org.matheus.ConnectionFactory.ConnectionFactory;
import org.matheus.sibov.model.Acao;
import org.matheus.sibov.model.Ativo;
import org.matheus.sibov.model.FII;
import org.matheus.sibov.model.TipoAtivo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;




public class AtivoDAO {

    
    
    
    /*---  Inserir ativo  ------------------------------------------------------*/
    public void salvar(Ativo ativo) {
        String sql = "INSERT INTO ativo (ticker, tipo, segmento, observacoes, valor_cota) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ativo.getTicker());
            stmt.setString(2, ativo.getTipo().name());
            stmt.setString(3, ativo.getSegmento());
            stmt.setString(4, ativo.getObservacoes());

            // Evita erro caso valorCota seja nulo
            if (ativo.getValorCota() != null) {
                stmt.setBigDecimal(5, ativo.getValorCota());
            } else {
                stmt.setNull(5, Types.DECIMAL);
            }

            stmt.executeUpdate();
            System.out.println("✅ Ativo cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar ativo: " + e.getMessage());
        }
    }
    /*--------------------------------------------------------------------------*/
    


    /*----  Editar ativo pelo Ticker  ------------------------------------------*/
    public void atualizar(Ativo ativo) {

        String sql = "UPDATE ativo SET ticker = ?, tipo = ?, segmento = ?, observacoes = ?, "
                + "valor_cota = ? WHERE ticker = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ativo.getTicker());
            stmt.setString(2, ativo.getTipo().name());
            stmt.setString(3, ativo.getSegmento());
            stmt.setString(4, ativo.getObservacoes());

            if (ativo.getValorCota() != null) {
                stmt.setBigDecimal(5, ativo.getValorCota());
            } else {
                stmt.setNull(5, Types.DECIMAL);
            }

            stmt.setString(6, ativo.getTicker());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("✅ Ativo atualizado com sucesso!");
            } else {
                System.out.println("⚠️ Nenhum ativo encontrado com o ticker informado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar ativo: " + e.getMessage());
        }
    }
    /*--------------------------------------------------------------------------*/
    


    /*---  Listar todos os ativos  ---------------------------------------------*/
    public List<Ativo> listarTodos() {

        String sql = "SELECT * FROM ativo";
        List<Ativo> ativos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String tipoStr = rs.getString("tipo");
                TipoAtivo tipo = TipoAtivo.valueOf(tipoStr);

                Ativo ativo;

                if (tipo == TipoAtivo.ACAO) {
                    ativo = new Acao();
                } else {
                    ativo = new FII();
                }

                ativo.setId(rs.getInt("id"));
                ativo.setTicker(rs.getString("ticker"));
                ativo.setTipo(tipo);
                ativo.setSegmento(rs.getString("segmento"));
                ativo.setObservacoes(rs.getString("observacoes"));
                ativo.setValorCota(rs.getBigDecimal("valor_cota"));

                ativos.add(ativo);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar ativos: " + e.getMessage());
        }

        return ativos;
    }
    /*--------------------------------------------------------------------------*/
    


    /*---  Deletar ativo por ID  -----------------------------------------------*/
    public void deletar(int id) {
        String sql = "DELETE FROM ativo WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Ativo deletado com sucesso!");
            } else {
                System.out.println("Nenhum ativo encontrado com o ID informado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar ativo: " + e.getMessage());
        }
    }
    /*--------------------------------------------------------------------------*/

    
    

    /*---  Deletar ativo por TICKER  -------------------------------------------*/
    public void deletarPorTicker(String ticker) {
        String sql = "DELETE FROM ativo WHERE ticker = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticker);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar ativo por ticker: " + e.getMessage(), e);
        }
    }
    /*--------------------------------------------------------------------------*/
    
    
    

    /*--- Buscar por ID  ------------------------------------------------------*/
    public Ativo buscarPorId(int id) {
        String sql = "SELECT * FROM ativo WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipoStr = rs.getString("tipo");
                    TipoAtivo tipo = (tipoStr == null) ? null : TipoAtivo.valueOf(tipoStr);
                    Ativo ativo = (tipo == TipoAtivo.ACAO) ? new Acao() : new FII();

                    ativo.setId(rs.getInt("id"));
                    ativo.setTicker(rs.getString("ticker"));
                    ativo.setTipo(tipo);
                    ativo.setSegmento(rs.getString("segmento"));
                    ativo.setObservacoes(rs.getString("observacoes"));
                    ativo.setValorCota(rs.getBigDecimal("valor_cota"));

                    return ativo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ativo por ID: " + e.getMessage());
        }
        return null;
    }
    /*--------------------------------------------------------------------------*/
    
    

    /*--- Buscar por Ticket  ---------------------------------------------------*/
    public Ativo buscarPorTicker(String ticker) {
        String sql = "SELECT * FROM ativo WHERE ticker = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticker);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    TipoAtivo tipo = TipoAtivo.valueOf(rs.getString("tipo"));
                    Ativo ativo = (tipo == TipoAtivo.ACAO) ? new Acao() : new FII();

                    ativo.setId(rs.getInt("id"));
                    ativo.setTicker(rs.getString("ticker"));
                    ativo.setTipo(tipo);
                    ativo.setSegmento(rs.getString("segmento"));
                    ativo.setObservacoes(rs.getString("observacoes"));
                    ativo.setValorCota(rs.getBigDecimal("valor_cota"));

                    return ativo;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar ativo por ticker: " + e.getMessage());
        }

        return null;
    }
    /*--------------------------------------------------------------------------*/
}
