package com.kakstd.game.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.SubmarineGame;

public class RubyOre extends InteractiveTileObject{
    Texture rubyOre;
    Sprite sprite;
    @Override
    public void collide_torpedo() {
        Health = Health - 20;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void update() {
        sprite.setPosition(body.getPosition().x - sprite.getWidth()/2,body.getPosition().y - sprite.getHeight()/2);
        sprite.draw(SubmarineGame.batch);
    }

    @Override
    public World getWorld() {
        return world;
    }

    public  RubyOre (World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
        Health = 60;
        fixture.setUserData(this);
        setCategoryFilter(SubmarineGame.ORE_BIT);
        rubyOre = new Texture(Gdx.files.internal("Collectable/RubyOre.png"));
        sprite = new Sprite(rubyOre);
        sprite.setScale(1/SubmarineGame.PPM);

    }
    public void dispose(){
        world.destroyBody(body);
    }

}
