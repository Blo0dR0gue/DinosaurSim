package com.dhbw.thesim.core.map;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.core.util.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a landscape.
 *
 * @author Daniel Czeschner, Eric Stefan
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
    public static final int WIDTH = 36;
    /**
     * The height of the grid-field (as specified in the requirement's specification)
     */
    public static final int HEIGHT = 24;

    /**
     * The name for the first defined landscape.
     */
    public static final String LANDSCAPE_ONE_NAME = "Landschaft1";

    /**
     * The name for the second defined landscape.
     */
    public static final String LANDSCAPE_TWO_NAME = "Landschaft2";

    /**
     * The instance of the {@link SpriteLibrary}
     */
    private final SpriteLibrary spriteLibrary;

    //endregion

    /**
     * Available tiles. <br>
     * Enum contains all information for each tile type.
     */
    public enum TILES {
        GRASS("grass.png", "Gras", false, false, true),
        SAND("sand.png", "Sand", false, false, false),
        WATER("water.png", "Wasser", true, false, false),
        MOUNTAIN("mountain.png", "Gebirge", false, true, false),
        FOREST("forest.png", "Wald", false, true, true);

        public final String imgName;
        public final String deName;
        public final boolean swimmable;
        public final boolean climbable;
        public final boolean canContainPlants;

        TILES(String imgName, String deName, boolean swimmable, boolean climbable, boolean canContainPlants) {
            this.imgName = imgName;
            this.deName = deName;
            this.swimmable = swimmable;
            this.climbable = climbable;
            this.canContainPlants = canContainPlants;
        }
    }

    //endregion

    /**
     * The constructor for a new {@link SimulationMap}.
     *
     * @param landscapeName The name for this landscape
     * @param spriteLibrary The instance of the {@link SpriteLibrary}
     */
    public SimulationMap(String landscapeName, SpriteLibrary spriteLibrary) {
        this.landscapeName = landscapeName;
        this.spriteLibrary = spriteLibrary;
        this.tiles = new Tile[WIDTH][HEIGHT];

        //Select a defined landscape based on the name.
        if (landscapeName.equalsIgnoreCase(LANDSCAPE_ONE_NAME))
            landscapeOne();
        else if (landscapeName.equalsIgnoreCase(LANDSCAPE_TWO_NAME))
            landscapeTwo();
        else
            landscapeOne();
    }

    //region landscape definition

    /**
     * The first landscape. <br>
     * Defined in code, because json based and procedural generated maps are priority 3 and will be added in a later version .
     */
    private void landscapeOne() {

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.GRASS.imgName), x, y, TILES.GRASS.swimmable, TILES.GRASS.climbable, TILES.GRASS.canContainPlants);
            }
        }

        //Create river
        for (int x = 0; x < 14; x++) {
            int y = 10;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        }
        for (int y = 6; y < HEIGHT - 7; y++) {
            int x = 13;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        }
        for (int x = 13; x < 17; x++) {
            int y = HEIGHT - 7;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        }

        //Create lake
        for (int x = WIDTH - 10; x < WIDTH - 4; x++) {
            for (int y = 8; y < 14; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }
        tiles[WIDTH - 11][8] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), WIDTH - 11, 8, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        tiles[WIDTH - 12][8] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), WIDTH - 12, 8, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);

        //Create desert
        tiles[WIDTH - 5][13] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), WIDTH - 4, 14, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.WATER.canContainPlants);
        for (int x = WIDTH - 9; x < WIDTH; x++) {
            for (int y = 0; y < 8; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }
        for (int x = WIDTH - 4; x < WIDTH; x++) {
            for (int y = 8; y < 14; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }
        for (int x = WIDTH - 3; x < WIDTH; x++) {
            for (int y = 14; y < 16; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }

        //Create mountain
        for (int x = WIDTH - 16; x < WIDTH - 10; x++) {
            for (int y = HEIGHT - 5; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.MOUNTAIN.imgName), x, y, TILES.MOUNTAIN.swimmable, TILES.MOUNTAIN.climbable, TILES.MOUNTAIN.canContainPlants);
            }
        }
        for (int x = WIDTH - 20; x < WIDTH - 16; x++) {
            for (int y = HEIGHT - 1; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.MOUNTAIN.imgName), x, y, TILES.MOUNTAIN.swimmable, TILES.MOUNTAIN.climbable, TILES.MOUNTAIN.canContainPlants);
            }
        }

        //Create small lake
        for (int x = 0; x < 9; x++) {
            if (x < 6)
                for (int y = 0; y < 6; y++) {
                    tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
                }
            else
                tiles[x][0] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, 0, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        }
        tiles[6][1] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), 6, 1, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);

        //Create small desert
        for (int x = 0; x < 3; x++) {
            for (int y = HEIGHT - 3; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }
        tiles[3][HEIGHT - 1] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), 3, HEIGHT - 1, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);

    }

    /**
     * The second landscape. <br>
     * Defined in code, because json based and procedural generated maps are priority 3 and will be added in a later version .
     */
    private void landscapeTwo() {
        //initial
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.GRASS.imgName), x, y, TILES.GRASS.swimmable, TILES.GRASS.climbable, TILES.GRASS.canContainPlants);
            }
        }

        //create river
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }
        for (int x = 0; x < 5; x++) {
            for (int y = 6; y < 9; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }
        for (int x = 0; x < 3; x++) {
            for (int y = 9; y < 11; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }
        tiles[0][11] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), 0, 11, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        for (int x = 7; x < 11; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }

        //create second river
        for (int y = 0; y < 3; y++) {
            int x = WIDTH - 7;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        }

        for (int x = WIDTH - 6; x < WIDTH; x++) {
            for (int y = 0; y < 8; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }
        for (int x = WIDTH - 4; x < WIDTH; x++) {
            for (int y = 8; y < 11; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }

        //create lake in the middle
        for (int x = 10; x < 15; x++) {
            for (int y = 10; y < 15; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
            }
        }
        for (int x = 12; x < 15; x++) {
            int y = 15;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.WATER.imgName), x, y, TILES.WATER.swimmable, TILES.WATER.climbable, TILES.WATER.canContainPlants);
        }

        //create little dessert in the middle
        for (int y = 11; y < 15; y++) {
            int x = 15;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
        }
        for (int y = 11; y < 13; y++) {
            int x = 16;
            tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
        }

        //create big right dessert
        for (int x = WIDTH - 11; x < WIDTH; x++) {
            for (int y = 11; y < 17; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }
        for (int x = WIDTH - 9; x < WIDTH - 4; x++) {
            for (int y = 8; y < 11; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }
        for (int x = WIDTH - 7; x < WIDTH - 3; x++) {
            for (int y = 17; y < 20; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), x, y, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);
            }
        }
        tiles[WIDTH - 8][17] = new Tile(this.spriteLibrary.getImage(TILES.SAND.imgName), WIDTH - 8, 17, TILES.SAND.swimmable, TILES.SAND.climbable, TILES.SAND.canContainPlants);

        //create mountain
        for (int x = WIDTH - 3; x < WIDTH; x++) {
            for (int y = HEIGHT - 7; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.MOUNTAIN.imgName), x, y, TILES.MOUNTAIN.swimmable, TILES.MOUNTAIN.climbable, TILES.MOUNTAIN.canContainPlants);
            }
        }
        for (int x = WIDTH - 10; x < WIDTH - 3; x++) {
            for (int y = HEIGHT - 4; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.MOUNTAIN.imgName), x, y, TILES.MOUNTAIN.swimmable, TILES.MOUNTAIN.climbable, TILES.MOUNTAIN.canContainPlants);
            }
        }
        for (int x = WIDTH - 13; x < WIDTH - 10; x++) {
            for (int y = HEIGHT - 2; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(this.spriteLibrary.getImage(TILES.MOUNTAIN.imgName), x, y, TILES.MOUNTAIN.swimmable, TILES.MOUNTAIN.climbable, TILES.MOUNTAIN.canContainPlants);
            }
        }
    }

    //endregion

    /**
     * Checks, if grid coordination are inside the grid.
     *
     * @param gridX The x grid position.
     * @param gridY The y grid position.
     * @return true, if the gird position is inside the grid.
     */
    public boolean isInsideOfGrid(int gridX, int gridY) {
        return gridX >= 0 && gridY >= 0 && gridX < WIDTH && gridY < HEIGHT;
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

    /**
     * Gets the world position for a grid tile.
     *
     * @param gridX The x coordinate of the tile.
     * @param gridY The y coordinate of the tile.
     * @return The center position of the grid tile.
     */
    public Vector2D getWorldPosition(int gridX, int gridY) {
        if (!isInsideOfGrid(gridX, gridY)) return null;

        return new Vector2D(gridX * Tile.TILE_SIZE, gridY * Tile.TILE_SIZE);
    }

    /**
     * Gets a {@link Tile} at a specific grid position.
     *
     * @param gridX The x grid coordinate.
     * @param gridY The y grid coordinate.
     * @return The {@link Tile} at this position or null if not found.
     */
    public Tile getTileAtPosition(int gridX, int gridY) {
        if (isInsideOfGrid(gridX, gridY)) {
            return tiles[gridX][gridY];
        }
        return null;
    }

    /**
     * Gets the grid position of a {@link Vector2D}. <br>
     * It is not checked, if this position is inside the grid.
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
     * @return The {@link Tile} at this position or null if not found.
     */
    public Tile getTileAtPosition(Vector2D worldPosition) {
        Vector2D pos = getGridPosition(worldPosition);
        return getTileAtPosition((int) pos.getX(), (int) pos.getY());
    }


    /**
     * Checks if a tile at a specific grid coordinate matched the conditions for swimmable and climbable for a {@link com.dhbw.thesim.core.entity.Dinosaur}
     *
     * @param gridX    The x grid coordinate.
     * @param gridY    The y grid coordinate.
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

    /**
     * Checks if the conditions of a {@link Tile} are the passed one.
     *
     * @param tile      The {@link Tile} which should be checked.
     * @param swimmable Does the tile need to be swimmable? (water)
     * @param climbable Does the tile need to be climbable? (mountain)
     * @return true if the {@link Tile} matches the conditions.
     */
    private boolean tileConditionsAre(Tile tile, boolean swimmable, boolean climbable) {
        return tile.isSwimmable() == swimmable && tile.isClimbable() == climbable;
    }

    /**
     * Get all {@link Tile}s in range, which met the conditions exactly.
     *
     * @param tile      The tile, from where we want to check
     * @param range     The range of tiles.
     * @param swimmable Do the matching {@link Tile}s need to be swimmable?
     * @param climbable Do the matching {@link Tile}s need to be climbable?
     * @return A List with all matching {@link Tile}s.
     * @see #tileConditionsAre(Tile, boolean, boolean)
     */
    private List<Tile> getTilesInRangeWhereConditionsAre(Tile tile, int range, boolean swimmable, boolean climbable) {
        List<Tile> tileObjects = new ArrayList<>();
        if (tile != null)
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

    /**
     * Get all {@link Tile}s in range, which met the conditions.
     *
     * @param tile      The tile, from where we want to check
     * @param range     The range of tiles.
     * @param swimmable Do the matching {@link Tile}s need to be swimmable?
     * @param climbable Do the matching {@link Tile}s need to be climbable?
     * @return A List with all matching {@link Tile}s.
     * @see #tileMatchedConditions(Tile, boolean, boolean)
     */
    private List<Tile> getTilesInRangeWhereConditionsMatch(Tile tile, int range, boolean swimmable, boolean climbable) {
        List<Tile> tileObjects = new ArrayList<>();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {

                if (x == 0 && y == 0)
                    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY)) {
                    Tile tmpTile = getTileAtPosition(checkX, checkY);
                    if (tileMatchedConditions(tmpTile, swimmable, climbable))
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
     * Gets all {@link Tile}s as an 2-dimensional array.
     *
     * @return The 2-dimensional array with all {@link Tile}s, which represents the current map.
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Gets a random tile on the map, matching the conditions.
     *
     * @param canSwim  Can the {@link Tile} be swimmable?
     * @param canClimb Can the {@link Tile} be climbable?
     * @param random   A {@link Random} object.
     * @return A random {@link Tile}.
     * @see #tileMatchedConditions(Tile, boolean, boolean)
     */
    public Tile getRandomTile(boolean canSwim, boolean canClimb, Random random) {
        if(canSwim && canClimb) return null;

        Tile tile = getTileAtPosition(random.nextInt(0, WIDTH), random.nextInt(0, HEIGHT));

        if (!tileMatchedConditions(tile, canSwim, canClimb)) {
            return getRandomTile(canSwim, canClimb, random);
        }
        return tile;
    }

    /**
     * Gets a random {@link Tile} on the map, where the conditions are.
     *
     * @param canSwim     Does the {@link Tile} needs to be swimmable?
     * @param canClimb    Does the {@link Tile} needs to be climbable?
     * @param allowPlants Does the tile needs to allow plants?
     * @param random      A {@link Random} object.
     * @return A random {@link Tile}.
     * @see #tileConditionsAre(Tile, boolean, boolean)
     */
    public Tile getRandomTileWhereConditionsAre(boolean canSwim, boolean canClimb, boolean allowPlants, Random random) {
        if((canSwim || canClimb) && allowPlants || canSwim && canClimb) return null;

        Tile tile = getTileAtPosition(random.nextInt(0, WIDTH), random.nextInt(0, HEIGHT));

        if (!tileConditionsAre(tile, canSwim, canClimb) || tile.arePlantsAllowed() != allowPlants) {
            return getRandomTileWhereConditionsAre(canSwim, canClimb, allowPlants, random);
        }
        return tile;
    }

    /**
     * Gets the center {@link Vector2D} world position of a {@link Tile}.
     *
     * @param tile The {@link Tile}.
     * @return The center position of the {@link Tile}.
     */
    private Vector2D getCenterPositionOfTile(Tile tile) {
        return getWorldPosition(tile.getGridX(), tile.getGridY()).add(new Vector2D(Tile.TILE_SIZE / 2f, Tile.TILE_SIZE / 2f));
    }

    /**
     * Gets the center position of a random {@link Tile}, matching a {@link com.dhbw.thesim.core.entity.Dinosaur} conditions.
     *
     * @param canSwim  Can the {@link Tile} be swimmable?
     * @param canClimb Can the {@link Tile} be climbable?
     * @param random   A {@link Random}.
     * @return A random {@link Vector2D} word position matching the conditions.
     */
    public Vector2D getRandomTileCenterPosition(boolean canSwim, boolean canClimb, Random random) {
        Tile tile = getRandomTile(canSwim, canClimb, random);
        if(tile != null)
            return getCenterPositionOfTile(tile);
        else
            return null;
    }

    /**
     * Checks if the neighbor {@link Tile}s match the conditions.
     *
     * @param tileWorldPosition The {@link Vector2D} world position of the {@link Tile}, which should be checked.
     * @param swimmable         Can the {@link Tile} be swimmable?
     * @param climbable         Can the {@link Tile} be climbable
     * @param rangeInPixel      The range in pixel which neighbors should be selected.
     * @return true if they match the conditions.
     * @see #tileMatchedConditions(Tile, boolean, boolean)
     */
    public boolean checkIfNeighborTilesMatchConditions(Vector2D tileWorldPosition, boolean swimmable, boolean climbable, double rangeInPixel) {
        Tile tile = getTileAtPosition(tileWorldPosition);

        int localRange = (int) Math.ceil(rangeInPixel / Tile.TILE_SIZE);

        for (int x = -localRange; x <= localRange; x++) {
            for (int y = -localRange; y <= localRange; y++) {

                if (x == 0 && y == 0)
                    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY)) {
                    Tile tmpTile = getTileAtPosition(checkX, checkY);
                    if (!tileMatchedConditions(tmpTile, swimmable, climbable))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the neighbor {@link Tile}s have the conditions.
     *
     * @param tileWorldPosition The {@link Vector2D} world position of the {@link Tile}, which should be checked.
     * @param swimmable         Does the {@link Tile} need to be swimmable?
     * @param climbable         Does the {@link Tile} need to be climbable
     * @param allowPlants       Does the tile need to allow plants?
     * @param rangeInPixel      The range in pixel which neighbors should be selected.
     * @return true, if they match the conditions
     * @see #tileConditionsAre(Tile, boolean, boolean)
     */
    public boolean checkIfNeighborTilesHasConditions(Vector2D tileWorldPosition, boolean swimmable, boolean climbable, boolean allowPlants, double rangeInPixel) {
        Tile tile = getTileAtPosition(tileWorldPosition);

        int localRange = (int) Math.ceil(rangeInPixel / Tile.TILE_SIZE);

        for (int x = -localRange; x <= localRange; x++) {
            for (int y = -localRange; y <= localRange; y++) {

                if (x == 0 && y == 0)
                    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY)) {
                    Tile tmpTile = getTileAtPosition(checkX, checkY);
                    if (!tileConditionsAre(tmpTile, swimmable, climbable) || tile.arePlantsAllowed() != allowPlants)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets a random {@link Vector2D} of a tile, where conditions are...
     *
     * @param swimmable   Does the tile need to be swimmable?
     * @param climbable   Does the tile need to be climbable?
     * @param allowPlants Does the tile need to allow plants?
     * @param random      A {@link Random}.
     * @return A random {@link Vector2D} word position matching the conditions.
     */
    public Vector2D getRandomTileCenterPositionWhereConditionsAre(boolean swimmable, boolean climbable, boolean allowPlants, Random random) {
        Tile tile = getRandomTileWhereConditionsAre(swimmable, climbable, allowPlants, random);
        if(tile != null)
            return getCenterPositionOfTile(tile);
        return null;
    }

    /**
     * Gets all center coordinates of {@link Tile}s that satisfy the requirements.
     *
     * @param origin    The world position from which we want to check
     * @param range     The radial range (e.g. view range)
     * @param swimmable Can the {@link Tile}s be swimmable? (water tile)
     * @param climbable Can the {@link Tile}s need to be climbable? (mountain tile)
     * @return A list of {@link Vector2D} with all center coordinates for each {@link Tile}.
     * @see #getTilesInRangeWhereConditionsAre(Tile, int, boolean, boolean)
     */
    public List<Vector2D> getMidCoordinatesTilesWhereConditionsAre(Vector2D origin, double range, boolean swimmable, boolean climbable) {
        List<Vector2D> matchingPosition = new ArrayList<>();

        Tile startTile = getTileAtPosition(origin);

        int localRange = (int) Math.ceil(range / Tile.TILE_SIZE);

        for (Tile tile : getTilesInRangeWhereConditionsAre(startTile, localRange, swimmable, climbable)) {
            matchingPosition.add(getCenterPositionOfTile(tile));
        }

        return matchingPosition;
    }

    /**
     * Gets all center coordinates of {@link Tile}s that satisfy the requirements.
     *
     * @param origin    The world position from which we want to check
     * @param range     The radial range (e.g. view range)
     * @param swimmable Does the {@link Tile}s need to be swimmable? (water)
     * @param climbable Does the {@link Tile}s need to be climbable? (mountain)
     * @return A list of {@link Vector2D} with all center coordinates of each {@link Tile}.
     * @see #getTilesInRangeWhereConditionsMatch(Tile, int, boolean, boolean)
     */
    public List<Vector2D> getMidCoordinatesOfTilesWhereConditionsMatch(Vector2D origin, double range, boolean swimmable, boolean climbable) {
        List<Vector2D> matchingPosition = new ArrayList<>();

        Tile startTile = getTileAtPosition(origin);

        int localRange = (int) Math.ceil(range / Tile.TILE_SIZE);

        for (Tile tile : getTilesInRangeWhereConditionsMatch(startTile, localRange, swimmable, climbable)) {
            matchingPosition.add(getCenterPositionOfTile(tile));
        }

        return matchingPosition;
    }


    //endregion
}
