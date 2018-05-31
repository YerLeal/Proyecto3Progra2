/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.awt.Rectangle;
import java.util.ArrayList;
import domain.Character;
/**
 *
 * @author maikel
 */

public class SharedBuffer {

    private ArrayList<Character> characters;

    public SharedBuffer(ArrayList<Character> characters) {
        this.characters = characters;
    }
    public SharedBuffer(){
        this.characters=new ArrayList<>();
    }

    public synchronized boolean colisionVs(int order) {
        int size = characters.get(0).getSize();
        int xC, yC, xMe, yMe;
        int dirMe = characters.get(order).getDirection();
        int dirO;
        xMe = characters.get(order).getX();
        yMe = characters.get(order).getY();int aux;
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
            dirO = characters.get(i).getDirection();
            Rectangle elOtro = new Rectangle(xC, yC, size, size);
            if (i != order && elOtro.intersects(yo)) {
                if (characters.get(order).oposDir(dirMe) == dirO) {
                    System.err.println("vs"+order);
                    characters.get(order).setCrash(true);
                    characters.get(i).setCrash(true);
                    return true;
                } else {
                        System.err.println("otro"+order);
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

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

}
