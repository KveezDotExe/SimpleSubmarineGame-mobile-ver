package com.kakstd.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.Scenes.Controller;
import com.kakstd.game.Sprites.Enemy;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.Sprites.Ruby;
import com.kakstd.game.Sprites.RubyOre;
import com.kakstd.game.Sprites.Scrap;
import com.kakstd.game.Sprites.Torpedo_enemy;
import com.kakstd.game.Sprites.Torpedo_player;
import com.kakstd.game.SubmarineGame;
import com.kakstd.game.Tools.B2WorldCreator;
import com.kakstd.game.Tools.Collectables;
import com.kakstd.game.Tools.WorldContactListener;

import java.util.LinkedList;
import java.util.ListIterator;


public class PlayScreen implements Screen {
    private boolean gameStarted = false;
    private Torpedo_player torpedoPlayer;
    private Torpedo_enemy torpedoEnemy;
    private TextureAtlas atlas;
    private SubmarineGame game;
    private OrthographicCamera cam;
    private Viewport viewport;
    private Viewport UIport;
    private Texture texture;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private int[] backgroundLayer = {0};
    private int[] foregroundLayer = {1};

    private TmxMapLoader loader;
    private World world;
    private Box2DDebugRenderer b2dr;
    public Player player;
    public Enemy enemy;
    Controller controller;
    private LinkedList<Torpedo_player> torpedoPlayerList = new LinkedList<Torpedo_player>();
    private LinkedList<Torpedo_enemy> enemyTorpedoList = new LinkedList<Torpedo_enemy>();
    private LinkedList<Enemy> enemyList = new LinkedList<>();
    private LinkedList<Collectables> collectables = new LinkedList<>();
    private Array<Player> players = new Array<>();

    float fStart = 0;
    float SpawnEnd = 5f;
    float SpawnStart = 0;
    private ShapeRenderer sr = new ShapeRenderer();
    public ParticleEffect torpedo_explosion;
    public Hud hud;
    public static int collected_gold = 0;
    public static int collected_scrap = 0;
    public DeadWindow deadWindow;
    public Stage stage;
    public static int torpedo_lvl = 1;
    public static int player_lvl = 1;
    private Data data;
    public  FileHandle file = Gdx.files.local("save.json");



    public PlayScreen(SubmarineGame game, Data data){
        this.data = data;
        load();
        atlas = new TextureAtlas("Submarines.atlas");
        torpedo_explosion = new ParticleEffect();
        torpedo_explosion.load(Gdx.files.internal("Effects/boom.p"),Gdx.files.internal("Img"));
        torpedo_explosion.scaleEffect(0.5f/SubmarineGame.PPM);
        torpedo_explosion.setPosition(15000, 15000);
        this.game = game;
        // cam
        cam = new OrthographicCamera();
        viewport = new StretchViewport(game.V_WIDTH/SubmarineGame.PPM, game.V_HEIGHT/SubmarineGame.PPM,cam);
        UIport = new StretchViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT);

        // map
        texture = new Texture("Maps/bricks.png");
        loader = new TmxMapLoader();
        map = loader.load("Maps/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/SubmarineGame.PPM);


        // start cam pos
        cam.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2,0);


        // define the world objects
        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);
        world.setContactListener(new WorldContactListener());

        //define a player
        player = new Player(world, this, B2WorldCreator.playerSpawns.get(0).getBody().getPosition(), player_lvl, data);
        players.add(player);
        //def controller
        controller = new Controller(SubmarineGame.batch);
        //HUD
        hud = new Hud(game.batch, player.getHealth(), collected_gold);

        //UI
        deadWindow = new DeadWindow(game);

        //Stage

        stage = new Stage(UIport, game.batch);
        stage.addActor(controller.touchpad);
        stage.addActor(controller.gTouchpad);
        stage.addActor(deadWindow.window);




        deadWindow.window.setVisible(false);

        Gdx.input.setInputProcessor(stage);



    }
    public void SpawnEnemies(float dt){
        SpawnStart += dt;
        if(enemyList.size() <= 30 && (SpawnStart - SpawnEnd) >= 0){
            Vector2 pos = B2WorldCreator.spawnPoints.get(MathUtils.random(B2WorldCreator.spawnPoints.size() -1)).getBody().getPosition();
            enemyList.add(new Enemy(world, this, pos, 1, player.getBody()));
            SpawnStart = 0;
        }
    }
    public void oreLogic(){
        ListIterator<RubyOre> iterator = B2WorldCreator.rubyOresSpawn.listIterator();
        while (iterator.hasNext()){
            RubyOre rubyOre = iterator.next();
            rubyOre.update();
            if(rubyOre.Health <= 0){
                rubyOre.Destroyed = true;
                iterator.remove();
                collectables.add(new Ruby(world, this, rubyOre.getBody().getPosition(), "Ruby"));
                rubyOre.getWorld().destroyBody(rubyOre.getBody());
            }
        }
    }
    public void collectableLogic(){
        ListIterator<Collectables> iterator = collectables.listIterator();
        while (iterator.hasNext()){
            Collectables collectables1 = iterator.next();
            collectables1.update();
            if(collectables1.collected){
                iterator.remove();
                collectables1.getWorld().destroyBody(collectables1.getBody());
            }
        }
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    public void handleInput(float dt){
        if(controller.touchpad.isTouched()) {
            Vector2 player_velocity = player.b2d.getLinearVelocity();
            float currSpeed = player_velocity.len2();
            if(currSpeed > player.playerMaxSpeed * player.playerMaxSpeed){
                player.b2d.setLinearVelocity(player_velocity.scl(player.playerMaxSpeed/ (float) Math.sqrt(currSpeed)));
            }
            player.b2d.applyLinearImpulse(new Vector2(controller.touchpad.getKnobPercentX(), controller.touchpad.getKnobPercentY()).nor().scl(player.playerSpeed), player.b2d.getWorldCenter(), true);

        }


    }
    public void ShootMech(float del){
        fStart += del;
        if(controller.gTouchpad.isTouched() && (fStart - player.fire_end)>=0 && (controller.gTouchpad.getKnobPercentX() != 0 && controller.gTouchpad.getKnobPercentY() != 0)) {
            torpedoPlayerList.add(torpedoPlayer = new Torpedo_player(world, this, player, controller.gTouchpad.getKnobPercentX(), controller.gTouchpad.getKnobPercentY(), torpedo_lvl));
            fStart = 0;
        }

    }
    public void PlayerArrayIterator(){
        Array.ArrayIterator<Player> iterator = players.iterator();
        while (iterator.hasNext()){
            Player player1 = iterator.next();
            player1.draw(game.batch);
            if(!player1.isAlive){
                iterator.remove();
                player1.world.destroyBody(player1.b2d);
            }
        }
    }
    public void EnemyListIterator(float dt){
        ListIterator<Enemy> iterator = enemyList.listIterator();
        while (iterator.hasNext()){
            Enemy enemies = iterator.next();
            if(!player.isAlive){
                enemies.runAI = false;
            }
            enemies.draw(game.batch);
            if(!enemies.enemy_isAlive){
                int b = MathUtils.random(1, 4);
                for (int i = 1; i<=b; i++) {
                    collectables.add(new Scrap(world, this, enemies.getBody().getPosition(), "Scrap"));
                }
                iterator.remove();
                enemies.world.destroyBody(enemies.b2d);
            }
        }
    }
    public void EnemyListIteratorlogic(float dt){
        ListIterator<Enemy> iterator = enemyList.listIterator();
        while (iterator.hasNext()){
            Enemy enemies = iterator.next();

            enemies.enemy_update(dt, player.b2d.getPosition().x, player.b2d.getPosition().y);
            enemies.p1.set(enemies.b2d.getPosition().x, enemies.b2d.getPosition().y);
            enemies.p2.set(player.b2d.getPosition().x, player.b2d.getPosition().y);
            enemies.AI(enemyTorpedoList, this, enemies, torpedoEnemy, dt);
        }
    }
    public void EnemyListIteratorRender(){
        ListIterator<Enemy> iterator = enemyList.listIterator();
        while (iterator.hasNext()){
            Enemy enemies = iterator.next();
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
    public void disposeIterator(){
        ListIterator<RubyOre> rubyOreListIterator = B2WorldCreator.rubyOresSpawn.listIterator();
        ListIterator<Collectables> collectablesListIterator = collectables.listIterator();
        while (rubyOreListIterator.hasNext()){
            RubyOre rubyOre = rubyOreListIterator.next();
            rubyOreListIterator.remove();
            rubyOre.dispose();
        }
        while (collectablesListIterator.hasNext()){
            Collectables collectables1 = collectablesListIterator.next();
            collectablesListIterator.remove();
            collectables1.dispose();
        }
    }

    public void update(float dt){
        if(player.isAlive) {
            handleInput(dt);
        }
        ShootMech(dt);
        world.step(1/60f, 6,2);
        player.update(dt);
        hud.update(dt, player.getHealth(), collected_gold);
        if(player.isAlive) {
            SpawnEnemies(dt);
        }
        EnemyListIteratorlogic(dt);
        if(player.isAlive) {
            cam.position.x = player.b2d.getPosition().x;
            cam.position.y = player.b2d.getPosition().y;
        }
        cam.update();
        renderer.setView(cam);
    }
    public void save(Data data){
        Json json = new Json();
        json.setUsePrototypes(false);
        json.setOutputType(JsonWriter.OutputType.json);
        file.writeString(Base64Coder.encodeString(json.toJson(data)), false);
    }
    public void load(){
        Json json = new Json();
        data = json.fromJson(Data.class, Base64Coder.decodeString(file.readString()));
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
        renderer.render(backgroundLayer);

        renderer.render(foregroundLayer);

        // player render
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        collectableLogic();
        PlayerArrayIterator();
        EnemyListIterator(delta);
        oreLogic();
        torpedo_explosion.draw(game.batch, delta);
        PlayertorpedoListIterator();
        EnemytorpedoListIterator();

        game.batch.end();


        //render debug lines
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        EnemyListIteratorRender();
        sr.end();
        b2dr.render(world,cam.combined);


        //hud rend
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //draw touchpad

        SubmarineGame.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.act(delta);
        stage.draw();

        if(!player.isAlive){
            controller.touchpad.setVisible(false);
            controller.gTouchpad.setVisible(false);
            deadWindow.window.setVisible(true);
            if (deadWindow.Workshop.isChecked()){
                deadWindow.save = true;
                data.Gold += collected_gold;
                data.Scrap += collected_scrap;
                collected_gold = 0;
                collected_scrap =0;
                save(data);
                Gdx.app.log("Total gold ", String.valueOf(data.Gold));
                game.setScreen(new Workshop(game, data));
                dispose();
            }
            if(deadWindow.Restart.isChecked()){
                data.Gold += collected_gold;
                data.Scrap += collected_scrap;

                collected_gold = 0;
                collected_scrap = 0;
                Gdx.app.log("Total gold ", String.valueOf(data.Gold));
                dispose();
                game.setScreen(new PlayScreen(game, data));
            }
        }



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        UIport.update(width, height);
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
        disposeIterator();
        map.dispose();
        renderer.dispose();
        texture.dispose();
        game.dispose();
        controller.dispose();
        b2dr.dispose();
        torpedo_explosion.dispose();
        atlas.dispose();
        stage.dispose();


    }
}
