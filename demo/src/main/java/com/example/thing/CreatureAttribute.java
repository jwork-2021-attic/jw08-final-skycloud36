package com.example.thing;

import java.util.List;

import com.example.asciiPanel.AsciiPanel;

public interface CreatureAttribute {    

    public default String getTeam(){
        return null;
    }

    public default String getEnemyTeam(){
        return null;
    }

    public default void beAttacked(Thing attacker){}
    
    public default void Attack(Thing victim){}

    public default void Attack(){}

    public default boolean ifExist(){
        return false;
    }

    public default boolean ifSelected(){
        return false;
    }

    public default void beDead(){}

    public default int getHP(){return 0;}

    public default void setHP(int HP){};

    public default int getDefence(){return 0;}
    
    public default int getATK(){return 0;}

    public default int getMaxHP(){return 0;}

    public default void disPlayout(AsciiPanel terminal){}

    public default List<String> getInfo(){return null;}

    public default void addBullet(Bullet bullet){}

    public default void setCode(int code){}

    public default int getCode(){return 0;}

    public default void Select(){};

    public default Thing getOwner(){return null;}

    public default Thing findBullet(int code){return null;}

    public default Thing moveBy(int xPos, int yPos){return null;}

    public default void moveReplay(int xPos, int yPos){}

    public default void changeGlyph(){}

    public default Thing getTarget(){return null;}

    public default void changeToward(int toward){}

    public default void processCreate(Thing creature){
        if(creature.world.ifRecord() == true){
            String process = "Create" + " " + creature.getTeam() + " " + creature.getName() + " " + creature.getCode() + " " + creature.getX() + " " + creature.getY();
            if (Debug.DebugCreateProcess){
                System.out.println(process);
            }
            creature.world.addProcess(process);
        }
    }

    public default void processMove(Thing creature, int xPos, int yPos){
        if(creature.world.ifRecord() == true){
            String process = "Move" + " " + creature.getTeam() + " " + creature.getName() + " " + creature.getCode() +  " " + xPos + " " + yPos;
            if (Debug.DebugMoveProcess){
                System.out.println(process);
            }
            creature.world.addProcess(process);
        }
    }

    public default void processAttack(Thing attacker, Thing victim){
        if(attacker.world.ifRecord() == true){
            String process = "Attack" + " " + attacker.getTeam() + " " + attacker.getName() + " " + attacker.getCode()
                            + " " + victim.getTeam() + " " + victim.getName() + " " + victim.getCode();
            if (Debug.DebugAttackProcess){
                System.out.println(process);
            }
            attacker.world.addProcess(process);
        }
    }

    public default void processBeAttack(Thing attacker, Thing victim){
        if(victim.world.ifRecord() == true){
            String process = "BeAttack" + " " + victim.getTeam() + " " + victim.getName() + " " + victim.getCode()
            + " " + attacker.getTeam() + " " + attacker.getName() + " " + attacker.getCode()                            ;
            if (Debug.DebugBeAttackProcess){
                System.out.println(process);
            }
            victim.world.addProcess(process);
        }
    }

    public static int UP = 0;
    public static int DOWN = 1;
    public static int RIGHT = 2;
    public static int LEFT = 3;

    public static String FLOOR = "Floor";
    public static String WALL = "Wall";
    public static String FIRST = "First";
    public static String SECOND = "Second";
    public static String BLUETEAM = "BlueTeam";
    public static String REDTEAM = "RedTeam";
    public static String BULLET = "Bullet";
    public static String PLAYER = "Player";
    public static String TESTSECOND = "TestSecond";
}
