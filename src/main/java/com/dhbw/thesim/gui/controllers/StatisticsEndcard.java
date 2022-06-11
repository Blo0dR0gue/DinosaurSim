package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.stats.Statistics;
import com.dhbw.thesim.stats.StatisticsStruct;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Controller Class for the End Screen FXML file
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class StatisticsEndcard extends AnchorPane {
    @FXML
    public Button restartSimulationButton;
    @FXML
    public LineChart lineChart;


    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public StatisticsEndcard() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ConfigScreen}
     * @return The newly created and initialized {@link ConfigScreen}
     */
    public static StatisticsEndcard newInstance() {
        return (StatisticsEndcard) Display.makeFXMLController("statistics-endcard.fxml", StatisticsEndcard.class);
    }

    /**
     * Method to initialize the Statistics Endcard screen, its listeners and adding custom controls dynamically
     */
    public void initialize(Statistics statistics) {
        lineChart.setTitle("Dinosaurierpopulation");
        lineChart.getXAxis().setLabel("Messpunkte");

        //TODO get data from stats.???
        StatisticsStruct stats = statistics.getSimulationStats();

        addChartToStatistics(stats.getAllLivingDinosaurs(), "Alle");

        for (int i = 0; i < stats.getAllSpecies().size(); i++) {
            String speciesName = stats.getAllSpecies().get(i);

            ArrayList<Integer> speciesList = new ArrayList<>();
            for (List<Integer> update:
                 stats.getAllLivingSpecies()) {
                speciesList.add(update.get(i));
            }

            addChartToStatistics(speciesList, "Alle" + speciesName);
        }

        initializeListeners();
    }

    private void addChartToStatistics(List<Integer> integerList, String chartName) {
        XYChart.Series allLivingDinosaurs = new XYChart.Series();
        allLivingDinosaurs.setName(chartName);

        for (int count = 0; count < integerList.size(); count++) {
            allLivingDinosaurs.getData().add(new XYChart.Data(count, integerList.get(count)));
        }
        lineChart.getData().add(allLivingDinosaurs);
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    private void initializeListeners() {
        restartSimulationButton.setOnAction(event -> {
            Stage window = (Stage) restartSimulationButton.getScene().getWindow();
            window.setScene(Display.configScene);
            window.setMaximized(true);
        });
    }
}
