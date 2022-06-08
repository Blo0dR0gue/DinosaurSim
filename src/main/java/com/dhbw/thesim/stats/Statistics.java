package com.dhbw.thesim.stats;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<List<SimulationObject>> statSimObjects;

    public Statistics(){
        statSimObjects = new ArrayList<>();
    }

    public void addSimulationObjectList(List<SimulationObject> simulationObjectList){
        statSimObjects.add(simulationObjectList);
    }

    public void getSingleStats(Dinosaur dino, List<SimulationObject> simulationObjectList){

    }

    public void getSimulationStats(){

    }

}
