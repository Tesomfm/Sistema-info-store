package main.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ProdutoAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nome;
    protected double preco;
    protected int quantidadeEmEstoque;

    private transient List<Observer> observers = new ArrayList<>();

    public ProdutoAbstract(String nome, double preco, int quantidadeEmEstoque) {
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.observers = new ArrayList<>(); // Garante que a lista não seja nula após a desserialização
    }

    public abstract String getDescricao();

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidadeEmEstoque() { return quantidadeEmEstoque; }

    public void setPreco(double preco) {
        if (this.preco > preco) {
            this.preco = preco;
            notifyObservers("preco_caiu");
        }
        this.preco = preco;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        if (this.quantidadeEmEstoque == 0 && quantidadeEmEstoque > 0) {
            notifyObservers("estoque_voltou");
        }
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }
    public void addObserver(Observer observer) {
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
        if (!this.observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        if (this.observers != null) {
            this.observers.remove(observer);
        }
    }

    public void notifyObservers(String tipoNotificacao) {
        if (this.observers == null) return;
        for (Observer observer : new ArrayList<>(this.observers)) {
            observer.update(this, tipoNotificacao);
        }
    }

    public void vender(int qtd) {
        this.quantidadeEmEstoque -= qtd;
    }

}