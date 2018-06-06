package domain;

import business.SharedBuffer;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Character extends Thread {

    protected SharedBuffer buff;
    protected int xPos, yPos, x, y, size, speed, movement, order,action;
    protected Block currentBlock, nextBlock,auxBlock, starto;
    protected ArrayList<Block> finish;
    protected int direction, dirAux;
    protected boolean crash = false, ini = false;
    protected String type ,name;
    private Boolean flag = true;
    private ArrayList<Image> spritesArray;

    public Character(int size, SharedBuffer buffer,ArrayList<Block> finish, String nombre) {
        this.size = size;
        this.buff = buffer;
        this.spritesArray = new ArrayList<>();
        this.finish=finish;
        this.name=nombre;
    }

    public void setStarto(Block starto) {
        xPos = starto.getX();
        yPos = starto.getY();
        x = xPos * size;
        y = yPos * size;
        this.starto = starto;
        this.currentBlock = starto;

    }
    
    

    public ArrayList<Image> getSprites() {
        return spritesArray;
    }

    public void setSprites(Image sprites) {
        this.spritesArray.add(sprites);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isIni() {
        return ini;
    }

    public Block getStarto() {
        return starto;
    }

    public void setIni(boolean ini) {
        this.ini = ini;
    }

    public Boolean getFlag() {
        return flag;
    }

    public int getSize() {
        return size;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public int getDirection() {
        return direction;
    }

    public SharedBuffer getBuff() {
        return buff;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed() {
        this.speed -= 1;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public boolean isCrash() {
        return crash;
    }

    public void setCrash(boolean crash) {
        this.crash = crash;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean next(int dir) {
//        System.err.println(dir+ " "+ dirAux);
        if (((dir == 1 && dirAux == 3) || (dirAux == 1 && dir == 3)) && !isCaught() && !crash) {
            return false;
        } else if (((dir == 2 && dirAux == 4) || (dirAux == 2 && dir == 4)) && !isCaught() && !crash) {
            return false;
        }
        

        int aux;
        if (dir == 1 || dir == 2) {
            aux = 1;
        } else {
            aux = -1;
        }

        if (dir == 1 || dir == 3) {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getY() == yPos + aux) {
                    this.nextBlock = this.currentBlock.getNext().get(i);
                    return true;

                }
            }
        } else {
            for (int i = 0; i < this.currentBlock.getNext().size(); i++) {
                if (this.currentBlock.getNext().get(i).getX() == xPos + aux) {

                    this.nextBlock = this.currentBlock.getNext().get(i);
                    return true;

                }
            }
        }
        return false;
    }

    public void metodoRandom(int dir) {
        int aux;
        if (dir == 1 || dir == 2) {
            aux = 1;
        } else {
            aux = -1;
        }

        if (dir == 1 || dir == 3) {
            yPos += aux;
        } else {
            xPos += aux;
        }
        this.dirAux = dir;
    }

    public boolean isCaught() {
        int dir = oppositeDirection(dirAux);
        int aux;
        if (dir == 1 || dir == 2) {
            aux = 1;
        } else {
            aux = -1;
        }
        if (this.currentBlock.getNext().size() == 1) {
            if ((dir == 1 || dir == 3) && this.currentBlock.getNext().get(0).getY() == yPos + aux) {
                direction = dir;
            } else if ((dir == 2 || dir == 4) && this.currentBlock.getNext().get(0).getX() == xPos + aux) {
                direction = dir;
            }
            return true;
        }
        return false;
    }

    public int oppositeDirection(int dir) {
        switch (dir) {
            case 1:
                return 3;

            case 2:
                return 4;

            case 3:
                return 1;

            default:
                return 2;
        }
    }

    public void collision(){
        this.action++;
    }
    
    public void repositioning() throws InterruptedException {
        while (crash) {
        int aux=action;
        if(action>1){
            auxBlock=currentBlock;
            currentBlock=nextBlock;
            nextBlock=auxBlock;
        }
        int xB = currentBlock.getX() * size;
        int yB = currentBlock.getY() * size;
        direction = oppositeDirection(direction);
        
        switch (direction) {
            case 1:
                while (y < yB && aux==action) {
                    Thread.sleep(speed);
                    buff.colisionVs(order);
                    y += movement;

                }
                if(aux==action){
                    crash=false;
                    xPos=currentBlock.getX();
                    yPos=currentBlock.getY();
                    action=0;
                }
                break;
            case 2:
                while (x < xB && aux==action) {
                    Thread.sleep(speed);
                    buff.colisionVs(order);
                    x += movement;

                }
                if(aux==action){
                    System.err.println("Entra?"+order);
                    crash=false;
                    xPos=currentBlock.getX();
                    yPos=currentBlock.getY();
                    action=0;
                }
                break;
            case 3:
                while (y > yB && aux==action) {
                    Thread.sleep(speed);
                    buff.colisionVs(order);
                    y -= movement;

                }
                if(aux==action){
                    crash=false;
                    xPos=currentBlock.getX();
                    yPos=currentBlock.getY();
                    action=0;
                }
                break;
            case 4:
                while (x > xB && aux==action) {
                    Thread.sleep(speed);
                    buff.colisionVs(order);
                    x -= movement;
                }
                if(aux==action){
                    crash=false;
                    xPos=currentBlock.getX();
                    yPos=currentBlock.getY();
                    action=0;
                }
                break;
        }
        dirAux = direction;
        }
    }
    
    public void isFinish(){
        for(int i=0;i<finish.size();i++){
            if(currentBlock.getX()==finish.get(i).getX() && currentBlock.getY()==finish.get(i).getY()){
                buff.addFinisher(new Record(name, type));
                this.flag=false;
            }
        }
    }

    public String getType() {
        return type;
    }

    public abstract void draw(GraphicsContext gc);

} // end class
