package graph;

public class Edge {
    public final int u;
    public final int v;
    public final double weight;

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int other(int vertex) {
        if (vertex == u) return v;
        if (vertex == v) return u;
        throw new IllegalArgumentException("Vertex " + vertex + " is not incident to this edge");
    }

    @Override
    public String toString() {
        return String.format("(%d - %d : %.2f)", u, v, weight);
    }
}

