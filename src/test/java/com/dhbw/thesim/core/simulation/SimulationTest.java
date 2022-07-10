package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    Image testImage;

    Simulation simulation;

    List<Vector2D> testMidCoordinatesWhereConditionsAre;

    @BeforeEach
    void setUp() {
        testImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testDinosaur.png")));

        testMidCoordinatesWhereConditionsAre = new ArrayList<>();
        testMidCoordinatesWhereConditionsAre.add(new Vector2D(10, 10));
        testMidCoordinatesWhereConditionsAre.add(new Vector2D(50, 50));

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
        when(simulationMap.getMidCoordinatesTilesWhereConditionsAre(any(), anyDouble(), anyBoolean(), anyBoolean())).thenReturn(testMidCoordinatesWhereConditionsAre);
        //act
        Vector2D closestWater = simulation.getClosestReachableWaterSource(new Vector2D(0, 0), 500, true, false);
        //assert
        assertAll("Get closes Water source",
                () -> assertNotNull(closestWater, "There is a test target."),
                () -> assertEquals(10, closestWater.getX(), "Need to be the closest test data. (X)"),
                () -> assertEquals(10, closestWater.getY(), "Need to be the closest test data. (Y)")
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

    @DisplayName("Get the closest food source.")
    @Test
    void getClosestReachableFoodSourceInRange() {
        //arrange
        Dinosaur testDinosaur = new Dinosaur("test", testImage, 1, 2,
                3, 4, 5, 6, 7, 8,
                true, false, 'a', 500,
                10, 'f');
        testDinosaur.setPosition(new Vector2D(10, 10));

        Dinosaur testDinosaurEat = new Dinosaur("test2", testImage, 1, 2,
                2, 4, 5, 6, 7, 8,
                true, false, 'a', 500,
                10, 'p');

        testDinosaurEat.setPosition(new Vector2D(70, 70));

        simulation.getSimulationObjects().add(testDinosaur);
        simulation.getSimulationObjects().add(testDinosaurEat);

        when(simulationMap.isInsideOfGrid(any())).thenReturn(true);
        when(simulationMap.getTileAtPosition(any())).thenReturn(new Tile(testImage, 0, 0, false, false, true));
        when(simulationMap.tileMatchedConditions(any(), anyBoolean(), anyBoolean())).thenReturn(true);

        //act
        SimulationObject closest = simulation.getClosestReachableFoodSourceInRange(testDinosaur.getPosition(), testDinosaur.getViewRange(), testDinosaur.getInteractionRange(),
                testDinosaur.getDiet(), testDinosaur.getType(), testDinosaur.canSwim(), testDinosaur.canClimb(), testDinosaur.getStrength());
        //assert
        assertAll("Check",
                () -> assertNotNull(closest, "There is a test dinosaur."),
                () -> assertEquals("test2", closest.getType(), "Closest needs to be the other test dinosaur."));
    }

    @DisplayName("Get the closest dinosaur for a partner")
    @Test
    void getClosestReachableSuitablePartnerInRange() {
        //arrange
        Dinosaur testDinosaur = new Dinosaur("test", testImage, 1, 2,
                3, 4, 5, 6, 7, 8,
                true, false, 'a', 500,
                10, 'm');

        testDinosaur.setPosition(new Vector2D(10, 10));

        Dinosaur partner = new Dinosaur("test", testImage, 1, 2,
                2, 4, 5, 6, 7, 8,
                true, false, 'a', 500,
                10, 'f');

        partner.setPosition(new Vector2D(70, 70));
        partner.setReproductionValue(Dinosaur.REPRODUCTION_VALUE_FULL);

        simulation.getSimulationObjects().add(testDinosaur);
        simulation.getSimulationObjects().add(partner);

        when(simulationMap.isInsideOfGrid(any())).thenReturn(true);
        when(simulationMap.getTileAtPosition(any())).thenReturn(new Tile(testImage, 0, 0, false, false, true));

        //act
        SimulationObject closest = simulation.getClosestReachableSuitablePartnerInRange(testDinosaur.getPosition(), testDinosaur.getViewRange(), testDinosaur.getType(), testDinosaur.canSwim(), testDinosaur.canClimb(), testDinosaur.getGender());
        //assert
        assertAll("Check",
                () -> assertNotNull(closest, "There is a test dinosaur."),
                () -> assertEquals(70, closest.getPosition().getX(), "Closest needs to be the other test dinosaur."),
                () -> assertEquals(70, closest.getPosition().getY(), "Closest needs to be the other test dinosaur.")
        );
    }

    @DisplayName("There is not partner who is willing to mate.")
    @Test
    void getClosestReachableSuitablePartnerInRangeNull() {
        //arrange
        Dinosaur testDinosaur = new Dinosaur("test", testImage, 1, 2,
                3, 4, 5, 6, 7, 8,
                true, false, 'a', 500,
                10, 'm');

        testDinosaur.setPosition(new Vector2D(10, 10));

        Dinosaur partner = new Dinosaur("test", testImage, 1, 2,
                2, 4, 5, 6, 7, 8,
                true, false, 'a', 500,
                10, 'f');

        partner.setPosition(new Vector2D(70, 70));
        partner.setReproductionValue(Dinosaur.REPRODUCTION_VALUE_FULL-1);

        simulation.getSimulationObjects().add(testDinosaur);
        simulation.getSimulationObjects().add(partner);

        //act
        SimulationObject closest = simulation.getClosestReachableSuitablePartnerInRange(testDinosaur.getPosition(), testDinosaur.getViewRange(), testDinosaur.getType(), testDinosaur.canSwim(), testDinosaur.canClimb(), testDinosaur.getGender());
        //assert
        assertAll("Check",
                () -> assertNull(closest, "There is a no test dinosaur who is willing to mate.")
        );
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