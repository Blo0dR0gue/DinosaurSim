package com.dhbw.thesim.gui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartSimulation {
    public BorderPane outer_borderPane = null;
        //top
        public VBox top_vbox = null;
            public Label top_vbox_label_top = null;
            public Label top_vbox_label_bottom = null;
        //right
        public BorderPane start_borderPane = null;
            //top
            public Label start_borderPane_label_top = null;
            public Label start_borderPane_label_bottom = null;
            //bottom
            public Button start_borderPane_button = null;
        //center
        public VBox center_vbox = null;
            public HBox center_vbox_hbox = null;
                public ListView listView1 = null;
                public ListView listView2 = null;
                public ListView listView3 = null;
            public BorderPane borderPane_bottom = null;

}
