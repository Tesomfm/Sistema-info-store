
import main.java.LojaFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class LojaFacadeTest {
    private LojaFacade facade;

    @BeforeEach
    public void setUp() {
        new File("usuarios.dat").delete();
        new File("estoque.dat").delete();
        new File("vendas.dat").delete();


        facade = new LojaFacade();
        facade.registrarFuncionario("Admin Teste", "admin", "admin", "Gerente");
    }
    @Test
    @DisplayName("test funcionario pode cadastrar e adicionar estoque")
    public void testFuncionarioPodeCadastrarEAdicionarEstoque() {
        assertTrue(facade.login("admin", "admin"), "Login do admin deve funcionar");
        boolean cadastroSucesso = facade.cadastrarProduto("componente", "CPU", 1500.0, 10, "Intel");
        boolean estoqueSucesso = facade.adicionarEstoque("CPU", 5);

        assertTrue(cadastroSucesso, "Funcionário deveria conseguir cadastrar produto.");
        assertTrue(estoqueSucesso, "Funcionário deveria conseguir adicionar estoque.");
    }
    @Test
    @DisplayName("test cliente nao pode adicionar estoque")
    public void testSegurancaClienteNaoPodeAdicionarEstoque() {
        facade.login("admin", "admin");
        facade.cadastrarProduto("componente", "CPU", 1500.0, 10, "Intel");
        facade.logout();

        facade.registrarCliente("Cliente Teste", "cliente", "123", "111.111.111-11");
        assertTrue(facade.login("cliente", "123"), "Login do cliente deve funcionar");

        boolean estoqueSucesso = facade.adicionarEstoque("CPU", 99);
        assertFalse(estoqueSucesso, "Cliente NÃO PODE adicionar estoque.");
    }
    @Test
    @DisplayName("test de fluxo completo,login,lista desejo e compra")
    public void testFluxoCompletoLoginListaDesejoCompra() {
        facade.login("admin", "admin");
        facade.cadastrarProduto("periferico", "Mouse Gamer", 350.0, 20, "USB");
        facade.logout();

        facade.registrarCliente("Elias", "elias", "senha123", "222.222.222-22");
        assertTrue(facade.login("elias", "senha123"));

        boolean listaSucesso = facade.adicionarProdutoListaDesejo("Mouse Gamer");
        assertTrue(listaSucesso, "Deve conseguir adicionar à lista de desejos");

        facade.logout();

        facade.login("elias", "senha123");
        boolean compraSucesso = facade.comprarProduto("Mouse Gamer", 2);

        assertTrue(compraSucesso, "A compra deveria ter sido bem-sucedida.");
    }
}
