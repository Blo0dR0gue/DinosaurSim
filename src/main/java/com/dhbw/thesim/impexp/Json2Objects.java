package com.dhbw.thesim.impexp;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author eric stefan
 */
public class Json2Objects {

    private static HashMap dinosaurs;
    private static HashMap plants;

    public static ArrayList<SimulationObject> initSimObjects() throws IOException {
        dinosaurs = JsonHandler.importSimulationObjectConfig(JsonHandler.SimulationObjectType.DINO);
        plants = JsonHandler.importSimulationObjectConfig(JsonHandler.SimulationObjectType.PLANT);

        ArrayList<SimulationObject> simulationObjects = new ArrayList<>();

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
