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
public class FastCharacter extends Character {

    private int cont = 1;
    private int sleep = 1000;
    private boolean state = true;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            while (true) {

                if (cont <= 2) {
                    cont++;

                } else if (cont > 2) {
                    state = false;
                }

                if (state) {
                    speed = 2;
                } else {
                    state = true;
                    cont = 1;
                    speed = 5000;
                    sleep = speed;
                }
                System.err.println("Cont:" + cont + " Sleep:" + sleep + "State:" + state);
                try {
                    Thread.sleep(sleep);
                    sleep = 1000;
                } catch (InterruptedException ex) {
                    Logger.getLogger(FastCharacter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    public FastCharacter(int size, Block start) {
        super(size, start);
        super.speed = 2;
    }

    @Override
    public void run() {
        new Thread(timer).start();
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

    public void burst() {

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, size, size);
    }
}
