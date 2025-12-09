/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.perfis;

public class Administrador extends Usuario {

    //  Construtor sem argumentos (necessário para o DAO)
    public Administrador() {
        super();
        setPerfil(Perfil.ADMINISTRADOR);
    }

    public Administrador(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf, Perfil.ADMINISTRADOR);
    }

    @Override
    public void acessarSistema() {
        System.out.println("Bem-vindo, administrador " + nome + "!");
        // Chamar a tela ou menu do administrador ***
    }
}
