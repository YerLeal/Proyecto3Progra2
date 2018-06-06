package business;

import domain.Block;
import domain.Item;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Logic {

    private final int WIDTH = 1360;
    private final int HEIGHT = 720;
    private int size;
    private int difficulty;
    private int itemCont;
    private Block maze[][];
    private ArrayList<Item> items = new ArrayList<>();
    private String name;

    public int getDifficulty() {
        return this.difficulty;
    } // getDifficulty

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    } // setDifficulty

    public Logic(int dificulty) {
        this.difficulty = dificulty;
        getDificultad();
        this.maze = new Block[WIDTH / size][HEIGHT / size];
    } // constructor

    public ArrayList<Block> getStart() {
        ArrayList<Block> starts = new ArrayList<>();
        switch (getDifficulty()) {
            case 1:
                starts.add(this.maze[0][4]);
                return starts;
            case 2:
                starts.add(this.maze[14][0]);
                return starts;
            default:
                starts.add(this.maze[0][9]);
                starts.add(this.maze[27][7]);
                return starts;
        }
    } // getStart

    public ArrayList<Block> getFinish() {
        ArrayList<Block> finish = new ArrayList<>();
        switch (getDifficulty()) {
            case 1:
                finish.add(this.maze[10][8]);
                return finish;
            case 2:
                finish.add(this.maze[14][17]);
                return finish;
            default:
                finish.add(this.maze[10][4]);
                finish.add(this.maze[24][11]);
                return finish;
        } // switch
    } // getFinish

    public Block[][] getMaze() {
        return this.maze;
    } // getMaze

    public void changeTypeBlock(int x, int y, GraphicsContext gc) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].isClicked(x, y)) {
                    if (this.maze[i][j].getType().equals("wall")) {
                        this.maze[i][j].setType("floor");
                    } else {
                        this.maze[i][j].setType("wall");
                    }
                    break;
                } // if
            } // for 
        } // for i
        drawMaze(gc);
        lookForNewWays();
    } // changeTypeBlock

    public void startItems() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).start();
        } // for
    } // startItems

    public void addItem(int x, int y, SharedBuffer buffer, boolean life, GraphicsContext gc) {
        Item itemAux = new Item(size, buffer, name);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].isClicked(x, y)) {
                    if (this.maze[i][j].getType().equals("floor")) {
                        itemAux.setOrder(itemCont);
                        itemAux.setStarto(maze[i][j]);
                        items.add(itemAux);
                        buffer.getItems().add(items.get(itemCont));
                        if (life) {
                            items.get(itemCont).start();
                        } else {
                            items.get(itemCont).draw(gc);
                        }
                        itemCont++;
                    } // if (this.maze[i][j].getType().equals("floor"))
                    break;
                } // if (this.maze[i][j].isClicked(x, y))
            } // for j
        } // for i
    } // addItem

    public int getSize() {
        return this.size;
    } // getSize

    public void createMaze() {
        Maze m = new Maze();
        this.maze = m.getMaze(getDifficulty(), size);
        lookForNewWays();
    } // createMaze

    private void getDificultad() {
        switch (this.getDifficulty()) {
            case 1:
                size = 80;
                break;
            case 2:
                size = 40;
                break;
            case 3:
                size = 40;
                break;
        } // switch
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
            } // for j
        } // for i
        int xS, yS, xA, yA;
        ArrayList<Block> finish = getFinish();
        gc.setFill(Color.GREEN);
        if (getDifficulty() < 3) {
            xS = finish.get(0).getX();
            yS = finish.get(0).getY();
            gc.fillRect(xS * size, yS * size, size, size);
        } else {
            xS = finish.get(0).getX();
            yS = finish.get(0).getY();
            xA = finish.get(1).getX();
            yA = finish.get(1).getY();
            gc.fillRect(xS * size, yS * size, size, size);
            gc.fillRect(xA * size, yA * size, size, size);
        }
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(gc);
        }
    } // drawMaze

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

    private void lookForNewWays() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j].setNext(caminos(i, j));
            } // for j
        } // for i
    } // lookForNewWays

} // end class
