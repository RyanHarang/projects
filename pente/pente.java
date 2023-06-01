import java.util.Scanner;

public class pente {
    public static void main(String args[]) {
        board board = new board();
        startGame(board);
    }

    private static void startGame(board board) {
        board.clear();
        board.setBoard(9, 9, 1);
        String inp;
        String[] put = new String[2];
        Scanner scan = new Scanner(System.in);
        while (!board.complete) {
            System.out.println("Next turn.");
            inp = scan.nextLine();
            put = inp.split(" ");
            board.update(Integer.parseInt(put[0]), Integer.parseInt(put[1]));
            board.print();
        }
        scan.close();
    }
}