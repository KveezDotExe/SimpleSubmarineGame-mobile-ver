package com.kakstd.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kakstd.game.Screens.PlayScreen;
import com.kakstd.game.Sprites.Enemy;
import com.kakstd.game.Sprites.Ground;
import com.kakstd.game.Sprites.Player;
import com.kakstd.game.SubmarineGame;

import org.lwjgl.Sys;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

public class AutoPlay{
    private boolean active;
    private Player playerBody;
    private Body enemyBody;
    private Vector2 topVector;
    private Vector2 midVector;
    private Vector2 botVector;
    private float timer = 0;
    Vector2 moveTo = new Vector2(0,0);
    public LinkedList<Enemy> enemyPool;
    public LinkedList<Collectables> collectablesPool;
    public LinkedList<Vector2> path;
    public LinkedList<Node> checkedNodes;
    public LinkedList<Node> waitingNodes;
    public NodesComparator comparator = new NodesComparator();
    public boolean letCalc = false;


    public AutoPlay (boolean active, Player playerBody){

        this.active = active;
        this.playerBody = playerBody;
        midVector = new Vector2();
        enemyPool = new LinkedList<>();
        collectablesPool = new LinkedList<>();


    }
    public boolean isActive(){
        return active;
    }
    public void switchActive(boolean x){
        active = x;
    }
    private void playerMoving(){

        playerAI();

    }
    private Vector2 calculateWay(){
        Vector2 vector2 = new Vector2();
        float x, y;

        x = MathUtils.random(-100,100);
        y = MathUtils.random(-100,100);
        vector2.x = x;
        vector2.y = y;
        vector2.nor().scl(playerBody.playerSpeed);

        return vector2;
    }
    private void playerAim(){

    }
    public LinkedList<Vector2> getPath (){
        path = new LinkedList<>();
        waitingNodes = new LinkedList<>();
        checkedNodes = new LinkedList<>();
        Vector2 targetPos;
        Vector2 startPos = new Vector2(MathUtils.round(playerBody.b2d.getPosition().x), MathUtils.round(playerBody.b2d.getPosition().y));
        Rectangle posRect = new Rectangle((startPos.x - 16f/SubmarineGame.PPM), startPos.y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);
        if (collectablesPool.size() > 0 && isWalkable(posRect)) {

           targetPos = new Vector2(MathUtils.round(collectablesPool.getFirst().b2d.getPosition().x), MathUtils.round(collectablesPool.getFirst().b2d.getPosition().y));
           Rectangle tarPos = new Rectangle((targetPos.x - 16f/SubmarineGame.PPM), targetPos.y - 16f/SubmarineGame.PPM, 32f/SubmarineGame.PPM, 32f/SubmarineGame.PPM);
            if (!isWalkable(tarPos)) {
                collectablesPool.removeFirst();
                return path;
            }




            //targetPos = new Vector2(10,140);
            //System.out.println("1");
            if(startPos.isOnLine(targetPos)){
                System.out.println("2");
                return path;
            }
            Node startNode = new Node(0, startPos, targetPos, null);
            checkedNodes.add(startNode);
            waitingNodes.addAll(getNeighbourNodes(startNode));
            Collections.sort(waitingNodes, comparator);
           // System.out.println("3");

            for (int i = 0; i < waitingNodes.size(); i++){
               // System.out.println("4");
                Collections.sort(waitingNodes, comparator);
                Node nodeToCheck = waitingNodes.pollFirst();
                Rectangle pathPoint = new Rectangle();
                pathPoint.width = 32f/SubmarineGame.PPM;
                pathPoint.height = 32f/SubmarineGame.PPM;
                pathPoint.setPosition(nodeToCheck.nodePos.x - pathPoint.width/2, nodeToCheck.nodePos.y - pathPoint.height/2);
                boolean walkable = isWalkable(pathPoint);
                if (!walkable){
                    nodeToCheck.c = 1;
                  //  System.out.println("5");
                    checkedNodes.add(nodeToCheck);
                } else{
                  //  System.out.println("6");


                    if (!isEqualsNode(nodeToCheck)){

                        checkedNodes.add(nodeToCheck);
                        waitingNodes.addAll(getNeighbourNodes(nodeToCheck));
                    }
                }
              //  System.out.println("8");


                if (MathUtils.round(nodeToCheck.nodePos.x) == MathUtils.round(targetPos.x) && MathUtils.round(nodeToCheck.nodePos.y) == MathUtils.round(targetPos.y)){


                    return calculateFromNode(nodeToCheck);
                }


            }


        }





        return path;
    }

    public LinkedList<Node> sameNodes(LinkedList<Node> checkedNodes, Node nodeToCheck){
        LinkedList<Node> sameNodeList = new LinkedList<>();

        ListIterator<Node> iterator = checkedNodes.listIterator();
        while (iterator.hasNext()){
            Node nodeFromList = iterator.next();
            if (nodeFromList.nodePos == nodeToCheck.nodePos){
                sameNodeList.add(nodeFromList);
            }
        }

        return sameNodeList;

    }

    public boolean isEqualsNode(Node nodeToCheck){
        boolean equals = true;
        ListIterator<Node> iterator = checkedNodes.listIterator();
        while (iterator.hasNext()){
            Node node = iterator.next();
            if (node.nodePos.x == nodeToCheck.nodePos.x && node.nodePos.y == nodeToCheck.nodePos.y){
                equals = true;
                break;
            }else {
                equals = false;
            }

        }

        return  equals;
    }

    public boolean isWalkable(Rectangle point){
        boolean walkable = false;
        ListIterator<Chunks> chunkIterator = B2WorldCreator.chunks.listIterator();
        while (chunkIterator.hasNext()){
            Chunks chunk = chunkIterator.next();
            if (chunk.chunk.contains(playerBody.b2d.getPosition())){
                ListIterator<Rectangle> wallIterator = chunk.containWalls.listIterator();
                while (wallIterator.hasNext()){
                    Rectangle wall = wallIterator.next();
                    if (wall.overlaps(point)){
                        walkable = false;
                        return walkable;
                    }else {
                        walkable = true;
                    }
                }

            }
        }
        /*for (int i = 0; i < B2WorldCreator.chunks.size(); i++) {
            if (B2WorldCreator.chunks.get(i).chunk.contains(playerBody.b2d.getPosition())){
                for (int j = 0; j < B2WorldCreator.chunks.get(i).containWalls.size(); j++) {
                    if (B2WorldCreator.chunks.get(i).containWalls.get(j).overlaps(point)){
                        walkable = false;
                        break;
                    }else {
                        walkable = true;
                    }
                }
            }
        }*/

        return walkable;
    }
    public LinkedList<Vector2> calculateFromNode(Node node){
        LinkedList<Vector2> path = new LinkedList<>();
        Node currNode = node;
        while (currNode.previousNode != null){
            path.add(new Vector2(currNode.nodePos));
            currNode = currNode.previousNode;
        }

        return path;
    }
    public LinkedList<Node> getNeighbourNodes(Node node){
        LinkedList<Node> neighbours = new LinkedList<>();
        neighbours.add(new Node(node.g+1, new Vector2(node.nodePos.x-1f, node.nodePos.y), node.targetPos, node));
        neighbours.add(new Node(node.g+1, new Vector2(node.nodePos.x+1f, node.nodePos.y), node.targetPos, node));
        neighbours.add(new Node(node.g+1, new Vector2(node.nodePos.x, node.nodePos.y-1f), node.targetPos, node));
        neighbours.add(new Node(node.g+1, new Vector2(node.nodePos.x, node.nodePos.y+1f), node.targetPos, node));




        return neighbours;
    }

    public void playerAI(){
        path = getPath();
        if (path.size() > 0){

            Vector2 vec1 = new Vector2(path.pollLast());
            vec1.sub(playerBody.b2d.getPosition());
            if (playerBody.getBody().getLinearVelocity().len2() < playerBody.playerMaxSpeed * playerBody.playerMaxSpeed){
                playerBody.getBody().applyLinearImpulse(vec1.nor().scl(playerBody.playerSpeed), playerBody.getBody().getWorldCenter(), true);
            }
        }
    }
    public void AI (float dt){
        if (isActive() && playerBody.isAlive) {
            timer += dt;
            playerMoving();

        }
    }

}

