package com.kakstd.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kakstd.game.Sprites.Ground;
import com.kakstd.game.Sprites.PlayerSpawn;
import com.kakstd.game.Sprites.RubyOre;
import com.kakstd.game.Sprites.SpawnPoints;
import com.kakstd.game.SubmarineGame;

import java.util.LinkedList;

public class B2WorldCreator {
    public static LinkedList<SpawnPoints>spawnPoints = new LinkedList<>();
    public  static LinkedList<PlayerSpawn> playerSpawns = new LinkedList<>();
    public static LinkedList<RubyOre> rubyOresSpawn = new LinkedList<>();
    public B2WorldCreator (World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // creating the world obj
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Ground(world, map, rect);
        }
        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spawnPoints.add(new SpawnPoints(world,map,rect));
        }
        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playerSpawns.add(new PlayerSpawn(world, map, rect));
        }
        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(MathUtils.randomBoolean() == true) {
                rubyOresSpawn.add(new RubyOre(world, map, rect));
            }
        }
    }
}
