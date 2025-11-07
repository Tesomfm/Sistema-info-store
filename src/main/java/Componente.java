package main.java;

import java.io.Serializable;

public class Componente extends ProdutoAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fabricante;

    public Componente(String nome, double preco, int quantidadeEmEstoque, String fabricante) {
        super(nome, preco, quantidadeEmEstoque);
        this.fabricante = fabricante;
    }

    @Override
    public String getDescricao() {
        return String.format("Componente: %s (Fabricante: %s)", nome, fabricante);
    }

    public String getFabricante() {
        return fabricante;
    }
}