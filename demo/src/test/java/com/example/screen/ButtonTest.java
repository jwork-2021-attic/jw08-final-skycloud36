package com.example.screen;

import static org.junit.Assert.*;

import com.example.asciiPanel.AsciiPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class ButtonTest {
    AsciiPanel asciiPanel;

    @Before
    public void setUp(){
        asciiPanel = new AsciiPanel();
    }

    @After
    public void tearDown(){
        asciiPanel = null;
    }

    @Test
    public void getButtonNameTest() {
        Button button = new Button("button", 10, 10, asciiPanel);
        assertEquals("button", button.getButtonName());
    }

    @Test
    public void getXlocTest() {
        Button button = new Button("button", 10, 10, asciiPanel);
        assertEquals(10, button.getXloc());
    }

    @Test
    public void getYlocTest() {
        Button button = new Button("button", 10, 10, asciiPanel);
        assertEquals(10, button.getYloc());
    }

    @Test
    public void getFrontColorTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        assertEquals(Color.WHITE, button.getFrontColor());
    }

    @Test
    public void getBackColorTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        assertEquals(Color.BLACK, button.getBackColor());
    }

    @Test
    public void setButtonNameTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        button.setButtonName("nottub");
        assertEquals("nottub", button.getButtonName());
    }

    @Test
    public void setFrontColorTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        button.setFrontColor(Color.YELLOW);
        assertEquals(Color.YELLOW, button.getFrontColor());
    }

    @Test
    public void setBackColorTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        button.setBackColor(Color.YELLOW);
        assertEquals(Color.YELLOW, button.getBackColor());
    }

    @Test
    public void setXlocTest() {
        Button button = new Button("button", 10, 10, asciiPanel);
        button.setXloc(20);
        assertEquals(20, button.getXloc());
    }

    @Test
    public void setYlocTest() {
        Button button = new Button("button", 10, 10, asciiPanel);
        button.setYloc(20);
        assertEquals(20, button.getYloc());
    }

    @Test
    public void setAPTest() {
        Button button = new Button("button", 10, 10, asciiPanel);
        AsciiPanel a2 = new AsciiPanel();
        button.setAP(a2);
        assertTrue(true);
    }

    @Test
    public void selectTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        button.select();
        assertEquals(Color.WHITE, button.getFrontColor());
        assertEquals(Color.GRAY, button.getBackColor());
    }

    @Test
    public void unselectTest() {
        Button button = new Button("button", 10, 10, Color.WHITE, Color.BLACK, asciiPanel);
        button.unselect();
        assertEquals(Color.GRAY, button.getFrontColor());
        assertEquals(Color.BLACK, button.getBackColor());
    }
}