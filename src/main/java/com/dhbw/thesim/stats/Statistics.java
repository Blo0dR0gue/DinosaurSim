package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.util.SimulationTime;

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
     *
     * @see SimulationObject
     * startTime is set in Constructor to determine simulation runtime.
     */
    private final List<List<SimulationObject>> statSimObjects;
    private final List<SimulationTime> simulationTimeList;
    private final long startTime;

    private enum dinosaurType{
        CHASED,
        PREDATOR
    }

    private enum nutritionType{
        HYDRATION,
        NUTRITION
    }

    /**
     * Constructor for class Statistics ->  Called from GUI to generate a statistics-object
     */
    public Statistics() {
        statSimObjects = new ArrayList<>();
        simulationTimeList = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    /**
     * Method appends list of SimulationObjects to statSimObjects
     *
     * @param simulationObjectList The list of all current simulationiObjects
     * @param simulationTime       The simulation time
     */
    public void addSimulationObjectList(List<SimulationObject> simulationObjectList, SimulationTime simulationTime) {

        List<SimulationObject> iteration = new ArrayList<>();

        for (SimulationObject simulationObject : simulationObjectList){
            if(simulationObject instanceof Dinosaur dinosaur){
                iteration.add(dinosaur.copyOf());
            }
        }

        statSimObjects.add(iteration);
        simulationTimeList.add(new SimulationTime(simulationTime.getTime()));
    }

    /**
     * Method responsible for generation of overall statistics
     */
    public StatisticsStruct getSimulationStats() {
        long simulationTime = System.currentTimeMillis() - startTime;
        List<Integer> livingDinosaursIterations = new ArrayList<>();
        List<List<Integer>> livingSpeciesIterations = new ArrayList<>();
        List<Integer> livingPredatorsIterations = new ArrayList<>();
        List<Integer> livingChasedIterations = new ArrayList<>();
        List<Double> averageNutritionPredatorsIterations = new ArrayList<>();
        List<Double> averageNutritionChasedIterations = new ArrayList<>();
        List<Double> averageHydrationPredatorsIterations = new ArrayList<>();
        List<Double> averageHydrationChasedIterations = new ArrayList<>();
        List<String> allDinoSpecies = getAllDinoSpecies(statSimObjects.get(0));

        double allTimeLivingDinosaursCounter = 0d;
        List<Dinosaur> allDinosaursEverAliveList = new ArrayList<>();

        for (List<SimulationObject> currentObjects : statSimObjects) {  //iterate over every list of objects per statSimObjects update

            int iterationLivingDinosaursCounter = 0;

            List<Integer> iterationLivingSpeciesCounter = initiateListOfLivingSpeciesCounters(allDinoSpecies);

            int iterationLivingPredatorsCounter = 0;
            int iterationLivingChasedCounter = 0;



            HashMap<dinosaurType, List<Double>> iterationNutritionPercentages = new HashMap<>();
            initPercentagesHashMap(iterationNutritionPercentages);

            HashMap<dinosaurType, List<Double>> iterationHydrationPercentages = new HashMap<>();
            initPercentagesHashMap(iterationHydrationPercentages);

            for (SimulationObject obj : currentObjects) {   //iterate over every object in the list of current objects
                if (obj instanceof Dinosaur dinosaur) {

                    iterationLivingDinosaursCounter++;

                    if (!allDinosaursEverAliveList.contains(dinosaur)) {    //Checking if the current dinosaur has already been counted toward the list of all time living dinosaurs and adding it if not
                        allTimeLivingDinosaursCounter++;
                        allDinosaursEverAliveList.add(dinosaur);
                    }


                    increaseLivingSpeciesCount(allDinoSpecies, iterationLivingSpeciesCounter, obj);

                    if (dinosaur.getDiet().equals(Dinosaur.dietType.HERBIVORE)) {   //adding stats for chased vs predators to counters
                        iterationLivingChasedCounter++;
                        getDietPercentages(dinosaurType.CHASED, iterationNutritionPercentages, iterationHydrationPercentages, dinosaur);
                    } else {
                        iterationLivingPredatorsCounter++;
                        getDietPercentages(dinosaurType.PREDATOR, iterationNutritionPercentages, iterationHydrationPercentages, dinosaur);
                    }
                }
            }

            double iterationNutritionPredatorsPercentage = calculateTotalAverage(iterationNutritionPercentages.get(dinosaurType.PREDATOR));
            double iterationNutritionChasedPercentage = calculateTotalAverage(iterationNutritionPercentages.get(dinosaurType.CHASED));

            double iterationHydrationPredatorsPercentage = calculateTotalAverage(iterationHydrationPercentages.get(dinosaurType.PREDATOR));
            double iterationHydrationChasedPercentage = calculateTotalAverage(iterationHydrationPercentages.get(dinosaurType.CHASED));

            averageNutritionPredatorsIterations.add(iterationNutritionPredatorsPercentage);
            averageNutritionChasedIterations.add(iterationNutritionPredatorsPercentage);

            averageHydrationPredatorsIterations.add(iterationHydrationPredatorsPercentage);
            averageHydrationChasedIterations.add(iterationHydrationChasedPercentage);

            livingDinosaursIterations.add(iterationLivingDinosaursCounter);

            livingSpeciesIterations.add(iterationLivingSpeciesCounter);

            livingPredatorsIterations.add(iterationLivingPredatorsCounter);

            livingChasedIterations.add(iterationLivingChasedCounter);
        }

        double sumOfLivingDinosaursAcrossAllIterations = 0.0;
        double sumOfLivingPredatorsAcrossAllIterations = 0.0;
        double sumOfLivingChasedAcrossAllIterations = 0.0;
        int countOfIterations = livingDinosaursIterations.size();

        for (int i = 0; i < countOfIterations; i++) {
            sumOfLivingDinosaursAcrossAllIterations += livingDinosaursIterations.get(i);
            sumOfLivingPredatorsAcrossAllIterations += livingPredatorsIterations.get(i);
            sumOfLivingChasedAcrossAllIterations += livingChasedIterations.get(i);
        }

        double averageNutritionPredatorsAcrossAllIterations = calculateTotalAverage(averageNutritionPredatorsIterations);
        double averageNutritionChasedAcrossAllIterations = calculateTotalAverage(averageNutritionChasedIterations);

        double averageHydrationPredatorsAcrossAllIterations = calculateTotalAverage(averageHydrationPredatorsIterations);
        double averageHydrationChasedAcrossAllIterations = calculateTotalAverage(averageHydrationChasedIterations);

        double percentagePredatorsAcrossAllIterations = sumOfLivingPredatorsAcrossAllIterations / sumOfLivingDinosaursAcrossAllIterations;
        double percentageChasedAcrossAllIterations = sumOfLivingChasedAcrossAllIterations / sumOfLivingDinosaursAcrossAllIterations;


        return new StatisticsStruct(simulationTime, averageNutritionPredatorsAcrossAllIterations,
                averageNutritionChasedAcrossAllIterations,
                averageHydrationPredatorsAcrossAllIterations,
                averageHydrationChasedAcrossAllIterations,
                percentagePredatorsAcrossAllIterations,
                percentageChasedAcrossAllIterations,
                livingDinosaursIterations, livingSpeciesIterations, allDinoSpecies, livingPredatorsIterations, livingChasedIterations,
                this.simulationTimeList);
    }

    private double calculateTotalAverage(List<Double> averages) {
        return averages.size() != 0 ? getTotalOfAllAverages(averages) / averages.size() : 0;
    }

    private void getDietPercentages(dinosaurType type, HashMap<dinosaurType, List<Double>> iterationNutritionPercentages, HashMap<dinosaurType, List<Double>> iterationHydrationPercentages, Dinosaur dinosaur) {
        iterationNutritionPercentages.get(type).add(getDinosaurNutritionPercentage(dinosaur));
        iterationHydrationPercentages.get(type).add(getDinosaurHydrationPercentage(dinosaur));
    }

    private void initPercentagesHashMap(HashMap<dinosaurType, List<Double>> iterationNutritionPercentages) {
        iterationNutritionPercentages.put(dinosaurType.PREDATOR, new ArrayList<>());
        iterationNutritionPercentages.put(dinosaurType.CHASED, new ArrayList<>());
    }

    private double getDinosaurHydrationPercentage(Dinosaur dinosaur) {
        //getting the percentage of current hydration
        return dinosaur.getHydration() / dinosaur.getMaxHydration();
    }

    private double getDinosaurNutritionPercentage(Dinosaur dinosaur) {
        //getting the percentage of current nutrition
        return dinosaur.getNutrition() / dinosaur.getMaxNutrition();
    }

    private void increaseLivingSpeciesCount(List<String> allDinoSpecies, List<Integer> iterationLivingSpeciesCounter, SimulationObject obj) {
        for (int i = 0; i < allDinoSpecies.size(); i++) { //adding the current dinosaur to the corresponding species count
            if (obj.getType().equals(allDinoSpecies.get(i))) {
                iterationLivingSpeciesCounter.set(i, iterationLivingSpeciesCounter.get(i) + 1);
            }
        }
    }

    private List<Integer> initiateListOfLivingSpeciesCounters(List<String> allDinoSpecies) {
        //initiate list of living species
        List<Integer> iterationLivingSpeciesCounter = new ArrayList<>();
        for (int i = 0; i < allDinoSpecies.size(); i++) {
            iterationLivingSpeciesCounter.add(0);
        }
        return iterationLivingSpeciesCounter;
    }

    private double getTotalOfAllAverages(List<Double> averageList) {
        double helper = 0;
        for (double avg :
                averageList) {
            helper += avg;
        }
        return helper;
    }

    /**
     * Method responsible for singleStats for a single Dinosaur
     *
     * @param dino                 The dinosaur objects to get its stat-values
     * @param simulationObjectList The list of all current simulationObjects to calculate species percentage
     * @return Returning Hashmap with information according to a single Dinosaur-object
     * @see Dinosaur
     */
    public Map<String, Double> getSingleStats(Dinosaur dino, List<SimulationObject> simulationObjectList, SimulationTime currentSimulationTime) {
        Map<String, Double> singleStats = new HashMap<>();
        singleStats.put("Hunger", dino.getNutrition());
        singleStats.put("MaxHunger", dino.getMaxNutrition());
        singleStats.put("Durst", dino.getHydration());
        singleStats.put("MaxDurst", dino.getMaxHydration());
        singleStats.put("Staerke", dino.getStrength());
        singleStats.put("Geschwindigkeit", dino.getSpeed());
        singleStats.put("Fortpflanzungswilligkeit", dino.getReproductionValue());
        singleStats.put("Gewicht", dino.getWeight());
        singleStats.put("Hoehe", dino.getHeight());
        singleStats.put("Laenge", dino.getLength());
        singleStats.put("KannSchwimmen", dino.canSwim() ? 1d : 0d);
        singleStats.put("KannKlettern", dino.canClimb() ? 1d : 0d);
        singleStats.put("WirdGejagt", dino.isChased() ? 1d : 0d);
        singleStats.put("Ueberlebenszeit", (dino.getTimeOfBirth().timeSince(currentSimulationTime)));
        singleStats.put("Artenanteil", calculateSpeciesPercentage(simulationObjectList, dino.getType()));
        return singleStats;
    }

    /**
     * Private method called from getSingleStats() to calculate share of a specified species compared to overall population
     *
     * @param simulationObjectList The list of all current simulationObjects
     * @param dinoType             The type of the given dino to split it from other types in calculation
     * @return Share of species
     * @see Dinosaur
     */
    private double calculateSpeciesPercentage(List<SimulationObject> simulationObjectList, String dinoType) {
        int dinoCounter = 0;
        int typeCounter = 0;
        for (SimulationObject obj : simulationObjectList) {
            if (obj instanceof Dinosaur) {
                dinoCounter++;
                if (obj.getType().equals(dinoType)) {
                    typeCounter++;
                }
            }
        }
        return (double) typeCounter / dinoCounter;
    }

    /**
     * Private method called from getSimulationStats() to get all dinosaur species appearing in simulation
     *
     * @param simulationObjectList The list of all current simulationObjects
     * @return List of all appearing species of Dinosaurs
     * @see Dinosaur
     */
    private List<String> getAllDinoSpecies(List<SimulationObject> simulationObjectList) {
        List<String> allDinoSpecies = new ArrayList<>();
        for (SimulationObject obj : simulationObjectList) {
            if (obj instanceof Dinosaur) {
                if (!allDinoSpecies.contains(obj.getType())) {
                    allDinoSpecies.add(obj.getType());
                }
            }
        }
        return allDinoSpecies;
    }
}
