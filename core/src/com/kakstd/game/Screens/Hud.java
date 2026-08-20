package com.kakstd.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kakstd.game.SubmarineGame;

public class Hud {
    public Stage stage;
    public Viewport viewport;
    public int Health;
    private Label healthLabel;
    private Label collectedGold;
    private Label showDeep;
    private Image goldheap;
    private int gold;
    private int deep;
    Table table;
    private Texture texture;


    public Hud(SpriteBatch spriteBatch, int Health, int gold, int deep){
        this.Health = Health;
        this.gold = gold;
        this.deep = deep;
        viewport = new FitViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        table = new Table();
        table.top();
        table.setFillParent(true);
        healthLabel = new Label("HEALTH: " + String.format("%d", Health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        collectedGold = new Label(String.format("%d", gold), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        showDeep = new Label(String.format("%d", deep) + "m", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(healthLabel).expandX().left().padTop(30).padLeft(30);
        table.add(showDeep).center().padRight(280).padTop(30);
        texture = new Texture(Gdx.files.internal("Hud/GoldCoinHeap.png"));
        goldheap = new Image(texture);
        table.add(collectedGold).padTop(30).right().padRight(30);
        table.add(goldheap).padTop(30).right().padRight(30);
        stage.addActor(table);

    }
    public void update(float dt, int Health, int gold, int deep){
        healthLabel.setText("HEALTH: " + String.format("%d", Health));
        collectedGold.setText(String.format("%d", gold));
        showDeep.setText(String.format("%d", deep)+ "m");
    }
}
