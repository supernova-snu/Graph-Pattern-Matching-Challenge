package com.supernova;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
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
            CandidateSet candidateSet = readSetFile(args[2]);

//            dataGraph.printAdjacencyList();
//            queryGraph.printAdjacencyList();
//            candidateSet.printCandidateSet();

            BackTrack backTrack = new BackTrack(dataGraph, queryGraph, candidateSet);

            backTrack.printAllMatches();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static CandidateSet readSetFile(String path) throws IOException {
        List<String> setLines = Files.readAllLines(Paths.get(path));

        String[] tagElements = setLines.get(0).strip().split(" ");
        if (!tagElements[0].equals("t")) {
            System.err.println("Wrong tag line found\n");
            System.exit(0);
        }

        Integer numQueryVertices = Integer.valueOf(tagElements[1]);

        // index가 queryId 이고 원소가 mapping되는 data vertices id 의 배열
        ArrayList<ArrayList<Integer>> mappingSet = new ArrayList<>(numQueryVertices);

        for (String line : setLines.subList(1, 1+numQueryVertices)) {

            String[] elements = line.split(" ");

            if (!elements[0].equals("c")) {
                System.err.println("Wrong candidate set line found\n");
                System.exit(0);
            }

            Integer vertexQueryId = Integer.valueOf(elements[1]);
            Integer numDataVertices = Integer.valueOf(elements[2]);

            // 해당하는 query id에 매핑되는 data vertices 배열
            ArrayList<Integer> mappingDataVertices = new ArrayList<>(numDataVertices);
            for(int i=3;i<3+numDataVertices;i++){
                mappingDataVertices.add(Integer.valueOf(elements[i]));
            }

            mappingSet.add(vertexQueryId,mappingDataVertices);
        }

        return new CandidateSet(mappingSet);
    }
}


