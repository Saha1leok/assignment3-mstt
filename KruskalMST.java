package mst;

import graph.Edge;
import graph.Graph;

import java.util.*;

public class KruskalMST {
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

    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        private long ops = 0;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        public int find(int x) {
            ops++; // comparison
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        public boolean union(int a, int b) {
            ops++; // comparison
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) parent[ra] = rb;
            else if (rank[rb] < rank[ra]) parent[rb] = ra;
            else { parent[rb] = ra; rank[ra]++; }
            ops++; // union action
            return true;
        }

        public long getOps() { return ops; }
    }

    public static Result run(Graph g) {
        long start = System.currentTimeMillis();
        List<Edge> edges = new ArrayList<>(g.edges());
        edges.sort(Comparator.comparingDouble(e -> e.weight));
        UnionFind uf = new UnionFind(g.V());
        List<Edge> mst = new ArrayList<>();
        long totalOpsBefore = uf.getOps();
        for (Edge e : edges) {
            if (uf.find(e.u) != uf.find(e.v)) {
                if (uf.union(e.u, e.v)) {
                    mst.add(e);
                }
            }
        }
        long ops = uf.getOps() - totalOpsBefore + edges.size(); // count iterations as comparisons
        double total = mst.stream().mapToDouble(ed -> ed.weight).sum();
        long time = System.currentTimeMillis() - start;
        boolean connected = mst.size() == g.V() - 1;
        return new Result(mst, total, ops, time, connected);
    }
}
