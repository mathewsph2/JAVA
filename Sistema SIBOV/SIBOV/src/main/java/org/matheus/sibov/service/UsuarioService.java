/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.matheus.sibov.service;

import java.text.DecimalFormat;
import org.matheus.sibov.dao.UsuarioDAO;
import org.matheus.perfis.Usuario;
import org.matheus.perfis.Investidor;
import org.matheus.perfis.Perfil;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Criar usuário com regras de negócio
    public void cadastrarUsuario(Usuario usuario) throws Exception {

        validarUsuario(usuario);

        // Regras: Investidor sempre deve ter carteira
        if (usuario instanceof Investidor && usuario.getPerfil() != Perfil.INVESTIDOR) {
            usuario.setPerfil(Perfil.INVESTIDOR);
        }

        usuarioDAO.salvar(usuario);
    }

    // Editar com regras antes de chamar o DAO
    public void editarUsuario(Usuario usuario) throws Exception {

        validarUsuario(usuario);

        // Não implementei editar ainda:  usuarioDAO.editar(usuario);
    }

    // LISTAR - REGRAS:
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.buscarTodos();
    }

    // DELELAR - REGRAS:
    public void deletarUsuario(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido para exclusão.");
        }
        usuarioDAO.deletar(id);
    }

    // Deletar por CPF
    public void deletarPorCpf(String cpf) throws Exception {

        if (cpf == null || cpf.isEmpty()) {
            throw new Exception("CPF inválido para exclusão.");
        }

        usuarioDAO.deletarPorCpf(cpf);
    }

    // INSERIR - REGRAS: 
    private void validarUsuario(Usuario usuario) throws Exception {
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new Exception("Nome não pode estar vazio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new Exception("Email inválido.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new Exception("Senha não pode ser vazia.");
        }

        if (usuario.getSenha().length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres.");
        }

        if (usuario.getCpf() == null || usuario.getCpf().isEmpty()) {
            throw new Exception("CPF é obrigatório.");
        }
        if (usuario.getPerfil() == null) {
            throw new Exception("Perfil do usuário é obrigatório.");
        }
    }

    public List<Usuario> buscarTodos() {
        UsuarioDAO dao = new UsuarioDAO();
        return dao.buscarTodos();
    }

    /* Editar - REGRAS: */
    public void atualizarUsuario(Usuario usuario) throws Exception {

        Usuario atual = usuarioDAO.buscarPorCpf(usuario.getCpf());

        if (atual == null) {
            throw new Exception("Usuário não encontrado.");
        }

        // Restringe trocar de perfil 
        if (atual.getPerfil() != usuario.getPerfil()) {
            throw new Exception("Não é permitido alterar o perfil!");
        }

        usuarioDAO.editar(usuario);
    }

    // Regras para o Saldo que aparecerá na tela do investidor 
    public double obterSaldoInvestidor(int idUsuario) {
        return usuarioDAO.buscarSaldoInvestidor(idUsuario);
    }

    public String obterSaldoFormatado(int idUsuario) {
        double saldo = usuarioDAO.buscarSaldoInvestidor(idUsuario);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "R$ " + df.format(saldo);
    }

    // Regras para validação da tela de Login : 
    public Usuario efetuarLogin(String email, String senha) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O email não pode estar vazio.");
        }

        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("A senha não pode estar vazia.");
        }

        Usuario user = usuarioDAO.efetuaLogin(email, senha);

        if (user == null) {
            throw new IllegalArgumentException("Email ou senha incorretos.");
        }

        return user;
    }

    /* ============================================================
      DEPOSITAR EM UMA CARTEIRA (POR ID DO INVESTIDOR)
     ============================================================ */
    public boolean depositar(int idUsuario, double valor) {
        UsuarioDAO dao = new UsuarioDAO();
        return dao.depositar(idUsuario, valor);
    }

}
