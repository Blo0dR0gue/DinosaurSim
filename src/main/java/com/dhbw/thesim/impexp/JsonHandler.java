package com.dhbw.thesim.impexp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.*;

/**
 * @author eric stefan
 */
public class JsonHandler {
    static String workingDirectory;

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
     * @return HashMap containing dinosaurs and plants each nested in an HashMap within an ArrayList
     * @throws IOException when the "defaultSimulationsobjektKonfig.json" cannot be found
     */
    public static HashMap importSimulationObjectConfig() throws IOException {
        //this is the idea of the structure:
        //HashMap1<"Dinosaurierart",ArrayList1<HashMap2<"Name","Dinosauchus">>>
        //HashMap1<"Pflanzenart",ArrayList2<HashMap3<"Name","Farn">>>
        HashMap<String,ArrayList> simulationObjects = new HashMap<>();
        ArrayList<HashMap> dinosaurs = new ArrayList<>();
        ArrayList<HashMap> plants = new ArrayList<>();

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

        //add all dinosaurs to the HashMap "simulationObjects" with key "Dinosaurierart"
        JSONArray jsonArrayDinosaurs = (JSONArray) ((JSONObject)(jsonArraySimulationObjects.get(0))).get("Dinosaurierart");
        for(int i=0;jsonArrayDinosaurs.length()>i;i++){
            HashMap<String,Object> dino = new HashMap<>();
            JSONObject jsonObjectDino = (JSONObject) jsonArrayDinosaurs.get(i);
            for (String key:jsonObjectDino.keySet()) {
                dino.put(key,jsonObjectDino.get(key));
            }
            dinosaurs.add(dino);
        }
        simulationObjects.put("Dinosaurierart",dinosaurs);

        //add all plants to the HashMap "simulationObjects" with key "Pflanzenart"
        JSONArray jsonArrayPlants = (JSONArray) ((JSONObject)(jsonArraySimulationObjects.get(1))).get("Pflanzenart");
        for(int i=0;jsonArrayPlants.length()>i;i++){
            HashMap<String,Object> plant = new HashMap<>();
            JSONObject jsonObjectPlant = (JSONObject) jsonArrayPlants.get(i);
            for (String key:jsonObjectPlant.keySet()) {
                plant.put(key,jsonObjectPlant.get(key));
            }
            plants.add(plant);
        }
        simulationObjects.put("Pflanzenart",plants);


        return simulationObjects;
    }
}
