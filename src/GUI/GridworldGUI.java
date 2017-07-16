package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by Adam Hayes on 7/12/2017 for project Gridworld.
 */
public class GridworldGUI {
    JFrame frame = new JFrame("Gridworld");
    JLabel[][] grid;
    JLabel[] statPanels;
    JPanel gridworld;
    JPanel title;
    JPanel stats;
    JPanel mainPanel;
    int completedRuns;
    int numOfMoves;
    int goalRow;
    int goalCol;
    int aiRow;
    int aiCol;
    int[] wallRows;
    int[] wallCols;
    Color aiColor = new Color(0,120,255);
    Color goalColor = new Color(253,102,0);
    Color wallColor = Color.darkGray;

    public GridworldGUI(int row, int col, int numOfWalls) {
        wallRows = new int[numOfWalls];
        wallCols = new int[numOfWalls];
        gridworld = new JPanel();

        title = new JPanel(new BorderLayout());
        title.setBounds(10, 10, 100, 100);
        title.add(new JLabel("Gridworld", SwingConstants.CENTER));

        statPanels = new JLabel[3];
        statPanels[0] = new JLabel("Completed Runs: "  + completedRuns, SwingConstants.LEFT);
        statPanels[1] = new JLabel("Num of Moves: " + numOfMoves, SwingConstants.CENTER);
        statPanels[2] = new JLabel("Placeholder", SwingConstants.RIGHT);
        stats = new JPanel(new GridLayout(0, 3));
        stats.add(statPanels[0], BorderLayout.PAGE_START);
        stats.add(statPanels[1], BorderLayout.PAGE_START);
        stats.add(statPanels[2], BorderLayout.PAGE_START);

        //JScrollPane gridworldScroll = new JScrollPane(gridworld, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gridworld.setLayout(new GridLayout(row, col));
        gridworld.setBounds(400, 400, 1000, 1000);
        frame.setBounds(400, 400, 1000, 1000);


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
        //mainPanel.setLayout();
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(stats);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(gridworld);

        //frame.getContentPane().add(mainPanel);
        frame.getContentPane().add(gridworld);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setColor(int row, int col, Color color) {
        grid[row][col].setBackground(color);
        /*frame.invalidate();
        frame.revalidate();
        frame.repaint();*/
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
        wallRows[(wallNum - 1)] = row;
        wallCols[(wallNum - 1)] = col;
    }

    public void setWalls() {
        for (int i = 0; i < wallRows.length; i++) {
            int row = wallRows[i];
            int col = wallCols[i];
            System.out.println("Row: " + row + " Column: " + col);
            grid[wallRows[i]][wallCols[i]].setBackground(wallColor);
        }
    }

    public void newEpisode(int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j].setBackground(Color.lightGray);
            }
        }
        setGoalSpace(goalRow, goalCol);
    }
}
