package com.example.thing;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BulletTest {

    Second second;
    World world;
    Bullet bullet;
    @Before
    public void setUp() throws Exception {
        world = new World();
        second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
    }

    @After
    public void tearDown() throws Exception {
        world = null;
        second = null;
        bullet = null;
    }

    @Test
    public void testMoveDirect() {
        bullet = new Bullet(second, 1, 0);
        bullet.moveDirect();
        assertEquals(11, bullet.getX());
        assertEquals(10, bullet.getY());
    }

    @Test
    public void testGetOwner() {
        second = new Second(world, 10, 10, CreatureAttribute.REDTEAM);
        bullet = new Bullet(second, 1, 0);
        assertEquals(second, bullet.getOwner());
    }

    @Test
    public void testMoveWithHandle() {
        bullet = new Bullet(second, 1, 0);
        bullet.moveWithHandle(0,1);
        assertEquals(10,bullet.getX());
        assertEquals(11,bullet.getY());
    }

    @Test
    public void testAttack() {
        bullet = new Bullet(second, 1, 0);
        First first = new First(world, 15, 15, CreatureAttribute.REDTEAM);
        bullet.Attack(first);
        assertEquals(first.getMaxHP()-bullet.getATK(), first.getHP());
    }

    @Test
    public void testBeAttacked() {
        bullet = new Bullet(second, 1, 0);
        First first = new First(world, 15, 15, CreatureAttribute.REDTEAM);
        bullet.beAttacked(first);
        assertEquals(false, bullet.ifExist());
    }

}