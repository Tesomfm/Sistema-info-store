package main.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Cliente extends PessoaAbstract implements Observer, Serializable {
    private static final long serialVersionUID = 1L;
    private String cpf;
    private List<ProdutoAbstract> listaDeDesejo;
    private transient List<String> notificacoes;

    public Cliente(String nome, String login, String senha, String cpf) {
        super(nome, login, senha);
        this.cpf = cpf;
        this.listaDeDesejo = new ArrayList<>();
        this.notificacoes = new ArrayList<>();
    }

    public void adicionarAListaDesejo(ProdutoAbstract p, boolean cond) {
        if (!listaDeDesejo.contains(p)) {
            listaDeDesejo.add(p);
            p.addObserver(this);
            if  (cond) {
                System.out.println(p.getNome() + " adicionado à sua lista de desejos!");
            }
        }
    }

    public void verListaDeDesejo() {
        if (listaDeDesejo.isEmpty()) {
            System.out.println("Sua lista de desejos está vazia.");
            return;
        }
        System.out.println("=== SUA LISTA DE DESEJOS ===");
        for (ProdutoAbstract p : listaDeDesejo) {
            String statusEstoque = p.getQuantidadeEmEstoque() > 0 ? "EM ESTOQUE" : "FORA DE ESTOQUE";
            System.out.printf("- %s (R$ %.2f) - %s\n", p.getNome(), p.getPreco(), statusEstoque);
        }
    }

    public List<ProdutoAbstract> getListaDeDesejo(){
        return this.listaDeDesejo;
    }

    public void removerDaListaDesejo(ProdutoAbstract p, boolean cond) {
        if (listaDeDesejo.contains(p)) {
            listaDeDesejo.remove(p);
            p.removeObserver(this);
            if (cond){
                System.out.println(p.getNome() + " foi removido da sua lista de desejos!");
        }
        }
        else {
            System.out.println(p.getNome() + " não está na lista de desejos!");
        }
    }

    @Override
    public void update(ProdutoAbstract produto, String tipoNotificacao) {
        if (notificacoes == null) {
            notificacoes = new ArrayList<>();
        }

        if (listaDeDesejo.contains(produto)) {
            String msg = "";
            if ("estoque_voltou".equals(tipoNotificacao)) {
                msg = String.format("BOA NOTÍCIA! O produto '%s' da sua lista de desejos está de volta ao estoque!", produto.getNome());
            } else if ("preco_caiu".equals(tipoNotificacao)) {
                msg = String.format("O PREÇO CAIU! O produto '%s' da sua lista de desejos está custando R$ %.2f!", produto.getNome(), produto.getPreco());
            }

            if (!msg.isEmpty()) {
                notificacoes.add(msg);
                System.out.println("\n*** Notificação lançada ! ***\n");
            }
        }
    }

    public List<String> getNotificacoes() {
        if (notificacoes == null) {
            notificacoes = new ArrayList<>();
        }
        return notificacoes;
    }

   public void limparNotificacoes() {
        if (notificacoes != null) {
            notificacoes.clear();
        }
    }

    public String getCpf() {
        return cpf;
    }

    public void restaurarObservadores() {

        if (this.listaDeDesejo == null) {
            this.listaDeDesejo = new ArrayList<>();
        }
        if (this.notificacoes == null) {
            this.notificacoes = new ArrayList<>();
        }
    }

}