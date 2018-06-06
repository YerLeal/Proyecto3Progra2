package domain;

import business.SharedBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Character extends Thread {

    protected SharedBuffer buff;
    protected int xPos, yPos, x, y, size, speed, movement, order, action;
    protected Block currentBlock, nextBlock, auxBlock, starto;
    protected ArrayList<Block> finish;
    protected int direction, directionAux;
    protected boolean crash = false, ini = false;
    protected String type, name;
    private Boolean flag = true;
    private ArrayList<Image> spritesArray;
    protected Color color;

    public Character(int size, SharedBuffer buffer, ArrayList<Block> finish, String nombre, String colorN) {
        this.size = size;
        this.buff = buffer;
        this.spritesArray = new ArrayList<>();
        this.finish = finish;
        this.name = nombre;
        setColor(colorN);
        movement=1;
    } // constructor

    public void setColor(String color) {
        switch (color) {
            case "GREEN":
                this.color = Color.GREEN;
                break;
            case "BLUE":
                this.color = Color.BLUE;
                break;
            case "AQUA":
                this.color = Color.AQUA;
                break;
            case "RED":
                this.color = Color.RED;
                break;
            case "YELLOW":
                this.color = Color.YELLOW;
                break;
            case "BLUEVIOLET":
                this.color = Color.BLUEVIOLET;
                break;
            case "CORAL":
                this.color = Color.CORAL;
                break;
            case "MAGENTE":
                this.color = Color.MAGENTA;
                break;
            case "LIGHTSEAGREEN":
                this.color = Color.LIGHTGREEN;
                break;
        }
    }

    public void setStarto(Block starto) {
        this.xPos = starto.getX();
        this.yPos = starto.getY();
        this.x = this.xPos * this.size;
        this.y = this.yPos * this.size;
        this.starto = starto;
        this.currentBlock = starto;
    } // setStarto

    public ArrayList<Image> getSprites() {
        return this.spritesArray;
    } // getSprites
    
    public void pause(){
        try {
            this.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Character.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSprites(Image sprites) {
        this.spritesArray.add(sprites);
    } // setSprites

    public int getOrder() {
        return this.order;
    } // getOrder

    public void setOrder(int order) {
        this.order = order;
    } // setOrder

    public Block getStarto() {
        return this.starto;
    } // getStarto

    public void setIni(boolean ini) {
        this.ini = ini;
    }

    public Boolean getFlag() {
        return this.flag;
    } // getFlag

    public int getSize() {
        return this.size;
    } // getSize

    public void setFlag(Boolean flag) {
        this.flag = flag;
    } // setFlag

    public int getDirection() {
        return this.direction;
    } // getDirection

    public SharedBuffer getBuff() {
        return this.buff;
    } // getBuff

    public int getXPos() {
        return this.xPos;
    } // getXPos

    public void setXPos(int xPos) {
        this.xPos = xPos;
    } // setXPos

    public int getYPos() {
        return this.yPos;
    } // getYPos

    public void setYPos(int yPos) {
        this.yPos = yPos;
    } // setYPos

    public int getSpeed() {
        return this.speed;
    } // getSpeed

    public void setSpeed() {
        this.speed -= 1;
    } // setSpeed

    public void setMovement(int movement) {
        this.movement = movement;
    } // setMovement

    public Block getCurrentBlock() {
        return this.currentBlock;
    } // getCurrentBlock

    public boolean isCrash() {
        return this.crash;
    } // isCrash

    public void setCrash(boolean crash) {
        this.crash = crash;
    } // setCrash

    public int getX() {
        return x;
    } // getX

    public int getY() {
        return y;
    } // getY

    public void setY(int y) {
        this.y = y;
    } // setY

    public boolean next(int dir) {
        if (((dir == 1 && directionAux == 3) || (directionAux == 1 && dir == 3)) && !isCaught() && !crash) {
            return false;
        } else if (((dir == 2 && directionAux == 4) || (directionAux == 2 && dir == 4)) && !isCaught() && !crash) {
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
    } // next: retorna true si el bloque al que intento avanzar
    // es un movimiento valido

    public void changePositionInMatrix(int direction) {
        int aux;
        if (direction == 1 || direction == 2) {
            aux = 1;
        } else {
            aux = -1;
        }
        if (direction == 1 || direction == 3) {
            this.yPos += aux;
        } else {
            this.xPos += aux;
        }
        this.directionAux = direction;
    } // cambia mi posicion a nivel de matriz

    public boolean isCaught() {
        int direction1 = oppositeDirection(this.directionAux);
        int aux;
        if (direction1 == 1 || direction1 == 2) {
            aux = 1;
        } else {
            aux = -1;
        }
        if (this.currentBlock.getNext().size() == 1) {
            if ((direction1 == 1 || direction1 == 3) && this.currentBlock.getNext().get(0).getY() == this.yPos + aux) {
                this.direction = direction1;
            } else if ((direction1 == 2 || direction1 == 4) && this.currentBlock.getNext().get(0).getX() == this.xPos + aux) {
                this.direction = direction1;
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

    public void collision() {
        this.action++;
    }

    public void repositioning() throws InterruptedException {
        while (crash) {
            int aux = action;
            if (action > 1) {
                auxBlock = currentBlock;
                currentBlock = nextBlock;
                nextBlock = auxBlock;
            }
            int xB = currentBlock.getX() * size;
            int yB = currentBlock.getY() * size;
            direction = oppositeDirection(direction);

            switch (direction) {
                case 1:
                    while (y < yB && aux == action) {
                        Thread.sleep(speed);
                        buff.colisionVs(order);
                        y += movement;
                    }
                    if (aux == action) {
                        crash = false;
                        xPos = currentBlock.getX();
                        yPos = currentBlock.getY();
                        action = 0;
                    }
                    break;
                case 2:
                    while (x < xB && aux == action) {
                        Thread.sleep(speed);
                        buff.colisionVs(order);
                        x += movement;
                    }
                    if (aux == action) {
                        crash = false;
                        xPos = currentBlock.getX();
                        yPos = currentBlock.getY();
                        action = 0;
                    }
                    break;
                case 3:
                    while (y > yB && aux == action) {
                        Thread.sleep(speed);
                        buff.colisionVs(order);
                        y -= movement;

                    }
                    if (aux == action) {
                        crash = false;
                        xPos = currentBlock.getX();
                        yPos = currentBlock.getY();
                        action = 0;
                    }
                    break;
                case 4:
                    while (x > xB && aux == action) {
                        Thread.sleep(speed);
                        buff.colisionVs(order);
                        x -= movement;
                    }
                    if (aux == action) {
                        crash = false;
                        xPos = currentBlock.getX();
                        yPos = currentBlock.getY();
                        action = 0;
                    }
                    break;
            }
            directionAux = direction;
        }
    }

    public void isFinish() {
        for (int i = 0; i < finish.size(); i++) {
            if (currentBlock.getX() == finish.get(i).getX() && currentBlock.getY() == finish.get(i).getY()) {
                buff.addFinisher(new Record(name, type));
                this.flag = false;
            }
        }
    }

    public String getType() {
        return type;
    }

    public abstract void draw(GraphicsContext gc);

} // end class
