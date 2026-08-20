package com.kakstd.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.SubmarineGame;


public class Controller {
    private Skin skin;
    public  Touchpad touchpad;
    public  Touchpad gTouchpad;
    public ImageButton button;

    public Controller(SpriteBatch batch){
        skin = new Skin(Gdx.files.internal("Buttons/njj.json"));
        touchpad = new Touchpad(20, skin);
        touchpad.setPosition(50,50);
        gTouchpad = new Touchpad(20, skin);
        gTouchpad.setPosition(750,50, Align.bottomRight);
        skin = new Skin(Gdx.files.internal("Buttons/redButton.json"));

        button = new ImageButton( new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("Buttons/r1.png")))),
                new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("Buttons/r2.png")))),
                new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("Buttons/r2.png")))));
        button.setPosition(740, 420);
    }
    public void dispose(){
        skin.dispose();
    }
}
