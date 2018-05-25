/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author maikel
 */
public abstract class Character extends Thread {

    protected int xPos, yPos, x, y, size,speed;
    protected Block currentBlock,nextBlock;
    protected int direction, dirAux;
    protected boolean crash = true;

    public Character(int size, Block start) {
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
                return true;
            }else if((dir==2||dir==4)&&this.currentBlock.getNext().get(0).getX()==xPos+aux){
                direction=dir;
                return true;
            }
        }
        return false;
    }

    public abstract void draw(GraphicsContext gc); 

    

}
