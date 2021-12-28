package com.example.thing;

public class Tile<T extends Thing> {

    private T thing;
    private int xPos;
    private int yPos;

    public T getThing() {
        return thing;
    }

    public void setThing(T thing) {
        synchronized(this){
            this.thing = thing;
            
            // this.thing.setTile(this);
            thing.setTile(this);
        }
    }

    public Thing setPlayingThing(T thing){
        synchronized(this){
            // try {
            //     if(this.getThing() instanceof Floor){
            //         this.thing = thing;
            //         thing.setTile(this);
            //         Thread.sleep(1000);
            //     }
            // } catch (InterruptedException e) {
            //     System.out.println(Thread.currentThread().getId() + "tile" + this.toString());
            //     e.printStackTrace();
            // }
            // return true;
            if(this.getThing() instanceof Floor){
                this.thing = thing;
                thing.setTile(this);
                return null;
            }
            else return this.getThing();
        }
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public Tile() {
        this.xPos = -1;
        this.yPos = -1;
    }

    public Tile(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

}
