package com.kakstd.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.SubmarineGame;
import com.kakstd.game.Tools.Torpedo;


import java.util.LinkedList;


public class Torpedo_enemy extends Torpedo {
    private float start = 0;
    private float end = 1f;
    public static Fixture f_fixture;

    public Torpedo_enemy(World world, PlayScreen screen, Enemy player, float knobX, float knobY, int lvl) {
        super(world, screen, player, knobX, knobY, lvl);
        fixture.setUserData(this);
    }

    @Override
    public void Explosion() {
        Explose = true;
    }

    @Override
    public void update(float dt) {
        start += dt;
        if(start - end >= 0){
            Explose = true;
        }
    }
}

