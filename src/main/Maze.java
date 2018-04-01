package main;

/**
 * Class That Represents The Maze Of The Program
 */
public class Maze {

    private int row;
    private int column;
    private TileType[][] tiles;

    /**
     * Creating The Maze Of The Application
     * @param row number of rows for the maze
     * @param column number of columns for the maze
     */
    public Maze(int row, int column) {
        this.row = row;
        this.column = column;
        this.tiles = new TileType[row][column];
        initializeMaze();
    }

    /**
     * The maze is created. All tiles are initialized to white tiles initially.
     */
    private void initializeMaze() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                tiles[i][j] = TileType.White;
            }
        }
    }

    /**
     * Add a wall to the maze at the location specified by the parameters.
     * @param row The row where the wall will be added.
     * @param column The column where the wall will be added.
     */
    public void addWall(int row, int column) {
        tiles[row][column] = TileType.Wall;
    }

    /**
     * Add a green tile to the maze at the location specified by the parameters.
     * @param row The row where the green tile will be added.
     * @param column The column where the green tile will be added.
     */
    public void addGreenTile(int row, int column) {
        tiles[row][column] = TileType.Green;
    }

    /**
     * Add a brown tile to the maze at the location specified by the parameters.
     * @param row The row where the brown tile will be added.
     * @param column The column where the brown tile will be added.
     */
    public void addBrownTile(int row, int column) {
        tiles[row][column] = TileType.Brown;
    }

    /**
     * Return the number of rows in the maze.
     * @return The number of rows in the maze.
     */
    public int getNumberOfRows() {
        return this.row;
    }

    /**
     * Return the number of columns in the maze.
     * @return The number of columns in the maze.
     */
    public int getNumberOfColumns() {
        return this.column;
    }

    /**
     * Check if the tile at the specified location is a wall.
     * @param row The row where the tile is located.
     * @param column The column where the tile is located.
     * @return Return true if the tile specified at the location is a wall. Else, return false.
     */
    public boolean isWall(int row, int column) {
        return tiles[row][column].equals(TileType.Wall);
    }

    /**
     * Return the type of the tile at the specified location.
     * @param row The row where the tile is located.
     * @param column The column where the tile is located.
     * @return Return the tile type at the specified location.
     */
    public TileType getTileType(int row, int column) {
        return tiles[row][column];
    }

}
