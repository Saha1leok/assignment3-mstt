package graph;

import java.util.*;

public class Graph {
    private final int V;
    private final List<Edge> edges;
    private final List<List<Edge>> adj;

    public Graph(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public int V() { return V; }

    public void addEdge(Edge e) {
        edges.add(e);
        adj.get(e.u).add(e);
        adj.get(e.v).add(e);
    }

    public List<Edge> edges() { return Collections.unmodifiableList(edges); }

    public List<Edge> adj(int v) { return Collections.unmodifiableList(adj.get(v)); }
}

