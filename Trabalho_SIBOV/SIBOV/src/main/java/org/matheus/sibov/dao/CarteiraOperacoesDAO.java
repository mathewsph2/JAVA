/* ----------------------------------------------------------------------------
 * Extrato imutável da compra, venda, depósito e saque
 * Alinhado ao schema atual da tabela `carteira_operacoes`:
 *  - id (bigint, PK, auto_increment)
 *  - id_usuario (int, NOT NULL, FK usuarios.id)
 *  - id_ativo (int, NULL, FK ativo.id)
 *  - tipo_operacao (enum('DEPOSITO','SAQUE','COMPRA','VENDA','TRANSFERENCIA'))
 *  - quantidade (decimal(18,2), NULL)
 *  - valor_unitario (decimal(18,2), NULL)
 *  - valor_total (decimal(18,2), NULL)
 *  - data_operacao (timestamp, DEFAULT CURRENT_TIMESTAMP)
 *  - observacoes (varchar(300), NULL)
 *  - saldo_depois (decimal(18,2), NULL)
 * ---------------------------------------------------------------------------- */
package org.matheus.sibov.dao;

import org.matheus.ConnectionFactory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CarteiraOperacoesDAO {

    /* ------------------------------------------------------------------------
     * Registrar operação completa (COMPRA, VENDA, DEPÓSITO, SAQUE, TRANSFERÊNCIA)
     * - quantidade e valor_unitario podem ser nulos para depósito/saque
     * - id_ativo pode ser nulo para depósito/saque
     * - valor_total: se valorOperacao vier informado, usa ele; senão, calcula qty * unit
     * - saldo_antes/saldo_depois devem refletir o caixa antes e depois da operação
     * ------------------------------------------------------------------------ */
    public void registrarOperacao(int idUsuario, Integer idAtivo, String tipoOperacao,
            Integer quantidade, BigDecimal valorUnitario,
            BigDecimal valorOperacao, BigDecimal saldoApos,
            Integer idCorretora, String observacoes) {

        String sql = "INSERT INTO carteira_operacoes "
                + "(id_usuario, id_ativo, tipo_operacao, quantidade, valor_unitario, "
                + "valor_total, data_operacao, observacoes, id_corretora, saldo_apos, valor_operacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            if (idAtivo == null) {
                stmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(2, idAtivo);
            }

            stmt.setString(3, tipoOperacao);

            if (quantidade == null) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, quantidade);
            }

            if (valorUnitario == null) {
                stmt.setNull(5, java.sql.Types.DECIMAL);
            } else {
                stmt.setBigDecimal(5, valorUnitario);
            }

            BigDecimal valorTotal;
            if (valorOperacao != null) {
                valorTotal = valorOperacao;
            } else if (quantidade != null && valorUnitario != null) {
                valorTotal = valorUnitario.multiply(new BigDecimal(quantidade));
            } else {
                valorTotal = null;
            }

            if (valorTotal == null) {
                stmt.setNull(6, java.sql.Types.DECIMAL);
            } else {
                stmt.setBigDecimal(6, valorTotal);
            }

            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            if (observacoes == null || observacoes.isBlank()) {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(8, observacoes);
            }

            if (idCorretora == null) {
                stmt.setNull(9, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(9, idCorretora);
            }

            stmt.setBigDecimal(10, saldoApos);
            stmt.setBigDecimal(11, valorOperacao);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar operação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ------------------------------------------------------------------------
     * Listar operações (com usuário e ativo opcionais)
     * - LEFT JOIN para ativo pois id_ativo pode ser NULL (depósito/saque)
     * - Retorna linhas em array para fácil exibição na JTable
     * ------------------------------------------------------------------------ */
    public List<Object[]> listarOperacoes() {
        List<Object[]> lista = new ArrayList<>();

        String sql = "SELECT o.id, o.id_usuario, o.id_ativo, u.nome AS usuario, a.ticker AS ativo, c.nome AS corretora, "
                + "o.tipo_operacao, o.valor_operacao, o.quantidade, o.valor_unitario, "
                + "o.valor_total, o.data_operacao, o.observacoes, o.saldo_apos "
                + "FROM carteira_operacoes o "
                + "JOIN usuarios u ON o.id_usuario = u.id "
                + "LEFT JOIN ativo a ON o.id_ativo = a.id "
                + "LEFT JOIN corretoras c ON o.id_corretora = c.id "
                + "ORDER BY o.data_operacao DESC, o.id DESC";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] linha = new Object[14]; // agora com 14 posições
                linha[0] = rs.getLong("id");                    // ID da Operação
                linha[1] = rs.getInt("id_usuario");             // ID do Usuário (interno)
                linha[2] = rs.getInt("id_ativo");               // ID do Ativo (interno)
                linha[3] = rs.getString("usuario");             // Nome do Usuário
                linha[4] = rs.getString("ativo");               // Ticker do Ativo
                linha[5] = rs.getString("corretora");           // Nome da Corretora
                linha[6] = rs.getString("tipo_operacao");       // Tipo de Operação
                linha[7] = rs.getBigDecimal("valor_operacao");  // Valor da Operação
                linha[8] = rs.getInt("quantidade");             // Quantidade
                linha[9] = rs.getBigDecimal("valor_unitario");  // Valor Unitário
                linha[10] = rs.getBigDecimal("valor_total");     // Valor Total
                linha[11] = rs.getTimestamp("data_operacao");    // Data da Operação
                linha[12] = rs.getString("observacoes");         // Observações
                linha[13] = rs.getBigDecimal("saldo_apos");      // Saldo Após
                lista.add(linha);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar operações: " + e.getMessage());
        }

        return lista;
    }

    /* ------------------------------------------------------------------------
     * Listar operações por usuário (Para histórico individual)
     * ------------------------------------------------------------------------ */
    public List<Object[]> listarOperacoesPorUsuario(int idUsuario) {
        List<Object[]> lista = new ArrayList<>();

        final String sql = "SELECT "
                + "  o.id, "
                + "  u.nome AS usuario, "
                + "  a.ticker AS ativo, "
                + "  o.tipo_operacao, "
                + "  o.quantidade, "
                + "  o.valor_unitario, "
                + "  o.valor_total, "
                + "  o.valor_total AS valor_operacao, "
                + "  o.data_operacao, "
                + "  o.observacoes, "
                + "  o.saldo_antes, "
                + "  o.saldo_depois "
                + "FROM carteira_operacoes o "
                + "JOIN usuarios u ON o.id_usuario = u.id "
                + "LEFT JOIN ativo a ON o.id_ativo = a.id "
                + "WHERE o.id_usuario = ? "
                + "ORDER BY o.data_operacao DESC, o.id DESC";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] linha = new Object[12];
                    linha[0] = rs.getLong("id");
                    linha[1] = rs.getString("usuario");
                    linha[2] = rs.getString("ativo");
                    linha[3] = rs.getString("tipo_operacao");
                    linha[4] = rs.getBigDecimal("quantidade");
                    linha[5] = rs.getBigDecimal("valor_unitario");
                    linha[6] = rs.getBigDecimal("valor_total");
                    linha[7] = rs.getBigDecimal("valor_operacao");
                    linha[8] = rs.getTimestamp("data_operacao");
                    linha[9] = rs.getString("observacoes");
                    linha[10] = rs.getBigDecimal("saldo_antes");
                    linha[11] = rs.getBigDecimal("saldo_depois");
                    lista.add(linha);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar operações do usuário: " + e.getMessage());
        }

        return lista;
    }
    
    
    
    
    /* ------------------------------------------------------------------------
 * Listar ativos distintos por usuário -> para usar no combobox 
 * - Retorna apenas os tickers dos ativos que o usuário possui operações
 * - Usa LEFT JOIN pois id_ativo pode ser NULL (depósito/saque)
 * ------------------------------------------------------------------------ */
public List<String> listarAtivosPorUsuario(int idUsuario) {
    List<String> ativos = new ArrayList<>();

    final String sql = "SELECT DISTINCT a.ticker AS ativo "
            + "FROM carteira_operacoes o "
            + "LEFT JOIN ativo a ON o.id_ativo = a.id "
            + "WHERE o.id_usuario = ? AND a.ticker IS NOT NULL "
            + "ORDER BY a.ticker";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ativos.add(rs.getString("ativo"));
            }
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar ativos do usuário: " + e.getMessage());
    }

    return ativos;
}

    


/* ------------------------------------------------------------------------
 * Listar tipos de operação distintos por usuário para o combobox
 * ------------------------------------------------------------------------ */
public List<String> listarTiposOperacaoPorUsuario(int idUsuario) {
    List<String> tipos = new ArrayList<>();

    final String sql = "SELECT DISTINCT o.tipo_operacao "
            + "FROM carteira_operacoes o "
            + "WHERE o.id_usuario = ? "
            + "ORDER BY o.tipo_operacao";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tipos.add(rs.getString("tipo_operacao"));
            }
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar tipos de operação: " + e.getMessage());
    }

    return tipos;
}

    
}
