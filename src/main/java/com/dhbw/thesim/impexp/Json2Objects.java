package com.dhbw.thesim.impexp;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * For creating the needed objects based on the parsed json-files gotten from the class JsonHandler.
 * @author Eric Stefan
 */
public class Json2Objects {

    public enum Type {
        empty,
        imported
    }

    /**
     * Get the amount of the simulation objects as specified in the scenario configuration file also with the names of their pictures. Needed especially for the GUI.
     * The importance of this function is to check, if the simulation objects specified in the configuration file even exist.
     * @param scenarioConfigFileName
     * @param type
     * @param simulationObjectType
     * @return
     * @throws IOException
     */
    //TODO fertig schreiben
    public static HashMap getSimulationObjects(String scenarioConfigFileName, Type type, JsonHandler.SimulationObjectType simulationObjectType) throws IOException {

        if (type==Type.empty){

       }else if(type==Type.imported){


       }


        HashMap<String, Object[]> formattedSimulationObjects = new HashMap<>();
        HashMap<String, HashMap<String, Object>> simulationObjects = new HashMap<>();
        HashMap<String, Object> importedSimulationObjects = new HashMap<>();

        if (simulationObjectType == JsonHandler.SimulationObjectType.DINO) {
            simulationObjects = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO);
            importedSimulationObjects = JsonHandler.importScenarioConfig(scenarioConfigFileName,JsonHandler.ScenarioConfigParams.DINO);
        } else if (simulationObjectType == JsonHandler.SimulationObjectType.PLANT) {
            simulationObjects = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.PLANT);
            importedSimulationObjects = JsonHandler.importScenarioConfig(scenarioConfigFileName,JsonHandler.ScenarioConfigParams.PLANT);
        } else if (simulationObjectType == JsonHandler.SimulationObjectType.LANDSCAPE) {
            if (type == Type.empty) {
                //default landscape
                //TODO wie kann man landscape ändern? -> gucke im Pflichtenheft
                Object[] object = {"defaultLandscape"};
                formattedSimulationObjects.put("Landscape", object);
            } else if (type == Type.imported) {

            }
            importedSimulationObjects = JsonHandler.importScenarioConfig(scenarioConfigFileName,JsonHandler.ScenarioConfigParams.LANDSCAPE);
            String key = importedSimulationObjects.values().toArray()[0].toString();
            if (key != null) {
                //TODO check if such file exists with name of value
                File landscapeFile = new File(JsonHandler.workingDirectory + "/" + key + ".json");
                if (landscapeFile.exists()) {
                    Object[] object = {importedSimulationObjects.get(key)};
                    formattedSimulationObjects.put(key, object);
                } else {
                    System.out.println("The specified file " + key + ".json does not exist.");
                    System.out.println("For the default landscape you should leave the value empty.");
                }
            }

            System.out.println(importedSimulationObjects.values().toArray()[0].getClass());

        } else {
            System.out.println("No valid simulationObjectType.");
        }

        for (Object key : simulationObjects.keySet()) {
            Object[] object = new Object[2];
            object[0] = ((HashMap) (simulationObjects.get(key))).get("Bild");
            formattedSimulationObjects.put(key.toString(), object);
        }

        if (type == Type.empty) {
            for (Object key : formattedSimulationObjects.keySet()) {
                formattedSimulationObjects.get(key.toString())[1] = 0;
            }
        } else if (type == Type.imported) {
            for (Object key : importedSimulationObjects.keySet()) {
                key = key.toString();
                if (formattedSimulationObjects.containsKey(key) && formattedSimulationObjects.get(key)[1] == null) {
                    formattedSimulationObjects.get(key)[1] = importedSimulationObjects.get(key);
                }
            }
            for (Object key : formattedSimulationObjects.keySet()) {
                if (formattedSimulationObjects.get(key.toString())[1] == null)
                    formattedSimulationObjects.get(key.toString())[1] = 0;
            }
        }
        System.out.println(((Object []) formattedSimulationObjects.get("Deinosuchus"))[1]);
        return formattedSimulationObjects;



        /*
        maybe needed here:
            else if(type==Type.imported){
                if (objects[0] instanceof String){
                    String scenarioConfigFileName = (String) objects[0];
                    dinosaursAmount = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.DINO);
                    plantsAmount = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.PLANT);
                    Object temp = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.PLANT_GROWTH).get("PlantGrowth");
                    if (temp.getClass() == Integer.class){
                        plantGrowth = ((Integer) temp).doubleValue();
                    }else{
                        plantGrowth = ((BigDecimal) temp).doubleValue();
                    }
                }
            }
         */
    }

    /**
     * Initialize all simulation objects based on the delivered HashMaps by calling initDinos() and initPlants().
     * @param dinosaursAmount HashMap<String, Integer> contains the amount of each dinosaur species
     * @param plantsAmount HashMap<String, Integer> contains the amount of each plant species
     * @param plantGrowth double value containing the plant growth
     * @return an ArrayList containing all SimulationObjects
     * @throws IOException if the simulation objects configuration file can not be found
     */
    public static ArrayList<SimulationObject> initSimObjects(HashMap<String, Integer> dinosaursAmount, HashMap<String, Integer> plantsAmount, double plantGrowth) throws IOException {
        ArrayList<SimulationObject> allSimulationObjects = new ArrayList<>();

        allSimulationObjects.addAll(initDinos(dinosaursAmount));
        allSimulationObjects.addAll(initPlants(plantsAmount, plantGrowth));

        System.out.println("allSimulationObjects: "+allSimulationObjects);
        System.out.println(allSimulationObjects.get(3).getInteractionRange());
        return allSimulationObjects;
    }

    /**
     * Returns a double value in between the two given ones.
     * @param limitValuesObject contains two values as the so-called variance. (first value as bottom limit and second value as upper limit)
     * @return a coincidental chosen double value in between
     */
    private static double returnValueInBetween(Object limitValuesObject) {
        double[] limitValues = (double[]) limitValuesObject;
        double valueInBetween = 0.0;

        double min = limitValues[0];
        double max = limitValues[1];
        for (double i = min; i <= max; i++) {
            valueInBetween = (Math.random() * (max - min)) + min;
        }
        valueInBetween=Math.round(valueInBetween*100);
        valueInBetween/=100;
        return valueInBetween;
    }

    /**
     * Based on a coincidence of 50/50 the char 'm' or 'f' will be returned.
     * @return the char 'm' or 'f'
     */
    private static char returnRandomGender() {
        if (Math.random() % 2 < 0.5) {
            return 'm';
        } else {
            return 'f';
        }
    }

    /**
     * This method will create all dinosaurs based on their particular specified amount, in compliance with their variance.
     * @param dinosaursAmount which contains the names of dinosaurs (as key) and their amount (as value)
     * @return all created dinosaurs (as SimulationObjects) in an ArrayList
     * @throws IOException if the simulation objects configuration file can not be found
     */
    private static ArrayList<Dinosaur> initDinos(HashMap<String, Integer> dinosaursAmount) throws IOException {
        ArrayList<Dinosaur> dinosaurs = new ArrayList<>();

        HashMap<String, HashMap<String, Object>> DinosaurSpecies = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO);

        //create the dinosaur objects based on the "dinosaursAmount" HashMap
        for (String speciesName : dinosaursAmount.keySet()) { //for each species
            for (int i = 0; i < dinosaursAmount.get(speciesName); i++) { //for each dinosaur of one species
                Dinosaur dino = new Dinosaur(
                        speciesName,
                        (String) DinosaurSpecies.get(speciesName).get("Bild"),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Nahrung")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Hydration")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Staerke")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Geschwindigkeit")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Fortpflanzungswilligkeit")),
                        ((BigDecimal) (DinosaurSpecies.get(speciesName)).get("Gewicht")).doubleValue(),
                        ((BigDecimal) (DinosaurSpecies.get(speciesName)).get("Länge")).doubleValue(),
                        ((BigDecimal) (DinosaurSpecies.get(speciesName)).get("Höhe")).doubleValue(),
                        (boolean) (DinosaurSpecies.get(speciesName)).get("KannSchwimmen"),
                        (boolean) (DinosaurSpecies.get(speciesName)).get("KannKlettern"),
                        ((String) (DinosaurSpecies.get(speciesName)).get("Nahrungsart")).charAt(0),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Sichtweite")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Interaktionsweite")),
                        returnRandomGender()
                );
                dinosaurs.add(dino);
            }
        }
        return dinosaurs;
    }

    /**
     * This method will create all plants based on their particular specified amount, in compliance with their variance.
     * @param plantsAmount which contains the names of plants (as key) and their amount (as value)
     * @return all created plants (as SimulationObjects) in an ArrayList
     * @throws IOException if the simulation objects configuration file can not be found
     */
    private static ArrayList<Plant> initPlants(HashMap<String, Integer> plantsAmount, double plantGrowth) throws IOException {
        ArrayList<Plant> plants = new ArrayList<>();

        HashMap<String, HashMap<String, Object>> PlantSpecies = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.PLANT);

        //create the plant objects based on the "plantsAmount" HashMap
        for (String speciesName : plantsAmount.keySet()) { //for each species
            for (int i = 0; i < plantsAmount.get(speciesName); i++) { //for each plant of one species
                Plant plant = new Plant(
                        speciesName,
                        (String) PlantSpecies.get(speciesName).get("Bild"),
                        returnValueInBetween((PlantSpecies.get(speciesName)).get("Interaktionsweite")),
                        plantGrowth
                );
                plants.add(plant);
            }
        }
        return plants;
    }
}
