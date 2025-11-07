package main.java;

import java.io.Serializable;

public class Periferico extends ProdutoAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipoConexao;

    public Periferico(String nome, double preco, int quantidadeEmEstoque, String tipoConexao) {
        super(nome, preco, quantidadeEmEstoque);
        this.tipoConexao = tipoConexao;
    }

    @Override
    public String getDescricao() {
        return String.format("Periférico: %s (Conexão: %s)", nome, tipoConexao);
    }

    public String getTipoConexao() {
        return tipoConexao;
    }
}