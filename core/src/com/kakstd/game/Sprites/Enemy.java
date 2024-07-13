package com.kakstd.game.Sprites;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Tools.Submarines;

import java.util.LinkedList;

public class Enemy extends Submarines {
    public boolean runAI = true;
    @Override
    public int getHealth() {
        return Health;
    }

    @Override
    public void onDamage() {
        Health = Health - 20;
    }

    @Override
    public void AI(LinkedList<Torpedo_enemy> ammo, PlayScreen screen, Enemy enem, Torpedo_enemy torpedoEnemy, float dt) {
        if(ar.contains(targetX,targetY) && runAI) {
            enemy.update(dt);
            GdxAI.getTimepiece().update(dt);
            world.rayCast(callback, p1, p2);
            if (enemy_fStart - enemy_fEnd >= 0 && enemy_decideShoot) {
                ammo.add(torpedoEnemy = new Torpedo_enemy(world, screen, enem, playerXpos, playerYpos, enemy_lvl));
                enemy_fStart = 0;
            }
            playerXpos = 0;
            playerYpos = 0;
        }
    }

    @Override
    public void enemy_update(float dt, float x, float y) {
        if(Health <=0 ){
            enemy_isAlive = false;
        }
        setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        setRegion(enemy_getFrame(dt));
        ar.setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        targetX = x;
        targetY = y;
        if (ar.contains(x,y)) {

            playerXpos = x - b2d.getPosition().x;
            playerYpos = y - b2d.getPosition().y;
        }
        enemy_fStart += dt;
    }

    @Override
    public void update(float dt) {}
    public Enemy (World world, PlayScreen screen, Vector2 pos, int enemy_type, Body playerBody){
        super(world, screen, pos, enemy_type, playerBody);
        fixture.setUserData(this);

    }
    public Body getBody(){
        return b2d;
    }

    @Override
    public void draw(Batch batch, float dt) {

    }
}
