package com.dhbw.thesim.core.map;

import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a landscape
 *
 * @author Daniel Czeschner
 */
public class SimulationMap {

    //region variables
    /**
     * The name if the current landscape
     */
    private final String landscapeName;
    /**
     * The background grid-field
     *
     * @see Tile
     */
    private final Tile[][] tiles;

    //region constants

    /**
     * The width of the grid-field (as specified in the requirement's specification)
     */
    public static final int width = 36;
    /**
     * The height of the grid-field (as specified in the requirement's specification)
     */
    public static final int height = 24;

    //endregion

    //endregion

    /**
     * The constructor for a new simulation map
     *
     * @param landscapeName The name for this landscape
     */
    public SimulationMap(String landscapeName) {
        this.landscapeName = landscapeName;
        this.tiles = new Tile[width][height];
        initMap();
    }

    //TODO make dynamic
    private void initMap() {
        Image tmp = Tile.tmpSprite();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(tmp, x, y);
            }
        }

        for (int x = 15; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].setSwimmable(true);
            }
        }
    }

    /**
     * Checks, if gird coordination are inside the grid.
     *
     * @param gridX The x grid position.
     * @param gridY The y grid position.
     * @return true, if the gird position is inside the grid.
     */
    private boolean isInsideOfGrid(int gridX, int gridY) {
        return gridX >= 0 && gridY >= 0 && gridX < width && gridY < height;
    }

    /**
     * Checks, if a {@link Vector2D} point is inside the grid.
     *
     * @param point The {@link Vector2D}, which should be checked.
     * @return true, if the point is inside the grid.
     */
    public boolean isInsideOfGrid(Vector2D point) {
        Vector2D gridPos = getGridPosition(point);
        return isInsideOfGrid((int) gridPos.getX(), (int) gridPos.getY());
    }

    public Vector2D getWorldPosition(int gridX, int gridY) {
        if (!isInsideOfGrid(gridX, gridY)) return null;

        return new Vector2D(gridX * Tile.TILE_SIZE, gridY * Tile.TILE_SIZE);
    }

    /**
     * Gets the Tile at a specific grid position.
     *
     * @param gridX The x grid position.
     * @param gridY The y grid position.
     * @return {@link Tile} at this position or null
     */
    public Tile getTileAtPosition(int gridX, int gridY) {
        if (isInsideOfGrid(gridX, gridY)) {
            return tiles[gridX][gridY];
        }
        return null;
    }

    /**
     * Gets the grid position of a {@link Vector2D}. It is not checked, if this position is inside the grid.
     *
     * @param worldPosition The {@link Vector2D}, which should be checked.
     * @return An {@link Vector2D} with the x,y position.
     */
    private Vector2D getGridPosition(Vector2D worldPosition) {
        int x = (int) Math.floor(worldPosition.getX() / Tile.TILE_SIZE);
        int y = (int) Math.floor(worldPosition.getY() / Tile.TILE_SIZE);
        return new Vector2D(x, y);
    }

    /**
     * Gets a {@link Tile} at a specific {@link Vector2D}.
     *
     * @param worldPosition The position {@link Vector2D}.
     * @return {@link Tile} at this position or null.
     */
    public Tile getTileAtPosition(Vector2D worldPosition) {
        Vector2D pos = getGridPosition(worldPosition);
        return getTileAtPosition((int) pos.getX(), (int) pos.getY());
    }


    /**
     * Checks, if a tile at a specific grid coordination matched the conditions for swimmable and climbable for a {@link com.dhbw.thesim.core.entity.Dinosaur}
     *
     * @param gridX    The x grid position.
     * @param gridY    The y grid position.
     * @param canSwim  true, if the dinosaur can swim
     * @param canClimb true, if the dinosaur can climb.
     * @return true, if the dinosaur can move to this position.
     */
    public boolean tileMatchedConditions(int gridX, int gridY, boolean canSwim, boolean canClimb) {
        Tile tile = getTileAtPosition(gridX, gridY);
        return tileMatchedConditions(tile, canSwim, canClimb);
    }

    /**
     * Checks, if a tile at a specific {@link Vector2D} position matched the conditions for swimmable and climbable for a {@link com.dhbw.thesim.core.entity.Dinosaur}
     *
     * @param worldPosition The {@link Vector2D} position, on which the tile should be checked.
     * @param canSwim       true, if the dinosaur can swim
     * @param canClimb      true, if the dinosaur can climb.
     * @return true, if the dinosaur can move to this position.
     */
    public boolean tileMatchedConditions(Vector2D worldPosition, boolean canSwim, boolean canClimb) {
        Tile tile = getTileAtPosition(worldPosition);
        return tileMatchedConditions(tile, canSwim, canClimb);
    }

    /**
     * Checks if a tile matched the conditions for swimmable and climbable for a {@link com.dhbw.thesim.core.entity.Dinosaur}
     *
     * @param tile     The {@link Tile} we want to check.
     * @param canSwim  true, if the dinosaur can swim
     * @param canClimb true, if the dinosaur can climb.
     * @return true, if the dinosaur can move onto this tile.
     */
    private boolean tileMatchedConditions(Tile tile, boolean canSwim, boolean canClimb) {
        return tile != null && (!tile.isSwimmable() && !tile.isClimbable() || tile.isSwimmable() && canSwim || tile.isClimbable() && canClimb);
    }

    private boolean tileConditionsAre(Tile tile, boolean swimmable, boolean climbable) {
        return tile.isSwimmable() == swimmable && tile.isClimbable() == climbable;
    }


    /**
     * Get all {@link Tile} in range, which met the condisions.
     *
     * @param tile      The tile, from where we want to check
     * @param range     The range of tiles.
     * @param swimmable Do the matching {@link Tile}s need to be swimmable
     * @param climbable Do the matching {@link Tile}s need to be climbable
     * @return A List with all matching tiles.
     */
    private List<Tile> getTilesInRangeWhereConditionsAre(Tile tile, int range, boolean swimmable, boolean climbable) {
        List<Tile> tileObjects = new ArrayList<>();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {

                if (x == 0 && y == 0)
                    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY)) {
                    Tile tmpTile = getTileAtPosition(checkX, checkY);
                    if (tileConditionsAre(tmpTile, swimmable, climbable))
                        tileObjects.add(tmpTile);
                }
            }
        }

        return tileObjects;
    }

    //region getter & setter

    /**
     * Gets the name of the landscape.
     *
     * @return The name of this map.
     */
    public String getLandscapeName() {
        return landscapeName;
    }

    /**
     * Gets all Tiles as an 2-dimensional array.
     *
     * @return The 2-Dimensional array with all Tiles. Which represents the current map.
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Gets a random tile on the map, matching the conditions.
     *
     * @param canSwim  Does the object can swim.
     * @param canClimb Does the object can climb.
     * @param random   A {@link Random} object.
     * @return A random {@link Tile}.
     */
    public Tile getRandomTile(boolean canSwim, boolean canClimb, Random random) {
        Tile tile = getTileAtPosition(random.nextInt(0, width), random.nextInt(0, height));

        if (!tileMatchedConditions(tile, canSwim, canClimb)) {
            return getRandomTile(canSwim, canClimb, random);
        }
        return tile;
    }

    /**
     * Gets the center {@link Vector2D} world position of a tile.
     *
     * @param tile The {@link Tile}
     * @return The center position of the tile.
     */
    private Vector2D getCenterPositionOfTile(Tile tile) {
        return getWorldPosition(tile.getGridX(), tile.getGridY()).add(new Vector2D(Tile.TILE_SIZE / 2f, Tile.TILE_SIZE / 2f));
    }

    /**
     * Gets a random tile, matching a {@link com.dhbw.thesim.core.entity.Dinosaur} conditions.
     *
     * @param canSwim  Can the dinosaur swim?
     * @param canClimb Cna the dinosaur climb?
     * @param random   A {@link Random}.
     * @return A random {@link Vector2D} word position matching the conditions.
     */
    public Vector2D getRandomTileCenterPosition(boolean canSwim, boolean canClimb, Random random) {
        return getCenterPositionOfTile(getRandomTile(canSwim, canClimb, random));
    }

    /**
     * Gets all center coordinates of tiles that satisfy the requirements.
     *
     * @param origin    The world position from which we want to check
     * @param range     The radial range (e.g. view range)
     * @param swimmable Do the tile need to be swimmable (water tile)
     * @param climbable Do the tile need to be climbable (mountain tile)
     * @return A list of {@link Vector2D} with all center coordinates
     */
    public List<Vector2D> getMidCoordinatesOfMatchingTiles(Vector2D origin, double range, boolean swimmable, boolean climbable) {
        List<Vector2D> matchingPosition = new ArrayList<>();

        Tile startTile = getTileAtPosition(origin);

        int localRange = (int) Math.ceil(range / Tile.TILE_SIZE);

        for (Tile tile : getTilesInRangeWhereConditionsAre(startTile, localRange, swimmable, climbable)) {
            matchingPosition.add(getCenterPositionOfTile(tile));
        }

        return matchingPosition;
    }

    //endregion
}
