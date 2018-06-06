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
    private boolean pause=false;

    public SharedBuffer(ArrayList<Character> characters, ArrayList<Item> items) {
        this.items = items;
        this.characters = characters;
        this.records = FXCollections.observableArrayList();
    } // costructor
    
    public void clear(){
        this.characters.clear();
        this.items.clear();
    }

    public synchronized boolean colisionVs(int order) {
        int size = this.characters.get(0).getSize();
        int xC, yC, xMe, yMe;
        int directionMe = this.characters.get(order).getDirection();
        int directionOther;
        xMe = this.characters.get(order).getX();
        yMe = this.characters.get(order).getY();
        Rectangle rectangleMe;
        switch (directionMe) {
            case 1:
                // abajo
                yMe += size + 10;
                rectangleMe = new Rectangle(xMe, yMe, size, 10);
                break;
            case 3:
                //arriba
                yMe -= 10;
                rectangleMe = new Rectangle(xMe, yMe, size, 10);
                break;
            case 2:
                //derecha
                xMe += size + 10;
                rectangleMe = new Rectangle(xMe, yMe, 10, size);
                break;
            default:
                //izquierda
                xMe -= 10;
                rectangleMe = new Rectangle(xMe, yMe, 10, size);
                break;
        } // switch

        for (int i = 0; i < this.characters.size(); i++) {
            xC = this.characters.get(i).getX();
            yC = this.characters.get(i).getY();
            directionOther = this.characters.get(i).getDirection();
            Rectangle rectangleOther = new Rectangle(xC, yC, size, size);
            if (i != order && rectangleOther.intersects(rectangleMe) && this.characters.get(i).getFlag()) {
                if (this.characters.get(order).oppositeDirection(directionMe) == directionOther) {
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
        if(!pause){
            this.characters.get(order).setMovement(1);
        }else{
            this.characters.get(order).setMovement(0);
        }
        
        return false;
    } // colisionVs: retorna true si colisionan directamente o si el personaje debe disminuir su velocidad

    public ArrayList<Character> getCharacters() {
        return this.characters;
    } // getCharacters: retorna array characters

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
            Rectangle rectangleMe = new Rectangle(xMe, yMe, size, size);
            for (int i = 0; i < this.characters.size(); i++) {
                xC = this.characters.get(i).getX();
                yC = this.characters.get(i).getY();
                Rectangle rectangleOther = new Rectangle(xC, yC, size, size);
                if (rectangleOther.intersects(rectangleMe)) {
                    if (this.characters.get(i).getType().equals("S")) {
                        if (this.characters.get(i).getSpeed() > 1) {
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
    } // itemColision: Método que detecta si un item colisiona con un personaje, si es smart aumenta su velocidad
    // y si es un furious lo destruye

    public void stopAll(){
        for(int i=0;i<characters.size();i++){
            characters.get(i).stop();
        }
    }
    
    public boolean verifyStart(Block starto) {
        int size = starto.getSize();
        int xStart = starto.getX() * size;
        int yStart = starto.getY() * size;
        int xCharacter, yC;
        int cont = 0;
        Rectangle startBlock = new Rectangle(xStart, yStart, size, size);
        for (int i = 0; i < this.characters.size(); i++) {
            xCharacter = this.characters.get(i).getX();
            yC = this.characters.get(i).getY();
            Rectangle rC = new Rectangle(xCharacter, yC, size, size);
            if (startBlock.intersects(rC)) {
                cont++;
            }
        } // for i
        return cont == 0;
    } // verifyStart: retorna true si el personaje llegó a la meta

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    } // setCharacters: setea array de characters

    public ArrayList<Item> getItems() {
        return this.items;
    } // getItems: retorna array de items

    public void addFinisher(Record record) {
        record.setTime(Proyecto3Progra2.timer);
        this.records.add(record);
    } // addFinisher: se añaden los datos de personaje cuando finaliza

    public ObservableList<Record> getRecords() {
        return this.records;
    } // getRecords: retorna observableList de records
    
    public void pauseCharacters(boolean pause){        
        this.pause=pause;
        
    }
} // end class
