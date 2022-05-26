package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import javafx.scene.image.Image;
import java.util.Objects;

/**
 * Represents a dinosaur object in our simulation
 *
 * @author Daniel Czeschner
 */
public class Dinosaur extends SimulationObject{

    public Dinosaur() {
        super();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/dinosaur/test.png")).toString());
        setSprite(image);
    }

    @Override
    public void update(double deltaTime, Simulation currentSimulationData) {
        //TODO remove tests
        double lastX = this.getPosition().getX();
        this.getPosition().setX(lastX+25*deltaTime);
    }

}
