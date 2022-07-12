package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.util.SimulationTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        dinosaur.setNutrition(baseCarnivoreHydration);
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
