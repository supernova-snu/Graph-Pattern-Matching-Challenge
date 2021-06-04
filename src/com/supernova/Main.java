package com.supernova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Graph readGraphFile(boolean isDataGraph, String path) throws IOException {
        List<String> graphLines = Files.readAllLines(Paths.get(path));

        String[] tagElements = graphLines.get(0).strip().split(" ");
        if (!tagElements[0].equals("t")) {
            System.err.println("Wrong tag line found\n");
            System.exit(0);
        }
        Integer graphId = Integer.valueOf(tagElements[1]);
        Integer numVertices = Integer.valueOf(tagElements[2]);

        List<Node> nodes = new ArrayList<>(numVertices);
        List<Integer[]> edgeLines = new ArrayList<>(graphLines.size()-numVertices-1);
        // edgeLines is a list of integer pairs(2-tuple)

        for (String line : graphLines.subList(1, 1+numVertices)) {
            String[] elements = line.split(" ");
            if (!elements[0].equals("v")) {
                System.err.println("Wrong vertex line found\n");
                System.exit(0);
            }

            Integer vertexId = Integer.valueOf(elements[1]);
            Integer vertexLabel = Integer.valueOf(elements[2]);
            nodes.add(new Node(vertexId, vertexLabel));
        }

        for (String line : graphLines.subList(1+numVertices, graphLines.size())) {
            String[] elements = line.split(" ");
            if (!elements[0].equals("e")) {
                System.err.println("Wrong edge line found\n");
                System.exit(0);
            }

            Integer fromVertexId = Integer.valueOf(elements[1]);
            Integer toVertexId = Integer.valueOf(elements[2]);
            // edge label은 사용하지 않음
            edgeLines.add(new Integer[]{fromVertexId, toVertexId});
        }

        return new Graph(isDataGraph, graphId, nodes, edgeLines);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Main <data graph file> <query graph file> <candidate set file>\n");
            System.exit(0);
        }

        // example command
        // java Main data/lcc_yeast.igraph query/lcc_yeast_n1.igraph candidate_set/lcc_yeast_n1.cs

        try {
            Graph dataGraph = readGraphFile(true, args[0]);
            Graph queryGraph = readGraphFile(false, args[1]);

            dataGraph.printAdjacencyList();
            queryGraph.printAdjacencyList();

            BackTrack backTrack = new BackTrack();

            List<String> candidateLines = Files.readAllLines(Paths.get(args[2]));
            CandidateSet candidateSet = new CandidateSet(); // FIXME:

            backTrack.printAllMatches(dataGraph, queryGraph, candidateSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


