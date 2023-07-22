import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashSet;

// may optimize residual graph making with hashsets...?
class Main {
  static int n;
  static int m;
  static int[] scores;
  static int maxflow;
  static int[][] adjMatrix;
  static int profit_upper_bound = 0;

  static ArrayList<HashSet<Integer>> adjList;

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int[] nm = spl(br.readLine(), 2);
    m = nm[1];
    n = nm[0];
    scores = spl(br.readLine(), nm[0]);
    adjMatrix = new int[n + 2][n + 2];
    adjList = new ArrayList<HashSet<Integer>>(n + 2);
    for (int i = 0; i < n + 2; i++)
      adjList.add(new HashSet<Integer>());
    for (int i = 0; i < m; i++) {
      int[] e = spl(br.readLine(), 3);
      add_edge(e[0] - 1, e[1] - 1, e[2]);
    }
    adjMatrix = ff(0, n - 1, n, adjMatrix);
    scores[n - 1] = -30000; // never take sink
    maxflow = 0;
    remake_resid(n, n + 1);
    add_edge(n, 0, 30000); // always take source
    adjMatrix = ff(n, n + 1, n + 2, adjMatrix);
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
    out.write(Integer.toString(profit_upper_bound - maxflow));
    out.write("\n");
    out.flush();
  }

  public static int[] spl(String s, int runs) {
    int[] res = new int[runs];
    int start = 0;
    int end;
    for (int i = 0; i < runs - 1; i++) {
      end = s.indexOf(' ', start);
      res[i] = Integer.parseInt(s.substring(start, end));
      start = end + 1;
    }
    res[runs - 1] = Integer.parseInt(s.substring(start));
    return res;
  }

  public static int[][] ff(int source, int sink, int n, int[][] graph) {
    int[][] path = new int[n][2];
    boolean[] visited = new boolean[n];
    int curr = 0;
    visited[source] = true;
    path[curr][0] = source;
    path[curr][1] = 30000;
    while (path[curr][0] != sink) {
      boolean fail = true;
      for (int neighbor : adjList.get(path[curr][0])) {
        if (graph[path[curr][0]][neighbor] > 0 && !visited[neighbor]) {
          curr++;
          path[curr][0] = neighbor;
          path[curr][1] = Math.min(path[curr - 1][1], graph[path[curr - 1][0]][path[curr][0]]);
          visited[neighbor] = true;
          fail = false;
          break;
        }
      }
      if (fail) {
        if (curr == 0)
          return graph;
        else {
          curr--;
        }
      }
    }
    // unravel
    int flow = path[curr][1];
    maxflow += flow;
    for (; curr > 0; curr--) {
      graph[path[curr - 1][0]][path[curr][0]] -= flow; // decrement forward edge
      graph[path[curr][0]][path[curr - 1][0]] += flow; // increment backward edge)
    }
    return ff(source, sink, n, graph);
  }

  public static void remake_resid(int source, int sink) {
    for (int i = 0; i < n; i++) {
      if (scores[i] > 0) {
        profit_upper_bound += scores[i];
      }
      for (int j = 0; j < n; j++) {
        if (adjMatrix[i][j] != 0) {
          adjMatrix[i][j] = 30000;
        } else if (scores[i] > 0) {
          add_edge(source, i, scores[i]);
        } else if (scores[i] < 0) {
          add_edge(i, sink, -scores[i]);
        }
      }
    }
    return;
  }

  // could be optimized. only need to add backedge when you're adding backedge...?
  public static void add_edge(int i, int j, int cap) {
    adjMatrix[i][j] = cap;
    adjList.get(i).add(j);
    adjList.get(j).add(i); // add backward edge...
  }
}
