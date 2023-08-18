package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Board {

    private Block [][] board;
    private Mine [] mines;
    private int length;
    private int width;
    Set<Block> arr = new HashSet<Block>();
    JFrame frame = new JFrame();
    JPanel jp;

    public Board(int length, int width) {

        this.length = length;
        this.width = width;
        board = new Block[length][width];

        for (int i = 0; i < length; i++)
            for (int j = 0; j < width; j++)
                board[i][j] = new Block(i, j);

    }

    public void GUIBoard() {
        frame = new JFrame();
        frame.setBounds(10, 10, 655, 680);

        jp = new JPanel() {

            public void paint(Graphics g) {
                for (int i = 0; i < board.length; i++){
                    for (int j = 0; j < board[0].length; j++) {
                        if (board[i][j].getVisible())
                            g.drawString(String.valueOf(board[i][j].getValue()), j*64, i*64 );
                        g.drawRect(j*64, i*64, 64, 64);

                    }
                }

            }
        };

        frame.add(jp);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void displayBoard() {
        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }

        for (Block block : arr) {

        }
    }

    public void temp() {
        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print(this.board[i][j].temp());
            }
            System.out.println();
        }
    }

    public void setUpMines(int numberOfMines) {
        Random rand = new Random();
        mines = new Mine[numberOfMines];
        ArrayList<Integer> numberlist = new ArrayList<Integer>();
        int temp;
        for (int i = 0; i < numberOfMines; i++) {
            do {
                temp = rand.nextInt(1, length * length);
            }
            while (numberlist.contains(temp));
            numberlist.add(temp);
            mines[i] = new Mine(temp/length, temp%width);
            board[mines[i].getX()][mines[i].getY()] = mines[i];
        }
    }

    public void setUpValues() {
        int counter = 0;
        for (Mine mine : mines) {
            for (int i = mine.getX()-1; i < mine.getX()+2; i++) {
                for (int j = mine.getY()-1; j < mine.getY()+2; j++) {
                    try {
                        board[i][j].increaseValue();
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        counter++;
                    }
                }
            }
            mine.clearValue();
        }
    }

    public void findStart() {
        Random rand = new Random();
        int temp;
        Block start;
        boolean findNew;
        do {
            findNew=false;
            temp = rand.nextInt(1,length*width);
            start = board[temp/length][temp%width];
            if (start.getValue()>0)
                findNew=true;
        }
        while (findNew);
        System.out.println(start.getX());
        System.out.println(start.getY());

        arr.add(start);
        exposeArea(start);
        start.exposeCell();
    }

    private void exposeArea(Block start){
        for (int i = start.getX()-1; i < start.getX()+2; i++) {
            for (int j = start.getY()-1; j < start.getY()+2; j++) {
                try {
                    if (arr.contains(board[i][j]))
                        continue;
                    if (board[i][j].getValue()>0)
                        board[i][j].exposeCell();
                    else {
                        arr.add(board[i][j]);
                        board[i][j].exposeCell();
                        exposeArea(board[i][j]);
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    assert true;
                }
            }
        }
    }

}
