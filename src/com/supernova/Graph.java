package com.supernova;


import java.util.*;

public class Graph {
    private int id;

    private boolean isDataGraph;

    private int numNodes;

    private int numEdges;

    private int numLabels;

    private int maxLabels;

    public Set<Integer> labelSet;

    public HashMap<Integer, Node> nodeMap;

    public HashMap<Integer, ArrayList<Node>> adjacencyMap;

    public int getNumNodes() { return numNodes; }

    public int getNumEdges() { return numEdges; }

    public int getNumLabels() { return numLabels; }

    public int getMaxLabels() { return maxLabels; }

    public static boolean isNeighbor(Node x, Node y) { return x.neighbors.contains(y); }

    public Graph(boolean isDataGraph, int id, List<Node> nodes, List<Integer[]> edges) {
        // initialize
        this.id = id;
        this.isDataGraph = isDataGraph;
        this.numNodes = nodes.size();
        this.numEdges = edges.size();

        labelSet = new HashSet<>();
        nodeMap = new HashMap<>();
        adjacencyMap = new HashMap<>();

        // initialize node hash map (id -> node)
        nodes.forEach(node -> {
            labelSet.add(node.getLabel());
            nodeMap.put(node.getId(), node);
            adjacencyMap.put(node.getId(), new ArrayList<>());
        });

        // initialize adjacency map (node -> sorted node list)
        edges.forEach(elems -> {
            Node fromNode = nodeMap.get(elems[0]);
            Node toNode = nodeMap.get(elems[1]);
            // undirected graph
            adjacencyMap.get(fromNode.getId()).add(toNode);
            adjacencyMap.get(toNode.getId()).add(fromNode);
            fromNode.neighbors.add(toNode);
            toNode.neighbors.add(fromNode);
        });

        // find num, max label
        numLabels = labelSet.size();
        maxLabels = Collections.max(labelSet);

        // sort adjacent nodes list by id, ascending order
        for (Map.Entry<Integer, ArrayList<Node>> entry : adjacencyMap.entrySet()) {
            Node fromNode = nodeMap.get(entry.getKey());
            Collections.sort(fromNode.neighbors);
            Collections.sort(entry.getValue());
        }
    }

    public void printAdjacencyList() {
        for (Map.Entry<Integer, ArrayList<Node>> entry : adjacencyMap.entrySet()) {
            Node node = nodeMap.get(entry.getKey());
            node.neighbors.forEach(toNode -> {
                System.out.printf("Node %d (%d) -> Node %d (%d)\n", node.getId(), node.getLabel(), toNode.getId(), toNode.getLabel());
            });
        }
    }
}
