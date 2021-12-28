package com.example.thing;

import static org.junit.Assert.*;

import com.example.asciiPanel.AsciiPanel;
import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WallTest {
    World world;
    Wall wall;
    @Before
    public void setUp(){
        world = new World();
        wall = new Wall(world);
    }

    @After
    public void tearDown(){
        world = null;
        wall = null;
    }

    @Test
    public void testBuild(){
        assertEquals("Wall", wall.name);
        assertEquals(AsciiPanel.cyan, wall.getColor());
    }

}