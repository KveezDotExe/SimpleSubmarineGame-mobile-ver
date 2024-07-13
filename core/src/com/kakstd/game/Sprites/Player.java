package com.kakstd.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.Data;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Screens.Workshop;
import com.kakstd.game.SubmarineGame;
import com.kakstd.game.Tools.Submarines;

import java.util.LinkedList;

public class Player extends Submarines {
    @Override
    public int getHealth() {
        return Health;
    }

    @Override
    public void onDamage() {
        Health = Health - 20;
    }

    @Override
    public void AI(LinkedList<Torpedo_enemy> ammo, PlayScreen screen, Enemy enem, Torpedo_enemy torpedoEnemy, float dt) {}

    @Override
    public void enemy_update(float dt, float x, float y) {}

    @Override
    public void update(float dt) {
        if(Health <= 0){
            Health = 0;
            isAlive = false;
            lastposX = b2d.getPosition().x;
            lastposY = b2d.getPosition().y;

        }
        else {
            setPosition(b2d.getPosition().x - getWidth() / 2, b2d.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
        }
    }
    public Player (World world, PlayScreen screen, Vector2 player_pos, int type, Data data){
        super(world, screen, player_pos, type, data);
        fixture.setUserData(this);
    }
}
