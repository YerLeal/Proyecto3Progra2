package business;

import domain.Block;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Logica {

    private final int WIDTH = 1360;
    private final int HEIGHT = 720;
    private int size;
    int difficulty;
    private Block maze[][];

    public Logica() {
        this.difficulty = 1;
        getDificultad();
        this.maze = new Block[WIDTH / size][HEIGHT / size];
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public Block[][] getMaze() {
        return this.maze;
    }

    public Block ini() {
        return this.maze[0][0];
    }

    public Block ini3() {
        return this.maze[4][1];
    }

    public void cambiarTipo(int x, int y, GraphicsContext gc) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].isClicked(x, y)) {
                    System.out.println("i:" + i + ", j:"+j);
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
        Maze m = new Maze();
        this.maze = m.getMaze(difficulty, size);
        buscarNuevosCaminos();
    } // createMaze

    private void getDificultad() {
        switch (this.difficulty) {
            case 1:
                size = 80;
                break;
            case 2:
                size = 40;
                break;
            case 3:
                size = 40;
                break;
        }
    } // getDificultad

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

} // end class
