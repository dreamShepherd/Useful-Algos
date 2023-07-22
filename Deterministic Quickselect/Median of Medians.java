import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.Arrays;

class kSmallest {
  public static void main(String[] args) throws IOException {
    /* Parsing Begin */
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // start parsing
    int k = Integer.parseInt(br.readLine());
    int[] nums = spl(br.readLine(), 1);
    /* Parsing End */
    int res = kthSmallest(nums, 0, nums.length, nums.length - k + 1);
    /* Outputting Begin */
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));// write output
    out.write(res); // output is the max profit with no constraints-maxflow
    out.write("\n");
    out.flush();// end write output
    /* Outputting End */
  }

  public static int getPivot(int[] nums, int start, int end) {
    int[] medians = new int[(end - start) / 5];
    int i = start;
    int k = 0;
    for (int j = start + 5; j < end; i = j, j += 5) {
      Arrays.sort(nums, i, j);
      medians[k++] = nums[i + 2];
    }
    // median of medians.
    return kthSmallest(medians, 0, medians.length, medians.length / 2 + 1);
  }

  public static int arrangeByPivot(int[] nums, int start, int end, int pivot) {
    int b = start;
    int e = end - 1;
    for (int i = start; i <= e;) {
      if (nums[i] < pivot) {
        int temp = nums[i];
        nums[i] = nums[b];
        nums[b] = temp;
        b++;
        i++;
      } else if (nums[i] == pivot) {
        i++;
      } else {
        int temp = nums[i];
        nums[i] = nums[e];
        nums[e] = temp;
        e--;
      }
    }

    return b;
  }

  public static int kthSmallest(int[] nums, int start, int end, int elm) {
    if (end - start <= 5) {
      Arrays.sort(nums, start, end);
      return nums[start + elm - 1];
    }

    int pivot = getPivot(nums, start, end);
    int loc = arrangeByPivot(nums, start, end, pivot);

    if (loc - start == elm - 1) {
      return nums[loc];
    } else if (loc - start > elm - 1) {
      return kthSmallest(nums, start, loc, elm);
    }

    return kthSmallest(nums, loc + 1, end, (elm - (loc + 1 - start)));
  }

  public static int findKthLargest(int[] nums, int k) {
    return kthSmallest(nums, 0, nums.length, nums.length - k + 1);
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

}