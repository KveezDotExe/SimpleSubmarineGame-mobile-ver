package com.kakstd.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Sprites.Enemies;
import com.kakstd.game.Sprites.InteractiveTileObject;
import com.kakstd.game.Sprites.Submarine;
import com.kakstd.game.Sprites.Torpedo_enemy;
import com.kakstd.game.Sprites.Torpedo_player;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if ((fixtureA.getUserData().getClass() == Torpedo_player.class || fixtureA.getUserData().getClass() == Torpedo_enemy.class) || (fixtureB.getUserData().getClass() == Torpedo_player.class || fixtureB.getUserData().getClass() == Torpedo_enemy.class)) {
                Fixture torpedoBox = (fixtureA.getUserData().getClass() == Torpedo_player.class) || (fixtureA.getUserData().getClass() == Torpedo_enemy.class) ? fixtureA : fixtureB;
                Fixture object = torpedoBox == fixtureA ? fixtureB : fixtureA;
                if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass()) && Torpedo.class.isAssignableFrom(torpedoBox.getUserData().getClass())) {
                    ((InteractiveTileObject) object.getUserData()).collide_torpedo();
                    ((Torpedo) torpedoBox.getUserData()).Explosion();

                }
                if (object.getUserData() == "enemy_box" && torpedoBox.getUserData().getClass() == Torpedo_player.class) {
                    Enemies.onDamage();
                    ((Torpedo) torpedoBox.getUserData()).Explosion();

                }
                if (object.getUserData() == "player_box" && torpedoBox.getUserData().getClass() == Torpedo_enemy.class) {
                    Submarine.onGamage();
                    ((Torpedo) torpedoBox.getUserData()).Explosion();
                }
                if(torpedoBox.getUserData().getClass() == Torpedo_player.class && object.getUserData().getClass() == Torpedo_enemy.class){
                    ((Torpedo) torpedoBox.getUserData()).Explosion();
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
