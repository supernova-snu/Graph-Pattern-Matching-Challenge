package com.supernova;
import java.util.*;
/*
* queryId 0부터 끝까지 시행
* 새로운 vertex를 받을 때 이전 queryId들에 대해 연결되는 것이 없는지 다시 돌아가서 체크(BackTracking)
* 만약 currentPath에 해당하는 queryId에 대한 dataVertex가 있으면 valid
* valid 체크를 하며 currentPath 가 다 채워지면 print
* */
public class BackTrack {

    Graph data;
    Graph query;
    CandidateSet cs;

    BackTrack(Graph data, Graph query, CandidateSet cs){
        this.data = data;
        this.query = query;
        this.cs = cs;
    }

    public void printAllMatches() {

        System.out.println("t " + query.getNumNodes());

        int i=0;
        int flag;
        ArrayList<Integer> currentPath = new ArrayList<>(query.getNumNodes());

        // BackTracking
        while(i>-1){

            flag = 0;
            ArrayList<Integer> possibleNextVertices = findPossibleVerticeIds(currentPath);

            // 가능한 x_i에 대한 반복문
            for(Integer nextVertex : possibleNextVertices){
                // 현재 유효할 때
                if(isPossiblePath(currentPath,nextVertex)){
                    flag = 1;
                    currentPath.add(nextVertex);
                    // matching 성공
                    if(currentPath.size() == query.getNumNodes()){
                        System.out.print("a");
                        for(Integer matchingVertices : currentPath){
                            System.out.print(" "+matchingVertices);
                        }
                        System.out.println();
                        return;
                    }

                    i=i+1;
                    break;
                }
            }
            // 위에서 유효한 nextVertice가 없을 때
            if(flag==0){
                i=i-1;
            }
        }

    }

    // T(x_1,x_2,...,x_i-1)
    private ArrayList<Integer> findPossibleVerticeIds(ArrayList<Integer> currentVerticeIds){
        int nextQueryId = currentVerticeIds.size();
        return this.cs.mappingSet.get(nextQueryId);
    }

    // f(x_1,x_2,...,x_i)
    private boolean isPossiblePath(ArrayList<Integer> currentPathIds,Integer newVerticeId ){

        int newQueryId = currentPathIds.size();

        ArrayList<Node> shouldConnentQueries = this.query.adjacencyMap.get(newQueryId);

        // newVerticeId 가 연결되어야할 targetQuery
        for(Node targetQuery: shouldConnentQueries){
            // 이미 sorting되어있으므로 새로들어온 쿼리의 id보다 크면 검사안함
            if(targetQuery.getId()>newQueryId){
                break;
            }

            boolean valid =false;
            // targetQuery에 매핑된 data vertex id에 대해 currentPath에 있으면 ok, 없으면 return false
            for(Integer dataVertexId : this.cs.mappingSet.get(targetQuery.getId())){
                // newVertice에 연겱된 node들을 id 배열로 변환
                ArrayList<Integer> neighborIds = new ArrayList<Integer>(data.adjacencyMap.get(newVerticeId).size());
                for(Node node : data.adjacencyMap.get(newVerticeId)){
                    neighborIds.add(node.getId());
                }
                // newVertice에 연결된 dataVertextId가 있을 때 valid
                if(neighborIds.contains(dataVertexId)){
                    valid =true;
                    break;
                }
            }
            // targetQuery들 중 하나라도 연결된 부분이 없으면 return false
            if(!valid){
                return false;
            }
        }

        return true;
    }

}
