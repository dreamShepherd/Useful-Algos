import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Stack;

class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String[] mn = spl(br.readLine(), 2);
    int m = Integer.parseInt(mn[0]);
    int n = Integer.parseInt(mn[1]);
    int[] capacity = new int[m];
    int[][] hprefs = new int[m][n];
    int[][] sprefs = new int[n][m];
    Stack<Integer> stck = new Stack<Integer>();
    String[] rankingstr;
    for (int i = 0; i < m; i++) {
      capacity[i] = Integer.parseInt(br.readLine());
      stck.add(i);
    }
    for (int i = 0; i < m; i++) {
      rankingstr = spl(br.readLine(), n);
      for (int j = 0; j < n; j++) {
        hprefs[i][j] = Integer.parseInt(rankingstr[j]) - 1;
      }
    }
    for (int i = 0; i < n; i++) {
      rankingstr = spl(br.readLine(), m);
      for (int j = 0; j < m; j++) {
        sprefs[i][Integer.parseInt(rankingstr[j]) - 1] = j;
      }
    }
    int[] s_match = new int[n];
    int[] proposals_sent = new int[m];
    Arrays.fill(s_match, -1);

    while (!stck.empty()) {
      int h = stck.pop();
      while (capacity[h] != 0) {
        int s = hprefs[h][proposals_sent[h]];
        proposals_sent[h] += 1;
        if (s_match[s] != -1) {
          if (sprefs[s][h] < sprefs[s][s_match[s]]) {
            capacity[h] -= 1;
            capacity[s_match[s]] += 1;
            stck.add(s_match[s]);
            s_match[s] = h;
          }
        } else {
          capacity[h] -= 1;
          s_match[s] = h;
        }
      }
    }

    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
    for (int i = 0; i < s_match.length; i++) {
      out.write(Integer.toString(1 + s_match[i]) + "\n");
    }
    out.flush();
  }

  public static String[] spl(String s, int runs) {
    String[] res = new String[runs];
    int start = 0;
    int end;
    for (int i = 0; i < runs - 1; i++) {
      end = s.indexOf(' ', start);
      res[i] = (s.substring(start, end));
      start = end + 1;
    }
    res[runs - 1] = s.substring(start);
    return res;
  }
}
