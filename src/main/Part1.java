package main;

import util.Utilities;

/**
 * Class That Represents Part 1 of the assignment.
 */
public class Part1 {

    private int no_of_rows = 6;
    private int no_of_columns = 6;
    String directoryAdder = "Part1/";

    /**
     * Start of the part 1 of the assignment.
     */
    public void start(){
        System.out.println("Part 1 is starting.\n");
        Maze maze = new Maze(no_of_rows, no_of_columns);

        addWalls(maze);
        addGreenTiles(maze);
        addBrownTiles(maze);

        Utilities.printOutMaze(maze);

        ValueIteration valueIteration = new ValueIteration(maze, directoryAdder);
        valueIteration.start();
        PolicyIteration policyIteration = new PolicyIteration(maze, directoryAdder);
        policyIteration.start();
    }

    /**
     * Add walls at the specified location according to the diagram in the assignment.
     * @param maze The maze where the walls will be added.
     */
    private void addWalls(Maze maze) {
        maze.addWall(0, 1);
        maze.addWall(1, 4);
        maze.addWall(4, 1);
        maze.addWall(4, 2);
        maze.addWall(4, 3);
    }

    /**
     * Add green tiles at the specified location according to the diagram in the assignment.
     * @param maze The maze where the green tiles will be added.
     */
    private void addGreenTiles(Maze maze) {
        maze.addGreenTile(0, 0);
        maze.addGreenTile(0, 2);
        maze.addGreenTile(0, 5);
        maze.addGreenTile( 1, 3);
        maze.addGreenTile(2, 4);
        maze.addGreenTile(3, 5);
    }

    /**
     * Add brown tiles at the specified location according to the diagram in the assignment.
     * @param maze The maze where the brown tiles will be added.
     */
    private void addBrownTiles(Maze maze) {
        maze.addBrownTile(1, 1);
        maze.addBrownTile(1, 5);
        maze.addBrownTile(2, 2);
        maze.addBrownTile(3, 3);
        maze.addBrownTile(4,4);
    }

}
