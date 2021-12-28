package com.example.thing;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class FloorTest {
    World world;
    Floor floor;
    @Before
    public void setUp(){
        world = new World();
        floor = new Floor(world);
    }

    @After
    public void tearDown(){
        world = null;
        floor = null;
    }


    @Test
    public void testBuild(){
        assertEquals("Floor", floor.name);
        assertEquals(Color.gray, floor.getColor());
    }

}