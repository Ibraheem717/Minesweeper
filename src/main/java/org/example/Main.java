package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Board board = new Board(10,10);

        board.setUpMines(25);
        board.setUpValues();
        board.findStart();
        board.displayBoard();
        board.GUIBoard();
    }
}