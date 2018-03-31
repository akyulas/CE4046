package main;

import main.Maze;

/**
 * Created by jodiakyulas on 6/3/18.
 */
public class Part1 {

    private int no_of_rows = 6;
    private int no_of_columns = 6;
    String directoryAdder = "Part1/";

    public void start(){
        System.out.println("Part 1 is starting.\n");
        Maze maze = new Maze(no_of_rows, no_of_columns);

        addWalls(maze);
        addGreenTiles(maze);
        addBrownTiles(maze);

        ValueIteration valueIteration = new ValueIteration(maze, directoryAdder);
        valueIteration.start();
        PolicyIteration policyIteration = new PolicyIteration(maze, directoryAdder);
        policyIteration.start();
    }

    private void addWalls(Maze maze) {
        maze.addWall(0, 1);
        maze.addWall(1, 4);
        maze.addWall(4, 1);
        maze.addWall(4, 2);
        maze.addWall(4, 3);
    }

    private void addGreenTiles(Maze maze) {
        maze.addGreenTile(0, 0);
        maze.addGreenTile(0, 2);
        maze.addGreenTile(0, 5);
        maze.addGreenTile( 1, 3);
        maze.addGreenTile(2, 4);
        maze.addGreenTile(3, 5);
    }


    private void addBrownTiles(Maze maze) {
        maze.addBrownTile(1, 1);
        maze.addBrownTile(1, 5);
        maze.addBrownTile(2, 2);
        maze.addBrownTile(3, 3);
        maze.addBrownTile(4,4);
    }

}
