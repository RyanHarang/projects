import java.util.ArrayList;
import java.util.HashMap;

public class BestSum {
    public static void main(String args[]) {
        System.out.println(best(8, new int[] { 4, 1, 5, 6 }).toString());
        System.out.println(best(600, new int[] { 9, 50, 1 }).toString());
        System.out.println(best(7, new int[] { 3, 1, 5, 2 }).toString());
    }

    private static ArrayList<Integer> best(int target, int[] nums) {
        HashMap<Integer, ArrayList<Integer>> memo = new HashMap<>();
        return best(target, nums, memo);
    }

    private static ArrayList<Integer> best(int target, int[] nums, HashMap<Integer, ArrayList<Integer>> memo) {
        if (memo.containsKey(target)) {
            return memo.get(target);
        }
        if (target == 0) {
            return new ArrayList<Integer>();
        }
        if (target < 0) {
            return null;
        }
        int rem;
        ArrayList<Integer> res, shortest = null;
        for (int num : nums) {
            rem = target - num;
            res = best(rem, nums, memo);
            if (res != null) {
                ArrayList<Integer> combination = new ArrayList<>(res);
                combination.add(num);
                if (shortest == null || shortest.size() > combination.size()) {
                    shortest = combination;
                }
            }
        }
        memo.put(target, shortest);
        return shortest;
    }
}
