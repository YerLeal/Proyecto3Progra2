/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import business.SharedBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author maikel
 */
public class FuriousCharacter extends Character {

    public FuriousCharacter(int size, Block start, SharedBuffer buffer, int order) {
        super(size, start, buffer, order);
        super.speed = 10;
        super.tipo = "F";
    }

    @Override
    public void run() {
        while (super.getFlag()) {
            direction = (int) (Math.random() * (5 - 1) + 1);

            if (next(direction)) {
                crash = false;
                try {
                    switch (direction) {
                        case 1:
                            while (currentBlock.in(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                y += movement;
                            }
                            break;
                        case 2:
                            while (currentBlock.in(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                x += movement;
                            }
                            break;
                        case 3:
                            while (currentBlock.in(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                y -= movement;
                            }
                            break;
                        case 4:
                            while (currentBlock.in(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                x -= movement;
                            }
                            break;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Character.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                if (!crash) {
                    metodoRandom(direction);
                    currentBlock = nextBlock;
                } else {
                    try {
                        rePos();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SmartCharacter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        switch (direction) {
            case 1:
                gc.setFill(Color.AQUA);
                break;
            case 2:
                gc.setFill(Color.RED);
                break;
            case 3:
                gc.setFill(Color.GREEN);
                break;
            default:
                gc.setFill(Color.BLUE);
                break;
        }
        gc.fillRect(x, y, size, size);
    }

}
