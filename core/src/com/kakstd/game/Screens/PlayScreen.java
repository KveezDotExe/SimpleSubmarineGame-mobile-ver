package com.kakstd.game.Screens;


import static com.badlogic.gdx.math.MathUtils.sin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.Scenes.Controller;
import com.kakstd.game.Sprites.Enemy;
import com.kakstd.game.Sprites.Ground;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.Sprites.Ruby;
import com.kakstd.game.Sprites.RubyOre;
import com.kakstd.game.Sprites.Scrap;
import com.kakstd.game.Sprites.SpawnPoints;
import com.kakstd.game.Sprites.Torpedo_enemy;
import com.kakstd.game.Sprites.Torpedo_player;
import com.kakstd.game.SubmarineGame;
import com.kakstd.game.Tools.AutoPlay;
import com.kakstd.game.Tools.B2WorldCreator;
import com.kakstd.game.Tools.Chunks;
import com.kakstd.game.Tools.Collectables;
import com.kakstd.game.Tools.Lightning;
import com.kakstd.game.Tools.Node;
import com.kakstd.game.Tools.WorldContactListener;

import net.java.games.input.Component;

import java.util.LinkedList;
import java.util.List;
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
    private int[] backgroundWavesLayer = {1};
    private int[] foregroundLayer = {3};
    private int[] grass = {4};

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

    public FPSLogger logger = new FPSLogger();
    private Rectangle scissors, clipBounds, dontSpawn, Spawn;

    private Sprite vignette;
    private Lightning lightning;
    public int deep;

    private Body counterDeepStart;
    private ShaderProgram WaterShader;
    private float shaderTimer = 0;
    Texture noiseTex, darkblueTex, waterTex, skyBox;

    private FrameBuffer frameBuffer;
    private Image image;
    private TextureRegion region;
    private Vector2 firePosVec2;
    boolean dd =false;
    private AutoPlay autoPlay;
    private ImageButton aiButton;





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
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.textureMinFilter = Texture.TextureFilter.Nearest;
        parameters.textureMagFilter = Texture.TextureFilter.Nearest;
        map = loader.load("Maps/mapTest.tmx", parameters);
        renderer = new OrthogonalTiledMapRenderer(map, 1/SubmarineGame.PPM);





        // shader
        ShaderProgram.pedantic = false;
        noiseTex = new Texture(Gdx.files.internal("Shaders/pnoise.jpg"));
        darkblueTex = new Texture(Gdx.files.internal("Shaders/darkblue.jpg"));
        waterTex = new Texture(Gdx.files.internal("Shaders/water2.png"));
        skyBox = new Texture(Gdx.files.internal("Maps/sky.png"));

        WaterShader = new ShaderProgram(Gdx.files.internal("Shaders/WaterShader.vsh"), Gdx.files.internal("Shaders/WaterShader.fsh"));


        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        noiseTex.bind();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE2);
        darkblueTex.bind();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);




        // define the world objects
        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        counterDef();
        world.setContactListener(new WorldContactListener());

        //define a player
        player = new Player(world, this, B2WorldCreator.playerSpawns.get(0).getBody().getPosition(), player_lvl, data);
        players.add(player);

        // start cam pos
        cam.position.x = player.b2d.getPosition().x;
        cam.position.y = player.b2d.getPosition().y;


        //def controller
        controller = new Controller(SubmarineGame.batch);

        autoPlay = new AutoPlay(false, player);

        


        //HUD
        createFrameBuffer();
        hud = new Hud(game.batch, player.getHealth(), collected_gold, deep);
        region = new TextureRegion(frameBuffer.getColorBufferTexture());
        region.flip(false, true);
        image = new Image(region);
        image.setBounds(10,300,200, 200);
        hud.table.addActor(image);

        //UI
        deadWindow = new DeadWindow(game);
        firePosVec2 = new Vector2();



        //Stage

        stage = new Stage(UIport, game.batch);
        stage.addActor(controller.touchpad);
        stage.addActor(controller.gTouchpad);
        stage.addActor(controller.button);
        stage.addActor(deadWindow.window);



        deadWindow.window.setVisible(false);

        Gdx.input.setInputProcessor(stage);


        //masking

        scissors = new Rectangle();
        clipBounds = new Rectangle(player.b2d.getPosition().x, player.b2d.getPosition().y, 1920/SubmarineGame.PPM, 1600/SubmarineGame.PPM);
        Spawn = new Rectangle(player.b2d.getPosition().x, player.b2d.getPosition().y, 1600/SubmarineGame.PPM, 1600/SubmarineGame.PPM);
        dontSpawn = new Rectangle(player.b2d.getPosition().x, player.b2d.getPosition().y, 1240/SubmarineGame.PPM, 720/SubmarineGame.PPM);
        ScissorStack.calculateScissors(cam, game.batch.getTransformMatrix(), clipBounds, scissors);

        //graphics

        vignette = new Sprite(new Texture("Hud/vignette.png"));
        vignette.setBounds(0,0, 800,600);
        lightning = new Lightning(map, world, player);






    }

    public void buttonProcessor(){
        if(controller.button.isChecked()){
            autoPlay.switchActive(true);
        }
        else{
            autoPlay.switchActive(false);

        }
    }
    public Vector2 getFireVector(){
        firePosVec2.set(controller.gTouchpad.getKnobX() , controller.gTouchpad.getKnobY());
        return firePosVec2.nor().scl(0);
    }

    public void createFrameBuffer(){
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, false);
    }
    public void FrameBuffering(float delta){
        frameBuffer.begin();

        renderer.getBatch().begin();
        renderer.getBatch().draw(noiseTex, 0,0, 19200/SubmarineGame.PPM, 18720/SubmarineGame.PPM);
        renderer.getBatch().draw(darkblueTex, 0,0, 19200/SubmarineGame.PPM, 18720/SubmarineGame.PPM);
        renderer.getBatch().end();
        // clearing game screen (black color)
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderTimer += Gdx.graphics.getDeltaTime();
        renderer.getBatch().begin();
        renderer.getBatch().draw(waterTex, 0,0, 19200/SubmarineGame.PPM, 18870/SubmarineGame.PPM);
        renderer.getBatch().end();
        renderer.render(backgroundWavesLayer);;
        renderer.render(foregroundLayer);
        // player render
        game.batch.setProjectionMatrix(cam.combined);
        renderer.render(grass);
        lightning.render(delta);
        //render debug lines
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.end();
        game.batch.flush();
        sr.flush();
        frameBuffer.end();
    }

    public int calculateDeepPlayer(){
        // 0 589
        Vector2 vector = new Vector2(0, counterDeepStart.getPosition().y);

        deep = (int) (vector.dst(0,player.b2d.getPosition().y) * 10);
        return deep;
    }
    public int calculateDeepSpawnEnemies(SpawnPoints spawnPoints){
        // 0 589
        Vector2 vector = new Vector2(0, counterDeepStart.getPosition().y);

        deep = (int) (vector.dst(0,spawnPoints.getBody().getPosition().y) * 10);
        return deep;
    }

    public void counterDef(){
        BodyDef bdef;
        for (MapObject object: map.getLayers().get(20).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth() / 2) / SubmarineGame.PPM, (rectangle.y + rectangle.getHeight() / 2) / SubmarineGame.PPM);
            counterDeepStart = world.createBody(bdef);
        }
    }

    public void SpawnEnemies(float dt){
        SpawnStart += dt;
        ListIterator<SpawnPoints> iterator = B2WorldCreator.spawnPoints.listIterator();
        if(enemyList.size() <= 30 && (SpawnStart - SpawnEnd) >= 0){
            while (iterator.hasNext()){
                SpawnPoints spawnPoints = iterator.next();
                if(Spawn.contains(spawnPoints.getBody().getPosition()) && !dontSpawn.contains(spawnPoints.getBody().getPosition()) && MathUtils.randomBoolean() == true && calculateDeepSpawnEnemies(spawnPoints) <= 200){
                    enemyList.add(new Enemy(world, this, spawnPoints.getBody().getPosition(), 1, player.getBody()));
                    SpawnStart = 0;
                }
            }


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
                autoPlay.collectablesPool.remove(collectables1);
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
    public void AutoShooting(){

        if(autoPlay.isActive()  && autoPlay.enemyPool.size() > 0 && (fStart - 0.1f) >= 0){
            Vector2 vector2 = new Vector2(autoPlay.enemyPool.getFirst().b2d.getPosition()).sub(player.b2d.getPosition());
            torpedoPlayerList.add(torpedoPlayer = new Torpedo_player(true,world, this, player, vector2, torpedo_lvl));
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

            autoPlay.enemyPool.add(enemies);
            if(!enemies.enemy_isAlive){
                int b = MathUtils.random(1, 4);
                for (int i = 1; i<=b; i++) {
                    collectables.add(new Scrap(world, this, enemies.getBody().getPosition(), "Scrap"));
                    collectables.add(new Ruby(world, this, enemies.getBody().getPosition(), "Ruby"));

                }
                iterator.remove();
                enemies.world.destroyBody(enemies.b2d);
            }
            if(!Spawn.contains(enemies.b2d.getPosition())){
                iterator.remove();
                enemies.world.destroyBody(enemies.b2d);
            }

        }




    }
    public void AimIterator(){
        ListIterator<Enemy> poolIterator = autoPlay.enemyPool.listIterator();
        int i = 0;
        while (poolIterator.hasNext()){
            Enemy poolEnemy = poolIterator.next();
            if(!poolEnemy.enemy_isAlive){
                poolIterator.remove();
            }
            if(!Spawn.contains(poolEnemy.b2d.getPosition())){
                poolIterator.remove();
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
    public void PlayertorpedoListIterator(float dt){
        ListIterator<Torpedo_player> iterator = torpedoPlayerList.listIterator();

        while (iterator.hasNext()){
            Torpedo_player torpedoPlayerL = iterator.next();
            torpedoPlayerL.update(dt);
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
    public void EnemytorpedoListIterator(float dt){
        ListIterator<Torpedo_enemy> enemyIterator = enemyTorpedoList.listIterator();
        while (enemyIterator.hasNext()){
            Torpedo_enemy torpedoEnemyL = enemyIterator.next();
            torpedoEnemyL.update(dt);
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
        B2WorldCreator.walls.clear();
        B2WorldCreator.chunks.clear();
        B2WorldCreator.spawnPoints.clear();
        B2WorldCreator.playerSpawns.clear();
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
            buttonProcessor();
            calculateDeepPlayer();

        }
        ShootMech(dt);
        world.step(1/60f, 6,2);
        player.update(dt);
        if (deep >= player.getMaxDeep()){
            player.isAlive = false;
        }
        hud.update(dt, player.getHealth(), collected_gold, deep);
        if(player.isAlive) {
            SpawnEnemies(dt);
        }
        EnemyListIteratorlogic(dt);
        if(player.isAlive) {

            cam.position.x = player.b2d.getPosition().x;
            cam.position.y = player.b2d.getPosition().y;
            cam.position.x = MathUtils.clamp(cam.position.x, 432/SubmarineGame.PPM, 18736/SubmarineGame.PPM );
            cam.position.y = MathUtils.clamp(cam.position.y, 432/SubmarineGame.PPM, 18736/SubmarineGame.PPM);
            clipBounds.setPosition(player.b2d.getPosition().x - clipBounds.getWidth()/2, player.b2d.getPosition().y - clipBounds.getHeight()/2);
            dontSpawn.setPosition(player.b2d.getPosition().x - clipBounds.getWidth()/2, player.b2d.getPosition().y - clipBounds.getHeight()/2);
            Spawn.setPosition(player.b2d.getPosition().x - clipBounds.getWidth()/2, player.b2d.getPosition().y - clipBounds.getHeight()/2);

        }
        ScissorStack.calculateScissors(cam, game.batch.getTransformMatrix(), clipBounds, scissors);
        cam.update();
        renderer.setView(cam);

    }
    public void debugWalls(){
        ListIterator<Ground> iterator = B2WorldCreator.walls.listIterator();
        while (iterator.hasNext()){
            Ground ground = iterator.next();
            sr.rect(ground.wall.x, ground.wall.y, ground.wall.width, ground.wall.height);

        }
    }

    public void visualizationPath(){
        if (autoPlay.path != null) {
            ListIterator<Vector2> iterator = autoPlay.path.listIterator();
            while (iterator.hasNext()) {
                Vector2 node = iterator.next();
                sr.setColor(Color.FIREBRICK);
                sr.rect(node.x - 16f/SubmarineGame.PPM, node.y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);

            }
        }

        sr.circle(10, 140, 128f/SubmarineGame.PPM);
    }
    public void visualizationPath2(){
        if (autoPlay.path != null) {
            ListIterator<Node> iterator = autoPlay.waitingNodes.listIterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (node.c == 1) {
                    sr.setColor(Color.OLIVE);
                }else {
                    sr.setColor(Color.GOLD);
                }
                sr.rect(node.nodePos.x - 16f/SubmarineGame.PPM, node.nodePos.y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);


            }

        }

        sr.rect(player.b2d.getPosition().x - 16f/SubmarineGame.PPM,player.b2d.getPosition().y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);
        sr.circle(10, 140, 128f/SubmarineGame.PPM);
    }
    public void visualizationPath3(){
        if (autoPlay.path != null) {
            ListIterator<Node> iterator = autoPlay.checkedNodes.listIterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (node.c == 1) {
                    sr.setColor(Color.FOREST);
                }else {
                    sr.setColor(Color.ORANGE);
                }
                sr.rect(node.nodePos.x - 16f/SubmarineGame.PPM, node.nodePos.y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);


            }
        }
        sr.rect(player.b2d.getPosition().x - 16f/SubmarineGame.PPM,player.b2d.getPosition().y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);
        sr.circle(10, 140, 128f/SubmarineGame.PPM);
    }
    public void debugChunks(){
        ListIterator<Chunks> iterator = B2WorldCreator.chunks.listIterator();

        sr.setColor(Color.LIME);
        while (iterator.hasNext()){
            Chunks chunks = iterator.next();
            if (chunks.chunk.contains(player.b2d.getPosition())) {
                sr.rect(chunks.chunk.x, chunks.chunk.y, chunks.chunk.width, chunks.chunk.height);
                ListIterator<Rectangle> iterator1 = chunks.containWalls.listIterator();
                sr.setColor(Color.GOLD);
                while (iterator1.hasNext()){
                    Rectangle rectangle = iterator1.next();
                    sr.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            }
        }

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
        lightning.update(delta);

        shaderTimer += Gdx.graphics.getDeltaTime();
        FrameBuffering(delta);



        // clearing game screen (black color)
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // water shader preparing
        game.batch.begin();
        game.batch.draw(noiseTex, 0,0, 19200/SubmarineGame.PPM, 18720/SubmarineGame.PPM);
        game.batch.draw(darkblueTex, 0,0, 19200/SubmarineGame.PPM, 18720/SubmarineGame.PPM);
        game.batch.end();
        logger.log();

        if (ScissorStack.pushScissors(scissors)){



            WaterShader.bind();
            WaterShader.setUniformi("noiseTex", 1);
            WaterShader.setUniformi("darkblueTex", 2);
            WaterShader.setUniformf("time", shaderTimer);

            game.batch.begin();
            game.batch.draw(skyBox, 0,18848/SubmarineGame.PPM, 19200/SubmarineGame.PPM, 352/SubmarineGame.PPM);
            game.batch.end();
            renderer.render(backgroundWavesLayer);
            game.batch.setShader(WaterShader);
            game.batch.begin();
            game.batch.draw(waterTex, 0,0, 19200/SubmarineGame.PPM, 18870/SubmarineGame.PPM);
            game.batch.end();
            game.batch.setShader(null);
            renderer.render(foregroundLayer);


            // player render
            game.batch.setProjectionMatrix(cam.combined);
            lightning.rayHandler.setCombinedMatrix(cam);
            game.batch.begin();
            collectableLogic();
            PlayerArrayIterator();
            EnemyListIterator(delta);
            oreLogic();
            torpedo_explosion.draw(game.batch, delta);
            PlayertorpedoListIterator(delta);
            EnemytorpedoListIterator(delta);
            autoPlay.collectablesPool = collectables;
            autoPlay.AI(delta);

            AimIterator();

            AutoShooting();
            game.batch.end();

            renderer.render(grass);

            lightning.render(delta);


            //render debug lines
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            EnemyListIteratorRender();

            if(controller.gTouchpad.isTouched()){
                Vector2 aim = new Vector2(player.b2d.getPosition());
                Vector2 toaim = new Vector2(controller.gTouchpad.getKnobPercentX(), controller.gTouchpad.getKnobPercentY()).nor().scl(2);
                toaim.add(aim);
                sr.setColor(Color.RED);
                sr.line(aim,toaim);
            }
            Vector2 st = new Vector2(player.b2d.getPosition());
            Vector2 fn = new Vector2(player.b2d.getLinearVelocity());
            fn.add(st);
            sr.setColor(Color.FOREST);
            sr.line(st, fn);
            visualizationPath2();
            //visualizationPath3();
            //visualizationPath();

            sr.setColor(Color.GOLD);
            //debugWalls();
            //debugChunks();

            sr.setColor(Color.WHITE);

            sr.end();

            game.batch.flush();
            sr.flush();
            ScissorStack.popScissors();
        }

        //b2dr.render(world,cam.combined);



        //hud rend
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //draw touchpad

        SubmarineGame.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.act(delta);
        stage.draw();

        /*game.batch.begin();
        vignette.draw(game.batch);
        game.batch.end();
*/
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
        frameBuffer.dispose();
        waterTex.dispose();
        darkblueTex.dispose();
        noiseTex.dispose();
        WaterShader.dispose();
        map.dispose();
        renderer.dispose();
        texture.dispose();
        game.dispose();
        controller.dispose();
        b2dr.dispose();
        torpedo_explosion.dispose();
        atlas.dispose();
        stage.dispose();
        lightning.dispose();

    }
}
