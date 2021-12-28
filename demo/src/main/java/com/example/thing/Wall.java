package com.example.thing;

import com.example.maze.World;

import com.example.asciiPanel.AsciiPanel;

public class Wall extends Thing {

    public Wall(World world) {
        super(AsciiPanel.cyan, (char) 177, world);
        this.name = WALL;
    }

}
