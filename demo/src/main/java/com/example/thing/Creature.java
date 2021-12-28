package com.example.thing;

import com.example.maze.World;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Creature extends Thing implements Debug {
    protected int xPos, yPos;
    static int redCnt = 0;
    static int blueCnt = 0;

    Creature(Color color, char glyph, World world, String team){
        super(color, glyph, world);
        this.team = team;
        // if (team.equals(BLUETEAM)) {
        if (team == BLUETEAM) {
            this.enemyTeam = REDTEAM;
            this.enemyList = world.getRed();
            this.setColor(Color.BLUE);
            this.toward = LEFT;
            this.code = blueCnt;
            blueCnt += 1;
        } 
        // else if (team.equals(REDTEAM)) {
        else if (team == REDTEAM) {
            this.enemyTeam = BLUETEAM;
            this.enemyList = world.getBlue();
            this.setColor(Color.RED);
            this.toward = RIGHT;
            this.code = redCnt;
            redCnt += 1;
        }      
        this.exist = true;
    }

    Creature(char glyph, World world, int xPos, int yPos, String team){
        super(Color.BLACK, glyph, world);
        this.team = team;
        // if (team.equals(BLUETEAM)) {
        if (team == BLUETEAM) {
            this.enemyTeam = REDTEAM;
            this.enemyList = world.getRed();
            this.setColor(Color.BLUE);
            this.code = blueCnt;
            blueCnt += 1;
        } 
        // else if (team.equals(REDTEAM)) {
        else if (team == REDTEAM) {
            this.enemyTeam = BLUETEAM;
            this.enemyList = world.getBlue();
            this.setColor(Color.RED);
            this.code = redCnt;
            redCnt += 1;
        }
        this.xPos = xPos;
        this.yPos = yPos;
        world.put(this, xPos, yPos);
        this.exist = true;
        this.toward = 1;
    }

    Creature(char glyph, World world, int xPos, int yPos, String team, int code){
        super(Color.BLACK, glyph, world);
        this.team = team;
        // if (team.equals(BLUETEAM)) {
        if (team == BLUETEAM) {
            this.enemyTeam = REDTEAM;
            this.enemyList = world.getRed();
            this.setColor(Color.BLUE);
            this.code = code;
            blueCnt += 1;
        }
        // else if (team.equals(REDTEAM)) {
        else if (team == REDTEAM) {
            this.enemyTeam = BLUETEAM;
            this.enemyList = world.getBlue();
            this.setColor(Color.RED);
            this.code = code;
            redCnt += 1;
        }
        this.xPos = xPos;
        this.yPos = yPos;
        world.put(this, xPos, yPos);
        this.exist = true;
        this.toward = 1;
    }

    public Boolean moveTo(int xPos, int yPos) {
        if (this.world.putPlayingThing(this, xPos, yPos) == null) {
            this.world.setBackground(this.getX(), this.getY());
            this.setxPos(xPos);
            this.setyPos(yPos);
            if (DebugMoveTo)
                System.out.println(this.getTeam() + " " + this.getName() + " " + Thread.currentThread().getId() + "x:"
                        + xPos + "y:" + yPos);
            return true;
        }
        if (DebugMoveTo)
            System.out.println(this.getTeam() + " " + this.getName() + " " + Thread.currentThread().getId() + "x:"
                    + xPos + "y:" + yPos);
        return false;
    }

    public Boolean moveTest(int xPos, int yPos) {
        if (this.world.putPlayingThing(this, xPos, yPos) == null) {
            this.world.setBackground(this.getX(), this.getY());
            this.setxPos(xPos);
            this.setyPos(yPos);
            if (DebugEnemyMove)
                System.out.println(this.getTeam() + " " + this.getName() + " " + Thread.currentThread().getId() + "x:"
                        + xPos + "y:" + yPos);
            return true;
        }
        if (DebugEnemyMove)
            System.out.println(this.getTeam() + " " + this.getName() + " " + Thread.currentThread().getId() + "x:"
                    + xPos + "y:" + yPos);
        return false;
    }

    @Override
    public int getX() {
        return this.xPos;
    }

    @Override
    public int getY() {
        return this.yPos;
    }

    @Override
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    @Override
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    @Override
    public Thing moveBy(int xPos, int yPos) {
        int x = this.xPos + xPos;
        int y = this.yPos + yPos;
        Thing thing = this.world.get(x, y);
        // if (thing.getName().equals(FLOOR)) {
        if (thing.getName() == FLOOR) {
            if (this.moveTo(x, y)) {
                return null;
            } else {
                return this.world.get(x, y);
            }
        } else {
            return thing;
        }
    }

    public synchronized void moveWithHandle(int xPos, int yPos) {
        moveBy(xPos, yPos);
    }

    @Override
    public void moveReplay(int xPos, int yPos){
        if(xPos == 0 && yPos == -1){
            this.toward = UP;
        }
        else if(xPos == 0 && yPos == 1){
            this.toward = DOWN;
        }
        else if(xPos == -1 && yPos == 0){
            this.toward = LEFT;
        }
        else if(xPos == 1 && yPos == 0){
            this.toward = RIGHT;
        }
        this.changeGlyph();
        this.moveBy(xPos, yPos);
    }

    public void moveUp() {
        // this.moveBy(0, -1);
        this.toward = UP;
        this.moveWithHandle(0, -1);
    }

    public void moveDown() {
        // this.moveBy(0, 1);
        this.toward = DOWN;
        this.moveWithHandle(0, 1);
    }

    public void moveRight() {
        // this.moveBy(1, 0);
        this.toward = RIGHT;
        this.moveWithHandle(1, 0);
    }

    public void moveLeft() {
        // this.moveBy(-1, 0);
        this.toward = LEFT;
        this.moveWithHandle(-1, 0);
    }

    private boolean exist;

    @Override
    public boolean ifExist() {
        return this.exist;
    }

    protected long curTime = 0;
    boolean timeToAttack() {
        if (System.currentTimeMillis() - curTime >= 200) {
            curTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public void beDead() {
        if (DebugDie) {
            System.out.println(this.getTeam() + " " + this.getName() + Thread.currentThread().getId() + " die");
        }
        this.exist = false;
        if (this.name != BULLET) {
            // if (this.team.equals(REDTEAM)) {
            if (this.team == REDTEAM) {
                this.world.getRed().remove(this);
            } else {
                this.world.getBlue().remove(this);
            }
            this.world.setBackground(this.getX(), this.getY());
        }
        else{
            if(this.world.ifRecord() == true){
                String process = "Dead" + " " + this.getTeam() + " " + this.getName() + " " + this.getCode() + " " + this.getOwner().getCode();
                if (Debug.DebugAttackProcess){
                    System.out.println(process);
                }
                this.world.addProcess(process);
            }
        }
    }

    protected String team;

    @Override
    public String getTeam() {
        return this.team;
    }

    protected String enemyTeam;

    @Override
    public String getEnemyTeam() {
        return this.enemyTeam;
    }

    protected int speed;
    protected List<Creature> enemyList;

    protected int HP;

    @Override
    public int getHP() {
        return this.HP;
    }

    @Override
    public void setHP(int HP){
        this.HP = HP;
    }

    protected int MaxHP;

    @Override
    public int getMaxHP() {
        return this.MaxHP;
    }

    protected int Defence;

    @Override
    public int getDefence() {
        return this.Defence;
    }

    protected int ATK;

    @Override
    public int getATK() {
        return this.ATK;
    }

    @Override
    public int getCode(){
        return this.code;
    }

    @Override
    public void setCode(int code){
        this.code = code;
    }

    protected int toward;

    protected boolean selected;

    @Override
    public boolean ifSelected(){
        return selected;
    }

    public synchronized void Select(){
        this.selected = true;
        this.setColor(Color.GREEN);
    }

    public synchronized void UnSelect(){
        this.selected = false;
        this.setColor(Color.RED);
    }

    public synchronized void Choose(){
        this.setColor(Color.GRAY);
    }

    public synchronized void UnChoose(){
        this.setColor(Color.RED);
    }
}
