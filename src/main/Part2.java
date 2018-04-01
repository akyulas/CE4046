package main;

import util.Utilities;

/**
 * Class That Represents Part 2 of the assignment.
 */
public class Part2 {

    private int no_of_rows = 10;
    private int no_of_columns = 10;
    String directoryAdder = "Part2/";

    /**
     * Start of the part 2 of the assignment.
     */
    public void start(){
        System.out.println("Part 2 is starting.\n");
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
        for (int i = 0; i < no_of_columns; i++) {
            if (i % 2 == 0)
                maze.addWall(0, i);
        }
        maze.addWall(1, 0);
        maze.addWall(2, 9);
        maze.addWall(3, 1);
        maze.addWall(3, 9);
        maze.addWall(4, 0);
        maze.addWall(4, 1);
        maze.addWall(5, 4);
        maze.addWall(5, 9);
        maze.addWall(6, 4);
        maze.addWall(7, 5);
        maze.addWall(8, 0);
        maze.addWall(8, 9);
        for (int i = 0; i < no_of_columns; i++) {
            if (i % 2 == 0)
                maze.addWall(no_of_rows - 1, i);
        }
    }

    /**
     * Add green tiles at the specified location according to the diagram in the assignment.
     * @param maze The maze where the green tiles will be added.
     */
    private void addGreenTiles(Maze maze) {
        maze.addGreenTile(1, 0);
        maze.addGreenTile(1, 3);
        maze.addGreenTile(1, 5);
        maze.addGreenTile(2, 0);
        maze.addGreenTile(2, 4);
        maze.addGreenTile( 1, 3);
        maze.addGreenTile(2, 4);
        maze.addGreenTile(3, 5);
        maze.addGreenTile(5, 0);
        maze.addGreenTile(5, 3);
        maze.addGreenTile(6, 8);
        maze.addGreenTile(7, 4);
        maze.addGreenTile(7, 6);
        maze.addGreenTile(7, 9);
        maze.addGreenTile(8, 3);
        maze.addGreenTile(8, 6);
        maze.addGreenTile(9, 4);
    }

    /**
     * Add brown tiles at the specified location according to the diagram in the assignment.
     * @param maze The maze where the brown tiles will be added.
     */
    private void addBrownTiles(Maze maze) {
        maze.addBrownTile(1, 7);
        maze.addBrownTile(1, 8);
        maze.addBrownTile(2, 5);
        maze.addBrownTile(3, 9);
        maze.addBrownTile(4, 3);
        maze.addBrownTile(4,4);
        maze.addBrownTile(5, 6);
        maze.addBrownTile(6, 0);
        maze.addBrownTile(6, 9);
        maze.addBrownTile(8, 1);
        maze.addBrownTile(8, 4);
        maze.addBrownTile(8, 5);
        maze.addBrownTile(9, 8);
    }

}
