package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.util.SimulationTime;

import java.util.List;

/**
 * Class used as data struct to return Statistics to GUI.
 *
 * @author Kai Grübener
 * @see Statistics
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused", "FieldCanBeLocal"})
public class StatisticsStruct {

    /**
     * Attributes for overall statistics.
     */
    private final long simulationTime;
    private final double averageNutritionPredators;
    private final double averageNutritionChased;
    private final double averageHydrationPredators;
    private final double averageHydrationChased;
    private final double absolutePercentagePredators;
    private final double absolutePercentageChased;
    private final List<Integer> allLivingDinosaurs;
    private final List<List<Integer>> allLivingSpecies;
    private final List<String> allSpecies;
    private final List<SimulationTime> simulationTimeList;

    /**
     * Constructor for {@link StatisticsStruct}-object
     * @param simulationTime The duration of the previous simulation.
     * @param averageNutritionPredators The average hunger of predators.
     * @param averageNutritionChased The average Hunger of chased dinosaurs.
     * @param averageHydrationPredators The average thirst of predators.
     * @param averageHydrationChased The average thirst of chased dinosaurs.
     * @param absolutePercentagePredators The proportion of predators.
     * @param absolutePercentageChased The proportioin of chased dinosaurs.
     * @param allLivingDinosaurs The timestamped count of dinosaurs.
     * @param allLivingSpecies The timestamped count of dinosaurs divides by species.
     * @param allSpecies The overall types of dinosaurs in the previous simulation.
     * @param simulationTimeList
     */
    public StatisticsStruct(long simulationTime, double averageNutritionPredators, double averageNutritionChased, double averageHydrationPredators, double averageHydrationChased, double absolutePercentagePredators, double absolutePercentageChased, List<Integer> allLivingDinosaurs, List<List<Integer>> allLivingSpecies, List<String> allSpecies, List<SimulationTime> simulationTimeList){
        this.simulationTime = simulationTime;
        this.averageNutritionPredators = averageNutritionPredators;
        this.averageNutritionChased = averageNutritionChased;
        this.averageHydrationPredators = averageHydrationPredators;
        this.averageHydrationChased = averageHydrationChased;
        this.absolutePercentagePredators = absolutePercentagePredators;
        this.absolutePercentageChased = absolutePercentageChased;
        this.allLivingDinosaurs = allLivingDinosaurs;
        this.allLivingSpecies = allLivingSpecies;
        this.allSpecies = allSpecies;
        this.simulationTimeList = simulationTimeList;
    }

    /**
     * Getter methods for class {@link StatisticsStruct}.
     */
    public double getAbsolutePercentageChased() {
        return absolutePercentageChased;
    }
    public double getAbsolutePercentagePredators() {
        return absolutePercentagePredators;
    }
    public double getAverageHydrationChased() {
        return averageHydrationChased;
    }
    public double getAverageHydrationPredators() {
        return averageHydrationPredators;
    }
    public double getAverageNutritionChased() {
        return averageNutritionChased;
    }
    public double getAverageNutritionPredators() {
        return averageNutritionPredators;
    }
    public List<Integer> getAllLivingDinosaurs() {
        return allLivingDinosaurs;
    }
    public List<List<Integer>> getAllLivingSpecies() {
        return allLivingSpecies;
    }
    public List<String> getAllSpecies() {
        return allSpecies;
    }
    public long getSimulationTime() {
        return simulationTime;
    }
    public List<SimulationTime> getSimulationTimeList(){return simulationTimeList;}
}