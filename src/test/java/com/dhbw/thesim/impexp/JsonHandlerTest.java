package com.dhbw.thesim.impexp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Eric Stefan
 */
class JsonHandlerTest {

    @Test
    @DisplayName("Check if workingDirectory String is null")
    void getWorkingDirectory() {
        assertNotNull(JsonHandler.getWorkingDirectory());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    @DisplayName("Check if workingDirectory String is set correctly for Windows")
    void setDirectoryWindows() {
        assertEquals(System.getenv("AppData") + "/TheSim", JsonHandler.getWorkingDirectory());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    @DisplayName("Check if workingDirectory String is set correctly for Linux")
    void setDirectoryLinux() {
        assertEquals(System.getProperty("user.home") + "/.local/share/TheSim", JsonHandler.getWorkingDirectory());
    }

    @Test
    @EnabledOnOs(OS.MAC)
    @DisplayName("Check if workingDirectory String is set correctly for Mac")
    void setDirectoryMac() {
        assertEquals(System.getProperty("user.home") + "/Library/Application Support/TheSim", JsonHandler.getWorkingDirectory());
    }

    @Test
    @DisplayName("Check if all files returned by this method really are scenario.json files")
    void getExistingScenarioFileNames() {
        try {//if the method getExistingScenarioFileNames() returns not null -> check if the right file names were selected
            JsonHandler.setDirectory();
            var existingScenarioFiles = JsonHandler.getExistingScenarioFileNames();
            existingScenarioFiles.forEach(file -> assertTrue(file.toLowerCase().contains("scenario") && file.toLowerCase().endsWith(".json")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Import the defaultSimulationObjectsConfig and check if all plant-names are present")
    void importSimulationObjectsConfigPlants() {
        try {
            JsonHandler.setDirectory();

            var plants = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.PLANT);

            System.out.println(plants);

            var checkKeySet = new HashSet<String>();
            checkKeySet.add("Farn");
            checkKeySet.add("Ginkgoales");
            checkKeySet.add("Birke");

            var interactionRange = (double[]) plants.get("Farn").get("Interaktionsweite");

            assertAll(
                    () -> assertNotNull(plants),
                    () -> assertEquals(checkKeySet, plants.keySet()),
                    () -> assertEquals("farn.png", plants.get("Farn").get("Bild")),
                    () -> assertEquals(16.0, interactionRange[0]),
                    () -> assertEquals(30.0, interactionRange[1])
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Import the defaultSimulationObjectsConfig and check if all dinosaur names exist, as well if the parameters are set right")
    void importSimulationObjectsConfigDinosaurs() {
        try {
            JsonHandler.setDirectory();

            var dinosaurs = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO);

            var checkKeySet = new HashSet<String>();
            checkKeySet.add("Brachiosaurus");
            checkKeySet.add("Abrictosaurus");
            checkKeySet.add("Tyrannosaurus Rex");
            checkKeySet.add("Deinosuchus");

            var food = (double[]) dinosaurs.get("Brachiosaurus").get("Nahrung");
            var hydration = (double[]) dinosaurs.get("Brachiosaurus").get("Hydration");
            var strength = (double[]) dinosaurs.get("Brachiosaurus").get("Staerke");
            var speed = (double[]) dinosaurs.get("Brachiosaurus").get("Geschwindigkeit");
            var breedingWill = (double[]) dinosaurs.get("Brachiosaurus").get("Fortpflanzungswilligkeit");
            var visibility = (double[]) dinosaurs.get("Brachiosaurus").get("Sichtweite");
            var interactionRange = (double[]) dinosaurs.get("Brachiosaurus").get("Interaktionsweite");
            var weight = ((BigDecimal) dinosaurs.get("Brachiosaurus").get("Gewicht")).doubleValue();
            var length = ((BigDecimal) dinosaurs.get("Brachiosaurus").get("Laenge")).doubleValue();
            var height = ((BigDecimal) dinosaurs.get("Brachiosaurus").get("Hoehe")).doubleValue();
            var canSwim = (boolean) dinosaurs.get("Brachiosaurus").get("KannSchwimmen");
            var canClimb = (boolean) dinosaurs.get("Brachiosaurus").get("KannKlettern");
            var foodKind = ((String) dinosaurs.get("Brachiosaurus").get("Nahrungsart")).charAt(0);

            assertAll(
                    () -> assertNotNull(dinosaurs),
                    () -> assertEquals(checkKeySet, dinosaurs.keySet()),
                    () -> assertEquals(40.0, food[0]),
                    () -> assertEquals(50.0, food[1]),
                    () -> assertEquals(30.0, hydration[0]),
                    () -> assertEquals(45.0, hydration[1]),
                    () -> assertEquals(2.0, strength[0]),
                    () -> assertEquals(3.5, strength[1]),
                    () -> assertEquals(20.0, speed[0]),
                    () -> assertEquals(35.0, speed[1]),
                    () -> assertEquals(0.5, breedingWill[0]),
                    () -> assertEquals(1.5, breedingWill[1]),
                    () -> assertEquals(320.0, visibility[0]),
                    () -> assertEquals(400.0, visibility[1]),
                    () -> assertEquals(60.0, interactionRange[0]),
                    () -> assertEquals(70.0, interactionRange[1]),
                    () -> assertEquals(40000.0, weight),
                    () -> assertEquals(27.0, length),
                    () -> assertEquals(12.0, height),
                    () -> assertFalse(canSwim),
                    () -> assertFalse(canClimb),
                    () -> assertEquals('p', foodKind)
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Simulation Objects and check if they are converted correctly into a JSONObject inside a JSONArray")
    void createScenarioConfigSimulationObjects() {
        try {
            var method = JsonHandler.class.getDeclaredMethod("createScenarioConfigSimulationObjects", HashMap.class);

            var testSimulationObjects = new HashMap<String, Integer>();
            testSimulationObjects.put("Test1", 5);
            testSimulationObjects.put("Test2", 0);

            method.setAccessible(true);
            try {
                var result = method.invoke(null, testSimulationObjects);
                assertAll(
                        () -> assertTrue(result.getClass().equals(JSONArray.class)),
                        () -> {
                            var innerResultTest1 = ((JSONArray) (result)).get(0);
                            var innerResultTest2 = ((JSONArray) (result)).get(1);

                            assertEquals(5, ((JSONObject) (innerResultTest1)).get("Test1"));
                            assertEquals(0, ((JSONObject) (innerResultTest2)).get("Test2"));
                        }
                );
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Check if the landscape name is imported correctly out of the defaultScenarioConfiguration.json")
    void importScenarioConfigLandscape() {
        JsonHandler.setDirectory();
        try {
            var importedScenarioConfig = JsonHandler.importScenarioConfig("defaultScenarioConfiguration", JsonHandler.ScenarioConfigParams.LANDSCAPE);
            assertAll(
                    () -> assertNotNull(importedScenarioConfig),
                    () -> assertEquals("Landschaft1", importedScenarioConfig.get("Landscape").toString())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Check if the dinosaur values are imported correctly out of the defaultScenarioConfiguration.json")
    void importScenarioConfigDinosaurs() {
        JsonHandler.setDirectory();
        try {
            var importedScenarioConfig = JsonHandler.importScenarioConfig("defaultScenarioConfiguration", JsonHandler.ScenarioConfigParams.DINO);
            System.out.println(importedScenarioConfig);
            assertAll(
                    () -> assertNotNull(importedScenarioConfig),
                    () -> assertEquals(4, importedScenarioConfig.get("Deinosuchus")),
                    () -> assertEquals(4, importedScenarioConfig.get("Abrictosaurus")),
                    () -> assertEquals(2, importedScenarioConfig.get("Tyrannosaurus Rex")),
                    () -> assertEquals(6, importedScenarioConfig.get("Brachiosaurus"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}