import java.util.HashMap;
import java.util.ArrayList;

public class HowSum {
    public static void main(String args[]) {
        System.out.println(how(6, new int[] { 4, 1 }).toString());
        System.out.println(how(600, new int[] { 9, 50 }).toString());
    }

    private static ArrayList<Integer> how(int target, int[] nums) {
        HashMap<Integer, ArrayList<Integer>> memo = new HashMap<>();
        return how(target, nums, memo);
    }

    private static ArrayList<Integer> how(int target, int[] nums, HashMap<Integer, ArrayList<Integer>> memo) {
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
        ArrayList<Integer> res;
        for (int num : nums) {
            rem = target - num;
            res = how(rem, nums, memo);
            if (res != null) {
                res.add(num);
                memo.put(target, res);
                return memo.get(target);
            }
        }
        memo.put(target, null);
        return null;
    }
}
