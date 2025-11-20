package iscteiul.ista;

import iscteiul.ista.battleship.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Análise Global de Cobertura de Código.
 * Estratégia organizada de acordo com as métricas exigidas no Ponto D:
 * (a) Instrução, (b) Decisão, (c) Condição, (d) Caminho, (e) Método.
 */
public class AppTest {

    // --- (a) Métrica: INSTRUÇÃO (Statement Coverage) ---
    // Objetivo: Alcançar qualquer instrução, garantindo que o código é executado pelo menos uma vez.
    @Nested
    @DisplayName("(a) Cobertura de Instrução")
    class InstructionCoverage {

        @Test
        @DisplayName("App: Executar instruções de arranque")
        public void testAppInstructions() {
            App app = new App();
            assertNotNull(app);
        }

        @Test
        @DisplayName("Game: Executar instruções de impressão (Loops de visualização)")
        void testPrintingInstructions() {
            Fleet fleet = new Fleet();
            fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));
            Game game = new Game(fleet);

            // Estas chamadas garantem que as instruções dentro dos loops 'for'
            // dos métodos de print são executadas.
            game.printFleet();
            game.printValidShots();
        }

        @Test
        @DisplayName("Compass: Executar instruções do Enum")
        void testCompassInstructions() {
            // Garante que as instruções values() e valueOf() geradas pelo compilador correm.
            for(Compass c : Compass.values()) {
                assertNotNull(c.name());
            }
        }
    }

    // --- (e) Métrica: MÉTODO (Method Coverage) ---
    // Objetivo: Avaliar de forma independente qualquer objetivo ou comportamento (Getters/Setters/Construtores).
    @Nested
    @DisplayName("(e) Cobertura de Método")
    class MethodCoverage {

        @Test
        @DisplayName("Game: Avaliar comportamento independente (Construtor e Getters)")
        void testGameMethods() {
            Fleet fleet = new Fleet();
            Game game = new Game(fleet);

            // Avalia se cada método cumpre o seu objetivo isolado
            assertNotNull(game, "Construtor deve criar objeto");
            assertNotNull(game.getShots(), "getShots deve retornar lista");
            assertEquals(0, game.getInvalidShots(), "getInvalidShots deve iniciar a 0");
            assertEquals(0, game.getRepeatedShots(), "getRepeatedShots deve iniciar a 0");
            assertEquals(0, game.getHits(), "getHits deve iniciar a 0");
            assertEquals(0, game.getSunkShips(), "getSunkShips deve iniciar a 0");
            assertEquals(0, game.getRemainingShips(), "getRemainingShips deve iniciar a 0");
        }
    }

    // --- (b) e (c) Métricas: DECISÃO (Branch) e CONDIÇÃO ---
    // Objetivo: Avaliar qualquer ramo de decisão (if/else) e condições booleanas.
    @Nested
    @DisplayName("(b)/(c) Cobertura de Decisão e Condição")
    class BranchCoverage {

        @Test
        @DisplayName("Game: Avaliar ramos de decisão no disparo (Fire)")
        void testFireDecisions() {
            Fleet fleet = new Fleet();
            fleet.addShip(new Barge(Compass.NORTH, new Position(5, 5)));
            Game game = new Game(fleet);

            // Decisão 1: if (!validShot) -> TRUE
            game.fire(new Position(-1, -1));
            assertEquals(1, game.getInvalidShots());

            // Decisão 2: if (!validShot) -> FALSE, if (repeatedShot) -> FALSE, if (s != null) -> FALSE
            // (Tiro válido, novo, na água)
            game.fire(new Position(0, 0));
            assertEquals(0, game.getHits());

            // Decisão 3: if (repeatedShot) -> TRUE
            game.fire(new Position(0, 0));
            assertEquals(1, game.getRepeatedShots());

            // Decisão 4: if (s != null) -> TRUE
            // (Tiro no navio - Hit)
            IShip result = game.fire(new Position(5, 5));
            assertNotNull(result);
        }
    }

    // --- (d) Métrica: CAMINHO (Path Coverage) ---
    // Objetivo: Seguir um caminho distinto e completo de execução (Cenário de Jogo).
    @Nested
    @DisplayName("(d) Cobertura de Caminho")
    class PathCoverage {

        @Test
        @DisplayName("Caminho: Ciclo de vida completo de um navio (Criar -> Atingir -> Afundar)")
        void testShipLifecyclePath() {
            // Define o caminho de execução
            Fleet fleet = new Fleet();
            Position pos = new Position(1, 1);
            fleet.addShip(new Barge(Compass.NORTH, pos));
            Game game = new Game(fleet);

            // Executa o caminho
            IShip result = game.fire(pos);

            // Verifica o estado final do caminho
            assertNotNull(result, "O caminho deve terminar com o navio retornado");
            assertEquals(1, game.getSunkShips(), "O caminho deve resultar num afundanço");
            assertEquals(1, game.getHits(), "O caminho deve contabilizar um hit");
        }
    }
}