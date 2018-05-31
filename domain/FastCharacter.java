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
public class FastCharacter extends Character {

    private int cont = 1;
    private int sleep = 1000;
    private boolean state = true;
    private int speedAux=5000;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            while (true) {
//                System.err.println("EntraFast");
                if (cont <= 2) {
                    cont++;

                } else if (cont > 2) {
                    state = false;
                }

                if (state) {
                    speed = 2;
                } else {
                    
                    speed = speedAux;
                    sleep = speed;
                }
                if(speedAux==1000){
                    speedAux=5000;
                    state = true;
                    cont = 1;
                }
//                System.err.println("Cont:" + cont + " Sleep:" + sleep + "State:" + state);
                try {
                    Thread.sleep(1000);
                    speedAux-=1000;
                } catch (InterruptedException ex) {
                    Logger.getLogger(FastCharacter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    public FastCharacter(int size, Block start, SharedBuffer buffer, int order) {
        super(size, start, buffer, order);
        super.speed = 2;
    }

    @Override
    public void run() {
        new Thread(timer).start();
        while (flag) {
           
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
                }else{
                    try {
                        rePos();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FastCharacter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, size, size);
    }
}
