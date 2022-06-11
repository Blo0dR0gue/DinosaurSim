package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

public class WarningPopup extends DialogPane {
    @FXML
    DialogPane container;
    @FXML
    TextArea textArea;

    public WarningPopup(){}

    public WarningPopup newInstance(){
        return (WarningPopup) Display.makeFXMLController("warning-popup.fxml", WarningPopup.class);
    }

    public void initialize(String warning){
        textArea.setText(warning);

        container.getButtonTypes().add(ButtonType.OK);
    }

    private void initializeListeners(){

    }
}
