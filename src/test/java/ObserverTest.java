
import main.java.LojaFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {
    private LojaFacade facade;
    private final String NOME_PRODUTO = "Placa de video";
    @BeforeEach
    public void setUp() {
        new File("usuarios.dat").delete();
        new File("estoque.dat").delete();
        new File("vendas.dat").delete();

        facade = new LojaFacade();

        facade.registrarFuncionario("Admin", "admin", "admin", "Gerente");
        facade.registrarCliente("Elias Observador", "elias_obs", "123", "333");

        facade.login("admin", "admin");
        facade.cadastrarProduto("componente", NOME_PRODUTO, 5000.0, 0, "NVIDIA");
        facade.logout();

        facade.login("elias_obs", "123");
        facade.adicionarProdutoListaDesejo(NOME_PRODUTO);
        facade.logout();
    }
    @Test
    @DisplayName("est cliente recebe notificacao quando produto volta ao estoque")
    public void testClienteRecebeNotificacaoQuandoProdutoVoltaAoEstoque() {
        assertTrue(facade.login("elias_obs", "123"));
        boolean listaSucesso = facade.adicionarProdutoListaDesejo(NOME_PRODUTO);
        assertTrue(listaSucesso, "Deve conseguir adicionar à lista de desejos");
        facade.logout();

        facade.login("admin", "admin");
        boolean estoqueSucesso = facade.adicionarEstoque(NOME_PRODUTO, 100);
        assertTrue(estoqueSucesso, "Admin deve conseguir adicionar estoque.");
        facade.logout();

        boolean loginSucesso = facade.login("elias_obs", "123");
        assertTrue(loginSucesso, "Cliente deve conseguir fazer login.");

        List<String> notificacoes = facade.getNotificacoesUsuarioLogado();

        assertNotNull(notificacoes, "Lista de notificações não pode ser nula.");
        assertEquals(1, notificacoes.size(), "Deveria haver exatamente 1 notificação.");

        String msg = notificacoes.get(0);
        assertTrue(msg.contains("de volta ao estoque"), "Mensagem de 'de volta ao estoque' esperada.");
        assertTrue(msg.contains(NOME_PRODUTO), "Mensagem deve conter o nome do produto.");
    }

    @Test
    @DisplayName("test cliente recebe notificacao quando produto cair de preco")
    public void testClienteRecebeNotificacaoQuandoProdutoCairDePreco(){
        assertTrue(facade.login("elias_obs", "123"));
        boolean listaSucesso = facade.adicionarProdutoListaDesejo(NOME_PRODUTO);
        assertTrue(listaSucesso, "Deve conseguir adicionar à lista de desejos");
        facade.logout();

        facade.login("admin", "admin");
        boolean mudarPreco = facade.alterarPreco(NOME_PRODUTO, 3330);
        assertTrue(mudarPreco, "Admin deve conseguir alterar preco.");
        facade.logout();

        boolean loginSucesso = facade.login("elias_obs", "123");
        assertTrue(loginSucesso, "Cliente deve conseguir fazer login.");

        List <String> notificacoes = facade.getNotificacoesUsuarioLogado();

        assertNotNull(notificacoes, "Lista de notificações não pode ser nula.");
        assertEquals(1, notificacoes.size(), "Deveria haver exatamente 1 notificação.");

        String msg = notificacoes.get(0);
        assertTrue(msg.contains("O PREÇO CAIU!"), "Mensagem de 'de preço caiu' esperada.");
        assertTrue(msg.contains(NOME_PRODUTO), "Mensagem deve conter o nome do produto.");

    }
}
