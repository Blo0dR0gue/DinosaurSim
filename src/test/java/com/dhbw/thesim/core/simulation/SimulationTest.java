package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link Simulation} class.
 *
 * @author Daniel Czeschner
 */
@ExtendWith(MockitoExtension.class)
class SimulationTest {

    @Mock
    SimulationMap simulationMap;

    @Mock
    GraphicsContext graphicsContext;

    @Mock
    SpriteLibrary spriteLibrary;

    Image testImage;

    Simulation simulation;

    List<Vector2D> testMidCoordinatesWhereConditionsAre;

    @BeforeEach
    void setUp() {
        testImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testDinosaur.png")));

        testMidCoordinatesWhereConditionsAre = new ArrayList<>();
        testMidCoordinatesWhereConditionsAre.add(new Vector2D(10, 10));
        testMidCoordinatesWhereConditionsAre.add(new Vector2D(50, 50));

        when(simulationMap.getMidCoordinatesTilesWhereConditionsAre(any(), anyDouble(), anyBoolean(), anyBoolean())).thenReturn(testMidCoordinatesWhereConditionsAre);
        simulation = new Simulation(simulationMap, graphicsContext, new Random());
    }

    @AfterEach
    void tearDown() {
        simulation = null;
        testMidCoordinatesWhereConditionsAre = null;
        testImage = null;
    }

    @DisplayName("Sort a list by the distance to a vector")
    @Test
    void sortByDistanceRandomTest() {

        List<SimulationObject> simulationObjectList = new ArrayList<>();

        Random random = new Random();

        Plant plant;
        for (int i = 0; i < 50; i++) {
            plant = mock(Plant.class);
            when(plant.getPosition()).thenReturn(new Vector2D(random.nextDouble(0, 1860), random.nextDouble(0, 1080)));
            when(plant.getInteractionRange()).thenReturn(2d);
            when(plant.getGrowthRate()).thenReturn(2d);
            simulationObjectList.add(plant);
        }

        Vector2D sortTo = new Vector2D(random.nextDouble(0, 1860), random.nextDouble(0, 1080));

        simulation.sortByDistance(sortTo, simulationObjectList);

        double lastDistance = 0;

        for (SimulationObject simulationObject : simulationObjectList) {

            double distance = Vector2D.distance(sortTo, simulationObject.getPosition());

            assertTrue(lastDistance <= distance);

            lastDistance = distance;

        }

    }

    @DisplayName("Check if the simulation is over because all dinosaurs died")
    @Test
    void isOver() {
        //arrange
        Dinosaur dinosaurMock = mock(Dinosaur.class);
        simulation.getSimulationObjects().add(dinosaurMock);
        //act
        boolean isNotOver = simulation.isOver();
        simulation.getSimulationObjects().clear();
        boolean isOver = simulation.isOver();
        //assert
        assertAll("Check if over",
                () -> assertFalse(isNotOver, "There is a dinosaur alive. (Not Over)"),
                () -> assertTrue(isOver, "All dinosaurs died. (Over)"));
    }

    @DisplayName("Get the closest water source.")
    @Test
    void getClosestReachableWaterSource() {
        //arrange
        when(simulationMap.isInsideOfGrid(any())).thenReturn(true);
        when(simulationMap.getTileAtPosition(any())).thenReturn(new Tile(testImage, 0, 0, false, false, true));
        //act
        Vector2D closestWater = simulation.getClosestReachableWaterSource(new Vector2D(0, 0), 500, true, false);
        //assert
        assertAll("Get closes Water source",
                () -> assertNotNull(closestWater, "There is a test target."),
                () -> assertEquals(10, closestWater.getX(), "Need to be the closest test data. (X)"),
                () -> assertEquals(10, closestWater.getY(), "Need to be the closest test data. (X)")
        );

    }

    @DisplayName("No water in range.")
    @Test
    void getClosestReachableWaterSourceNull() {
        //arrange
        testMidCoordinatesWhereConditionsAre.clear();
        //act
        Vector2D closestWater = simulation.getClosestReachableWaterSource(new Vector2D(0, 0), 500, true, false);
        //assert
        assertNull(closestWater, "There is a no test target.");
    }

    @Test
    void getClosestReachableFoodSourceInRange() {
        //arrange

        //act

        //assert

    }

    @Test
    void getClosestReachableSuitablePartnerInRange() {
        //arrange

        //act

        //assert

    }

    @Test
    void makeBaby() {
        //arrange

        //act

        //assert

    }

    @Test
    void getNearestPositionInMapWhereConditionsAre() {
        //arrange

        //act

        //assert

    }

    @Test
    void getFreePositionInMap() {
        //arrange

        //act

        //assert

    }

    @Test
    void getFreePositionInMapWhereConditionsAre() {
        //arrange

        //act

        //assert

    }

    @Test
    void canMoveTo() {
        //arrange

        //act

        //assert

    }

    @Test
    void getRandomMovementTargetInRange() {
        //arrange

        //act

        //assert

    }

    @Test
    void getRandomMovementTargetInRangeInDirection() {
        //arrange

        //act

        //assert

    }

    @DisplayName("Check if two circles (interaction ranges) intersect.")
    @ParameterizedTest
    @CsvSource({"3, 5, 5, 14, 18, 8, false", "2, 3, 12, 5, 5, 10, true", "17, 29, 500, 1029, 100, 300, false"})
    void doTheCirclesIntersect(double x1, double y1, double r1, double x2, double y2, double r2, boolean expected) {
        //arrange
        Vector2D circle1 = new Vector2D(x1, y1);
        Vector2D circle2 = new Vector2D(x2, y2);
        //act
        boolean doTheCirclesIntersect = simulation.doTheCirclesIntersect(circle1, r1, circle2, r2);
        //assert
        assertEquals(expected, doTheCirclesIntersect);
    }
}