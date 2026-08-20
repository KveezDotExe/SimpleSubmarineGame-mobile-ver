package com.kakstd.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.SubmarineGame;

public class Workshop implements Screen {
    private Data data;
    private Game game;
    private Stage stage;
    private Viewport viewport;
    private TextureAtlas atlas;
    private Skin skin;
    private Window window;
    private Button left, right;
    private Button torpedoButton_lvl_1, torpedoButton_lvl_2, torpedoButton_lvl_3, torpedoButton_lvl_4, torpedoButton_lvl_5, torpedoButton_lvl_6, torpedoButton_lvl_7, torpedoButton_lvl_8;
    private Button submarine1, submarine2;
    private ButtonGroup buttonGroup;
    private Label label_b, gold_count, scrap_count;
    private ScrollPane scrollPane;
    private Table table, table_buttons, table_submarine_1, table_submarine_2;
    private Image torpedo_lvl_1, torpedo_lvl_2;
    private Image submarine_lvl_1, submarine_lvl_2;
    private Image Coins, Scrap_heap;
    private Array<Table> arrayTableSubmarines = new Array<>();
    private int i = 0;

    private ProgressBar hp_bar, spd_bar, cd_bar;
    private Button hp_upgrade, spd_upgrade, cd_upgrade;

    private Label hp_upgrade_price, spd_upgrade_price, cd_upgrade_price;


    public  FileHandle file = Gdx.files.local("save.json");

    private float delay_start = 0;
    private float delay_end = 0.3f;

    public void save(Data data){
        Json json = new Json();
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);

        file.writeString(Base64Coder.encodeString(json.toJson(data)), false);
    }
    public void load(){
        Json json = new Json();
        data = json.fromJson(Data.class, Base64Coder.decodeString(file.readString()));
    }

    public Workshop(Game game, Data data){
        this.game = game;
        this.data = data;
        load();

        viewport = new StretchViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SubmarineGame) game).batch);
        atlas = new TextureAtlas("UI/Workshop.atlas");
        skin = new Skin(Gdx.files.internal("UI/WorkShop.json"), atlas);
        Coins = new Image(atlas.getRegions().get(0));
        Scrap_heap = new Image(atlas.getRegions().get(12));
        torpedo_lvl_1 = new Image( new Texture("Submarines/Torpedo_Meksikano.png"));
        torpedo_lvl_1.setRotation(90);
        torpedo_lvl_2 = new Image( new Texture("Img/TorpedoLVL4.png"));
        torpedo_lvl_2.setRotation(90);
        submarine_lvl_1 = new Image(new Texture("Submarines/WoodSubmarine.png"));
        submarine_lvl_2 = new Image(new Texture("Submarines/Enemy.png"));


        //purchased torpedo & submarines
        //desc = new DataDescriptor();




        //Window sets
        window = new Window("", skin, "default2");
        window.setFillParent(true);

        //table for submarines
        submarine1 = new Button(skin, "play");
        label_b = new Label("PLAY", skin, "red");
        label_b.setFontScale(1f);
        submarine1.add(label_b);
        submarine2 = new Button(skin, "play");

        table_submarine_1 = new Table();
        table_submarine_1.setFillParent(true);
        table_submarine_1.add(submarine_lvl_1).prefSize(200,100).pad(47,367,217,81).colspan(2);
        table_submarine_1.row();
        table_submarine_1.add(submarine1).pad(47,367,46,81);

        /*table_submarine_1.debug();
        s1_HP.debug();*/

        table_submarine_2 = new Table();
        table_submarine_2.setFillParent(true);
        table_submarine_2.add(submarine_lvl_2).prefSize(200,100).pad(47,367,217,81);;
        table_submarine_2.row();
        table_submarine_2.add(submarine2).pad(47,367,46,81);;

        arrayTableSubmarines.add(table_submarine_1);
        arrayTableSubmarines.add(table_submarine_2);

        //upgrade buttons and bar
        hp_bar = new ProgressBar(1,2, 0.1f, false, skin, "default");
        spd_bar = new ProgressBar(1,2, 0.1f, false, skin, "default");
        cd_bar = new ProgressBar(0,0.5f, 0.1f, false, skin, "default");
        hp_upgrade = new Button(skin, "upgrade");
        hp_bar.setAnimateDuration(0.1f);
        spd_bar.setAnimateDuration(0.1f);
        cd_bar.setAnimateDuration(0.1f);
        spd_upgrade = new Button(skin, "upgrade");
        cd_upgrade = new Button(skin, "upgrade");

        hp_upgrade_price = new Label("HP UPGRADE:        ", skin, "yellow");
        spd_upgrade_price = new Label("SPD UPGRADE:       ", skin, "yellow");
        cd_upgrade_price = new Label("CD UPGRADE:        ", skin, "yellow");
        hp_upgrade_price.setFontScale(0.4f);
        spd_upgrade_price.setFontScale(0.4f);
        cd_upgrade_price.setFontScale(0.4f);

        //text on buttons
        label_b = new Label("TORPEDO LVL 1", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(45,100);


        //Buttons sets
        left = new Button(skin, "left");
        right = new Button(skin, "right");


        torpedoButton_lvl_1 = new Button(skin, "swipe");
        torpedoButton_lvl_1.add(torpedo_lvl_1).left().prefSize(16,16).padRight(110).padBottom(90);
        torpedoButton_lvl_1.addActor(label_b);
        label_b = new Label("DMG: 20", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(45, 70);
        torpedoButton_lvl_1.addActor(label_b);
        label_b = new Label("SPEED: 5", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(45, 40);
        torpedoButton_lvl_1.addActor(label_b);
        Coins.setPosition(45, 10);
        torpedoButton_lvl_1.addActor(Coins);
        label_b = new Label(": FREE", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(77,0);
        torpedoButton_lvl_1.addActor(label_b);





        torpedoButton_lvl_2 = new Button(skin, "swipe");
        torpedoButton_lvl_2.add(torpedo_lvl_2).left().prefSize(64,32).padRight(35).padBottom(15);
        label_b = new Label("TORPEDO LVL 2", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(45, 100);
        torpedoButton_lvl_2.addActor(label_b);
        label_b = new Label("DMG: 100", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(45, 70);
        torpedoButton_lvl_2.addActor(label_b);
        label_b = new Label("SPEED: 2", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(45, 40);
        torpedoButton_lvl_2.addActor(label_b);
        Coins = new Image(atlas.getRegions().get(0));
        Coins.setPosition(45, 10);
        torpedoButton_lvl_2.addActor(Coins);
        label_b = new Label(": 30 000", skin, "black");
        label_b.setFontScale(0.3f);
        label_b.setPosition(77,0);
        torpedoButton_lvl_2.addActor(label_b);




        torpedoButton_lvl_3 = new Button(skin, "swipe");





        torpedoButton_lvl_4 = new Button(skin, "swipe");





        torpedoButton_lvl_5 = new Button(skin, "swipe");




        torpedoButton_lvl_6 = new Button(skin, "swipe");




        torpedoButton_lvl_7 = new Button(skin, "swipe");




        torpedoButton_lvl_8 = new Button(skin, "swipe");

        buttonGroup = new ButtonGroup<>();
        buttonGroup.add(torpedoButton_lvl_1);
        buttonGroup.add(torpedoButton_lvl_2);
        buttonGroup.add(torpedoButton_lvl_3);
        buttonGroup.add(torpedoButton_lvl_4);
        buttonGroup.add(torpedoButton_lvl_5);
        buttonGroup.add(torpedoButton_lvl_6);
        buttonGroup.add(torpedoButton_lvl_7);
        buttonGroup.add(torpedoButton_lvl_8);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setMaxCheckCount(1);




        table_buttons = new Table();
        table_buttons.add(torpedoButton_lvl_1).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_2).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_3).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_4).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_5).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_6).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_7).pad(10,0,10,0);
        table_buttons.row();
        table_buttons.add(torpedoButton_lvl_8).pad(10,0,10,0);
        table_buttons.row();

        table_buttons.pack();

        scrollPane = new ScrollPane(table_buttons);
        scrollPane.setOverscroll(false, true);

        //Window adding
        window.align(Align.right);
        window.add(scrollPane).pad(53,34,53,0);
        window.add(left).pad(88, 133,88,297);
        hp_upgrade_price.setPosition(420,360);
        spd_upgrade_price.setPosition(420,300);
        cd_upgrade_price.setPosition(420,250);
        hp_bar.setPosition(420,340);
        spd_bar.setPosition(420,280);
        cd_bar.setPosition(420,230);
        hp_upgrade.setSize(50,25);
        spd_upgrade.setSize(50,25);
        cd_upgrade.setSize(50,25);
        hp_upgrade.setPosition(600,340);
        spd_upgrade.setPosition(600,280);
        cd_upgrade.setPosition(600,230);
        window.addActor(hp_upgrade_price);
        window.addActor(spd_upgrade_price);
        window.addActor(cd_upgrade_price);
        hp_bar.setAnimateInterpolation(Interpolation.swingOut);
        spd_bar.setAnimateInterpolation(Interpolation.swingOut);
        cd_bar.setAnimateInterpolation(Interpolation.swingOut);
        window.addActor(hp_bar);
        window.addActor(spd_bar);
        window.addActor(cd_bar);
        window.addActor(hp_upgrade);
        window.addActor(spd_upgrade);
        window.addActor(cd_upgrade);
        window.addActor(table_submarine_1);
        window.addActor(table_submarine_2);
        gold_count = new Label(String.format("%d", data.Gold), skin, "yellow");
        gold_count.setFontScale(0.5f);
        gold_count.setPosition(130,550);
        window.addActor(gold_count);
        scrap_count = new Label(String.format("%d", data.Scrap), skin, "orange");
        scrap_count.setFontScale(0.5f);
        scrap_count.setPosition(580,550);
        window.addActor(scrap_count);
        window.add(right).padRight(53).padLeft(23).padTop(88).padBottom(88);



        //tables submarines
        table_submarine_2.setVisible(false);



        window.pack();
        //window.debug();
        //scrollPane.debug();
        //table_buttons.debug();


        stage.addActor(window);
        Gdx.input.setInputProcessor(stage);

    }
    private void submarineTableIterator(float dt){
        Array.ArrayIterator<Table> iterator = arrayTableSubmarines.iterator();
        int a = 0; //counters of massive
        delay_start += dt;
        while (iterator.hasNext()){
            Table tableSubmarine = iterator.next();
            if(tableSubmarine.isVisible()){
                if (hp_upgrade.isPressed() && hp_bar.getValue() < 2 && (delay_start - delay_end) >= 0 && (data.Gold - data.prices[a][0]) >= 0) {
                    data.upgrade_massive[a][0] = data.upgrade_massive[a][0] + 0.1f;
                    Gdx.app.log("pc", String.valueOf(hp_bar.getPercent()));
                    Gdx.app.log("um", String.valueOf(data.upgrade_massive[a][0]));
                    data.Gold -= data.prices[a][0];
                    data.prices[a][0] = data.prices[a][0] + 500;
                    save(data);
                    delay_start = 0;
                }
                if(spd_upgrade.isPressed() && spd_bar.getValue() < 2 && (delay_start - delay_end) >= 0 && (data.Gold - data.prices[a][1]) >= 0){
                    data.upgrade_massive[a][1] = data.upgrade_massive[a][1] + 0.1f;
                    data.Gold -= data.prices[a][1];
                    data.prices[a][1] = data.prices[a][1] + 750;
                    save(data);
                    delay_start = 0;
                }
                if(cd_upgrade.isPressed() && cd_bar.getValue() < 0.5f && (delay_start - delay_end) >= 0 && (data.Gold - data.prices[a][2]) >= 0){
                   data.upgrade_massive[a][2] = data.upgrade_massive[a][2] + 0.1f;
                   data.Gold -= data.prices[a][2];
                   data.prices[a][2] = data.prices[a][2] + 1500;
                   save(data);
                    delay_start = 0;
                }
                if (hp_bar.getPercent() <= 0.9f) {
                    hp_upgrade_price.setText("HP UPGRADE:        " + String.format("%d", data.prices[a][0]));
                } else if (hp_bar.getPercent() > 0.9f) {
                    hp_upgrade_price.setText("HP UPGRADE:        " + "FULL");
                }
                if(spd_bar.getPercent() <= 0.9f){
                    spd_upgrade_price.setText("SPD UPGRADE:       " + String.format("%d", data.prices[a][1]));
                } else if (spd_bar.getPercent() > 0.9f) {
                    spd_upgrade_price.setText("SPD UPGRADE:        " + "FULL");
                }
                if(cd_bar.getPercent() <= 0.9f) {
                    cd_upgrade_price.setText("CD UPGRADE:        " + String.format("%d", data.prices[a][2]));
                } else if (cd_bar.getPercent() > 0.9f) {
                    cd_upgrade_price.setText("CD UPGRADE:        " + "FULL");
                }
                hp_bar.setValue(data.upgrade_massive[a][0]);
                spd_bar.setValue(data.upgrade_massive[a][1]);
                cd_bar.setValue(data.upgrade_massive[a][2]);

            }
            a += 1;
        }

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        gold_count.setText(String.format("%d", data.Gold));
        if(torpedoButton_lvl_1.isChecked()){
            PlayScreen.torpedo_lvl = 1;
        }
        if(torpedoButton_lvl_2.isChecked()){
            if(data.Gold >= 30000 && torpedoButton_lvl_2.isChecked() && !data.purchased2) {
                data.Gold -= 30000;
                PlayScreen.torpedo_lvl = 2;
                data.purchased2 = true;
                save(data);
            } else if (data.purchased2) {
                PlayScreen.torpedo_lvl = 2;
            }else {
                torpedoButton_lvl_2.setChecked(false);
                //torpedoButton_lvl_1.setChecked(true);
                Gdx.app.log("butt2", "need more gold");
            }

        }
        if(torpedoButton_lvl_3.isChecked()){

        }
        if(torpedoButton_lvl_4.isChecked()){

        }
        if(torpedoButton_lvl_5.isChecked()){

        }
        if(torpedoButton_lvl_6.isChecked()){

        }
        if(torpedoButton_lvl_7.isChecked()){

        }
        if(torpedoButton_lvl_8.isChecked()){

        }
        if(submarine1.isChecked()){
            PlayScreen.player_lvl = 1;
            game.setScreen(new PlayScreen((SubmarineGame) game, data));
            dispose();
        }
        if(submarine2.isChecked()){
            PlayScreen.player_lvl = 2;
            game.setScreen(new PlayScreen((SubmarineGame) game, data));
            dispose();
        }
        if(left.isPressed() && i > 0){
            i -= 1;
            arrayTableSubmarines.get(i+1).setVisible(false);
            arrayTableSubmarines.get(i).setVisible(true);
        }
        if(right.isPressed() && i < 1){
            i += 1;
            arrayTableSubmarines.get(i-1).setVisible(false);
            arrayTableSubmarines.get(i).setVisible(true);
        }
        submarineTableIterator(delta);
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
        stage.dispose();
    }
}
