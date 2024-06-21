package com.kakstd.game.Sprites;

import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Tools.Submarines;

import java.util.LinkedList;

public class Player extends Submarines {
    @Override
    public void AI(LinkedList<Torpedo_enemy> ammo, PlayScreen screen, Enemies enem, Torpedo_enemy torpedoEnemy, float dt) {

    }

    @Override
    public void enemy_update(float dt, float x, float y) {}

    @Override
    public void update(float dt) {
        setPosition(b2d.getPosition().x - getWidth()/2, b2d.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));
    }
    public Player (World world, PlayScreen screen, int xPos, int yPos, String type){
        super(world, screen, xPos, yPos, type);
    }
}
