package com.kakstd.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.SubmarineGame;


public class Ground extends InteractiveTileObject {
    public static Fixture f_fixture;
    public Ground (World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
        fixture.setUserData(this);
        f_fixture =fixture;
        setCategoryFilter(SubmarineGame.GROUND_BIT);
    }

    @Override
    public void collide_torpedo() {
        Gdx.app.log("Ground","Collided");
    }

    @Override
    public Body getBody() {
        return body;
    }
}
