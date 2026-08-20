package com.kakstd.game.Tools;

import java.util.Comparator;

public class NodesComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        if (node1.f > node2.f){
            return 1;
        } else if (node1.f < node2.f) {
            return -1;
        }else {
            return 0;
        }
    }
}
