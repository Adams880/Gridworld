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
    double lambda = .1;
    double epsilon = 99.95;
    double epsilonDecay = .001;
    double gamma = .95;
    double delta;
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
        double min = .00001;
        double max = .0003;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                for (int k = 0; k < z; k++) {
                    double weight = (rand.nextDouble() * ((max - min) + min));
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

        System.out.println("Starting coordinates: " + rowCoord + "," + colCoord);
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

        gui.newEpisode(row, col);
        ai.initialPos(rowCoord, colCoord);
        gui.setInitAIPos(rowCoord, colCoord);
        ai.initialAction(epsilon);
    }

    private void findDelta() {
        int rowPrime, colPrime;
        int reward;
        double actionValue, actionValuePrime;
        actionValue = ai.getActionValue();
        actionValuePrime = ai.getActionValuePrime();
        rowPrime = ai.getRowPrime();
        colPrime = ai.getColPrime();

        if (rowPrime == goalRow && colPrime == goalCol) {
            reward = 1;
        } else {
            reward = 0;
        }

        delta = ( (reward) + (gamma * ((actionValuePrime) - (actionValue)) ) );
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
            mapFinished = true;
        }
        gui.changeAIPos(rowPrime, colPrime);
        ai.setMove();
        return mapFinished;
    }
}
