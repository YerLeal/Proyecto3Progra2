/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author maikel
 */
public class Personaje extends Thread {

    private int xPos, yPos, x, y, size;
    private ArrayList<Block> camino, past;
    private int direction;

    public Personaje(int size, Block start) {
        xPos = start.getX();
        yPos = start.getY();
        x = xPos * size;
        y = yPos * size;
        this.size = size;
        this.camino = new ArrayList<>();
        this.camino.add(start);
        this.past = new ArrayList<>();
    }
    Boolean flag = true;
    int cont = 0;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void run() {

        while (flag) {
            direction = new Random().nextInt(4);
            //System.err.println(camino.get(cont).getNext().get(0).getX());
            if (next(direction)) {
                try {
                    switch (direction) {
                        case 0:
                            while (this.camino.get(cont).in(x, y)) {
                                y += 1;
                                Thread.sleep(10);
                            }
                        case 1:
                            while (this.camino.get(cont).in(x, y)) {
                                x += 1;
                                Thread.sleep(10);
                            }
                        case 2:
                            while (this.camino.get(cont).in(x, y)) {
                                y -= 1;
                                Thread.sleep(10);
                            }
                        case 3:
                            while (this.camino.get(cont).in(x, y)) {
                                x -= 1;
                                Thread.sleep(10);
                            }

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.past.add(this.camino.get(cont));
                cont++;
            }
        }
    }

    public boolean next(int dir) {
        int aux;
        if (dir == 0 || dir == 1) {
            aux = 1;
        } else {
            aux = -1;
        }

        if (dir == 0 || dir == 2) {
            for (int i = 0; i < this.camino.get(cont).getNext().size(); i++) {
                if (this.camino.get(cont).getNext().get(i).getY() == yPos + aux) {
                    if (validation(i)) {
                        this.camino.add(this.camino.get(cont).getNext().get(i));
                        yPos += aux;
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < this.camino.get(cont).getNext().size(); i++) {
                if (this.camino.get(cont).getNext().get(i).getX() == xPos + aux) {
                    if (validation(i)) {
                        this.camino.add(this.camino.get(cont).getNext().get(i));
                        xPos += aux;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean validation(int i) {
        for (int j = 0; j < this.past.size(); j++) {
            if (this.camino.get(cont).getNext().get(i).getX() == this.past.get(j).getX() && this.camino.get(cont).getNext().get(i).getY() == this.past.get(j).getY()) {
                return false;
            }
        }
        return true;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.AQUA);
        gc.fillRect(x, y, size, size);
    }

}
