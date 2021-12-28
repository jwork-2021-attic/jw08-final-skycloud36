package com.example.screen;

import java.awt.Color;
// import java.awt.event.KeyEvent;
// import java.util.ArrayList;
// import java.util.List;

import com.example.asciiPanel.AsciiPanel;

public class Button{
    String buttonName;
    int xloc, yloc;
    Color frontColor, backColor;
    AsciiPanel AP;

    public Button(String name, int xloc, int yloc, AsciiPanel AP){
        buttonName = name;
        this.xloc = xloc;
        this.yloc = yloc;
        this.frontColor = Color.GRAY;
        this.backColor = Color.BLACK;
        this.AP = AP;
    }

    public Button(String name, int xloc, int yloc, Color frontColor, Color backColor, AsciiPanel AP){
        buttonName = name;
        this.xloc = xloc;
        this.yloc = yloc;
        this.frontColor = frontColor;
        this.backColor = backColor;
        this.AP = AP;
    }
    public String getButtonName(){
        return this.buttonName;
    }

    public int getXloc(){
        return this.xloc;
    }

    public int getYloc(){
        return this.yloc;
    }

    public Color getFrontColor(){
        return this.frontColor;
    }

    public Color getBackColor(){
        return this.backColor;
    }

    public void setButtonName(String name){
        this.buttonName = name;
    }

    public void setFrontColor(Color color){
        this.frontColor = color;
    }

    public void setBackColor(Color color){
        this.backColor = color;
    }

    public void setXloc(int xloc){
        this.xloc = xloc;
    }

    public void setYloc(int yloc){
        this.yloc = yloc;
    }

    public void setAP(AsciiPanel AP){
        this.AP = AP;
    }
    
    public void select(){
        this.frontColor = Color.WHITE;
        this.backColor = Color.GRAY;
    }

    public void unselect(){
        this.frontColor = Color.GRAY;
        this.backColor = Color.BLACK;
    }

    public void display(){
        AP.write(buttonName, xloc, yloc, frontColor, backColor);
    }
}
