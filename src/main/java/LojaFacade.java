package main.java;

import java.util.ArrayList;
import java.util.List;

public class LojaFacade {
    private LojaDeInformatica loja;
    public SistemaAutenticacao auth;
    private ProdutoFactory produtoFactory;
    private PessoaAbstract usuarioLogado;

    public LojaFacade() {
        this.loja = LojaDeInformatica.getInstancia();
        this.auth = new SistemaAutenticacao();
        this.produtoFactory = new ProdutoFactory();
        this.usuarioLogado = null;
    }

    public boolean registrarCliente(String nome, String login, String senha, String cpf) {
        return auth.registrarCliente(nome, login, senha, cpf);
    }

    public boolean registrarFuncionario(String nome, String login, String senha, String cargo) {
        return auth.registrarFuncionario(nome, login, senha, cargo);
    }

    public boolean login(String login, String senha) {
        this.usuarioLogado = auth.login(login, senha);
        if (this.usuarioLogado != null && this.usuarioLogado instanceof Cliente c) {
            restaurarObservadoresCliente(c);
        }
        return this.usuarioLogado != null;
    }

    private void restaurarObservadoresCliente(Cliente cliente) {
        cliente.verListaDeDesejo();
        List<ProdutoAbstract> ProdutosDoEstoque = loja.getEstoque();
        for(ProdutoAbstract ProdutoListaDesejo : new ArrayList<>(cliente.getListaDeDesejo())) {
            for (ProdutoAbstract produtoEstoque : ProdutosDoEstoque) {
                if (ProdutoListaDesejo.getNome().equals(produtoEstoque.getNome())) {
                    cliente.removerDaListaDesejo(ProdutoListaDesejo, false);
                    cliente.adicionarAListaDesejo(produtoEstoque, false);
                    break;
                }
            }
        }
    }


    public void logout() {
        System.out.println("Fazendo logout... Até logo, " + usuarioLogado.getNome());
        this.usuarioLogado = null;
    }

    public String getTipoUsuarioLogado() {
        if (usuarioLogado instanceof Cliente) {
            return "cliente";
        } else if (usuarioLogado instanceof Funcionario) {
            return "funcionario";
        }
        return null;
    }

    public void verNotificacoes() {
        if (usuarioLogado instanceof Cliente cliente) {
            List<String> notificacoes = cliente.getNotificacoes();
            if (notificacoes.isEmpty()) {
                System.out.println("Nenhuma notificação nova.");
                return;
            }
            else {
                System.out.println("\n--- SUAS NOTIFICAÇÕES ---");
                for (String msg : notificacoes) {
                    System.out.println("- " + msg);
                }
                cliente.limparNotificacoes();
                System.out.println("---------------------------");
            }
        }
    }

    public void consultarEstoque() {
        loja.exibirEstoque();
    }

    public void cadastrarProduto(String tipo, String nome, double preco, int qtd, String especificacao) {
        if (getTipoUsuarioLogado() != "funcionario") {
            System.out.println("Erro: Apenas funcionários podem cadastrar produtos.");
            return;
        }
        ProdutoAbstract p = produtoFactory.criarProduto(tipo, nome, preco, qtd, especificacao);
        loja.adicionarProduto(p);
    }

    public void adicionarEstoque(String nomeProduto, int quantidade) {
        if (getTipoUsuarioLogado() != "funcionario") {
            System.out.println("Erro: Apenas funcionários podem adicionar estoque.");
            return;
        }
        loja.adicionarEstoque(nomeProduto, quantidade);
    }

    public void alterarPreco(String nomeProduto, double novoPreco) {
        if (getTipoUsuarioLogado() != "funcionario") {
            System.out.println("Erro: Apenas funcionários podem alterar preços.");
            return;
        }
        loja.alterarPreco(nomeProduto, novoPreco);
    }

    public void verRelatorioVendas() {
        if (getTipoUsuarioLogado() != "funcionario") {
            System.out.println("Erro: Apenas funcionários podem ver o relatório.");
            return;
        }
        loja.exibirRelatorioDeVendas();
    }

    public void comprarProduto(String nomeProduto, int qtd) {
        if (getTipoUsuarioLogado() != "cliente") {
            System.out.println("Erro: Apenas clientes podem comprar.");
            return;
        }
        loja.realizarVenda(nomeProduto, qtd, (Cliente) usuarioLogado);
    }

    public void adicionarProdutoListaDesejo(String nomeProduto) {
        if (getTipoUsuarioLogado() != "cliente") {
            System.out.println("Erro: Apenas clientes podem ter lista de desejos.");
            return;
        }
        ProdutoAbstract p = loja.buscarProduto(nomeProduto);
        if (p != null) {
            ((Cliente) usuarioLogado).adicionarAListaDesejo(p,true);
        } else {
            System.out.println("Erro: Produto não encontrado no estoque.");
        }
    }

    public void verListaDesejo() {
        if (getTipoUsuarioLogado() != "cliente") {
            System.out.println("Erro: Apenas clientes podem ter lista de desejos.");
            return;
        }
        ((Cliente) usuarioLogado).verListaDeDesejo();
    }
    public boolean verificadorDeNome(String nome) {
        if((auth.validar(nome))){
            return true;
        }
        else return false;
    }

    public boolean AutenticadorDeCpf(String cpf) {
        if(auth.validador(cpf)){
            return true;
        }
        else return false;
    }
}