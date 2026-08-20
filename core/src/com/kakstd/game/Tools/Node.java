package com.kakstd.game.Tools;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Node {
    public Vector2 nodePos;
    public Vector2 targetPos;
    public Node previousNode;
    int f, g, h;
    public int c;

    public Node (int g, Vector2 nodePos, Vector2 targetPos, Node previousNode){
        this.nodePos = nodePos;
        this.targetPos = targetPos;
        this.previousNode = previousNode;
        this.g = g;

        h = (int)((Math.abs(targetPos.x - nodePos.x)) + (Math.abs(targetPos.y - nodePos.y)));

        f = g + h;

    }
}
