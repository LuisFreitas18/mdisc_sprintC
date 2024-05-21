package org.example;

import java.io.*;

public class EvacuationRoutePlanner {

    static final String inputCsv = "src/main/java/org/example/dataset/input.csv";
    static final String outputCsv = "src/main/java/org/example/dataset/output.csv";
    static final String outputDot = "src/main/java/org/example/dataset/output.dot";
    static final String outputPng = "src/main/java/org/example/dataset/output.png";

    public static void main(String[] args) {

        /*
        if (args.length != 3) {
            System.out.println("Usage: java EvacuationRoutePlanner <dataset/input.csv> <dataset/output.csv> <dataset/output.dot>");
            return;
        }

        String inputCsv = args[0];
        String outputCsv = args[1];
        String outputDot = args[2];
         */

        Graph graph = new Graph();
        String assemblyPoint = null;

        // Read the CSV file and construct the graph
        try (BufferedReader br = new BufferedReader(new FileReader(inputCsv))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String pointA = parts[0].trim();
                String pointB = parts[1].trim();
                double distance = Double.parseDouble(parts[2].trim());

                graph.addEdge(pointA, pointB, distance);
                assemblyPoint = pointB;  // Assuming Assembly Point is always in the second column
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (assemblyPoint == null) {
            System.out.println("Assembly point not found in the input data.");
            return;
        }

        // Find the shortest path from each vertex to the Assembly Point
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputCsv))) {
            String[] vertices = graph.getVertices();
            for (String vertex : vertices) {
                if (!vertex.equals(assemblyPoint)) {
                    String[] path = graph.shortestPath(vertex, assemblyPoint);
                    double pathDistance = graph.getPathDistance(path);
                    csvWriter.write(String.join(" -> ", path) + "; " + pathDistance + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Generate the DOT file for visualization
        try (BufferedWriter dotWriter = new BufferedWriter(new FileWriter(outputDot))) {
            dotWriter.write("digraph G {\n");
            dotWriter.write("node [shape=circle];\n");

            double[][] adjacencyMatrix = graph.getAdjacencyMatrix();
            String[] vertices = graph.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                for (int j = i + 1; j < vertices.length; j++) {
                    if (adjacencyMatrix[i][j] > 0) {
                        dotWriter.write("\"" + vertices[i] + "\" -> \"" + vertices[j] + "\" [label=\"" + adjacencyMatrix[i][j] + "\"];\n");
                    }
                }
            }
            dotWriter.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        GraphvizRunner.graphGenerator(outputDot, outputPng);

    }
}
