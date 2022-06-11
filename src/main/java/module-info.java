module com.dhbw.thesim {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    exports com.dhbw.thesim;
    exports com.dhbw.thesim.gui;
    exports com.dhbw.thesim.gui.controllers;
    exports com.dhbw.thesim.stats;
    exports com.dhbw.thesim.impexp;
    opens com.dhbw.thesim to javafx.fxml;
    opens com.dhbw.thesim.gui to javafx.fxml;
    opens com.dhbw.thesim.gui.controllers to javafx.fxml;
}