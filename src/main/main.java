package main;

/**
 * Created by jodiakyulas on 6/3/18.
 */
public class main {
    public static void main(String args[]) {
        Part1 part1 = new Part1();
        part1.start();
        Part2 part2 = new Part2();
        part2.start(8, 8);
        part2.start(10, 10);
        part2.start(12, 12);
    }
}
