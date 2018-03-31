package main;

/**
 * Created by jodiakyulas on 6/3/18.
 */
public class Part2 {

    public Part2() {
        System.out.println("Part2 is starting.\n");
    }

    public void start(int rows, int columns){
        int no_of_rows = rows;
        int no_of_columns = columns;
        System.out.println("The maze is: " + no_of_rows + "x" + no_of_columns + "\n");
        String directoryAdder = "Part2/" + rows + "x" + columns + "/";
        Maze maze = new Maze(no_of_rows, no_of_columns);
        int count = 0;
        for (int i = 0; i < no_of_rows; i++) {
            for (int j = 0; j < no_of_columns; j++) {
                int result = count % 4;
                switch(result) {
                    case(1):
                        maze.addGreenTile(i, j);
                        break;
                    case(2):
                        maze.addBrownTile(i, j);
                        break;
                    case(3):
                        maze.addWall(i, j);
                        break;
                }
                count++;
            }
        }
        ValueIteration valueIteration = new ValueIteration(maze, directoryAdder);
        valueIteration.start();
        PolicyIteration policyIteration = new PolicyIteration(maze, directoryAdder);
        policyIteration.start();
    }

}
