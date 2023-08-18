package org.example;

public class Mine extends Block{


    public Mine(int x, int y) {
        super(x, y);
    }

    public String toString() {
        return ( "█" );
    }

    public String temp() {
        return ( "█" );
    }

    public void clearValue() {
        this.value=100;
    }

}
