package com.example.screen;

import static org.junit.Assert.*;

import com.example.asciiPanel.AsciiFont;
import com.example.asciiPanel.AsciiPanel;
import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class StartScreenTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void respondToUserInput() {
        StartScreen ss = new StartScreen();
        ss.displayOutput(new AsciiPanel(World.WIDTH, World.HEIGHT + 5, AsciiFont.CP437_16x16));
        KeyEvent key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_ENTER, 'Z');
        Screen temp = ss.respondToUserInput(key);
        assertEquals(WorldScreen.class, temp.getClass());

        ss = new StartScreen();
        ss.displayOutput(new AsciiPanel(World.WIDTH, World.HEIGHT + 5, AsciiFont.CP437_16x16));
        key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_DOWN, 'Z');
        temp = ss.respondToUserInput(key);
        assertEquals(StartScreen.class, temp.getClass());
        key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_ENTER, 'Z');
        temp = ss.respondToUserInput(key);
        assertEquals(WorldScreen.class, temp.getClass());

//        ss = new StartScreen();
//        ss.displayOutput(new AsciiPanel(World.WIDTH, World.HEIGHT + 5, AsciiFont.CP437_16x16));
//        key = new KeyEvent(new Component() {},
//                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
//                KeyEvent.VK_DOWN, 'Z');
//        temp = ss.respondToUserInput(key);
//        assertEquals(StartScreen.class, temp.getClass());
//        key = new KeyEvent(new Component() {},
//                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
//                KeyEvent.VK_DOWN, 'Z');
//        temp = ss.respondToUserInput(key);
//        assertEquals(StartScreen.class, temp.getClass());
//        key = new KeyEvent(new Component() {},
//                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
//                KeyEvent.VK_ENTER, 'Z');
//        temp = ss.respondToUserInput(key);
//        assertEquals(WorldScreen.class, temp.getClass());

        ss = new StartScreen();
        ss.displayOutput(new AsciiPanel(World.WIDTH, World.HEIGHT + 5, AsciiFont.CP437_16x16));
        key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_DOWN, 'Z');
        temp = ss.respondToUserInput(key);
        assertEquals(StartScreen.class, temp.getClass());
        key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_DOWN, 'Z');
        temp = ss.respondToUserInput(key);
        assertEquals(StartScreen.class, temp.getClass());
        key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_DOWN, 'Z');
        temp = ss.respondToUserInput(key);
        assertEquals(StartScreen.class, temp.getClass());
        key = new KeyEvent(new Component() {},
                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_ENTER, 'Z');
        temp = ss.respondToUserInput(key);
        assertEquals(ReplayScreen.class, temp.getClass());

    }
}