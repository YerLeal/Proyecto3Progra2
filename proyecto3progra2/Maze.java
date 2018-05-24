package proyecto3progra2;

import domain.Block;
import file.MazeFile;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yerlin Leal
 */
public class Maze {

    private MazeFile file;
    private Block[][] maze;
    private Block[][] m;
    private int size;

    public Maze() {
        try {
            file = new MazeFile();
            m = file.getMaze(0);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Block[][] getMaze(int difficulty) {
        if (difficulty == 1) {
            this.maze = new Block[11][6];
            this.size = 120;
//            for (int i = 0; i < m.length; i++) {
//                for (int j = 0; j < m[0].length; j++) {
//                    System.out.print(m[i][j].getType() + "  ");
//                }
//                System.out.println();
//            }
            System.out.println(m[0].length);
            maze= new Block[11][6];
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
                    if(i==0){
                        maze[i][j] = new Block(i, j, size, "wall");
                    }else{
                        maze[i][j] = new Block(i, j, size, "floor");
                    }
                }
            }
        }
        
        return m;
    }
    
}