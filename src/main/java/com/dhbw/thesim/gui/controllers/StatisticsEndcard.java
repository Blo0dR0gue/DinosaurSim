package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.core.util.SimulationTime;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.stats.Statistics;
import com.dhbw.thesim.stats.StatisticsStruct;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller Class for the End Screen FXML file
 *
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class StatisticsEndcard extends AnchorPane {
    @FXML
    public Button restartSimulationButton;
    @FXML
    public LineChart<Number, Number> lineChart;
    @FXML
    public SideBar sideBar;

    private final String PREDATORS = "Fleischfresser";
    private final String CHASED = "Pflanzenfresser";

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public StatisticsEndcard() {
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            //Prevent space to trigger the reset button
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        });
    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ConfigScreen}
     *
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

        StatisticsStruct stats = statistics.getSimulationStats();

        addStatisticsToChart(stats.allLivingDinosaurs(), "Alle", stats.simulationTimeList(), isSimulationModeAuto);

        addStatisticsToChart(stats.allLivingPredators(), PREDATORS, stats.simulationTimeList(), isSimulationModeAuto);
        addStatisticsToChart(stats.allLivingChased(), CHASED, stats.simulationTimeList(), isSimulationModeAuto);

        for (int i = 0; i < stats.allSpecies().size(); i++) {
            String speciesName = stats.allSpecies().get(i);

            ArrayList<Integer> speciesList = new ArrayList<>();
            for (List<Integer> update :
                    stats.allLivingSpecies()) {
                speciesList.add(update.get(i));
            }

            addStatisticsToChart(speciesList, speciesName, stats.simulationTimeList(), isSimulationModeAuto);
        }

        createBarChart(stats);

        addPercentage(stats.absolutePercentageChased(), CHASED);
        addPercentage(stats.absolutePercentagePredators(), PREDATORS);

        initializeListeners();
    }

    private void addPercentage(double percentage, String betrachtungsgegenstand) {
        sideBar.getBody().add(new Label(String.format("%.2f", percentage * 100) + "%", new Text("Prozentzahl " + betrachtungsgegenstand + ": ")));
    }

    /**
     * Creates a {@link BarChart} displaying statistics from the previous simulation
     *
     * @param stats The {@link StatisticsStruct} containing the required statistics for display
     */
    private void createBarChart(StatisticsStruct stats) {
        CategoryAxis barChartCategoryAxis = new CategoryAxis();
        NumberAxis numberAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(barChartCategoryAxis, numberAxis);
        barChart.setTitle("");
        barChartCategoryAxis.setLabel("Nahrungsart");
        numberAxis.setLabel("Wert");

        addBars(stats, barChart);

        sideBar.getBody().add(barChart);
    }

    /**
     * Adding bars to the {@link BarChart}
     *
     * @param stats    The {@link StatisticsStruct} containing the required statistics for display
     * @param barChart The {@link BarChart} in which the bars should be added to
     */
    private void addBars(StatisticsStruct stats, BarChart<String, Number> barChart) {
        createBars(barChart, "Durchschnittlicher Durst", stats.averageHydrationChased(), stats.averageHydrationPredators());
        createBars(barChart, "Durchschnittlicher Hunger", stats.averageNutritionChased(), stats.averageNutritionPredators());
    }

    /**
     * Method for creating and adding a bar to the {@link BarChart}
     *
     * @param barChart      The {@link BarChart} in which the bars should be added to
     * @param title         The title for the bar
     * @param chasedValue   The value for a prey
     * @param predatorValue The value for a predator
     */
    private void createBars(BarChart<String, Number> barChart, String title, double chasedValue, double predatorValue) {
        XYChart.Series<String, Number> bars = new XYChart.Series<>();
        bars.setName(title);
        bars.getData().add(new XYChart.Data<>(CHASED, chasedValue));
        bars.getData().add(new XYChart.Data<>(PREDATORS, predatorValue));
        barChart.getData().add(bars);
    }

    /**
     * Method to add the statistics to the {@link LineChart}
     *
     * @param integerList          The {@link List} containg data values as {@link Integer} for displaying the chart
     * @param chartName            The title to the {@link LineChart}
     * @param simulationTimeList   The {@link List} of {@link SimulationTime}
     * @param isSimulationModeAuto Tells whether the previous simulation has been running in automatic or manual mode
     */
    private void addStatisticsToChart(List<Integer> integerList, String chartName, List<SimulationTime> simulationTimeList, boolean isSimulationModeAuto) {
        XYChart.Series<Number, Number> allLivingDinosaurs = new XYChart.Series<>();
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
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            window.setScene(Display.configScene);
            window.setFullScreen(true);
        });
    }
}
