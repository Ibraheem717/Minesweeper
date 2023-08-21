package org.example;

public class Mine extends Block{

    public String getStringValue(){return "!";}

    public Mine(int x, int y) {
        super(x, y);
        this.mine=true;
        this.value=100;
    }

    public String toString() {
        return ( "█" );
    }
    public int getValue(){return 99;}


    public void check(){
        System.out.println(x + ", " + y + " " + this.mine);
    }
    public String temp() {
        return ( "█" );
    }


}
