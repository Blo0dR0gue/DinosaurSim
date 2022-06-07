package com.dhbw.thesim.core.map;

import com.dhbw.thesim.core.util.SpriteLibrary;
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
        landscapeOne();
    }

    //TODO make dynamic
    private void initMap() {
        Image tmp = Tile.tmpSprite();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(tmp, x, y, false, false);
            }
        }

        for (int x = 15; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(tmp, x, y, true, false);
            }
        }

    }

    private enum TILES {
        grass("grass.png", false, false),
        sand("sand.png", false, false),
        water("water.png", true, false),
        mountain("mountain.png", false, true);

        public final String imgName;
        public final boolean swimmable;
        public final boolean climbable;

        TILES(String imgName, boolean swimmable, boolean climbable) {
            this.imgName = imgName;
            this.swimmable = swimmable;
            this.climbable = climbable;
        }
    }

    private void landscapeOne() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.grass.imgName), x, y, TILES.grass.swimmable, TILES.grass.climbable);
            }
        }

        //Create river
        for (int x = 0; x < 14; x++) {
            int y = 10;
            tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), x, y, TILES.water.swimmable, TILES.water.climbable);
        }

        for (int y = 6; y < height - 7; y++) {
            int x = 13;
            tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), x, y, TILES.water.swimmable, TILES.water.climbable);
        }

        for (int x = 13; x < 17; x++) {
            int y = height - 7;
            tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), x, y, TILES.water.swimmable, TILES.water.climbable);
        }

        //Create lake
        for (int x = width - 10; x < width - 4; x++) {
            for (int y = 8; y < 14; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), x, y, TILES.water.swimmable, TILES.water.climbable);
            }
        }
        tiles[width - 11][8] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), width - 11, 8, TILES.water.swimmable, TILES.water.climbable);
        tiles[width - 12][8] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), width - 12, 8, TILES.water.swimmable, TILES.water.climbable);

        //Create desert
        tiles[width - 5][13] = new Tile(SpriteLibrary.getInstance().getImage(TILES.sand.imgName), width - 4, 14, TILES.sand.swimmable, TILES.sand.climbable);

        for (int x = width - 9; x < width; x++) {
            for (int y = 0; y < 8; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.sand.imgName), x, y, TILES.sand.swimmable, TILES.sand.climbable);
            }
        }

        for (int x = width - 4; x < width; x++) {
            for (int y = 8; y < 14; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.sand.imgName), x, y, TILES.sand.swimmable, TILES.sand.climbable);
            }
        }

        for (int x = width - 3; x < width; x++) {
            for (int y = 14; y < 16; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.sand.imgName), x, y, TILES.sand.swimmable, TILES.sand.climbable);
            }
        }


        //Create mountain
        for (int x = width - 16; x < width - 10; x++) {
            for (int y = height - 5; y < height; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.mountain.imgName), x, y, TILES.mountain.swimmable, TILES.mountain.climbable);
            }
        }
        for (int x = width - 20; x < width - 16; x++) {
            for (int y = height - 1; y < height; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.mountain.imgName), x, y, TILES.mountain.swimmable, TILES.mountain.climbable);
            }
        }

        //Create small lake
        for (int x = 0; x < 9; x++) {
            if (x < 6)
                for (int y = 0; y < 6; y++) {
                    tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), x, y, TILES.water.swimmable, TILES.water.climbable);
                }
            else
                tiles[x][0] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), x, 0, TILES.water.swimmable, TILES.water.climbable);
        }
        tiles[6][1] = new Tile(SpriteLibrary.getInstance().getImage(TILES.water.imgName), 6, 1, TILES.water.swimmable, TILES.water.climbable);

        //Create small desert
        for (int x = 0; x < 3; x++) {
            for (int y = height - 3; y < height; y++) {
                tiles[x][y] = new Tile(SpriteLibrary.getInstance().getImage(TILES.sand.imgName), x, y, TILES.sand.swimmable, TILES.sand.climbable);
            }
        }
        tiles[3][height - 1] = new Tile(SpriteLibrary.getInstance().getImage(TILES.sand.imgName), 3, height - 1, TILES.sand.swimmable, TILES.sand.climbable);

    }

    private void landscapeTwo() {

    }

    /**
     * Checks, if gird coordination are inside the grid.
     *
     * @param gridX The x grid position.
     * @param gridY The y grid position.
     * @return true, if the gird position is inside the grid.
     */
    public boolean isInsideOfGrid(int gridX, int gridY) {
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
