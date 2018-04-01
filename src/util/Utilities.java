package util;

import main.Direction;
import main.Maze;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.File;

/**
 * Class that can be used for utility.
 */
public class Utilities {

    /**
     * Print out the maze.
     * @param maze The maze to be printed out.
     */
    public static void printOutMaze(Maze maze) {
        int row = maze.getNumberOfRows();
        int column = maze.getNumberOfColumns();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < 3 * column; j++) {
                System.out.print("-");
            }
            System.out.println();
            for (int j = 0; j < column; j++) {
                System.out.print("|");
                switch(maze.getTileType(i, j)) {
                    case Wall:
                        System.out.print("O");
                        break;
                    case Brown:
                        System.out.print("B");
                        break;
                    case Green:
                        System.out.print("G");
                        break;
                    case White:
                        System.out.print("W");
                        break;
                }
                System.out.print("|");
            }
            System.out.println();
            for (int j = 0; j < 3 * column; j++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    /**
     * Print out the policy
     * @param maze The maze created by the application.
     * @param policy The policy obtained from the application.
     * @param no_of_rows The number of rows in the maze.
     * @param no_of_columns The number of columns in the maze.
     */
    public static void printOutPolicy(Maze maze, Direction[][] policy, int no_of_rows, int no_of_columns) {
        int row = no_of_rows;
        int column = no_of_columns;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < 3 * column; j++) {
                System.out.print("-");
            }
            System.out.println();
            for (int j = 0; j < column; j++) {
                System.out.print("|");
                if (maze.isWall(i, j)) {
                    System.out.print("X");
                    System.out.print("|");
                    continue;
                }
                switch(policy[i][j]) {
                    case Up:
                        System.out.print("U");
                        break;
                    case Right:
                        System.out.print("R");
                        break;
                    case Down:
                        System.out.print("D");
                        break;
                    case Left:
                        System.out.print("L");
                        break;
                }
                System.out.print("|");
            }
            System.out.println();
            for (int j = 0; j < 3 * column; j++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    /**
     * Print out the results.
     * @param maze The maze created by the application.
     * @param utilities The utilites obtained from the application.
     * @param policy The policy obtained from the application.
     * @param numberOfIterations The number of iterations before convergence.
     */
    public static void printOutResults(Maze maze, Double[][] utilities, Direction[][] policy, int numberOfIterations) {
        for (int j = 0; j < maze.getNumberOfColumns(); j++) {
            for (int i = 0; i < maze.getNumberOfRows(); i++) {
                if (maze.isWall(i , j))
                    continue;
                System.out.println("(" + j + "," + i + ")" + ": " + utilities[i][j]+ " (" + policy[i][j] + ")");
            }
        }
        System.out.println("Number of iterations: " + numberOfIterations);
        System.out.println("===========================================\n");
    }

    /**
     * Used to export the utilities.
     * @param maze The maze created by the application.
     * @param recordOfUtilities The record of utilities obtained from the application.
     * @param directoryAdder The directory that can help point to the correct location to be stored.
     */
    public static void exportUtilites(Maze maze, HashMap<Integer, Double[][]> recordOfUtilities, String directoryAdder) {
        String directory = "utilities/" + directoryAdder;
        try {
            File dir = new File(directory);
            dir.mkdirs();
            for(File file: dir.listFiles())
                if (!file.isDirectory())
                    file.delete();
        } catch(Exception e) {
            System.out.println("An exception occurs while exporting the file.");
        }
        Set<Integer> iterations = recordOfUtilities.keySet();
        for (Integer iteration: iterations) {
            Double[][] utilities = recordOfUtilities.get(iteration);
            for (int j = 0; j < maze.getNumberOfColumns(); j++) {
                for (int i = 0; i < maze.getNumberOfRows(); i++) {
                    if (maze.isWall(i , j))
                        continue;
                    export(iteration, utilities[i][j], i, j, directoryAdder);
                }
            }
        }

    }

    /**
     * Export the utilites in a .csv file format
     * @param iteration The iteration where a particular utility is obtained.
     * @param utility The utility obtained at a particular iteration.
     * @param row The row of the maze that corresponds to the utility value.
     * @param column The column of the maze that corresponds to the utility value.
     * @param directoryAdder The directory that can help point to the correct location to be stored.
     */
    private static void export(Integer iteration, Double utility, int row, int column, String directoryAdder) {
        String filePathString = "utilities/" + directoryAdder + row + "_" + column + ".csv";
        try {
            File file = new File(filePathString);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter;
            if (iteration == 0) {
                fileWriter = new FileWriter(file, false);
            } else {
                fileWriter = new FileWriter(file, true);
            }
            fileWriter.append(String.valueOf(iteration));
            fileWriter.append(",");
            fileWriter.append(String.valueOf(utility));
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException e) {
            System.out.println("An IO exception occurs while exporting the file.");
        } catch(Exception e) {
            System.out.println("An exception occurs while exporting the file.");
        }
    }


    /**
     * Used to export all the utilities to one file.
     * @param maze The maze created by the application.
     * @param recordOfUtilities The record of utilities obtained from the application.
     * @param directoryAdder The directory that can help point to the correct location to be stored.
     */
    public static void exportAllUtilitesToOneFile(Maze maze, HashMap<Integer, Double[][]> recordOfUtilities, String directoryAdder) {
        String directory = "utilities/" + directoryAdder;
        try {
            File dir = new File(directory);
            dir.mkdirs();
        } catch(Exception e) {
            System.out.println("An exception occurs while exporting the file.");
        }
        int rows = maze.getNumberOfRows();
        int columns = maze.getNumberOfColumns();
        boolean start = true;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (maze.isWall(i, j))
                    continue;
                ArrayList<Double> utilities = new ArrayList<Double>();
                Set<Integer> iterations = recordOfUtilities.keySet();
                for (Integer iteration: iterations) {
                    utilities.add(recordOfUtilities.get(iteration)[i][j]);
                }
                exportUtilitiesToOneFile(start, utilities , directoryAdder);
                start = false;
            }
        }

    }

    /**
     * Export the utilities to the file pointed by the directory adder.
     * @param start If start is true, a new file will be created. If not the utilities will be appended.
     * @param utilities The utilities to be added.
     * @param directoryAdder The directory that can help point to the correct location to be stored.
     */
    private static void exportUtilitiesToOneFile(boolean start, ArrayList<Double> utilities, String directoryAdder) {
        String filePathString = "utilities/" + directoryAdder + "all_the_utilities.csv";
        try {
            File file = new File(filePathString);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter;
            if (start) {
                fileWriter = new FileWriter(file, false);
            } else {
                fileWriter = new FileWriter(file, true);
            }
            String resultString = "";
            for (Double utility: utilities) {
                resultString += String.valueOf(utility);
                resultString += ",";
            }
            resultString = resultString.substring(0, resultString.length() - 1);
            resultString += "\n";
            fileWriter.append(resultString);
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException e) {
            System.out.println("An IO exception occurs while exporting the file.");
        } catch(Exception e) {
            System.out.println("An exception occurs while exporting the file.");
        }
    }


}
