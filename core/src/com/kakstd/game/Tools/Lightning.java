package com.kakstd.game.Tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.SubmarineGame;

import java.util.ArrayList;
import java.util.ListIterator;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Lightning implements Disposable {
    public RayHandler rayHandler;
    private ArrayList<PointLight> pl = new ArrayList<>();
    private ListIterator<PointLight> iterator;
    private OrthographicCamera cam;
    private Body body;
    private Player player;
    private BodyDef bdef;
    private ConeLight coneLight;

    public Lightning(TiledMap map, World world, Player b){
        player = b;
        rayHandler = new RayHandler(world);


        for (MapObject object: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.CORAL, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.LIME, 0.5f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.GRAY, 0.5f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.OLIVE, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.RED, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.CYAN, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(16).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.FIREBRICK, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(17).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.FOREST, 0.5f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(18).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.LIGHT_GRAY, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        for (MapObject object: map.getLayers().get(19).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.x + rectangle.getWidth()/2)/ SubmarineGame.PPM, (rectangle.y + rectangle.getHeight()/2)/ SubmarineGame.PPM);
            body = world.createBody(bdef);
            pl.add(new PointLight(rayHandler, 5, Color.SKY, 0.25f,body.getPosition().x, body.getPosition().y));
        }
        iterator = pl.listIterator();
        while (iterator.hasNext()){
            PointLight pointLight = iterator.next();
            pointLight.setStaticLight(true);
            pointLight.setSoft(true);
            pointLight.setXray(true);
            pointLight.setSoftnessLength(0);
        }
        coneLight = new ConeLight(rayHandler, 32, Color.WHITE, 3, 0,0, player.b2d.getAngle(), 60);
        coneLight.setContactFilter(SubmarineGame.LIGHT_BIT, (short) 0, SubmarineGame.GROUND_BIT);
        coneLight.attachToBody(player.b2d, player.offsetLight_X, player.offsetLight_Y);
        coneLight.setSoftnessLength(0);

        rayHandler.setShadows(false);
        rayHandler.setAmbientLight(0.5f);
    }

    public void update(float dt){
        rayHandler.update();
    }
    public void render(float dt){
        rayHandler.render();
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
    }
}
