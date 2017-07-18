package Objects;

import java.util.Random;

/**
 * Created by Adam Hayes on 7/13/2017 for project Gridworld.
 */
public class AI {
    private Map map;
    private int mapRow, mapCol;
    private int row, col;
    private String action;
    private double actionValue;
    private double actionValuePrime;
    private int rowPrime, colPrime;
    private int rowPrimePrime, colPrimePrime;
    private String actionPrime;

    public AI() {}

    public AI(Map map) {
        //AI with the size of the gridmap it is on
        this.map = map;
        mapRow = map.getRow();
        mapCol = map.getCol();
    }

    public double getActionValue() { return actionValue; }

    public double getActionValuePrime() { return actionValuePrime; }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public int getRowPrime() { return rowPrime; }

    public int getColPrime() { return colPrime; }

    public String getAction() { return action; }

    public String getActionPrime() { return actionPrime; }

    public void initialPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void initialAction(double epsilon) {
        Random rand = new Random(System.currentTimeMillis());
        int chance = rand.nextInt(100);
        if (chance <= (100 - epsilon)) {
            action = exploit(false);
        } else if (chance > (100 - epsilon)) {
            action = explore(false);
        }

    }

    public void nextAction(double epsilon) {
        Random rand = new Random(System.currentTimeMillis());
        int chance = rand.nextInt(100);
        //System.out.println("Chance: " + chance);
        if (chance <= (100 - epsilon)) {
            actionPrime = exploit(true);
            //System.out.println("Picked Exploit");
        } else if (chance > (100 - epsilon)) {
            actionPrime = explore(true);
            //System.out.println("Picked Explore");
        }
        /*if (actionPrime.equalsIgnoreCase("OOB")) {
            nextAction(epsilon);
        }*/
    }

    public void setMove() {
        //System.out.println("Row: " + row);
        //System.out.println("Col: " + col);
        //System.out.println("Row Prime: " + rowPrime);
        //System.out.println("Col Prime: " + colPrime);
        //System.out.println("Row Prime 2: " + rowPrimePrime);
        //System.out.println("Col Prime 2: " + colPrimePrime);
        row = rowPrime;
        col = colPrime;
        action = actionPrime;
        colPrime = colPrimePrime;
        rowPrime = rowPrimePrime;
    }

    private String explore(boolean actionPrime) {
        String explore;
        Random rand = new Random(System.currentTimeMillis());
        int d4 = rand.nextInt(3);
        double[] values;
        if (!actionPrime) {
            values = map.getIndexValues(row, col);
        } else {
            values = map.getIndexValues(rowPrime, colPrime);
        }
        switch (d4) {
            case 0:
                explore = "left";
                if (!actionPrime) {
                    if ((col + 1) < 0 || (col + 1) > 19) {
                        explore = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[0];
                        rowPrime = row;
                        colPrime = col + 1;
                    }
                } else {
                    if ((colPrime + 1) < 0 || (colPrime + 1) > 19) {
                        explore = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[0];
                        rowPrimePrime = rowPrime;
                        colPrimePrime = colPrime + 1;
                    }
                }
                break;
            case 1:
                explore = "right";
                if (!actionPrime) {
                    if ((col - 1) < 0 || (col - 1) > 19) {
                        explore = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[1];
                        rowPrime = row;
                        colPrime = col - 1;
                    }
                } else {
                    if ((colPrime - 1) < 0 || (colPrime - 1) > 19) {
                        explore = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[1];
                        rowPrimePrime = rowPrime;
                        colPrimePrime = colPrime - 1;
                    }
                }
                break;
            case 2:
                explore = "up";
                if (!actionPrime) {
                    if ((row + 1) < 0 || (row + 1) > 19) {
                        explore = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[2];
                        rowPrime = row + 1;
                        colPrime = col;
                    }
                } else {
                    if ((rowPrime + 1) < 0 || (rowPrime + 1) > 19) {
                        explore = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[2];
                        rowPrimePrime = rowPrime + 1;
                        colPrimePrime = colPrime;
                    }
                }
                break;
            case 3:
                explore = "down";
                if (!actionPrime) {
                    if ((row - 1) < 0 || (row - 1) > 19) {
                        explore = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[3];
                        rowPrime = row - 1;
                        colPrime = col;
                    }
                } else {
                    if ((rowPrime - 1) < 0 || (rowPrime - 1) > 19) {
                        explore = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[3];
                        rowPrimePrime = rowPrime - 1;
                        colPrimePrime = colPrime;
                    }
                }
                break;
            default:
                explore = "left";
                if (!actionPrime) {
                    if ((col + 1) < 0 || (col + 1) > 19) {
                        explore = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[0];
                        rowPrime = row;
                        colPrime = col + 1;
                    }
                } else {
                    if ((colPrime + 1) < 0 || (colPrime + 1) > 19) {
                        explore = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[0];
                        rowPrimePrime = rowPrime;
                        colPrimePrime = colPrime + 1;
                    }
                }
        }
        //System.out.println("Explore Value: " + explore);
        return explore;
    }

    private String exploit(boolean actionPrime) {
        String exploit;
        double[] values;
        if (!actionPrime) {
            values = map.getIndexValues(row, col);
        } else {
            values = map.getIndexValues(rowPrime, colPrime);
        }
        double max = -10;
        int maxValueIndex = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
                maxValueIndex = i;
            }
        }
        switch (maxValueIndex) {
            case 0:
                exploit = "left";
                if (!actionPrime) {
                    if ((col + 1) < 0 || (col + 1) > 19) {
                        exploit = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[0];
                        rowPrime = row;
                        colPrime = col + 1;
                    }
                } else {
                    if ((colPrime + 1) < 0 || (colPrime + 1) > 19) {
                        exploit = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[0];
                        rowPrimePrime = rowPrime;
                        colPrimePrime = colPrime + 1;
                    }
                }
                break;
            case 1:
                exploit = "right";
                if (!actionPrime) {
                    if ((col - 1) < 0 || (col - 1) > 19) {
                        exploit = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[1];
                        rowPrime = row;
                        colPrime = col - 1;
                    }
                } else {
                    if ((colPrime - 1) < 0 || (colPrime - 1) > 19) {
                        exploit = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[1];
                        rowPrimePrime = rowPrime;
                        colPrimePrime = colPrime - 1;
                    }
                }
                break;
            case 2:
                exploit = "up";
                if (!actionPrime) {
                    if ((row + 1) < 0 || (row + 1) > 19) {
                        exploit = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[2];
                        rowPrime = row + 1;
                        colPrime = col;
                    }
                } else {
                    if ((rowPrime + 1) < 0 || (rowPrime + 1) > 19) {
                        exploit = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[2];
                        rowPrimePrime = rowPrime + 1;
                        colPrimePrime = colPrime;
                    }
                }
                break;
            case 3:
                exploit = "down";
                if (!actionPrime) {
                    if ((row - 1) < 0 || (row - 1) > 19) {
                        exploit = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[3];
                        rowPrime = row - 1;
                        colPrime = col;
                    }
                } else {
                    if ((rowPrime - 1) < 0 || (rowPrime - 1) > 19) {
                        exploit = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[3];
                        rowPrimePrime = rowPrime - 1;
                        colPrimePrime = colPrime;
                    }
                }
                break;
            default:
                exploit = "left";
                if (!actionPrime) {
                    if ((col + 1) < 0 || (col + 1) > 19) {
                        exploit = "OOB";
                        actionValue = -1;
                        //System.out.println("OOB Move");
                    } else {
                        actionValue = values[0];
                        rowPrime = row;
                        colPrime = col + 1;
                    }
                } else {
                    if ((colPrime + 1) < 0 || (colPrime + 1) > 19) {
                        exploit = "OOB";
                        actionValuePrime = -1;
                        //System.out.println("OOB move");
                    } else {
                        actionValuePrime = values[0];
                        rowPrimePrime = rowPrime;
                        colPrimePrime = colPrime + 1;
                    }
                }
        }
        return exploit;
    }
}
