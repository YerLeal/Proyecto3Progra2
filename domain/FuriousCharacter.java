/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author maikel
 */
public class FuriousCharacter extends Character {

    public FuriousCharacter(int size, Block start) {
        super(size, start);
        super.speed = 10;
    }

    @Override
    public void run() {
        while (flag) {
            if (crash) {
                direction = (int) (Math.random() * (5 - 1) + 1);
            }
            if (next(direction)) {
                try {
                    switch (direction) {
                        case 1:
                            while (currentBlock.in(x, y)) {
                                y += 1;
                                Thread.sleep(speed);
                            }
                        case 2:
                            while (currentBlock.in(x, y)) {
                                x += 1;
                                Thread.sleep(speed);
                            }
                        case 3:
                            while (currentBlock.in(x, y)) {
                                y -= 1;
                                Thread.sleep(speed);
                            }
                        case 4:
                            while (currentBlock.in(x, y)) {
                                x -= 1;
                                Thread.sleep(speed);

                            }

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Character.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                currentBlock = nextBlock;
            } else {
                crash = !crash;
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x, y, size, size);
    }

}
