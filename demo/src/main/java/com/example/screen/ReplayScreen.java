package com.example.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.example.thing.*;

// import org.w3c.dom.events.MouseEvent;

import com.example.thing.First;
import com.example.maze.World;

import com.example.asciiPanel.AsciiPanel;

public class ReplayScreen implements Screen {

    volatile public static boolean gameStart = false;
    volatile public static boolean gamePause = false;
    boolean noFile = false;
    private World world;
    // Player player;
    int buttonIndex = 0;
    BufferedReader inputStream;
    List<String> Process;
    int pindex; 
    boolean replayAuto = false;

    public ReplayScreen(){
        try {
            this.noFile = false;
            world = new World("GameMap.txt");
            // inputStream = new BufferedReader(new FileReader("GameProcess.txt"));
            Path path = Paths.get("GameProcess.txt");
            Process = Files.readAllLines(path);  
            pindex = 0;
            replayAuto = false;
            handleByThread();
            // if(inputStream == null){
            //     this.noFile = true;
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }

    public boolean ifNoFile(){
        return this.noFile;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        displayCreature(terminal);
    }

    private void displayCreature(AsciiPanel terminal){
        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {
                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
            }
        }
        for (Creature t : world.getBlue()) {
            t.disPlayout(terminal);
        }
        for (Creature t : world.getRed()) {
            t.disPlayout(terminal);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        try{
            switch(key.getKeyCode()){
                // case KeyEvent.VK_SPACE:
                //     this.replayAuto = false;
                //     // String process = inputStream.readLine();
                //     String process = Process.get(pindex);
                //     if(pindex < Process.size() - 1)
                //         pindex++;
                //     if(process != null)
                //         this.handleProcess(process);
                    
                //     // handleByThread();
                //     break;
                case KeyEvent.VK_ENTER:
                    if(replayAuto == false){
                        replayAuto = true;
                        // notifyAll();
                    }
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return this;
    }

    private void handleByThread(){
        class Myrun implements Runnable{
            public Myrun(){
            }

            @Override
            public void run() {
                while(true){
                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(pindex < Process.size() -1 && replayAuto == true){
                        // System.out.println("m");
                        String process = Process.get(pindex);
                        // System.out.println(process);
                        if(pindex < Process.size())
                            // System.out.println(pindex);
                            pindex++;
                        if(process != null)
                            handleProcess(process);
                        try {
                            TimeUnit.MILLISECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(pindex >= Process.size()){
                        break;
                    }
                }
            }
        };

        Myrun myrun = new Myrun();
        Thread thread = new Thread(myrun);
        thread.start();

    }

    private void handleProcess(String process){
        // System.out.println(process);
        String[] Process = process.split(" ");
        if(Process[0].equals("Create")){
            this.handleCreate(Process);
        }
        else if(Process[0].equals("Move")){
            this.handleMove(Process);
        }
        else if(Process[0].equals("Attack")){
            this.handleAttack(Process);
        }
        else if(Process[0].equals("Dead")){
            this.handleDead(Process);
        }
    }    

    private void handleCreate(String[] Process){
        if(Process[2].equals(CreatureAttribute.FIRST)){
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                First temp = new First(world, Integer.valueOf(Process[4]), Integer.valueOf(Process[5]), CreatureAttribute.REDTEAM, Integer.valueOf(Process[3]), true);
                world.addRed(temp);
            }
            else{
                First temp = new First(world, Integer.valueOf(Process[4]), Integer.valueOf(Process[5]), CreatureAttribute.BLUETEAM, Integer.valueOf(Process[3]), true);
                world.addBlue(temp);
            }
        }
        else if(Process[2].equals(CreatureAttribute.SECOND)){
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Second temp = new Second(world, Integer.valueOf(Process[4]), Integer.valueOf(Process[5]), CreatureAttribute.REDTEAM, Integer.valueOf(Process[3]), true);
                world.addRed(temp);
            }
            else{
                Second temp = new Second(world, Integer.valueOf(Process[4]), Integer.valueOf(Process[5]), CreatureAttribute.BLUETEAM, Integer.valueOf(Process[3]), true);
                world.addBlue(temp);
            }
        }
        else if(Process[2].equals(CreatureAttribute.BULLET)){
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Thing owner = world.findInRed(Integer.valueOf(Process[8]));
                Bullet temp = new Bullet(owner, Integer.valueOf(Process[4]), Integer.valueOf(Process[5]), Integer.valueOf(Process[3]));
                owner.addBullet(temp);
            }
            else{
                Thing owner = world.findInBlue(Integer.valueOf(Process[8]));
                Bullet temp = new Bullet(owner, Integer.valueOf(Process[4]), Integer.valueOf(Process[5]), Integer.valueOf(Process[3]));
                owner.addBullet(temp);
            }
        }
    }

    private void handleMove(String[] Process){
        // System.out.println("world");
        if(Process[2].equals(CreatureAttribute.BULLET) == false){
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Thing temp = world.findInRed(Integer.valueOf(Process[3]));
                if(temp != null)
                    temp.moveReplay(Integer.valueOf(Process[4]), Integer.valueOf(Process[5]));
            }
            else{
                Thing temp = world.findInBlue(Integer.valueOf(Process[3]));
                if(temp != null)
                    temp.moveReplay(Integer.valueOf(Process[4]), Integer.valueOf(Process[5]));
            }
        }
        else{
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Thing temp = world.findInRed(Integer.valueOf(Process[6])).findBullet(Integer.valueOf(Process[3]));
                if(temp != null){
                    temp.setxPos(temp.getX()+Integer.valueOf(Process[4]));
                    temp.setyPos(temp.getY()+Integer.valueOf(Process[5]));
                }
            }
            else{
                Thing temp = world.findInBlue(Integer.valueOf(Process[6])).findBullet(Integer.valueOf(Process[3]));
                if(temp != null){
                    temp.setxPos(temp.getX()+Integer.valueOf(Process[4]));
                    temp.setyPos(temp.getY()+Integer.valueOf(Process[5]));
                }
            }
        }
    }

    private void handleAttack(String[] Process){
        if(Process[2].equals(CreatureAttribute.SECOND) == false){
            if(Process[2].equals(CreatureAttribute.BULLET)){
                if(Process[1].equals(CreatureAttribute.REDTEAM)){
                    Thing attacker = world.findInRed(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
                    Thing victim = world.findInBlue(Integer.valueOf(Process[7]));
                    if(attacker != null && victim != null)
                        attacker.Attack(victim);
                }
                else{
                    Thing attacker = world.findInBlue(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
                    Thing victim = world.findInRed(Integer.valueOf(Process[7]));
                    if(attacker != null && victim != null)
                        attacker.Attack(victim);
                }
            }
            else{
                if(Process[1].equals(CreatureAttribute.REDTEAM)){
                    Thing attacker = world.findInRed(Integer.valueOf(Process[3]));
                    Thing victim = world.findInBlue(Integer.valueOf(Process[6]));
                    if(attacker != null && victim != null)
                        attacker.Attack(victim);
                }
                else{
                    Thing attacker = world.findInRed(Integer.valueOf(Process[3]));
                    Thing victim = world.findInBlue(Integer.valueOf(Process[6]));
                    if(attacker != null && victim != null)
                        attacker.Attack(victim);
                }
            }
        }
    }

    private void handleDead(String[] Process){
        if(Process[2].equals(CreatureAttribute.BULLET)){
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Thing temp = world.findInRed(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
                temp.beDead();
            }
            else{
                Thing temp = world.findInBlue(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
                temp.beDead();
            }
        }
    }

    public Screen Finish() {
        if(gameStart == true){
            if (this.world.getBlue().size() == 0) {
                gameStart = false;
                return new WinScreen();
            } else if (this.world.getRed().size() == 0) {
                gameStart = false;
                return new LoseScreen();
            }
        }
        return null;
    }
}