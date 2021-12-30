package com.example.maze;

import javax.swing.text.StyledEditorKit.BoldAction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.Server;
import com.example.screen.ProcessScreen;
import com.example.thing.*;

public class World {

    // public static final int WIDTH = 40;
    // public static final int HEIGHT = 20;
    public static int WIDTH = 80;
    public static int HEIGHT = 40;
    private Tile<Thing>[][] tiles;
    private Thing[][] background;

    private MazeGenerator maze;
    private List<Creature> blueTeam;
    private List<Creature> redTeam;
    private List<String> process;

    private Server server = null;

    public World() {
        World.WIDTH = 80;
        World.HEIGHT = 40;
        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT]; 
        }

        if (background == null){
            background = new Thing[WIDTH][HEIGHT];
        }

        maze = new MazeGenerator(WIDTH, HEIGHT);
        maze.generateMaze();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                // int t = maze.PassByLoc(i, j);
                Thing t = getThingByrank(maze.PassByLoc(i, j));
                background[i][j] = t;
                tiles[i][j].setThing(t);
            }
        }

        blueTeam = new ArrayList<>();
        redTeam = new ArrayList<>();
        process = new ArrayList<>();

        // this.processByThread();
    }

    public World(boolean record) {
        World.WIDTH = 80;
        World.HEIGHT = 40;
        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT]; 
        }

        if (background == null){
            background = new Thing[WIDTH][HEIGHT];
        }

        maze = new MazeGenerator(WIDTH, HEIGHT);
        maze.generateMaze();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                // int t = maze.PassByLoc(i, j);
                Thing t = getThingByrank(maze.PassByLoc(i, j));
                background[i][j] = t;
                tiles[i][j].setThing(t);
            }
        }

        blueTeam = new ArrayList<>();
        redTeam = new ArrayList<>();
        process = new ArrayList<>();

        this.record = record;
        if(record == true){
            // ps = new ProcessScreen(this);
            this.processByThread();
        }
    }

    public World(boolean record, Server server){
        World.WIDTH = 80;
        World.HEIGHT = 40;
        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT]; 
        }

        if (background == null){
            background = new Thing[WIDTH][HEIGHT];
        }

        maze = new MazeGenerator(WIDTH, HEIGHT);
        maze.generateMaze();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                // int t = maze.PassByLoc(i, j);
                Thing t = getThingByrank(maze.PassByLoc(i, j));
                background[i][j] = t;
                tiles[i][j].setThing(t);
            }
        }

        blueTeam = new ArrayList<>();
        redTeam = new ArrayList<>();
        process = new ArrayList<>();
        this.server = server;

        this.record = record;
        this.serverworld = true;
        if(record == true){
            // ps = new ProcessScreen(this);
            this.processByThread();
        }
    }

    public World(String file) throws IOException {
        Scanner sc = new Scanner(new FileReader(file));
        if(sc.hasNextInt()){
            World.WIDTH = sc.nextInt();
            World.HEIGHT = sc.nextInt();
            
            maze = new MazeGenerator(WIDTH, HEIGHT);
            maze.generateMaze();
            
            if (tiles == null) {
                tiles = new Tile[WIDTH][HEIGHT]; 
            }
    
            if (background == null){
                background = new Thing[WIDTH][HEIGHT];
            }
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    tiles[i][j] = new Tile<>(i, j);
                    maze.setByLoc(i, j, sc.nextInt());
                    int k = maze.PassByLoc(i, j);
                    Thing t = getThingByrank(k);
                    background[i][j] = t;
                    tiles[i][j].setThing(t);
                }
            }
            sc.close();
        }
        blueTeam = new ArrayList<>();
        redTeam = new ArrayList<>();
    }

    public Thing get(int x, int y) {
        if (pointInWorld(x, y)){
            return this.tiles[x][y].getThing();
        }
        return null;
    }

    public void setBackground(int x, int y){
        tiles[x][y].setThing(background[x][y]);
    }

    public Thing getBackground(int x, int y) {
        if (pointInWorld(x, y)){
            return this.background[x][y];
        }
        return null;
    }


    public void put(Thing t, int x, int y) {
        if (pointInWorld(x, y)){
            // this.tiles[x][y].setThing(t);
            this.tiles[x][y].setThing(t);
        }
    }

    public Thing putPlayingThing(Thing t, int x, int y) {
        if (pointInWorld(x, y)){
            // this.tiles[x][y].setThing(t);
            return this.tiles[x][y].setPlayingThing(t);
        }
        else return null;
    }


    private Thing getThingByrank(int x){
        switch(x){
            case 0:return new Wall(this);
            case 1:return new Floor(this);
            default: return new Wall(this);
        }
    }

    private Boolean pointInWorld(int x, int y){
        return x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT;
    }

    public void addBlue(Creature creature){
        synchronized(this.blueTeam){
            this.blueTeam.add(creature);
        }
    }

    public List<Creature> getRed(){
        return redTeam;
    }

    public Thing findInRed(int code){
        for(Creature creature : this.redTeam){
            if(creature.code == code){
                return creature;
            }
        }
        return null;
    }

    public void addRed(Creature creature){
        synchronized(this.redTeam){
            this.redTeam.add(creature);
        }
    }

    public Thing findInBlue(int code){
        for(Creature creature : this.blueTeam){
            if(creature.code == code){
                return creature;
            }
        }
        return null;
    }

    public List<Creature> getBlue(){
        return blueTeam;
    }

    public List<String> saveGame(){
        List<String> result = new ArrayList<>();
        for(Creature creature : redTeam){
            List<String> Info = creature.getInfo();
            if(Info != null)
                result.addAll(Info);
        }
        for(Creature creature : blueTeam){
            List<String> Info = creature.getInfo();
            if(Info != null)
                result.addAll(Info);
        }
        return result;
    }

    public List<String> saveMap(){
        List<String> result = new ArrayList<>();
        String info = World.WIDTH + " " + World.HEIGHT;
        result.add(info);
        for (int i = 0; i < WIDTH; i++) {
            info = "";
            for (int j = 0; j < HEIGHT; j++) {
                // System.out.println(info);
                // System.out.println(i + " " + j);
                info = info + maze.PassByLoc(i, j) + " ";
            }
            result.add(info);
        }
        return result;
    }

    private ProcessScreen ps;

    private void processByThread(){
        class TempRun implements Runnable{
            World world;
            TempRun(World world){
                this.world = world;
            }
            @Override 
            public void run(){
                ps = new ProcessScreen(this.world);
            }
        }
        // TempRun temprun = new TempRun();
        TempRun temprun = new TempRun(this);
        Thread t1 = new Thread(temprun);
        t1.start();
        try {
            t1.join();
            // System.out.println("join");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    synchronized public void addProcess(String process){
        if(Debug.DebugProcess){
            System.out.println("World:" + process);
        }
        if(ps != null)
            ps.addProcess(process);
        if(server != null){
            server.writeToClient(process);

        }
    }

    private boolean record = false;

    public boolean ifRecord(){
        return this.record;
    }

    private boolean serverworld = false;

    public boolean ifServer(){
        return this.serverworld;
    }
}
