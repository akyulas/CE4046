package main;

/**
 * Created by jodiakyulas on 6/3/18.
 */
public class Maze {

    private int row;
    private int column;
    private TileType[][] tiles;

    public Maze(int row, int column) {
        this.row = row;
        this.column = column;
        this.tiles = new TileType[row][column];
        initializeMaze();
    }

    private void initializeMaze() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                tiles[i][j] = TileType.White;
            }
        }
    }

    public void addWall(int row, int column) {
        tiles[row][column] = TileType.Wall;
    }

    public void addGreenTile(int row, int column) {
        tiles[row][column] = TileType.Green;
    }

    public void addBrownTile(int row, int column) {
        tiles[row][column] = TileType.Brown;
    }

    public int getNumberOfRows() {
        return this.row;
    }

    public int getNumberOfColumns() {
        return this.column;
    }

    public boolean isWall(int row, int column) {
        return tiles[row][column].equals(TileType.Wall);
    }

    public TileType getTileType(int row, int column) {
        return tiles[row][column];
    }

}
