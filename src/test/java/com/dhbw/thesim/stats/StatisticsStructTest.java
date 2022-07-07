package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.util.SimulationTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    }

    @Test
    void getAverageNutritionPredators(){

    }

    @Test
    void getAverageNutritionChased(){

    }

    @Test
    void getAverageHydrationPredators(){

    }

    @Test
    void getAverageHydrationChased(){

    }

    @Test
    void getAbsolutePercentagePredators(){

    }

    @Test
    void getAbsolutePercentageChased(){

    }

    @Test
    void getAllLivingDinosaurs(){

    }

    @Test
    void getAllLivingSpecies(){

    }

    @Test
    void getAllSpecies(){

    }

    @Test
    void getAllLivingPredators(){

    }

    @Test
    void getAllLivingChased(){

    }

    @Test
    void getSimulationTimeList(){

    }
}
