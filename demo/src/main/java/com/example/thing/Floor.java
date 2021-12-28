package com.example.thing;

import com.example.maze.World;

import java.awt.Color;

public class Floor extends Thing {

    public Floor(World world) {
        super(Color.gray, (char) 0, world);
        this.name = FLOOR;
    }

}
