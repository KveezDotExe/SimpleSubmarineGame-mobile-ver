package com.kakstd.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.SubmarineGame;
import com.kakstd.game.Tools.EnemiesSteeringBehaviour;

import java.util.LinkedList;

public class Enemies extends Sprite{




    public enum State {STANDING, SWIMMING}
    public boolean isAlive = true;
    public Submarine.State currentState;
    public Submarine.State previousState;
    public World world;
    public Body b2d;
    private TextureRegion submarineStand;
    private Animation submarineSwim;
    private float stateTimer;
    private boolean swimmingRight;
    Vector2 pos;
    String type;
    public static FixtureDef fire_area_fixture;
    public static CircleShape fire_area_shape;
    protected  float playerXpos, playerYpos;
    Circle ar;
    private float fStart = 0;
    private final float fEnd = 0.125f;
    private float targetX, targetY;
    private PlayScreen screen;
    public Vector2 p1 = new Vector2(), p2 = new Vector2(), collision = new Vector2(), normal = new Vector2();
    private boolean decideShoot = false;
    Body playerBody;
    EnemiesSteeringBehaviour enemy, target;
    Wander<Vector2> wander;
    Arrive<Vector2> arrive;
    public static int Health;

    public Enemies (World world, PlayScreen screen, Vector2 pos, String type, Body playerBody){
        super(screen.getAtlas().findRegion("Enemy"));
        this.world = world;
        this.pos = pos;
        this.type = type;
        this.screen = screen;
        this.playerBody = playerBody;
        defineSubmarine();
        //Animation
        currentState = Submarine.State.STANDING;
        previousState = Submarine.State.STANDING;
        stateTimer = 0;
        swimmingRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        switch (type) {
            case ("enemy_lvl_1"):
                for (int i = 505; i < 583; i += 39) {
                    frames.add(new TextureRegion(getTexture(), i + 39, 0, 39, 25));

                }
                //def sensors
                fire_area_fixture = new FixtureDef();
                fire_area_shape = new CircleShape();
                fire_area_shape.setRadius(500f / SubmarineGame.PPM);
                fire_area_fixture.isSensor = true;
                fire_area_fixture.shape = fire_area_shape;
                b2d.createFixture(fire_area_fixture).setUserData("area");
                CircleShape enemy_hitBox = new CircleShape();
                enemy_hitBox.setRadius(15 / SubmarineGame.PPM);
                fire_area_fixture.shape = enemy_hitBox;
                fire_area_fixture.isSensor = true;
                b2d.createFixture(fire_area_fixture).setUserData("enemy_box");
                //def idle pos
                submarineStand = new TextureRegion(getTexture(), 544, 0, 39, 25);
                //def stats
                Health = 100;
                enemy = new EnemiesSteeringBehaviour(b2d, 15/SubmarineGame.PPM);
                enemy.setLinearSpeed(0.1f);
                enemy.setMaxLinearSpeed(1f);
                target = new EnemiesSteeringBehaviour(playerBody, 15/SubmarineGame.PPM);
                wander = new Wander<>(enemy).setFaceEnabled(false).setWanderOffset(50f/SubmarineGame.PPM).setWanderOrientation(0f).setWanderRadius(10f/SubmarineGame.PPM).setWanderRate(MathUtils.PI2 * 16);
                arrive = new Arrive<>(enemy, target).setTimeToTarget(0.01f).setArrivalTolerance(2f/SubmarineGame.PPM).setDecelerationRadius(10/SubmarineGame.PPM);
                enemy.setBehavior(arrive);
                break;
        }
        submarineSwim = new Animation(0.1f,frames);
        frames.clear();
        setBounds(0,0, 39/SubmarineGame.PPM, 25/SubmarineGame.PPM);
        setRegion(submarineStand);
        //detecting
        ar = new Circle();
        ar.setRadius(500f/SubmarineGame.PPM);


    }
    public void update(float dt, float x, float y){
        if(Health <=0 ){
            isAlive = false;
        }
        setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));
        ar.setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        targetX = x;
        targetY = y;
        if (ar.contains(x,y)) {

            playerXpos = x - b2d.getPosition().x;
            playerYpos = y - b2d.getPosition().y;
        }
        fStart += dt;

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
    public Submarine.State getState(){
        if(b2d.getLinearVelocity().x != 0){
            return Submarine.State.SWIMMING;
        }
        else {
            return Submarine.State.STANDING;
        }
    }
    public void defineSubmarine(){
        BodyDef bdef = new BodyDef();
        bdef.linearDamping = 1f;
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2d = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = SubmarineGame.ENEMY_BIT;
        fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.PLAYER_BIT | SubmarineGame.GROUND_BIT;
        CircleShape shape = new CircleShape();
        shape.setRadius(15/SubmarineGame.PPM);
        fdef.shape = shape;
        b2d.createFixture(fdef);
    }
    public static void onDamage(){
        Health = Health - 20;
    }
    RayCastCallback callback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if(!(fixture.getUserData() == "bullet") && fixture.getUserData() != null && fixture.getUserData().getClass() == Ground.class) {
                collision.set(point);
                Enemies.this.normal.set(normal).add(point);
                decideShoot = false;
                enemy.setBehavior(wander);
                return 0;
            }else {
                enemy.setBehavior(arrive);
                decideShoot = true;
                return 1;
            }
        }
    };

    public void AI(LinkedList<Torpedo_enemy> ammo, PlayScreen screen, Enemies enem, Torpedo_enemy torpedoEnemy, float dt){
        if(ar.contains(targetX,targetY)) {
            enemy.update(dt);
            GdxAI.getTimepiece().update(dt);
            world.rayCast(callback, p1, p2);
            if (fStart - fEnd >= 0 && decideShoot) {
                ammo.add(torpedoEnemy = new Torpedo_enemy(world, screen, enem, playerXpos, playerYpos));
                fStart = 0;
            }
            playerXpos = 0;
            playerYpos = 0;


        }

    }
    public void dispose(){
        world.dispose();
    }
}
