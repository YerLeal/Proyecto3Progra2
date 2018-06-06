package domain;

import business.SharedBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Item extends Character {

    private int dir, visDir, image;

    private boolean bandera, cosa;

    public Item(int size, SharedBuffer buffer, String name) {
        super(size, buffer, null, name);
        super.speed = 10;
        bandera = true;
        cosa = true;
        addSprites();
    } // constructor

    @Override
    public void run() {
        while (super.getFlag()) {
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
                            while (currentBlock.isInTheBlock(x, y) && super.getFlag()) {
                                buff.itemColision(order);
                                y += 1;
                                Thread.sleep(this.speed);
                            }
                        case 2:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && super.getFlag()) {
                                this.buff.itemColision(this.order);
                                this.x += 1;
                                Thread.sleep(this.speed);
                            }
                        case 3:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && super.getFlag()) {
                                this.buff.itemColision(this.order);
                                this.y -= 1;
                                Thread.sleep(this.speed);
                            }
                        case 4:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && super.getFlag()) {
                                this.buff.itemColision(this.order);
                                this.x -= 1;
                                Thread.sleep(this.speed);
                            }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Character.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                this.currentBlock = this.nextBlock;
            } else {
                if (!this.bandera) {
                    int aux = this.dir;
                    this.dir = this.visDir;
                    this.visDir = aux;
                    this.cosa = false;
                }
            }
        } // while
    } // run

    @Override
    public boolean next(int direction) {
        if (((direction == 1 && this.directionAux == 3) || (this.directionAux == 1 && direction == 3)) && this.cosa) {
            return false;
        } else if (((direction == 2 && this.directionAux == 4) || (this.directionAux == 2 && direction == 4)) && this.cosa) {
            return false;
        }
        int aux;
        if (direction == 1 || direction == 2) {
            aux = 1;
        } else {
            aux = -1;
        }
        if (direction == 1 || direction == 3) {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getY() == this.yPos + aux) {
                    this.nextBlock = this.currentBlock.getNext().get(i);
                    this.yPos += aux;
                    this.directionAux = direction;
                    return true;
                }
            } // for i
        } else {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getX() == this.xPos + aux) {
                    this.nextBlock = this.currentBlock.getNext().get(i);
                    this.xPos += aux;
                    this.directionAux = direction;
                    return true;
                }
            } // for i
        }
        return false;
    } // next

    public void addSprites() {
        for (int i = 0; i < 3; i++) {
            super.setSprites(new Image("assets/i" + i + ".png"));
        }
    } // addSprites

    public void setImage(int image) {
        this.image = image;
    } // setImage

    @Override
    public void draw(GraphicsContext gc) {
        switch (this.image) {
            case 0:
                gc.drawImage(super.getSprites().get(this.image), this.x, this.y, this.size, this.size);
                break;
            case 1:
                gc.drawImage(super.getSprites().get(this.image), this.x, this.y, this.size, this.size);
                break;
            case 2:
                gc.drawImage(super.getSprites().get(this.image), this.x, this.y, this.size, this.size);
                break;
        } // switch
    } // draw

} // end class
