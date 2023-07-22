import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int n = Integer.parseInt(br.readLine());
    int[] rings = spl(br.readLine(), n);
    int[][] dp = new int[n + 2][n + 2];
    for (int i = n + 1; i >= 0; i--) {
      for (int j = i; j < n + 2; j++) {
        for (int k = i + 1; k < j; k++) {
          if (j == n + 1 && i == 0) {
            dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + rings[k - 1]);
          } else if (j == n + 1) {
            dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + rings[i - 1] * rings[k - 1]);
          } else if (i == 0) {
            dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + rings[k - 1] * rings[j - 1]);
          } else {
            dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + rings[i - 1] * rings[k - 1] * rings[j - 1]);
          }
        }
      }
    }
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
    out.write(Integer.toString(dp[0][n + 1]));
    out.write('\n');
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
}
