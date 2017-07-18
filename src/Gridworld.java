import GUI.GridworldGUI;
import Objects.*;

import java.util.concurrent.TimeUnit;
import java.util.Scanner;

/**
 * Created by Adam Hayes on 7/12/2017 for project Gridworld.
 */
public class Gridworld {
    public static void main(String[] args) {
        GridworldGUI gui = new GridworldGUI(20, 20, 4);
        /*try{
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            System.out.println(e);
        }
        gui.setInitAIPos(10, 10);
        gui.addWall(5,10, 1);
        gui.addWall(5,11, 2);
        gui.addWall(6, 10, 3);
        gui.addWall(6,11,4);
        gui.setWalls();*/
        gui.setGoalSpace(15, 7);

        Map map = new Map(20, 20, 4, gui);
        AI ai = new AI(map);
        map.setAi(ai);

        map.newMap();

        int numOfGen;

        numOfGen = gui.numOfGens();

        boolean mapFinished;
        int mapCounter = 0;
        int moveCounter = 0;
        do {
            do {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (Exception e) {
                    System.out.println(e);
                }
                mapFinished = map.makeMove();
                moveCounter++;
                //System.out.println(moveCounter);
            } while (!mapFinished);
            mapCounter++;
            System.out.println("Previous Map took " + moveCounter + " moves!");
            System.out.println("Loading in Map #" + mapCounter);
            moveCounter = 0;
            map.newEpisodeMap();
        } while (mapCounter < numOfGen);
    }
}
