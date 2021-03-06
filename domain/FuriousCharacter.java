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

public class FuriousCharacter extends Character {

    private int image = 0;

    public FuriousCharacter(int size, SharedBuffer buffer, ArrayList<Block> finish, String name,String color) {
        super(size, buffer, finish, name,color);
        super.speed = 10;
        super.type = "F";
        addSprites();
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
                        Logger.getLogger(SmartCharacter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                super.isFinish();
            }
        }
    } // run

    public void addSprites() {
        SnapshotParameters parameters = new SnapshotParameters();
        for (int i = 0; i < 12; i++) {
            Image image = new Image(
                    "assets/f" + i + ".png"
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
                            super.color));
            imageView.setEffect(blush);
            parameters.setFill(Color.TRANSPARENT);
            super.setSprites(imageView.snapshot(parameters, null));
        }
    } // addSprites

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
    } // draw

} // end class
