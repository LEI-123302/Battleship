package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Testes para a classe Barge (Barca)")
class BargeTest {
    private Barge bargeInstance;
    private final IPosition mockPos = new Position(5, 5);
    private final Compass mockBearing = Compass.NORTH;

    @BeforeEach
    void setUp() {
        bargeInstance = new Barge(mockBearing, mockPos);
    }

    @AfterEach
    void tearDown() {
        bargeInstance = null;
    }


    @DisplayName("Teste do construtor Barge")
    @Test
    void Barge() {
        // Assertions grouped using assertAll for clarity on constructor state
        assertAll(
                "Constructor Initialization Check",
                () -> assertNotNull(bargeInstance, "Error: Barge instance should not be null after construction."),
                () -> assertEquals("Barca", bargeInstance.getName(), "Error: expected name 'Barca' but got " + bargeInstance.getName()),
                () -> assertEquals(mockBearing, ((Ship)bargeInstance).getBearing(), "Error: expected bearing NORTH but got " + ((Ship)bargeInstance).getBearing()),
                () -> assertEquals(1, bargeInstance.getPositions().size(), "Error: expected 1 position added to the list, but got " + bargeInstance.getPositions().size()),
                () -> assertEquals(new Position(5, 5), bargeInstance.getPositions().get(0), "Error: expected position (5,5) in the list, but got " + bargeInstance.getPositions().get(0))
        );
    }

    @DisplayName("Teste do método getSize()")
    @Test
    void getSize() {
        Integer expectedSize = 1;
        Integer actualSize = bargeInstance.getSize();
        assertEquals(expectedSize, actualSize, "Error: expected size 1 but got " + actualSize);
    }

}