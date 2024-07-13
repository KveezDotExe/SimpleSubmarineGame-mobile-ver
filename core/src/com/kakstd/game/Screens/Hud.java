package com.kakstd.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Image goldheap;
    private int gold;

    private Texture texture;
    private Sprite sprite;

    public Hud(SpriteBatch spriteBatch, int Health, int gold ){
        this.Health = Health;
        this.gold = gold;
        viewport = new FitViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        healthLabel = new Label("HEALTH: " + String.format("%d", Health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        collectedGold = new Label(String.format("%d", gold), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        table.add(healthLabel).expandX().left().padTop(15).padLeft(15);
        texture = new Texture(Gdx.files.internal("Hud/GoldCoinHeap.png"));
        goldheap = new Image(texture);
        table.add(collectedGold).padTop(15).right().padRight(15);
        table.add(goldheap).padTop(15).right().padRight(15);
        stage.addActor(table);

    }
    public void update(float dt, int Health, int gold){
        healthLabel.setText("HEALTH: " + String.format("%d", Health));
        collectedGold.setText(String.format("%d", gold));
    }
}
