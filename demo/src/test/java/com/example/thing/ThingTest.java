package com.example.thing;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class ThingTest {

    World world;
    @Before
    public void setUp() throws Exception {
        world = new World();
    }

    @After
    public void tearDown() throws Exception {
        world = null;
    }

    @Test
    public void setxPos() {
        Tile t = new Tile(5, 5);
        Thing thing = new Thing(Color.LIGHT_GRAY,(char)1,world);
        thing.setTile(t);
        thing.setxPos(10);
        assertEquals(10, thing.getX());
        t = null;
    }

    @Test
    public void setyPos() {
        Tile t = new Tile(5, 5);
        Thing thing = new Thing(Color.LIGHT_GRAY,(char)1,world);;
        thing.setTile(t);
        thing.setyPos(10);
        assertEquals(10, thing.getY());
        t = null;
    }
}