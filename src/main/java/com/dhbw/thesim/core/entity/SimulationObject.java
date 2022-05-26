package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an object, which is handled in our simulation
 *
 * @author Daniel Czeschner
 */
public abstract class SimulationObject {

    private ImageView imageObj;
    private Vector2D position;

    public SimulationObject() {
        super();
        imageObj = new ImageView();
        position = new Vector2D(0,0);
    }

    public abstract void update(double deltaTime, Simulation currentSimulation);

    //region getter & setter
    public void setSprite(Image image) {
        imageObj.setImage(image);
    }

    public void updatePosition(){
        imageObj.setTranslateX(position.getX());
        imageObj.setTranslateY(position.getY());
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Node getJavaFXObj(){
        return imageObj;
    }

    //endregion
}
