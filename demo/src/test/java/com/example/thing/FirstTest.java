package com.example.thing;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FirstTest {

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
    public void moveRandomTest() {
        First first = new First(world, 10, 10, CreatureAttribute.BLUETEAM);
        first.moveRandom();
        assertNotEquals(10 + 10, first.getX()+first.getY());
    }

    @Test
    public void moveByAITest() {
        World world1 = new World();
        First first = new First(world1, 10, 10, CreatureAttribute.BLUETEAM);
        First enemy = new First(world1, 10, 20, CreatureAttribute.REDTEAM);
        world1.addBlue(first);
        world1.addRed(enemy);
        first.moveByAI();
        assertEquals(enemy, first.getTarget());
        first.moveByAI();
        assertEquals(11, first.getY());
        world1 = null;
    }

    @Test
    public void moveWithHandleTest() {
        First first = new First(world, 10, 10, CreatureAttribute.BLUETEAM);
        first.moveWithHandle(1,0);
        assertEquals(11, first.getX());
        assertEquals(10, first.getY());
    }

    @Test
    public void changeGlyphTest() {
        First first = new First(world, 10, 10, CreatureAttribute.BLUETEAM);
        first.moveLeft();
        assertEquals((char)185, first.getGlyph());
    }

    @Test
    public void attackTest() {
        First first = new First(world, 10, 10, CreatureAttribute.REDTEAM);
        First enemy = new First(world, 20, 20, CreatureAttribute.BLUETEAM);
        first.Attack(enemy);
        assertEquals(160, enemy.getHP());
    }

    @Test
    public void beAttackedTest() {
        First first = new First(world, 10, 10, CreatureAttribute.BLUETEAM);
        First enemy = new First(world, 20, 20, CreatureAttribute.REDTEAM);
        first.beAttacked(enemy);
        assertEquals(160, first.getHP());
    }


    @Test
    public void getInfoTest() {
        First first = new First(world, 10, 10, CreatureAttribute.BLUETEAM, 40);
        String info = first.getInfo().get(0);
        assertEquals("First 10 10 BlueTeam 40 200", info);
    }
}