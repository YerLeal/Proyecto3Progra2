package domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {

    private int x, y, size;
    private String type;
    private ArrayList<Block> next;
//    private BufferedImage image;

    public Block(int x, int y, int size, String type) {
//        this.image = image;
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }

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

    public String getType() {
        return type;
    }

    public ArrayList<Block> getNext() {
        return next;
    }

    public void setNext(ArrayList<Block> next) {
        this.next = next;
    }

    public int getSize() {
        return size;
    }
    
//    public BufferedImage getImage() {
//        return image;
//    }
//
//    public void setImage(BufferedImage image) {
//        this.image = image;
//    }
    public boolean in(int xMouse, int yMouse) {

        return (((xMouse >= this.x * size && xMouse < this.x * size + this.size) || (xMouse + size > this.x * size && xMouse + size < this.x * size + this.size)) && ((yMouse >= this.y * size && yMouse < this.y * size + this.size) || (yMouse + size >= this.y * size && yMouse + size < this.y * size + this.size)));
    } // isClicked: retorna true si el botón fue clickeado y false si no

    public boolean isClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.x * this.size && xMouse <= this.x * this.size + this.size)
                && (yMouse >= this.y * this.size && yMouse <= this.y * this.size + this.size)) {
            return true;
        }
        return false;
    }

    public void setType(String type) {
        this.type = type;
    }

}
