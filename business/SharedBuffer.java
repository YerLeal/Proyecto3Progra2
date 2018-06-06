package business;

import domain.Block;
import domain.Item;
import java.awt.Rectangle;
import java.util.ArrayList;
import domain.Character;
import domain.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyecto3progra2.Proyecto3Progra2;

public class SharedBuffer {

    private ArrayList<Character> characters;
    private ObservableList<Record> records;
    private ArrayList<Item> items;

    public SharedBuffer(ArrayList<Character> characters, ArrayList<Item> items) {
        this.items = items;
        this.characters = characters;
        this.records = FXCollections.observableArrayList();
    } // costructor

    public synchronized boolean colisionVs(int order) {
        int size = this.characters.get(0).getSize();
        int xC, yC, xMe, yMe;
        int dirMe = this.characters.get(order).getDirection();
        int dirO;
        xMe = this.characters.get(order).getX();
        yMe = this.characters.get(order).getY();
        Rectangle yo;
        switch (dirMe) {
            case 1:
                // abajo
                yMe += size + 10;
                yo = new Rectangle(xMe, yMe, size, 10);
                break;
            case 3:
                //arriba
                yMe -= 10;
                yo = new Rectangle(xMe, yMe, size, 10);
                break;
            case 2:
                //derecha
                xMe += size + 10;
                yo = new Rectangle(xMe, yMe, 10, size);
                break;
            default:
                //izquierda
                xMe -= 10;
                yo = new Rectangle(xMe, yMe, 10, size);
                break;
        } // switch

        for (int i = 0; i < this.characters.size(); i++) {
            xC = this.characters.get(i).getX();
            yC = this.characters.get(i).getY();
            dirO = this.characters.get(i).getDirection();
            Rectangle elOtro = new Rectangle(xC, yC, size, size);
            if (i != order && elOtro.intersects(yo) && this.characters.get(i).getFlag()) {
                if (this.characters.get(order).oppositeDirection(dirMe) == dirO) {
                    this.characters.get(order).collision();
                    this.characters.get(i).collision();
                    this.characters.get(order).setCrash(true);
                    this.characters.get(i).setCrash(true);
                    return true;
                } else {
                    this.characters.get(order).setMovement(0);
                    return true;
                }
            } // if (i != order && elOtro.intersects(yo) && this.characters.get(i).getFlag())
        } //for

        this.characters.get(order).setMovement(1);
        return false;
    } // colisionVs

    public ArrayList<Character> getCharacters() {
        return this.characters;
    } // getCharacters

    public synchronized void itemColision(int order) {
        if (this.items.get(order).getFlag()) {
            int size = this.items.get(0).getSize();
            int xC, yC, xMe, yMe;
            int dirMe = this.items.get(order).getDirection();
            xMe = this.items.get(order).getX();
            yMe = this.items.get(order).getY();
            int aux;
            if (dirMe == 1 || dirMe == 2) {
                aux = 10;
            } else {
                aux = -10;
            }
            if (dirMe == 1 || dirMe == 3) {
                yMe += aux;
            } else {
                xMe += aux;
            }
            Rectangle yo = new Rectangle(xMe, yMe, size, size);
            for (int i = 0; i < this.characters.size(); i++) {
                xC = this.characters.get(i).getX();
                yC = this.characters.get(i).getY();
                Rectangle elOtro = new Rectangle(xC, yC, size, size);
                if (elOtro.intersects(yo)) {
                    if (this.characters.get(i).getType().equals("S")) {
                        if (this.characters.get(i).getSpeed() > 0) {
                            this.characters.get(i).setSpeed();
                        }
                        this.items.get(order).setFlag(false);
                        this.items.get(order).setImage(1);
                    } else if (this.characters.get(i).getType().equals("F")) {
                        this.items.get(order).setFlag(false);
                        this.items.get(order).setImage(2);
                    }
                } // if (elOtro.intersects(yo))
            } // for i
        } // if (this.items.get(order).getFlag())
    } // itemColision

    public boolean verifyStart(Block starto) {
        int size = starto.getSize();
        int xS = starto.getX() * size;
        int yS = starto.getY() * size;
        int xC, yC;
        int cont = 0;
        Rectangle sBlock = new Rectangle(xS, yS, size, size);
        for (int i = 0; i < this.characters.size(); i++) {
            xC = this.characters.get(i).getX();
            yC = this.characters.get(i).getY();
            Rectangle rC = new Rectangle(xC, yC, size, size);
            if (sBlock.intersects(rC)) {
                cont++;
            }
        } // for i
        return cont == 0;
    } // verifyStart

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    } // setCharacters

    public ArrayList<Item> getItems() {
        return this.items;
    } // getItems

    public void addFinisher(Record r) {
        r.setTime(Proyecto3Progra2.timer);
        this.records.add(r);
    } // addFinisher

    public ObservableList<Record> getRecords() {
        return this.records;
    } // getRecords

} // end class
