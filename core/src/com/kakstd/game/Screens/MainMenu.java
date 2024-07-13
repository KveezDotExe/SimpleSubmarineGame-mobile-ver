package com.kakstd.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.SubmarineGame;

public class MainMenu implements Screen {
    private Stage stage;
    private Table table;
    private Skin skin;
    private BitmapFont white, black;
    private TextButton buttonPlay;
    private Viewport viewport;
    private Label gameName;
    private TextureAtlas atlas;
    private Game game;
    private Data data;
    public  FileHandle file = Gdx.files.local("save.json");

    public MainMenu (Game game, Data data){
        this.game = game;
        this.data = data;
        viewport = new FitViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SubmarineGame) game).batch);
        atlas = new TextureAtlas("Fonts/Start_Button.atlas");
        skin = new Skin(Gdx.files.internal("UI/MainMenu.json"),atlas);
        table = new Table(skin);
        table.setBounds(0,0, SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT);
        buttonPlay = new TextButton("PLAY", skin);
        buttonPlay.pad(20);
        gameName = new Label("SUBMARINE ADVENTURE", skin, "yellow");
        gameName.setPosition(400,800);
        table.add(gameName).expandX().padTop(50);
        table.top();

        //gameName.addAction(Actions.moveTo(400,300));
        table.row();
        table.add(buttonPlay).padTop(100);
        stage.addActor(table);
        if(!file.exists()) {
            save(data);
        }
        load();
        Gdx.input.setInputProcessor(stage);
    }
    public void save(Data data){
        Json json = new Json();
        json.setUsePrototypes(false);
        json.setOutputType(JsonWriter.OutputType.json);
        file.writeString(Base64Coder.encodeString(json.toJson(data)), false);
    }
    public void load (){
        Json json = new Json();
        data = json.fromJson(Data.class, Base64Coder.decodeString(file.readString()));

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(buttonPlay.isChecked()){
            save(data);
            game.setScreen(new PlayScreen((SubmarineGame) game, data));
            dispose();
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
