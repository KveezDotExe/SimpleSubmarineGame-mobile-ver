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
    private Skin skin;
    public  Touchpad touchpad;
    public  Touchpad gTouchpad;

    public Controller(SpriteBatch batch){
        skin = new Skin(Gdx.files.internal("Buttons/njj.json"));
        touchpad = new Touchpad(20, skin);
        touchpad.setPosition(50,50);
        gTouchpad = new Touchpad(20, skin);
        gTouchpad.setPosition(750,50, Align.bottomRight);

    }
    public void dispose(){
        skin.dispose();
    }
}
