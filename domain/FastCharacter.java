package domain;

import business.SharedBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class FastCharacter extends Character {

    private int cont = 1;
    private boolean state = true;
    private int speedAux = 5000;
    private int charge = 0;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (cont <= 2) {
                    charge = 0;
                    cont++;

                } else if (cont > 2) {
                    state = false;
                }

                if (state) {
                    speed = 2;
                } else {
                    speed = 5000;
                }
                if (!state) {
                    charge++;
                }
                if (speedAux == 1000) {
                    speedAux = 5000;
                    state = true;
                    cont = 1;
                    speed = 2;
                }
                try {
                    Thread.sleep(1000);
                    if (!state) {
                        speedAux -= 1000;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(FastCharacter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    public FastCharacter(int size, SharedBuffer buffer, ArrayList<Block> finish, String name) {
        super(size, buffer, finish, name);
        super.speed = 2;
        super.type = "FA";
        addSprites();
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
                            while (currentBlock.isInTheBlock(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                y += movement;
                            }
                            break;
                        case 2:
                            while (currentBlock.isInTheBlock(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                x += movement;
                            }
                            break;
                        case 3:
                            while (currentBlock.isInTheBlock(x, y) && !crash) {
                                Thread.sleep(speed);
                                buff.colisionVs(order);
                                y -= movement;
                            }
                            break;
                        case 4:
                            while (currentBlock.isInTheBlock(x, y) && !crash) {
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
                    changePositionInMatrix(direction);
                    currentBlock = nextBlock;
                } else {
                    try {
                        repositioning();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FastCharacter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                super.isFinish();
            }
        }
    }

    public void addSprites() {
        SnapshotParameters parameters = new SnapshotParameters();
        for (int i = 0; i < 12; i++) {
            Image image = new Image(
                    "assets/fa" + i + ".png"
            );
            ImageView imageView = new ImageView(image);
            imageView.setClip(new ImageView(image));
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-1.0);
            Blend blush = new Blend(
                    BlendMode.MULTIPLY,
                    monochrome,
                    new ColorInput(0, 0,
                            imageView.getImage().getWidth(),
                            imageView.getImage().getHeight(),
                            Color.BLUE));
            imageView.setEffect(blush);
            parameters.setFill(Color.TRANSPARENT);
            super.setSprites(imageView.snapshot(parameters, null));
        }
    }
    private int image = 0;

    @Override
    public void draw(GraphicsContext gc) {
        switch (direction) {
            case 1:
                if (image > 2) {
                    image = 0;
                }
                gc.drawImage(super.getSprites().get(image), x, y, size, size);
                image++;
                break;
            case 2:
                if (image > 8 || image < 6) {
                    image = 6;
                }
                gc.drawImage(super.getSprites().get(image), x, y, size, size);
                image++;
                break;
            case 3:
                if (image > 11 || image < 9) {
                    image = 9;
                }
                gc.drawImage(super.getSprites().get(image), x, y, size, size);
                image++;
                break;
            default:
                if (image > 5 || image < 3) {
                    image = 3;
                }
                gc.drawImage(super.getSprites().get(image), x, y, size, size);
                image++;
                break;
        }
        Color color = Color.rgb(0, 255, 0);

        gc.setFill(color);
        int yAux = y + 1;
        switch (this.charge) {
            case 1:
                gc.fillRect(x, yAux, size / 5, 5);
                break;
            case 2:
                gc.fillRect(x, yAux, 2 * size / 5, 5);
                break;
            case 3:
                gc.fillRect(x, yAux, 3 * size / 5, 5);
                break;
            case 4:
                gc.fillRect(x, yAux, 4 * size / 5, 5);
                break;
            case 5:
                gc.fillRect(x, yAux, size, 5);
                break;
            default:
                break;
        }
    } // draw

} // end class
