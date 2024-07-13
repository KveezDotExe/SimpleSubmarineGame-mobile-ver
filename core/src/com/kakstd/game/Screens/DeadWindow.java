package com.kakstd.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.kakstd.game.SubmarineGame;

public class DeadWindow {
    public Window window;
    private Skin skin;
    private TextureAtlas atlas;
    public Button Restart, Workshop, Add;
    private Game game;
    private Image label;
    private SequenceAction labelAnim;

    public boolean pressed = false;
    public boolean save = false;
    private Stack stack;
    private Table table;
    public DeadWindow(Game game){
        this.game = game;
        atlas = new TextureAtlas("UI/DeadWindow.atlas");
        skin = new  Skin(Gdx.files.internal("UI/DeadScreen.json"), atlas);
        window = new Window("", skin, "default2");
        Workshop = new Button(skin, "workshop");
        Restart = new Button(skin, "restart");
        Add = new Button(skin, "add");
        label = new Image(skin, "DeadWindowLabel");
        stack = new Stack();


        //animations
        labelAnim = new SequenceAction();
        labelAnim.addAction(Actions.fadeIn(0.5f));
        labelAnim.addAction(Actions.fadeOut(0.5f));
        RepeatAction loop = new RepeatAction();
        loop.setCount(RepeatAction.FOREVER);
        loop.setAction(labelAnim);
        label.addAction(loop);
        //---------------------------------

        window.add(label).pad(17,85,0,85).size(label.getWidth()*2, label.getHeight()*2);
        window.row();
        window.add(Workshop).pad(52,48,0,48).size(Workshop.getWidth()*2, Workshop.getHeight()*2);
        window.add(Add).top().pad(52,-112,0,48);
        window.row();
        window.add(Restart).pad(8, 48, 16, 48).size(Restart.getWidth()*2, Restart.getHeight()*2);
        window.bottom();
        window.pack();
        window.align(Align.center);
        window.setPosition(SubmarineGame.V_WIDTH/2 - window.getWidth()/2,SubmarineGame.V_HEIGHT/2 - window.getHeight()/2);
        Gdx.app.log("WH", "W " + String.valueOf(window.getWidth()) + "H " + String.valueOf(window.getHeight()));
        //window.debug();
    }


}
