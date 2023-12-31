package org.example;
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
    private int length;
    private int width;
    private boolean gameOver;
    private Set<Block> arr = new HashSet<>();
    private Set<Block> flagged = new HashSet<>();
    private JFrame frame = new JFrame();
    private JPanel jp;
    private Font font;
    private MouseAdapter mouse;
    private Random rand = new Random();

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

            private void decideFlagged(Graphics g, boolean flag, int i, int j) {
                if (flag) {
                    g.setColor(Color.red);
                    g.fillRect(j*64, i*64, 64, 64);
                }
            }
            private void decideColour(Graphics g, int transparancy, int value) {
                switch (value) {
                    case (1) -> g.setColor(new Color(0,255,0, transparancy));
                    case (2) -> g.setColor(new Color(255, 165, 0, transparancy));
                    case (3) -> g.setColor(new Color(255, 0, 0, transparancy));
                    case (4) -> g.setColor(new Color(	128, 0, 128, transparancy));
                    default -> g.setColor(new Color(0,0,0, transparancy));
                }
            }

            private void drawTiles(Graphics g, int transparancy, int i, int j) {
                if (board[i][j].getVisible()) {
                    decideColour(g, transparancy, board[i][j].getValue());
                    g.drawString(board[i][j].getStringValue(), (j * 64 + 20), ((i + 1) * 64 - 15));
                }
                else {
                    decideFlagged(g, board[i][j].getFlagged(), i, j);
                }
                g.setColor(Color.BLACK);
                g.drawRect(j*64, i*64, 64, 64);
            }

            private void gameOverMessage(Graphics g) {
                Font gameOverFont = new Font("Arial", Font.BOLD, length*5);
                g.setFont(gameOverFont);
                String gameOverMessage = (win ? "You Win" : "You Lose");
                String returnMessage = "Click anywhere on the board to return";
                int gameMessageWidth = getWidth()/2 - (g.getFontMetrics().stringWidth(gameOverMessage)/2);
                int gameMessageHeight = getWidth()/2 - (g.getFontMetrics().getHeight()/2);
                g.drawString(gameOverMessage, gameMessageWidth, gameMessageHeight);
                g.drawString(returnMessage, getWidth()/2 - (g.getFontMetrics().stringWidth(returnMessage)/2), gameMessageHeight+g.getFontMetrics().getHeight());

            }
            @Override
            public void paint(Graphics g) {
                int transparancy = (gameOver ? 50 : 250);
                for (int i = 0; i < length*width; i++){
                    g.setFont(font);
                    drawTiles(g, transparancy, i/length, i%width);
                }
                if (gameOver)
                    gameOverMessage(g);
            }
        };


        this.frame.add(jp);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void displayBoard() {

        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print(this.board[i][j].getValue());
            }
            System.out.println();
        }

    }
    private boolean checkFlagged() {
        Block iteratedValue;
        Iterator<Block> flaggedIterator = flagged.iterator();
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

            private boolean checkWin() {
                if (arr.size() == safeSquares)
                    return true;
                return flagged.size() == numberOfMines && checkFlagged();
            }

            private void revealTile(int x, int y) {

                board[y][x].exposeCell();
                if (board[y][x].getMine()) {
                    win = false;
                    endGame();
                }
                if (board[y][x].getValue()==0)
                    exposeArea(board[y][x]);
                arr.add(board[y][x]);
            }

            private void flagTile(int x, int y) {
                if (!arr.contains(board[y][x])) {
                    if (flagged.size() < numberOfMines) {
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

            private void decidePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1 -> revealTile(e.getX()/64, e.getY()/64);
                    case MouseEvent.BUTTON3 -> flagTile(e.getX()/64, e.getY()/64);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;

                if (x<length && y<width) {
                    decidePressed(e);
                    frame.repaint();
                }

                if (checkWin()) {
                    win = true;
                    endGame();
                }

            }
        };

        this.jp.addMouseListener(mouse);
    }

    private void endGame() {
        arr.clear();
        this.gameOver = true;
        this.font = new Font("TimesRoman", Font.PLAIN, 50);
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
        this.numberOfMines = numberOfMines;
        this.safeSquares = (this.length *  this.width) - numberOfMines;
        mines = new Mine[numberOfMines];
        ArrayList<Integer> numberlist = new ArrayList<Integer>();
        int temp;
        for (int i = 0; i < numberOfMines; i++) {
            do {
                temp = this.rand.nextInt(1, length * length);
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

    private void exposeArea(Block start) {
        for (int i = start.getX()-1; i < start.getX()+2; i++) {
            for (int j = start.getY()-1; j < start.getY()+2; j++) {
                try {
                    if (arr.contains(board[i][j]))
                        continue;
                    if (Math.sqrt(Math.pow((double)start.getX()-board[i][j].getX(), 2)) +
                            (Math.pow((double)start.getY()-board[i][j].getY(),2))
                            ==2)
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
                    assert true;
                }
            }
        }
    }

}
