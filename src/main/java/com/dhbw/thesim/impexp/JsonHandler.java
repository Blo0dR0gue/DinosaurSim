package com.dhbw.thesim.impexp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.*;

/**
 * For handling all json-files. Concretely it contains the import and export for different kinds of configuration json-files.
 *
 * @author eric stefan
 */
public class JsonHandler {
    //saving the operating systems specific path to its appdata folder
    private static String workingDirectory;

    public enum SimulationObjectType {
        DINO,
        PLANT
    }

    public enum ScenarioConfigParams {
        DINO,
        PLANT,
        LANDSCAPE,
        PLANT_GROWTH
    }

    /**
     * Helper function, to get the working directory of this simulation.
     *
     * @return the working directory for this app.
     */
    public static String getWorkingDirectory() {
        if (workingDirectory == null)
            setDirectory();
        return workingDirectory;
    }

    /**
     * Set the String workingDirectory based on the operating systems appdata specific folder and create a folder "TheSim" if needed
     */
    public static void setDirectory() {
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN"))//Windows
        {
            workingDirectory = System.getenv("AppData");
            workingDirectory += "/TheSim";
        } else if (OS.contains("LINUX"))//Linux
        {
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/.local/share/TheSim";
        } else {//macOS
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/Library/Application Support/TheSim";
        }
        File file = new File(workingDirectory);
        if (!file.isDirectory()) {
            if (file.mkdirs()) {
                System.out.println("Directory 'TheSim' was created successfully");
            } else {
                System.out.println("Directory 'TheSim' could not be created");
            }
        }
    }

    /**
     * All json-files with "scenario" in their names are returned. (independent if capitalized or not)
     * @return ArrayList<String> containing the proper file names
     * @throws FileNotFoundException if the "workingDirectory" does not exist yet
     */
    public static ArrayList<String> getExistingScenarioFileNames() throws FileNotFoundException {
        ArrayList<String> existingScenarioFiles = new ArrayList<>();

        //set the directoryPath
        File directoryPath = new File(workingDirectory);
        if (!directoryPath.isDirectory()){
            throw new FileNotFoundException("The directory: "+workingDirectory+" does not exist yet.");
        }
        //create a FilenameFilter that filters for all json-Files with scenario (independent if capitalized or not) in its names
        FilenameFilter textFileFilter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".json") && lowercaseName.contains("scenario");
            }
        };
        //use the FilenameFilter on all files in the "workingDirectory" and get only the proper ones
        existingScenarioFiles.addAll(List.of(Objects.requireNonNull(directoryPath.list(textFileFilter))));

        return existingScenarioFiles;
    }

    /**
     * For exporting the default Scenario Configuration File (which is included in the jar-file) into the "working Directory"
     *
     * @throws IOException when the "defaultScenarioConfig.json" cannot be found in the jar-file
     */
    public static void exportDefaultScenarioConfig() throws IOException {
        //get the default "Scenario" configuration file
        InputStream inputStreamDefaultConfigFile = JsonHandler.class.getResourceAsStream("/configuration-files/defaultScenarioConfig.json");
        if (inputStreamDefaultConfigFile == null) {
            throw new FileNotFoundException("Cannot find resource file 'defaultScenarioConfig.json'");
        }
        //if there is no locally stored "defaultScenarioConfig" configuration file, load the default file in the working directory
        File configFile = new File(workingDirectory + "/defaultScenarioConfig.json");
        if (!configFile.exists()) {
            Files.copy(inputStreamDefaultConfigFile, Path.of(workingDirectory + "/defaultScenarioConfig.json"));
        }
    }

    /**
     * For exporting the default Simulation Objects Configuration File (which is included in the jar-file) into the "working Directory"
     *
     * @throws IOException when the "defaultSimulationObjectsConfig.json" cannot be found in the jar-file
     */
    public static void exportDefaultSimulationObjectsConfig() throws IOException {
        //get the default "SimulationObjects" configuration file
        InputStream inputStreamDefaultConfigFile = JsonHandler.class.getResourceAsStream("/configuration-files/defaultSimulationObjectsConfig.json");
        if (inputStreamDefaultConfigFile == null) {
            throw new FileNotFoundException("Cannot find resource file 'defaultSimulationObjectsConfig.json'");
        }
        //if there is no locally stored "defaultSimulationObjectsConfig" configuration file, load the default file in the working directory
        File configFile = new File(workingDirectory + "/defaultSimulationObjectsConfig.json");
        if (!configFile.exists()) {
            Files.copy(inputStreamDefaultConfigFile, Path.of(workingDirectory + "/defaultSimulationObjectsConfig.json"));
        }
    }

    /**
     * Parse the locally stored "defaultSimulationObjectsConfig.json" and return all dinosaurs or plants in a HashMap
     *
     * @return HashMap containing dinosaurs or plants each nested in an HashMap with its names
     * @throws IOException when the "defaultSimulationObjectsConfig.json" cannot be found in the appdata folder "TheSim"
     */
    public static HashMap<String, HashMap<String, Object>> importSimulationObjectsConfig(SimulationObjectType type) throws IOException {
        //this is the idea of the structure:
        //HashMap<"Dinosauchus",<HashMap<"Bild","dinosauchus.png">>>
        //or HashMap<"Farn",<HashMap<"Bild","farn.png">>>
        HashMap<String, HashMap<String, Object>> simulationObjects = new HashMap<>();

        File configFile = new File(workingDirectory + "/defaultSimulationObjectsConfig.json");
        if (!configFile.exists()) {
            throw new FileNotFoundException("Cannot find resource file 'defaultSimulationObjectsConfig.json'");
        }

        //get the local "defaultSimulationObjectsConfig" configuration file and parse it into a JsonArray
        FileInputStream inputStreamConfigFile = new FileInputStream(configFile);
        JSONTokener jsonTokener = new JSONTokener(inputStreamConfigFile);
        JSONArray jsonArraySimulationObjects = new JSONArray(jsonTokener);
        //Json Array containing the simulation objects of the chosen SimulationObjectType
        JSONArray jsonArrayChosenSimulationObjects;

        if (type == SimulationObjectType.DINO) { //all dinosaurs should be returned
            //initialize the "jsonArrayChosenSimulationObjects" with all dinosaurs as Json Array
            jsonArrayChosenSimulationObjects = (JSONArray) ((JSONObject) (jsonArraySimulationObjects.get(0))).get("Dinosaurierart");
        } else if (type == SimulationObjectType.PLANT) { //all plants should be returned
            //initialize the "jsonArrayChosenSimulationObjects" with all plants as Json Array
            jsonArrayChosenSimulationObjects = (JSONArray) ((JSONObject) (jsonArraySimulationObjects.get(1))).get("Pflanzenart");
        } else {
            System.out.println("No valid SimulationObject Type.");
            return null;
        }

        //write each element (dinosaur or plant) of the "jsonArrayChosenSimulationObjects" Json Array into the HashMap "simulationObjects"
        //with all of its properties
        for (int i = 0; jsonArrayChosenSimulationObjects.length() > i; i++) {
            HashMap<String, Object> simulationObject = new HashMap<>();
            String currentName = "";
            JSONObject jsonObjectDino = (JSONObject) jsonArrayChosenSimulationObjects.get(i);
            for (String key : jsonObjectDino.keySet()) {
                //the value for the key "Name" will be the key of the HashMap "simulationObjects",
                //which is why it should not be added into the "simulationObject" HashMap
                if (key.equals("Name")) {
                    currentName = (jsonObjectDino.get(key)).toString();
                } else {
                    //Convert all inner Json Arrays into normal double-Arrays
                    if (jsonObjectDino.get(key).getClass() == JSONArray.class) {
                        JSONArray innerJsonArray = (JSONArray) jsonObjectDino.get(key);
                        double[] convertedArray = new double[2];
                        convertedArray[0] = innerJsonArray.getDouble(0);
                        convertedArray[1] = innerJsonArray.getDouble(1);
                        simulationObject.put(key, convertedArray);
                    } else {
                        simulationObject.put(key, jsonObjectDino.get(key));
                    }
                }
            }
            simulationObjects.put(currentName, simulationObject);
        }

        return simulationObjects;
    }

    /**
     * Get the desired scenarioConfigObjects in a HashMap, respectively import them.
     *
     * @param fileName of the file which scenarioConfigObjects should be imported
     * @param type     specifies which scenarioConfigObjects are wanted (dinosaurs, plants, landscape name or plant growth)
     * @return a HashMap which contains the names of the simulationObjects as key and the amount as value
     * Special cases are "landscape name" and "plant growth", where there is only one entry in the HashMap with static key
     * @throws IOException if the resource file with the name "fileName" could not be found
     */
    public static HashMap<String, Object> importScenarioConfig(String fileName, ScenarioConfigParams type) throws IOException {
        HashMap<String, Object> scenarioConfigObjects = new HashMap<>();

        File configFile = new File(workingDirectory + "/" + fileName + ".json");
        if (!configFile.exists()) {
            throw new FileNotFoundException("Cannot find resource file '" + fileName + ".json'");
        }
        FileInputStream inputStreamConfigFile = new FileInputStream(configFile);
        JSONTokener jsonTokener = new JSONTokener(inputStreamConfigFile);
        JSONArray jsonArrayScenario = new JSONArray(jsonTokener);

        // get all dinosaurs or plants or the landscape name or the plant growth value and store them into "scenarioConfigObjects" HashMap
        if (type == ScenarioConfigParams.DINO) {
            JSONArray jsonArrayDinosaurs = (JSONArray) ((JSONObject) (jsonArrayScenario.get(0))).get("Dinosaurier");
            for (int i = 0; jsonArrayDinosaurs.length() > i; i++) {
                JSONObject jsonObjectDino = (JSONObject) jsonArrayDinosaurs.get(i);
                String key = jsonObjectDino.keySet().toArray()[0].toString();
                scenarioConfigObjects.put(key, jsonObjectDino.get(key));
            }
        } else if (type == ScenarioConfigParams.PLANT) {
            JSONArray jsonArrayPlants = (JSONArray) ((JSONObject) (jsonArrayScenario.get(1))).get("Pflanzen");
            for (int i = 0; jsonArrayPlants.length() > i; i++) {
                JSONObject jsonObjectPlant = (JSONObject) jsonArrayPlants.get(i);
                String key = jsonObjectPlant.keySet().toArray()[0].toString();
                scenarioConfigObjects.put(key, jsonObjectPlant.get(key));
            }
        } else if (type == ScenarioConfigParams.LANDSCAPE) {
            String name = ((JSONObject) (jsonArrayScenario.get(2))).get("Landschaft").toString();
            scenarioConfigObjects.put("Landscape", name);
        } else if (type == ScenarioConfigParams.PLANT_GROWTH) {
            scenarioConfigObjects.put("PlantGrowth", (((JSONObject) (jsonArrayScenario.get(1))).get("Pflanzenwachstum")));
        }

        return scenarioConfigObjects;
    }

    /**
     * Write the "simulationObjects" HashMap as an JsonArray.
     *
     * @param simulationObjects is a HashMap which contains simulation objects
     * @return a JsonArray with all entrys of "simulationObjects" HashMap cling together
     */
    private static JSONArray createScenarioConfigSimulationObjects(HashMap<String, Integer> simulationObjects) {
        JSONArray innerJsonArray = new JSONArray();

        int i = 0;
        for (String key : simulationObjects.keySet()) {
            JSONObject simulationObject = new JSONObject();
            simulationObject.put(key, simulationObjects.get(key));
            innerJsonArray.put(i, simulationObject);
            i++;
        }
        return innerJsonArray;
    }

    /**
     * Create a Scenario Configuration json-file in the "workingDirectory" based on the following parameters.
     *
     * @param dinosaurs     is a HashMap containing all dinosaurs
     * @param plants        is a HashMap containing all plants
     * @param landscapeName is a String which contains the landscape name to be exported
     * @param plantGrowth   is a Double value which contains the plant growth
     * @param fileName      is the name of the file which will be created
     * @throws IOException if the file "fileName" could not be created or could not be written to
     */
    public static void exportScenarioConfig(HashMap<String, Integer> dinosaurs, HashMap<String, Integer> plants, String landscapeName, Double plantGrowth, String fileName) throws IOException {
        //wrappingJsonArray of all objects, in which all objects are added to
        JSONArray wrappingJsonArray = new JSONArray();

        JSONObject wrappingJsonObjectDino = new JSONObject();
        wrappingJsonObjectDino.put("Dinosaurier", createScenarioConfigSimulationObjects(dinosaurs));
        wrappingJsonArray.put(0, wrappingJsonObjectDino);

        JSONObject wrappingJsonObjectPlant = new JSONObject();
        wrappingJsonObjectPlant.put("Pflanzen", createScenarioConfigSimulationObjects(plants));
        wrappingJsonObjectPlant.put("Pflanzenwachstum", plantGrowth.doubleValue());
        wrappingJsonArray.put(1, wrappingJsonObjectPlant);

        JSONObject JsonObjectLandscape = new JSONObject();
        JsonObjectLandscape.put("Landschaft", landscapeName);
        wrappingJsonArray.put(2, JsonObjectLandscape);

        //create the file and write to it
        try {
            File configFile = new File(workingDirectory + "/" + fileName + "ScenarioConfiguration.json");
            if (!configFile.createNewFile()) {
                System.out.println("Warning: The file '" + fileName + ".json' will be overwritten.");
            } else {
                System.out.println("File created: " + fileName + "ScenarioConfiguration.json");
            }
            try {
                FileWriter fileWriter = new FileWriter(workingDirectory + "/" + fileName + "ScenarioConfiguration.json");
                fileWriter.write(String.valueOf(wrappingJsonArray));
                fileWriter.close();
                System.out.println("Successfully written to file: " + fileName + "ScenarioConfiguration.json");
            } catch (IOException e) {
                System.out.println("The file " + fileName + "ScenarioConfiguration.json could not be written to.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
