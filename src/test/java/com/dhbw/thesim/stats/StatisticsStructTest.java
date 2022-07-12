package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.util.SimulationTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse Testet die Funktionalität der stats.StatisticsStruct Klasse
 *
 * @author Tamina Mühlenberg
 */
class StatisticsStructTest {

    StatisticsStruct statisticsStruct;

    private List<Integer> integerList = List.of(10, 9, 8, 8, 7, 6, 5, 4);

    private long simulationTime = 10;
    private double averageNutritionPredators = 0.5;
    private double averageNutritionChased = 0.25;
    private double averageHydrationPredators = 0.5;
    private double averageHydrationChased = 0.25;
    private double absolutePercentagePredators = 0.6;
    private double absolutePercentageChased = 0.4;
    private List<Integer> allLivingDinosaurs = new ArrayList<>(integerList);
    private List<List<Integer>> allLivingSpecies = List.of(integerList, integerList);
    private List<String> allSpecies = List.of("Species1", "Species2");
    private List<Integer> allLivingPredators = integerList;
    private List<Integer> allLivingChased = integerList;
    private List<SimulationTime> simulationTimeList = List.of(new SimulationTime(), new SimulationTime(10), new SimulationTime(20));

    @BeforeEach
    void setUp(){
        //creating statisticsStruct with initial values
        statisticsStruct = new StatisticsStruct(
                simulationTime,
                averageNutritionPredators,
                averageNutritionChased,
                averageHydrationPredators,
                averageHydrationChased,
                absolutePercentagePredators,
                absolutePercentageChased,
                allLivingDinosaurs,
                allLivingSpecies,
                allSpecies,
                allLivingPredators,
                allLivingChased,
                simulationTimeList
        );
    }

    @AfterEach
    void tearDown(){
        statisticsStruct = null;
    }

    @Test
    void getSimulationTime(){

        //arrange

        //act

        long result = statisticsStruct.simulationTime();

        //assert
        long expected = simulationTime;

        assertEquals(expected, result);
    }

    @Test
    void getAverageNutritionPredators(){

        double result = statisticsStruct.averageNutritionPredators();

        double expected = averageNutritionPredators;

        assertEquals(expected, result);

    }

    @Test
    void getAverageNutritionChased(){
        double result = statisticsStruct.averageNutritionChased();

        double expected = averageNutritionChased;

        assertEquals(expected, result);
    }

    @Test
    void getAverageHydrationPredators(){
        double result = statisticsStruct.averageHydrationPredators();

        double expected = averageHydrationPredators;

        assertEquals(expected, result);
    }

    @Test
    void getAverageHydrationChased(){
        double result = statisticsStruct.averageHydrationChased();

        double expected = averageHydrationChased;

        assertEquals(expected, result);
    }

    @Test
    void getAbsolutePercentagePredators(){
        double result = statisticsStruct.absolutePercentagePredators();

        double expected = absolutePercentagePredators;

        assertEquals(expected, result);
    }

    @Test
    void getAbsolutePercentageChased(){

        double result = statisticsStruct.absolutePercentageChased();

        double expected = absolutePercentageChased;

        assertEquals(expected, result);

    }

    @Test
    void getAllLivingDinosaurs(){

        List<Integer> result = statisticsStruct.allLivingDinosaurs();

        List<Integer> expected = allLivingDinosaurs;

        assertEquals(expected, result);

    }

    @Test
    void getAllLivingSpecies(){

        List<List<Integer>> result = statisticsStruct.allLivingSpecies();

        List<List<Integer>> expected = allLivingSpecies;

        assertEquals(expected, result);

    }

    @Test
    void getAllSpecies(){

        List<String> result = statisticsStruct.allSpecies();

        List<String> expected = allSpecies;

        assertEquals(expected, result);
    }

    @Test
    void getAllLivingPredators(){
        List<Integer> result = statisticsStruct.allLivingPredators();

        List<Integer> expected = allLivingPredators;

        assertEquals(expected, result);
    }

    @Test
    void getAllLivingChased(){
        List<Integer> result = statisticsStruct.allLivingChased();

        List<Integer> expected = allLivingChased;

        assertEquals(expected, result);
    }

    @Test
    void getSimulationTimeList(){
        List<SimulationTime> result = statisticsStruct.simulationTimeList();

        List<Double> resultAsDoubles = getSimulationTimesAsDoubles(result);

        List<SimulationTime> expected = simulationTimeList;

        List<Double> expectedAsDoubles = getSimulationTimesAsDoubles(expected);

        assertEquals(expected, result);
    }

    private List<Double> getSimulationTimesAsDoubles(List<SimulationTime> simulationTimes) {
        List<Double> simulationTimesAsDoubles = new ArrayList<>();

        for (int i = 0; i < simulationTimes.size(); i++) {
            simulationTimesAsDoubles.add(simulationTimes.get(i).getTime());
        }

        return simulationTimesAsDoubles;
    }
}
