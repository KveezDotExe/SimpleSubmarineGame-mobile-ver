package com.kakstd.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    public Hud(SpriteBatch spriteBatch, int Health){
        this.Health = Health;
        viewport = new FitViewport(SubmarineGame.V_WIDTH, SubmarineGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        healthLabel = new Label("HEALTH: " + String.format("%d", Health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(healthLabel).expandX().padTop(10).left().padLeft(100);

        stage.addActor(table);
    }
    public void update(float dt, int Health){
        healthLabel.setText("HEALTH: " + String.format("%d", Health));
    }
}
