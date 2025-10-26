package mst;

import graph.Edge;
import graph.Graph;

import java.util.*;

public class PrimMST {
    public static class Result {
        public final List<Edge> mstEdges;
        public final double totalWeight;
        public final long operations;
        public final long timeMs;
        public final boolean isConnected;

        public Result(List<Edge> mstEdges, double totalWeight, long operations, long timeMs, boolean isConnected) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.operations = operations;
            this.timeMs = timeMs;
            this.isConnected = isConnected;
        }
    }

    public static Result run(Graph g) {
        long start = System.currentTimeMillis();
        int V = g.V();
        if (V == 0) return new Result(Collections.emptyList(), 0.0, 0, 0, true);
        boolean[] visited = new boolean[V];
        List<Edge> mst = new ArrayList<>();
        long ops = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        visited[0] = true;
        for (Edge e : g.adj(0)) pq.add(e);
        ops += g.adj(0).size();

        while (!pq.isEmpty() && mst.size() < V - 1) {
            Edge e = pq.poll();
            ops++; // poll
            int u = e.u, v = e.v;
            int next = visited[u] ? v : u;
            if (visited[next]) continue; // both visited
            mst.add(e);
            visited[next] = true;
            for (Edge ne : g.adj(next)) {
                if (!visited[ne.other(next)]) {
                    pq.add(ne);
                    ops++; // add
                }
            }
        }

        double total = mst.stream().mapToDouble(ed -> ed.weight).sum();
        long time = System.currentTimeMillis() - start;
        boolean connected = mst.size() == V - 1;
        return new Result(mst, total, ops, time, connected);
    }
}
