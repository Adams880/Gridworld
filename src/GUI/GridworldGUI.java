package GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Adam Hayes on 7/12/2017 for project Gridworld.
 */
public class GridworldGUI {
    JFrame frame = new JFrame("Gridworld");
    JLabel[][] grid;
    JLabel[] statPanels;
    JPanel gridworld;
    JPanel stats;
    JPanel mainPanel;
    JPanel slider;
    int speedMinMilli = 0;
    int speedMaxMilli = 100;
    int speedInitMilli = 20;
    int currentSpeed = speedInitMilli;
    int completedRuns;
    int numOfMoves;
    int goalsFound;
    int goalRow;
    int goalCol;
    int aiRow;
    int aiCol;
    int[] wallRows;
    int[] wallCols;
    Color aiColor = new Color(0,120,255);
    Color goalColor = new Color(253,102,0);
    Color wallColor = Color.darkGray;
    ImageIcon left = new ImageIcon("LeftArrow.png");
    ImageIcon leftMed = left;
    ImageIcon leftSmall = left;
    ImageIcon right = new ImageIcon("RightArrow.png");
    ImageIcon rightMed = right;
    ImageIcon rightSmall = right;
    ImageIcon up = new ImageIcon("UpArrow.png");
    ImageIcon upMed = up;
    ImageIcon upSmall = up;
    ImageIcon down = new ImageIcon("DownArrow.png");
    ImageIcon downMed = down;
    ImageIcon downSmall = down;

    public GridworldGUI(int row, int col, int numOfWalls) {
        //most of this is not in use in the final product, I tried to make a gui that would keep stats on the window and I was going
        //to add a slider and the whole shebang. Couldn't get the grid window to be the proper size for the life of me,
        //so I scrapped it in saving of time
        wallRows = new int[numOfWalls];
        wallCols = new int[numOfWalls];
        gridworld = new JPanel();

        Image temp = left.getImage();
        Image newTemp = temp.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        Image newTempMed = temp.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image newTempSmall = temp.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        left = new ImageIcon(newTemp);
        leftMed = new ImageIcon(newTempMed);
        leftSmall = new ImageIcon(newTempSmall);

        temp = right.getImage();
        newTemp = temp.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        newTempMed = temp.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        newTempSmall = temp.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        right = new ImageIcon(newTemp);
        rightMed = new ImageIcon(newTempMed);
        rightSmall = new ImageIcon(newTempSmall);

        temp = up.getImage();
        newTemp = temp.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        newTempMed = temp.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        newTempSmall = temp.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        up = new ImageIcon(newTemp);
        upMed = new ImageIcon(newTempMed);
        upSmall = new ImageIcon(newTempSmall);

        temp = down.getImage();
        newTemp = temp.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        newTempMed = temp.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        newTempSmall = temp.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        down = new ImageIcon(newTemp);
        downMed = new ImageIcon(newTempMed);
        downSmall = new ImageIcon(newTempSmall);

        statPanels = new JLabel[3];
        statPanels[0] = new JLabel("Completed Runs: "  + completedRuns, SwingConstants.LEFT);
        statPanels[1] = new JLabel("Num of Moves: " + numOfMoves, SwingConstants.CENTER);
        statPanels[2] = new JLabel("Total Num of Goals Found: " + goalsFound, SwingConstants.RIGHT);
        stats = new JPanel(new GridLayout(0, 3));
        stats.add(statPanels[0], BorderLayout.PAGE_START);
        stats.add(statPanels[1], BorderLayout.PAGE_START);
        stats.add(statPanels[2], BorderLayout.PAGE_START);

        JSlider speedSlide = new JSlider(JSlider.HORIZONTAL, speedMinMilli, speedMaxMilli, speedInitMilli);
        speedSlide.addChangeListener(this::stateChanged);
        speedSlide.setMajorTickSpacing(10);
        speedSlide.setMinorTickSpacing(5);
        speedSlide.setPaintTicks(true);
        slider = new JPanel(new GridLayout(0, 2));
        JLabel sliderTitle = new JLabel("Speed Slider (lower is faster)", SwingConstants.RIGHT);
        slider.add(sliderTitle);
        slider.add(speedSlide, BorderLayout.CENTER);

        //JScrollPane gridworldScroll = new JScrollPane(gridworld, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gridworld.setLayout(new GridLayout(row, col));
        gridworld.setBounds(300, 300, 800, 800);


        grid = new JLabel[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = new JLabel();
                grid[i][j].setBorder(new LineBorder(Color.BLACK));
                grid[i][j].setOpaque(true);
                grid[i][j].setBackground(Color.lightGray);
                gridworld.add(grid[i][j]);
            }
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(stats);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(slider);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(gridworld);

        //frame.getContentPane().add(mainPanel);
        frame.setBounds(400, 400, 1000, 1050);
        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public int getCurrentSpeed() { return currentSpeed; }

    public void setColor(int row, int col, Color color) {
        grid[row][col].setBackground(color);
    }

    public void stateChanged(ChangeEvent e) {
        //Change Listener for the speed Slider
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
            currentSpeed = source.getValue();
        }
    }

    public void addMove() {
        //updates the stats at the top of the screen
        numOfMoves++;
        statPanels[1].setText("Num of Moves: " + numOfMoves);
    }

    public void resetMoves() { numOfMoves = 0; }

    public void addCompletion() {
        completedRuns++;
        statPanels[0].setText("Completed Runs: "  + completedRuns);
    }

    public void addGoalFound() {
        goalsFound++;
        statPanels[2].setText("Total Num of Goals Found: " + goalsFound);
    }

    public void setArrow(int row, int col, int direction, int size) {
        //sets all the directional arrows on the gridworld
        //for size, 0 is small, 1 is med, 2 is large
        switch (direction) {
            case 0:
                if (size == 2) {
                    grid[row][col].setIcon(left);
                } else if (size == 1) {
                    grid[row][col].setIcon(leftMed);
                } else {
                    grid[row][col].setIcon(leftSmall);
                }
                break;
            case 1:
                if (size == 2) {
                    grid[row][col].setIcon(right);
                } else if (size == 1) {
                    grid[row][col].setIcon(rightMed);
                } else {
                    grid[row][col].setIcon(rightSmall);
                }
                break;
            case 2:
                if (size == 2) {
                    grid[row][col].setIcon(up);
                } else if (size == 1) {
                    grid[row][col].setIcon(upMed);
                } else {
                    grid[row][col].setIcon(upSmall);
                }
                break;
            case 3:
                if (size == 2) {
                    grid[row][col].setIcon(down);
                } else if (size == 1) {
                    grid[row][col].setIcon(downMed);
                } else {
                    grid[row][col].setIcon(downSmall);
                }
                break;
        }

    }

    public void setInitAIPos(int aiRow, int aiCol) {
        this.aiCol = aiCol;
        this.aiRow = aiRow;
        grid[aiRow][aiCol].setBackground(aiColor);
    }

    public void setGoalSpace(int goalRow, int goalCol) {
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        grid[goalRow][goalCol].setBackground(goalColor);
    }

    public void changeAIPos (int row, int col) {
        grid[aiRow][aiCol].setBackground(Color.WHITE);
        grid[row][col].setBackground(aiColor);
        aiRow = row;
        aiCol = col;
    }

    public void addWall(int row, int col, int wallNum) {
        //adds a wall to the array of walls
        wallRows[(wallNum - 1)] = row;
        wallCols[(wallNum - 1)] = col;
    }

    public void setWalls() {
        //sets the color of the walls on the map
        for (int i = 0; i < wallRows.length; i++) {
            int row = wallRows[i];
            int col = wallCols[i];
            System.out.println("Row: " + row + " Column: " + col);
            grid[wallRows[i]][wallCols[i]].setBackground(wallColor);
        }
    }

    public void newEpisode(int row, int col) {
        //updates the map for each episode
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j].setBackground(Color.lightGray);
            }
        }
        setGoalSpace(goalRow, goalCol);
    }

    public int numOfGens() {
        //pops up the number of generations dialog box
        String strNumOfGenerations;
        int numOfGenerations;
        strNumOfGenerations = JOptionPane.showInputDialog("Number of Generations to Run \n(Positive Whole Number)");

        numOfGenerations = Integer.parseInt(strNumOfGenerations);

        return numOfGenerations;
    }

    public void showFinished() {
        DecimalFormat df = new DecimalFormat("##.#");
        double accuracy = (goalsFound);
        accuracy = accuracy / completedRuns;
        accuracy = accuracy * 100;
        JOptionPane.showMessageDialog(frame, "      Finished\nPercent of Runs with Goals found: " + df.format(accuracy) + "%");
    }
}
