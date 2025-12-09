/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.perfis;

public class Investidor extends Usuario {

    // 🔹 Construtor sem argumentos (necessário para o DAO)
    public Investidor() {
        super();
        setPerfil(Perfil.INVESTIDOR);
    }

    public Investidor(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf, Perfil.INVESTIDOR);

    }

    @Override
    public void acessarSistema() {
        System.out.println("Bem-vindo, investidor " + nome + "!");
        // Aqui você pode chamar a tela ou menu do investidor
    }

}
