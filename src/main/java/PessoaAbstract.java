package main.java;

import java.io.Serializable;

public abstract class PessoaAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nome;
    protected String login;
    protected String senha;

    public PessoaAbstract(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }
}