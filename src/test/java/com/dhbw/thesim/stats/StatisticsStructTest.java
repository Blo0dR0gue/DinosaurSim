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

class StatisticsStructTest {

    StatisticsStruct statisticsStruct;

    private List<Integer> integerList = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());

    private long simulationTime = 10;
    private double averageNutritionPredators = 0.5;
    private double averageNutritionChased = 0.25;
    private double averageHydrationPredators = 0.5;
    private double averageHydrationChased = 0.25;
    private double absolutePercentagePredators = 0.6;
    private double absolutePercentageChased = 0.4;
    private List<Integer> allLivingDinosaurs = new ArrayList<>(integerList);
    private List<List<Integer>> allLivingSpecies = new ArrayList<>();   //TODO fill with values
    private List<String> allSpecies = new ArrayList<>();    //TODO fill with values
    private List<Integer> allLivingPredators = new ArrayList<>();   //TODO fill with values
    private List<Integer> allLivingChased = new ArrayList<>();  //TODO fill with values
    private List<SimulationTime> simulationTimeList = new ArrayList<>(); //TODO fill with values

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

        assertEquals(result, expected);
    }

    @Test
    void getAverageNutritionPredators(){

        double result = statisticsStruct.averageNutritionPredators();

        double expected = averageNutritionPredators;

        assertEquals(result, expected);

    }

    @Test
    void getAverageNutritionChased(){
        double result = statisticsStruct.averageNutritionChased();

        double expected = averageNutritionChased;

        assertEquals(result, expected);
    }

    @Test
    void getAverageHydrationPredators(){
        double result = statisticsStruct.averageHydrationPredators();

        double expected = averageHydrationPredators;

        assertEquals(result, expected);
    }

    @Test
    void getAverageHydrationChased(){
        double result = statisticsStruct.averageHydrationChased();

        double expected = averageHydrationChased;

        assertEquals(result, expected);
    }

    @Test
    void getAbsolutePercentagePredators(){
        double result = statisticsStruct.absolutePercentagePredators();

        double expected = absolutePercentagePredators;

        assertEquals(result, expected);
    }

    @Test
    void getAbsolutePercentageChased(){

        double result = statisticsStruct.absolutePercentageChased();

        double expected = absolutePercentageChased;

        assertEquals(result, expected);

    }

    @Test
    void getAllLivingDinosaurs(){

        List<Integer> result = statisticsStruct.allLivingDinosaurs();

        List<Integer> expected = allLivingDinosaurs;

        assertEquals(result, expected);

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
