package com.kakstd.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.SubmarineGame;

public class Submarine extends Sprite {
    public enum State {STANDING, SWIMMING}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2d;
    private TextureRegion submarineStand;
    private Animation submarineSwim;
    private float stateTimer;
    private boolean swimmingRight;
    int xPos, yPos;
    String type;
    public int Health;
    public Submarine(World world, PlayScreen screen, int xPos, int yPos, String type){
        super(screen.getAtlas().findRegion("WoodSubmarine"));
        this.xPos = xPos;
        this.yPos = yPos;
        this.world = world;
        this.type = type;
        defineSubmarine();
        //Animation
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        swimmingRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        switch (type) {
            case("Player"):
                for (int i = 583; i <= 622; i+=39) {
                    frames.add(new TextureRegion(getTexture(), i + 39, 0, 39, 25));

                }
                //def sensors
                FixtureDef fdef = new FixtureDef();
                CircleShape player_hitBox = new CircleShape();
                player_hitBox.setRadius(15/SubmarineGame.PPM);
                fdef.shape = player_hitBox;
                fdef.isSensor = true;
                b2d.createFixture(fdef).setUserData("player_box");
                //def idle pos
                submarineStand = new TextureRegion(getTexture(),622,0, 39,25);
                break;
        }
        submarineSwim = new Animation(0.1f,frames);
        frames.clear();
        setBounds(0,0, 39/SubmarineGame.PPM, 25/SubmarineGame.PPM);
        setRegion(submarineStand);

    }
    public void update(float dt){
        setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));

    }
    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case SWIMMING:
                region = (TextureRegion) submarineSwim.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = submarineStand;
                break;
        }
        if((b2d.getLinearVelocity().x <0 || !swimmingRight) && !region.isFlipX()){
            region.flip(true, false);
            swimmingRight = false;
        } else if ((b2d.getLinearVelocity().x > 0 ||swimmingRight) && region.isFlipX()) {
            region.flip(true,false);
            swimmingRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }
    public State getState(){
        if(b2d.getLinearVelocity().x != 0){
            return State.SWIMMING;
        }
        else {
            return State.STANDING;
        }
    }
    public void defineSubmarine(){
        BodyDef bdef = new BodyDef();
        bdef.linearDamping = 1f;
        bdef.position.set(xPos/ SubmarineGame.PPM,yPos/SubmarineGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2d = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = SubmarineGame.PLAYER_BIT;
        fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.ENEMY_BIT | SubmarineGame.GROUND_BIT;
        CircleShape shape = new CircleShape();
        shape.setRadius(15/SubmarineGame.PPM);
        fdef.shape = shape;
        b2d.createFixture(fdef);
    }
    public Body getBody(){
        return b2d;
    }
    public static void onGamage(){ Gdx.app.log("onDamage_player","damaged");}

    public void dispose(){
        world.dispose();
    }

}
