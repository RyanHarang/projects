public class board {
    
    int[][] board = new int[19][19];
    //variable to track active player
    Integer activePlayer = -1;
    //variables to track the number of captures each player has made
    Integer p1Cap = 0;
    Integer p2Cap = 0;
    //variable to track if the game is over
    boolean complete = false;

    public int[][] getBoard() {
        return this.board;
    }

    public void setBoard(int row, int col, int val) {
        board[row][col] = val;
    }

    //method that resests the board to all 0s
    public void clear() {
        for (int row = 0; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                board[row][col] = 0;
            }
        }
    }

    //method to add strones to the board
    public void update(int row, int col) {
        //if the given numbers are within the board
        if (-1 < row && row < 19 && -1 < col && col < 19) {
            //if chosen spot is unoccupied
            if (board[row][col] == 0) {
                //place a stone of active player
                board[row][col] = activePlayer;
                //switch active player
                captures(row, col);
                complete = win(row, col);
                activePlayer *= -1;
            }
            //when players try to put stones in taken spots
            else {
                System.out.println(row + " " + col + " is already taken!");
            }
        }
        else {
            System.out.println("Not within the board.");
        }
    }

    //method that will check for and initiate captures
    private void captures(int row, int col) {        
        //vertical up
        if ((row - 3) > -1) {
            if ((board[row - 3][col] == activePlayer) && (board[row - 2][col] == activePlayer * -1) && 
            (board[row - 1][col] == activePlayer * -1)) {
                board[row - 2][col] = 0;
                board[row - 1][col] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }
        
        //vertical down
        if ((row + 3) < 19) {
            if ((board[row + 3][col] == activePlayer) && (board[row + 2][col] == activePlayer * -1) && (board[row + 1][col] == activePlayer * -1)) {
                board[row + 2][col] = 0;
                board[row + 1][col] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }

        //horizontal left
        if ((col - 3) > -1) {
            if ((board[row][col - 3] == activePlayer) && (board[row][col - 2] == activePlayer * -1) && (board[row][col - 1] == activePlayer * -1)) {
                board[row][col - 2] = 0;
                board[row][col - 1] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }
        
        //horizontal right
        if ((col + 3) < 19) {
            if ((board[row][col + 3] == activePlayer) && (board[row][col + 2] == activePlayer * -1) &&
            (board[row][col + 1] == activePlayer * -1)) {
                board[row][col + 2] = 0;
                board[row][col + 1] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }

        //diagonal left up
        if ((row - 3) > -1 && (col - 3) > -1) {
            if ((board[row - 3][col - 3] == activePlayer) && (board[row - 2][col - 2] == activePlayer * -1) && 
            (board[row - 1][col - 1] == activePlayer * -1)) {
                board[row - 2][col - 2] = 0;
                board[row - 1][col - 1] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }

        //diagonal right down
        if ((row + 3) < 19 && (col + 3) < 19) {
            if ((board[row + 3][col + 3] == activePlayer) && (board[row + 2][col + 2] == activePlayer * -1) && 
            (board[row + 1][col + 1] == activePlayer * -1)) {
                board[row + 2][col + 2] = 0;
                board[row + 1][col + 1] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }

        //diagonal right up
        if ((row - 3) > -1 && (col + 3) < 19) {
            if ((board[row - 3][col + 3] == activePlayer) && (board[row - 2][col + 2] == activePlayer * -1) && 
            (board[row - 1][col + 1] == activePlayer * -1)) {
                board[row - 2][col + 2] = 0;
                board[row - 1][col + 1] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }

        //diagonal left down
        if((row + 3) < 19 && (col - 3) > -1) {
            if ((board[row + 3][col - 3] == activePlayer) && (board[row + 2][col - 2] == activePlayer * -1) && 
            (board[row + 1][col - 1] == activePlayer * -1)) {
                board[row + 2][col - 2] = 0;
                board[row + 1][col - 1] = 0;
                if (activePlayer == 1) {
                    p1Cap++;
                }
                else {
                    p2Cap++;
                }
            }
        }
    }

    //method that will check if active player has 5 in a row anywhere
    //or that a player has 5 captures
    private boolean win(int row, int col) {
        //checks for five-in-a-row in all directions and possibilities
        //diagonal, vertical, horizontal, and up to 4 stones in each direction
        //false if game still going
        //true if someone wins with a move
            if (p1Cap > 4 || p2Cap > 4) {
                return true;
            }
            if (five(row, col)) {
                return true;
            }
        return false;
    }
    private boolean five(int row, int col) {
        int num, vert, hori;

        //check for 5-in-a-row horizontal
        for (int i = 4; i > -1; i--) {
            num = 0;
            for (int j = 0; j < 5; j++) {
                hori = col - i + j;
                if (hori > -1 && hori < 19) {
                    if (board[row][hori] == activePlayer) {
                        num++;
                    }
                }
            }
            if (num == 5) {
                return true;
            }
        }
        
        //check for 5-in-a-row vertical
        for (int i = 4; i > -1; i--) {
            num = 0;
            for (int j = 0; j < 5; j++) {
                vert = row - i + j;
                if (vert > -1 && vert < 19) {
                    if (board[vert][col] == activePlayer) {
                        num++;
                    }
                }
            }
            if (num == 5) {
                return true;
            }
        }
        //check for 5-in-a-row diagonal: \
        for (int i = 4; i > -1; i--) {
            num = 0;
            for (int j = 0; j < 5; j++) {
                vert = row - i + j;
                hori = col - i + j;
                if (vert > -1 && vert < 19 && hori > -1 && hori < 19) {
                    if (board[vert][hori] == activePlayer) {
                        num++;
                    }
                }
            }
            if (num == 5) {
                return true;
            }
        }
        
        //check for diagonal: /
        for (int i = 4; i > -1; i--) {
            num = 0;
            for (int j = 0; j < 5; j++) {
                vert = row + i - j;
                hori = col - i + j;
                if (vert > -1 && vert < 19 && hori > -1 && hori < 19) {
                    if (board[vert][hori] == activePlayer) {
                        num++;
                    }
                }
            }
            if (num == 5) {
                return true;
            }
        }
        return false;
    }

    //method that prints board 
    public void print() {
        for (int row = 0; row < 19; row++) {
            for (int col = 0; col < 19; col++) {
                System.out.print("|" + board[row][col] + "|");
            }
            System.out.println();
        }
    }
}