package main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaAutenticacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<PessoaAbstract> usuarios;
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";

    public SistemaAutenticacao() {
        this.usuarios = carregarUsuarios();
    }

    private ArrayList<PessoaAbstract> carregarUsuarios() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_USUARIOS))) {
            return (ArrayList<PessoaAbstract>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void salvarUsuarios() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_USUARIOS))) {
            out.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    public boolean registrarCliente(String nome, String login, String senha, String cpf) {
        if (buscarUsuarioPorLogin(login) != null) {
            System.out.println("Erro: Login já cadastrado.");
            return false;
        }
        Cliente novoCliente = new Cliente(nome, login, senha, cpf);
        usuarios.add(novoCliente);
        salvarUsuarios();
        System.out.println("Cliente cadastrado com sucesso!");
        return true;
    }

    public boolean registrarFuncionario(String nome, String login, String senha, String cargo) {
        if (buscarUsuarioPorLogin(login) != null) {
            System.out.println("Erro: Login já cadastrado.");
            return false;
        }
        Funcionario novoFuncionario = new Funcionario(nome, login, senha, cargo);
        usuarios.add(novoFuncionario);
        salvarUsuarios();
        System.out.println("Funcionário cadastrado com sucesso!");
        return true;
    }

    public PessoaAbstract login(String login, String senha) {
        PessoaAbstract usuario = buscarUsuarioPorLogin(login);
        if (usuario != null && usuario.verificarSenha(senha)) {
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + usuario.getNome());

            if (usuario instanceof Cliente cliente) {
                cliente.restaurarObservadores();

                List<String> notificacoes = cliente.getNotificacoes();
                if (notificacoes != null && !notificacoes.isEmpty()) {
                    System.out.println("\n--- VOCÊ TEM NOVAS NOTIFICAÇÕES ---");
                    for (String msg : notificacoes) {
                        System.out.println("- " + msg);
                    }
                    cliente.limparNotificacoes();
                    System.out.println("-------------------------------------\n");
                }
            }
            return usuario;
        }
        System.out.println("Erro: Login ou senha inválidos.");
        return null;
    }

    public PessoaAbstract buscarUsuarioPorLogin(String login) {
        for (PessoaAbstract u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }

    public boolean validar (String nome) {
            if(nome == null || nome.length() <2){
                return false;
            }
            nome.trim().replaceAll(" +"," ");
            for(int i = 0; i < nome.length() ; i++){
                char c = nome.charAt(i);
                if( !( Character.isLetter(c) ) && ( c != '-' ) && (c != '\'') && c !=' '){
                    return false;
                }
            }
            return true;
    }

    public boolean validador(String cpf){
            cpf = cpf.replaceAll("[^0-9]",""); // remove todos os caracteres não numéricos e troca por nada
            cpf = cpf.trim();// remove os espaços em branco no início e no final da string.
            char primeiraLetra = cpf.charAt(0);
            boolean todosIguas = true;
            if (!cpf.isEmpty()) {
                for (int i = 0; cpf.length() > i; i++) {
                    if (cpf.charAt(i) != primeiraLetra) { // se o primeiro caractere do cpf for == ao primeiro caractere continua até que seja diferente
                        todosIguas = false;
                        break;
                    }
                }
            }
            if((cpf.length() != 11) || todosIguas) {
                return false;  // isso serve de tratamento de erro para o novePrimeirosDigitos
            }
            String novePrimeirosDigtos = cpf.substring(0, 9);
            int digitoVerificador1 =calcularDigito(novePrimeirosDigtos,10);
            int digitoVerificador2 = calcularDigito(novePrimeirosDigtos+digitoVerificador1,11);
            //verificar se os digitos verificadores estão == ao do cpf
            return cpf.equals(novePrimeirosDigtos + digitoVerificador1 + digitoVerificador2);
        }
        public static int calcularDigito(String str, int pesoInicial) {
            int soma = 0;
            for (int i = 0; i < str.length(); i++) {
                int digito = Character.getNumericValue(str.charAt(i));
                soma += digito * pesoInicial--;
            }
            int resto = soma % 11;
            if (resto <2 ){
                return 0;
            }else {
                return 11-resto;
            }
        }

}