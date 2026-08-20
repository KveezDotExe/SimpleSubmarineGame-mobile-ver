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
import com.kakstd.game.Sprites.CamBorder;
import com.kakstd.game.Sprites.Ground;
import com.kakstd.game.Sprites.PlayerSpawn;
import com.kakstd.game.Sprites.RubyOre;
import com.kakstd.game.Sprites.SpawnPoints;
import com.kakstd.game.SubmarineGame;

import java.util.LinkedList;
import java.util.ListIterator;

public class B2WorldCreator {
    public static LinkedList<SpawnPoints>spawnPoints = new LinkedList<>();
    public  static LinkedList<PlayerSpawn> playerSpawns = new LinkedList<>();
    public static LinkedList<RubyOre> rubyOresSpawn = new LinkedList<>();
    public static LinkedList<Ground> walls = new LinkedList<>();
    public static LinkedList<Chunks> chunks = new LinkedList<>();

    public B2WorldCreator (World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // creating the world obj
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            walls.add(new Ground(world, map, rect));
        }

        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playerSpawns.add(new PlayerSpawn(world, map, rect));
        }

        for (MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spawnPoints.add(new SpawnPoints(world,map,rect));
        }
        for (MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(MathUtils.randomBoolean() == true) {
                rubyOresSpawn.add(new RubyOre(world, map, rect));
            }
        }

        //chunks

        for(MapObject object: map.getLayers().get(21).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            chunks.add(new Chunks(rectangle));
        }

        System.out.println(walls.size());
        ListIterator<Chunks> chunkIterator = chunks.listIterator();
        while (chunkIterator.hasNext()){
            Chunks chunk = chunkIterator.next();
            ListIterator<Ground> wallsIterator = walls.listIterator();
            while (wallsIterator.hasNext()) {
                Ground wall = wallsIterator.next();
                if (chunk.chunk.overlaps(wall.wall)) {
                    chunk.containWalls.add(wall.wall);
                }
            }
            System.out.println(chunk.containWalls.size());
        }

    }

}
