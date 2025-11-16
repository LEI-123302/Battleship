package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CarrackTest {
    private Carrack carrackInstance;
    private final IPosition mockPos = new Position(5, 5);
    private final Integer SHIP_SIZE = 3;

    @BeforeEach
    void setUp() {
        try {
            carrackInstance = new Carrack(Compass.EAST, mockPos);
        } catch (Exception e) {
        }
    }

    @AfterEach
    void tearDown() {
        carrackInstance = null;
    }

    @DisplayName("Teste do construtor Carrack")
    @Test
    void Carrack1() {
        assertThrows(AssertionError.class, () -> {
            new Carrack(null, mockPos);
        }, "Error: expected an AssertionError (from base Ship constructor) when bearing is null.");
    }

    @DisplayName("Teste do construtor Carrack - NORTH")
    @Test
    void Carrack2() throws Exception {
        carrackInstance = new Carrack(Compass.NORTH, mockPos);
        List<IPosition> positions = carrackInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(5, 5), // r=0
                new Position(6, 5), // r=1
                new Position(7, 5)  // r=2
        );

        assertAll(
                "Carrack NORTH Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions generated for NORTH were incorrect.")
        );
    }

    @DisplayName("Teste do construtor Carrack - SOUTH")
    @Test
    void Carrack3() throws Exception {
        carrackInstance = new Carrack(Compass.SOUTH, mockPos);
        List<IPosition> positions = carrackInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(5, 5), // r=0
                new Position(6, 5), // r=1
                new Position(7, 5)  // r=2
        );

        assertAll(
                "Carrack SOUTH Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions generated for SOUTH were incorrect.")
        );
    }

    @DisplayName("Teste do construtor Carrack - EAST")
    @Test
    void Carrack4() throws Exception {
        carrackInstance = new Carrack(Compass.EAST, mockPos);
        List<IPosition> positions = carrackInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(5, 5), // c=0
                new Position(5, 6), // c=1
                new Position(5, 7)  // c=2
        );

        assertAll(
                "Carrack EAST Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions generated for EAST were incorrect.")
        );
    }

    @DisplayName("Teste do construtor Carrack - WEST")
    @Test
    void Carrack5() throws Exception {
        carrackInstance = new Carrack(Compass.WEST, mockPos);
        List<IPosition> positions = carrackInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(5, 5), // c=0
                new Position(5, 6), // c=1
                new Position(5, 7)  // c=2
        );

        assertAll(
                "Carrack WEST Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions generated for WEST were incorrect.")
        );
    }

    @DisplayName("Teste do construtor Carrack - UNKNOWN bearing")
    @Test
    void Carrack6() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Carrack(Compass.UNKNOWN, mockPos);
        }, "Error: expected IllegalArgumentException to be thrown for unhandled bearing.");

        assertEquals("ERROR! invalid bearing for the carrack", exception.getMessage(),
                "Error: expected specific IllegalArgumentException message but got " + exception.getMessage());
    }

    @DisplayName("Teste do método getSize()")
    @Test
    void getSize() {
        Integer expectedSize = 3;
        Integer actualSize = carrackInstance.getSize();
        assertEquals(expectedSize, actualSize, "Error: expected size 3 but got " + actualSize);
    }
}