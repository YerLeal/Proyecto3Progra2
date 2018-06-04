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

    public FastCharacter(int size, SharedBuffer buffer,ArrayList<Block> finish) {
        super(size, buffer,finish);
        super.speed = 2;
        super.tipo = "FA";
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
                 super.isFinish();
            }
        }
    }

public void addSprites(){
       
        for(int i=0;i<12;i++){
            Image image = new Image(
                "assets/fa"+i+".png"
        );

        ImageView imageView = new ImageView(image);
        imageView.setClip(new ImageView(image));

        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1.0);
        imageView.setStyle("-fx-background-color: transparent");
        Blend blush = new Blend(
                BlendMode.MULTIPLY,
                monochrome,
                new ColorInput(
                        0,
                        0,
                        imageView.getImage().getWidth(),
                        imageView.getImage().getHeight(),
                        Color.ALICEBLUE
                )
        );
        imageView.setEffect(blush);
        super.setSprites(imageView.snapshot(new SnapshotParameters(), null));
        }
    }
    private int image=0;
    @Override
    public void draw(GraphicsContext gc) {
        switch (direction) {
            case 1:
                if(image>2){
                    image=0;
                }
                gc.drawImage(super.getSprites().get(image), x, y,size,size);
                image++;
                break;
            case 2:
                if(image>8 || image <6){
                    image=6;
                }
                gc.drawImage(super.getSprites().get(image), x, y,size,size);
                image++;
                break;
            case 3:
                if(image>11 || image < 9){
                    image=9;
                }
                gc.drawImage(super.getSprites().get(image), x, y,size,size);
                image++;
                break;
            default:
                if(image>5 || image<3){
                    image=3;
                }
                gc.drawImage(super.getSprites().get(image), x, y,size,size);
                image++;
                break;
        }

    } // draw

} // end class
