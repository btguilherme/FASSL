/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.methods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Guilherme Camargo
 * https://algorithms.tutorialhorizon.com/kruskals-algorithm-minimum-spanning-tree-mst-complete-java-implementation/
 */
public class KrushkalMST {

    static class Edge {

        int source;
        int destination;
        double weight;

        public Edge(int source, int destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Graph {

        int vertices;
        ArrayList<Edge> allEdges = new ArrayList<>();

        Graph(int vertices) {
            this.vertices = vertices;
        }

        public void addEgde(int source, int destination, double weight) {
            Edge edge = new Edge(source, destination, weight);
            allEdges.add(edge); //add to total edges
        }

        public List<Integer> kruskalMST() {
            PriorityQueue<Edge> pq = new PriorityQueue<>(allEdges.size(), Comparator.comparingDouble(o -> o.weight));

            //add all the edges to priority queue, //sort the edges on weights
            for (int i = 0; i < allEdges.size(); i++) {
                pq.add(allEdges.get(i));
            }

            //create a parent []
            int[] parent = new int[vertices];

            //makeset
            makeSet(parent);

            ArrayList<Edge> mst = new ArrayList<>();

            //process vertices - 1 edges
            int index = 0;
            while (index < vertices - 1) {
                Edge edge = pq.remove();
                //check if adding this edge creates a cycle
                int x_set = find(parent, edge.source);
                int y_set = find(parent, edge.destination);

                if (x_set == y_set) {
                    //ignore, will create cycle
                } else {
                    //add it to our final result
                    mst.add(0, edge);
                    index++;
                    union(parent, x_set, y_set);
                }
            }
            //print MST
//            System.out.println("Minimum Spanning Tree: ");
//            printGraph(mst);
            
            Set<Integer> aux = new LinkedHashSet<>();
            for (int i = 0; i < mst.size(); i++) {
                Edge edge = mst.get(i);
                aux.add(edge.source);
                aux.add(edge.destination);
            }
            
            List<Integer> indexes = aux.stream().collect(Collectors.toList()); 
            
            return indexes;
            
        }

        public void makeSet(int[] parent) {
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i < vertices; i++) {
                parent[i] = i;
            }
        }

        public int find(int[] parent, int vertex) {
            //chain of parent pointers from x upwards through the tree
            // until an element is reached whose parent is itself
            if (parent[vertex] != vertex) {
                return find(parent, parent[vertex]);
            };
            return vertex;
        }

        public void union(int[] parent, int x, int y) {
            int x_set_parent = find(parent, x);
            int y_set_parent = find(parent, y);
            //make x as parent of y
            parent[y_set_parent] = x_set_parent;
        }

        public void printGraph(ArrayList<Edge> edgeList) {
            for (int i = 0; i < edgeList.size(); i++) {
                Edge edge = edgeList.get(i);
                System.out.println("Edge-" + i + " source: " + edge.source
                        + " destination: " + edge.destination
                        + " weight: " + edge.weight);
                
            }
        }
    }
//    public static void main(String[] args) {
//        int vertices = 6;
//        Graph graph = new Graph(vertices);
//        graph.addEgde(0, 1, 4.1);
//        graph.addEgde(0, 2, 3.4);
//        graph.addEgde(1, 2, 1.1);
//        graph.addEgde(1, 3, 2.9);
//        graph.addEgde(2, 3, 4.4);
//        graph.addEgde(3, 4, 2.7);
//        graph.addEgde(4, 5, 6.2);
//        graph.kruskalMST();
//    }
}
