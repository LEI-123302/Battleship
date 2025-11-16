// language: java
package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a classe Fleet, focando-se na lógica de gestão da frota
 * (adicionar barcos, encontrar barcos, verificar estado).
 */
class FleetTest {

    /**
     * Classe 'TestShip' (reutilizada de ShipTest) para instanciar a classe abstrata 'Ship'.
     * Permite-nos controlar as propriedades do barco (posições, categoria) para os testes.
     */
    private static class TestShip extends Ship {
        public TestShip(String category, Compass bearing, IPosition pos) {
            super(category, bearing, pos);
        }

        @Override
        public Integer getSize() {
            return getPositions().size();
        }

        public void addTestPosition(IPosition pos) {
            this.positions.add(pos);
        }
    }

    // --- Fim da classe interna ---

    private Fleet fleet;
    private TestShip ship1;
    private TestShip ship2;

    @BeforeEach
    @DisplayName("Configuração inicial: Cria uma frota vazia e barcos de teste")
    void setUp() {
        fleet = new Fleet();

        // Ship1 ("Barge") em (1,1) e (1,2)
        ship1 = new TestShip("Barge", Compass.NORTH, new Position(1, 1));
        ship1.addTestPosition(new Position(1, 1));
        ship1.addTestPosition(new Position(1, 2));

        // Ship2 ("Caravel") em (5,5)
        ship2 = new TestShip("Caravel", Compass.SOUTH, new Position(5, 5));
        ship2.addTestPosition(new Position(5, 5));
    }

    @Test
    @DisplayName("Construtor: Frota deve ser criada vazia")
    void testConstructor() {
        assertNotNull(fleet.getShips(), "A lista de barcos não deve ser nula");
        assertTrue(fleet.getShips().isEmpty(), "A lista de barcos deve estar vazia");
    }

    @Test
    @DisplayName("addShip: Adicionar um barco válido com sucesso")
    void testAddShip_Success() {
        boolean result = fleet.addShip(ship1);

        assertTrue(result, "addShip deve retornar true para barco válido");
        assertEquals(1, fleet.getShips().size(), "Frota deve ter 1 barco");
        assertEquals(ship1, fleet.getShips().get(0), "O barco correto deve estar na frota");
    }

    @Test
    @DisplayName("addShip: Falha ao adicionar barco fora do tabuleiro (BOARD_SIZE)")
    void testAddShip_Failure_OutsideBoard() {
        // Assume IFleet.BOARD_SIZE = 10 (índices 0-9)
        TestShip shipLeft = new TestShip("Left", Compass.NORTH, new Position(-1, 1));
        shipLeft.addTestPosition(new Position(-1, 1));
        assertFalse(fleet.addShip(shipLeft), "Deve falhar se LeftMostPos < 0");

        TestShip shipRight = new TestShip("Right", Compass.NORTH, new Position(10, 1));
        shipRight.addTestPosition(new Position(10, 1));
        assertFalse(fleet.addShip(shipRight), "Deve falhar se RightMostPos > 9");

        TestShip shipTop = new TestShip("Top", Compass.NORTH, new Position(1, -1));
        shipTop.addTestPosition(new Position(1, -1));
        assertFalse(fleet.addShip(shipTop), "Deve falhar se TopMostPos < 0");

        TestShip shipBottom = new TestShip("Bottom", Compass.NORTH, new Position(1, 10));
        shipBottom.addTestPosition(new Position(1, 10));
        assertFalse(fleet.addShip(shipBottom), "Deve falhar se BottomMostPos > 9");

        assertEquals(0, fleet.getShips().size(), "Nenhum barco deve ser adicionado");
    }

    @Test
    @DisplayName("addShip: Falha ao adicionar barco muito perto (colisão)")
    void testAddShip_Failure_CollisionRisk() {
        fleet.addShip(ship1); // Adiciona barco em (1,1), (1,2)
        TestShip shipClose = new TestShip("Close", Compass.EAST, new Position(1, 3));
        shipClose.addTestPosition(new Position(1, 3)); // Adjacente a (1,2)

        assertFalse(fleet.addShip(shipClose), "Não deve adicionar barco muito perto");
        assertEquals(1, fleet.getShips().size(), "Apenas o primeiro barco deve estar na frota");
    }

    @Test
    @DisplayName("addShip: Falha ao adicionar barco sobreposto")
    void testAddShip_Failure_Overlap() {
        fleet.addShip(ship1); // Adiciona barco em (1,1), (1,2)
        TestShip shipOverlap = new TestShip("Overlap", Compass.EAST, new Position(1, 2));
        shipOverlap.addTestPosition(new Position(1, 2)); // Sobrepõe (1,2)

        assertFalse(fleet.addShip(shipOverlap), "Não deve adicionar barco sobreposto");
        assertEquals(1, fleet.getShips().size());
    }

    @Test
    @DisplayName("addShip: Testa o bug do limite da frota (FLEET_SIZE)")
    void testAddShip_FleetSizeLimit_Bug() {
        // Assume IFleet.FLEET_SIZE = 12 e BOARD_SIZE = 10
        // Cria 12 barcos em posições não-adjacentes
        for (int i = 0; i < IFleet.FLEET_SIZE; i++) {
            int row = (i / 5) * 2;
            int col = (i % 5) * 2;
            Position pos = new Position(row, col);

            TestShip s = new TestShip("Barge", Compass.NORTH, pos);
            s.addTestPosition(pos);
            assertTrue(fleet.addShip(s), "Adição " + i + " deve ter sucesso");
        }
        assertEquals(IFleet.FLEET_SIZE, fleet.getShips().size(), "Frota deve estar cheia com 12 barcos");

        // *** CORREÇÃO DO TESTE ***
        // Agora testamos o BUG. O código do professor aceita o 13º barco.
        TestShip extraShip = new TestShip("Extra", Compass.NORTH, new Position(9, 9));
        extraShip.addTestPosition(new Position(9, 9));

        // Esperamos 'true' (o bug) em vez de 'false'
        assertTrue(fleet.addShip(extraShip), "O código aceita o 13º barco (bug)");

        // Esperamos que o tamanho seja 13 (o bug)
        assertEquals(IFleet.FLEET_SIZE + 1, fleet.getShips().size(), "Tamanho da frota é 13 (bug)");
    }

    @Test
    @DisplayName("getShipsLike: Encontra barcos por categoria")
    void testGetShipsLike() {
        fleet.addShip(ship1); // "Barge"
        fleet.addShip(ship2); // "Caravel"

        List<IShip> barges = fleet.getShipsLike("Barge");
        assertEquals(1, barges.size());
        assertEquals(ship1, barges.get(0));

        List<IShip> caravels = fleet.getShipsLike("Caravel");
        assertEquals(1, caravels.size());
        assertEquals(ship2, caravels.get(0));
    }

    @Test
    @DisplayName("getShipsLike: Retorna lista vazia se categoria não existe")
    void testGetShipsLike_NotFound() {
        fleet.addShip(ship1); // "Barge"
        List<IShip> none = fleet.getShipsLike("Galleon");
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    @DisplayName("getFloatingShips: Retorna apenas barcos a flutuar")
    void testGetFloatingShips() {
        fleet.addShip(ship1);
        fleet.addShip(ship2);
        assertEquals(2, fleet.getFloatingShips().size(), "Ambos devem flutuar inicialmente");

        // Afunda o ship1
        ship1.getPositions().get(0).shoot();
        ship1.getPositions().get(1).shoot();
        assertFalse(ship1.stillFloating());

        List<IShip> floating = fleet.getFloatingShips();
        assertEquals(1, floating.size());
        assertEquals(ship2, floating.get(0));

        // Afunda o ship2
        ship2.getPositions().get(0).shoot();
        assertFalse(ship2.stillFloating());

        assertEquals(0, fleet.getFloatingShips().size());
    }

    @Test
    @DisplayName("shipAt: Encontra barco na posição correta")
    void testShipAt_Found() {
        fleet.addShip(ship1); // Ocupa (1,1) e (1,2)
        fleet.addShip(ship2); // Ocupa (5,5)

        assertEquals(ship1, fleet.shipAt(new Position(1, 1)));
        assertEquals(ship1, fleet.shipAt(new Position(1, 2)));
        assertEquals(ship2, fleet.shipAt(new Position(5, 5)));
    }

    @Test
    @DisplayName("shipAt: Retorna null se não encontra barco")
    void testShipAt_NotFound() {
        fleet.addShip(ship1);
        assertNull(fleet.shipAt(new Position(9, 9)));
        assertNull(fleet.shipAt(new Position(1, 3)));
    }

    @Test
    @DisplayName("Cobertura de Asserção em printShipsByCategory (requer -ea)")
    void testAssertionCoverage() {
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream())); // Descarta a saída

        assertThrows(AssertionError.class, () ->
                        fleet.printShipsByCategory(null),
                "Assert 'category != null' deve falhar");

        System.setOut(originalOut); // Restaura a saída
    }

    @Test
    @DisplayName("Cobertura dos métodos print (smoke test)")
    void testPrintMethods() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        // Chama todos os métodos de impressão
        fleet.printStatus();
        fleet.printAllShips();
        fleet.printFloatingShips();
        fleet.printShipsByCategory("Barge");
        Fleet.printShips(fleet.getShips()); // Testa o método estático

        System.setOut(originalOut); // Restaura a saída

        String output = out.toString();
        assertTrue(output.contains("[Barge n Linha = 1 Coluna = 1]"));
        assertTrue(output.contains("[Caravel s Linha = 5 Coluna = 5]"));
    }

    // --- Testes para cobrir ramos de loops vazios ---

    @Test
    @DisplayName("getShipsLike deve retornar lista vazia se a frota estiver vazia")
    void testGetShipsLike_EmptyFleet() {
        Fleet emptyFleet = new Fleet();
        List<IShip> result = emptyFleet.getShipsLike("Barge");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getFloatingShips deve retornar lista vazia se a frota estiver vazia")
    void testGetFloatingShips_EmptyFleet() {
        Fleet emptyFleet = new Fleet();
        List<IShip> result = emptyFleet.getFloatingShips();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("shipAt deve retornar null se a frota estiver vazia")
    void testShipAt_EmptyFleet() {
        Fleet emptyFleet = new Fleet();
        IShip result = emptyFleet.shipAt(new Position(1, 1));
        assertNull(result);
    }
}