package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.util.SimulationTime;
import org.junit.jupiter.api.*;

import javafx.scene.image.Image;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsTest {

    Statistics statistics;
    SimulationTime baseSimulationTime = new SimulationTime(10);
    long startTime;

    double maxHydrationNutrition = 100;

    Dinosaur baseOmnivore = new Dinosaur(
            "omnivore",
            makeMockImage("dinosaur"),
            maxHydrationNutrition,
            maxHydrationNutrition,
            100,
            6,
            0.4,
            1000,
            40,
            30,
            true,
            false,
            'a',
            60,
            50,
            'f'
    );

    double baseOmnivoreNutrition = 80;
    double baseOmnivoreHydration = 60;

    Dinosaur baseCarnivore = new Dinosaur(
            "carnivore",
            makeMockImage("dinosaur"),
            maxHydrationNutrition,
            maxHydrationNutrition,
            80,
            45,
            0.6,
            500,
            20,
            10,
            false,
            true,
            'f',
            330,
            70,
            'm'
    );

    double baseCarnivoreNutrition = 40;
    double baseCarnivoreHydration = 50;

    Dinosaur baseHerbivore = new Dinosaur(
            "herbivore",
            makeMockImage("dinosaur"),
            maxHydrationNutrition,
            maxHydrationNutrition,
            50,
            20,
            0.9,
            2000,
            20,
            40,
            true,
            true,
            'p',
            400,
            60,
            'f'
    );

    double baseHerbivoreNutrition = 90;
    double baseHerbivoreHydration = 80;

    Plant basePlant = new Plant(
        "plant",
            makeMockImage("plant"),
10,
    10
    );

    enum DietType {
        HYDRATION,
        NUTRITION
    }

    enum DinoType {
        PREDATOR,
        CHASED
    }

    @BeforeEach
    void setUp(){
        startTime = System.currentTimeMillis(); //hopefully this will be the same startTime that statistics has. otherwise, System.currentTimeMillis needs to be mocked
        statistics = new Statistics();
    }

    @AfterEach
    void tearDown(){
        statistics = null;
        startTime = 0;
    }

    @Test
    @Timeout(value=200, unit= TimeUnit.MILLISECONDS)
    @Disabled("Testing with System.currentTimeMillis() and timeouts is too unreliable")
    @DisplayName("Test if Simulation Time from initialisation of statistics to call of getSimulationStats() calculates the time correctly")
    void getSimulationTime() throws InterruptedException {

        addNewStatsUpdate(0,0,0,0,0,1);


        long currentTime = System.currentTimeMillis();

        //TODO timeout for x millis and get difference
        int delay = 100;

        TimeUnit.MILLISECONDS.sleep(delay);
        long expected = delay+(currentTime-startTime);

        long result = statistics.getSimulationStats().simulationTime();
        assertEquals(expected, result);

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAverageNutritionPredators(int withOmnivores){
        DietType dietType = DietType.NUTRITION;

        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 2;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, 1, 0, attributeMultiplier);
        double update1 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseCarnivore(), amountOfCarnivores, getBaseOmnivore(), amountOfOmnivores), attributeMultiplier, dietType);

        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, 0, 0, attributeMultiplier);
        double update2 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseCarnivore(), amountOfCarnivores, getBaseOmnivore(), amountOfOmnivores), attributeMultiplier, dietType);

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, 0, 0, attributeMultiplier);
        double update3 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseCarnivore(), amountOfCarnivores, getBaseOmnivore(), amountOfOmnivores), attributeMultiplier, dietType);

        double expected = (update1 + update2 + update3) / 3;

        double result = statistics.getSimulationStats().averageNutritionPredators();

        assertEquals(expected, result);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAverageNutritionChased(int withOmnivores){
        DietType dietType = DietType.NUTRITION;

        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 3;
        int amountOfHerbivores = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        double update1 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseHerbivore(), amountOfHerbivores), attributeMultiplier, dietType);

        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        double update2 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseHerbivore(), amountOfHerbivores), attributeMultiplier, dietType);

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        double update3 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseHerbivore(), amountOfHerbivores), attributeMultiplier, dietType);

        double expected = (update1 + update2 + update3) / 3;

        double result = statistics.getSimulationStats().averageNutritionChased();

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAverageHydrationPredators(int withOmnivores){
        DietType dietType = DietType.HYDRATION;

        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 2;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, 1, 0, attributeMultiplier);
        double update1 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseCarnivore(), amountOfCarnivores, getBaseOmnivore(), amountOfOmnivores), attributeMultiplier, dietType);

        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, 0, 0, attributeMultiplier);
        double update2 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseCarnivore(), amountOfCarnivores, getBaseOmnivore(), amountOfOmnivores), attributeMultiplier, dietType);

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, 0, 0, attributeMultiplier);
        double update3 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseCarnivore(), amountOfCarnivores, getBaseOmnivore(), amountOfOmnivores), attributeMultiplier, dietType);

        double expected = (update1 + update2 + update3) / 3;

        double result = statistics.getSimulationStats().averageHydrationPredators();

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAverageHydrationChased(int withOmnivores){
        DietType dietType = DietType.HYDRATION;

        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 3;
        int amountOfHerbivores = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        double update1 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseHerbivore(), amountOfHerbivores), attributeMultiplier, dietType);

        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        double update2 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseHerbivore(), amountOfHerbivores), attributeMultiplier, dietType);

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        double update3 = getExpectedAverageDietForDinosPerUpdate(Map.of(getBaseHerbivore(), amountOfHerbivores), attributeMultiplier, dietType);

        double expected = (update1 + update2 + update3) / 3;

        double result = statistics.getSimulationStats().averageHydrationChased();

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAbsolutePercentagePredators(int withOmnivores){

        int sumOfPredators = 0;
        int sumOfChased = 0;

        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 3;
        int amountOfHerbivores = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        sumOfPredators += amountOfOmnivores + amountOfCarnivores;
        sumOfChased += amountOfHerbivores;


        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        sumOfPredators += amountOfOmnivores + amountOfCarnivores;
        sumOfChased += amountOfHerbivores;

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        sumOfPredators += amountOfOmnivores + amountOfCarnivores;
        sumOfChased += amountOfHerbivores;

        double expected = ((double) sumOfPredators) / ((double) (sumOfChased+sumOfPredators));

        double result = statistics.getSimulationStats().absolutePercentagePredators();

        assertEquals(expected, result);
    }



    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAbsolutePercentageChased(int withOmnivores){

        int sumOfPredators = 0;
        int sumOfChased = 0;

        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 3;
        int amountOfHerbivores = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        sumOfPredators += amountOfOmnivores + amountOfCarnivores;
        sumOfChased += amountOfHerbivores;


        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        sumOfPredators += amountOfOmnivores + amountOfCarnivores;
        sumOfChased += amountOfHerbivores;

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        sumOfPredators += amountOfOmnivores + amountOfCarnivores;
        sumOfChased += amountOfHerbivores;

        double expected = ((double) sumOfChased) / ((double) (sumOfChased+sumOfPredators));

        double result = statistics.getSimulationStats().absolutePercentageChased();

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAllLivingDinosaurs(int withOmnivores){


        int amountOfOmnivores = 2 * withOmnivores;
        int amountOfCarnivores = 3;
        int amountOfHerbivores = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        int update1 = amountOfOmnivores + amountOfCarnivores + amountOfHerbivores;


        amountOfOmnivores = 1 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        int update2 = amountOfOmnivores + amountOfCarnivores + amountOfHerbivores;

        amountOfOmnivores = 0 * withOmnivores;
        amountOfCarnivores = 2;
        amountOfHerbivores = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores, amountOfCarnivores, amountOfHerbivores, 0, attributeMultiplier);
        int update3 = amountOfOmnivores + amountOfCarnivores + amountOfHerbivores;

        List<Integer> expected = List.of(update1, update2, update3);

        List<Integer> result = statistics.getSimulationStats().allLivingDinosaurs();

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAllLivingSpecies(int withOmnivores){

        int amountOfOmnivores1 = 2 * withOmnivores;
        int amountOfCarnivores1 = 3;
        int amountOfHerbivores1 = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores1, amountOfCarnivores1, amountOfHerbivores1, 0, attributeMultiplier);



        int amountOfOmnivores2 = 1 * withOmnivores;
        int amountOfCarnivores2 = 2;
        int amountOfHerbivores2 = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores2, amountOfCarnivores2, amountOfHerbivores2, 0, attributeMultiplier);


        int amountOfOmnivores3 = 0 * withOmnivores;
        int amountOfCarnivores3 = 2;
        int amountOfHerbivores3 = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores3, amountOfCarnivores3, amountOfHerbivores3, 0, attributeMultiplier);


        Map<String, List<Integer>> expected = new HashMap<>(Map.of(
                "omnivore", List.of(amountOfOmnivores1, amountOfOmnivores2, amountOfOmnivores3),
                "carnivore", List.of(amountOfCarnivores1, amountOfCarnivores2, amountOfCarnivores3),
                "herbivore", List.of(amountOfHerbivores1, amountOfHerbivores2, amountOfHerbivores3)
        ));
        if (withOmnivores==0) {
            expected.remove("omnivore");
        }

        List<String> speciesList = statistics.getSimulationStats().allSpecies();
        List<List<Integer>> result = statistics.getSimulationStats().allLivingSpecies();
        Map<String, List<Integer>> speciesMap = mapToSpecies(speciesList, result);

        assertAll("All Species Lists must be equivalent",
                () -> assertEquals(expected.get("omnivore"), speciesMap.get("omnivore")),
                () -> assertEquals(expected.get("carnivore"), speciesMap.get("carnivore")),
                () -> assertEquals(expected.get("herbivore"), speciesMap.get("herbivore"))
        );

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAllSpecies(int withOmnivores){
        int amountOfOmnivores1 = 2 * withOmnivores;
        int amountOfCarnivores1 = 3;
        int amountOfHerbivores1 = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores1, amountOfCarnivores1, amountOfHerbivores1, 0, attributeMultiplier);

        List<String> expected = new ArrayList<>(List.of("omnivore", "carnivore", "herbivore"));
        if (withOmnivores==0) {
            expected.remove("omnivore");
        }

        List<String> result = statistics.getSimulationStats().allSpecies();

        assertEquals(expected, result);

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAllLivingPredators(int withOmnivores){
        int amountOfOmnivores1 = 2 * withOmnivores;
        int amountOfCarnivores1 = 3;
        int amountOfHerbivores1 = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores1, amountOfCarnivores1, amountOfHerbivores1, 0, attributeMultiplier);



        int amountOfOmnivores2 = 1 * withOmnivores;
        int amountOfCarnivores2 = 2;
        int amountOfHerbivores2 = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores2, amountOfCarnivores2, amountOfHerbivores2, 0, attributeMultiplier);


        int amountOfOmnivores3 = 0 * withOmnivores;
        int amountOfCarnivores3 = 2;
        int amountOfHerbivores3 = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores3, amountOfCarnivores3, amountOfHerbivores3, 0, attributeMultiplier);

        List<Integer> expected = List.of(amountOfOmnivores1+amountOfCarnivores1, amountOfOmnivores2+amountOfCarnivores2, amountOfOmnivores3+amountOfCarnivores3);

        List<Integer> result = statistics.getSimulationStats().allLivingPredators();

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAllLivingChased(int withOmnivores){
        int amountOfOmnivores1 = 2 * withOmnivores;
        int amountOfCarnivores1 = 3;
        int amountOfHerbivores1 = 3;
        double attributeMultiplier = 1;
        addNewStatsUpdate(10, amountOfOmnivores1, amountOfCarnivores1, amountOfHerbivores1, 0, attributeMultiplier);



        int amountOfOmnivores2 = 1 * withOmnivores;
        int amountOfCarnivores2 = 2;
        int amountOfHerbivores2 = 3;
        attributeMultiplier = 1.2;
        addNewStatsUpdate(10, amountOfOmnivores2, amountOfCarnivores2, amountOfHerbivores2, 0, attributeMultiplier);


        int amountOfOmnivores3 = 0 * withOmnivores;
        int amountOfCarnivores3 = 2;
        int amountOfHerbivores3 = 1;
        attributeMultiplier = 0.5;
        addNewStatsUpdate(10, amountOfOmnivores3, amountOfCarnivores3, amountOfHerbivores3, 0, attributeMultiplier);

        List<Integer> expected = List.of(amountOfHerbivores1, amountOfHerbivores2, amountOfHerbivores3);

        List<Integer> result = statistics.getSimulationStats().allLivingChased();

        assertEquals(expected, result);
    }

    @Test
    void getSimulationTimeList(){
        //arrange
        List<Integer> timeSinceList = List.of(
                10,
                15,
                20
        );

        List<SimulationTime> expectedSimulationTimeList = new ArrayList<>();

        for (int timeSince:
             timeSinceList) {
            expectedSimulationTimeList.add(makeSimulationTime(timeSince));
        }

        //act
        for (int timeSince:
             timeSinceList) {
            addNewStatsUpdate(timeSince, 0, 0, 0, 0, 0);
        }

        //assert
        List<SimulationTime> result = statistics.getSimulationStats().simulationTimeList();

        assertAll("All simulationTimes must be equal",
                () -> assertEquals(expectedSimulationTimeList.get(0).getTime(), result.get(0).getTime()),
                () -> assertEquals(expectedSimulationTimeList.get(1).getTime(), result.get(1).getTime()),
                () -> assertEquals(expectedSimulationTimeList.get(2).getTime(), result.get(2).getTime())
        );
    }

    private Map<String, List<Integer>> mapToSpecies(List<String> speciesList, List<List<Integer>> listList) {
        Map<String, List<Integer>> speciesMap = new HashMap<>();

        for (int i = 0; i < speciesList.size(); i++) {
            String speciesName = speciesList.get(i);
            speciesMap.put(speciesName, new ArrayList<>());


            for (List<Integer> update :
                    listList) {
                speciesMap.get(speciesName).add(update.get(i));
            }
        }
        return speciesMap;
    }

    private double getExpectedPercentageForDinoType(int amountOfPredators, int amountOfHerbivores, DinoType dinoType) {
        double allDinos = amountOfPredators + amountOfHerbivores;

        if (dinoType == DinoType.PREDATOR) {
            return amountOfPredators / allDinos;
        } else {
            return amountOfHerbivores / allDinos;
        }
    }

    private double getExpectedAverageDietForDinosPerUpdate(Map<Dinosaur, Integer> dinosaurs, double attributeMultiplier, DietType dietType){

        List<Double> percentages = new ArrayList<>();

        for (Dinosaur baseDinosaur:
                dinosaurs.keySet()) {
            for (int i = 0; i < dinosaurs.get(baseDinosaur); i++) {
                percentages.add(getDietPercentage(baseDinosaur, attributeMultiplier, dietType));
            }
        }

        double sumPercentage = 0;
        for (double percentage:
             percentages) {
            sumPercentage += percentage;
        }

        return percentages.size() != 0 ? sumPercentage / percentages.size() : 0;

    }

    private double getDietPercentage(Dinosaur baseDinosaur, double attributeMultiplier, DietType dietType) {
        double max = switch(dietType) {
            case NUTRITION -> baseDinosaur.getMaxNutrition();
            default -> baseDinosaur.getMaxHydration();
        };

        double current = switch (dietType) {
            case NUTRITION -> baseDinosaur.getNutrition();
            default -> baseDinosaur.getHydration();
        };

        return multiplyAttribute(current, max, attributeMultiplier) / max;
    }

    private void addNewStatsUpdate(int timeSince, int amountOfOmnivores, int amountOfCarnivores, int amountOfHerbivores, int amountOfPlants, double attributeMultiplier){
        statistics.addSimulationObjectList(
                makeSimulationObjectList(
                        amountOfOmnivores,
                        amountOfCarnivores,
                        amountOfHerbivores,
                        amountOfPlants,
                        attributeMultiplier
                ),
                makeSimulationTime(
                        timeSince
                )
        );
    }

    private SimulationTime makeSimulationTime(int timeSince) {
        SimulationTime newSimulationTime = new SimulationTime(baseSimulationTime.getTime());
        newSimulationTime.setTime(newSimulationTime.getTime()+timeSince);
        return newSimulationTime;
    }

    private List<SimulationObject> makeSimulationObjectList(int amountOfOmnivores, int amountOfCarnivores, int amountOfHerbivores, int amountOfPlants, double attributeMultiplier) {
        List<SimulationObject> simulationObjectList = new ArrayList<>();

        for (int i = 0; i < amountOfOmnivores; i++) {
            simulationObjectList.add(makeDinosaur(Dinosaur.dietType.OMNIVORE, attributeMultiplier));
        }

        for (int i = 0; i < amountOfCarnivores; i++) {
            simulationObjectList.add(makeDinosaur(Dinosaur.dietType.CARNIVORE, attributeMultiplier));
        }

        for (int i = 0; i < amountOfHerbivores; i++) {
            simulationObjectList.add(makeDinosaur(Dinosaur.dietType.HERBIVORE, attributeMultiplier));
        }

        for (int i = 0; i < amountOfPlants; i++) {
            simulationObjectList.add(makePlant(attributeMultiplier));
        }

        return simulationObjectList;
    }

    private Plant makePlant(double attributeMultiplier) {
        Plant plant = basePlant;

        plant.setGrowth(multiplyAttribute(plant.getGrowth(), Plant.MAX_GROWTH, attributeMultiplier));

        return null;
    }

    private Dinosaur makeDinosaur(Dinosaur.dietType dietType, double attributeMultiplier) {
        Dinosaur dinosaur = switch (dietType) {
            case OMNIVORE ->    getBaseOmnivore();
            case CARNIVORE ->   getBaseCarnivore();
            default ->          getBaseHerbivore();
        };

        dinosaur.setNutrition(multiplyAttribute(dinosaur.getNutrition(), dinosaur.getMaxNutrition(), attributeMultiplier));
        dinosaur.setHydration(multiplyAttribute(dinosaur.getHydration(), dinosaur.getMaxHydration(), attributeMultiplier));

        if (dinosaur.getNutrition() == 0 || dinosaur.getHydration() == 0){
            return null;
        } else {
            return dinosaur;
        }
    }

    private double multiplyAttribute(double attribute, double maxValue, double attributeMultiplier) {
        double multipliedAttribute = attribute * attributeMultiplier;
        multipliedAttribute = Math.min(multipliedAttribute, maxValue);
        return multipliedAttribute;
    }

    private Dinosaur getBaseHerbivore() {
        Dinosaur dinosaur;
        dinosaur = baseHerbivore.copyOf();
        dinosaur.setNutrition(baseHerbivoreNutrition);
        dinosaur.setHydration(baseHerbivoreHydration);
        return dinosaur;
    }

    private Dinosaur getBaseCarnivore() {
        Dinosaur dinosaur;
        dinosaur = baseCarnivore.copyOf();
        dinosaur.setNutrition(baseCarnivoreNutrition);
        dinosaur.setHydration(baseCarnivoreHydration);
        return dinosaur;
    }

    private Dinosaur getBaseOmnivore() {
        Dinosaur dinosaur;
        dinosaur = baseOmnivore.copyOf();
        dinosaur.setNutrition(baseOmnivoreNutrition);
        dinosaur.setHydration(baseOmnivoreHydration);
        return dinosaur;
    }

    private Image makeMockImage(String type){
        String filename = type.equalsIgnoreCase("dinosaur") ? "testDinosaur.png" : "testPlant.png";

        return new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename)));
    }
}
