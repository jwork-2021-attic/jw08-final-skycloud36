package com.example.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import com.example.maze.World;
import com.example.Server;
import com.example.asciiPanel.AsciiPanel;

public class ServerWorldScreen implements Screen {

    volatile public static boolean gameStart = false;
    volatile public static boolean gamePause = false;
    private World world;
    // Player player;
    Shop client1Shop = null;
    Shop client2Shop = null;
    Creature client1choose = null;
    Creature client2choose = null;
    int index = 0;
    int cost = 500;
    List<Button> buttons = new ArrayList<>();
    int buttonIndex = 0;

    Server server;
    
    
    public void makeTeam(){
        Second b1 = new Second(world, 70, 27, CreatureAttribute.BLUETEAM);   world.addBlue(b1);
        Second b2 = new Second(world, 70, 16, CreatureAttribute.BLUETEAM);    world.addBlue(b2);
        Second b3 = new Second(world, 70, 19, CreatureAttribute.BLUETEAM);    world.addBlue(b3);
        // Second b4 = new Second(world, 70, 2,CreatureAttribute.BLUETEAM);   world.addBlue(b4);
        // Second b5 = new Second(world, 70, 6,CreatureAttribute.BLUETEAM);    world.addBlue(b5);
        // Second b6 = new Second(world, 70, 35,CreatureAttribute.BLUETEAM);    world.addBlue(b6);
        Second r4 = new Second(world, 30, 2, CreatureAttribute.REDTEAM);   world.addRed(r4);
        Second r5 = new Second(world, 30, 6, CreatureAttribute.REDTEAM);    world.addRed(r5);
        Second r6 = new Second(world, 30, 35, CreatureAttribute.REDTEAM);    world.addRed(r6);

        First b7 = new First(world, 40, 20, CreatureAttribute.BLUETEAM);   world.addBlue(b7);
        First b8 = new First(world, 40, 21, CreatureAttribute.BLUETEAM);     world.addBlue(b8);
        // First b9 = new First(world, 40, 25,CreatureAttribute.BLUETEAM);   world.addBlue(b9);
        // First b10 = new First(world, 40, 18, CreatureAttribute.BLUETEAM);     world.addBlue(b10);
        First r9 = new First(world, 20, 25, CreatureAttribute.REDTEAM);   world.addRed(r9);
        First r10 = new First(world, 20, 18, CreatureAttribute.REDTEAM);     world.addRed(r10);
    }

    public ServerWorldScreen(Server server) {
        this.server = server;
        client1Shop = null; client1Ready = false;
        client2Shop = null; client2Ready = false;
        client1player = null;
        client2player = null;
        world = new World(true, server);
    }

    public ServerWorldScreen(boolean record) {
        world = new World(record);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

    }

    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        // System.out.println(key.getKeyCode());
        if (this.gameStart == false) {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    this.gameStart();
                    break;
            }
        }
        else if(this.gamePause == true){
            handleInputGameIsPause(key);
        } 
        else {
            if (client1player != null) {
                handleInputPlayerNotNull(key);
            } else {
                handleInputPlayerIsNull(key);
            }
        }
        return this;
    }

    private void handleInputPlayerNotNull(KeyEvent key){
        if (client1player.ifExist() == false) {
            UnselectPlayer();
        }
        else{
            switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    client1player.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    client1player.moveRight();
                    break;
                case KeyEvent.VK_UP:
                    client1player.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    client1player.moveDown();
                    break;
                case KeyEvent.VK_SPACE:
                    client1player.Attack();
                    break;
                case KeyEvent.VK_Q:
                    this.UnselectPlayer();
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (this.gamePause == false) {
                        gamePause();
                    } else {
                        gameUnPause();
                    }
                    break;
            }
        }
    }
    
    private void handleInputPlayerIsNull(KeyEvent key){
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(this.world.getRed().size() > 0){
                    index = (index - 1 + this.world.getRed().size()) % this.world.getRed().size();
                    this.ChoosePlayer(this.world.getRed().get(index % this.world.getRed().size()));
                }
                break;
            case KeyEvent.VK_DOWN:
            if(this.world.getRed().size() > 0){
                index = (index + 1 + this.world.getRed().size()) % this.world.getRed().size();
                this.ChoosePlayer(this.world.getRed().get(index % this.world.getRed().size()));
            }
                break;
            case KeyEvent.VK_ENTER:
                if (client1choose != null && client1choose.ifExist()) {
                    client1player = client1choose;
                    client1player.Select();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (this.gamePause == false) {
                    gamePause();
                } else {
                    gameUnPause();
                }
                break;
        }
    }

    private void handleInputGameIsPause(KeyEvent key){
        switch(key.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                gameUnPause();
                break;
            case KeyEvent.VK_ENTER:
                // if(buttons.get(index).getButtonName().equals("CONTINUE"))
                if(buttons.get(buttonIndex).getButtonName()=="CONTINUE")
                    this.gameUnPause();
                // else if(buttons.get(index).getButtonName().equals("SAVE GAME"))
                else if(buttons.get(buttonIndex).getButtonName()=="SAVE GAME")
                    try{
                        this.saveGame();
                        this.saveMap();
                        this.saveStatus();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                break;
            case KeyEvent.VK_DOWN:
                buttons.get(buttonIndex).unselect();
                buttonIndex = (buttonIndex+1)%buttons.size();
                buttons.get(buttonIndex).select();
                break;
            case KeyEvent.VK_UP:
                buttons.get(buttonIndex).unselect();
                buttonIndex = (buttonIndex-1+buttons.size())%buttons.size();
                buttons.get(buttonIndex).select();
                break;
        }
    }
    public String Finish() {
        if(gameStart == true){
            if (this.world.getBlue().size() == 0) {
                gameStart = false;
                return "Finish RedTeamWin";
            } else if (this.world.getRed().size() == 0) {
                gameStart = false;
                return "Finish BlueTeamWin";
            }
        }
        return null;
    }

    public void gameStart() {
        this.gameStart = true;
        for (Creature t : world.getBlue()) {
            synchronized (t) {
                t.notifyAll();
            }
        }
        for (Creature t : world.getRed()) {
            synchronized (t) {
                t.notifyAll();
            }
        }        
        if (client1choose == null || !client1choose.ifExist()) {
            if (this.world.getRed().size() > 0) {
                this.ChoosePlayer(this.world.getRed().get(0));
            }
        }
    }

    public void gameUnPause() {
        this.gamePause = false;
        for (Creature t : world.getBlue()) {
            synchronized (t) {
                t.notifyAll();
            }
        }
        for (Creature t : world.getRed()) {
            synchronized (t) {
                t.notifyAll();
            }
        }
    }

    public void gamePause() {
        this.gamePause = true;
    }

    void ChoosePlayer(Creature c) {
        if (this.client1choose != null) {
            client1choose.UnChoose();
        }
        client1choose = c;
        client1choose.Choose();
    }

    void SelectPlayer() {
        client1player = client1choose;
        client1player.Select();
    }

    void UnselectPlayer() {
        if (this.client1player != null) {
            synchronized (client1player) {
                client1player.UnSelect();
                client1player.notifyAll();
            }
            if (client1player.ifExist() == true) {
                this.ChoosePlayer(client1player);
            } else {
                if (this.world.getRed().size() > 0) {
                    this.ChoosePlayer(this.world.getRed().get(0));
                }
            }
        }
        client1player = null;
    }

    int cursorxToWorldx(int x) {
        return x / 16;
    }

    int cursoryToWorldy(int y) {
        return (y - 30) / 16;
    }

    void saveGame() throws IOException{
        List<String> Info = world.saveGame();
        BufferedWriter outputStream = null;
        try{
            outputStream = new BufferedWriter(new FileWriter("GameInfo.txt"));
            for(String info : Info){
                // System.out.println(info);
                outputStream.write(info + "\n");
            }
        }
        finally{
            if(outputStream != null)
                outputStream.close();
        }
    }

    void saveMap() throws IOException{
        List<String> Info = world.saveMap();
        BufferedWriter outputStream = null;
        try{
            outputStream = new BufferedWriter(new FileWriter("GameMap.txt"));
            for(String info : Info){
                // System.out.println(info);
                outputStream.write(info + "\n");
            }
        }
        finally{
            if(outputStream != null)
                outputStream.close();
        }
    }

    void saveStatus() throws IOException{
        BufferedWriter outputStream = null;
        try{
            outputStream = new BufferedWriter(new FileWriter("GameStatus.txt"));
            outputStream.write(this.cost + "\n");
        }
        finally{
            if(outputStream != null)
                outputStream.close();
        }
    }

    public void handleProcess(String process){
        String[] Process = process.split(" ");
        if(Process[1].equals("Mouse")){
            handleMouse(Process);
        }
        if(Process[1].equals("Key")){
            switch(Process[2]){
                case "GameStart":handleGameStart(Process[0]);break;
                case "SelectPlayer":handleSelectPlayer(Process);break;
                case "UP":handleUp(Process);break;
                case "DOWN":handleDown(Process);break;
                case "LEFT":handleLeft(Process);break;
                case "RIGHT":handleRight(Process);break;
                case "Attack":handleAttack(Process);break;
            }
            // handleKey(Process);
        }
    }

    public void handleMouse(String[] Process){
        if(gameStart == false){
            int x = Integer.valueOf(Process[2]);
            int y = Integer.valueOf(Process[3]);
            // System.out.println("x: " + x + " y: " + y);
            Shop shop = null;
            if(Process[0].equals("1")){
                shop = client1Shop;
            }
            else{
                shop = client2Shop;
            }
            if(y >= world.HEIGHT){
                int index = x / 10;
                if(index < Shop.values().length){
                    shop = Shop.values()[index];
                    // System.out.println(this.shop);
                }
                else{
                    shop = null;
                }
            }
            else{
                Thing temp = this.world.get(x, y);
                // if(temp.getName().equals(CreatureAttribute.FLOOR)){                
                if(temp.getName() == CreatureAttribute.FLOOR){     
                    if(shop != null){
                        switch(shop){
                            case Lancer:
                                if(Process[0].equals("1")){
                                    First f = new First(world, x, y, CreatureAttribute.REDTEAM);
                                    world.getRed().add(f);
                                }
                                else{
                                    First f = new First(world, x, y, CreatureAttribute.BLUETEAM);
                                    world.getBlue().add(f);
                                }
                                break;
                            case Archer:
                            if(Process[0].equals("1")){
                                Second f = new Second(world, x, y, CreatureAttribute.REDTEAM);
                                world.getRed().add(f);
                            }
                            else{
                                Second f = new Second(world, x, y, CreatureAttribute.BLUETEAM);
                                world.getBlue().add(f);
                            }
                                break;
                        }
                        // cost -= shop.cost();
                    }
                }
            }
            if(Process[0].equals("1")){
                client1Shop = shop;
            }
            else{
                client2Shop = shop;
            }
        }
    }

    // public void handleKey(Process){

    // }
    private boolean client1Ready;
    private boolean client2Ready;

    public void handleGameStart(String client){
        if(client.equals("1")){
            client1Ready = true;
            server.writeToClient("Ready Client1");
        }
        else{
            client2Ready = true;
            server.writeToClient("Ready Client2");
        }
        if(client1Ready && client2Ready){
            gameStart();
            server.writeToClient("Ready GameStart");
            client1Ready = false;
            client2Ready = false;
        }
    }

    Creature client1player;
    Creature client2player;
    
    public void handleSelectPlayer(String[] Process){
        if(Process[0].equals("1")){
            Thing creature = world.findInRed(Integer.valueOf(Process[3]));
            if(client1player != null && client1player.ifExist()){
                synchronized (client1player) {
                    client1player.UnSelect();
                    client1player.notifyAll();
                }
            }
            if(creature != null && creature.ifExist()){
                creature.Select();
                client1player = (Creature)creature;
            }
        }
        else{
            Thing creature = world.findInBlue(Integer.valueOf(Process[3]));
            if(client2player != null && client2player.ifExist()){
                synchronized (client2player) {
                    client2player.UnSelect();
                    client2player.notifyAll();
                }
            }
            if(creature != null && creature.ifExist()){
                creature.Select();
                client2player = (Creature)creature;
            }
        }
    }

    public void handleUp(String[] Process){
        if(Process[0].equals("1")){
            client1player.moveUp();
        }
        else{
            client2player.moveUp();
        }
    }

    public void handleDown(String[] Process){
        if(Process[0].equals("1")){
            client1player.moveDown();
        }
        else{
            client2player.moveDown();
        }
    }

    public void handleLeft(String[] Process){
        if(Process[0].equals("1")){
            client1player.moveLeft();
        }
        else{
            client2player.moveLeft();
        }
    }

    public void handleRight(String[] Process){
        if(Process[0].equals("1")){
            client1player.moveRight();
        }
        else{
            client2player.moveRight();
        }
    }

    public void handleAttack(String[] Process){
        if(Process[0].equals("1")){
            client1player.Attack();
        }
        else{
            client2player.Attack();
        }
    }
}
