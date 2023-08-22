package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    @DisplayName("Number of Mines")
    void setUpMines() {
        Board board = new Board(10);
        board.setUpMines(20);
        assertEquals(20, board.getNumberOfMines());
    }
    @Test
    @DisplayName("Throwable")
    void exposeArea() {
        Board board = new Board(10);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.testExposeArea(11,11));
    }
}