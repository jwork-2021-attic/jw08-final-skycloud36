package com.example;
import static org.junit.Assert.assertEquals;

import com.example.maze.World;
import com.example.screen.LoseScreen;
import com.example.screen.StartScreen;
import com.example.screen.WorldScreen;

import java.io.*;
import java.lang.reflect.Method;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// import javafx.scene.input.KeyEvent;

public class MainTest {
    Main app;

    @Before 
    public void initialize() {
        app= new Main();
    }

    @After
    public void finish(){
        app = null;
    }

    @Test
    public void testRepaint() {
        assertEquals("StartScreen",StartScreen.class, app.getScreen().getClass());
        WorldScreen worldScreen = new WorldScreen();
        app.screen = worldScreen;
        assertEquals("StartScreen",WorldScreen.class, app.getScreen().getClass());
        worldScreen.gameStart();
        app.repaint();
        assertEquals("StartScreen",LoseScreen.class, app.getScreen().getClass());
    }

    @Test
    public void testBuild(){
        int eh = (World.HEIGHT + 5) * app.terminal.getCharHeight();
        int rh = app.terminal.getHeight();
        int ew = World.WIDTH * app.terminal.getCharWidth();
        int rw = app.terminal.getWidth();
        assertEquals("HEIGHT" + eh  + " = " + rh, eh, rh);
        assertEquals("WIDTH" + ew  + " = " + rw, ew, rw);
    }
}
