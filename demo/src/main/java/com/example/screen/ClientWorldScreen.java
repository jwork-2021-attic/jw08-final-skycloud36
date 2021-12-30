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
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.plaf.synth.SynthEditorPaneUI;

import com.example.thing.*;

// import org.w3c.dom.events.MouseEvent;
import com.example.maze.World;

import com.example.asciiPanel.AsciiPanel;

public class ClientWorldScreen implements Screen {

    volatile public boolean gameStart = false;
    volatile public boolean gamePause = false;
    private World world;
    // Player player;
    Shop shop = null;
    Creature player = null;
    Creature choose = null;
    int index = 0;
    int cost = 500;
    List<Button> buttons = new ArrayList<>();
    boolean team;
    int buttonIndex = 0;

    private void makeTeam(){
        // Second b1 = new Second(world, 70, 27,CreatureAttribute.BLUETEAM);   world.addBlue(b1);
        // Second b2 = new Second(world, 70, 16,CreatureAttribute.BLUETEAM);    world.addBlue(b2);
        // Second b3 = new Second(world, 70, 19,CreatureAttribute.BLUETEAM);    world.addBlue(b3);
        // Second b4 = new Second(world, 70, 2,CreatureAttribute.BLUETEAM);   world.addBlue(b4);
        // Second b5 = new Second(world, 70, 6,CreatureAttribute.BLUETEAM);    world.addBlue(b5);
        // Second b6 = new Second(world, 70, 35,CreatureAttribute.BLUETEAM);    world.addBlue(b6);
        // First b7 = new First(world, 40, 20,CreatureAttribute.BLUETEAM);   world.addBlue(b7);
        // First b8 = new First(world, 40, 21, CreatureAttribute.BLUETEAM);     world.addBlue(b8);
        // First b9 = new First(world, 40, 25,CreatureAttribute.BLUETEAM);   world.addBlue(b9);
        // First b10 = new First(world, 40, 18, CreatureAttribute.BLUETEAM);     world.addBlue(b10);
    }

    SocketChannel readChannel;
    SocketChannel writeChannel;
    private Selector selector;
    private ByteBuffer byteBuffer;

    public ClientWorldScreen() {
        try {
            // readChannel = SocketChannel.open();
            // readChannel.connect(new InetSocketAddress(8888));
            world = new World();
            selector = Selector.open();
            byteBuffer = ByteBuffer.allocate(1024);
            writeChannel = SocketChannel.open();
            writeChannel.connect(new InetSocketAddress(8888));
            if(writeChannel.isConnected()){
                writeChannel.configureBlocking(false);
                writeChannel.register(selector, SelectionKey.OP_READ);
                handleSocketByThread();
                writeToServer("hello server\n");
            }
            // TimeUnit.MILLISECONDS.sleep(1000);
            // socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeClient(){
        if(writeChannel != null){
            try {
                writeChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listen(){
        try{
            boolean listen = true;
            while(listen){
                // System.out.println(111);
                if(selector.select() == 0){
                    // System.out.println(222);
                    continue;
                }
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    if(key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        listen = readData(socketChannel);
                    }
                }
                it.remove();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean readData(SocketChannel socketChannel) throws IOException{
        int res = socketChannel.read(byteBuffer);
        if(res > 0){
            List<String> process = bufToString(byteBuffer);
            for(int i = 0; i < process.size(); i++){
                System.out.println(socketChannel.getLocalAddress()+ ":" + process.get(i));
                handleProcess(process.get(i));
            }
        }
        else if(res == -1){
            return false;
        }
        return true;
    }

    public List<String> bufToString(ByteBuffer buf){
        List<String> process = new ArrayList<>();

        buf.flip();
        Charset charset = Charset.forName("utf-8");
        CharBuffer charBuffer = charset.decode(buf);
        String temp = charBuffer.toString();
        
        String[] Process = temp.split("\n");
        System.out.println("length:" + Process.length + "\ntemp:" + temp);
        int index = 0;
        for(int i = 0; i < Process.length; i++){
            if(temp.charAt(index + Process[i].length()) == '\n'){
                process.add(Process[i]);
                index += Process[i].length()+1;
            }
        }
        buf.position(index);
        buf.compact();
        return process;
    } 

    public void handleSocketByThread(){
        class myRun implements Runnable{
            @Override
            public void run(){
                listen();
            }
        }
        Thread myThread = new Thread(new myRun());
        myThread.start();
    }

    public void writeToServer(String t){
        t = t + "\n";
        if(writeChannel.isConnected()){
            try {
                ByteBuffer buffer = ByteBuffer.wrap(t.getBytes("utf-8"));
                writeChannel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                // if(!(world.getBackground(x, y).getName().equals("Floor") || world.getBackground(x, y).getName().equals("Wall"))){
                //     System.out.println(world.getBackground(x, y).getName() + " " + x + " " + y);
                // }
                terminal.write(world.getBackground(x, y).getGlyph(), x, y, world.get(x, y).getColor());
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
        if (this.gameStart == false) {
            // switch (key.getKeyCode()) {
            //     case KeyEvent.VK_ENTER:
            //         this.gameStart();
            //         break;
            // }
            String process = "Key GameStart";
            writeToServer(process);
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
                    // player.moveLeft();
                    writeToServer("Key LEFT");
                    break;
                case KeyEvent.VK_RIGHT:
                    // player.moveRight();
                    writeToServer("Key RIGHT");
                    break;
                case KeyEvent.VK_UP:
                    // player.moveUp();
                    writeToServer("Key UP");
                    break;
                case KeyEvent.VK_DOWN:
                    // player.moveDown();
                    writeToServer("Key DOWN");
                    break;
                case KeyEvent.VK_SPACE:
                    // player.Attack();
                    writeToServer("Key Attack");
                    break;
                case KeyEvent.VK_Q:
                    // this.UnselectPlayer();
                    writeToServer("Key Q");
                    break;
                // case KeyEvent.VK_ESCAPE:
                //     if (this.gamePause == false) {
                //         gamePause();
                //     } else {
                //         gameUnPause();
                //     }
                //     break;
            }
        }
    }
    
    private void handleInputPlayerIsNull(KeyEvent key){
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(this.world.getRed().size() > 0){
                    if(this.team == true){
                        index = (index - 1 + this.world.getRed().size()) % this.world.getRed().size();
                        this.ChoosePlayer(this.world.getRed().get(index % this.world.getRed().size()));
                    }
                    else{
                        index = (index - 1 + this.world.getBlue().size()) % this.world.getBlue().size();
                        this.ChoosePlayer(this.world.getBlue().get(index % this.world.getBlue().size()));
                    }
                }
                break;
            case KeyEvent.VK_DOWN:
            if(this.world.getRed().size() > 0){
                if(this.team == true){
                    index = (index + 1 + this.world.getRed().size()) % this.world.getRed().size();
                    this.ChoosePlayer(this.world.getRed().get(index % this.world.getRed().size()));
                }
                else{
                    index = (index + 1 + this.world.getBlue().size()) % this.world.getBlue().size();
                    this.ChoosePlayer(this.world.getBlue().get(index % this.world.getBlue().size()));
                }
            }
                break;
            case KeyEvent.VK_ENTER:
                if (choose != null && choose.ifExist()) {
                    player = choose;
                    player.Select();
                    String process = "Key SelectPlayer " +  choose.getCode();
                    // choose.getTeam() + " " + choose.getName() + " " +
                    writeToServer(process);
                }
                break;
            // case KeyEvent.VK_ESCAPE:
            //     if (this.gamePause == false) {
            //         gamePause();
            //     } else {
            //         gameUnPause();
            //     }
            //     break;
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
                String mouseProcess = "Mouse " + x + " " + y;
                writeToServer(mouseProcess);
            }
            else{
                Thing temp = this.world.get(x, y);
                // if(temp.getName().equals(CreatureAttribute.FLOOR)){                
                if(temp.getName() == CreatureAttribute.FLOOR){    
                    if(shop != null && cost >= shop.cost()){
                        cost -= shop.cost();
                        String mouseProcess = "Mouse " + x + " " + y;
                        writeToServer(mouseProcess);
                    }
                }
            }
            // else{
            //     Thing temp = this.world.get(x, y);
            //     // if(temp.getName().equals(CreatureAttribute.FLOOR)){                
            //     if(temp.getName() == CreatureAttribute.FLOOR){     
            //         if(this.shop != null && cost >= shop.cost()){
            //             switch(this.shop){
            //                 case Lancer:
            //                     First f = new First(world, x, y, CreatureAttribute.REDTEAM);
            //                     world.getRed().add(f);
            //                     break;
            //                 case Archer:
            //                     Second s = new Second(world, x, y, CreatureAttribute.REDTEAM);
            //                     world.getRed().add(s);
            //                     break;
            //             }
            //             cost -= shop.cost();
            //         }
            //     }
            // }
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

    private void handleProcess(String process){
        // System.out.println(process);
        String[] Process = process.split(" ");
        switch(Process[0]){
            case "Create":this.handleCreate(Process);break;
            case "Move":this.handleMove(Process);break;
            case "Attack":this.handleAttack(Process);break;
            case "Dead":this.handleDead(Process);break;
            case "Ready":this.handleReady(Process);break;
            case "Client":this.handleClient(Process[1]);break;
        }
        // if(Process[0].equals("Create")){
        //     this.handleCreate(Process);
        // }
        // else if(Process[0].equals("Move")){
        //     this.handleMove(Process);
        // }
        // else if(Process[0].equals("Attack")){
        //     this.handleAttack(Process);
        // }
        // else if(Process[0].equals("Dead")){
        //     this.handleDead(Process);
        // }
        // else if(Process[0].equals("Ready")){
        //     this.handleReady(Process);
        // }
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
                Thing owner = world.findInRed(Integer.valueOf(Process[6]));
                if(owner != null){
                    Thing temp = owner.findBullet(Integer.valueOf(Process[3]));
                    if(temp != null){
                        temp.setxPos(temp.getX()+Integer.valueOf(Process[4]));
                        temp.setyPos(temp.getY()+Integer.valueOf(Process[5]));
                    }
                }
            }
            else{
                Thing owner = world.findInBlue(Integer.valueOf(Process[6]));
                if(owner != null){
                    Thing temp = owner.findBullet(Integer.valueOf(Process[3]));
                    if(temp != null){
                        temp.setxPos(temp.getX()+Integer.valueOf(Process[4]));
                        temp.setyPos(temp.getY()+Integer.valueOf(Process[5]));
                    }
                }
            }
        }
    }

    private void handleAttack(String[] Process){
        if(Process[2].equals(CreatureAttribute.SECOND) == false){
            if(Process[2].equals(CreatureAttribute.BULLET)){
                if(Process[1].equals(CreatureAttribute.REDTEAM)){
                    Thing owner = world.findInRed(Integer.valueOf(Process[4]));
                    if(owner != null){
                        Thing attacker = owner.findBullet(Integer.valueOf(Process[3]));
                        Thing victim = world.findInBlue(Integer.valueOf(Process[7]));
                        if(attacker != null && victim != null)
                        attacker.Attack(victim);
                    }
                    // Thing attacker = world.findInRed(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
                    // Thing victim = world.findInBlue(Integer.valueOf(Process[7]));
                    // if(attacker != null && victim != null)
                    //     attacker.Attack(victim);
                }
                else{
                    Thing owner = world.findInBlue(Integer.valueOf(Process[4]));
                    if(owner != null){
                        Thing attacker = owner.findBullet(Integer.valueOf(Process[3]));
                        Thing victim = world.findInRed(Integer.valueOf(Process[7]));
                        if(attacker != null && victim != null)
                        attacker.Attack(victim);
                    }
                    // Thing attacker = world.findInBlue(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
                    // Thing victim = world.findInRed(Integer.valueOf(Process[7]));
                    // if(attacker != null && victim != null)
                    //     attacker.Attack(victim);
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
                    Thing attacker = world.findInBlue(Integer.valueOf(Process[3]));
                    Thing victim = world.findInRed(Integer.valueOf(Process[6]));
                    if(attacker != null && victim != null)
                        attacker.Attack(victim);
                }
            }
        }
        else{
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Thing attacker = world.findInRed(Integer.valueOf(Process[3]));
                attacker.changeToward(Integer.valueOf(Process[4]));
            }
            else{
                Thing attacker = world.findInBlue(Integer.valueOf(Process[3]));
                attacker.changeToward(Integer.valueOf(Process[4]));
            }
        }
    }

    private void handleDead(String[] Process){
        if(Process[2].equals(CreatureAttribute.BULLET)){
            if(Process[1].equals(CreatureAttribute.REDTEAM)){
                Thing owner = world.findInRed(Integer.valueOf(Process[4]));
                if(owner != null){
                    Thing temp = owner.findBullet(Integer.valueOf(Process[3]));
                    if(temp != null){
                        temp.beDead();
                    }
                }
            }
            else{
                Thing owner = world.findInBlue(Integer.valueOf(Process[4]));
                if(owner != null){
                    Thing temp = owner.findBullet(Integer.valueOf(Process[3]));
                    if(temp != null){
                        temp.beDead();
                    }
                }
            }
            // if(Process[1].equals(CreatureAttribute.REDTEAM)){
            //     Thing temp = world.findInRed(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
            //     temp.beDead();
            // }
            // else{
            //     Thing temp = world.findInBlue(Integer.valueOf(Process[4])).findBullet(Integer.valueOf(Process[3]));
            //     temp.beDead();
            // }
        }
    }

    public void handleReady(String[] Process){
        if(Process[1].equals("GameStart")){
            this.gameStart = true;
        }
    }

    public void handleClient(String client){
        if(client.equals("client1")){
            this.team = true;
        }
        else{
            this.team = false;
        }
    }
}
