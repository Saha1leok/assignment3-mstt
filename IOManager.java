package io;

import graph.Edge;
import graph.Graph;
import mst.KruskalMST;
import mst.PrimMST;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class IOManager {
    public static class GraphWithNames {
        public final Graph graph;
        public final String[] names;
        public final int id;

        public GraphWithNames(Graph graph, String[] names, int id) {
            this.graph = graph;
            this.names = names;
            this.id = id;
        }
    }

    public static GraphWithNames readGraphFromJsonObject(JSONObject gjo) {
        // expected format: { "id": 1, "nodes": ["A","B"...], "edges": [{"from":"A","to":"B","weight":4} ...] }
        int id = gjo.optInt("id", -1);
        JSONArray nodes = gjo.getJSONArray("nodes");
        int V = nodes.length();
        String[] names = new String[V];
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < V; i++) {
            String name = nodes.getString(i);
            names[i] = name;
            idx.put(name, i);
        }
        Graph g = new Graph(V);
        JSONArray edges = gjo.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            String fu = e.getString("from");
            String fv = e.getString("to");
            double w = e.getDouble("weight");
            Integer u = idx.get(fu);
            Integer v = idx.get(fv);
            if (u == null || v == null) throw new IllegalArgumentException("Unknown node in edge: " + fu + "," + fv);
            g.addEdge(new Edge(u, v, w));
        }
        return new GraphWithNames(g, names, id);
    }

    public static JSONObject resultToJson(int graphId, GraphWithNames gwn, PrimMST.Result p, KruskalMST.Result k) {
        JSONObject out = new JSONObject();
        out.put("graph_id", graphId);
        JSONObject inputStats = new JSONObject();
        inputStats.put("vertices", gwn.graph.V());
        inputStats.put("edges", gwn.graph.edges().size());
        out.put("input_stats", inputStats);

        JSONObject prim = new JSONObject();
        JSONArray primEdges = new JSONArray();
        for (Edge e : p.mstEdges) {
            JSONObject eo = new JSONObject();
            eo.put("from", gwn.names[e.u]);
            eo.put("to", gwn.names[e.v]);
            eo.put("weight", e.weight);
            primEdges.put(eo);
        }
        prim.put("mst_edges", primEdges);
        prim.put("total_cost", p.totalWeight);
        prim.put("operations_count", p.operations);
        prim.put("execution_time_ms", p.timeMs);
        prim.put("is_connected", p.isConnected);
        out.put("prim", prim);

        JSONObject kr = new JSONObject();
        JSONArray krEdges = new JSONArray();
        for (Edge e : k.mstEdges) {
            JSONObject eo = new JSONObject();
            eo.put("from", gwn.names[e.u]);
            eo.put("to", gwn.names[e.v]);
            eo.put("weight", e.weight);
            krEdges.put(eo);
        }
        kr.put("mst_edges", krEdges);
        kr.put("total_cost", k.totalWeight);
        kr.put("operations_count", k.operations);
        kr.put("execution_time_ms", k.timeMs);
        kr.put("is_connected", k.isConnected);
        out.put("kruskal", kr);

        return out;
    }

    // utility to read whole input file
    public static JSONObject readInputFile(Path p) throws IOException {
        String s = Files.readString(p);
        return new JSONObject(s);
    }
}
