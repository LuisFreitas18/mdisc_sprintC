package org.example;

public class Graph {
    private final int MAX_VERTICES = 100;
    private final double[][] adjacencyMatrix = new double[MAX_VERTICES][MAX_VERTICES];
    private final String[] vertices = new String[MAX_VERTICES];
    private int vertexCount = 0;

    public void addEdge(String start, String end, double distance) {
        int startIndex = getVertexIndex(start);
        int endIndex = getVertexIndex(end);
        adjacencyMatrix[startIndex][endIndex] = distance;
        adjacencyMatrix[endIndex][startIndex] = distance;  // Since the graph is undirected
    }

    private int getVertexIndex(String vertex) {
        for (int i = 0; i < vertexCount; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }
        vertices[vertexCount] = vertex;
        return vertexCount++;
    }

    public String[] shortestPath(String start, String goal) {
        int startIdx = getVertexIndex(start);
        int goalIdx = getVertexIndex(goal);

        double[] distances = new double[vertexCount];
        boolean[] visited = new boolean[vertexCount];
        int[] previous = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            distances[i] = Double.MAX_VALUE;
            visited[i] = false;
            previous[i] = -1;
        }
        distances[startIdx] = 0.0;

        for (int i = 0; i < vertexCount - 1; i++) {
            int current = getMinDistanceVertex(distances, visited);
            if (current == -1) break;
            visited[current] = true;
            for (int j = 0; j < vertexCount; j++) {
                if (!visited[j] && adjacencyMatrix[current][j] > 0 &&
                        distances[current] + adjacencyMatrix[current][j] < distances[j]) {
                    distances[j] = distances[current] + adjacencyMatrix[current][j];
                    previous[j] = current;
                }
            }
        }

        String[] path = reconstructPath(previous, goalIdx);
        return path;
    }

    private int getMinDistanceVertex(double[] distances, boolean[] visited) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < vertexCount; i++) {
            if (!visited[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private String[] reconstructPath(int[] previous, int goalIdx) {
        int[] tempPath = new int[vertexCount];
        int count = 0;
        for (int at = goalIdx; at != -1; at = previous[at]) {
            tempPath[count++] = at;
        }
        String[] path = new String[count];
        for (int i = 0; i < count; i++) {
            path[i] = vertices[tempPath[count - i - 1]];
        }
        return path;
    }

    public double getPathDistance(String[] path) {
        double distance = 0.0;
        for (int i = 0; i < path.length - 1; i++) {
            int currentIdx = getVertexIndex(path[i]);
            int nextIdx = getVertexIndex(path[i + 1]);
            distance += adjacencyMatrix[currentIdx][nextIdx];
        }
        return distance;
    }

    public String[] getVertices() {
        String[] result = new String[vertexCount];
        System.arraycopy(vertices, 0, result, 0, vertexCount);
        return result;
    }

    public double[][] getAdjacencyMatrix() {
        double[][] result = new double[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, result[i], 0, vertexCount);
        }
        return result;
    }
}
