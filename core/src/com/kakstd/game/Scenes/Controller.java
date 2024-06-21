package com.kakstd.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.SubmarineGame;


public class Controller {
    public Stage stage;
    private Skin skin;
    public static Touchpad touchpad;
    public static Touchpad gTouchpad;
    public static Viewport viewport;

    public Controller(SpriteBatch batch){
        viewport = new StretchViewport(SubmarineGame.V_WIDTH,SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("Buttons/njj.json"));
        touchpad = new Touchpad(20, skin);
        touchpad.setPosition(50,50);
        gTouchpad = new Touchpad(20, skin);
        gTouchpad.setPosition(750,50, Align.bottomRight);
        stage.addActor(touchpad);
        stage.addActor(gTouchpad);
        Gdx.input.setInputProcessor(stage);
    }
    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
}
