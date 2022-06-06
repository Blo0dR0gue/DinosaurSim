package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Mock
    SimulationMap simulationMap;

    @Mock
    GraphicsContext graphicsContext;

    Simulation simulation;

    @BeforeEach
    void setUp() {
        //TODO
        simulation = new Simulation(simulationMap, graphicsContext, new HashMap<>(), new HashMap<>(), 2, new Random());
    }

    @AfterEach
    void tearDown() {
        simulation = null;
    }

    @Test
    void sortByDistance() throws Exception {

        List<SimulationObject> simulationObjectList = new ArrayList<>();

        //TODO add powermock
        //Image myObjectMock = mock(Image.class);
        //PowerMockito.whenNew(Image.class).withAnyArguments().thenReturn(myObjectMock);

        Plant p2 = new Plant("test", "test.png", 2,2);
        p2.setPosition(new Vector2D(800,800));
        simulationObjectList.add(p2);

        Plant p1 = new Plant("test", "test.png", 2,2);
        p1.setPosition(new Vector2D(40,40));
        simulationObjectList.add(p1);

        Vector2D sortTo = new Vector2D(50,50);

        simulation.sortByDistance(new Vector2D(50,50), simulationObjectList);

        double lastDistance = 0;

        for (SimulationObject simulationObject: simulationObjectList) {

            double distance = Vector2D.distance(sortTo, simulationObject.getPosition());

            assertTrue(lastDistance <= distance);

        }


    }
}