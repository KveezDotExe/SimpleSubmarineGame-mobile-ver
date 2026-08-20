package com.kakstd.game.Tools;

import com.badlogic.gdx.math.Rectangle;
import com.kakstd.game.SubmarineGame;

import java.util.LinkedList;

public class Chunks {
    public LinkedList<Rectangle> containWalls;
    public Rectangle chunk;
    private boolean loaded;
    public Chunks(Rectangle rectangle){
        containWalls = new LinkedList<>();
        chunk = rectangle;
        chunk.width = chunk.width/SubmarineGame.PPM;
        chunk.height = chunk.height/SubmarineGame.PPM;
        chunk.setPosition(chunk.x/SubmarineGame.PPM, chunk.y/SubmarineGame.PPM);

    }
    public void setLoaded(boolean bl){
        loaded = bl;
    }
    public boolean isLoaded(){
        return loaded;
    }
}
