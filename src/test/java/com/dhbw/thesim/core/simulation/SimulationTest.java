package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link Simulation} class.
 *
 * @author Daniel Czeschner
 */
class SimulationTest {

    @Mock
    SimulationMap simulationMap;

    @Mock
    GraphicsContext graphicsContext;

    Simulation simulation;

    @BeforeEach
    void setUp() {
        simulation = new Simulation(simulationMap, graphicsContext, new HashMap<>(), new HashMap<>(), 2, new Random());
    }

    @AfterEach
    void tearDown() {
        simulation = null;
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

    @Test
    void isOver() {
    }

    @Test
    void getClosestReachableWaterSource() {
    }

    @Test
    void getClosestReachableFoodSourceInRange() {
    }

    @Test
    void findReachableFoodSourcesInRange() {
    }

    @Test
    void getClosestReachableSuitablePartnerInRange() {
    }

    @Test
    void makeBaby() {
    }

    @Test
    void getNearestPositionInMapWhereConditionsAre() {
    }

    @Test
    void getFreePositionInMap() {
    }

    @Test
    void getFreePositionInMapWhereConditionsAre() {
    }

    @Test
    void canMoveTo() {
    }

    @Test
    void getRandomMovementTargetInRange() {
    }

    @Test
    void getRandomMovementTargetInRangeInDirection() {
    }

    @Test
    void doTheCirclesIntersect() {
    }
}