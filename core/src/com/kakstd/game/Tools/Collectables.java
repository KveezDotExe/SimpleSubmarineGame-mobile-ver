package com.kakstd.game.Tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.SubmarineGame;

public abstract class Collectables extends Sprite {
    protected World world;
    private String type;
    private Vector2 pos;
    protected Body b2d;
    public boolean collected = false;
    protected Fixture fixture;

    public Collectables(World world, PlayScreen screen, Vector2 pos, String type){
        super(screen.getAtlas().findRegion(type));

        this.world = world;
        this.pos = pos;
        this.type = type;
        switch (type){
            case("Ruby"):
                setScale(0.5f/ SubmarineGame.PPM);
                BodyDef bdef = new BodyDef();
                bdef.linearDamping = 1f;
                bdef.position.set(pos);
                bdef.type = BodyDef.BodyType.DynamicBody;
                FixtureDef fdef = new FixtureDef();
                fdef.filter.categoryBits = SubmarineGame.COLLECTABLE_BIT;
                fdef.filter.maskBits = SubmarineGame.GROUND_BIT;
                b2d = world.createBody(bdef);
                CircleShape shape = new CircleShape();
                shape.setRadius(8/SubmarineGame.PPM);
                fdef.shape = shape;
                b2d.createFixture(fdef);
                shape.setRadius(9/SubmarineGame.PPM);
                fdef.filter.categoryBits = SubmarineGame.COLLECTABLE_BIT;
                fdef.filter.maskBits = SubmarineGame.PLAYER_BIT;
                fdef.shape = shape;
                fixture = b2d.createFixture(fdef);
                applyLinearImpulse();
                break;
            case("Scrap"):
                setScale(1/ SubmarineGame.PPM);
                bdef = new BodyDef();
                bdef.linearDamping = 1f;
                bdef.position.set(pos);
                bdef.type = BodyDef.BodyType.DynamicBody;
                fdef = new FixtureDef();
                fdef.filter.categoryBits = SubmarineGame.COLLECTABLE_BIT;
                fdef.filter.maskBits = SubmarineGame.GROUND_BIT;
                b2d = world.createBody(bdef);
                shape = new CircleShape();
                shape.setRadius(8/SubmarineGame.PPM);
                fdef.shape = shape;
                b2d.createFixture(fdef);
                shape.setRadius(9/SubmarineGame.PPM);
                fdef.filter.categoryBits = SubmarineGame.COLLECTABLE_BIT;
                fdef.filter.maskBits = SubmarineGame.PLAYER_BIT;
                fdef.shape = shape;
                fixture = b2d.createFixture(fdef);
                applyLinearImpulse();
        }

    }
    public void applyLinearImpulse(){
        b2d.applyLinearImpulse(new Vector2(MathUtils.random(-2,2), MathUtils.random(-2,2)).nor().scl(1f), b2d.getWorldCenter(), true);
    }
    public void update(){
        setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        draw(SubmarineGame.batch);
    }
    public void dispose(){
        world.destroyBody(b2d);
    }
    public abstract void collect();
    public abstract World getWorld();
    public abstract Body getBody();
}