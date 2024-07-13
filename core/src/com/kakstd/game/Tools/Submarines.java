package com.kakstd.game.Tools;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.kakstd.game.Screens.Data;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Screens.Workshop;
import com.kakstd.game.Sprites.Enemy;
import com.kakstd.game.Sprites.Ground;
import com.kakstd.game.Sprites.Torpedo_enemy;
import com.kakstd.game.SubmarineGame;

import java.util.LinkedList;


public abstract class Submarines  extends Sprite {
    // PLAYER VAR
    public enum State {STANDING, SWIMMING}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2d;
    private TextureRegion submarineStand;
    private Vector2 player_pos;
    private Animation submarineSwim;
    private float stateTimer;
    private boolean swimmingRight;
    protected int type;
    public int Health;
    protected Fixture fixture;
    public boolean isAlive = true;
    public float playerSpeed;
    public float playerMaxSpeed;
    public float lastposX, lastposY;
    public  float fire_end;


    // ENEMY VAR
    public enum enemy_State {STANDING, SWIMMING}
    public boolean enemy_isAlive = true;
    public enemy_State enemy_currentState;
    public enemy_State enemy_previousState;
    private TextureRegion enemy_submarineStand;
    private Animation enemy_submarineSwim;
    private float enemy_stateTimer;
    private boolean enemy_swimmingRight;
    Vector2 pos;
    protected int enemy_type;
    public static FixtureDef fire_area_fixture;
    public static CircleShape fire_area_shape;
    protected  float playerXpos, playerYpos;
    protected Circle ar;
    protected float enemy_fStart = 0;
    protected  float enemy_fEnd;
    protected float targetX, targetY;
    public Vector2 p1 = new Vector2(), p2 = new Vector2(), collision = new Vector2(), normal = new Vector2();
    protected boolean enemy_decideShoot = false;
    Body playerBody;
    protected EnemiesSteeringBehaviour enemy, target;
    public Wander<Vector2> wander;
    Arrive<Vector2> arrive;
    protected int enemy_lvl = 1;
    PlayScreen screen;


    // PLAYER CONSTRUCTOR
    public  Submarines(World world, PlayScreen screen, Vector2 player_pos, int type, Data data){
        super(screen.getAtlas().findRegion("WoodSubmarine"));

        this.player_pos = player_pos;
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
            case(1):
                for (int i = 78; i < 156; i+=39) {
                    frames.add(new TextureRegion(getTexture(), i, 0, 39, 25));

                }
                //def stats
                Health = (int)(500 * data.upgrade_massive[0][0]);
                playerSpeed = 0.1f * data.upgrade_massive[0][1];
                playerMaxSpeed = 1.5f + data.upgrade_massive[0][1]/2;
                fire_end = 0.7f - data.upgrade_massive[0][2];
                //def sensors
                FixtureDef fdef = new FixtureDef();
                CircleShape player_hitBox = new CircleShape();
                player_hitBox.setRadius(15/ SubmarineGame.PPM);
                fdef.shape = player_hitBox;
                fdef.isSensor = true;
                fdef.filter.categoryBits = SubmarineGame.PLAYER_BIT;
                fdef.filter.maskBits =SubmarineGame.COLLECTABLE_BIT | SubmarineGame.ENEMY_BULLET_BIT;
                fixture = b2d.createFixture(fdef);
                //def idle pos
                submarineStand = new TextureRegion(getTexture(),78,0, 39,25);
                break;
            case(2):
                for (int i = 0; i < 78; i+=39) {
                    frames.add(new TextureRegion(getTexture(), i, 0, 39, 25));

                }
                //def stats
                Health = (int)(750 * data.upgrade_massive[1][0]);
                playerSpeed = 0.2f * data.upgrade_massive[1][1];
                playerMaxSpeed = 2f + data.upgrade_massive[1][1]/2;
                //def sensors
                fdef = new FixtureDef();
                player_hitBox = new CircleShape();
                player_hitBox.setRadius(15/ SubmarineGame.PPM);
                fdef.shape = player_hitBox;
                fdef.isSensor = true;
                fdef.filter.categoryBits = SubmarineGame.PLAYER_BIT;
                fdef.filter.maskBits =SubmarineGame.COLLECTABLE_BIT | SubmarineGame.ENEMY_BULLET_BIT;
                fixture = b2d.createFixture(fdef);
                //def idle pos
                submarineStand = new TextureRegion(getTexture(),0,0, 39,25);
                break;
        }

        submarineSwim = new Animation(0.1f,frames);
        frames.clear();
        setBounds(0,0, 39/SubmarineGame.PPM, 25/SubmarineGame.PPM);
        setRegion(submarineStand);

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
        bdef.position.set(player_pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2d = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = SubmarineGame.PLAYER_BIT;
        fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.ENEMY_BIT | SubmarineGame.GROUND_BIT | SubmarineGame.ENEMY_BULLET_BIT;
        CircleShape shape = new CircleShape();
        shape.setRadius(15/SubmarineGame.PPM);
        fdef.shape = shape;
        b2d.createFixture(fdef);
    }
    public Body getBody(){
        return b2d;
    }



    // ENEMY CONSTRUCTOR
    public Submarines (World world, PlayScreen screen, Vector2 pos, int enemy_type, Body playerBody){
        super(screen.getAtlas().findRegion("Enemy"));

        this.world = world;
        this.pos = pos;
        this.enemy_type = enemy_type;
        this.screen = screen;
        this.playerBody = playerBody;
        enemy_defineSubmarine();
        //Animation
        enemy_currentState = enemy_State.STANDING;
        enemy_previousState = enemy_State.STANDING;
        enemy_stateTimer = 0;
        enemy_swimmingRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        switch (enemy_type) {
            case (1):
                enemy_lvl = 1;
                for (int i = 0; i < 78; i += 39) {
                    frames.add(new TextureRegion(getTexture(), i, 0, 39, 25));

                }
                //def stats
                enemy_fEnd = 0.7f;
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
                enemy_submarineStand = new TextureRegion(getTexture(), 0, 0, 39, 25);
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
        enemy_submarineSwim = new Animation(0.1f,frames);
        frames.clear();
        setBounds(0,0, 39/SubmarineGame.PPM, 25/SubmarineGame.PPM);
        setRegion(enemy_submarineStand);
        //detecting
        ar = new Circle();
        ar.setRadius(500f/SubmarineGame.PPM);
    }
    public TextureRegion enemy_getFrame(float dt){
        enemy_currentState = enemy_getState();
        TextureRegion region;
        switch (enemy_currentState){
            case SWIMMING:
                region = (TextureRegion) enemy_submarineSwim.getKeyFrame(enemy_stateTimer, true);
                break;
            case STANDING:
            default:
                region = enemy_submarineStand;
                break;
        }
        if((b2d.getLinearVelocity().x <0 || !enemy_swimmingRight) && !region.isFlipX()){
            region.flip(true, false);
            enemy_swimmingRight = false;
        } else if ((b2d.getLinearVelocity().x > 0 ||enemy_swimmingRight) && region.isFlipX()) {
            region.flip(true,false);
            enemy_swimmingRight = true;
        }
        enemy_stateTimer = enemy_currentState == enemy_previousState ? enemy_stateTimer + dt : 0;
        enemy_previousState = enemy_currentState;
        return region;
    }
    public enemy_State enemy_getState(){
        if(b2d.getLinearVelocity().x != 0){
            return enemy_State.SWIMMING;
        }
        else {
            return enemy_State.STANDING;
        }
    }
    public void enemy_defineSubmarine(){
        BodyDef bdef = new BodyDef();
        bdef.linearDamping = 1f;
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2d = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = SubmarineGame.ENEMY_BIT;
        fdef.filter.maskBits = SubmarineGame.DEFAULT_BIT | SubmarineGame.PLAYER_BIT | SubmarineGame.GROUND_BIT | SubmarineGame.BULLET_BIT | SubmarineGame.ENEMY_BIT;
        CircleShape shape = new CircleShape();
        shape.setRadius(15/SubmarineGame.PPM);
        fdef.shape = shape;
        fixture = b2d.createFixture(fdef);
    }
    public RayCastCallback callback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if(!(fixture.getUserData() == "bullet") && fixture.getUserData() != null && fixture.getUserData().getClass() == Ground.class) {
                collision.set(point);
                Submarines.this.normal.set(normal).add(point);
                enemy_decideShoot = false;
                enemy.setBehavior(wander);
                return 0;
            }else {
                enemy.setBehavior(arrive);
                enemy_decideShoot = true;
                return 1;
            }
        }
    };


    public abstract void update(float dt);
    public abstract void enemy_update(float dt, float x, float y);
    public abstract void AI(LinkedList<Torpedo_enemy> ammo, PlayScreen screen, Enemy enem, Torpedo_enemy torpedoEnemy, float dt);
    public abstract void onDamage();
    public abstract int getHealth();

}
