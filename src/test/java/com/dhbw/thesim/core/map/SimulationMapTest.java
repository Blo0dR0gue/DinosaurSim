package com.dhbw.thesim.core.map;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.image.Image;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link SimulationMap} class.
 *
 * @author Daniel Czeschner
 */
class SimulationMapTest {

    SimulationMap simulationMap;
    SpriteLibrary spriteLibraryMock;
    Image testImage;

    @BeforeEach
    void setUp() {
        testImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testPlant.png")));
        spriteLibraryMock = mock(SpriteLibrary.class);
        when(spriteLibraryMock.getImage(anyString())).thenReturn(testImage);
        simulationMap = new SimulationMap(SimulationMap.LANDSCAPE_ONE_NAME, spriteLibraryMock);
    }

    @AfterEach
    void tearDown() {
        simulationMap = null;
        spriteLibraryMock = null;
    }

    @DisplayName("Check if a grid coordinate is inside the grid.")
    @ParameterizedTest
    @CsvSource({"0, 0, true", "-1, 0, false", "35, 23, true", "36, 24, false", "36, 10, false", "10, 24, false"})
    void isInsideOfGrid(int gridX, int gridY, boolean expected) {
        //arrange

        //act
        boolean isInsideOfGrid = simulationMap.isInsideOfGrid(gridX, gridY);
        //assert
        assertEquals(expected, isInsideOfGrid, "The check should be correct.");
    }

    @DisplayName("Check if a vector is inside the grid.")
    @ParameterizedTest
    @CsvSource({"0, 0, true", "1960, 1080, false", "35, 23, true", "36, 24, true", "1619, 1079, true", "1620, 1080, false", "1961, 1080, false", "-4, 50, false"})
    void isVectorInsideOfGrid(int x, int y, boolean expected) {
        //arrange
        Vector2D vector2D = new Vector2D(x, y);
        //act
        boolean isInsideOfGrid = simulationMap.isInsideOfGrid(vector2D);
        //assert
        assertEquals(expected, isInsideOfGrid, "The check should be correct.");
    }

    @DisplayName("Get world position")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0", "-1, 0, -1, -1", "35, 23, 1575, 1035", "36, 24, -1, -1", "36, 10, -1, -1", "10, 24, -1, -1"})
    void getWorldPosition(int gridX, int gridY, int expectedX, int expectedY) {
        //arrange

        //act
        Vector2D worldPosition = simulationMap.getWorldPosition(gridX, gridY);
        //assert
        if (simulationMap.isInsideOfGrid(gridX, gridY)) {
            assertAll("Check values",
                    () -> assertNotNull(worldPosition),
                    () -> assertEquals(expectedX, worldPosition.getX(), "X need to be equal"),
                    () -> assertEquals(expectedY, worldPosition.getY(), "Y need to be equal"));
        } else {
            assertNull(worldPosition);
        }
    }

    @DisplayName("Check if a grid coordinate is inside the grid.")
    @ParameterizedTest
    @CsvSource({"0, 0", "-1, 0", "35, 23", "36, 24", "36, 10", "10, 24"})
    void getTileAtPosition(int gridX, int gridY) {
        //arrange

        //act
        Tile tileAt = simulationMap.getTileAtPosition(gridX, gridY);
        //assert
        if (simulationMap.isInsideOfGrid(gridX, gridY)) {
            assertAll("",
                    () -> assertEquals(gridX, tileAt.getGridX(), "X should be equal"),
                    () -> assertEquals(gridY, tileAt.getGridY(), "Y should be equal"));
        } else {
            assertNull(tileAt, "Tile should be null");
        }
    }

    @DisplayName("Check if a grid coordinate is inside the grid.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0", "1960, 1080, -1, -1", "35, 23, 0, 0", "36, 46, 0, 1", "1619, 1079, 35, 23", "1620, 1080, -1, -1", "1961, 1080, -1, -1", "-4, 50, -1, -1"})
    void getTileAtVector(int x, int y, int gridX, int gridY) {
        //arrange
        Vector2D vector2D = new Vector2D(x, y);
        //act
        Tile tileAt = simulationMap.getTileAtPosition(vector2D);
        //assert
        if (simulationMap.isInsideOfGrid(vector2D)) {
            assertAll("Check tile gird position",
                    () -> assertEquals(gridX, tileAt.getGridX(), "X should be equal"),
                    () -> assertEquals(gridY, tileAt.getGridY(), "Y should be equal"));
        } else {
            assertNull(tileAt, "Tile should be null");
        }
    }

    @DisplayName("Check if a tile has specific conditions.")
    @ParameterizedTest
    @CsvSource({"0, 0, false, false, false", "-1, 0, false, false, false", "0, 1, false, true, true", "0, 1, false, false, false", "0, 3, true, true, true", "0, 2, false, false, true"})
    void tileMatchedConditions(int gridX, int gridY, boolean canSwim, boolean canClimb, boolean expected) {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, false, true, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, false, false, true);
        simulationMap.getTiles()[0][3] = new Tile(testImage, 0, 3, false, false, false);
        //act
        boolean tileMatchedCondition = simulationMap.tileMatchedConditions(gridX, gridY, canSwim, canClimb);
        //assert
        assertEquals(expected, tileMatchedCondition);
    }

    @DisplayName("Get a random tile")
    @ParameterizedTest
    @CsvSource({"false, false", "true, false", "false, true", "true, true"})
    void getRandomTile(boolean canSwim, boolean canClimb) {
        //act
        Tile tile = simulationMap.getRandomTile(canSwim, canClimb, new Random());
        //assert
        if (canClimb && canSwim)
            assertNull(tile);
        else
            assertNotNull(tile);
    }

    @DisplayName("Check if a tile has specific conditions.")
    @ParameterizedTest
    @CsvSource({"false, false, false", "true, false, false", "false, true, false", "true, true, false"})
    void getRandomTileWhereConditionsAre(boolean canSwim, boolean canClimb, boolean allowPlants) {
        //act
        Tile tile = simulationMap.getRandomTileWhereConditionsAre(canSwim, canClimb, allowPlants, new Random());
        //assert
        if ((canSwim || canClimb) && allowPlants || canSwim && canClimb) {
            assertNull(tile);
        } else {
            assertAll("Check conditions",
                    () -> assertNotNull(tile),
                    () -> assertEquals(canSwim, tile.isSwimmable(), "Can swim"),
                    () -> assertEquals(canClimb, tile.isClimbable(), "Can climb"),
                    () -> assertEquals(allowPlants, tile.arePlantsAllowed(), "Allow plants")
            );
        }
    }

    @Test
    void getRandomTileCenterPosition() {
        //arrange

        //act
        Vector2D center = simulationMap.getRandomTileCenterPosition(true, true, new Random());
        //assert
        assertNull(center);
    }

    @DisplayName("Check Neighbor conditions.")
    @ParameterizedTest
    @CsvSource({"false, false, false", "true, false, false", "false, true, false", "true, true, true"})
    void checkIfNeighborTilesMatchConditions(boolean canSwim, boolean canClimb, boolean expected) {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, false, true, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, false, false, true);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, false, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, false, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, false, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, false, false, true);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, false, true, false);
        //act
        boolean check = simulationMap.checkIfNeighborTilesMatchConditions(simulationMap.getWorldPosition(1, 1), canSwim, canClimb, Tile.TILE_SIZE + 5);
        //assert
        assertEquals(expected, check);
    }

    @DisplayName("Check Neighbor has conditions. (false)")
    @ParameterizedTest
    @CsvSource({"false, false, false", "true, false, false", "false, true, false", "true, true, true", "true, true, true"})
    void checkIfNeighborTilesHasConditions(boolean canSwim, boolean canClimb, boolean allowPlants) {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, false, true, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, false, false, true);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, false, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, false, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, false, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, false, false, true);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, false, true, false);
        //act
        boolean check = simulationMap.checkIfNeighborTilesHasConditions(simulationMap.getWorldPosition(1, 1), canSwim, canClimb, allowPlants, Tile.TILE_SIZE + 5);
        //assert
        assertFalse(check);
    }

    @DisplayName("Check Neighbor has conditions. (true)")
    @Test
    void checkIfNeighborTilesHasConditionsSuccess() {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, true, false, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, true, false, false);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, true, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, true, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, false, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, true, false, false);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, true, false, false);
        simulationMap.getTiles()[1][2] = new Tile(testImage, 1, 2, true, false, false);
        //act
        boolean check = simulationMap.checkIfNeighborTilesHasConditions(simulationMap.getWorldPosition(1, 1), true, false, false, Tile.TILE_SIZE + 5);
        //assert
        assertTrue(check);
    }

    @Disabled("Get a centered position of a random tile, where the conditions are as defined.")
    @ParameterizedTest
    @CsvSource({"false, false, false", "true, false, false", "false, true, false", "true, true, true", "true, true, true"})
    void getRandomTileCenterPositionWhereConditionsAre(boolean canSwim, boolean canClimb, boolean allowPlants) {
        //arrange

        //act
        Vector2D centerPosition = simulationMap.getRandomTileCenterPositionWhereConditionsAre(canSwim, canClimb, allowPlants, new Random());
        //assert
        if ((canSwim || canClimb) && allowPlants || canSwim && canClimb) {
            assertNull(centerPosition, "Should be null, if a invalid conditions is given.");
        } else {
            assertAll("Check vector conditions",
                    () -> assertNotNull(centerPosition),
                    () -> assertEquals(canClimb, simulationMap.getTileAtPosition(centerPosition).isClimbable(), "Climb condition needs to be equal."),
                    () -> assertEquals(canSwim, simulationMap.getTileAtPosition(centerPosition).isClimbable(), "Swim condition needs to be equal."),
                    () -> assertEquals(allowPlants, simulationMap.getTileAtPosition(centerPosition).isClimbable(), "Plant condition needs to be equal.")
            );
        }
    }

    @Disabled("Get the 8 neighbours with the conditions")
    @Test
    void getMidCoordinatesTilesWhereConditionsAre() {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, true, false, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, true, false, false);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, true, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, true, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, false, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, true, false, false);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, true, false, false);
        simulationMap.getTiles()[1][2] = new Tile(testImage, 1, 2, true, false, false);
        //act
        List<Vector2D> tiles = simulationMap.getMidCoordinatesTilesWhereConditionsAre(simulationMap.getWorldPosition(1, 1), Tile.TILE_SIZE, true, false);
        //assert
        assertEquals(8, tiles.size());
    }

    @Disabled("Get the 3 neighbours with the conditions")
    @Test
    void getMidCoordinatesTilesWhereConditionsAreOnly3() {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, false, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, false, false, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, true, false, false);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, true, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, true, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, true, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, false, false, false);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, false, false, false);
        simulationMap.getTiles()[1][2] = new Tile(testImage, 1, 2, false, false, false);
        //act
        List<Vector2D> tiles = simulationMap.getMidCoordinatesTilesWhereConditionsAre(simulationMap.getWorldPosition(1, 1), Tile.TILE_SIZE, true, false);
        //assert
        assertEquals(3, tiles.size());
    }


    @Disabled("Get the 8 neighbours where the conditions match")
    @Test
    void getMidCoordinatesOfTilesWhereConditionsMatch() {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, false, false, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, true, false, false);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, true, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, false, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, false, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, true, false, false);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, true, false, false);
        simulationMap.getTiles()[1][2] = new Tile(testImage, 1, 2, true, false, false);
        //act
        List<Vector2D> tiles = simulationMap.getMidCoordinatesOfTilesWhereConditionsMatch(simulationMap.getWorldPosition(1, 1), Tile.TILE_SIZE, true, false);
        //assert
        assertEquals(8, tiles.size());
    }

    @Disabled("Get the 3 neighbours with the conditions match")
    @Test
    void getMidCoordinatesOfTilesWhereConditionsMatchOnly3() {
        //arrange
        simulationMap.getTiles()[0][0] = new Tile(testImage, 0, 0, true, false, false);
        simulationMap.getTiles()[0][1] = new Tile(testImage, 0, 1, false, false, false);
        simulationMap.getTiles()[0][2] = new Tile(testImage, 0, 2, true, false, false);
        simulationMap.getTiles()[1][0] = new Tile(testImage, 1, 0, true, false, false);
        simulationMap.getTiles()[2][0] = new Tile(testImage, 2, 0, true, false, false);
        simulationMap.getTiles()[2][1] = new Tile(testImage, 2, 1, true, true, false);
        simulationMap.getTiles()[2][2] = new Tile(testImage, 2, 2, false, false, false);
        simulationMap.getTiles()[1][1] = new Tile(testImage, 1, 1, false, false, false);
        simulationMap.getTiles()[1][2] = new Tile(testImage, 1, 2, false, false, false);
        //act
        List<Vector2D> tiles = simulationMap.getMidCoordinatesOfTilesWhereConditionsMatch(simulationMap.getWorldPosition(1, 1), Tile.TILE_SIZE, false, false);
        //assert
        assertEquals(3, tiles.size());
    }

}