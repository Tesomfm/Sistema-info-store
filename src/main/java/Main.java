package main.java;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static LojaFacade facade = new LojaFacade();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        if ((facade.auth.buscarUsuarioPorLogin("admin")) == null) {
            System.out.println("Primeira execução: Criando usuário 'admin' com senha 'admin'...");
            facade.registrarFuncionario("Administrador", "admin", "admin", "Gerente");
        }

        int opcao = -1;
        do {
            System.out.println("\n=== BEM-VINDO À LOJA DE INFORMÁTICA ===");
            System.out.println("1 - Fazer Login");
            System.out.println("2 - Registrar nova conta de Cliente");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    registrarCliente();
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
        sc.close();
    }

    private static void fazerLogin() {
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        if (facade.login(login, senha)) {
            String tipoUsuario = facade.getTipoUsuarioLogado();
            if ("cliente".equals(tipoUsuario)) {
                menuCliente();
            } else if ("funcionario".equals(tipoUsuario)) {
                menuFuncionario();
            }
        }
    }

    private static void registrarCliente() {
        System.out.print("Nome completo: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Login (nome de usuário): ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        facade.registrarCliente(nome, login, senha, cpf);
    }

    private static void menuCliente() {
        int opcao = -1;
        do {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1 - Consultar Estoque");
            System.out.println("2 - Comprar Produto");
            System.out.println("3 - Adicionar Produto à Lista de Desejos");
            System.out.println("4 - Ver Lista de Desejos");
            System.out.println("5 - Ver Minhas Notificações");
            System.out.println("0 - Fazer Logout");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    facade.consultarEstoque();
                    break;
                case 2:
                    comprarProduto();
                    break;
                case 3:
                    addListaDesejo();
                    break;
                case 4:
                    facade.verListaDesejo();
                    break;
                case 5:
                    facade.verNotificacoes();
                    break;
                case 0:
                    facade.logout();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuFuncionario() {
        int opcao = -1;
        do {
            System.out.println("\n--- MENU FUNCIONÁRIO ---");
            System.out.println("1 - Consultar Estoque");
            System.out.println("2 - Cadastrar Novo Produto");
            System.out.println("3 - Adicionar Estoque a um Produto");
            System.out.println("4 - Alterar Preço de um Produto");
            System.out.println("5 - Ver Relatório de Vendas");
            System.out.println("0 - Fazer Logout");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    facade.consultarEstoque();
                    break;
                case 2:
                    cadastrarProduto();
                    break;
                case 3:
                    adicionarEstoque();
                    break;
                case 4:
                    alterarPreco();
                    break;
                case 5:
                    facade.verRelatorioVendas();
                    break;
                case 0:
                    facade.logout();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void comprarProduto() {
        facade.consultarEstoque();
        System.out.print("Digite o nome do produto que deseja comprar: ");
        String nome = sc.nextLine();
        System.out.print("Digite a quantidade: ");
        int qtd = lerInt();
        facade.comprarProduto(nome, qtd);
    }

    private static void addListaDesejo() {
        facade.consultarEstoque();
        System.out.print("Digite o nome do produto para adicionar à lista de desejos: ");
        String nome = sc.nextLine();
        facade.adicionarProdutoListaDesejo(nome);
    }

    private static void cadastrarProduto() {
        System.out.print("Tipo (componente / periferico): ");
        String tipo = sc.nextLine();
        System.out.print("Nome do produto: ");
        String nome = sc.nextLine();
        System.out.print("Preço (ex: 450.99): ");
        double preco = lerDouble();
        System.out.print("Quantidade inicial: ");
        int qtd = lerInt();
        String esp;
        if (tipo.equalsIgnoreCase("componente")) {
            System.out.print("Fabricante (ex: Intel, AMD): ");
            esp = sc.nextLine();
        } else if (tipo.equalsIgnoreCase("periferico")) {
            System.out.print("Tipo de Conexão (ex: USB, Bluetooth): ");
            esp = sc.nextLine();
        } else {
            System.out.println("Tipo inválido. Abortando.");
            return;
        }
        facade.cadastrarProduto(tipo, nome, preco, qtd, esp);
    }

    private static void adicionarEstoque() {
        facade.consultarEstoque();
        System.out.print("Digite o nome do produto para adicionar estoque: ");
        String nome = sc.nextLine();
        System.out.print("Quantidade a adicionar: ");
        int qtd = lerInt();
        facade.adicionarEstoque(nome, qtd);
    }

    private static void alterarPreco() {
        facade.consultarEstoque();
        System.out.print("Digite o nome do produto para alterar o preço: ");
        String nome = sc.nextLine();
        System.out.print("Digite o novo preço: ");
        double preco = lerDouble();
        facade.alterarPreco(nome, preco);
    }

    private static int lerInt() {
        while (true) {
            try {
                int val = sc.nextInt();
                sc.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Digite um número inteiro: ");
                sc.nextLine();
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                double val = sc.nextDouble();
                sc.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Digite um número (ex: 120.50): ");
                sc.nextLine();
            }
        }
    }
}