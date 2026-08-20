package com.kakstd.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.SubmarineGame;

import java.awt.geom.Point2D;


public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Point2D point;
    protected Body body;
    protected Fixture fixture;
    public boolean Destroyed = false;
    public int Health;
    public Vector2 wallPos;
    public InteractiveTileObject (World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((float) ((bounds.x + bounds.getWidth()/2)/ SubmarineGame.PPM), (float) ((bounds.y+bounds.getHeight()/2)/SubmarineGame.PPM));
        wallPos = new Vector2(bdef.position);
        body = world.createBody(bdef);
        shape.setAsBox((float) ((bounds.getWidth()/2)/SubmarineGame.PPM), (float) ((bounds.getHeight()/2)/SubmarineGame.PPM));
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }
    public abstract void collide_torpedo();
    public abstract Body getBody();
    public abstract void update();
    public abstract World getWorld();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
