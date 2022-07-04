package com.dhbw.thesim;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.application.Application;

import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarEntry;

import static com.dhbw.thesim.impexp.Json2Objects.initSimObjects;

/**
 * Main entrypoint in this app.
 *
 * @author Daniel Czeschner
 */
public class Main {

    public static void main(String[] args) {
        Application.launch(Display.class,args);
    }

}
