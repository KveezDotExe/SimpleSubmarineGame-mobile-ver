package com.kakstd.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.Scenes.Controller;
import com.kakstd.game.Sprites.Enemies;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.Sprites.SpawnPoints;
import com.kakstd.game.Sprites.Submarine;
import com.kakstd.game.Sprites.Torpedo_enemy;
import com.kakstd.game.Sprites.Torpedo_player;
import com.kakstd.game.SubmarineGame;
import com.kakstd.game.Tools.B2WorldCreator;
import com.kakstd.game.Tools.WorldContactListener;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;


public class PlayScreen implements Screen {
    private Torpedo_player torpedoPlayer;
    private Torpedo_enemy torpedoEnemy;
    private TextureAtlas atlas;
    private SubmarineGame game;
    private OrthographicCamera cam;
    private Viewport gayport;
    private Texture texture;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader loader;
    private World world;
    private Box2DDebugRenderer b2dr;
    public Player player;
    public Enemies enemy;
    Controller controller;
    private LinkedList<Torpedo_player> torpedoPlayerList = new LinkedList<Torpedo_player>();
    private LinkedList<Torpedo_enemy> enemyTorpedoList = new LinkedList<Torpedo_enemy>();
    private LinkedList<Enemies> enemyList = new LinkedList<>();
    private LinkedList<SpawnPoints> spawns;
    private float fEnd = 0.125f;
    float PfStart = 0;
    float SpawnEnd = 5f;
    float SpawnStart = 0;
    private ShapeRenderer sr = new ShapeRenderer();
    public ParticleEffect torpedo_explosion;




    public PlayScreen(SubmarineGame game){
        atlas = new TextureAtlas("Submarines.atlas");
        torpedo_explosion = new ParticleEffect();
        torpedo_explosion.load(Gdx.files.internal("Effects/boom.p"),Gdx.files.internal("Img"));
        torpedo_explosion.scaleEffect(0.5f/SubmarineGame.PPM);
        torpedo_explosion.setPosition(15000, 15000);
        this.game = game;
        // cam
        cam = new OrthographicCamera();
        gayport = new StretchViewport(game.V_WIDTH/SubmarineGame.PPM, game.V_HEIGHT/SubmarineGame.PPM,cam);


        // map
        texture = new Texture("Maps/bricks.png");
        loader = new TmxMapLoader();
        map = loader.load("Maps/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/SubmarineGame.PPM);


        // start cam pos
        cam.position.set(gayport.getWorldWidth()/2,gayport.getWorldHeight()/2,0);


        // define the world objects
        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);
        spawns = new LinkedList<>(B2WorldCreator.spawnPoints);
        world.setContactListener(new WorldContactListener());

        //define a player
        player = new Player(world, this, 96,96, "Player");

        //def controller
        controller = new Controller(SubmarineGame.batch);
        
    }
    public void SpawnEnemies(float dt){
        SpawnStart += dt;
        if(enemyList.size() <= 30 && (SpawnStart - SpawnEnd) >= 0){
            Vector2 pos = spawns.get(MathUtils.random(spawns.size())).getBody().getPosition();
            enemyList.add(new Enemies(world, this, pos, "enemy_lvl_1", player.getBody()));
            SpawnStart = 0;
        }
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    public void handleInput(float dt){
        if(Controller.touchpad.isTouched()) {
            if ((Controller.touchpad.getKnobPercentX() > 0) && player.b2d.getLinearVelocity().x <= 1.5f) {
                player.b2d.applyLinearImpulse(new Vector2(0.1f, 0), player.b2d.getWorldCenter(), true);
            }
            if ((Controller.touchpad.getKnobPercentX() < 0) && -player.b2d.getLinearVelocity().x <= 1.5f) {
                player.b2d.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2d.getWorldCenter(), true);
            }
            if ((Controller.touchpad.getKnobPercentY() > 0) && player.b2d.getLinearVelocity().y <= 1.5f) {
                player.b2d.applyLinearImpulse(new Vector2(0, 0.1f), player.b2d.getWorldCenter(), true);
            }
            if ((Controller.touchpad.getKnobPercentY() < 0) && -player.b2d.getLinearVelocity().y <= 1.5f) {
                player.b2d.applyLinearImpulse(new Vector2(0, -0.1f), player.b2d.getWorldCenter(), true);
            }
        }


    }
    public void ShootMech(float del){
        PfStart += del;
        if(Controller.gTouchpad.isTouched() && (PfStart - fEnd)>=0 && (Controller.gTouchpad.getKnobPercentX() != 0 && Controller.gTouchpad.getKnobPercentY() != 0)) {
            torpedoPlayerList.add(torpedoPlayer = new Torpedo_player(world, this, player, Controller.gTouchpad.getKnobPercentX(), Controller.gTouchpad.getKnobPercentY()));
            PfStart = 0;
        }

    }
    public void EnemyListIterator(float dt){
        ListIterator<Enemies> iterator = enemyList.listIterator();
        while (iterator.hasNext()){
            Enemies enemies = iterator.next();
            enemies.draw(game.batch);
        }
    }
    public void EnemyListIteratorlogic(float dt){
        ListIterator<Enemies> iterator = enemyList.listIterator();
        while (iterator.hasNext()){
            Enemies enemies = iterator.next();

            enemies.update(dt, player.b2d.getPosition().x, player.b2d.getPosition().y);
            enemies.p1.set(enemies.b2d.getPosition().x, enemies.b2d.getPosition().y);
            enemies.p2.set(player.b2d.getPosition().x, player.b2d.getPosition().y);
            enemies.AI(enemyTorpedoList, this, enemies, torpedoEnemy, dt);
        }
    }
    public void EnemyListIteratorRender(){
        ListIterator<Enemies> iterator = enemyList.listIterator();
        while (iterator.hasNext()){
            Enemies enemies = iterator.next();
            sr.line(enemies.p1, enemies.p2);
            sr.line(enemies.collision, enemies.normal);

        }
    }
    public void PlayertorpedoListIterator(){
        ListIterator<Torpedo_player> iterator = torpedoPlayerList.listIterator();
        while (iterator.hasNext()){
            Torpedo_player torpedoPlayerL = iterator.next();
            torpedoPlayerL.draw(game.batch);
            if(torpedoPlayerL.Explose || !(torpedoPlayerL.b2d.isAwake())){
                iterator.remove();
                torpedoPlayerL.world.destroyBody(torpedoPlayerL.b2d);
                torpedo_explosion.setPosition(torpedoPlayerL.b2d.getPosition().x, torpedoPlayerL.b2d.getPosition().y);
                torpedo_explosion.setDuration(1);
                torpedo_explosion.start();
            }
        }
    }
    public void EnemytorpedoListIterator(){
        ListIterator<Torpedo_enemy> enemyIterator = enemyTorpedoList.listIterator();
        while (enemyIterator.hasNext()){
            Torpedo_enemy torpedoEnemyL = enemyIterator.next();
            torpedoEnemyL.draw(game.batch);
            if(torpedoEnemyL.Explose || !(torpedoEnemyL.b2d.isAwake())){
                enemyIterator.remove();
                torpedoEnemyL.world.destroyBody(torpedoEnemyL.b2d);
                torpedo_explosion.setPosition(torpedoEnemyL.b2d.getPosition().x, torpedoEnemyL.b2d.getPosition().y);
                torpedo_explosion.setDuration(1);
                torpedo_explosion.start();
            }
        }
    }

    public void update(float dt){
        handleInput(dt);
        ShootMech(dt);
        world.step(1/60f, 6,2);
        player.update(dt);
        SpawnEnemies(dt);
        EnemyListIteratorlogic(dt);
        cam.position.x = player.b2d.getPosition().x;
        cam.position.y = player.b2d.getPosition().y;
        cam.update();
        renderer.setView(cam);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // handle render loop
        update(delta);
        // clearing game screen (black color)
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // map render
        renderer.render();

        // player render
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.draw(game.batch);
        EnemyListIterator(delta);
        torpedo_explosion.draw(game.batch, delta);
        PlayertorpedoListIterator();
        EnemytorpedoListIterator();
        game.batch.end();
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        EnemyListIteratorRender();
        sr.end();
        //render debug lines
        b2dr.render(world,cam.combined);
        //draw touchpad
        SubmarineGame.batch.setProjectionMatrix(controller.stage.getCamera().combined);
        controller.stage.draw();




    }

    @Override
    public void resize(int width, int height) {
        gayport.update(width, height);
        Controller.viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        map.dispose();
        renderer.dispose();
        texture.dispose();
        game.dispose();
        controller.dispose();
        enemy.dispose();
        b2dr.dispose();
        torpedo_explosion.dispose();
        atlas.dispose();

    }
}
