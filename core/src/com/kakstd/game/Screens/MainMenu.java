package com.kakstd.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    public MainMenu (Game game){
        this.game = game;
        viewport = new FitViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SubmarineGame) game).batch);
        atlas = new TextureAtlas("Fonts/Start_Button.atlas");
        skin = new Skin(atlas);
        table = new Table(skin);
        table.setBounds(0,0, SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("ButtonUp");
        textButtonStyle.down = skin.getDrawable("ButtonDown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        white = new BitmapFont(Gdx.files.internal("Fonts/White.fnt"), false);
        textButtonStyle.font = white;
        buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonPlay.pad(20);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.YELLOW);
        gameName = new Label("SUBMARINE ADVENTURE", font);
        gameName.setFontScale(3);
        table.add(gameName).expandX();
        table.row();
        table.add(buttonPlay);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(buttonPlay.isChecked()){
            game.setScreen(new PlayScreen((SubmarineGame) game));
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
