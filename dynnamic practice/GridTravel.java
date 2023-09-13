import java.util.HashMap;

public class GridTravel {
    // In how many ways can you traverse a grid with n * m dimensions if you start
    // in the top left and end in the bottom right?
    // Assume you can only move right and down
    public static void main(String args[]) {
        System.out.println(calcPaths(15, 15));
    }

    private static int calcPaths(int n, int m) {
        HashMap<String, Integer> memo = new HashMap<>();
        return calcPaths(n, m, memo);
    }

    private static int calcPaths(int n, int m, HashMap<String, Integer> memo) {
        if (n == 0 || m == 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        String key = n + "," + m;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        memo.put(key, calcPaths(n - 1, m, memo) + calcPaths(n, m - 1, memo));
        return memo.get(key);
    }
}