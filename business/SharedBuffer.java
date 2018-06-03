package business;

import domain.Block;
import domain.Item;
import java.awt.Rectangle;
import java.util.ArrayList;
import domain.Character;

public class SharedBuffer {

    private ArrayList<Character> characters;
    private ArrayList<Item> items;

    public SharedBuffer(ArrayList<Character> characters, ArrayList<Item> items) {
        this.items = items;
        this.characters = characters;
    }

    public synchronized boolean colisionVs(int order) {
        int size = characters.get(0).getSize();
        int xC, yC, xMe, yMe;
        int dirMe = characters.get(order).getDirection();
        int dirO;
        xMe = characters.get(order).getX();
        yMe = characters.get(order).getY();
        Rectangle yo;
        switch (dirMe) {
            case 1:
                // abajo
                yMe += size+10;
                yo = new Rectangle(xMe, yMe, size, 10);
                break;
            case 3:
                //arriba
                yMe-=10;
                yo = new Rectangle(xMe, yMe, size, 10);
                break;
            case 2:
                //derecha
                xMe += size+10;
                yo = new Rectangle(xMe, yMe, 10, size);
                break;
            default:
                //izquierda
                xMe-=10;
                yo = new Rectangle(xMe, yMe, 10, size);
                break;
        }
        
        for (int i = 0; i < characters.size(); i++) {
            xC = characters.get(i).getX();
            yC = characters.get(i).getY();
            dirO = characters.get(i).getDirection();
            Rectangle elOtro = new Rectangle(xC, yC, size, size);
            if (i != order && elOtro.intersects(yo) && characters.get(i).getFlag()) {
                if (characters.get(order).oposDir(dirMe) == dirO) {
                    if (characters.get(order).isCrash() && !characters.get(i).isCrash()) {
                        characters.get(order).setCrash(false);
                        characters.get(i).setCrash(true);
                    } else if (characters.get(i).isCrash() && !characters.get(order).isCrash()) {
                        characters.get(i).setCrash(false);
                        characters.get(order).setCrash(true);
                    } else if (characters.get(i).isCrash() && characters.get(order).isCrash()) {
                        characters.get(i).setCrash(false);
                        characters.get(order).setCrash(false);
                    } else {
                        characters.get(order).setCrash(true);
                        characters.get(i).setCrash(true);
                    }
                    return true;
                } else {
                    characters.get(order).setMovement(0);
                    return true;
                }
            }
        } //for
        characters.get(order).setMovement(1);
        return false;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public synchronized void itemColision(int order) {
        if (items.get(order).getFlag()) {
            int size = items.get(0).getSize();
            int xC, yC, xMe, yMe;
            int dirMe = items.get(order).getDirection();
            xMe = items.get(order).getX();
            yMe = items.get(order).getY();
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
            for (int i = 0; i < characters.size(); i++) {
                xC = characters.get(i).getX();
                yC = characters.get(i).getY();
                Rectangle elOtro = new Rectangle(xC, yC, size, size);
                if (elOtro.intersects(yo)) {
                    if (characters.get(i).getTipo().equals("S")) {
                        if(characters.get(i).getSpeed()>0){
                            characters.get(i).setSpeed();
                        }
                        items.get(order).setFlag(false);
                        items.get(order).setImage(1);
                    } else if (characters.get(i).getTipo().equals("F")) {
                        items.get(order).setFlag(false);
                        items.get(order).setImage(2);

                    }
                }
            }
        }
    }

    public boolean verifyStart(Block starto) {
        int size = starto.getSize();
        int xS = starto.getX() * size;
        int yS = starto.getY() * size;
        int xC, yC;
        int cont = 0;
        Rectangle sBlock = new Rectangle(xS, yS, size, size);
        for (int i = 0; i < characters.size(); i++) {
            xC = characters.get(i).getX();
            yC = characters.get(i).getY();
            Rectangle rC = new Rectangle(xC, yC, size, size);
            if (sBlock.intersects(rC)) {
                cont++;
            }
        }
        return cont == 0;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

} // end class
