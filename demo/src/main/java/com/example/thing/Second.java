package com.example.thing;

import java.awt.Color;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.maze.World;
import com.example.screen.ServerWorldScreen;
import com.example.screen.WorldScreen;

import com.example.asciiPanel.AsciiPanel;

public class Second extends Creature {
    public Second(World world, int xPos, int yPos, String team) {
        super((char) 24, world, xPos, yPos, team);
        this.name = SECOND;
        target = null;
        this.speed = 700;
        this.HP = 100;
        this.MaxHP = 100;
        this.Defence = 0;
        this.ATK = 50;
        curTime = System.currentTimeMillis();
        // this.bullets = new ArrayList<>(100);
        bullets = new HashMap<>();
        bulletByThread(this, 50);
        moveByThread(this);
        // if(world.ifRecord() == true){
        //     String process = "Create" + " " + team + " " + SECOND + " " + this.code;
        //     if (DebugCreateProcess){
        //         System.out.println(process);
        //     }
        //     world.addProcess(process);
        // }
        processCreate(this);
    }

    public Second(World world, int xPos, int yPos, String team, int code) {
        super((char) 24, world, xPos, yPos, team, code);
        this.name = SECOND;
        target = null;
        this.speed = 700;
        this.HP = 100;
        this.MaxHP = 100;
        this.Defence = 0;
        this.ATK = 50;
        curTime = System.currentTimeMillis();
        // this.bullets = new ArrayList<>(100);
        bullets = new HashMap<>();
        bulletByThread(this, 50);
        moveByThread(this);
        // if(world.ifRecord() == true){
        //     String process = "Create" + " " + team + " " + SECOND + " " + this.code;
        //     if (DebugCreateProcess){
        //         System.out.println(process);
        //     }
        //     world.addProcess(process);
        // }
        processCreate(this);
    }

    public Second(World world, int xPos, int yPos, String team, int code, boolean replay) {
        super((char) 24, world, xPos, yPos, team, code);
        this.name = SECOND;
        target = null;
        this.speed = 700;
        this.HP = 100;
        this.MaxHP = 100;
        this.Defence = 0;
        this.ATK = 50;
        curTime = System.currentTimeMillis();
        // this.bullets = new ArrayList<>(100);
        bullets = new HashMap<>();
    }

    private Thing target;
    // private List<Bullet> bullets;
    private HashMap<Integer, Bullet> bullets;
    private long curTime;

    @Override
    public Thing getTarget(){
        return target;
    }
    public void moveRandom() {
        Random random = new Random();
        int k = random.nextInt(4);
        switch (k) {
            case UP:
                this.moveUp();
                break;
            case DOWN:
                this.moveDown();
                break;
            case RIGHT:
                this.moveRight();
                break;
            case LEFT:
                this.moveLeft();
                break;
        }
    }

    public void moveByAI() {
        if (target == null || target.ifExist() == false) {
            int min = 1000;
            for (Creature t : this.enemyList) {
                int len = Math.min(Math.abs(t.getX() - this.getX()), Math.abs(t.getY() - this.getY()));
                if (len < min) {
                    target = t;
                }
            }
            if (target == null || target.ifExist() == false) {
                target = null;
                moveRandom();
            }
        } else {
            int dx = target.getX() - this.getX();
            int dy = target.getY() - this.getY();
            if (dx == 0) {
                if (dy < 0) {
                    this.toward = UP;
                    this.setGlyph((char) 24);
                    this.Attack();
                } else {
                    this.toward = DOWN;
                    this.setGlyph((char) 25);
                    this.Attack();
                }
            } else if (dy == 0) {
                if (dx < 0) {
                    this.toward = LEFT;
                    this.setGlyph((char) 27);
                    this.Attack();
                } else {
                    this.toward = RIGHT;
                    this.setGlyph((char) 26);
                    this.Attack();
                }
            } else if (Math.abs(dx) >= 1 && Math.abs(dy) >= 1) {
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dy < 0) {
                        this.moveUp();
                    } else {
                        this.moveDown();
                    }
                } else {
                    if (dx < 0) {
                        this.moveLeft();
                    } else {
                        this.moveRight();
                    }
                }
            }
        }
    }

    @Override
    public void moveWithHandle(int xPos, int yPos) {
        Thing temp = moveBy(xPos, yPos);
        if(temp == null){
            // if(this.world.ifRecord() == true){
            //     String process = "Move" + " " + team + " " + this.name + " " + this.code +  " " + xPos + " " + yPos;
            //     if (DebugMoveProcess){
            //         System.out.println(process);
            //     }
            //     world.addProcess(process);
            // }
            processMove(this, xPos, yPos);
        }
        this.changeGlyph();
    }

    @Override
    public void addBullet(Bullet bullet){
        // this.bullets.add(bullet);
        this.bullets.put(new Integer(bullet.getCode()), bullet);
    }

    @Override
    public Thing findBullet(int code){
        // for(Bullet bullet : this.bullets){
        //     if(bullet.getCode() == code){
        //         return bullet;
        //     }
        // }
        Bullet bullet = bullets.get(new Integer(code));
        return bullet;

        // return null;
    }

    @Override
    public void changeGlyph() {
        switch (this.toward) {
            case UP:
                this.setGlyph((char) 24);
                break;
            case DOWN:
                this.setGlyph((char) 25);
                break;
            case LEFT:
                this.setGlyph((char) 27);
                break;
            case RIGHT:
                this.setGlyph((char) 26);
                break;
        }
    }

    @Override
    public void Attack(Thing victim) {
        if (victim.ifExist()) {
            if (DebugEnemyAttack) {
                System.out.println(this.getTeam() + " " + this.getName()
                        + " Attack "
                        + victim.getTeam() + " " + victim.getName()
                        + " " + this.getATK());
            }
            victim.beAttacked(this);
        }
        // if(world.ifRecord() == true){
        //     String process = "Attack" + " " + this.team + " " + this.name + " " + this.code
        //                     + " " + victim.getTeam() + " " + victim.getName() + " " + victim.getCode();
        //     if (DebugAttackProcess){
        //         System.out.println(process);
        //     }
        //     world.addProcess(process);
        // }
        processAttack(this, victim);
    }

    @Override
    public void beAttacked(Thing attacker) {
        synchronized (this) {
            if (DebugBeAttacked) {
                System.out.println(this.getTeam() + " " + this.getName()
                        + " be attacked by "
                        + attacker.getTeam() + " " + attacker.getName()
                        + " " + attacker.getATK());
            }
            this.HP -= attacker.getATK() - this.getDefence();
            if (this.HP <= 0) {
                this.HP = 0;
                this.beDead();
            }
            // if(world.ifRecord() == true){
            //     String process = "BeAttack" + " " + this.team + " " + this.name + " " + this.code
            //                     + " " + attacker.getTeam() + " " + attacker.getName() + " " + attacker.getCode();
            //     if (DebugBeAttackProcess){
            //         System.out.println(process);
            //     }
            //     world.addProcess(process);
            // }
            processBeAttack(attacker, this);
        }
    }

    @Override
    public void Attack() {
        if (timeToAttack()) {
            Bullet bullet = null;
            switch (this.toward) {
                case UP:
                    bullet = new Bullet(this, 0, -1);
                    break;
                case DOWN:
                    bullet = new Bullet(this, 0, 1);
                    break;
                case RIGHT:
                    bullet = new Bullet(this, 1, 0);
                    break;
                case LEFT:
                    bullet = new Bullet(this, -1, 0);
                    break;
            }
            synchronized (this.bullets) {
                if (bullet != null) {
                    // bullets.add(bullet);
                    bullets.put(bullet.getCode(), bullet);
                }
            }
            if(world.ifRecord() == true){
                String process = "Attack" + " " + this.team + " " + this.name + " " + this.code;
                if (DebugAttackProcess){
                    System.out.println(process);
                }
                world.addProcess(process);
            }
        }
    }

    boolean timeToAttack() {
        if (System.currentTimeMillis() - curTime >= 200) {
            curTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void bulletByThread(Second second, int bulletSpeed) {
        Timer myTimer = new Timer();
        class myTask extends TimerTask {
            Second second;

            myTask(Second second) {
                this.second = second;
            }

            @Override
            public void run() {
                synchronized (this.second.bullets) {
                    if(this.second.world.ifServer() && (ServerWorldScreen.gameStart == false|| 
                    ServerWorldScreen.gamePause == true || second.selected == true)){
                        synchronized(this.second){
                            try{
                                this.second.wait();
                            }
                            catch(InterruptedException r){
                            }   
                        }
                    }
                    else if((this.second.world.ifServer() == false)&& (WorldScreen.gameStart == false || 
                    WorldScreen.gamePause == true || this.second.selected == true)){
                        synchronized(second){
                            try{
                                this.second.wait();
                            }
                            catch(InterruptedException r){
                            }   
                        }
                    }
                    // Iterator<Bullet> it = second.bullets.iterator();
                    Iterator <HashMap.Entry<Integer, Bullet>>it = bullets.entrySet().iterator();
                    Bullet bullet;
                    while (it.hasNext()) {
                        // bullet = it.next();
                        HashMap.Entry<Integer, Bullet> entry = it.next();
                        bullet = entry.getValue();
                        if (bullet.ifExist()) {
                            bullet.moveDirect();
                        } else {
                            it.remove();
                        }
                    }
                    if (second.ifExist() == false) {
                        if (second.bullets.size() > 0) {
                            second.bullets.clear();
                        }
                        cancel();
                    }
                }
            }
        }
        ;
        myTask mytask = new myTask(second);
        myTimer.schedule(mytask, 0, bulletSpeed);
    }

    public void moveByThread(Second second) {
        Timer myTimer = new Timer();
        class myTask extends TimerTask {
            Second second;

            myTask(Second second) {
                this.second = second;
            }

            @Override
            public void run() {
                if(this.second.world.ifServer() && (ServerWorldScreen.gameStart == false|| 
                ServerWorldScreen.gamePause == true || second.selected == true)){
                    synchronized(this.second){
                        try{
                            this.second.wait();
                        }
                        catch(InterruptedException r){
                        }   
                    }
                }
                else if ((this.second.world.ifServer() == false)&& (WorldScreen.gameStart == false || 
                WorldScreen.gamePause == true || this.second.selected == true)) {
                        synchronized (this.second) {
                            try {
                                this.second.wait();
                            } catch (Exception r) {
                            }
                        }
                    } 
                else {
                    if (this.second.ifExist()) {
                        this.second.moveByAI();
                    } else {
                        cancel();
                    }
                }
            }
        }
        ;
        myTask mytask = new myTask(second);
        myTimer.schedule(mytask, 0, second.speed);
    }

    @Override
    public void disPlayout(AsciiPanel terminal) {
        // for (Bullet bullet : this.bullets) 
        for (Bullet bullet : this.bullets.values()){
            if (bullet.ifExist()) {
                // System.out.println(bullet);
                terminal.write(bullet.getGlyph(), bullet.getX(), bullet.getY(), this.getColor());
            }
        }
        if (this.ifExist())
            terminal.write(this.getGlyph(), this.getX(), this.getY(), this.getColor());
    }

    @Override
    public List<String> getInfo(){
        List<String> info = new ArrayList<>();
        String singleInfo = this.name + " " + this.xPos + " " + this.yPos + " " + this.team + " " + this.code + " " + this.HP;
        info.add(singleInfo);
        // for (Bullet bullet : this.bullets) 
        for (Bullet bullet : this.bullets.values()){
            List<String> temp = bullet.getInfo();
            if(temp != null)
                info.addAll(temp);
        }
        return info;
    }
}
