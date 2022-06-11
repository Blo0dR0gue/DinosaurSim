package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for statistics shown in GUI (methods are called from GUI)
 * Functionality splits into singleStatistics for a single entity (Dinosaur) ans SimulationStatistics for an overall view at the simulation's end.
 *
 * @author Kai Grübener
 * @see Dinosaur
 * @see com.dhbw.thesim.core.entity.Dinosaur
 * @see SimulationObject
 * @see com.dhbw.thesim.core.entity.SimulationObject
 * @see StatisticsStruct
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused"})
public class Statistics {

    /**
     * Variables:
     * A list of lists of SimulationObjects -> Evaluating statistics out of multiple SimulationObject-lists to generate an informational graph out of the data
     * @see SimulationObject
     * startTime is set in Constructor to determine simulation runtime.
     */
    private final List<List<SimulationObject>> statSimObjects;
    private final long startTime;

    /**
     * Constructor for class Statistics ->  Called from GUI to generate a statistics-object
     */
    public Statistics(){
        statSimObjects = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    /**
     * Method appends list of SimulationObjects to statSimObjects
     * @param simulationObjectList The list of all current simulationiObjects
     */
    public void addSimulationObjectList(List<SimulationObject> simulationObjectList){
        statSimObjects.add(List.copyOf(simulationObjectList));
    }

    /**
     * Method responsible for generation of overall statistics
     */
    public StatisticsStruct getSimulationStats(){
        long simulationTime = System.currentTimeMillis() - startTime;
        List<Integer> livingDinosaurs = new ArrayList<>();
        List<List<Integer>> livingSpecies = new ArrayList<>();
        List<Integer> livingPredators = new ArrayList<>();
        List<Integer> livingChased = new ArrayList<>();
        List<Double> averageNutritionPredators = new ArrayList<>();
        List<Double> averageNutritionChased = new ArrayList<>();
        List<Double> averageHydrationPredators = new ArrayList<>();
        List<Double> averageHydrationChased = new ArrayList<>();
        List<String> allDinoSpecies = getAllDinoSpecies(statSimObjects.get(0));

        for (List<SimulationObject> objList : statSimObjects){
            int livingDinosaursCounter = 0;
            List<Integer> livingSpeciesCounter = new ArrayList<>();
            for (int i=0; i<allDinoSpecies.size(); i++){livingSpeciesCounter.add(0);}
            int livingPredatorsCounter = 0;
            int livingChasedCounter = 0;
            double nutritionPredatorsCounter = 0.0;
            double nutritionChasedCounter = 0.0;
            double hydrationPredatorsCounter = 0.0;
            double hydrationChasedCounter = 0.0;

            for (SimulationObject obj : objList){
                if (obj instanceof Dinosaur){
                    livingDinosaursCounter++;
                    for (int i=0; i<allDinoSpecies.size(); i++){
                        if (obj.getType().equals(allDinoSpecies.get(i))){ livingSpeciesCounter.set(i, livingSpeciesCounter.get(i)+1); }
                    }
                    if (((Dinosaur) obj).getDiet().equals(Dinosaur.dietType.herbivore)){
                        livingChasedCounter++;
                        nutritionChasedCounter += ((Dinosaur) obj).getNutrition();
                        hydrationChasedCounter += ((Dinosaur) obj).getHydration();
                    }
                    else{
                        livingPredatorsCounter++;
                        nutritionPredatorsCounter += ((Dinosaur) obj).getNutrition();
                        hydrationPredatorsCounter += ((Dinosaur) obj).getHydration();
                    }
                }
            }
            livingDinosaurs.add(livingDinosaursCounter);
            livingSpecies.add(livingSpeciesCounter);
            livingPredators.add(livingPredatorsCounter);
            livingChased.add(livingChasedCounter);
            averageNutritionPredators.add(nutritionPredatorsCounter / livingPredatorsCounter);
            averageNutritionChased.add(nutritionChasedCounter / livingChasedCounter);
            averageHydrationPredators.add(hydrationPredatorsCounter / livingPredatorsCounter);
            averageHydrationChased.add(hydrationChasedCounter / livingChasedCounter);
        }

        double helperNutritionPredators = 0;
        double helperNutritionChased = 0;
        double helperHydrationPredators = 0;
        double helperHydrationChased = 0;
        int helperLivingDinosaurs = 0;
        int helperLivingPredators = 0;
        int helperLivingChased = 0;
        int listElementCounter = livingDinosaurs.size();
        for (int i = 0; i < listElementCounter; i++){
            helperNutritionPredators += averageNutritionPredators.get(i);
            helperNutritionChased += averageNutritionChased.get(i);
            helperHydrationPredators += averageHydrationPredators.get(i);
            helperHydrationChased += averageHydrationChased.get(i);
            helperLivingDinosaurs += livingDinosaurs.get(i);
            helperLivingPredators += livingPredators.get(i);
            helperLivingChased += livingChased.get(i);
        }
        return new StatisticsStruct(simulationTime, helperNutritionPredators/listElementCounter, helperNutritionChased/listElementCounter, helperHydrationPredators/listElementCounter, (helperHydrationChased/listElementCounter), helperLivingPredators/helperLivingDinosaurs, helperLivingChased/helperLivingDinosaurs, livingDinosaurs, livingSpecies, allDinoSpecies);
    }

    /**
     * Method responsible for singleStats for a single Dinosaur
     * @param dino The dinosaur objects to get its stat-values
     * @param simulationObjectList The list of all current simulationObjects to calculate species percentage
     * @return Returning Hashmap with information according to a single Dinosaur-object
     * @see Dinosaur
     */
    public Map<String, Double> getSingleStats(Dinosaur dino, List<SimulationObject> simulationObjectList){
        Map<String, Double> singleStats = new HashMap<>();
        singleStats.put("Hunger", dino.getNutrition());
        singleStats.put("Durst", dino.getHydration());
        singleStats.put("Fortpflanzungswilligkeit", dino.getReproductionValue());
        singleStats.put("Gewicht", dino.getWeight());
        singleStats.put("Hoehe", dino.getHeight());
        singleStats.put("Laenge", dino.getLength());
        singleStats.put("Ueberlebenszeit", (double)(System.currentTimeMillis() - dino.getTimeOfBirth()));
        singleStats.put("Artenanteil", calculateSpeciesPercentage(simulationObjectList, dino.getType()));
        return singleStats;
    }

    /**
     * Private method called from getSingleStats() to calculate share of a specified species compared to overall population
     * @param simulationObjectList The list of all current simulationObjects
     * @param dinoType The type of the given dino to split it from other types in calculation
     * @return Share of species
     * @see Dinosaur
     */
    private double calculateSpeciesPercentage(List<SimulationObject> simulationObjectList, String dinoType){
        int dinoCounter = 0;
        int typeCounter = 0;
        for (SimulationObject obj : simulationObjectList){
            if (obj instanceof Dinosaur){
                dinoCounter++;
                if (obj.getType().equals(dinoType)){ typeCounter++; }
            }
        }
        return (double)typeCounter/dinoCounter;
    }

    /**
     * Private method called from getSimulationStats() to get all dinosaur species appearing in simulation
     * @param simulationObjectList The list of all current simulationObjects
     * @return List of all appearing species of Dinosaurs
     * @see Dinosaur
     */
    private List<String> getAllDinoSpecies(List<SimulationObject> simulationObjectList){
        List<String> allDinoSpecies = new ArrayList<>();
        for (SimulationObject obj : simulationObjectList){
            if (obj instanceof Dinosaur){
                if (!allDinoSpecies.contains(obj.getType())){
                    allDinoSpecies.add(obj.getType());
                }
            }
        }
        return allDinoSpecies;
    }
}
