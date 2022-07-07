package com.dhbw.thesim.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link SimulationTime} class.
 *
 * @author Daniel Czeschner
 */
class SimulationTimeTest {

    @DisplayName("Add a delta time in seconds to the simulation time.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0", "1, 2, 3", "0.3, 0.000323, 0.300323", "2.16, 0.09339, 2.25339"})
    void addDeltaTime(double startTime, double deltaTime, double result) {
        //arrange
        SimulationTime simulationTime = new SimulationTime(startTime);
        //act
        simulationTime.addDeltaTime(deltaTime);
        //assert
        assertEquals(result, simulationTime.getTime(), "The simulation time should update correctly.");
    }

    @DisplayName("Get simulation time.")
    @ParameterizedTest
    @CsvSource({"0", "300", "7.3", "27.16313"})
    void getTime(double startTime) {
        //arrange
        SimulationTime simulationTime = new SimulationTime(startTime);
        //act

        //assert
        assertEquals(startTime, simulationTime.getTime(), "The simulation time should be the creation time.");
    }

    @DisplayName("Set the simulation time.")
    @ParameterizedTest
    @CsvSource({"0", "300", "7.3", "27.16313"})
    void setTime(double time) {
        //arrange
        SimulationTime simulationTime = new SimulationTime();
        //act
        simulationTime.setTime(time);
        //assert
        assertEquals(time, simulationTime.getTime(), "The simulation time should be set correctly.");
    }

    @DisplayName("Add minutes to the simulation time.")
    @ParameterizedTest
    @CsvSource({"0, 0", "300, 18000", "7.3, 438", "27.16313, 1629.7877999999998"})
    void addMinutesTime(double minutes, double result) {
        //arrange
        SimulationTime simulationTime = new SimulationTime();
        //act
        simulationTime.addMinutesToTime(minutes);
        //assert
        assertEquals(result, simulationTime.getTime(), "The simulation time should update correctly.");
    }

    @DisplayName("Get the time between to simulation times.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0", "27.33, 30.2, 2.870000000000001"})
    void timeSince(double startTime1, double startTime2, double result) {
        //arrange
        SimulationTime simulationTime1 = new SimulationTime(startTime1);
        SimulationTime simulationTime2 = new SimulationTime(startTime2);
        //act
        double since = simulationTime1.timeSince(simulationTime2);
        //assert
        assertEquals(result, since, "The simulation time between two times should calculated correctly.");
    }
}