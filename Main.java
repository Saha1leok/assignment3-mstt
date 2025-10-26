package app;

import io.IOManager;
import io.IOManager.GraphWithNames;
import mst.KruskalMST;
import mst.PrimMST;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Path input = Paths.get("assign_3_input.json");
        if (args.length > 0) input = Paths.get(args[0]);

        JSONObject root = IOManager.readInputFile(input);
        JSONArray graphs = root.getJSONArray("graphs");
        JSONArray results = new JSONArray();
        for (int i = 0; i < graphs.length(); i++) {
            JSONObject gjo = graphs.getJSONObject(i);
            GraphWithNames gwn = IOManager.readGraphFromJsonObject(gjo);
            PrimMST.Result p = PrimMST.run(gwn.graph);
            KruskalMST.Result k = KruskalMST.run(gwn.graph);
            JSONObject res = IOManager.resultToJson(gwn.id, gwn, p, k);
            results.put(res);
        }
        JSONObject out = new JSONObject();
        out.put("results", results);
        Files.writeString(Paths.get("assign_3_output.json"), out.toString(2));
        System.out.println("Processed " + graphs.length() + " graphs. Output written to assign_3_output.json");
    }
}
