Assignment 3 — City Transport Network Optimization (Minimum Spanning Tree)

Overview

This project implements a complete solution for the assignment: reading graph descriptions from JSON files, computing Minimum Spanning Trees (MST) using both Prim's and Kruskal's algorithms, collecting metrics (MST edges, total cost, operation counts and execution time), producing JSON results, and running automated JUnit tests.

Input JSON format

The program expects an input JSON file with a root object that contains an array named "graphs". Each graph must be described as an object with the following fields:
- `id` (integer): a graph identifier.
- `nodes` (array of strings): node names. Node indices are derived from the array order (0..V-1).
- `edges` (array of objects): each edge object must contain `from` (node name), `to` (node name), and `weight` (number).

Example graph object:
{
  "id": 1,
  "nodes": ["A","B","C","D"],
  "edges": [
    {"from":"A","to":"B","weight":4},
    {"from":"B","to":"C","weight":2},
    ...
  ]
}

Output JSON format

The program writes `assign_3_output.json` with a root object containing an array `results`. Each entry corresponds to one input graph and follows this structure:
- `graph_id`: same id as input.
- `input_stats`: object with `vertices` and `edges` counts.
- `prim`: object with fields:
  - `mst_edges`: array of edges in MST (each with `from`, `to`, `weight`).
  - `total_cost`: numeric total weight of MST.
  - `operations_count`: integer (approximate counter of key algorithmic operations).
  - `execution_time_ms`: execution time in milliseconds (long integer).
  - `is_connected`: boolean (true if MST has V-1 edges).
- `kruskal`: same fields as `prim` but for Kruskal's algorithm.

Example output entry:
{
  "graph_id": 1,
  "input_stats": { "vertices": 5, "edges": 7 },
  "prim": { ... },
  "kruskal": { ... }
}

What is implemented

- Graph model: `Graph.java` and `Edge.java` represent an undirected weighted graph.
- Prim's algorithm: `PrimMST.java` uses a priority queue and records the MST edges, total cost, operation count (approximate) and execution time.
- Kruskal's algorithm: `KruskalMST.java` sorts edges and uses a Union-Find structure with path compression and union by rank; it also records metrics.
- IO manager: `IOManager.java` reads the input format and converts results to the required output JSON format.
- Runner: `app.Main` loads the input file (defaults to `assign_3_input.json`), runs both algorithms on each graph, and writes `assign_3_output.json`.
- Tests: JUnit tests are included under `src/test/java` to verify correctness and handle basic edge cases (including disconnected graphs).

Build and run

Requirements:
- Java JDK (matching the project's compiler level in `pom.xml`; adjust if needed).
- Apache Maven available on your PATH.

Run unit tests:

```bash
mvn test
```

Run the application (reads `assign_3_input.json` and writes `assign_3_output.json`):

```bash
mvn -q package
java -cp target/mst3-1.0-SNAPSHOT.jar app.Main
```

You may pass an alternative input file path as the first argument to `app.Main`.

Testing requirements covered

The project includes automated JUnit tests that verify:
- Prim and Kruskal produce the same total MST cost for the same connected input.
- Each MST produced (for connected graphs) has exactly V-1 edges.
- Acyclicity of the returned MST is checked via a simple union-find in tests.
- Disconnected graphs are handled (the MST will contain fewer than V-1 edges and `is_connected` will be false).
- Execution time and operation counts are recorded (non-negative values).

Grading mapping (from assignment criteria)

- Prim’s algorithm implementation – 25%
- Kruskal’s algorithm implementation – 25%
- Analytical report (results, interpretation, comparison, and conclusions) – 25%
- Code readability, commits, and GitHub repository – 15%
- Testing – 10%

Report requirements (what to include in your submitted report)

Prepare an analytical report that contains:
1. Summary of input datasets and results: for each dataset list algorithm used, MST total cost, execution time, and operation count.
2. A comparison of Prim vs Kruskal in terms of theoretical time complexity and the observed performance on your datasets.
3. Conclusions and recommendations stating under which conditions each algorithm is preferable (e.g., graph density, data structures used, implementation complexity).
4. References, if any external materials were used.

How the comparison is done in this project

For each dataset the program records:
- `total_cost` — should match between Prim and Kruskal for correct implementations.
- `execution_time_ms` — elapsed wall time measured in milliseconds.
- `operations_count` — an approximate integer counter of key algorithmic actions. This counter is implementation-specific and intended for relative comparison across datasets and implementations, not as an exact theoretical operation count.
- `is_connected` — indicates whether the input graph is connected (if false, report the graph as disconnected).

Notes and suggestions for further improvements

- Operation counters are approximate; you can extend counters into separate categories (comparisons, union-ops, heap ops) for more granular profiling.
- Add a dataset generator that produces graphs of different sizes and densities (small: 4–6 vertices, medium: 10–15, large: 20–30+) to automate benchmarking.
- Export results to CSV for easier tabular analysis and plotting.
- Provide visualizations of MSTs to help with validation.

If you want, I can also generate additional input datasets (small/medium/large of varying density) and add a CSV exporter for the results. If you'd like that, tell me how many datasets of each size and the desired densities and I'll generate them and add the export step.
