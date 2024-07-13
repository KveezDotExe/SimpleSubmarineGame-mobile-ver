package com.kakstd.game.Tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Sprites.Enemy;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.SubmarineGame;

public abstract class Torpedo extends Sprite {

    public World world;
    public Body b2d;
    protected FixtureDef fdef;
    private Enemy npc;
    private Player player;
    private TextureRegion torpedoTex;
    private float speed;
    private float startShoot = 0;
    private float nextShoot = 5;
    private float rt = 0;
    protected Fixture fixture;
    public boolean Explose = false;

    public float scaleX, scaleY;

    // ENEMY TORPEDO
    public Torpedo (World world, PlayScreen screen, Enemy npc, float knobX, float knobY, int lvl){
        super(screen.getAtlas().findRegion("Torpedo_Meksikano"));
        switch (lvl) {
            case(1):
                setOriginCenter();
                torpedoTex = new TextureRegion(getTexture(), 335, 16, 16, 16);
                this.world = world;
                this.npc = npc;
                speed = 4;
                rt = new Vector2(knobX, knobY).angleDeg();
                BodyDef bdef = new BodyDef();
                bdef.linearDamping = 1f;
                bdef.position.set((npc.b2d.getPosition().x), (npc.b2d.getPosition().y));
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2d = world.createBody(bdef);
                fdef = new FixtureDef();
                fdef.isSensor = true;
                fdef.filter.categoryBits = SubmarineGame.ENEMY_BULLET_BIT;
                fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.GROUND_BIT | SubmarineGame.PLAYER_BIT | SubmarineGame.BULLET_BIT | SubmarineGame.ORE_BIT;
                PolygonShape shape = new PolygonShape();
                shape.setAsBox((float) ((16 / 2) / SubmarineGame.PPM), (float) ((16 / 2) / SubmarineGame.PPM));
                fdef.shape = shape;
                fixture = b2d.createFixture(fdef);
                b2d.applyLinearImpulse(new Vector2(knobX, knobY).nor().scl(speed), b2d.getWorldCenter(), true);
                Vector2 linearVelocity = getLinearVelocity();
                float newOrientation = vectorToAngle(linearVelocity);
                b2d.setTransform(b2d.getPosition(), newOrientation);
                scaleX = 1;
                scaleY = 1;
                break;
        }
    }

    // PLAYER TORPEDO
    public Torpedo (World world, PlayScreen screen, Player player, float knobX, float knobY, int lvl){
        super(screen.getAtlas().findRegion("Torpedo_Meksikano"));
        switch (lvl) {
            case(1):
                setOriginCenter();
                torpedoTex = new TextureRegion(getTexture(), 335, 16, 16, 16);
                this.world = world;
                this.player = player;
                speed = 4;
                rt = new Vector2(knobX, knobY).angleDeg();
                BodyDef bdef = new BodyDef();
                bdef.linearDamping = 1f;
                bdef.position.set((player.b2d.getPosition().x), (player.b2d.getPosition().y));
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2d = world.createBody(bdef);
                fdef = new FixtureDef();
                fdef.isSensor = true;
                fdef.filter.categoryBits = SubmarineGame.BULLET_BIT;
                fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.GROUND_BIT | SubmarineGame.ENEMY_BIT | SubmarineGame.ENEMY_BULLET_BIT | SubmarineGame.ORE_BIT;
                PolygonShape shape = new PolygonShape();
                shape.setAsBox((float) ((16 / 2) / SubmarineGame.PPM), (float) ((16 / 2) / SubmarineGame.PPM));
                fdef.shape = shape;
                fixture = b2d.createFixture(fdef);
                b2d.applyLinearImpulse(new Vector2(knobX, knobY).nor().scl(speed), b2d.getWorldCenter(), true);
                Vector2 linearVelocity = getLinearVelocity();
                float newOrientation = vectorToAngle(linearVelocity);
                b2d.setTransform(b2d.getPosition(), newOrientation);
                scaleX = 1;
                scaleY = 1;
                break;
            case (2):
                setOriginCenter();
                torpedoTex = new TextureRegion(getTexture(), 156, 0, 64, 32);
                this.world = world;
                this.player = player;
                speed = 2;
                rt = new Vector2(knobX, knobY).angleDeg();
                bdef = new BodyDef();
                bdef.linearDamping = 1f;
                bdef.position.set((player.b2d.getPosition().x), (player.b2d.getPosition().y));
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2d = world.createBody(bdef);
                fdef = new FixtureDef();
                fdef.isSensor = true;
                fdef.filter.categoryBits = SubmarineGame.BULLET_BIT;
                fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.GROUND_BIT | SubmarineGame.ENEMY_BIT | SubmarineGame.ENEMY_BULLET_BIT | SubmarineGame.ORE_BIT;
                shape = new PolygonShape();
                shape.setAsBox((float) ((32 / 2) / SubmarineGame.PPM), (float) ((64 / 2) / SubmarineGame.PPM));
                fdef.shape = shape;
                fixture = b2d.createFixture(fdef);
                b2d.applyLinearImpulse(new Vector2(knobX, knobY).nor().scl(speed), b2d.getWorldCenter(), true);
                linearVelocity = getLinearVelocity();
                newOrientation = vectorToAngle(linearVelocity);
                b2d.setTransform(b2d.getPosition(), newOrientation);
                scaleX = 3.5f;
                scaleY = 2;
                break;
        }
    }

    @Override
    public void draw(Batch batch){
        batch.draw(torpedoTex, b2d.getPosition().x-(getWidth()/2)/SubmarineGame.PPM, b2d.getPosition().y-(getHeight()/2)/SubmarineGame.PPM, (getWidth()/2)/SubmarineGame.PPM,
                (getHeight()/2)/SubmarineGame.PPM, getWidth()/SubmarineGame.PPM,
                getHeight()/SubmarineGame.PPM, scaleX,scaleY,rt);
    }

    public abstract void Explosion();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    public float vectorToAngle(Vector2 vector){
        return SteeringUtils.vectorToAngle(vector);
    }
    public Vector2 getLinearVelocity(){
        return b2d.getLinearVelocity();
    }
    public void dispose(){
        world.dispose();
    }
}
