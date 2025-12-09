/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.perfis;

public abstract class Usuario {

    protected int id;
    protected String nome;
    protected String email;
    protected String senha;
    protected String cpf;
    protected Perfil perfil;

    public Usuario(String nome, String email, String senha, String cpf, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.perfil = perfil;
    }

    public Usuario() {

    }

    public abstract void acessarSistema();

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
