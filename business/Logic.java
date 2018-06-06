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

    public Logic(int dificulty) {
        this.difficulty = dificulty;
        getBlockSize();
        this.maze = new Block[WIDTH / this.size][HEIGHT / this.size];
    } // constructor

    public int getDifficulty() {
        return this.difficulty;
    } // getDifficulty: retorna dificultad

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    } // setDifficulty: setea dificultad

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
        } // switch
    } // getStart: retorna array con las entradas del laberinto

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
    } // getFinish: retorna array con las salidas del laberinto

    public Block[][] getMaze() {
        return this.maze;
    } // getMaze: retorna el laberinto

    public void changeTypeBlock(int x, int y, GraphicsContext gc) {
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].isClicked(x, y)) {
                    System.out.println(i + " " + j);
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
    } // changeTypeBlock:cambia el tipo de bloque

    public void startItems() {
        for (int i = 0; i < this.items.size(); i++) {
            this.items.get(i).start();
        } // for
    } // startItems: inicia los items

    public void addItem(int x, int y, SharedBuffer buffer, boolean life, GraphicsContext gc) {
        Item itemAux = new Item(this.size, buffer, this.name);
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].isClicked(x, y)) {
                    if (this.maze[i][j].getType().equals("floor")) {
                        itemAux.setOrder(this.itemCont);
                        itemAux.setStarto(this.maze[i][j]);
                        this.items.add(itemAux);
                        buffer.getItems().add(this.items.get(this.itemCont));
                        if (life) {
                            this.items.get(this.itemCont).start();
                        } else {
                            this.items.get(this.itemCont).draw(gc);
                        }
                        this.itemCont++;
                    } // if (this.maze[i][j].getType().equals("floor"))
                    break;
                } // if (this.maze[i][j].isClicked(x, y))
            } // for j
        } // for i
    } // addItem: annade nuevo item a el array de items

    public int getSize() {
        return this.size;
    } // getSize: retorna el size de los bloques y los personajes

    public void createMaze() {
        Maze m = new Maze();
        this.maze = m.getMaze(getDifficulty(), this.size);
        lookForNewWays();
    } // createMaze: creacion de laberinto

    private void getBlockSize() {
        switch (this.getDifficulty()) {
            case 1:
                this.size = 80;
                break;
            case 2:
                this.size = 40;
                break;
            case 3:
                this.size = 40;
                break;
        } // switch
    } // getBlockSize: da el tamanno del bloque segun dificultad

    public void drawMaze(GraphicsContext gc) {
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].getType().equals("wall")) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i * this.size, j * this.size, this.size, this.size);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i * this.size, j * this.size, this.size, this.size);
                }
            } // for j
        } // for i
        int xS, yS, xA, yA;
        ArrayList<Block> finish = getFinish();
        gc.setFill(Color.GREEN);
        if (getDifficulty() < 3) {
            xS = finish.get(0).getX();
            yS = finish.get(0).getY();
            gc.fillRect(xS * this.size, yS * this.size, this.size, this.size);
        } else {
            xS = finish.get(0).getX();
            yS = finish.get(0).getY();
            xA = finish.get(1).getX();
            yA = finish.get(1).getY();
            gc.fillRect(xS * this.size, yS * this.size, this.size, this.size);
            gc.fillRect(xA * this.size, yA * this.size, this.size, this.size);
        }
        for (int i = 0; i < this.items.size(); i++) {
            this.items.get(i).draw(gc);
        }
    } // drawMaze: dibuja el laberinto

    private ArrayList<Block> paths(int x, int y) {
        ArrayList<Block> next = new ArrayList<>();
        if (x + 1 < this.maze.length && this.maze[x + 1][y].getType().equals("floor")) {
            next.add(this.maze[x + 1][y]);
        }
        if (x - 1 >= 0 && this.maze[x - 1][y].getType().equals("floor")) {
            next.add(this.maze[x - 1][y]);
        }
        if (y + 1 < this.maze[0].length && this.maze[x][y + 1].getType().equals("floor")) {
            next.add(this.maze[x][y + 1]);
        }
        if (y - 1 >= 0 && this.maze[x][y - 1].getType().equals("floor")) {
            next.add(this.maze[x][y - 1]);
        }
        return next;
    } // paths: setea las rutas al bloque

    private void lookForNewWays() {
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                this.maze[i][j].setNext(paths(i, j));
            } // for j
        } // for i
    } // lookForNewWays: busca posibles rutas

} // end class
