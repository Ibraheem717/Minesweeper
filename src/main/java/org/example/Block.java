package org.example;

public class Block {

    protected int x;
    protected int y;

    protected int value;
    protected boolean visible;
    protected boolean mine;
    private boolean flagged;

    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getValue(){return this.value;}
    public String getStringValue(){return String.valueOf(this.value);}
    public boolean getVisible() {return this.visible;}
    public boolean getMine() {return this.mine;}

    public boolean getFlagged() {  return flagged;}

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
        this.visible=false;
        this.mine=false;
    }

    public String toString() {
        if (!this.visible)
            return "█";
        return ( (this.value>0) ? String.valueOf(this.value) : "▢" );
    }
    public String temp() {
        return ( (this.value>0) ? String.valueOf(this.value) : "▢" );
    }
    
    public void increaseValue() {
        this.value++;
    }

    public void exposeCell(){
        this.visible = true;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

}
