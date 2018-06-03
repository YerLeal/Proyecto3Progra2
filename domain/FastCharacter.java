package domain;

import business.SharedBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FastCharacter extends Character {

    private int cont = 1;
    private boolean state = true;
    private int speedAux = 5000;
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

                    speed = speedAux;
                }
                if (speedAux == 1000) {
                    speedAux = 5000;
                    state = true;
                    cont = 1;
                }
                try {
                    Thread.sleep(1000);
                    speedAux -= 1000;
                } catch (InterruptedException ex) {
                    Logger.getLogger(FastCharacter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    public FastCharacter(int size, SharedBuffer buffer) {
        super(size, buffer);
        super.speed = 2;
        super.tipo = "FA";
    }

    @Override
    public void run() {
        new Thread(timer).start();
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
                    if (currentBlock.getX() == 7 && currentBlock.getY() == 7) {
                        super.setFlag(false);
                    }
                } else {
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

} // end class
