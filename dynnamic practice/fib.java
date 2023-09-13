public class fib {

    public static void main(String args[]) {
        System.out.println(fibo(0) + ", " + fibo(1) + ", " + fibo(2) + ", " + fibo(3) + ", " + fibo(4) + ", " + fibo(5)
                + ", " + fibo(6) + ", " + fibo(7) + ", " + fibo(8) + ", " + fibo(9) + ", " + fibo(10) + ", " + fibo(11)
                + ", " + fibo(12) + ", " + fibo(13) + ", " + fibo(14));
        System.out.println(fibo(60));
    }

    private static int fibo(int n) {
        if (n < 2) {
            return n;
        }
        int a = 0, b = 0, c = 1;
        for (int i = 2; i <= n; i++) {
            b = a;
            a = c;
            c = a + b;
        }
        return c;
    }

}