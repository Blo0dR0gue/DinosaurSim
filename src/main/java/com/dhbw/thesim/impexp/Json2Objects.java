package com.dhbw.thesim.impexp;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.util.SpriteLibrary;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * For creating the needed objects based on the parsed json-files gotten from the class JsonHandler.
 *
 * @author Eric Stefan
 */
public class Json2Objects {

    public enum Type {
        NO_SCENARIO_FILE,
        WITH_SCENARIO_FILE
    }

    /**
     * Get the amount of the simulation objects as specified in the scenario configuration file also with the names of their pictures. Needed especially for the GUI.
     * Hereby it is also important to check, if the simulation objects specified in the configuration file even exist.
     *
     * @param type                   tells whether all simulation objects should be loaded with the amount of 0 or the amount specified in some scenario configuration file
     * @param scenarioConfigFileName is the name of the scenario configuration file, which should be read
     * @return a HashMap containing all simulation objects (that exist in the locally stored "defaultSimulationObjectsConfig.json"
     * @throws IOException if the scenarioConfigFileName file is not found
     */
    //TODO fertig schreiben
    public static HashMap<JsonHandler.ScenarioConfigParams, ArrayList<Object[]>> getParamsForGUI(Type type, String scenarioConfigFileName) throws IOException {
        //structure of the returning HashMap<ScenarioConfigParams, Object["name", "bild", amount]>
        HashMap<JsonHandler.ScenarioConfigParams, ArrayList<Object[]>> allFormattedSimulationObjects = new HashMap<>();
        //oneKindOfFormattedSimulationObjects will be inserted into allFormattedSimulationObjects containing the formatted values
        ArrayList<Object[]> oneKindOfFormattedSimulationObjects = new ArrayList<>();

        //simulationObjects stores all simulation objects that are mentioned in the defaultSimulationObjectsConfig.json
        HashMap<String, HashMap<String, Object>> simulationObjects;
        //importedScenarioConfigValues stores the values out of the scenario configuration file with the name of the parameter scenarioConfigFileName
        HashMap<String, Object> importedScenarioConfigValues;

        if (type == Type.NO_SCENARIO_FILE) { //get all dinosaurs and plants with an amount of 0
            //because there is no scenario configuration file which should be loaded, only all possible dinosaurs and plants are loaded with an amount of 0
            //additionally parameters like "landscape" or "plan growth" are set manually in the GUI

            simulationObjects = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO);
            for (Object key : simulationObjects.keySet()) {
                Object[] object = new Object[3];
                object[0] = key.toString();
                object[1] = simulationObjects.get(key.toString()).get("Bild");
                object[2] = 0;
                oneKindOfFormattedSimulationObjects.add(object);
            }
            allFormattedSimulationObjects.put(JsonHandler.ScenarioConfigParams.DINO, oneKindOfFormattedSimulationObjects);

            oneKindOfFormattedSimulationObjects = new ArrayList<>();
            simulationObjects = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.PLANT);
            for (Object key : simulationObjects.keySet()) {
                Object[] object = new Object[3];
                object[0] = key.toString();
                object[1] = simulationObjects.get(key.toString()).get("Bild");
                object[2] = 0;
                oneKindOfFormattedSimulationObjects.add(object);
            }
            allFormattedSimulationObjects.put(JsonHandler.ScenarioConfigParams.PLANT, oneKindOfFormattedSimulationObjects);

        } else if (type == Type.WITH_SCENARIO_FILE) { /*get all dinosaurs and plants that are mentioned in the "defaultSimulationObjectsConfig.json" file
                                                and initizalize their amount specified by the scenario configuration file,
                                                additionally get the landscape name and plant growth values*/

            //get the landscape name:
            String landscapeName = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.LANDSCAPE).values().toArray()[0].toString();
            oneKindOfFormattedSimulationObjects.add(new Object[]{landscapeName});
            allFormattedSimulationObjects.put(JsonHandler.ScenarioConfigParams.LANDSCAPE, oneKindOfFormattedSimulationObjects);

            //get the plant growth:
            oneKindOfFormattedSimulationObjects = new ArrayList<>();
            Object temp = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.PLANT_GROWTH).get("PlantGrowth");
            double plantGrowth = 0;
            if (temp.getClass() == Integer.class) {
                plantGrowth = ((Integer) temp).doubleValue();
            } else if (temp.getClass() == BigDecimal.class) {
                plantGrowth = ((BigDecimal) temp).doubleValue();
            }
            oneKindOfFormattedSimulationObjects.add(new Object[]{plantGrowth});
            allFormattedSimulationObjects.put(JsonHandler.ScenarioConfigParams.PLANT_GROWTH, oneKindOfFormattedSimulationObjects);

            //get the dinosaurs and their amount:
            simulationObjects = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO);
            importedScenarioConfigValues = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.DINO);
            oneKindOfFormattedSimulationObjects = new ArrayList<>();
            for (Object key : simulationObjects.keySet()) {
                Object[] object = new Object[3];
                object[0] = key.toString();
                object[1] = (simulationObjects.get(key.toString())).get("Bild").toString();
                if (importedScenarioConfigValues.get(key.toString()) == null) {
                    object[2] = 0;
                } else {
                    object[2] = importedScenarioConfigValues.get(key.toString());
                }
                oneKindOfFormattedSimulationObjects.add(object);
                allFormattedSimulationObjects.put(JsonHandler.ScenarioConfigParams.DINO, oneKindOfFormattedSimulationObjects);

            }

            //get the plants and their amount:
            simulationObjects = JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.PLANT);
            importedScenarioConfigValues = JsonHandler.importScenarioConfig(scenarioConfigFileName, JsonHandler.ScenarioConfigParams.PLANT);
            oneKindOfFormattedSimulationObjects = new ArrayList<>();
            for (Object key : simulationObjects.keySet()) {
                Object[] object = new Object[3];
                object[0] = key.toString();
                object[1] = (simulationObjects.get(key.toString())).get("Bild").toString();
                if (importedScenarioConfigValues.get(key.toString()) == null) {
                    object[2] = 0;
                } else {
                    object[2] = importedScenarioConfigValues.get(key.toString());
                }
                oneKindOfFormattedSimulationObjects.add(object);
                allFormattedSimulationObjects.put(JsonHandler.ScenarioConfigParams.PLANT, oneKindOfFormattedSimulationObjects);
            }
        }

        //TODO entfernen:
        //dieser nachfolgende Code ist fürs GUI Team zur Verständnis der Funktionsweise dieser Klasse
        for (int i = 0; i < 4; i++) { //print out dinosaurs
            Object[] temp = allFormattedSimulationObjects.get(JsonHandler.ScenarioConfigParams.DINO).get(i);
            System.out.println("dinosaurs:\t" + temp[0] + "\t" + temp[1] + "\t" + temp[2]);
        }
        for (int i = 0; i < 2; i++) { //print out plants
            Object[] temp = allFormattedSimulationObjects.get(JsonHandler.ScenarioConfigParams.PLANT).get(i);
            System.out.println("plants:\t" + temp[0] + "\t" + temp[1] + "\t" + temp[2]);
        }
        //if a scenario configuration file was imported, this values are also printable
        if (type == Type.WITH_SCENARIO_FILE) {
            //get landscape name
            System.out.println("landscape:\t" + ((Object[]) allFormattedSimulationObjects.get(JsonHandler.ScenarioConfigParams.LANDSCAPE).get(0))[0]);
            //get plant growth
            System.out.println("plant growth:\t" + ((Object[]) allFormattedSimulationObjects.get(JsonHandler.ScenarioConfigParams.PLANT_GROWTH).get(0))[0]);
        }

        return allFormattedSimulationObjects;
    }

    /**
     * Initialize all simulation objects based on the delivered HashMaps by calling initDinos() and initPlants().
     *
     * @param dinosaursAmount HashMap<String, Integer> contains the amount of each dinosaur species
     * @param plantsAmount    HashMap<String, Integer> contains the amount of each plant species
     * @param plantGrowth     double value containing the plant growth
     * @return an ArrayList containing all SimulationObjects
     * @throws IOException if the simulation objects configuration file can not be found
     */
    public static ArrayList<SimulationObject> initSimObjects(HashMap<String, Integer> dinosaursAmount, HashMap<String, Integer> plantsAmount, double plantGrowth) throws IOException {
        ArrayList<SimulationObject> allSimulationObjects = new ArrayList<>();

        allSimulationObjects.addAll(initDinos(dinosaursAmount));
        allSimulationObjects.addAll(initPlants(plantsAmount, plantGrowth));

        System.out.println("allSimulationObjects: " + allSimulationObjects);
        System.out.println(allSimulationObjects.get(3).getInteractionRange());
        return allSimulationObjects;
    }

    /**
     * Returns a double value in between the two given ones.
     *
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
        valueInBetween = Math.round(valueInBetween * 100);
        valueInBetween /= 100;
        return valueInBetween;
    }

    /**
     * Based on a coincidence of 50/50 the char 'm' or 'f' will be returned.
     *
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
     *
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
                assert DinosaurSpecies != null;
                Dinosaur dino = new Dinosaur(
                        speciesName,
                        SpriteLibrary.getInstance().getImage((String) DinosaurSpecies.get(speciesName).get("Bild")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Nahrung")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Hydration")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Staerke")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Geschwindigkeit")),
                        returnValueInBetween((DinosaurSpecies.get(speciesName)).get("Fortpflanzungswilligkeit")),
                        ((BigDecimal) (DinosaurSpecies.get(speciesName)).get("Gewicht")).doubleValue(),
                        ((BigDecimal) (DinosaurSpecies.get(speciesName)).get("Laenge")).doubleValue(),
                        ((BigDecimal) (DinosaurSpecies.get(speciesName)).get("Hoehe")).doubleValue(),
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
     *
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
                assert PlantSpecies != null;
                Plant plant = new Plant(
                        speciesName,
                        SpriteLibrary.getInstance().getImage((String) PlantSpecies.get(speciesName).get("Bild")),
                        returnValueInBetween((PlantSpecies.get(speciesName)).get("Interaktionsweite")),
                        plantGrowth
                );
                plants.add(plant);
            }
        }
        return plants;
    }
}
