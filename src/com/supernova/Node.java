package com.supernova;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private int id;

    private int label;

    public ArrayList<Node> neighbors;

    public int getId() { return id; }

    public int getLabel() { return label; }

    public int getDegree() { return neighbors.size(); }

    Node(int id, int label) {
        this.id = id;
        this.label = label;
        this.neighbors = new ArrayList<>();
    }

    Node(int id, ArrayList<Node> neighbors ){
        this.id = id;
        this.neighbors = neighbors;
    }

    @Override
    public boolean equals(Object obj) {
        Node _obj = (Node) obj;
        return id == _obj.id;
    }

    @Override
    public int compareTo(Node node) {
        // ascending order of label first
        int labelCmp = this.getLabel() - node.getLabel();
        if (labelCmp != 0) {
            return labelCmp;
        }
        // descending order of degree second
        int degreeCmp = node.getDegree() - this.getDegree();
        if (degreeCmp != 0) {
            return degreeCmp;
        }
        return this.id - node.id;
    }
}
