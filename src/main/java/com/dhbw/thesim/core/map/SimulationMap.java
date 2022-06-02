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
    private String landscapeName;
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
    }

    /**
     * Checks, if gird coordination are inside the grid.
     *
     * @param gridX The x grid position.
     * @param gridY The y grid position.
     * @return true, if the gird position is inside the grid.
     */
    private boolean isInsideOfGrid(int gridX, int gridY) {
        if (gridX >= 0 && gridY >= 0 && gridX < width && gridY < height) {
            return true;
        }
        return false;
    }

    /**
     * Checks, if a {@link Vector2D} point is inside the grid.
     *
     * @param point The {@link Vector2D}, which should be checked.
     * @return true, if the point is inside the grid.
     */
    public boolean isInsideOfGrid(Vector2D point) {
        int[] gridPos = getGridPosition(point);
        return isInsideOfGrid(gridPos[0], gridPos[1]);
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
     * @return An int array -> [x,y]
     */
    private int[] getGridPosition(Vector2D worldPosition) {
        int x = (int) Math.floor(worldPosition.getX() / Tile.TILE_SIZE);
        int y = (int) Math.floor(worldPosition.getY() / Tile.TILE_SIZE);
        return new int[]{x, y};
    }

    /**
     * Gets a {@link Tile} at a specific {@link Vector2D}.
     *
     * @param worldPosition The position {@link Vector2D}.
     * @return {@link Tile} at this position or null.
     */
    public Tile getTileAtPosition(Vector2D worldPosition) {
        int[] pos = getGridPosition(worldPosition);
        return getTileAtPosition(pos[0], pos[1]);
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
     * @param tile
     * @param canSwim  true, if the dinosaur can swim
     * @param canClimb true, if the dinosaur can climb.
     * @return true, if the dinosaur can move onto this tile.
     */
    public boolean tileMatchedConditions(Tile tile, boolean canSwim, boolean canClimb) {
        return tile != null && (!tile.isSwimmable() && !tile.isClimbable() || tile.isSwimmable() && canSwim || tile.isClimbable() && canClimb);
    }

    private List<Tile> getTilesInRange(Tile tile, int range) {
        List<Tile> tileObjects = new ArrayList<>();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                //if (x == 0 && y == 0)
                //    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY)) {
                    tileObjects.add(getTileAtPosition(checkX, checkY));
                }
            }
        }

        return tileObjects;
    }

    private List<Tile> getTilesInRangeMatchingConditions(Tile tile, int range, boolean swimmable, boolean climbable) {
        List<Tile> tileObjects = new ArrayList<>();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                //if (x == 0 && y == 0)
                //    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY)) {
                    Tile tmpTile = getTileAtPosition(checkX, checkY);
                    if (tileMatchedConditions(tile, swimmable, climbable))
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

    public Tile getRandomTileInRange(Vector2D center, double radius, Random random) {
        List<Tile> tiles = getTilesInRange(getTileAtPosition(center), (int) radius);
        return tiles.get(random.nextInt(tiles.size()));
    }

    /**
     * Gets a random tile on the map, matching the conditions.
     *
     * @param canSwim
     * @param canClimb
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

    public Vector2D getCenterPositionOfTile(Tile tile) {
        return getWorldPosition(tile.getGridX(), tile.getGridY()).add(new Vector2D(Tile.TILE_SIZE/2f, Tile.TILE_SIZE/2f));
    }

    public Vector2D getRandomTileCenterPosition(boolean canSwim, boolean canClimb, Random random){
        return getCenterPositionOfTile(getRandomTile(canSwim, canClimb, random));
    }

    //endregion
}
