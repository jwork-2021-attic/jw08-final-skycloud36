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
    Shop shop = null;
    Creature player = null;
    Creature choose = null;
    int index = 0;
    int cost = 500;
    List<Button> buttons = new ArrayList<>();
    int buttonIndex = 0;

    Server server;
    
    public ServerWorldScreen(Server server) {
        this.server = server;
        world = new World();
        // server.writeToClient(World.WIDTH + " " + World.HEIGHT);
    }

    public ServerWorldScreen(boolean record) {
        world = new World(record);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // if(terminal.hasFocus() == false){
        //     System.out.println("world");
        //     terminal.requestFocus(true);
        // }
        displayCreature(terminal);
        displayPlayer(terminal);
        if (this.gameStart == false) {
            displayShop(terminal);
        }
        if (this.gamePause == true) {
            displayPause(terminal);
        }
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

    private void displayPlayer(AsciiPanel terminal){
        String stats;
        if (player != null) {
            stats = String.format("Team:%s", player.getTeam());
            terminal.write(stats, 1, world.HEIGHT);
            stats = String.format("Creatures:%s", player.getName());
            terminal.write(stats, 1, world.HEIGHT + 1);
            stats = String.format("%3d/%3d hp", player.getHP(), player.getMaxHP());
            terminal.write(stats, 1, world.HEIGHT + 2);
        } else {
            stats = String.format("Player is null");
            terminal.write(stats, 1, world.HEIGHT);
        }
    }

    private void displayShop(AsciiPanel terminal){
        String stats;
        for(Shop item : Shop.values()){
            for(int i = 0; i < item.info().size(); i++){
                terminal.write(item.info().get(i), item.ordinal()*10, world.HEIGHT+i);
            }
        }
        if(shop != null){
            terminal.write(shop.name(), 30, world.HEIGHT);
        }
        else{
            terminal.write("null", 30, world.HEIGHT);
        }
        stats = String.format("Money %3d Left",this.cost);
        terminal.write(stats, 30, world.HEIGHT+1);
        terminal.write("After Set Your Army, Press Enter To Play", 30, world.HEIGHT+2);
    }

    private void displayPause(AsciiPanel terminal){
        String stats;
        stats = String.format("Game Pause");
        terminal.write(stats, world.WIDTH / 2 - 5, world.HEIGHT / 2 - 5);
        if(buttons.size() == 0){
            Button b1 = new Button("CONTINUE", 25, 21, Color.WHITE, Color.GRAY, terminal);
            Button b2 = new Button("SAVE GAME", 25, 23, terminal);
            buttons.add(b1);
            buttons.add(b2);
        }
        for(Button b : buttons){
            b.display();
        }
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
            if (player != null) {
                handleInputPlayerNotNull(key);
            } else {
                handleInputPlayerIsNull(key);
            }
        }
        return this;
    }

    private void handleInputPlayerNotNull(KeyEvent key){
        if (player.ifExist() == false) {
            UnselectPlayer();
        }
        else{
            switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    player.moveRight();
                    break;
                case KeyEvent.VK_UP:
                    player.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    player.moveDown();
                    break;
                case KeyEvent.VK_SPACE:
                    player.Attack();
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
                if (choose != null && choose.ifExist()) {
                    player = choose;
                    player.Select();
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
        if (choose == null || !choose.ifExist()) {
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
        if (this.choose != null) {
            choose.UnChoose();
        }
        choose = c;
        choose.Choose();
    }

    void SelectPlayer() {
        player = choose;
        player.Select();
    }

    void UnselectPlayer() {
        if (this.player != null) {
            synchronized (player) {
                player.UnSelect();
                player.notifyAll();
            }
            if (player.ifExist() == true) {
                this.ChoosePlayer(player);
            } else {
                if (this.world.getRed().size() > 0) {
                    this.ChoosePlayer(this.world.getRed().get(0));
                }
            }
        }
        player = null;
    }

    @Override
    public Screen respondToUserMouse(MouseEvent mouseEvent) {
        if(gameStart == false){
            int x = cursorxToWorldx(mouseEvent.getX());
            int y = cursoryToWorldy(mouseEvent.getY());
            if(y >= world.HEIGHT){
                int index = x / 10;
                if(index < Shop.values().length){
                    this.shop = Shop.values()[index];
                }
                else{
                    this.shop = null;
                }
            }
            else{
                Thing temp = this.world.get(x, y);
                // if(temp.getName().equals(CreatureAttribute.FLOOR)){                
                if(temp.getName() == CreatureAttribute.FLOOR){     
                    if(this.shop != null && cost >= shop.cost()){
                        switch(this.shop){
                            case Lancer:
                                First f = new First(world, x, y, CreatureAttribute.REDTEAM);
                                world.getRed().add(f);
                                break;
                            case Archer:
                                Second s = new Second(world, x, y, CreatureAttribute.REDTEAM);
                                world.getRed().add(s);
                                break;
                        }
                        cost -= shop.cost();
                    }
                }
            }
        }
        return this;
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
}
