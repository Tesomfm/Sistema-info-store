package main.java;

import com.github.freva.asciitable.AsciiTable;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LojaDeInformatica implements Serializable {
    private static final long serialVersionUID = 1L;
    private static LojaDeInformatica instancia;
    private List<ProdutoAbstract> estoque;
    private List<String> relatorioDeVendas;
    private static final String ARQUIVO_ESTOQUE = "estoque.dat";
    private static final String ARQUIVO_VENDAS = "vendas.dat";

    private LojaDeInformatica() {
        this.estoque = carregarEstoque();
        this.relatorioDeVendas = carregarVendas();
    }

    public static synchronized LojaDeInformatica getInstancia() {
        if (instancia == null) {
            instancia = new LojaDeInformatica();
        }
        return instancia;
    }

    public List<ProdutoAbstract> getEstoque() {
        return estoque;
    }
    public ProdutoAbstract buscarProduto(String nome) {
        for (ProdutoAbstract p : estoque) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    public boolean adicionarProduto(ProdutoAbstract produto) {
        ProdutoAbstract existente = buscarProduto(produto.getNome());
        if (existente != null) {
            System.out.println("Produto já existe. Usando 'adicionarEstoque' no lugar.");
            return adicionarEstoque(produto.getNome(), produto.getQuantidadeEmEstoque());
        } else {
            estoque.add(produto);
            salvarEstoque();
            System.out.println("Produto " + produto.getNome() + " cadastrado com sucesso.");
            return true;
        }
    }

    public boolean adicionarEstoque(String nomeProduto, int quantidade) {
        ProdutoAbstract p = buscarProduto(nomeProduto);
        if (p != null) {
            int qtdAntiga = p.getQuantidadeEmEstoque();
            p.setQuantidadeEmEstoque(qtdAntiga + quantidade);
            salvarEstoque();
            System.out.printf("Estoque de '%s' atualizado para %d unidades.\n", nomeProduto, p.getQuantidadeEmEstoque());

            return true;
        } else {
            System.out.println("Erro: Produto não encontrado para adicionar estoque.");
            return false;
        }
    }

    public boolean alterarPreco(String nomeProduto, double novoPreco) {
        ProdutoAbstract p = buscarProduto(nomeProduto);
        if (p != null) {
            p.setPreco(novoPreco);
            salvarEstoque();
            System.out.printf("Preço de '%s' atualizado para R$ %.2f.\n", nomeProduto, novoPreco);
            return true;
        } else {
            System.out.println("Erro: Produto não encontrado para alterar o preço.");
            return false;
        }
    }

    public void exibirEstoque() {
        if (estoque.isEmpty()) {
            System.out.println("Estoque vazio.");
            return;
        }
        String[] headers = {"NOME", "TIPO", "PREÇO", "QTD", "ESPECIFICAÇÃO"};
        String[][] data = new String[estoque.size()][5];
        for (int i = 0; i < estoque.size(); i++) {
            ProdutoAbstract p = estoque.get(i);
            data[i][0] = p.getNome();
            data[i][1] = p instanceof Componente ? "Componente" : "Periférico";
            data[i][2] = String.format("R$ %.2f", p.getPreco());
            data[i][3] = String.valueOf(p.getQuantidadeEmEstoque());
            if (p instanceof Componente c) {
                data[i][4] = "Fab: " + c.getFabricante();
            } else if (p instanceof Periferico pe) {
                data[i][4] = "Conex: " + pe.getTipoConexao();
            }
        }
        System.out.println(AsciiTable.getTable(headers, data));
    }

    public boolean realizarVenda(String nomeProduto, int qtd, Cliente cliente) {
        ProdutoAbstract p = buscarProduto(nomeProduto);
        if (p == null) {
            System.out.println("Erro:Produto '" + nomeProduto + "' não encontrado!");
            return false;
        }
        if (p.getQuantidadeEmEstoque() < qtd) {
            System.out.println("Erro: Estoque insuficiente. Disponível: " + p.getQuantidadeEmEstoque());
            return false;
        }

        p.vender(qtd);
        double total = p.getPreco() * qtd;

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = agora.format(formatter);

        String registro = String.format("%s;%d;%.2f;%s;%s",
                p.getNome(), qtd, total, dataHoraFormatada, cliente.getNome());

        relatorioDeVendas.add(registro);
        salvarVendas();
        salvarEstoque();
        System.out.println("Venda realizada com sucesso para " + cliente.getNome() + "!");
        return true;
    }

    public void exibirRelatorioDeVendas() {
        System.out.println("\n=== RELATÓRIO DE VENDAS ===");
        if (relatorioDeVendas == null || relatorioDeVendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }

        String[] headers = {"PRODUTO", "QTD", "VALOR TOTAL", "DATA", "CLIENTE"};
        String[][] data = new String[relatorioDeVendas.size()][5];
        double faturamentoTotal = 0.0;

        for (int i = 0; i < relatorioDeVendas.size(); i++) {
            String[] partes = relatorioDeVendas.get(i).split(";");
            if (partes.length < 5) continue;

            String nome = partes[0];
            String qtd = partes[1];
            double valor = Double.parseDouble(partes[2].replace(",", "."));
            String dataVenda = partes[3];
            String clienteNome = partes[4];

            data[i][0] = nome;
            data[i][1] = qtd;
            data[i][2] = String.format("R$ %.2f", valor);
            data[i][3] = dataVenda;
            data[i][4] = clienteNome;
            faturamentoTotal += valor;
        }
        System.out.println(AsciiTable.getTable(headers, data));
        System.out.printf("FATURAMENTO TOTAL: R$ %.2f\n", faturamentoTotal);
    }

    private void salvarEstoque() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_ESTOQUE))) {
            out.writeObject(estoque);
        } catch (IOException e) {
            System.err.println("Erro ao salvar estoque! " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<ProdutoAbstract> carregarEstoque() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_ESTOQUE))) {
            List<ProdutoAbstract> produtos = (List<ProdutoAbstract>) in.readObject();
            for (ProdutoAbstract p : produtos) {
                p.addObserver(null);
                p.removeObserver(null);
            }
            return produtos;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void salvarVendas() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_VENDAS))) {
            out.writeObject(relatorioDeVendas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar vendas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> carregarVendas() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_VENDAS))) {
            return (List<String>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}