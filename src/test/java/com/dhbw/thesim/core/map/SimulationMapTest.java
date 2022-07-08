package com.dhbw.thesim.core.map;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Objects;

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

    @BeforeEach
    void setUp() {
        spriteLibraryMock = mock(SpriteLibrary.class);
        when(spriteLibraryMock.getImage(anyString())).thenReturn(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testPlant.png"))));
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

    @Test
    void getWorldPosition() {
    }

    @Test
    void getTileAtPosition() {
    }

    @Test
    void testGetTileAtPosition() {
    }

    @Test
    void tileMatchedConditions() {
    }

    @Test
    void testTileMatchedConditions() {
    }

    @Test
    void getRandomTile() {
    }

    @Test
    void getRandomTileWhereConditionsAre() {
    }

    @Test
    void getRandomTileCenterPosition() {
    }

    @Test
    void checkIfNeighborTilesMatchConditions() {
    }

    @Test
    void checkIfNeighborTilesHasConditions() {
    }

    @Test
    void getRandomTileCenterPositionWhereConditionsAre() {
    }

    @Test
    void getMidCoordinatesTilesWhereConditionsAre() {
    }

    @Test
    void getMidCoordinatesOfTilesWhereConditionsMatch() {
    }
}