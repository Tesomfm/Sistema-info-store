package main.java;

import java.io.Serializable;


public class Funcionario extends PessoaAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cargo;

    public Funcionario(String nome, String login, String senha, String cargo) {
        super(nome, login, senha);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }
}