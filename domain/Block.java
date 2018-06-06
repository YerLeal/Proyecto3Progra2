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
    } // constructor

    public int getX() {
        return this.x;
    } // getX

    public void setX(int x) {
        this.x = x;
    } // setX

    public int getY() {
        return this.y;
    } // getY

    public void setY(int y) {
        this.y = y;
    } // setY

    public String getType() {
        return type;
    } // getType

    public ArrayList<Block> getNext() {
        return next;
    } // getNext

    public void setNext(ArrayList<Block> next) {
        this.next = next;
    } // setNext

    public int getSize() {
        return this.size;
    } // getSize

    public boolean isInTheBlock(int xPosition, int yPosition) {
        return (((xPosition >= this.x * this.size && xPosition < this.x * this.size + this.size)
                || (xPosition + this.size > this.x * this.size && xPosition + this.size < this.x * this.size + this.size))
                && ((yPosition >= this.y * this.size && yPosition < this.y * this.size + this.size)
                || (yPosition + this.size > this.y * this.size && yPosition + this.size < this.y * this.size + this.size)));
    } // isInTheBlock

    public boolean isClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.x * this.size && xMouse <= this.x * this.size + this.size)
                && (yMouse >= this.y * this.size && yMouse <= this.y * this.size + this.size)) {
            return true;
        }
        return false;
    } // isClicked

    public void setType(String type) {
        this.type = type;
    } // setType

} // end class
