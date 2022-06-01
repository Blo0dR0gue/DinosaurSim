package com.dhbw.thesim.impexp;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Eric Stefan
 */
public class Json2Objects {

    public enum Type{
        empty,
        imported
    }

    public static HashMap getSimulationObjects(Type type, JsonHandler.SimulationObjectType simulationObjectType) throws IOException {
        HashMap<String, Object[]> formattedSimulationObjects = new HashMap<>();
        HashMap<String,HashMap<String, Object>> simulationObjects = new HashMap();
        HashMap<String,Object> importedSimulationObjects = new HashMap<>();

        if (simulationObjectType == JsonHandler.SimulationObjectType.DINO){
            simulationObjects=JsonHandler.importSimulationObjectConfig(JsonHandler.SimulationObjectType.DINO);
            importedSimulationObjects = JsonHandler.importScenarioConfig(JsonHandler.SimulationObjectType.DINO);
        }else if(simulationObjectType == JsonHandler.SimulationObjectType.PLANT){
            simulationObjects=JsonHandler.importSimulationObjectConfig(JsonHandler.SimulationObjectType.PLANT);
            importedSimulationObjects = JsonHandler.importScenarioConfig(JsonHandler.SimulationObjectType.PLANT);
        }else if(simulationObjectType == JsonHandler.SimulationObjectType.LANDSCAPE){
            if (type==Type.empty){
                //default landscape
                //TODO wie kann man landscape ändern? -> gucke im Pflichtenheft
                Object[] object = {"defaultLandscape"};
                formattedSimulationObjects.put("Landscape",object);
            }else if (type==Type.imported){

            }
            importedSimulationObjects = JsonHandler.importScenarioConfig(JsonHandler.SimulationObjectType.LANDSCAPE);
            String key = importedSimulationObjects.values().toArray()[0].toString();
            if (key!=null){
                //TODO check if such file exists with name of value
                File landscapeFile = new File(JsonHandler.workingDirectory+"/"+key+".json");
                if (landscapeFile.exists()) {
                    Object[] object = {importedSimulationObjects.get(key)};
                    formattedSimulationObjects.put(key,object);
                }else{
                    System.out.println("The specified file "+key+".json does not exist.");
                    System.out.println("For the default landscape you should leave the value empty.");
                }
            }

            System.out.println(importedSimulationObjects.values().toArray()[0].getClass());

        }else {
            System.out.println("No valid simulationObjectType.");
        }

        for (Object key:simulationObjects.keySet()) {
            Object[] object = new Object[2];
            object[0]=((HashMap)(simulationObjects.get(key))).get("Bild");
            formattedSimulationObjects.put(key.toString(),object);
        }

        if (type==Type.empty){
            for (Object key:formattedSimulationObjects.keySet()) {
                formattedSimulationObjects.get(key.toString())[1]=0;
            }
        }else if(type==Type.imported){
            for (Object key:importedSimulationObjects.keySet()) {
                key = key.toString();
                if (formattedSimulationObjects.containsKey(key) && formattedSimulationObjects.get(key)[1]==null){
                    formattedSimulationObjects.get(key)[1]=importedSimulationObjects.get(key);
                }
            }
            for (Object key:formattedSimulationObjects.keySet()) {
                if (formattedSimulationObjects.get(key.toString())[1]==null)
                formattedSimulationObjects.get(key.toString())[1]=0;
            }
        }
        System.out.println(formattedSimulationObjects);
        return formattedSimulationObjects;
    }

    public static ArrayList<SimulationObject> initSimObjects() throws IOException {
        HashMap dinosaurs = JsonHandler.importSimulationObjectConfig(JsonHandler.SimulationObjectType.DINO);
        HashMap plants = JsonHandler.importSimulationObjectConfig(JsonHandler.SimulationObjectType.PLANT);

        ArrayList<SimulationObject> simulationObjects = new ArrayList<>();
        //TODO:
        //simulationObjects.addAll(initDinos());
        //simulationObjects.addAll(initPlants());

        return simulationObjects;
    }

    /**
     * This method will create all dinosaurs based on their particular specified amount, in compliance with their variance
     * @param hashMap which contains the names of dinosaurs (as key) and their amount (as value)
     * @return all created dinosaurs (as SimulationObjects) in an ArrayList
     */
    public static ArrayList<Dinosaur> initDinos(HashMap<String, Integer> hashMap){
        ArrayList<Dinosaur> dinosaurs = new ArrayList<>();

        for (String key : hashMap.keySet()) {
            for (int i=0;i<hashMap.get(key);i++){
                Dinosaur dino = new Dinosaur();
                dinosaurs.add(dino);
            }
        }

        return dinosaurs;
    }

    /**
     * This method will create all plants based on their particular specified amount, in compliance with their variance
     * @param hashMap which contains the names of plants (as key) and their amount (as value)
     * @return all created plants (as SimulationObjects) in an ArrayList
     */
    public static ArrayList<Plant> initPlants(HashMap<String, Integer> hashMap){
        ArrayList<Plant> plants = new ArrayList<>();

        for (String key : hashMap.keySet()) {
            for (int i=0;i<hashMap.get(key);i++){
                Plant plant = new Plant();
                plants.add(plant);
            }
        }

        return plants;
    }
}
