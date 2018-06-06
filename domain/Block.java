package domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {

    private int x, y, size;
    private String type;
    private ArrayList<Block> next;

    public Block(int x, int y, int size, String type) {
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

    public boolean isInTheBlock(int xPosition, int yPosition) {
        return (((xPosition >= this.x * size && xPosition < this.x * size + this.size) || (xPosition + size > this.x * size && xPosition + size < this.x * size + this.size)) && 
                ((yPosition >= this.y * size && yPosition < this.y * size + this.size) || (yPosition + size > this.y * size && yPosition + size < this.y * size + this.size)));
    }

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

} // end class
