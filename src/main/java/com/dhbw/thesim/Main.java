package com.dhbw.thesim;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.application.Application;

import java.io.IOException;
import java.util.HashMap;

/**
 * Main entrypoint in this app.
 *
 * @author Daniel Czeschner
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Application.launch(Display.class, args);

        //------------------------------------------------------------------------------------------------------------------
        //Testen von JsonHandler: TODO nur zum Testen, bzw. fürs Verständnis
        JsonHandler.setDirectory();

        JsonHandler.exportDefaultScenarioConfig();
        JsonHandler.exportDefaultSimulationObjectsConfig();

        HashMap<String, Integer> tempDino = new HashMap<>();
        tempDino.put("Abrictosaurus", 3);
        HashMap<String, Integer> tempPlant = new HashMap<>();
        tempPlant.put("Farn", 4);
        JsonHandler.exportScenarioConfig(tempDino, tempPlant, "landschaftsName", 9.0, "testScenarioKonfig");

        //Testen von Json2Objects: TODO nur zum Testen, bzw. fürs Verständnis
        HashMap<String, Integer> testInitDino = new HashMap<>();
        testInitDino.put("Abrictosaurus", 2);
        HashMap<String, Integer> testInitPlant = new HashMap<>();
        testInitPlant.put("Farn", 4);
        Json2Objects.initSimObjects(testInitDino, testInitPlant, 3.0);

        Json2Objects.getParamsForGUI(Json2Objects.Type.NO_SCENARIO_FILE, "");
        System.out.println("--------------");
        Json2Objects.getParamsForGUI(Json2Objects.Type.WITH_SCENARIO_FILE, "testScenarioKonfig");
        //------------------------------------------------------------------------------------------------------------------
    }

}
