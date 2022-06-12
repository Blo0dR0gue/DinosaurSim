package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.util.SimulationTime;
import com.dhbw.thesim.core.util.Vector2D;
import com.dhbw.thesim.gui.SimulationOverlay;
import com.dhbw.thesim.impexp.Json2Objects;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

/**
 * Holds all information for one Simulation and provides functions each {@link SimulationObject} needs to know which are using simulation data.
 *
 * @author Daniel Czeschner, Lucas Schaffer
 * @see SimulationMap
 * @see SimulationObject
 * @see SimulationLoop
 */
@SuppressWarnings("unused")
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

    /**
     * A List with all {@link SimulationObject}s, which will be removed at the end of a {@link SimulationLoop} update.
     */
    private List<SimulationObject> toBeRemoved;

    /**
     * A List with all {@link SimulationObject}s, which will be spawned at the end of a {@link SimulationLoop} update.
     */
    private List<SimulationObject> toBeSpawned;

    /**
     * The SimulationOverlay, on which the elements get spawned.
     */
    private SimulationOverlay simulationOverlay;

    /**
     * A {@link Random} used for e.g. get a random tile.
     */
    private final Random random;

    /**
     *
     */
    private final SimulationTime simulationTime;

    //endregion

    /**
     * Constructor used for test cases
     */
    public Simulation(SimulationMap simulationMap, GraphicsContext backgroundGraphics, Map<String, Integer> dinosaurs, Map<String, Integer> plants, int plantGrowthRate, Random random) {
        this.simulationMap = simulationMap;
        this.simulationObjects = new ArrayList<>();
        this.backgroundGraphics = backgroundGraphics;
        this.simulationTime = new SimulationTime();
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
    public Simulation(String landscapeName, GraphicsContext backgroundGraphicsContext, SimulationOverlay simulationOverlay, Map<String, Integer> dinosaurs, Map<String, Integer> plants, double plantGrowthRate) {
        this.random = new Random();
        this.simulationMap = new SimulationMap(landscapeName);
        this.backgroundGraphics = backgroundGraphicsContext;
        this.simulationObjects = new ArrayList<>();
        this.toBeRemoved = new ArrayList<>();
        this.toBeSpawned = new ArrayList<>();

        this.simulationTime = new SimulationTime();

        this.simulationOverlay = simulationOverlay;

        try {
            this.simulationObjects.addAll(Json2Objects.initSimObjects(dinosaurs, plants, plantGrowthRate));
        } catch (IOException e) {
            //TODO Gui error handling
            e.printStackTrace();
        }

        //Draw the map
        drawMap();
        //Spawn the objects
        spawnObjects(simulationOverlay);
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
     * Checks if a simulation is finished.
     *
     * @return true when all dinosaurs are extinct.
     */
    public boolean isOver() {
        for (SimulationObject simulationObject : simulationObjects) {
            if (simulationObject instanceof Dinosaur)
                return false;
        }
        return true;
    }

    /**
     * Remove all tagged {@link SimulationObject} out of the handled {@link #simulationObjects}.
     */
    public void removeDeletedObjects() {
        simulationObjects.removeAll(toBeRemoved);
        toBeRemoved.clear();
    }

    /**
     * Method, that spawns the {@link SimulationObject}s of the list {@link Simulation#simulationObjects}.
     *
     * @param simulationOverlay The {@link SimulationOverlay} object on which the {@link SimulationObject} are spawned.
     */
    private void spawnObjects(SimulationOverlay simulationOverlay) {
        for (SimulationObject obj : simulationObjects) {
            //Set the object start position
            if (obj instanceof Dinosaur dinosaur) {
                //If we are a dinosaur get a free position, where the dinosaur can walk on.
                dinosaur.setPosition(getFreePositionInMap(dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getInteractionRange(), dinosaur.getRenderOffset()));
                dinosaur.setTimeOfBirth(simulationTime.getTime());

                dinosaur.getJavaFXObj().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    simulationOverlay.dinosaurClicked(dinosaur);
                });

            } else if (obj instanceof Plant plant) {
                //Plants only can be spawned on tiles, which allow plant growing
                obj.setPosition(getFreePositionInMapWhereConditionsAre(false, false, true, plant.getInteractionRange() + 10, plant.getRenderOffset()));
            }
            simulationOverlay.centerPane.getChildren().add(obj.getSelectionRing());
            simulationOverlay.centerPane.getChildren().add(obj.getJavaFXObj());
        }
    }

    /**
     * Add a {@link SimulationObject}, which should be removed from the handled {@link #simulationObjects} to the {@link #toBeRemoved} list.
     * The object is also been removed from the visuals.
     *
     * @param simulationObject The {@link SimulationObject} which should be removed.
     */
    public void deleteObject(SimulationObject simulationObject) {
        this.toBeRemoved.add(simulationObject);
        Platform.runLater(() -> {
            simulationOverlay.centerPane.getChildren().remove(simulationObject.getSelectionRing());
            simulationOverlay.centerPane.getChildren().remove(simulationObject.getJavaFXObj());
        });
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
     * Gets the closest reachable Water source or null.
     *
     * @param position  The {@link Vector2D} position, where we check from.
     * @param viewRange The radial range, we want to check (as radius)
     * @param canSwim   Does the object, who wants to move to a water tile, can swim?
     * @param canClimb  Does the object, who wants to move to a water tile, can climb?
     * @return A {@link Vector2D} target of a water tile or null.
     */
    public Vector2D getClosestReachableWaterSource(Vector2D position, double viewRange, boolean canSwim, boolean canClimb) {
        List<Vector2D> waterSourcesInRange = simulationMap.getMidCoordinatesTilesWhereConditionsAre(position, viewRange, true, false);

        sortByDistance(waterSourcesInRange, position);

        if (!waterSourcesInRange.isEmpty()) {
            for (Vector2D vector : waterSourcesInRange) {
                if (isPointInsideCircle(position, viewRange, vector) && canMoveTo(position, vector, 0, canSwim, canClimb, null, true, true)) {
                    return vector;
                }
            }
        }
        return null;
    }

    /**
     * Gets the closest {@link SimulationObject} which can be eaten by the searcher {@link Dinosaur}
     *
     * @param position  The {@link Vector2D} position of the seeker.
     * @param viewRange The view range (as radius) of the seeker.
     * @param dietType  The {@link Dinosaur.dietType} of the seeker.
     * @param type      The type of the seeker. (e.g. Tyrannosaurus Rex)
     * @param strength  The strength of the seeker.
     * @return The closest {@link SimulationObject}s in range.
     */
    public SimulationObject getClosestReachableFoodSourceInRange(Vector2D position, double viewRange, Dinosaur.dietType dietType, String type,
                                                                 boolean canSwim, boolean canClimb, double strength) {

        List<SimulationObject> inRange = findReachableFoodSourcesInRange(position, viewRange, dietType, type, canSwim, canClimb, strength);

        if (dietType != Dinosaur.dietType.omnivore) {
            sortByDistance(position, inRange);
            if (!inRange.isEmpty())
                return inRange.get(0);
            return null;
        } else {
            //An omnivore prioritized plants. So if a plant is in range this will be the target.
            SimulationObject closest = null;
            for (SimulationObject simulationObject : inRange) {
                if (closest == null) {
                    closest = simulationObject;
                } else {
                    if (closest instanceof Plant && simulationObject instanceof Plant || closest instanceof Dinosaur && simulationObject instanceof Dinosaur) {
                        //If the distance of the last closest object ist lager than the distance of the current object, which is being checked, set it to the current object.
                        if (Vector2D.distance(position, closest.getPosition()) > Vector2D.distance(position, simulationObject.getPosition())) {
                            closest = simulationObject;
                        }
                    } else if (!(closest instanceof Plant) && simulationObject instanceof Plant) {
                        closest = simulationObject;
                    }
                }
            }
            return closest;
        }
    }

    /**
     * Gets all {@link SimulationObject}s which can be eaten by the searcher {@link Dinosaur} <br>
     * It is also been checked, if the food source can be eaten by the searcher.
     *
     * @param position  The {@link Vector2D} position of the seeker.
     * @param viewRange The view range (as radius) of the seeker.
     * @param dietType  The {@link Dinosaur.dietType} of the seeker.
     * @param type      The type of the seeker. (e.g. Tyrannosaurus Rex)
     * @return A list with all eatable {@link SimulationObject}s in range.
     */
    public List<SimulationObject> findReachableFoodSourcesInRange(Vector2D position, double viewRange, Dinosaur.dietType dietType, String type,
                                                                  boolean canSwim, boolean canClimb, double strength) {

        List<SimulationObject> inRange = new ArrayList<>();

        for (SimulationObject simulationObject : simulationObjects) {
            if (simulationObject.getPosition() != position) {
                if (doTheCirclesIntersect(position, viewRange, simulationObject.getPosition(), simulationObject.getInteractionRange())) {
                    if (dietType == Dinosaur.dietType.herbivore && simulationObject instanceof Plant plant) {
                        if (plant.canBeEaten(strength))
                            inRange.add(plant);
                    } else if (dietType == Dinosaur.dietType.carnivore && simulationObject instanceof Dinosaur dinosaur) {
                        //We don't want to hunt a dinosaur who is the same type as the searcher.
                        if (!dinosaur.getType().equalsIgnoreCase(type) && dinosaur.canBeEaten(strength)) {
                            inRange.add(dinosaur);
                        }
                    } else if (dietType == Dinosaur.dietType.omnivore) {
                        //It's an omnivore
                        if ((simulationObject instanceof Plant || simulationObject instanceof Dinosaur) && simulationObject.canBeEaten(strength))
                            inRange.add(simulationObject);
                    }
                }
            }
        }
        inRange.removeIf(simulationObject -> !canMoveTo(position, simulationObject.getPosition(), 0, canSwim, canClimb, null, true, true));
        return inRange;
    }

    public SimulationObject getClosestReachableSuitablePartnerInRange(Vector2D position, double viewRange, String type,
                                                                      boolean canSwim, boolean canClimb, char gender) {
        List<SimulationObject> inRange = findReachableSuitablePartnersInRange(position, viewRange, type, canSwim, canClimb, gender);

        sortByDistance(position, inRange);
        if (!inRange.isEmpty())
            return inRange.get(0);
        return null;

    }

    public List<SimulationObject> findReachableSuitablePartnersInRange(Vector2D position, double viewRange, String type,
                                                                       boolean canSwim, boolean canClimb, char gender) {
        List<SimulationObject> inRange = new ArrayList<>();

        for (SimulationObject simulationObject : simulationObjects) {

            if (simulationObject.getPosition() != position) {
                if (doTheCirclesIntersect(position, viewRange, simulationObject.getPosition(), simulationObject.getInteractionRange())) {
                    if (simulationObject instanceof Dinosaur dinosaur && dinosaur.getPartner() == null && dinosaur.getType().equalsIgnoreCase(type) && dinosaur.getGender() != gender && dinosaur.isWillingToMate()) {
                        inRange.add(dinosaur);
                    }
                }
            }
        }

        inRange.removeIf(simulationObject -> !canMoveTo(position, simulationObject.getPosition(), 0, canSwim, canClimb, null, true, true));

        return inRange;
    }

    public void makeBaby(Dinosaur mother, Dinosaur father) {

        double strength = inheritValue(mother.getStrength(), father.getStrength());
        double speed = inheritValue(mother.getSpeed(), father.getSpeed());
        double reproductionRate = inheritValue(mother.getReproductionRate(), father.getReproductionRate());
        double weight = inheritValue(mother.getWeight(), father.getWeight());
        double length = inheritValue(mother.getLength(), father.getLength());
        double height = inheritValue(mother.getHeight(), father.getHeight());
        double nutrition = inheritValue(mother.getNutrition(), father.getNutrition());
        double hydration = inheritValue(mother.getHydration(), father.getHydration());
        char gender;

        if (random.nextInt() % 2 == 0)
            gender = 'm';
        else
            gender = 'f';

        Dinosaur baby = new Dinosaur(
                mother.getType(), mother.getJavaFXObj().getImage(), nutrition, hydration, strength, speed,
                reproductionRate, weight, length, height, mother.canSwim(), mother.canClimb(), mother.getCharDiet(), mother.getViewRange(), mother.getInteractionRange(), gender);


        baby.setPosition(getNearestPositionInMapWhereConditionsAre(mother.getPosition(), mother.getInteractionRange(), baby.canSwim(), baby.canClimb(), baby.getInteractionRange()));

        spawnObject(baby);

        System.out.println("Baby Dinosaur was made");
    }

    public Vector2D getNearestPositionInMapWhereConditionsAre(Vector2D origin, double range, boolean swimmable, boolean climbable, double interactionRange) {

        List<Vector2D> positions = simulationMap.getMidCoordinatesTilesWhereConditionsMatch(origin, range + Tile.TILE_SIZE, swimmable, climbable);

        for (Vector2D pos : positions) {
            if (!doesPointWithRangeIntersectAnyInteractionRange(pos, interactionRange, origin)) {
                System.out.println("Spawn position found with range " + range);
                return pos;
            }
        }

        return getNearestPositionInMapWhereConditionsAre(origin, range + 1, swimmable, climbable, interactionRange);
    }

    public double inheritValue(double a, double b) {
        double result;
        if (random.nextInt() % 2 == 0)
            result = a;
        else
            result = b;

        if (random.nextInt(1, 100) <= 20) {
            result = random.nextDouble(result * 0.7, result * 1.3);
        }

        return result;
    }

    public void spawnObject(SimulationObject simulationObject) {
        if (simulationObject instanceof Dinosaur dinosaur)
            dinosaur.setTimeOfBirth(simulationTime.getTime());
        this.toBeSpawned.add(simulationObject);
        Platform.runLater(() -> simulationOverlay.centerPane.getChildren().add(simulationObject.getJavaFXObj()));
    }

    public void spawnNewObjects() {
        simulationObjects.addAll(toBeSpawned);
        toBeSpawned.clear();
    }

    /**
     * Sorts a passed list of simulation objects based on the distance to a {@link Vector2D}
     *
     * @param position The {@link Vector2D} we want sort to.
     * @param list     The list with the {@link SimulationObject}, which should be sorted.
     */
    public void sortByDistance(Vector2D position, List<SimulationObject> list) {
        list.sort(Comparator.comparingDouble(o -> Vector2D.distance(position, o.getPosition())));
    }

    /**
     * Sorts a passed list of simulation objects based on the distance to a {@link Vector2D}
     *
     * @param position The {@link Vector2D} we want sort to.
     * @param list     The list with the {@link Vector2D}, which should be sorted.
     */
    public void sortByDistance(List<Vector2D> list, Vector2D position) {
        list.sort(Comparator.comparingDouble(o -> Vector2D.distance(position, o)));
    }

    /**
     * Gets a random free position on the grid map.
     *
     * @param canSwim          Can the {@link Dinosaur} swim.
     * @param canClimb         Can the {@link Dinosaur} climb.
     * @param interactionRange The interaction range for the object, which wants to check this position, so that the target does not intersect with any other interaction range.
     * @return A random {@link Vector2D} position.
     */
    public Vector2D getFreePositionInMap(boolean canSwim, boolean canClimb, double interactionRange, Vector2D renderOffset) {
        Vector2D target = simulationMap.getRandomTileCenterPosition(canSwim, canClimb, random);
        if (doesPointWithRangeIntersectAnyInteractionRange(target, interactionRange, null) || SimulationObject.willBeRenderedOutside(target, renderOffset)
                || !simulationMap.checkIfNeighborTilesMatchConditions(target, canSwim, canClimb, interactionRange)) {
            return getFreePositionInMap(canSwim, canClimb, interactionRange, renderOffset);
        }
        return target;
    }

    /**
     * Gets a random free position on the grid map matching the conditions.
     *
     * @param climbable        Does the tile need to be climbable.
     * @param swimmable        Does the tile need to be swimmable.
     * @param allowPlants      Does the tile need to allow plants?
     * @param interactionRange The interaction range for the object, which wants to check this position, so that the target does not intersect with any other interaction range.
     * @return A random {@link Vector2D} position.
     */
    public Vector2D getFreePositionInMapWhereConditionsAre(boolean swimmable, boolean climbable, boolean allowPlants, double interactionRange, Vector2D renderOffset) {
        Vector2D target = simulationMap.getRandomTileCenterPositionWhereConditionsAre(swimmable, climbable, allowPlants, random);
        if (doesPointWithRangeIntersectAnyInteractionRange(target, interactionRange, null) || SimulationObject.willBeRenderedOutside(target, renderOffset)
                || !simulationMap.checkIfNeighborTilesHasConditions(target, swimmable, climbable, allowPlants, interactionRange)) {
            return getFreePositionInMapWhereConditionsAre(swimmable, climbable, allowPlants, interactionRange, renderOffset);
        }
        return target;
    }

    /**
     * Checks, if a point with a range (a circle) intersect any interaction range.
     *
     * @param target           The target {@link Vector2D} point
     * @param interactionRange The range of the point (circle), which should be checked.
     * @param ignore           This {@link Vector2D} will be ignored by the checks. Set it to null, if no {@link SimulationObject} should be ignored.
     * @return true, if the check circle intersect with any interaction range.
     */
    private boolean doesPointWithRangeIntersectAnyInteractionRange(Vector2D target, double interactionRange, Vector2D ignore) {
        if (isPointInsideAnyInteractionRange(target, ignore)) {
            return true;
        }
        for (SimulationObject simulationObject : simulationObjects) {
            if (simulationObject.getPosition() != ignore)
                if (doTheCirclesIntersect(target, interactionRange, simulationObject.getPosition(), simulationObject.getInteractionRange())) {
                    return true;
                }
        }
        return false;
    }

    /**
     * Checks if a {@link SimulationObject} can move to a position.
     *
     * @param start                  The {@link Vector2D} position of the {@link SimulationObject}.
     * @param target                 The {@link Vector2D} target, where he wants to move.
     * @param interactionRange       The interaction range of the {@link SimulationObject}.
     * @param canSwim                Can the {@link SimulationObject} swim?
     * @param canClimb               Can the {@link SimulationObject} climb?
     * @param renderOffset           The render offset of the {@link SimulationObject}.
     * @param ignoreRenderConditions true, if the render conditions (e.g. rendered outside and tile conditions) should be ignored
     * @param ignoreTargetTile       true, if we don't want to check the target tile. (See the linked functions for a better understanding)
     * @return true, if the {@link SimulationObject} can move to the target
     * @see SimulationMap#tileMatchedConditions(Vector2D, boolean, boolean)
     * @see SimulationObject#willBeRenderedOutside(Vector2D, Vector2D)
     * @see #targetTileCanBeReached(Vector2D, Vector2D, boolean, boolean, boolean)
     * @see #doesPointWithRangeIntersectAnyInteractionRange(Vector2D, double, Vector2D)
     * @see #doesLineSegmentCollideWithCircleRange(Vector2D, double, Vector2D, Vector2D, boolean)
     */
    public boolean canMoveTo(Vector2D start, Vector2D target, double interactionRange, boolean canSwim, boolean canClimb, Vector2D renderOffset, boolean ignoreRenderConditions, boolean ignoreTargetTile) {

        //Is this point inside the grid?
        if (!simulationMap.isInsideOfGrid(target)) {
            return false;
        }

        //Check, if the dinosaur can move on this tile, if this point is inside any collision area of any simulation object and if the dinosaur will be rendered outside.
        //If so get another point.
        if (!ignoreRenderConditions && SimulationObject.willBeRenderedOutside(target, renderOffset) || !ignoreTargetTile && !simulationMap.tileMatchedConditions(target, canSwim, canClimb)) {
            return false;
        }

        //Check if the tile can be reached. So whether the object can/may move over each tile to the target point. If not, find another target.
        if (!targetTileCanBeReached(start, target, canSwim, canClimb, ignoreTargetTile)) {
            return false;
        }

        //If we don't ignore the target tile and
        //does the point with the interaction range of the moving object intersect with any other interaction range, then find another point.
        if (!ignoreTargetTile && doesPointWithRangeIntersectAnyInteractionRange(target, interactionRange, start)) {
            return false;
        }

        //Check, if this target direction is in any interaction range. If so, find another target.
        for (SimulationObject simulationObject : simulationObjects) {
            if (simulationObject.getPosition() != start && doesLineSegmentCollideWithCircleRange(simulationObject.getPosition(), simulationObject.getInteractionRange(), start, target, ignoreTargetTile)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a random target vector inside a view range of a dinosaur.
     *
     * @param position         The position of the {@link SimulationObject} which wants to move.
     * @param viewRange        The view range as a radius.
     * @param interactionRange The interaction as a radius.
     * @param canSwim          Can the object, which should be tested, swim?
     * @param canClimb         Can the object, which should be tested, climb?
     * @param renderOffset     The offset for the image of the object.
     * @return A {@link Vector2D} target position.
     * @see #getRandomPointInCircle(Vector2D, double)
     */
    public Vector2D getRandomMovementTargetInRange(Vector2D position, double viewRange, double interactionRange, boolean canSwim, boolean canClimb, Vector2D renderOffset) {
        Vector2D target = getRandomPointInCircle(position, viewRange);
        //try it max. 500 times to get a target
        int maximumAttempts = 500;
        while (maximumAttempts > 0) {
            if (canMoveTo(position, target, interactionRange, canSwim, canClimb, renderOffset, false, false)) {
                return target;
            }
            target = getRandomPointInCircle(position, viewRange);
            maximumAttempts--;
        }
        return null;
    }

    /**
     * Get a random target facing in a direction with an offset of +-PI/3. <br>
     *
     * @param position         The position of the {@link SimulationObject} which wants to move.
     * @param viewRange        The view range as a radius.
     * @param interactionRange The interaction as a radius.
     * @param canSwim          Can the object, which should be tested, swim?
     * @param canClimb         Can the object, which should be tested, climb?
     * @param renderOffset     The offset for the image of the object.
     * @param direction        The normalized direction Vector we want move to.
     * @return A {@link Vector2D} target position.
     * @see #getRandomPositionInsideCircleRangeInDirection(Vector2D, double, Vector2D)
     */
    public Vector2D getRandomMovementTargetInRangeInDirection(Vector2D position, double viewRange, double interactionRange, boolean canSwim, boolean canClimb, Vector2D renderOffset, Vector2D direction) {
        Vector2D target = getRandomPositionInsideCircleRangeInDirection(position, viewRange, direction);
        //try it max. 500 times to get a target
        int maximumAttempts = 500;
        while (maximumAttempts > 0) {
            if (canMoveTo(position, target, interactionRange, canSwim, canClimb, renderOffset, false, false)) {
                return target;
            }
            target = getRandomPositionInsideCircleRangeInDirection(position, viewRange, direction);
            maximumAttempts--;
        }
        return null;
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
        double angle = random.nextDouble(0, 1) * 2 * Math.PI;
        //Calculate the hypotenuse, which should at least have a length in the upper 75% of the view range.
        double hypotenuse = Math.sqrt(random.nextDouble(0.25, 1)) * radius;

        //Calculate the sites
        double adjacent = Math.cos(angle) * hypotenuse;
        double opposite = Math.sin(angle) * hypotenuse;

        return new Vector2D(center.getX() + adjacent, center.getY() + opposite);
    }

    /**
     * Get a random position facing in a direction with an offset of +-PI/3. <br>
     *
     * @param center The circle {@link Vector2D} position from which we check.
     * @param radius The radial range of the circle.
     * @param dir    A direction {@link Vector2D}
     * @return A random {@link Vector2D}.
     */
    private Vector2D getRandomPositionInsideCircleRangeInDirection(Vector2D center, double radius, Vector2D dir) {
        double deg = Vector2D.angleToVector(dir);
        if (deg < 0) {
            deg = 2 * Math.PI + deg;
        }
        double low = deg - Math.PI / 4;
        double high = deg + Math.PI / 4;
        if (low < 0) {
            low = 0;
        }
        if (high > 2 * Math.PI) {
            high = 2 * Math.PI;
        }
        if (low > high) {
            double tmp = low;
            low = high;
            high = tmp;
        }
        double angle = random.nextDouble(low, high);
        double hypotenuse = Math.sqrt(random.nextDouble(0.25, 1)) * radius;
        //Calculate the sites
        double adjacent = Math.cos(angle) * hypotenuse;
        double opposite = -Math.sin(angle) * hypotenuse;

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
     * @param point  The point, which should be checked.
     * @param ignore This {@link Vector2D} will be ignored by the checks. Set it to null, if no {@link SimulationObject} should be ignored.
     * @return true, if the point is inside any collision circle.
     * @see #simulationObjects
     */
    private boolean isPointInsideAnyInteractionRange(Vector2D point, Vector2D ignore) {
        return simulationObjects.stream().anyMatch(simulationObject -> {
            if (simulationObject.getPosition() != ignore)
                return isPointInsideCircle(simulationObject.getPosition(), simulationObject.getInteractionRange(), point);
            return false;
        });
    }

    /**
     * Calculates, if all tiles between the start tile and the target tile can be reached. <br>
     * Uses the bresenham-algorithm. See <a href="https://de.wikipedia.org/wiki/Bresenham-Algorithmus">Wikipedia</a>
     *
     * @param start            The start {@link Vector2D} position in the simulation world.
     * @param target           The target {@link Vector2D} position in the simulation world.
     * @param canSwim          Can the {@link Dinosaur} swim?
     * @param canClimb         Can the {@link Dinosaur} climb?
     * @param ignoreTargetTile true, we don't want to check the conditions for the target tile. (Swimmable, Climbable)
     * @return true, if all tiles from the start to the target can be crossed.
     */
    private boolean targetTileCanBeReached(Vector2D start, Vector2D target, boolean canSwim, boolean canClimb, boolean ignoreTargetTile) {
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
            if (!(ignoreTargetTile && targetTile.getGridX() == x && targetTile.getGridY() == y))
                if (!simulationMap.tileMatchedConditions(x, y, canSwim, canClimb)) {
                    //If this tile can't be crossed be the dinosaur, return false.
                    return false;
                }
        }
        return !simulationMap.isInsideOfGrid(x, y + 1) || simulationMap.tileMatchedConditions(x, y + 1, canSwim, canClimb);
    }

    /**
     * Checks, if a line segment collide with a view range circle.
     *
     * @param circleOrigin     The origin {@link Vector2D} of the range circle.
     * @param radius           The radius of the range circle.
     * @param start            The start {@link Vector2D} of the line segment.
     * @param end              The end {@link Vector2D} of the line segment.
     * @param ignoreTargetTile if true, then the end point of the line segment is not checked, if it is inside the circle.
     * @return true, if the line collide with the circle.
     */
    private boolean doesLineSegmentCollideWithCircleRange(Vector2D circleOrigin, double radius, Vector2D start, Vector2D end, boolean ignoreTargetTile) {

        if (isPointInsideCircle(circleOrigin, radius, start) || !ignoreTargetTile && isPointInsideCircle(circleOrigin, radius, end)) {
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

        //If we ignore the end position and the origin and the end position are the same, we return false. (Happens for example, if we move to a food source (food source is target))
        if (ignoreTargetTile && Vector2D.distance(circleOrigin, end) == 0)
            return false;

        return minDist <= radius && maxDist >= radius;
    }

    /**
     * Checks, if two circles intersect.
     *
     * @param circleOrigin1 The {@link Vector2D} center point of the first circle.
     * @param radius1       The radius of the first circle.
     * @param circleOrigin2 The {@link Vector2D} center point of the second circle.
     * @param radius2       The radius of the second circle.
     * @return true, if the circles intersect.
     */
    public boolean doTheCirclesIntersect(Vector2D circleOrigin1, double radius1, Vector2D circleOrigin2, double radius2) {
        double distSq = Math.pow(Vector2D.distance(circleOrigin1, circleOrigin2), 2);
        double radSumSq = Math.pow(radius1 + radius2, 2);
        return distSq < radSumSq;
    }

    /**
     * Gets the current time of the simulation
     *
     * @return A {@link SimulationTime} object.
     */
    public SimulationTime getCurrentSimulationTime() {
        return simulationTime;
    }

}