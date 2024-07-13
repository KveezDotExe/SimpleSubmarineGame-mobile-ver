package com.kakstd.game.Sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Tools.Collectables;

public class Scrap extends Collectables {
    public Scrap(World world, PlayScreen screen, Vector2 pos, String type){
        super(world, screen, pos, type);
        fixture.setUserData(this);
    }
    @Override
    public void collect() {
        PlayScreen.collected_scrap += 10;
        collected = true;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Body getBody() {
        return b2d;
    }
}
