import java.util.HashMap;

public class CanSum {

    public static void main(String args[]) {
        System.out.println(can(6, new int[] { 4, 5 }));
    }

    private static boolean can(int target, int[] nums) {
        HashMap<Integer, Boolean> memo = new HashMap<>();
        return can(target, nums, memo);
    }

    private static boolean can(int target, int[] nums, HashMap<Integer, Boolean> memo) {
        if (memo.containsKey(target)) {
            return memo.get(target);
        }
        if (target == 0) {
            return true;
        }
        if (target < 0) {
            return false;
        }
        int rem;
        for (int num : nums) {
            rem = target - num;
            if (can(rem, nums, memo)) {
                memo.put(target, true);
                return true;
            }
        }
        memo.put(target, false);
        return false;
    }
}