package com.kakstd.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Sprites.Enemy;
import com.kakstd.game.Sprites.Ground;
import com.kakstd.game.Sprites.InteractiveTileObject;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.Sprites.Ruby;
import com.kakstd.game.Sprites.RubyOre;
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
                if (object.getUserData().getClass() == Enemy.class && torpedoBox.getUserData().getClass() == Torpedo_player.class) {
                    ((Submarines) object.getUserData()).onDamage();
                    ((Torpedo) torpedoBox.getUserData()).Explosion();

                }
                if (object.getUserData().getClass() == Player.class && torpedoBox.getUserData().getClass() == Torpedo_enemy.class) {
                    ((Submarines) object.getUserData()).onDamage();
                    ((Torpedo) torpedoBox.getUserData()).Explosion();
                }
                if(torpedoBox.getUserData().getClass() == Torpedo_player.class && object.getUserData().getClass() == Torpedo_enemy.class){
                    ((Torpedo) torpedoBox.getUserData()).Explosion();
                }
                if((torpedoBox.getUserData().getClass() == Torpedo_player.class || torpedoBox.getUserData().getClass() == Torpedo_enemy.class) && object.getUserData().getClass() == RubyOre.class){
                    ((Torpedo) torpedoBox.getUserData()).Explosion();
                    ((RubyOre) object.getUserData()).collide_torpedo();
                }
                if(torpedoBox.getUserData().getClass() == Torpedo_player.class && object.getUserData().getClass() == Torpedo_enemy.class || torpedoBox.getUserData().getClass() == Torpedo_enemy.class && object.getUserData().getClass() == Torpedo_player.class){
                    ((Torpedo)torpedoBox.getUserData()).Explosion();
                    ((Torpedo) object.getUserData()).Explosion();
                }

            }
            if(Collectables.class.isAssignableFrom(fixtureA.getUserData().getClass()) || Collectables.class.isAssignableFrom(fixtureB.getUserData().getClass())){
                Fixture collectable = Collectables.class.isAssignableFrom(fixtureA.getUserData().getClass()) ? fixtureA : fixtureB;
                Fixture player = collectable == fixtureA ? fixtureB:fixtureA;
                if(player.getUserData().getClass() == Player.class) {
                    ((Collectables) collectable.getUserData()).collect();
                    Gdx.app.log("Collected ", String.valueOf(PlayScreen.collected_gold));
                    Gdx.app.log("Collected ", String.valueOf(PlayScreen.collected_scrap));
                }
            }
            if((Ground.class.isAssignableFrom(fixtureA.getUserData().getClass()) && Player.class.isAssignableFrom(fixtureB.getUserData().getClass())) || (Player.class.isAssignableFrom(fixtureA.getUserData().getClass()) && Ground.class.isAssignableFrom(fixtureB.getUserData().getClass()))){
                Fixture ground = Ground.class.isAssignableFrom(fixtureA.getUserData().getClass()) ? fixtureA : fixtureB;
                Fixture player = ground == fixtureA ? fixtureB : fixtureA;
                if(player.getUserData().getClass() == Player.class){
                    Player.calculateAI();
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
