package com.kakstd.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.SubmarineGame;


public class SpawnPoints extends InteractiveTileObject{
    @Override
    public void collide_torpedo(){}

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void update() {

    }

    @Override
    public World getWorld() {
        return null;
    }

    public SpawnPoints(World world, TiledMap map, Rectangle point){
        super(world,map,point);
        setCategoryFilter(SubmarineGame.DESTROY_BIT);

    }
}
