package com.example.screen;

import static org.junit.Assert.*;

import com.example.maze.World;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProcessScreenTest {

    World world;
    @Before
    public void setUp() throws Exception {
        world = new World();
    }

    @After
    public void tearDown() throws Exception {
        world = null;
    }

    @Test
    public void saveByThread() throws FileNotFoundException {
        ProcessScreen ps = new ProcessScreen(world);
        String process = "Create BlueTeam First 0 40 18";
        ps.addProcess(process);
        ps.saveByThread();
        BufferedReader inputStream = new BufferedReader(new FileReader("GameProcess.txt"));
        if(inputStream != null){
            try {
                String t = inputStream.readLine();
                assertEquals(process, t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}