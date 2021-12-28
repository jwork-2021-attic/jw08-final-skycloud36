package com.example.maze;

// import java.util.Random;
import java.util.Arrays;

public class MazeGenerator {
    
    // private Random rand = new Random();
    private int[][] maze;
    private int row, col;

    public MazeGenerator(int row, int col) {
        maze = new int[col][row];
        this.row = col;
        this.col = row;
    }

    public void generateMaze() {
        for(int i = 1; i < row - 1 ; i++){
            for(int j = 1; j < col - 1; j++){
                maze[i][j] = 1;
            }
        }
    }

    public int PassByLoc(int x, int y){
        return maze[y][x];
    }

    public void setByLoc(int x, int y, int data){
        maze[y][x] = data;
    }

    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                sb.append(maze[i][j] == 1 ? "*" : " ");
                sb.append("  "); 
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // private Boolean pointOnGrid(int x, int y) {
    //     return x >= 1 && y >= 1 && x < row - 1 && y < col - 1;
    // }

    // private Boolean pointOnBound(int x, int y){
    //     return x == 0 || x == this.row - 1 || y == 0 || y == this.col - 1;
    // }

    // private Boolean pointOnMaze(int x, int y){
    //     return x >= 0 && y >= 0 && x <= row - 1 && y <= col - 1;
    // }

}