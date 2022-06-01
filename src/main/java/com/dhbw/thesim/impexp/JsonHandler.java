package com.dhbw.thesim.impexp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.json.*;

/**
 * @author eric stefan
 */
public class JsonHandler {
    static String workingDirectory;
    public enum SimulationObjectType{
        DINO,
        PLANT,
        LANDSCAPE
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
        }
        else if(OS.contains("LINUX"))//Linux
        {
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/.local/share/TheSim";
        }else{//macOS
            workingDirectory = ".";
        }
        File file = new File(workingDirectory);
        if (!file.isDirectory()) {
            if (file.mkdirs()) {
                System.out.println("Directory 'TheSim' was created successfully");
            }else {
                System.out.println("Directory 'TheSim' could not be created");
            }
        }
    }

    /**
     * Parse the local "simulationsobjektKonfig.json" and return its values in a HashMap
     * @return HashMap containing dinosaurs or plants each nested in an HashMap with its names
     * @throws IOException when the "defaultSimulationsobjektKonfig.json" cannot be found
     */
    public static HashMap importSimulationObjectConfig(SimulationObjectType type) throws IOException {
        //this is the idea of the structure:
        //HashMap<"Dinosauchus",<HashMap<"Bild","dinosauchus.png">>>
        //or HashMap<"Farn",<HashMap<"Bild","farn.png">>>
        HashMap<String,HashMap<String, Object>> simulationObjects = new HashMap<>();

        //get the default "SimulationObject" configuration file
        InputStream inputStreamDefaultConfigFile = JsonHandler.class.getResourceAsStream("/configuration-files/defaultSimulationsobjektKonfig.json");
        if (inputStreamDefaultConfigFile==null){
            throw new FileNotFoundException("Cannot find resource file 'defaultSimulationsobjektKonfig.json'");
        }
        //if there is no locally stored "SimulationObject" configuration file, load the default file in the working directory
        File configFile = new File(workingDirectory+"/simulationsobjektKonfig.json");
        if (!configFile.exists()) {
            Files.copy(inputStreamDefaultConfigFile, Path.of(workingDirectory + "/simulationsobjektKonfig.json"));
        }

        //get the local "SimulationObject" configuration file and parse it into a JsonArray
        FileInputStream inputStreamConfigFile = new FileInputStream(configFile);
        JSONTokener jsonTokener = new JSONTokener(inputStreamConfigFile);
        JSONArray jsonArraySimulationObjects = new JSONArray(jsonTokener);

        if (type==SimulationObjectType.DINO) {
            //add all dinosaurs to the HashMap "simulationObjects"
            JSONArray jsonArrayDinosaurs = (JSONArray) ((JSONObject) (jsonArraySimulationObjects.get(0))).get("Dinosaurierart");
                for (int i = 0; jsonArrayDinosaurs.length() > i; i++) {
                HashMap<String, Object> dino = new HashMap<>();
                String currentName="";
                JSONObject jsonObjectDino = (JSONObject) jsonArrayDinosaurs.get(i);
                for (String key : jsonObjectDino.keySet()) {
                    if (key.equals("Name")){
                        currentName=(jsonObjectDino.get(key)).toString();
                    }else{
                        dino.put(key, jsonObjectDino.get(key));
                    }
                }
                simulationObjects.put(currentName,dino);
            }
        }else if (type==SimulationObjectType.PLANT){
            //add all plants to the HashMap "simulationObjects"
            JSONArray jsonArrayPlants = (JSONArray) ((JSONObject)(jsonArraySimulationObjects.get(1))).get("Pflanzenart");
            for(int i=0;jsonArrayPlants.length()>i;i++){
                HashMap<String,Object> plant = new HashMap<>();
                String currentName="";
                JSONObject jsonObjectPlant = (JSONObject) jsonArrayPlants.get(i);
                for (String key:jsonObjectPlant.keySet()) {
                    if (key.equals("Name")){
                        currentName=(jsonObjectPlant.get(key)).toString();
                    }else{
                        plant.put(key, jsonObjectPlant.get(key));
                    }
                }
                simulationObjects.put(currentName,plant);
            }
        }else{
            System.out.println("No valid SimulationObject Type.");
        }
        return simulationObjects;
    }

    public static HashMap importScenarioConfig(SimulationObjectType type) throws IOException {
        HashMap<String,Object> hashMap = new HashMap<>();

        InputStream inputStreamDefaultConfigFile = JsonHandler.class.getResourceAsStream("/configuration-files/defaultScenarioKonfig.json");
        if (inputStreamDefaultConfigFile==null){
            throw new FileNotFoundException("Cannot find resource file 'defaultScenarioKonfig.json'");
        }
        File configFile = new File(workingDirectory+"/defaultScenarioKonfig.json");
        if (!configFile.exists()) {
            Files.copy(inputStreamDefaultConfigFile, Path.of(workingDirectory + "/defaultScenarioKonfig.json"));
        }

        FileInputStream inputStreamConfigFile = new FileInputStream(configFile);
        JSONTokener jsonTokener = new JSONTokener(inputStreamConfigFile);
        JSONArray jsonArrayScenario = new JSONArray(jsonTokener);

        if (type==SimulationObjectType.DINO){
            JSONArray jsonArrayDinosaurs = (JSONArray) ((JSONObject) (jsonArrayScenario.get(0))).get("Dinosaurier");
            for (int i = 0; jsonArrayDinosaurs.length() > i; i++) {
                JSONObject jsonObjectDino = (JSONObject) jsonArrayDinosaurs.get(i);
                String key = jsonObjectDino.keySet().toArray()[0].toString();
                hashMap.put(key,jsonObjectDino.get(key));
            }
        }else if (type==SimulationObjectType.PLANT){
            JSONArray jsonArrayPlants = (JSONArray) ((JSONObject) (jsonArrayScenario.get(1))).get("Pflanzen");
            for (int i = 0; jsonArrayPlants.length() > i; i++) {
                JSONObject jsonObjectPlant = (JSONObject) jsonArrayPlants.get(i);
                String key = jsonObjectPlant.keySet().toArray()[0].toString();
                hashMap.put(key,jsonObjectPlant.get(key));
            }
        }else {
            String name = ((JSONObject) (jsonArrayScenario.get(2))).get("Landschaft").toString();
            hashMap.put("Landscape",name);
        }

        return hashMap;
    }

    private static JSONArray createScenarioConfigSimulationObjects(HashMap simulationObjects){

        JSONArray InnerJsonArray = new JSONArray();

        int i=0;
        for (Object key:simulationObjects.keySet()) {
            JSONObject simulationObject = new JSONObject();
            simulationObject.put((String) key,simulationObjects.get(key));
            InnerJsonArray.put(i,simulationObject);
            i++;
        }
        return InnerJsonArray;
    }

    public static void exportScenarioConfig(HashMap dinosaurs, HashMap plants, String landscapeValue, String fileName) throws IOException {

        JSONArray wrappingJsonArray = new JSONArray();

        JSONObject wrappingJsonObjectDino = new JSONObject();
        wrappingJsonObjectDino.put("Dinosaurier",createScenarioConfigSimulationObjects(dinosaurs));
        wrappingJsonArray.put(0,wrappingJsonObjectDino);

        JSONObject wrappingJsonObjectPlant = new JSONObject();
        wrappingJsonObjectPlant.put("Pflanzen",createScenarioConfigSimulationObjects(plants));
        wrappingJsonArray.put(1,wrappingJsonObjectPlant);

        JSONObject JsonObjectLandscape = new JSONObject();
        JsonObjectLandscape.put("Landschaft",landscapeValue);
        wrappingJsonArray.put(2,JsonObjectLandscape);

        try {
            File configFile = new File(workingDirectory+"/"+fileName+".json");
            if (configFile.createNewFile()) {
                System.out.println("File created: " + fileName);
                try {
                    FileWriter fileWriter = new FileWriter(workingDirectory+"/"+fileName+".json");
                    fileWriter.write(String.valueOf(wrappingJsonArray));
                    fileWriter.close();
                    System.out.println("Successfully written to file: "+ fileName);
                } catch (IOException e) {
                    System.out.println("The file "+fileName+".json could not be written to.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("File already exists. So please make sure that the file "+fileName+".json does not exist yet, so that no data will be overwritten");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
