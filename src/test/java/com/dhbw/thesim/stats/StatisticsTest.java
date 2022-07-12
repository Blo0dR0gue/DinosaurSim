package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.util.SimulationTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsTest {

    Statistics statistics;
    SimulationTime baseSimulationTime = new SimulationTime(10);
    long startTime;

    @BeforeEach
    void setUp(){
        startTime = System.currentTimeMillis(); //hopefully this will be the same startTime that statistics has. otherwise, System.currentTimeMillis needs to be mocked
        statistics = new Statistics();

        statistics.addSimulationObjectList(makeSimulationObjectList(1), makeSimulationTime(1));
    }

    private SimulationTime makeSimulationTime(int multiplier) {
        SimulationTime newSimulationTime = new SimulationTime(baseSimulationTime.getTime());
        newSimulationTime.setTime(newSimulationTime.getTime()*multiplier);
        return newSimulationTime;
    }

    private List<SimulationObject> makeSimulationObjectList(int multiplier) {
        List<SimulationObject> simulationObjectList = new ArrayList<>();

        //TODO make a few dinosaurs and plants whose values and amounts (only dinos) change with the multiplier

        //TODO mock image
        //TODO get proper types
        //TODO get arbitrary interaction range

        SimulationObject simulationObject = new SimulationObject(null, 0, null) {
            @Override
            public void update(double deltaTime, Simulation currentSimulation) {

            }

            @Override
            public void updateGraphics() {

            }

            @Override
            public void eat() {

            }

            @Override
            public boolean canBeEaten(double checkValue) {
                return false;
            }
        };

        return null;
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Disabled("not yet implemented")
    void getSimulationTime(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAverageNutritionPredators(){



    }

    @Test
    @Disabled("not yet implemented")
    void getAverageNutritionChased(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAverageHydrationPredators(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAverageHydrationChased(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAbsolutePercentagePredators(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAbsolutePercentageChased(){



    }

    @Test
    @Disabled("not yet implemented")
    void getAllLivingDinosaurs(){



    }

    @Test
    @Disabled("not yet implemented")
    void getAllLivingSpecies(){



    }

    @Test
    @Disabled("not yet implemented")
    void getAllSpecies(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAllLivingPredators(){

    }

    @Test
    @Disabled("not yet implemented")
    void getAllLivingChased(){

    }

    @Test
    @Disabled("not yet implemented")
    void getSimulationTimeList(){

    }

}
