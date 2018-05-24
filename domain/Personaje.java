/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

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
    private Block currentBlock,nextBlock;
    private int direction, dirAux;
    private boolean crash = true;

    public Personaje(int size, Block start) {
        xPos = start.getX();
        yPos = start.getY();
        x = xPos * size;
        y = yPos * size;
        this.size = size;
        this.currentBlock=start;
    }
    Boolean flag = true;

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
            if (crash) {
                direction = (int) (Math.random() * (5 - 1) + 1);
            }
            if (next(direction)) {
                try {
                    switch (direction) {
                        case 1:
                            while (this.currentBlock.in(x, y)) {
                                y += 1;
                                Thread.sleep(10);
                            }
                        case 2:
                            while (this.currentBlock.in(x, y)) {
                                x += 1;
                                Thread.sleep(10);
                            }
                        case 3:
                            while (this.currentBlock.in(x, y)) {
                                y -= 1;
                                Thread.sleep(10);
                            }
                        case 4:
                            while (this.currentBlock.in(x, y)) {
                                x -= 1;
                                Thread.sleep(10);
                            }

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.currentBlock=nextBlock;
            } else {
                crash = !crash;
            }
        }
    }

    public boolean next(int dir) {
        
        if (((dir == 1 && dirAux == 3) || (dirAux == 1 && dir == 3))&& !encerrado()) {
            return false;
        } else if (((dir == 2 && dirAux == 4) || (dirAux == 2 && dir == 4))&& !encerrado()) {
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
                    this.nextBlock=this.currentBlock.getNext().get(i);
                    yPos += aux;
                    this.dirAux = dir;
                    return true;

                }
            }
        } else {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getX() == xPos + aux) {

                    this.nextBlock=this.currentBlock.getNext().get(i);
                    xPos += aux;
                    this.dirAux = dir;
                    return true;

                }
            }
        }

        return false;
    }
    public boolean encerrado(){
        int dir;
        switch (dirAux) {
            case 1:
                dir=3;
                break;
            case 2:
                dir=4;
                break;
            case 3:
                dir=1;
                break;
            default:
                dir=2;
                break;
        }
        int aux;
        if (dir == 1 || dir == 2) {
            aux = 1;
        } else {
            aux = -1;
        }
        
        if(this.currentBlock.getNext().size()==1){
            if((dir==1||dir==3)&&this.currentBlock.getNext().get(0).getY()==yPos+aux){
                direction=dir;
                System.err.println(dir+"primero:"+direction);
                return true;
            }else if((dir==2||dir==4)&&this.currentBlock.getNext().get(0).getX()==xPos+aux){
                direction=dir;
                System.err.println(dir+" "+"segundo:"+direction);
                return true;
            }
        }
        return false;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.AQUA);
        gc.fillRect(x, y, size, size);
    }

}
