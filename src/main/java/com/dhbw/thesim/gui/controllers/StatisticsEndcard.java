package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.core.util.SimulationTime;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.stats.Statistics;
import com.dhbw.thesim.stats.StatisticsStruct;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller Class for the End Screen FXML file
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class StatisticsEndcard extends AnchorPane {
    @FXML
    public Button restartSimulationButton;
    @FXML
    public LineChart<Number,Number> lineChart;
    @FXML
    public SideBar sideBar;

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
    public void initialize(Statistics statistics, boolean isSimulationModeAuto) {
        lineChart.setTitle("Dinosaurierpopulation");

        lineChart.getXAxis().setLabel(isSimulationModeAuto ? "Simulationszeit (in s)" : "Schritte");
        lineChart.getYAxis().setLabel("Anzahl");

        //TODO get data from stats.???
        StatisticsStruct stats = statistics.getSimulationStats();
        System.out.println("simulation times: " + stats.getSimulationTimeList());

        addChartToStatistics(stats.getAllLivingDinosaurs(), "Alle", stats.getSimulationTimeList(), isSimulationModeAuto);

        for (int i = 0; i < stats.getAllSpecies().size(); i++) {
            String speciesName = stats.getAllSpecies().get(i);

            ArrayList<Integer> speciesList = new ArrayList<>();
            for (List<Integer> update:
                 stats.getAllLivingSpecies()) {
                speciesList.add(update.get(i));
            }

            addChartToStatistics(speciesList, speciesName, stats.getSimulationTimeList(), isSimulationModeAuto);
        }

        createBarChart(stats);

        initializeListeners();
    }

    private void createBarChart(StatisticsStruct stats) {
        CategoryAxis barChartCategoryAxis = new CategoryAxis();
        NumberAxis numberAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(barChartCategoryAxis, numberAxis);
        barChart.setTitle("");
        barChartCategoryAxis.setLabel("Eigenschaften");
        numberAxis.setLabel("Wert");

        addBars(stats, barChart);

        sideBar.getBody().add(barChart);
    }

    private void addBars(StatisticsStruct stats, BarChart<String, Number> barChart) {
        createBars(barChart, "Absolute Prozentzahlen", stats.getAbsolutePercentageChased(), stats.getAbsolutePercentagePredators());
        createBars(barChart, "Durchschnittlicher Durst", stats.getAverageHydrationChased(), stats.getAverageHydrationPredators());
        createBars(barChart, "Durchschnittlicher Hunger", stats.getAverageNutritionChased(), stats.getAverageNutritionPredators());
    }

    private void createBars(BarChart<String, Number> barChart, String title, double chasedValue, double predatorValue) {
        XYChart.Series<String, Number> bars = new XYChart.Series<>();
        bars.setName(title);
        bars.getData().add(new XYChart.Data<>("Gejagte", chasedValue));
        bars.getData().add(new XYChart.Data<>("Jagende", predatorValue));
        barChart.getData().add(bars);
    }

    private void addChartToStatistics(List<Integer> integerList, String chartName, List<SimulationTime> simulationTimeList, boolean isSimulationModeAuto) {
        XYChart.Series<Number,Number> allLivingDinosaurs = new XYChart.Series<>();
        allLivingDinosaurs.setName(chartName);

        for (int count = 0; count < integerList.size(); count++) {
            double x = isSimulationModeAuto ? simulationTimeList.get(count).getTime() : count;

            allLivingDinosaurs.getData().add(new XYChart.Data<>(x, integerList.get(count)));
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
            window.setFullScreen(true);
        });
    }
}
