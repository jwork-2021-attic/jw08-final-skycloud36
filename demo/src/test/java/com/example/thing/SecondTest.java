package com.example.thing;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SecondTest {
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
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        second.moveRandom();
        assertNotEquals(10 + 10, second.getX()+second.getY());
    }

    @Test
    public void moveByAITest() {
        World world1 = new World();
        Second second = new Second(world1, 10, 10, CreatureAttribute.BLUETEAM);
        Second enemy = new Second(world1, 11, 20, CreatureAttribute.REDTEAM);
        world1.addBlue(second);
        world1.addRed(enemy);
        second.moveByAI();
        assertEquals(enemy, second.getTarget());
        second.moveByAI();
        assertEquals(11, second.getX());
        world1 = null;
    }

    @Test
    public void moveWithHandleTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        second.moveWithHandle(1,0);
        assertEquals(11, second.getX());
        assertEquals(10, second.getY());
    }

    @Test
    public void addBulletTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        Bullet bullet = new Bullet(second, 1, 0, 10);
        second.addBullet(bullet);
        assertEquals(bullet, second.findBullet(10));
    }

    @Test
    public void findBulletTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        Bullet bullet = new Bullet(second, 1, 0, 10);
        second.addBullet(bullet);
        assertEquals(bullet, second.findBullet(10));
    }

    @Test
    public void changeGlyphTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        second.moveLeft();
        assertEquals((char)27, second.getGlyph());
    }

    @Test
    public void attackTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        second.moveLeft();
        second.Attack();
        assertNull(second.findBullet(0));
    }

    @Test
    public void beAttackedTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM);
        First enemy = new First(world, 20, 20, CreatureAttribute.REDTEAM);
        second.beAttacked(enemy);
        assertEquals(60, second.getHP());
    }

    @Test
    public void timeToAttack() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM, 10);
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(true,second.timeToAttack());
    }

    @Test
    public void getInfoTest() {
        Second second = new Second(world, 10, 10, CreatureAttribute.BLUETEAM, 40);
        String info = second.getInfo().get(0);
        assertEquals("Second 10 10 BlueTeam 40 100", info);
    }
}