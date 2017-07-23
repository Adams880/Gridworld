package Objects;

import GUI.GridworldGUI;

import java.util.Random;

/**
 * Created by Adam Hayes on 7/13/2017 for project Gridworld.
 */
public class Map {
    //The maps will have a z of 4 for the different movement options
    //Z = 0, Left
    //Z = 1, Right
    //Z = 2, Up
    //Z = 3, Down
    double[][][] qMap;
    double[][][] eMap;
    int row, col, z;
    int goalRow = 15, goalCol = 7;
    double alpha = .1;
    double lambda = .2;
    double epsilon = 95.0;
    double epsilonDecay = .02;
    double gamma = .98;
    double delta;
    int wallCounter = 0;
    int goalCounter = 0;
    AI ai;
    GridworldGUI gui;

    public Map(int row, int col, int z, GridworldGUI gui) {
        this.row = row;
        this.col = col;
        this.z = z;
        this.gui = gui;
        qMap = new double[row][col][z];
        eMap = new double[row][col][z];
    }

    public Map(Map map) {
        qMap = map.getQMap();
        eMap = map.getEMap();
    }

    public int getWallCounter() { return wallCounter; }

    public int getGoalCounter() { return goalCounter; }

    public void resetWallCounter() { wallCounter = 0; }

    public void resetGoalCounter() { goalCounter = 0; }

    public double[][][] getQMap() { return qMap; }

    public double[][][] getEMap() { return eMap; }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public void setAi(AI ai) {
        this.ai = ai;
    }

    public double[] getIndexValues(int row, int col) {
        double[] values = new double[z];
        for (int i = 0; i < z; i++) {
            values[i] = qMap[row][col][i];
        }
        return values;
    }

    public void newMap() {
        //TODO: Generate randomly small values for Q on a brand new map
        //DO NOT USE for maps between episodes!!!
        Random rand = new Random(System.currentTimeMillis());
        double min = .0001;
        double max = .0002;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                for (int k = 0; k < z; k++) {
                    double weight = ((rand.nextDouble() * (max - min)) + min);
                    System.out.println("Weight: " + weight);
                    qMap[i][j][k] = weight;
                    eMap[i][j][k] = 0;
                }
            }
        }

        int rowCoord;
        int colCoord;
        do {
            rowCoord = rand.nextInt(19);
            colCoord = rand.nextInt(19);
        } while (rowCoord == goalRow && colCoord == goalCol);

        //System.out.println("Starting coordinates: " + rowCoord + "," + colCoord);
        ai.initialPos(rowCoord, colCoord);
        ai.initialAction(epsilon);
    }

    public void newEpisodeMap() {
        //USE FOR NEW EPISODE MAPS - DOES NOT GENERATE NEW INITIAL MAPS

        //sets all values of eMap back to 0
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                for (int k = 0; k < z; k++) {
                    eMap[i][j][k] = 0;
                }
            }
        }

        //drops AI in at a random position on the map
        //cannot drop on walls or goal space
        Random rand = new Random(System.currentTimeMillis());
        int rowCoord;
        int colCoord;
        do {
            rowCoord = rand.nextInt(19);
            colCoord = rand.nextInt(19);
        } while (rowCoord == goalRow && colCoord == goalCol);

        double maxValue;
        int maxIndex = 0;
        maxValue = -10;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                for (int k = 0; k < z; k++) {
                    if (qMap[i][j][k] > maxValue) {
                        maxValue = qMap[i][j][k];
                        maxIndex = k;
                    }
                }
                gui.setArrow(i, j, maxIndex);
                maxValue = -10;
                maxIndex = 0;
            }
        }

        System.out.println("Times Explored last map: " + ai.getExploreCounter());
        System.out.println("Times Exploited last map: " + ai.getExploitCounter());

        ai.resetExploitCounter();
        ai.resetExploreCounter();

        gui.newEpisode(row, col);
        ai.initialPos(rowCoord, colCoord);
        gui.setInitAIPos(rowCoord, colCoord);
        ai.initialAction(epsilon);
    }

    private void findDelta() {
        int rowPrime, colPrime;
        int reward;
        double actionValue, actionValuePrime;
        String actionPrime, action;
        action = ai.getAction();
        actionPrime = ai.getActionPrime();
        actionValue = ai.getActionValue();
        actionValuePrime = ai.getActionValuePrime();
        rowPrime = ai.getRowPrime();
        colPrime = ai.getColPrime();

        if (rowPrime == goalRow && colPrime == goalCol) {
            reward = 1;
        } else if (action.equalsIgnoreCase("OOB")) {
            reward = -1;
        } else {
            reward = 0;
        }

        delta = ( (reward) + (gamma * ((actionValuePrime) - (actionValue)) ) );
        //System.out.println("Delta = " + delta);
    }

    private void updateEMapMove() {
        int aiRow, aiCol;
        String action;
        aiRow = ai.getRow();
        aiCol = ai.getCol();
        action = ai.getAction();

        switch (action) {
            case "left":
                eMap[aiRow][aiCol][0] = 1;
                break;
            case "right":
                eMap[aiRow][aiCol][1] = 1;
                break;
            case "up":
                eMap[aiRow][aiCol][2] = 1;
                break;
            case "down":
                eMap[aiRow][aiCol][3] = 1;
                break;
            case "OOB":
                break;
        }
    }

    private void updateAllStates() {
        for (int i = 0; i < row; i++)  {
            for (int j = 0; j < col; j++) {
                for (int k = 0; k < z; k++) {
                    double qTemp = qMap[i][j][k];
                    double eTemp = eMap[i][j][k];
                    qMap[i][j][k] = (qTemp + (alpha * delta * eTemp));
                    eMap[i][j][k] = (gamma * lambda * eTemp);
                }
            }
        }
    }

    public boolean makeMove() {
        /*TODO: Make a move to a new state in the gridworld
            ADD: Change GUI color to new location
            ADD: Add heatmap value for moves
            ADD: Set new values for S and S'  (Maybe handle in AI)
            ADD: Set new values for A and A'  (Maybe handle in AI)
         */

        boolean mapFinished = false;
        ai.nextAction(epsilon);
        findDelta();
        updateEMapMove();
        updateAllStates();
        int rowPrime, colPrime;
        rowPrime = ai.getRowPrime();
        colPrime = ai.getColPrime();
        if (rowPrime == goalRow && colPrime == goalCol) {
            goalCounter++;
            double[] temp;
            System.out.println("Direction taken to goal: " + ai.getAction());
            System.out.println("Delta at Goal: " + delta);
            temp = this.getIndexValues(goalRow, goalCol - 1);
            System.out.println("Values of Goal Space above goal: \nLeft Q value: " + temp[0] + "\nRight Q value: " + temp[1]
             + "\nUp Q value: " + temp[2] + "\nDown Q value: " + temp[3]);
            temp = this.getIndexValues(goalRow - 1, goalCol);
            System.out.println("Values of Goal Space left of goal: \nLeft Q value: " + temp[0] + "\nRight Q value: " + temp[1]
                    + "\nUp Q value: " + temp[2] + "\nDown Q value: " + temp[3]);
            temp = this.getIndexValues(goalRow, goalCol + 1);
            System.out.println("Values of Goal Space below goal: \nLeft Q value: " + temp[0] + "\nRight Q value: " + temp[1]
                    + "\nUp Q value: " + temp[2] + "\nDown Q value: " + temp[3]);
            temp = this.getIndexValues(goalRow + 1, goalCol);
            System.out.println("Values of Goal Space right of goal: \nLeft Q value: " + temp[0] + "\nRight Q value: " + temp[1]
                    + "\nUp Q value: " + temp[2] + "\nDown Q value: " + temp[3]);

            mapFinished = true;
            if (!(epsilon <= 5)) {
                epsilon = epsilon - epsilonDecay;
            }
        }
        String action;
        action = ai.getAction();
        if (action.equalsIgnoreCase("OOB")) {
            wallCounter++;
            mapFinished = true;

            //System.out.println("Direction outside of Wall: " + action + "\n");
            if (!(epsilon <= 5)) {
                epsilon = epsilon - epsilonDecay;
            }
        }
        gui.changeAIPos(rowPrime, colPrime);
        ai.setMove();
        return mapFinished;
    }
}
