package com.example.thing;

import com.example.maze.World;

import java.awt.Color;

public class Thing implements CreatureAttribute{
    protected World world;

    // public World getWorld(){
    //     return world;
    // }

    public Tile<? extends Thing> tile;

    public int getX() {
        return this.tile.getxPos();
    }

    public int getY() {
        return this.tile.getyPos();
    }

    public void setxPos(int xPos){
        this.tile.setxPos(xPos);
    }

    
    public void setyPos(int yPos){
        this.tile.setyPos(yPos);
    }

    public void setTile(Tile<? extends Thing> tile) {
        this.tile = tile;
    }

    Thing(Color color, char glyph, World world) {
        this.color = color;
        this.glyph = glyph;
        this.world = world;
    }

    private Color color;

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    private char glyph;

    public char getGlyph() {
        return this.glyph;
    }

    public void setGlyph(char glyph){
        this.glyph = glyph;
    }


    protected String name;

    public String getName(){
        return this.name;
    }

    public int code;

    public int getCode(){
        return this.code;
    }

    public void setCode(int code){
        this.code = code;
    }
}
