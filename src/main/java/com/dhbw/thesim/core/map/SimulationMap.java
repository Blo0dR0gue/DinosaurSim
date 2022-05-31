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
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                tiles[x][y] = new Tile(tmp, x, y);
            }
        }
    }

    private boolean isInsideOfGrid(int gridX, int gridY)
    {
        if (gridX >= 0 && gridY >= 0 && gridX < width && gridY < height)
        {
            return true;
        }
        return false;
    }

    public boolean isInsideOfGrid(Vector2D point){
        int[] gridPos = getGridPosition(point);
        return gridPos != null && isInsideOfGrid(gridPos[0], gridPos[1]);
    }


    public Tile getTileAtPosition(int gridX, int gridY)
    {
        if (isInsideOfGrid(gridX, gridY))
        {
            return tiles[gridX][gridY];
        }
        return null;
    }

    public int[] getGridPosition(Vector2D worldPosition){
        int x = (int)Math.floor(worldPosition.getX()/Tile.TILE_SIZE);
        int y = (int)Math.floor(worldPosition.getY()/Tile.TILE_SIZE);
        if(x < 0 || y < 0)
            return null;
        return new int[]{x,y};
    }

    public Tile getTileAtPosition(Vector2D worldPosition)
    {
        int[] pos = getGridPosition(worldPosition);
        return getTileAtPosition(pos[0], pos[1]);
    }

    private List<Tile> getTilesInRange(Tile tile, int range)
    {
        List<Tile> tileObjects = new ArrayList<>();

        for (int x = -range; x <= range; x++)
        {
            for (int y = -range; y <= range; y++)
            {
                //if (x == 0 && y == 0)
                //    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY))
                {
                    tileObjects.add(getTileAtPosition(checkX, checkY));
                }
            }
        }

        return tileObjects;
    }


    public boolean tileMatchedConditions(Vector2D worldPosition, boolean swimmable, boolean climbable){
        Tile tile = getTileAtPosition(worldPosition);
        return tileMatchedConditions(tile, swimmable, climbable);
    }

    public boolean tileMatchedConditions(Tile tile, boolean swimmable, boolean climbable){
        return !tile.isSwimmable() && !tile.isClimbable() || tile.isSwimmable() && swimmable || tile.isClimbable() && climbable;
    }

    private List<Tile> getTilesInRangeMatchingConditions(Tile tile, int range, boolean swimmable, boolean climbable)
    {
        List<Tile> tileObjects = new ArrayList<>();

        for (int x = -range; x <= range; x++)
        {
            for (int y = -range; y <= range; y++)
            {
                //if (x == 0 && y == 0)
                //    continue;

                int checkX = tile.getGridX() + x;
                int checkY = tile.getGridY() + y;

                if (isInsideOfGrid(checkX, checkY))
                {
                    Tile tmpTile = getTileAtPosition(checkX, checkY);
                    if(tileMatchedConditions(tile, swimmable, climbable))
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
        List<Tile> tiles = getTilesInRange(getTileAtPosition(center), (int)radius);
        return tiles.get(random.nextInt(tiles.size()));
    }
    //endregion
}
