package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        openMainMenu();

    }

    static void startGame(int values, int mines, JFrame boardX) {
        boardX.dispose();
        Board board = new Board(values);
        board.setUpMines(mines);
        board.setUpValues();
        board.displayBoard();
        board.GUIBoard();
        board.regularMouse();
    }

    public static void openMainMenu() {
        JFrame jf = new JFrame();
        int height = (10*64)+20;
        int width = (10*64)+45;
        jf.setBounds(10, 10, (height), (width));

        String[] difficulty = {"Easy", "Medium", "Hard"};
        JComboBox<String> selectDifficulty = new JComboBox<>(difficulty);
        selectDifficulty.setBounds((width*33/100), (height*30/100), 200, 30);

        JButton startButton = new JButton("Start Game");
        startButton.setBounds((width*40/100),(height*40/100),100,50);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (String.valueOf(selectDifficulty.getSelectedItem())) {
                    case ("Easy") -> startGame(5, 5, jf);
                    case ("Medium") -> startGame(10, 20, jf);
                    default ->  startGame(20, 80, jf);
                }
            }
        });


        jf.add(selectDifficulty);
        jf.add(startButton);
        jf.setLayout(null);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}