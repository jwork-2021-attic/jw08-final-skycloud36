package com.example.thing;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class CreatureTest {
    Creature creature;
    World world;
    @Before
    public void setUp() throws Exception {
        world = new World();
    }

    @After
    public void tearDown() throws Exception {
        world = null;
        creature = null;
    }

    @Test
    public void moveToTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveTo(20,20);
        assertEquals(20,creature.getX());
        assertEquals(20,creature.getY());
    }

    @Test
    public void moveTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveBy(1,0);
        assertEquals(11,creature.getX());
        assertEquals(10,creature.getY());
    }

    @Test
    public void getXTest() {
        creature = new Creature(Color.LIGHT_GRAY, (char)1,world,CreatureAttribute.BLUETEAM);
        assertEquals(0,creature.getX());
    }

    @Test
    public void getYTest() {
        creature = new Creature((char)1,world,10,11,CreatureAttribute.BLUETEAM);
        assertEquals(11,creature.getY());
    }

    @Test
    public void setxPosTest() {
        creature = new Creature((char)1,world,10,11,CreatureAttribute.BLUETEAM);
        creature.setxPos(20);
        assertEquals(20,creature.getX());
    }

    @Test
    public void setyPosTest() {
        creature = new Creature((char)1,world,10,11,CreatureAttribute.BLUETEAM);
        creature.setyPos(20);
        assertEquals(20,creature.getY());
    }

    @Test
    public void moveByTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveBy(1,0);
        assertEquals(11,creature.getX());
        assertEquals(10,creature.getY());
    }

    @Test
    public void moveWithHandleTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.REDTEAM);
        creature.moveWithHandle(1,0);
        assertEquals(11,creature.getX());
        assertEquals(10,creature.getY());
    }

    @Test
    public void moveReplayTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveReplay(1,0);
        assertEquals(11,creature.getX());
        assertEquals(10,creature.getY());
        assertEquals(CreatureAttribute.RIGHT, creature.toward);
    }

    @Test
    public void moveUpTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveUp();
        assertEquals(10,creature.getX());
        assertEquals(9,creature.getY());
        assertEquals(CreatureAttribute.UP, creature.toward);
    }

    @Test
    public void moveDownTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveDown();
        assertEquals(10,creature.getX());
        assertEquals(11,creature.getY());
        assertEquals(CreatureAttribute.DOWN, creature.toward);
    }

    @Test
    public void moveRightTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveRight();
        assertEquals(11,creature.getX());
        assertEquals(10,creature.getY());
        assertEquals(CreatureAttribute.RIGHT, creature.toward);
    }

    @Test
    public void moveLeftTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.moveLeft();
        assertEquals(9,creature.getX());
        assertEquals(10,creature.getY());
        assertEquals(CreatureAttribute.LEFT, creature.toward);
    }

    @Test
    public void ifExistTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(true, creature.ifExist());
    }

    @Test
    public void timeToAttackTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(true,creature.timeToAttack());
    }

    @Test
    public void beDeadTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.beDead();
        assertEquals(false, creature.ifExist());
    }

    @Test
    public void getTeamTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(CreatureAttribute.BLUETEAM, creature.getTeam());
    }

    @Test
    public void getEnemyTeamTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(CreatureAttribute.REDTEAM, creature.getEnemyTeam());
    }

    @Test
    public void getHPTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(creature.getHP(),creature.getHP());
    }

    @Test
    public void setHPTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.setHP(200);
        assertEquals(200,creature.getHP());
    }

    @Test
    public void getMaxHPTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(creature.getMaxHP(),creature.getMaxHP());
    }

    @Test
    public void getDefenceTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(creature.getDefence(),creature.getDefence());
    }

    @Test
    public void getATKTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(creature.getATK(),creature.getATK());
    }

    @Test
    public void getCodeTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(creature.getCode(),creature.getCode());
    }

    @Test
    public void setCodeTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.setCode(50);
        assertEquals(50,creature.getCode());
    }

    @Test
    public void ifSelectedTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        assertEquals(false, creature.ifSelected());
    }

    @Test
    public void selectTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.Select();
        assertEquals(true, creature.ifSelected());
    }

    @Test
    public void unSelectTest() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.UnSelect();
        assertEquals(false, creature.ifSelected());
    }

    @Test
    public void choose() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.Choose();
        assertEquals(Color.GRAY, creature.getColor());
    }

    @Test
    public void unChoose() {
        creature = new Creature((char)1,world,10,10,CreatureAttribute.BLUETEAM);
        creature.Choose();
        assertEquals(Color.GRAY, creature.getColor());
        creature.UnChoose();
        assertNotEquals(Color.GRAY, creature.getColor());
    }
}