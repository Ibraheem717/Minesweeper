package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    @DisplayName("Hidden value")
    void testToString() {
        Block block = new Block(5,5);
        assertAll("Should return value depending on visibility",
                () -> assertEquals("█", block.toString()),
                () -> assertNotEquals("▢", block.toString()),
                () -> assertNotEquals("1", block.toString()),
                () -> assertNotEquals("2", block.toString()),
                () -> assertNotEquals("3", block.toString()),
                () -> assertNotEquals("4", block.toString()),
                () -> assertNotEquals("5", block.toString()),
                () -> assertNotEquals("6", block.toString()),
                () -> assertNotEquals("7", block.toString()),
                () -> assertNotEquals("8", block.toString()),
                () -> assertNotEquals("9", block.toString())
        );
    }

    @Test
    @DisplayName("Expose Tile")
    void testHideCell() {
        Block block = new Block(5,5);
        block.exposeCell();
        assertTrue(block.getVisible());
    }
}