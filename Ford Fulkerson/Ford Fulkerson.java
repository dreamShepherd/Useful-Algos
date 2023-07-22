import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;

class ford {
  static int n;
  static int m;
  static int[] scores;
  static int maxflow;
  static int[][] adjMatrix;
  static int profit_upper_bound = 0; // sum of all positive profits

  public static void main(String[] args) throws IOException {
    /* Parsing Begin */
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // start parsing
    int[] nm = spl(br.readLine(), 2);
    m = nm[1];
    n = nm[0];
    scores = spl(br.readLine(), nm[0]);
    adjMatrix = new int[n + 2][n + 2];
    for (int i = 0; i < m; i++) {
      int[] e = spl(br.readLine(), 3);
      adjMatrix[e[0] - 1][e[1] - 1] = e[2]; // end parsing
    }
    scores[n - 1] = -30000; // set sink profit to small number (at least >20000), so algo never picks it.
    /* Parsing End */
    /*
     * Begin Algorithm Logic
     */
    adjMatrix = ff(0, n - 1, n, adjMatrix); // Run ford fulk on input. return residual graph
    maxflow = 0; // reset max flow
    adjMatrix = remake_resid(n, n + 1, adjMatrix); // Reduce and convert residual graph to project selection
    adjMatrix = ff(n, n + 1, n + 2, adjMatrix); // run ford fulkerson on new input. result is raw max profit - maxflow
    /*
     * End Algorithm Logic
     */
    /* Outputting Begin */
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));// write output
    out.write(Integer.toString(profit_upper_bound - maxflow)); // output is the max profit with no constraints-maxflow
    out.write("\n");
    out.flush();// end write output
    /* Outputting End */
  }

  /* Function for parsing. Converts lines to int arrays. */
  public static int[] spl(String s, int runs) { // function for parsing input. converts lines to integer arrays
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

  /*
   * Runs Ford Fulkerson
   * - Returns residual graph
   * - Sets static maxflow variable to max flow of that graph
   */
  public static int[][] ff(int source, int sink, int n, int[][] graph) {
    int[][] path = new int[n][2]; // path is the augmenting path we are building
    boolean[] visited = new boolean[n];
    int curr = 0; // curr the head of the stack representing our augmenting path
    visited[source] = true;
    path[curr][0] = source;
    path[curr][1] = 30000; // arbirarily large flow at source. At least 20k by problem constraints.
    while (path[curr][0] != sink) { // conduct dfs
      boolean fail = true;
      for (int i = 0; i < n; i++) {
        if (graph[path[curr][0]][i] > 0 && !visited[i]) { // add a new vertex to augmenting path
          curr++;
          path[curr][0] = i;
          path[curr][1] = Math.min(path[curr - 1][1], graph[path[curr - 1][0]][path[curr][0]]); // new flow=min(new,old)
          visited[i] = true;
          fail = false;
          break; //
        }
      }
      if (fail) { // If no unexplored nodes, search fails...
        if (curr == 0)
          return graph; // search fails at source. no more augmenting paths. return final residual graph
        else {
          curr--; // backtrack to the previous node on the path, try again
        }
      }
    }
    // unravel the augmenting path, changing the residual graph to reflect this path
    int flow = path[curr][1]; // flow of the augmenting path
    maxflow += flow; // add augmenting flow to maxflow
    for (; curr > 0; curr--) {
      graph[path[curr - 1][0]][path[curr][0]] -= flow; // decrement forward edge
      graph[path[curr][0]][path[curr - 1][0]] += flow; // increment backward edge
    }
    return ff(source, sink, n, graph); // recurse and try finding another augmenting path... (tail recursively?)..
  }

  /*
   * Generate new flow graph from residual graph
   * Following the project selection requirements
   * i.e.
   * -Make new source/sink. (I use indices n and n+1 for this)
   * -For all positive profits, add edge from source to that vertex
   * -For all negative profits, add edge from that vertex to sink
   * -All pre-existing edges have infinite capacity (30000)
   */
  public static int[][] remake_resid(int source, int sink, int[][] adjMatrix) {
    for (int i = 0; i < n; i++) {
      if (scores[i] > 0) {
        profit_upper_bound += scores[i]; // profit_upper_bound is sum of all positive profits
      }
      for (int j = 0; j < n; j++) {
        if (adjMatrix[i][j] != 0) {
          adjMatrix[i][j] = 30000;
        } else if (scores[i] > 0) {
          adjMatrix[source][i] = scores[i];
        } else if (scores[i] < 0) {
          adjMatrix[i][sink] = -scores[i];
        }
      }
    }
    adjMatrix[source][0] = 30000; // so that we always pick the original source vertex.
    return adjMatrix;
  }
}