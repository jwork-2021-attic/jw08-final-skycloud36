package com.example.thing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.example.maze.World;

public class Bullet extends Creature {
    public Bullet(Thing owner, int dx, int dy) {
        super(owner.getColor(), (char)7, owner.world, owner.getTeam());
        this.owner = owner;
        this.xPos = owner.getX();
        this.yPos = owner.getY();
        this.name = BULLET;
        this.ATK = owner.getATK();
        this.dx = dx;
        this.dy = dy;
        this.code = Bullet.cnt;
        Bullet.cnt += 1;
        if(world.ifRecord() == true){
            String process = "Create" + " " + this.team + " " + this.name + " " + this.code + " " + dx + " " + dy
             + " " + owner.getTeam() + " " + owner.getName() + " " + owner.getCode();
            if (DebugCreateProcess){
                System.out.println(process);
            }
            world.addProcess(process);
        }
    }

    public Bullet(Thing owner, int dx, int dy, int code) {
        super(owner.getColor(), (char)7, owner.world, owner.getTeam());
        this.owner = owner;
        this.xPos = owner.getX();
        this.yPos = owner.getY();
        this.name = BULLET;
        this.ATK = owner.getATK();
        this.dx = dx;
        this.dy = dy;
        this.code = code;
        Bullet.cnt += 1;
    }

    private Thing owner;
    private int dx = 0;
    private int dy = 0;
    public static int cnt = 0;

    public void moveDirect(){
        this.moveWithHandle(dx, dy);
    } 

    @Override 
    public Thing getOwner(){
        return this.owner;
    }

    @Override
    public void moveWithHandle(int xPos, int yPos){
        this.xPos += xPos;
        this.yPos += yPos;
        Thing temp = this.world.get(this.xPos, this.yPos);
        if(temp.name != FLOOR && this.ifExist()){
            // if(temp.getName().equals(WALL)){
            if(temp.getName() == WALL){
                this.xPos -= xPos;
                this.yPos -= yPos;
                this.beDead();
            }
            // else if(temp.getTeam() == this.getTeam()){
            //     // this.world.setBackground(this.xPos, this.yPos);
            //     this.xPos += xPos;
            //     this.yPos += yPos;
            // }
            // else if(temp.getTeam().equals(this.enemyTeam)){  
            else if(temp.getTeam() == (this.enemyTeam)){   
                if(DebugBulletMove){
                    System.out.println(temp.name);
                }
                this.Attack(temp);
                this.beDead();
            }
        }
        else if (temp.name == FLOOR){
            if(this.world.ifRecord() == true){
                String process = "Move" + " " + team + " " + this.name + " " + this.code +  " " + xPos + " " + yPos + " " + owner.getCode();
                if (DebugMoveProcess){
                    System.out.println(process);
                }
                world.addProcess(process);
            }
            // processMove(this, xPos, yPos);
        }
    }

    @Override
    public void Attack(Thing victim){
        if(victim.ifExist()){
            if(DebugFirstAttack){
                System.out.println(this.getTeam() + " " + this.getName()
                + " Attack " 
                + victim.getTeam() + " " + victim.getName()
                + " " + this.getATK());
            }
            victim.beAttacked(this);
        }
        // processAttack(this, victim);
        if(this.world.ifRecord() == true){
            String process = "Attack" + " " + this.getTeam() + " " + this.getName() + " " + this.getCode() + " " + owner.getCode()
                            + " " + victim.getTeam() + " " + victim.getName() + " " + victim.getCode();
            if (Debug.DebugAttackProcess){
                System.out.println(process);
            }
            this.world.addProcess(process);
        }
    }

    @Override
    public void beAttacked(Thing attacker) {
        synchronized (this) {
            this.world.setBackground(this.getX(), this.getY());
            if (DebugBeAttacked) {
                System.out.println(this.getTeam() + " " + this.getName()
                        + " be attacked by "
                        + attacker.getTeam() + " " + attacker.getName()
                        + " " + attacker.getATK());
            }
            this.beDead();
            processBeAttack(attacker, this);
        }
    }

    @Override
    public List<String> getInfo(){
        List<String> info = new ArrayList<>();
        String singleInfo = this.name + " " + this.xPos + " " + this.yPos + " " + this.team + " " + this.code + " "
        + this.dx + " " + this.dy + " " + this.owner.code;
        info.add(singleInfo);
        return info;
    }

}
