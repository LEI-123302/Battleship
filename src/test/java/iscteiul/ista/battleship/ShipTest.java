// language: java
package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

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

    private TestShip ship;
    private IPosition p11;
    private IPosition p12;
    private IPosition p55;

    @BeforeEach
    void setUp() {
        ship = new TestShip("TestShip", Compass.NORTH, new Position(1,1));
        p11 = new Position(1,1);
        p12 = new Position(1,2);
        p55 = new Position(5,5);
        ship.addTestPosition(p11);
        ship.addTestPosition(p12);
    }

    @Test
    @DisplayName("Construtor e getters")
    void testConstructorAndGetters() {
        TestShip s = new TestShip("X", Compass.SOUTH, new Position(4,4));
        assertEquals("X", s.getCategory());
        assertEquals(Compass.SOUTH, s.getBearing());
        assertEquals(new Position(4,4), s.getPosition());
        assertNotNull(s.getPositions());
        assertEquals(0, s.getSize());
    }

    @Test
    @DisplayName("Factory buildShip cria tipos conhecidos e retorna null para desconhecido")
    void testBuildShipFactory() {
        Position pos = new Position(0,0);
        assertInstanceOf(Barge.class, Ship.buildShip("barca", Compass.NORTH, pos));
        assertInstanceOf(Caravel.class, Ship.buildShip("caravela", Compass.NORTH, pos));
        assertInstanceOf(Carrack.class, Ship.buildShip("nau", Compass.NORTH, pos));
        assertInstanceOf(Frigate.class, Ship.buildShip("fragata", Compass.NORTH, pos));
        assertInstanceOf(Galleon.class, Ship.buildShip("galeao", Compass.NORTH, pos));
        assertNull(Ship.buildShip("submarino", Compass.NORTH, pos));
    }

    @Test
    @DisplayName("stillFloating com nenhum, parcial e total hits")
    void testStillFloatingVariants() {
        assertTrue(ship.stillFloating()); // nenhum hit
        p11.shoot();
        assertTrue(ship.stillFloating()); // parcial
        p12.shoot();
        assertFalse(ship.stillFloating()); // todos atingidos
    }

    @Test
    @DisplayName("shoot marca apenas a posição correta")
    void testShoot() {
        ship.shoot(new Position(1,1)); // novo objecto mas equals deve bater
        assertTrue(p11.isHit());
        assertFalse(p12.isHit());
        // tiro fora não altera
        ship.shoot(p55);
        assertFalse(p55.isHit());
    }

    @Test
    @DisplayName("occupies funciona usando equals")
    void testOccupies() {
        assertTrue(ship.occupies(new Position(1,1)));
        assertTrue(ship.occupies(new Position(1,2)));
        assertFalse(ship.occupies(new Position(2,2)));
    }

    @Test
    @DisplayName("tooCloseTo por posição e por outro barco")
    void testTooCloseTo() {
        assertTrue(ship.tooCloseTo(new Position(0,1))); // adjacente a (1,1)
        TestShip other = new TestShip("O", Compass.EAST, new Position(1,3));
        other.addTestPosition(new Position(1,3)); // adjacente a (1,2)
        assertTrue(ship.tooCloseTo(other));
        // distante
        TestShip far = new TestShip("F", Compass.EAST, new Position(5,5));
        far.addTestPosition(new Position(5,5));
        assertFalse(ship.tooCloseTo(far));
    }

    @Test
    @DisplayName("Cálculo de extremos (top/bottom/left/right)")
    void testExtremes() {
        // posições: (1,1) e (1,2)
        assertEquals(1, ship.getTopMostPos());
        assertEquals(1, ship.getBottomMostPos());
        assertEquals(1, ship.getLeftMostPos());
        assertEquals(2, ship.getRightMostPos());

        ship.addTestPosition(new Position(3,0));
        ship.addTestPosition(new Position(0,5)); // <-- ADICIONADO para cobrir Ramo de getTopMostPos

        // agora (1,1), (1,2), (3,0), (0,5)
        assertEquals(0, ship.getTopMostPos());   // <-- ALTERADO: (0,5) tem a menor linha
        assertEquals(3, ship.getBottomMostPos()); // <-- ALTERADO: (3,0) tem a maior linha
        assertEquals(0, ship.getLeftMostPos());   // <-- ALTERADO: (3,0) tem a menor coluna
        assertEquals(5, ship.getRightMostPos()); // <-- ALTERADO: (0,5) tem a maior coluna
    }

    @Test
    @DisplayName("toString usa category, bearing e posição inicial")
    void testToStringFormat() {
        String expected = "[TestShip n Linha = 1 Coluna = 1]";
        assertEquals(expected, ship.toString());
    }

    // --- Testes de casos limite adicionados ---

    @Test
    void testStillFloatingWhenNoPositions() {
        TestShip s = new TestShip("Empty", Compass.NORTH, new Position(0,0));
        // implementação atual devolve false quando tamanho == 0
        assertFalse(s.stillFloating());
    }

    @Test
    void testExtremesThrowWhenNoPositions() {
        TestShip s = new TestShip("Empty", Compass.NORTH, new Position(0,0));
        assertThrows(IndexOutOfBoundsException.class, s::getTopMostPos);
        assertThrows(IndexOutOfBoundsException.class, s::getBottomMostPos);
        assertThrows(IndexOutOfBoundsException.class, s::getLeftMostPos);
        assertThrows(IndexOutOfBoundsException.class, s::getRightMostPos);
    }

    @Test
    void testTooCloseToWithOtherHavingNoPositions() {
        TestShip s = new TestShip("S", Compass.NORTH, new Position(1,1));
        s.addTestPosition(new Position(1,1));

        TestShip otherEmpty = new TestShip("O", Compass.EAST, new Position(5,5));
        // otherEmpty tem zero posições -> iterator vazio -> deve devolver false
        assertFalse(s.tooCloseTo(otherEmpty));
    }

    @Test
    void testBuildShipUnknownCaseSensitivity() {
        Position pos = new Position(0,0);
        // factory usa constantes em lower-case; string com maiúscula não deve ser reconhecida
        assertNull(Ship.buildShip("Barca", Compass.NORTH, pos));
    }

    @Test
    void testExtremesWithSinglePosition() {
        TestShip s = new TestShip("Single", Compass.SOUTH, new Position(2,3));
        s.addTestPosition(new Position(2,3));
        assertEquals(2, s.getTopMostPos());
        assertEquals(2, s.getBottomMostPos());
        assertEquals(3, s.getLeftMostPos());
        assertEquals(3, s.getRightMostPos());
    }

    // --- ADICIONADO: Testes para cobrir asserções (Requer -ea VM option) ---
    @Test
    @DisplayName("Cobertura de Asserções (requer -ea)")
    void testAssertionCoverage() {
        // Testar construtor
        assertThrows(AssertionError.class, () ->
                        new TestShip("Test", null, new Position(0,0)),
                "Assert 'bearing != null' deve falhar");

        assertThrows(AssertionError.class, () ->
                        new TestShip("Test", Compass.NORTH, null),
                "Assert 'pos != null' do construtor deve falhar");

        // Testar occupies

        assertThrows(AssertionError.class, () ->
                        ship.occupies(null),
                "Assert 'pos != null' de occupies deve falhar");

        // Testar tooCloseTo
        assertThrows(AssertionError.class, () ->
                        ship.tooCloseTo((IShip) null),
                "Assert 'other != null' de tooCloseTo deve falhar");

        // Testar shoot
        assertThrows(AssertionError.class, () ->
                        ship.shoot(null),
                "Assert 'pos != null' de shoot deve falhar");
    }
}
