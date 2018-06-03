package domain;

import business.SharedBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class FuriousCharacter extends Character {
    
    public FuriousCharacter(int size, SharedBuffer buffer) {
        super(size, buffer);
        super.speed = 10;
        super.tipo = "F";
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
    } // run
    
    public void addSprites(){
       
        for(int i=0;i<12;i++){
            super.setSprites(new Image("assets/f"+i+".png"));
        }
    }
    private int image=0;
    @Override
    public void draw(GraphicsContext gc) {
        System.err.println(super.getSprites().size());
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
