package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FrigateTest {
    private Frigate frigateInstance;
    private final IPosition mockPos = new Position(2, 2);
    private final Integer SHIP_SIZE = 4;

    @BeforeEach
    void setUp() {
        try {
            frigateInstance = new Frigate(Compass.NORTH, mockPos);
        } catch (Exception e) {
        }
    }

    @AfterEach
    void tearDown() {
        frigateInstance = null;
    }

    @DisplayName("Teste do construtor Frigate - NULL")
    @Test
    void Frigate1() {
        assertThrows(AssertionError.class, () -> {
            new Frigate(null, mockPos);
        }, "Error: expected an AssertionError (originado no construtor Ship) quando bearing é null.");
    }

    @DisplayName("Teste do construtor Frigate - NORTH")
    @Test
    void Frigate2() throws Exception {
        frigateInstance = new Frigate(Compass.NORTH, mockPos);
        List<IPosition> positions = frigateInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(2, 2), // r=0
                new Position(3, 2), // r=1
                new Position(4, 2), // r=2
                new Position(5, 2)  // r=3
        );

        assertAll(
                "Frigate NORTH Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para NORTH estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Frigate - SOUTH")
    @Test
    void Frigate3() throws Exception {
        frigateInstance = new Frigate(Compass.SOUTH, mockPos);
        List<IPosition> positions = frigateInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(2, 2), // r=0
                new Position(3, 2), // r=1
                new Position(4, 2), // r=2
                new Position(5, 2)  // r=3
        );

        assertAll(
                "Frigate SOUTH Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para SOUTH estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Frigate - EAST")
    @Test
    void Frigate4() throws Exception {
        frigateInstance = new Frigate(Compass.EAST, mockPos);
        List<IPosition> positions = frigateInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(2, 2), // c=0
                new Position(2, 3), // c=1
                new Position(2, 4), // c=2
                new Position(2, 5)  // c=3
        );

        assertAll(
                "Frigate EAST Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para EAST estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Frigate - WEST")
    @Test
    void Frigate5() throws Exception {
        frigateInstance = new Frigate(Compass.WEST, mockPos);
        List<IPosition> positions = frigateInstance.getPositions();

        List<IPosition> expectedPositions = List.of(
                new Position(2, 2), // c=0
                new Position(2, 3), // c=1
                new Position(2, 4), // c=2
                new Position(2, 5)  // c=3
        );

        assertAll(
                "Frigate WEST Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para WEST estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Frigate - UNKNOWN")
    @Test
    void Frigate6() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Frigate(Compass.UNKNOWN, mockPos);
        }, "Error: expected IllegalArgumentException to be thrown for unhandled bearing.");

        assertEquals("ERROR! invalid bearing for thr frigate", exception.getMessage(),
                "Error: expected specific IllegalArgumentException message but got " + exception.getMessage());
    }

    @DisplayName("Teste do método getSize()")
    @Test
    void getSize() {
        Integer expectedSize = SHIP_SIZE;
        Integer actualSize = frigateInstance.getSize();
        assertEquals(expectedSize, actualSize, "Error: expected size 4 but got " + actualSize);
    }
}