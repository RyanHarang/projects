import java.util.HashSet;

public class CanSum {

    public static void main(String args[]) {
        System.out.println(can(6, new int[] { 1, 2, 3 }));
    }

    private static boolean can(int target, int[] nums) {
        HashSet<Integer> seen = new HashSet<>();
        int diff, cur;
        for (int i = 0; i < nums.length; i++) {
            cur = nums[i];
            diff = target - cur;
            if (diff == 0) {
                return true;
            }
            if (seen.contains(diff)) {
                return true;
            }
            seen.add(cur);
        }
        return false;
    }
}