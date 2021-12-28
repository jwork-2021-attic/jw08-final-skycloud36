package com.example.thing;

import static org.junit.Assert.*;

import com.example.asciiPanel.AsciiPanel;
import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class CreatureAttributeTest {
    CreatureAttribute cat;
    World world = new World();
    @Before
    public void setUp(){
        world = new World();
        cat = new CreatureAttribute(){};
    }

    @After
    public void tearDown(){
        world = null;
        cat = null;
    }

    @Test
    public void getTeam() {
        assertNull(cat.getTeam());
    }

    @Test
    public void getEnemyTeam() {
        assertNull(cat.getEnemyTeam());
    }

    @Test
    public void beAttacked() {
        Thing t = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.beAttacked(t);
        assertTrue(true);
    }

    @Test
    public void attack() {
        Thing t = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.Attack(t);
        assertTrue(true);
    }

    @Test
    public void testAttack() {
        Thing t = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.Attack();
        assertTrue(true);
    }

    @Test
    public void ifExist() {
        assertFalse(cat.ifExist());
    }

    @Test
    public void ifSelected() {
        assertFalse(cat.ifSelected());
    }

    @Test
    public void beDead() {
        cat.beDead();
        assertTrue(true);
    }

    @Test
    public void getHP() {
        assertEquals(0,cat.getHP());
    }

    @Test
    public void setHP() {
        cat.setHP(10);
        assertTrue(true);
    }

    @Test
    public void getDefence() {
        assertEquals(0, cat.getDefence());
    }

    @Test
    public void getATK() {
        assertEquals(0, cat.getATK());
    }

    @Test
    public void getMaxHP() {
        assertEquals(0,cat.getMaxHP());
    }

    @Test
    public void disPlayout() {
        cat.disPlayout(new AsciiPanel());
        assertTrue(true);
    }

    @Test
    public void getInfo() {
        assertNull(cat.getInfo());
    }

    @Test
    public void addBullet() {
        cat.addBullet(new Bullet(new Second(world, 10, 10, CreatureAttribute.REDTEAM), 1, 0));
        assertTrue(true);
    }

    @Test
    public void setCode() {
        cat.setCode(5);
        assertTrue(true);
    }

    @Test
    public void getCode() {
        assertEquals(0,cat.getCode());
    }

    @Test
    public void getOwner() {
        assertNull(cat.getOwner());
    }

    @Test
    public void findBullet() {
        assertNull(cat.findBullet(5));
    }

    @Test
    public void moveBy() {
        cat.moveBy(0, 0);
        assertTrue(true);
    }

    @Test
    public void moveReplay() {
        cat.moveReplay(0, 0);
        assertTrue(true);
    }

    @Test
    public void changeGlyph() {
        cat.changeGlyph();
        assertTrue(true);
    }

    @Test
    public void getTarget() {
        assertNull(cat.getTarget());
    }

    @Test
    public void processCreate() {
        Thing t = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.processCreate(t);
        assertTrue(true);
    }

    @Test
    public void processMove() {
        Thing t = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.processMove(t, 0, 0);
        assertTrue(true);
    }

    @Test
    public void processAttack() {
        Thing t1 = new Thing(Color.LIGHT_GRAY, (char)1, world);
        Thing t2 = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.processAttack(t1, t2);
        assertTrue(true);
    }

    @Test
    public void processBeAttack() {
        Thing t1 = new Thing(Color.LIGHT_GRAY, (char)1, world);
        Thing t2 = new Thing(Color.LIGHT_GRAY, (char)1, world);
        cat.processBeAttack(t1, t2);
        assertTrue(true);
    }
}