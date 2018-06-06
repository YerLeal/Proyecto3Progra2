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

public class SmartCharacter extends Character {
    
    private int image = 0;

    public SmartCharacter(int size, SharedBuffer buffer, ArrayList<Block> finish, String name) {
        super(size, buffer, finish, name);
        super.speed = 6;
        super.type = "S";
        addSprites();
    } // constructor

    @Override
    public void run() {
        while (super.getFlag()) {
            this.direction = (int) (Math.random() * (5 - 1) + 1);
            if (next(direction)) {
                this.crash = false;
                try {
                    switch (this.direction) {
                        case 1:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && !this.crash) {
                                Thread.sleep(this.speed);
                                this.buff.colisionVs(this.order);
                                this.y += this.movement;
                            }
                            break;
                        case 2:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && !this.crash) {
                                Thread.sleep(this.speed);
                                this.buff.colisionVs(this.order);
                                this.x += this.movement;
                            }
                            break;
                        case 3:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && !this.crash) {
                                Thread.sleep(this.speed);
                                this.buff.colisionVs(this.order);
                                this.y -= this.movement;
                            }
                            break;
                        case 4:
                            while (this.currentBlock.isInTheBlock(this.x, this.y) && !this.crash) {
                                Thread.sleep(this.speed);
                                this.buff.colisionVs(this.order);
                                this.x -= this.movement;
                            }
                            break;
                    } // switch (direction)
                } catch (InterruptedException ex) {
                    Logger.getLogger(Character.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                if (!this.crash) {
                    metodoRandom(this.direction);
                    this.currentBlock = this.nextBlock;
                } else {

                    try {
                        repositioning();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SmartCharacter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                super.isFinish();
            } // if (next(direction))
        } // while (super.getFlag())
    } // run

    public void addSprites() {
        SnapshotParameters parameters = new SnapshotParameters();
        for (int i = 0; i < 12; i++) {
            Image image = new Image(
                    "assets/s" + i + ".png"
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
                            Color.YELLOWGREEN));
            imageView.setEffect(blush);
            parameters.setFill(Color.TRANSPARENT);
            super.setSprites(imageView.snapshot(parameters, null));
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        switch (this.direction) {
            case 1:
                if (this.image > 2) {
                    this.image = 0;
                }
                gc.drawImage(super.getSprites().get(this.image), this.x, this.y, this.size, this.size);
                this.image++;
                break;
            case 2:
                if (this.image > 8 || this.image < 6) {
                    this.image = 6;
                }
                gc.drawImage(super.getSprites().get(this.image), this.x, this.y, this.size, this.size);
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
                gc.drawImage(super.getSprites().get(image), this.x, this.y, this.size, this.size);
                image++;
                break;
        }
    } // draw

} // end class
