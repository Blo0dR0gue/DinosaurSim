package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.util.SimulationTime;

import java.util.List;

/**
 * Class used as data struct to return Statistics to the GUI.
 *
 * @param simulationTime              The time of the simulation the data got collected.
 * @param averageNutritionPredators   The average hunger of predators.
 * @param averageNutritionChased      The average Hunger of chased dinosaurs.
 * @param averageHydrationPredators   The average thirst of predators.
 * @param averageHydrationChased      The average thirst of chased dinosaurs.
 * @param absolutePercentagePredators The proportion of predators.
 * @param absolutePercentageChased    The proportion of chased dinosaurs.
 * @param allLivingDinosaurs          The timestamped count of dinosaurs.
 * @param allLivingSpecies            The timestamped count of dinosaurs divides by species.
 * @param allSpecies                  The overall types of dinosaurs in the previous simulation.
 * @param allLivingPredators          The timestamped count of dinosaurs who are predators
 * @param allLivingChased             The timestamped count of chased dinosaurs
 * @param simulationTimeList          The list of simulation times
 * @author Kai Grübener
 * @see Statistics
 */
public record StatisticsStruct(long simulationTime, double averageNutritionPredators, double averageNutritionChased,
                               double averageHydrationPredators, double averageHydrationChased,
                               double absolutePercentagePredators, double absolutePercentageChased,
                               List<Integer> allLivingDinosaurs, List<List<Integer>> allLivingSpecies,
                               List<String> allSpecies, List<Integer> allLivingPredators, List<Integer> allLivingChased,
                               List<SimulationTime> simulationTimeList) {
}