package com.example.screen;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReplayScreenTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void ifNoFileTest() {
        try {
            World world = new World("GameMap.txt");
            BufferedReader bf = new BufferedReader(new FileReader("GameProcess.txt"));
            assertNotNull(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReplayScreen rs = new ReplayScreen();
    }

    @Test
    public void respondToUserInput() {
        ReplayScreen rs = new ReplayScreen();
        KeyEvent key = new KeyEvent(new Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'Z');
        rs.respondToUserInput(key);
        assertTrue(true);
    }

    @Test
    public void finish() {
    }
}