/* ---------------------------------------------------------------------------- */
// Operaçãoes relacionadas ao usuário: Insert, updade, delete, select 
//
// 
//Usuários com perfil INVESTIDOR são criados automaticamente com uma carteira.
// Por isso, ao tentar excluir um investidor que possui registros vinculados 
// na tabela 'carteira', o banco lança uma exceção de integridade referencial 
// (SQLIntegrityConstraintViolationException) em 06 e 07.
//
// 
/* ---------------------------------------------------------------------------- */



package org.matheus.sibov.dao;

import org.matheus.perfis.*;
import org.matheus.ConnectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.matheus.sibov.view.admin.VisaoAdministrador;
import org.matheus.sibov.view.investidor.VisaoInvestidor;



public class UsuarioDAO {

    
    
    
    /* =========================================================================
      MÉTODO AUXILIAR PARA LOG DE ERROS TÉCNICOS
      (DAO NÃO MOSTRA MSG PRO USUÁRIO; SOMENTE LOGA)
      ========================================================================= */
    private void logErro(String acao, Exception e) {
        System.err.println("\n ERRO: " + acao);
        System.err.println("Mensagem: " + e.getMessage());
        e.printStackTrace();
    }

    
    
    /* 01) --- Inserir Usuários -----------------------------------------------------*/
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, cpf, perfil) VALUES (?, ?, MD5(?), ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCpf());
            stmt.setString(5, usuario.getPerfil().name());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                usuario.setId(idGerado);
                if (usuario instanceof Investidor) {
                    criarCarteiraParaInvestidor(idGerado, conn);
                }
            }

        } catch (SQLException e) {
            logErro("Erro ao salvar usuário", e);
            throw new RuntimeException("Não foi possível salvar o usuário.");
        }
    }
    /*--------------------------------------------------------------------------*/
    
    

    /* 02)--- Criação automática da carteira do investidor quando o usuário é criado ---*/
    private void criarCarteiraParaInvestidor(int idInvestidor, Connection conn) {
        String sql = "INSERT INTO carteira (id_investidor, saldo) VALUES (?, 0.00)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInvestidor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logErro("Erro ao criar carteira para investidor", e);
            throw new RuntimeException("Não foi possível criar a carteira do investidor.");
        }
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    /* 03) --- Listar todos os usuários ---------------------------------------------*/
    public ArrayList<Usuario> buscarTodos() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario;
                String perfil = rs.getString("perfil");
                if ("ADMINISTRADOR".equalsIgnoreCase(perfil)) {
                    usuario = new Administrador();
                } else {
                    usuario = new Investidor();
                }

                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setPerfil(Enum.valueOf(Perfil.class, perfil));

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            logErro("Erro ao buscar usuários", e);
            throw new RuntimeException("Não foi possível carregar a lista de usuários.");
        }
        return usuarios;
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    /* 04) --- Listar por CPF -------------------------------------------------------*/
    public Usuario buscarPorCpf(String cpf) {

        String sql = "SELECT * FROM usuarios WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario usuario;

                String perfil = rs.getString("perfil");

                if ("ADMINISTRADOR".equalsIgnoreCase(perfil)) {
                    usuario = new Administrador();
                } else {
                    usuario = new Investidor();
                }

                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setPerfil(Enum.valueOf(Perfil.class, perfil));

                return usuario;
            }

        } catch (SQLException e) {
            logErro("Erro ao buscar usuário por CPF", e);
            throw new RuntimeException("Não foi possível buscar o usuário.");
        }

        return null;
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    
    /* 05) --- Editar usuários pelo ID ----------------------------------------------*/
    public void editar(Usuario usuario) throws SQLException {

        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, perfil = ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil().name());
            stmt.setString(5, usuario.getCpf());

            stmt.executeUpdate();
        }
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    /* 06) --- Deletar usuários pelo ID ---------------------------------------------*/
public void deletar(int id) throws Exception {
    String sql = "DELETE FROM usuarios WHERE id = ?";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);

        try {
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Tratamento específico para chave estrangeira
            throw new Exception("Usuário não pode ser excluído porque possui vínculos ativos!");
        }

    } catch (SQLException e) {
        logErro("Erro ao deletar usuário", e);
        throw new Exception("Erro técnico ao excluir usuário: " + e.getMessage());
    }
}



    
/* 07) --- Deletar usuários pelo CPF --------------------------------------------*/
public void deletarPorCpf(String cpf) throws Exception {
    String sql = "DELETE FROM usuarios WHERE cpf = ?";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, cpf);

        try {
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Tratamento específico para chave estrangeira
            throw new Exception("Usuário não pode ser excluído porque possui vínculos ativos!");
        }

    } catch (SQLException e) {
        logErro("Erro ao deletar usuário", e);
        throw new Exception("Erro técnico ao excluir usuário: " + e.getMessage());
    }
}

    

    
    
    /* =========================================================================
   EFETUAR LOGIN
   Retorna um objeto Usuario quando email/senha estão corretos.
   Retorna null quando login é inválido.
   ============================================================================ */
    public Usuario efetuaLogin(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = MD5(?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                // Descobrir perfil e instanciar corretamente
                String perfil = rs.getString("perfil");
                Usuario usuario;

                if ("ADMINISTRADOR".equalsIgnoreCase(perfil)) {
                    usuario = new Administrador();
                } else {
                    usuario = new Investidor();
                }

                // Preencher dados
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha")); // já vem MD5 do BD
                usuario.setCpf(rs.getString("cpf"));
                usuario.setPerfil(Enum.valueOf(Perfil.class, perfil));

                // Retorna um objeto do tipo usuario 
                return usuario;
            }

        } catch (SQLException e) {
            logErro("Erro ao efetuar login", e);
            throw new RuntimeException("Não foi possível realizar o login.");
        }

        return null; // login inválido
    }
    /*--------------------------------------------------------------------------*/
    
    
    

    public double buscarSaldoInvestidor(int idInvestidor) {
        String sql = "SELECT saldo FROM carteira WHERE id_investidor = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInvestidor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("saldo");
            }

        } catch (SQLException e) {
            logErro("Erro ao buscar saldo do investidor", e);
        }

        return 0.0;
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    /*--- Depositar em uma carteira (Pelo ID do Investidor - O ID será o "número da conta do investidor") ---*/
    public boolean depositar(int idInvestidor, double valor) {
        String sql = "UPDATE carteira SET saldo = saldo + ? WHERE id_investidor = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, valor);
            stmt.setInt(2, idInvestidor);

            int linhas = stmt.executeUpdate();
            return linhas > 0; // depósito ok

        } catch (SQLException e) {
            logErro("Erro ao depositar na carteira", e);
            return false;
        }
    }
    /*--------------------------------------------------------------------------*/

    
    
    /*--- Sacar em uma carteira (Pelo ID do Usuário Logado)---------------------*/
    public boolean sacar(int idInvestidor, double valor) {
        String sql = "UPDATE carteira SET saldo = saldo - ? "
                + "WHERE id_investidor = ? AND saldo >= ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, valor);
            stmt.setInt(2, idInvestidor);
            stmt.setDouble(3, valor); // condição saldo >= valor

            int linhas = stmt.executeUpdate();
            return linhas > 0; // saque realizado com sucesso

        } catch (SQLException e) {
            logErro("Erro ao sacar da carteira", e);
            return false;
        }
    }
    /*--------------------------------------------------------------------------*/
}
