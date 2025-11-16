package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GalleonTest {
    private Galleon galleonInstance;
    private final IPosition mockPos = new Position(10, 10);
    private final Integer SHIP_SIZE = 5;
    private final String SHIP_NAME = "Galeao";

    @BeforeEach
    void setUp() {
        try {
            galleonInstance = new Galleon(Compass.EAST, mockPos);
        } catch (Exception e) {
        }
    }

    @AfterEach
    void tearDown() {
        galleonInstance = null;
    }

    @DisplayName("Teste do construtor Galleon - NULL")
    @Test
    void Galleon1() {
        assertThrows(AssertionError.class, () -> { //AssertionError porque a chamada super torna inacessível a verificação de NullPointerException
            new Caravel(null, mockPos);
        }, "Error: expected AssertionError (originating from the base Ship constructor) to be thrown when bearing is null.");
    }

    @DisplayName("Teste do construtor Galleon - NORTH")
    @Test
    void Galleon2() throws Exception {
        galleonInstance = new Galleon(Compass.NORTH, mockPos);
        List<IPosition> positions = galleonInstance.getPositions();

        // Shape: (10, 10), (10, 11), (10, 12), (11, 11), (12, 11)
        List<IPosition> expectedPositions = Arrays.asList(
                new Position(10, 10), new Position(10, 11), new Position(10, 12),
                new Position(11, 11), new Position(12, 11)
        );

        assertAll(
                "Galleon NORTH Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para NORTH estavam incorretas.")
        );
    }


    @DisplayName("Teste do construtor Galleon - SOUTH")
    @Test
    void Galleon3() throws Exception {
        galleonInstance = new Galleon(Compass.SOUTH, mockPos);
        List<IPosition> positions = galleonInstance.getPositions();

        // Shape: (10, 10), (11, 10), (12, 9), (12, 10), (12, 11)
        List<IPosition> expectedPositions = Arrays.asList(
                new Position(10, 10), new Position(11, 10),
                new Position(12, 9), new Position(12, 10), new Position(12, 11)
        );

        assertAll(
                "Galleon SOUTH Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para SOUTH estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Galleon - EAST")
    @Test
    void Galleon4() throws Exception {
        galleonInstance = new Galleon(Compass.EAST, mockPos);
        List<IPosition> positions = galleonInstance.getPositions();

        // Shape: (10, 10), (11, 8), (11, 9), (11, 10), (12, 10)
        List<IPosition> expectedPositions = Arrays.asList(
                new Position(10, 10),
                new Position(11, 8), new Position(11, 9), new Position(11, 10),
                new Position(12, 10)
        );

        assertAll(
                "Galleon EAST Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para EAST estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Galleon - WEST")
    @Test
    void Galleon5() throws Exception {
        galleonInstance = new Galleon(Compass.WEST, mockPos);
        List<IPosition> positions = galleonInstance.getPositions();

        // Shape: (10, 10), (11, 10), (11, 11), (11, 12), (12, 10)
        List<IPosition> expectedPositions = Arrays.asList(
                new Position(10, 10),
                new Position(11, 10), new Position(11, 11), new Position(11, 12),
                new Position(12, 10)
        );

        assertAll(
                "Galleon WEST Positioning Check",
                () -> assertEquals(SHIP_SIZE, positions.size(), "Error: expected size " + SHIP_SIZE + ", but got " + positions.size()),
                () -> assertEquals(expectedPositions, positions, "Error: positions geradas para WEST estavam incorretas.")
        );
    }

    @DisplayName("Teste do construtor Galleon - UNKNOWN")
    @Test
    void Galleon6() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Galleon(Compass.UNKNOWN, mockPos);
        }, "Error: expected IllegalArgumentException to be thrown for unhandled bearing.");

        assertEquals("ERROR! invalid bearing for the galleon", exception.getMessage(),
                "Error: expected specific IllegalArgumentException message but got " + exception.getMessage());
    }

    @DisplayName("Galleon7: Construtor - Inicialização da Superclasse")
    @Test
    void Galleon7() throws Exception {
        galleonInstance = new Galleon(Compass.NORTH, mockPos);

        assertAll(
                "Galleon Base Attributes Check",
                () -> assertEquals(SHIP_NAME, galleonInstance.getName(), "Error: expected ship name to be " + SHIP_NAME),
                () -> assertEquals(Compass.NORTH, galleonInstance.getBearing(), "Error: expected bearing NORTH")
        );
    }

    @DisplayName("Teste do método getSize()")
    @Test
    void getSize() {
        Integer expectedSize = SHIP_SIZE;
        Integer actualSize = galleonInstance.getSize();
        assertEquals(expectedSize, actualSize, "Error: expected size 5 but got " + actualSize);
    }
}