package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.util.Vector2D;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Holds all information for one Simulation and provides functions each {@link SimulationObject} needs to know which are using simulation data.
 *
 * @author Daniel Czeschner
 * @see SimulationMap
 * @see SimulationObject
 * @see SimulationLoop
 */
public class Simulation {

    //region variables
    /**
     * The used {@link SimulationMap}
     *
     * @see SimulationMap
     */
    private final SimulationMap simulationMap;
    /**
     * The {@link GraphicsContext} for the background canvas
     *
     * @see SimulationOverlay
     * @see javafx.scene.canvas.Canvas
     * @see GraphicsContext
     */
    private final GraphicsContext backgroundGraphics;
    /**
     * The List, with all handed {@link SimulationObject}s used in a Simulation.
     *
     * @see SimulationObject
     */
    private final List<SimulationObject> simulationObjects;

    private final Random random;

    //endregion

    /**
     * Constructor mainly used for test cases
     */
    public Simulation(SimulationMap simulationMap, GraphicsContext backgroundGraphics, HashMap<String, Integer> dinosaurs, HashMap<String, Integer> plants, int plantGrowthRate, Random random){
        this.simulationMap = simulationMap;
        this.simulationObjects = new ArrayList<>();
        this.backgroundGraphics = backgroundGraphics;
        this.random = random;
    }

    /**
     * Constructor
     *
     * @param landscapeName             The name of the used landscape.
     * @param backgroundGraphicsContext The {@link GraphicsContext} for the background canvas.
     * @param simulationOverlay         The {@link SimulationOverlay} object on which {@link SimulationObject}s are spawned.
     * @param dinosaurs                 Map with all dinosaurs, which should be added to this simulation. Key = Dinosaur-Name Value = Amount.
     * @param plants                    Map with all plants, which should be added to this simulation. Key = Plant-Name Value = Amount.
     * @param plantGrowthRate           The growth rate for each plant.
     */
    public Simulation(String landscapeName, GraphicsContext backgroundGraphicsContext, SimulationOverlay simulationOverlay, HashMap<String, Integer> dinosaurs, HashMap<String, Integer> plants, double plantGrowthRate) {
        //TODO load via json2objects
        this.random = new Random();
        this.simulationMap = new SimulationMap(landscapeName);
        this.backgroundGraphics = backgroundGraphicsContext;
        this.simulationObjects = new ArrayList<>();

        //TODO handle map calls to json2object

        //TODO remove temp code
        this.simulationObjects.add(new Dinosaur(
                "Test", "test.png", 10, 10, 5, 25,
                0.1, 100, 50, 10, true, true,
                'f', 400, 64, 'M')
        );

        //Draw the map
        drawMap();
        //TODO
        spawnObjects(simulationOverlay);
    }

    /**
     * Method, that spawns the {@link SimulationObject}s of the list {@link Simulation#simulationObjects}.
     *
     * @param simulationOverlay The {@link SimulationOverlay} object on which the {@link SimulationObject} are spawned.
     */
    private void spawnObjects(SimulationOverlay simulationOverlay) {
        for (SimulationObject obj : simulationObjects) {
            simulationOverlay.getChildren().add(obj.getJavaFXObj());
        }
    }

    /**
     * Draws the simulation background
     */
    private void drawMap() {

        backgroundGraphics.setFill(Color.BLACK);
        backgroundGraphics.fillRect(0, 0, SimulationOverlay.BACKGROUND_WIDTH, SimulationOverlay.BACKGROUND_HEIGHT);

        Tile[][] tiles = simulationMap.getTiles();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                backgroundGraphics.drawImage(
                        tiles[x][y].getBackground(),
                        x * Tile.TILE_SIZE,
                        y * Tile.TILE_SIZE
                );
            }
        }
    }

    /**
     * Gets the used {@link SimulationMap}.
     *
     * @return The currently used {@link SimulationMap}
     */
    public SimulationMap getSimulationMap() {
        return simulationMap;
    }

    /**
     * Gets all handled {@link SimulationObject}s.
     *
     * @return The list {@link Simulation#simulationObjects}.
     */
    public List<SimulationObject> getSimulationObjects() {
        return simulationObjects;
    }

    /**
     * Get a random point inside a circle.
     *
     * @param center The center {@link Vector2D} of the circle.
     * @param radius The radius of this circle.
     * @return A {@link Vector2D} point.
     */
    private Vector2D getRandomPointInCircle(Vector2D center, double radius) {
        //We use polar notation to calculate a random point.
        //The polar angle will be in the range [0, 2 * pi] and the hypotenuse will be in the range [0, radius].

        //Calculate a random angle.
        double angle = Math.random() * 2 * Math.PI;
        //Calculate the hypotenuse, which should at least have a length in the upper 75% of the view range.
        double hypotenuse = Math.sqrt(random.nextDouble(0.25, 1)) * radius;

        //Calculate the sites
        double adjacent = Math.cos(angle) * hypotenuse;
        double opposite = Math.sin(angle) * hypotenuse;

        return new Vector2D(center.getX() + adjacent, center.getY() + opposite);
    }

    /**
     * Checks, if a point is inside a circle
     *
     * @param circleCenter The center of the circle.
     * @param radius       The radius of this circle.
     * @param point        The {@link Vector2D} point, which should be checked.
     * @return true, if this point is inside the circle.
     */
    private boolean isPointInsideCircle(Vector2D circleCenter, double radius, Vector2D point) {
        // Compare radius of circle with distance
        // of its center from given point
        return (point.getX() - circleCenter.getX()) * (point.getX() - circleCenter.getX()) +
                (point.getY() - circleCenter.getY()) * (point.getY() - circleCenter.getY()) <= radius * radius;
    }

    /**
     * Check, if a point is inside any interaction range (collision circle) of any simulationobject.
     *
     * @param point The point, which should be checked.
     * @return true, if the point is inside any collision circle.
     * @see #simulationObjects
     */
    private boolean isPointInsideAnyInteractionRange(Vector2D point) {
        return simulationObjects.stream().anyMatch(simulationObject -> isPointInsideCircle(simulationObject.getPosition(), simulationObject.getInteractionRange(), point));
    }

    /**
     * TODO optimize, very stupid approach. :D
     * Gets a random target vector inside a view range of a dinosaur.
     *
     * @param position The center of the view radius circle
     * @param viewRange The view range as an radius.
     * @param canSwim Can the object, which should be tested, swim?
     * @param canClimb Can the object, which should be tested, climb?
     * @param renderOffset The offset for the image of the object.
     * @return A {@link Vector2D} target position.
     */
    public Vector2D getRandomPositionInRange(Vector2D position, double viewRange, boolean canSwim, boolean canClimb, Vector2D renderOffset) {
        Vector2D target = getRandomPointInCircle(position, viewRange);

        //Is this point inside the grid?
        if (!simulationMap.isInsideOfGrid(target)) {
            return getRandomPositionInRange(position, viewRange, canSwim, canClimb, renderOffset);
        }

        //Check, if the dinosaur can move on this tile, if this point is inside any collision area of any simulation object and if the dinosaur will be rendered outside.
        //If so get another point.
        if (!simulationMap.tileMatchedConditions(target, canSwim, canClimb) ||
                isPointInsideAnyInteractionRange(target) ||
                SimulationObject.willBeRenderedOutside(target, renderOffset)
        ) {
            return getRandomPositionInRange(position, viewRange, canSwim, canClimb, renderOffset);
        }

        //Check if the tile can be reached. So whether the object can/may move over each tile to the target point. If not, find another target.
        if (!targetTileCanBeReached(position, target, canSwim, canClimb)) {
            return getRandomPositionInRange(position, viewRange, canSwim, canClimb, renderOffset);
        }

        //Check, if this target point is in any interaction range. If so, find another target.
        for (SimulationObject simulationObject: simulationObjects) {
            if(simulationObject.getPosition() != position && doesLineSegmentCollideWithCircleRange(simulationObject.getPosition(), simulationObject.getInteractionRange(), position, target)){
                return getRandomPositionInRange(position, viewRange, canSwim, canClimb, renderOffset);
            }
        }

        return target;
    }


    /** TODO move to map?
     * Calculates, if all tiles between the start tile and the target tile can be reached. <br>
     * Uses the bresenham-algorithm. See <a href="https://de.wikipedia.org/wiki/Bresenham-Algorithmus">Wikipedia</a>
     *
     * @param start    The start {@link Vector2D} position in the simulation world.
     * @param target   The target {@link Vector2D} position in the simulation world.
     * @param canSwim  Can the {@link Dinosaur} swim?
     * @param canClimb Can the {@link Dinosaur} climb?
     * @return true, if all tiles from the start to the target can be crossed.
     */
    private boolean targetTileCanBeReached(Vector2D start, Vector2D target, boolean canSwim, boolean canClimb) {
        Tile startTile = simulationMap.getTileAtPosition(start);
        Tile targetTile = simulationMap.getTileAtPosition(target);

        //Distance between the tiles. (In x and y direction)
        int dx = targetTile.getGridX() - startTile.getGridX();
        int dy = targetTile.getGridY() - startTile.getGridY();

        //Absolute distances.
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);

        //Determine the sign of the increment
        int sdx = (int) Math.signum(dx);
        int sdy = (int) Math.signum(dy);

        //pd. is parallel step
        int pdx, pdy;

        //dd. is diagonal step
        int ddx, ddy;

        //delta in fast and slow direction
        int deltaSlowDirection, deltaFastDirection;

        //Determine which distance is greater
        if (adx > ady) {
            //x is faster
            pdx = sdx;
            pdy = 0;

            ddx = sdx;
            ddy = sdy;

            deltaSlowDirection = ady;
            deltaFastDirection = adx;

        } else {
            //y is faster
            pdx = 0;
            pdy = sdy;

            ddx = sdx;
            ddy = sdy;

            deltaSlowDirection = adx;
            deltaFastDirection = ady;
        }

        //Loop variables
        int x = startTile.getGridX();
        int y = startTile.getGridY();
        int error = deltaFastDirection / 2;

        for (int i = 0; i < deltaFastDirection; ++i) {
            //update error term
            error -= deltaSlowDirection;
            if (error < 0) {
                //Make the error term positive again
                error += deltaFastDirection;
                //diagonal step: Step in slow direction
                x += ddx;
                y += ddy;
            } else {
                //parallel step: Step in fast direction
                x += pdx;
                y += pdy;
            }

            //TODO delete debug
            System.out.println(x + " " + y);

            if (!simulationMap.tileMatchedConditions(x, y, canSwim, canClimb)) {
                //If this tile can't be crossed be the dinosaur, return false.
                return false;
            }
        }

        return true;
    }

    /**
     * Checks, if a line segment collide with a view range circle.
     *
     * @param circleOrigin The origin {@link Vector2D} of the range circle.
     * @param radius The radius of the range circle.
     * @param start The start {@link Vector2D} of the line segment.
     * @param end The end {@link Vector2D} of the line segment.
     * @return true, if the line collide with the circle.
     */
    private boolean doesLineSegmentCollideWithCircleRange( Vector2D circleOrigin, double radius, Vector2D start, Vector2D end) {

        if(isPointInsideCircle(circleOrigin, radius, start) || isPointInsideCircle(circleOrigin, radius, end)){
            return true;
        }

        double minDist;
        double maxDist = Math.max(Vector2D.distance(circleOrigin, start), Vector2D.distance(circleOrigin, end));

        Vector2D SO = start.subtract(circleOrigin);
        Vector2D EO = end.subtract(circleOrigin);
        double triangleAre = Math.abs(Vector2D.crossProduct(SO, EO)) / 2;

        if (Vector2D.dotProduct(circleOrigin.subtract(start), end.subtract(start)) > 0 && Vector2D.dotProduct(circleOrigin.subtract(end), start.subtract(end)) > 0) {
            minDist = (2 * triangleAre) / Vector2D.distance(start, end);
        } else {
            minDist = Math.min(Vector2D.distance(circleOrigin, start), Vector2D.distance(circleOrigin, end));
        }

        return minDist <= radius && maxDist >= radius;
    }

}
