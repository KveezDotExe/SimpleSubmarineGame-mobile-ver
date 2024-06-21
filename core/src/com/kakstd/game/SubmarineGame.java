package com.kakstd.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kakstd.game.Screens.PlayScreen;


public class SubmarineGame extends Game {
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short ENEMY_BIT = 4;
	public static final short GROUND_BIT = 8;
	public static final short DESTROY_BIT = 16;
	public static final short BULLET_BIT = 32;
	public static SpriteBatch batch;
	public static ParticleEffect effect;
	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}


	@Override
	public void render() {
		super.render();
	}


	@Override
	public void dispose() {




	}
}
