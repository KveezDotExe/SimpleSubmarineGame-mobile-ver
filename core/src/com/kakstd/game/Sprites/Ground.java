package com.kakstd.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.SubmarineGame;

import org.lwjgl.Sys;


public class Ground extends InteractiveTileObject {
    public static Fixture f_fixture;
    public Rectangle wall;
    float w, h;
    public Ground (World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
        fixture.setUserData(this);
        f_fixture =fixture;
        setCategoryFilter(SubmarineGame.GROUND_BIT);
        wall = bounds;
        w = wall.width;
        h = wall.height;
        wall.height = wall.height/SubmarineGame.PPM;
        wall.width = wall.width/SubmarineGame.PPM;
        wall.setPosition(body.getPosition().x - wall.width/2, body.getPosition().y - wall.height/2);
    }

    @Override
    public void collide_torpedo() {
        System.out.println(wall.x + " " + wall.y);
        System.out.println(wall.width + " " + wall.height);


        System.out.println(body.getPosition().x + " " + body.getPosition().y);
        System.out.println(" " + body.getFixtureList().size);
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void update() {

    }

    @Override
    public World getWorld() {
        return world;
    }
}
