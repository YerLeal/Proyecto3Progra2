/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import business.SharedBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author maikel
 */
public class Item extends Character {

    private int dir, visDir;

    private boolean bandera, cosa;

    public Item(int size, Block start,SharedBuffer buffer,int order) {
        super(size, start,buffer,order);
        super.speed = 10;
        bandera = true;
        cosa = true;
    }

    @Override
    public void run() {
        while (flag) {
            if (bandera) {
                dir = (int) (Math.random() * (5 - 1) + 1);
            }
            if (next(dir)) {
                bandera = false;
                cosa = true;
                switch (dir) {
                    case 1:
                        visDir = 3;
                        break;
                    case 2:
                        visDir = 4;
                        break;
                    case 3:
                        visDir = 1;
                        break;
                    default:
                        visDir = 2;
                        break;
                }
                try {
                    switch (dir) {
                        case 1:
                            while (currentBlock.in(x, y)) {
                                y += 1;
                                Thread.sleep(speed);
                            }
                        case 2:
                            while (currentBlock.in(x, y)) {
                                x += 1;
                                Thread.sleep(speed);
                            }
                        case 3:
                            while (currentBlock.in(x, y)) {
                                y -= 1;
                                Thread.sleep(speed);
                            }
                        case 4:
                            while (currentBlock.in(x, y)) {
                                x -= 1;
                                Thread.sleep(speed);

                            }

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Character.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                currentBlock = nextBlock;
            } else {
                if (!bandera) {
                    int aux = dir;
                    dir = visDir;
                    visDir = aux;
                    cosa = false;
                }
            }
        }
    }

    @Override
    public boolean next(int dir) {

        if (((dir == 1 && dirAux == 3) || (dirAux == 1 && dir == 3)) && cosa) {
            return false;
        } else if (((dir == 2 && dirAux == 4) || (dirAux == 2 && dir == 4)) && cosa) {
            return false;
        }
        int aux;
        if (dir == 1 || dir == 2) {
            aux = 1;
        } else {
            aux = -1;
        }

        if (dir == 1 || dir == 3) {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getY() == yPos + aux) {
                    this.nextBlock = this.currentBlock.getNext().get(i);
                    yPos += aux;
                    this.dirAux = dir;
                    return true;

                }
            }
        } else {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getX() == xPos + aux) {

                    this.nextBlock = this.currentBlock.getNext().get(i);
                    xPos += aux;
                    this.dirAux = dir;
                    return true;

                }
            }
        }

        return false;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.CHOCOLATE);
        gc.fillOval(x+(size/2), y+(size/2), 20, 20);
    }
}
