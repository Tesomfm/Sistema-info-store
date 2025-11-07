package main.java;

public class ProdutoFactory {
    public ProdutoAbstract criarProduto(String tipo, String nome, double preco, int qtd, String especificacao) {
        if ("componente".equalsIgnoreCase(tipo)) {
            return new Componente(nome, preco, qtd, especificacao);
        } else if ("periferico".equalsIgnoreCase(tipo)) {
            return new Periferico(nome, preco, qtd, especificacao);
        } else {
            throw new IllegalArgumentException("Tipo de produto desconhecido: " + tipo);
        }
    }
}