package mst;

import graph.Edge;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    private Graph sampleGraph() {
        Graph g = new Graph(4);
        g.addEdge(new Edge(0,1,1));
        g.addEdge(new Edge(0,2,4));
        g.addEdge(new Edge(0,3,3));
        g.addEdge(new Edge(1,3,2));
        g.addEdge(new Edge(2,3,5));
        return g;
    }

    @Test
    public void testPrimAndKruskalSameCost() {
        Graph g = sampleGraph();
        PrimMST.Result p = PrimMST.run(g);
        KruskalMST.Result k = KruskalMST.run(g);
        assertEquals(p.totalWeight, k.totalWeight, 1e-6);
        assertEquals(g.V() - 1, p.mstEdges.size());
        assertEquals(g.V() - 1, k.mstEdges.size());
    }

    @Test
    public void testAcyclicAndConnected() {
        Graph g = sampleGraph();
        PrimMST.Result p = PrimMST.run(g);
        assertEquals(g.V() - 1, p.mstEdges.size());
        // check acyclic via simple union-find
        int V = g.V();
        int[] parent = new int[V];
        for (int i = 0; i < V; i++) parent[i] = i;
        for (Edge e : p.mstEdges) {
            int a = find(parent, e.u);
            int b = find(parent, e.v);
            assertNotEquals(a, b);
            parent[a] = b;
        }
    }

    private int find(int[] parent, int x) {
        if (parent[x] == x) return x;
        parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    @Test
    public void testDisconnectedGraphHandled() {
        Graph g = new Graph(4);
        g.addEdge(new Edge(0,1,1));
        // nodes 2 and 3 disconnected
        PrimMST.Result p = PrimMST.run(g);
        KruskalMST.Result k = KruskalMST.run(g);
        // MST edge count less than V-1
        assertTrue(p.mstEdges.size() < g.V() - 1);
        assertTrue(k.mstEdges.size() < g.V() - 1);
    }
}

