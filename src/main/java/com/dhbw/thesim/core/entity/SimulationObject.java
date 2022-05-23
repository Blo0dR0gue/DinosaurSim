package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.util.Vector2D;
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
        imageObj = new ImageView();
    }

    public abstract void update(double deltaTime);

    //region getter & setter
    public void setSprite(Image image) {
        imageObj.setImage(image);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
    //endregion
}
