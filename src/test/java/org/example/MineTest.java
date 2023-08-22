package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MineTest {

    @Test
    @DisplayName("Mine value")
    void getValue() {
        Mine mine = new Mine(10,10);
        assertEquals(99, mine.getValue());
    }


}