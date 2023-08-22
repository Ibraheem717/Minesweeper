package org.example;

import org.junit.jupiter.api.function.Executable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import static org.example.Main.openMainMenu;

public class Board {

    private Block [][] board;
    private Mine [] mines;
    private int safeSquares;
    private int numberOfMines;
    private  boolean win;
    private boolean firstmove = true;
    private int length;
    private int width;
    private boolean gameOver;
    private Set<Block> arr = new HashSet<Block>();
    private Set<Block> flagged = new HashSet<Block>();
    private Iterator<Block> flaggedIterator;
    private JFrame frame = new JFrame();
    private JPanel jp;
    private Font font;
    private MouseAdapter firstMouse;
    private MouseAdapter mouse;

    public int getNumberOfMines() {return this.numberOfMines;}
    public Block [][] getBlock() {return this.board;}

    public Board(int size) {

        this.length = size;
        this.width = size;
        this.gameOver = false;
        board = new Block[length][width];

        for (int i = 0; i < length; i++)
            for (int j = 0; j < width; j++)
                board[i][j] = new Block(i, j);
    }

    public void GUIBoard() {
        this.frame = new JFrame();
        this.frame.setBounds(10, 10, (width*64)+20, (width*64)+45);
        this.font = new Font("TimesRoman", Font.PLAIN, 50);
        jp = new JPanel() {

            public void paint(Graphics g) {
                int transparancy = 250;
                for (int i = 0; i < length; i++){
                    for (int j = 0; j < length; j++) {
                        g.setFont(font);
                        if (gameOver)
                            transparancy = 50;
                        if (board[i][j].getVisible()) {
                            switch (board[i][j].getValue()) {
                                case (1) -> g.setColor(new Color(0,255,0, transparancy));
                                case (2) -> g.setColor(new Color(255, 165, 0, transparancy));
                                case (3) -> g.setColor(new Color(255, 0, 0, transparancy));
                                case (4) -> g.setColor(new Color(	128, 0, 128, transparancy));
                                default -> g.setColor(new Color(0,0,0, transparancy));
                            }
                            g.drawString(board[i][j].getStringValue(), (j * 64 + 20), ((i + 1) * 64 - 15));
                        }
                        else {
                            if (board[i][j].getFlagged()) {
                                g.setColor(Color.red);
                                g.fillRect(j*64, i*64, 64, 64);
                            }

                        }
                        g.setColor(Color.BLACK);
                        g.drawRect(j*64, i*64, 64, 64);

                    }
                }
                if (gameOver) {
                    Font gameOverFont = new Font("Arial", Font.BOLD, length*5);
                    g.setFont(gameOverFont);
                    String gameOverMessage = (win ? "You Win" : "You Lose");
                    String returnMessage = "Click anywhere on the board to return";
                    int gameMessageWidth = getWidth()/2 - (g.getFontMetrics().stringWidth(gameOverMessage)/2);
                    int gameMessageHeight = getWidth()/2 - (g.getFontMetrics().getHeight()/2);
                    g.drawString(gameOverMessage, gameMessageWidth, gameMessageHeight);
                    g.drawString(returnMessage, getWidth()/2 - (g.getFontMetrics().stringWidth(returnMessage)/2), gameMessageHeight+g.getFontMetrics().getHeight());
                }
            }
        };


        this.frame.add(jp);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void displayBoard() {

        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }

    }

    private boolean checkFlagged() {
        Block iteratedValue;
        flaggedIterator = flagged.iterator();
        while (flaggedIterator.hasNext()) {
            iteratedValue = flaggedIterator.next();
            if (iteratedValue.getValue()!=99) {
                return false;
            }
        }
        return true;
    }
    public void regularMouse() {
        mouse = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;

                if (x<length && y<width) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        board[y][x].exposeCell();
                        if (board[y][x].getMine()) {
                            win = false;
                            endGame();
                        }
                        if (board[y][x].getValue()==0)
                            exposeArea(board[y][x]);
                        arr.add(board[y][x]);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        System.out.println(arr.size());
                        if (!arr.contains(board[y][x])) {
                            System.out.println(flagged.size());
                            if (flagged.size() < numberOfMines) {
                                System.out.println(board[y][x].getFlagged());
                                if (!board[y][x].getFlagged()) {
                                    flagged.add(board[y][x]);
                                    board[y][x].setFlagged(true);
                                } else {
                                    flagged.remove(board[y][x]);
                                    board[y][x].setFlagged(false);
                                }
                            } else {
                                flagged.remove(board[y][x]);
                                board[y][x].setFlagged(false);
                            }
                        }
                    }
                    frame.repaint();


                    System.out.println(arr.size());
                    if (arr.size() == safeSquares) {
                        System.out.println("john");
                        win = true;
                        endGame();
                    } else if (flagged.size() == numberOfMines) {
                        if (checkFlagged()) {
                            win = true;
                            endGame();
                        }
                    }
                }

            }
        };

        this.jp.addMouseListener(mouse);
    }

    private void endGame() {
        arr.clear();
        this.gameOver = true;
        this.font = new Font("TimesRoman", Font.PLAIN, 50);
        System.out.println("gg");
        jp.removeMouseListener(mouse);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j].exposeCell();
            }
        }
        jp.repaint();

        JButton returnButton = new JButton();
        returnButton.setBounds(0,0,(width*64)+20, (width*64)+45);
        returnButton.setOpaque(false);
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                openMainMenu();
            }
        });
        this.jp.add(returnButton);
    }

    public void setUpMines(int numberOfMines) {
        Random rand = new Random();
        this.numberOfMines = numberOfMines;
        this.safeSquares = (this.length *  this.width) - numberOfMines;
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
        for (Mine mine : mines) {
            mine.check();
        }
        for (Mine mine : mines) {
            for (int i = mine.getX()-1; i < mine.getX()+2; i++) {
                for (int j = mine.getY()-1; j < mine.getY()+2; j++) {
                    try {
                        board[i][j].increaseValue();
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        assert true;
                    }
                }
            }
        }
    }
    public void testExposeArea(int x, int y) {
        exposeArea(board[x][y]);
    }

    // Reveals 0 -- reveal adjacent (up, down, left right) tiles until u hit a number
    private void exposeArea(Block start) throws ArrayIndexOutOfBoundsException {
        for (int i = start.getX()-1; i < start.getX()+2; i++) {
            for (int j = start.getY()-1; j < start.getY()+2; j++) {
                try {
                    if (arr.contains(board[i][j]))
                        continue;
                    if (Math.sqrt(Math.pow(start.getX()-board[i][j].getX(), 2)) +(Math.pow(start.getY()-board[i][j].getY(),2))==2)
                        continue;
                    if (board[i][j].getValue()!=99) {
                        if (board[i][j].getValue() > 0) {
                            arr.add(board[i][j]);
                            board[i][j].exposeCell();
                        } else {
                            arr.add(board[i][j]);
                            board[i][j].exposeCell();
                            exposeArea(board[i][j]);
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw e;
                }
            }
        }
    }

}
