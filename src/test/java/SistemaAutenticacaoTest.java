import main.java.LojaFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class SistemaAutenticacaoTest {
    private LojaFacade facade;
    @BeforeEach
    public void setUp() {

        new File("usuarios.dat").delete();
        new File("estoque.dat").delete();
        new File("vendas.dat").delete();
        facade = new LojaFacade();

    }
    @Test
    @DisplayName("test de registar e conseguir fazer login cliente")
    public void testRegistarEConseguirFazerLoginCliente(){
        String nome = "Elias Teste";
        String login = "elias_teste";
        String senha = "123";
        String cpf = "111.111.111-11";

        boolean registoSucesso = facade.registrarCliente(nome, login, senha, cpf);

        assertTrue(registoSucesso, "O registo do cliente deveria ter tido sucesso.");

        boolean loginSucesso = facade.login(login, senha);
        assertTrue(loginSucesso, "O login com o cliente recém-registado deveria ter tido sucesso.");
    }
    @Test
    @DisplayName("test não deve registrar login duplicado")
    public void testNaoDeveRegistrarLoginDuplicado() {
        facade.registrarCliente("Elias 1", "elias_unico", "123", "111.111.111-11");

        boolean segundoRegisto = facade.registrarCliente("Elias 2", "elias_unico", "456", "222");
        assertFalse(segundoRegisto, "O sistema não deveria permitir o registo de um login duplicado.");

    }
    @Test
    @DisplayName("test de login deve falhar com senha errada")
    public void testLoginDeveFalharComSenhaErrada() {
        facade.registrarCliente("Elias", "elias", "senha_certa", "111.111.111-11");

        boolean loginSucesso = facade.login("elias", "senha_errada");

        assertFalse(loginSucesso, "O login deveria ter falhado devido à senha incorreta.");
    }
}
