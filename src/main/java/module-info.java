module com.dhbw.thesim {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.dhbw.thesim to javafx.fxml;
    exports com.dhbw.thesim;
    exports com.dhbw.thesim.gui;
    opens com.dhbw.thesim.gui to javafx.fxml;
    opens com.dhbw.thesim.gui.controllers to javafx.fxml;
}