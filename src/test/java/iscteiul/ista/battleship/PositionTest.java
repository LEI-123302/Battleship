package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Position")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionTest {

    private Position p;

    @BeforeAll
    void setupAll() {
        System.out.println("Início da suite de testes de Position");
    }

    @BeforeEach
    void setup() {
        p = new Position(2, 3);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Teste concluído.");
    }

    @AfterAll
    void tearDownAll() {
        System.out.println("Fim da suite Position");
    }

    @Test
    @DisplayName("Construtor + getters funcionam")
    void testConstructorAndGetters() {
        assertAll(
                () -> assertEquals(2, p.getRow()),
                () -> assertEquals(3, p.getColumn()),
                () -> assertFalse(p.isHit()),
                () -> assertFalse(p.isOccupied())
        );
    }

    @Nested
    @DisplayName("Testes de igualdade")
    class EqualityTests {

        @Test
        @DisplayName("equals funciona para o mesmo objeto")
        void testEqualsSameObject() {
            assertEquals(p, p, "Mesmo objeto deve ser igual a si mesmo");
        }

        @Test
        @DisplayName("equals funciona para posições iguais")
        void testEqualsEqualPositions() {
            Position other = new Position(2, 3);
            assertEquals(p, other, "Posições com mesma linha e coluna devem ser iguais");
        }

        @Test
        @DisplayName("equals retorna false para posições diferentes")
        void testEqualsDifferentPositions() {
            Position pos3 = new Position(3, 3);
            Position pos4 = new Position(2, 2);
            Position pos5 = new Position(4, 2);
            assertNotEquals(p, pos3);
            assertNotEquals(p, pos4);
            assertNotEquals(p, pos5);
        }

        @Test
        @DisplayName("equals retorna false para null ou outro tipo")
        void testEqualsNullAndOtherType() {
            assertNotEquals(p, null);
            assertNotEquals(p, "texto");
            assertNotEquals(p, Boolean.FALSE);
        }

        @Test
        @DisplayName("hashCode muda se estado interno muda")
        void testHashCodeConsistency() {
            Position other = new Position(2, 3);
            // hashCode igual para estados iguais
            assertEquals(p.hashCode(), other.hashCode());
            // hashCode diferente se isHit mudar
            other.shoot();
            assertNotEquals(p.hashCode(), other.hashCode());
            // hashCode diferente se isOccupied mudar
            Position another = new Position(2, 3);
            another.occupy();
            assertNotEquals(p.hashCode(), another.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de adjacência")
    class AdjacencyTests {

        @ParameterizedTest
        @DisplayName("Posições adjacentes retornam true")
        @CsvSource({
                "1,2",
                "2,2",
                "3,2",
                "1,3",
                "3,3",
                "1,4",
                "2,4",
                "3,4"
        })
        void testIsAdjacentTrue(int r, int c) {
            Position adj = new Position(r, c);
            assertTrue(p.isAdjacentTo(adj));
        }

        @ParameterizedTest
        @DisplayName("Posições NÃO adjacentes retornam false")
        @CsvSource({
                "0,0",
                "5,5",
                "10,10",
                "2,6",
                "0,0,0,1",
                "3,3,4,5",
                "0,0,3,0"
        })
        void testIsAdjacentFalse(int r, int c) {
            Position far = new Position(r, c);
            assertFalse(p.isAdjacentTo(far));
        }
    }

    @Nested
    @DisplayName("Ocupar e alvejar")
    class StateTests {

        @Test
        @DisplayName("occupy() marca posição como ocupada")
        void testOccupy() {
            assertFalse(p.isOccupied());
            p.occupy();
            assertTrue(p.isOccupied());
        }

        @Test
        @DisplayName("shoot() marca posição como atingida")
        void testShoot() {
            assertFalse(p.isHit());
            p.shoot();
            assertTrue(p.isHit());
        }
    }

    @Test
    @DisplayName("toString retorna representação esperada")
    void testToString() {
        assertEquals("Linha = 2 Coluna = 3", p.toString());
    }
}
