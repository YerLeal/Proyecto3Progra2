package business;

import domain.Block;
import file.MazeFile;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Logica {

    private final int WIDTH = 1360;
    private final int HEIGHT = 720;
    private int size;
    int difficulty;
    private Block maze[][];
    
    public Logica() {
        this.difficulty = (int) (Math.random() * (4 - 1) + 1);
//        this.difficulty = 3;
        getDificultad();
        this.maze = new Block[WIDTH / size][HEIGHT / size];
    }
    
    public int getDifficulty(){
        return this.difficulty;
    }
    
    public Block[][] getMaze(){
        return this.maze;
    }

    public Block ini() {
        return this.maze[0][0];
    }

    public Block ini3() {
        return this.maze[3][0];
    }

    public void cambiarTipo(int x, int y, GraphicsContext gc) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].isClicked(x, y)) {
                    if (this.maze[i][j].getType().equals("wall")) {
                        this.maze[i][j].setType("floor");
                    } else {
                        this.maze[i][j].setType("wall");
                    }

                    break;
                }
            }
        }
        drawMaze(gc);
        buscarNuevosCaminos();
    }

    public int getSize() {
        return this.size;
    }

    public void imprimirTipo(int x, int y) {
        for (int f = 0; f < maze.length; f++) {
            for (int c = 0; c < maze[0].length; c++) {
                if (maze[f][c].isClicked(x, y)) {
                    System.out.println(maze[f][c].getType());
                }
            }
        }
    }

    public void createMaze() {
//        for(int i=0; i<maze.length; i++){
//            for(int j=0; j<maze[0].length;j++){
//                if((i+j)%2==0){
//                    maze[i][j]= new Block(i, j, size, "wall");
//                }else{
//                    maze[i][j]= new Block(i, j, size, "floor");
//                }
//            }
//        }
        
        
        MazeFile file = new MazeFile();
        try {
            this.maze = file.getMaze(difficulty-1);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Logica.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscarNuevosCaminos();

//            Maze m = new Maze();
//            this.maze = m.getMaze(difficulty);
//            buscarNuevosCaminos();
    }

    private void getDificultad() {
        switch (this.difficulty) {
            case 1:
                size = 80;
                break;
            case 2:
                size = 80;
                break;
            case 3:
                size = 40;
                break;
            default:
                break;
        }
    }

    public void drawMaze(GraphicsContext gc) {

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].getType().equals("wall")) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i * size, j * size, size, size);

                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i * size, j * size, size, size);
                }

            }
        }

    }

    private ArrayList<Block> caminos(int x, int y) {
        ArrayList<Block> next = new ArrayList<>();
        if (x + 1 < maze.length && maze[x + 1][y].getType().equals("floor")) {

            next.add(maze[x + 1][y]);
        }
        if (x - 1 >= 0 && maze[x - 1][y].getType().equals("floor")) {

            next.add(maze[x - 1][y]);
        }
        if (y + 1 < maze[0].length && maze[x][y + 1].getType().equals("floor")) {

            next.add(maze[x][y + 1]);
        }
        if (y - 1 >= 0 && maze[x][y - 1].getType().equals("floor")) {

            next.add(maze[x][y - 1]);
        }

        return next;
    }

    private void buscarNuevosCaminos() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j].setNext(caminos(i, j));
            }
        }
    }
    
}
