package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class CaravelTest {
    private Caravel caravelInstance;
    private final IPosition mockPos = new Position(10, 20);

    @BeforeEach
    void setUp() {
        try {
            caravelInstance = new Caravel(Compass.NORTH, mockPos);
        } catch (Exception e) {
        }
    }

    @AfterEach
    void tearDown() {
        caravelInstance = null;
    }

    @DisplayName("Teste do construtor Caravel")
    @Test
    void Caravel1() {
        assertThrows(AssertionError.class, () -> { //AssertionError porque a chamada super torna inacessível a verificação de NullPointerException
            new Caravel(null, mockPos);
        }, "Error: expected AssertionError (originating from the base Ship constructor) to be thrown when bearing is null.");
    }

  @DisplayName("Teste do construtor Caravel - NORTH")
    @Test
    void Caravel2() throws Exception {
        caravelInstance = new Caravel(Compass.NORTH, mockPos);
        List<IPosition> positions = caravelInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(10, 20), // pos.getRow() + 0
                new Position(11, 20)  // pos.getRow() + 1
        );

        assertAll(
                "Caravel NORTH Positioning Check",
                () -> assertEquals(2, positions.size(), "Error: expected 2 positions for a size 2 ship, but got " + positions.size()),
                () -> assertEquals(expectedPositions.get(0), positions.get(0), "Error: expected first position (10, 20) but got a different position."),
                () -> assertEquals(expectedPositions.get(1), positions.get(1), "Error: expected second position (11, 20) but got a different position.")
        );
    }

    @DisplayName("Teste do construtor Caravel - SOUTH")
    @Test
    void Caravel3() throws Exception {
        caravelInstance = new Caravel(Compass.SOUTH, mockPos);
        List<IPosition> positions = caravelInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(10, 20), // pos.getRow() + 0
                new Position(11, 20)  // pos.getRow() + 1
        );

        assertAll(
                "Caravel SOUTH Positioning Check",
                () -> assertEquals(2, positions.size(), "Error: expected 2 positions for a size 2 ship, but got " + positions.size()),
                () -> assertEquals(expectedPositions.get(0), positions.get(0), "Error: expected first position (10, 20) but got a different position."),
                () -> assertEquals(expectedPositions.get(1), positions.get(1), "Error: expected second position (11, 20) but got a different position.")
        );
    }

    @DisplayName("Teste do construtor Caravel - EAST")
    @Test
    void Caravel4() throws Exception {
        caravelInstance = new Caravel(Compass.EAST, mockPos);
        List<IPosition> positions = caravelInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(10, 20), // pos.getColumn() + 0
                new Position(10, 21)  // pos.getColumn() + 1
        );

        assertAll(
                "Caravel EAST Positioning Check",
                () -> assertEquals(2, positions.size(), "Error: expected 2 positions for a size 2 ship, but got " + positions.size()),
                () -> assertEquals(expectedPositions.get(0), positions.get(0), "Error: expected first position (10, 20) but got a different position."),
                () -> assertEquals(expectedPositions.get(1), positions.get(1), "Error: expected second position (10, 21) but got a different position.")
        );
    }

    @DisplayName("Teste do construtor Caravel - WEST")
    @Test
    void Caravel5() throws Exception {
        caravelInstance = new Caravel(Compass.WEST, mockPos);
        List<IPosition> positions = caravelInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(10, 20), // pos.getColumn() + 0
                new Position(10, 21)  // pos.getColumn() + 1
        );

        assertAll(
                "Caravel WEST Positioning Check",
                () -> assertEquals(2, positions.size(), "Error: expected 2 positions for a size 2 ship, but got " + positions.size()),
                () -> assertEquals(expectedPositions.get(0), positions.get(0), "Error: expected first position (10, 20) but got a different position."),
                () -> assertEquals(expectedPositions.get(1), positions.get(1), "Error: expected second position (10, 21) but got a different position.")
        );
    }

    @DisplayName("Teste do construtor Caravel - UNKNOWN")
    @Test
    void Caravel6() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Caravel(Compass.UNKNOWN, mockPos);
        }, "Error: expected IllegalArgumentException to be thrown when bearing is OTHER.");

        assertEquals("ERROR! invalid bearing for the caravel", exception.getMessage(),
                "Error: expected specific IllegalArgumentException message but got " + exception.getMessage());
    }

    @DisplayName("Teste do método getSize()")
    @Test
    void getSize() {
        Integer expectedSize = 2;
        Integer actualSize = caravelInstance.getSize();
        assertEquals(expectedSize, actualSize, "Error: expected size 2 but got " + actualSize);
    }
}