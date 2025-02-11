import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private static final int SIZE = 5; // Size of the board
    private static final int MINES = 3; // Number of mines
    private char[][] board;
    private boolean[][] revealed;
    private boolean[][] flagged; // Array to track flagged cells
    private boolean gameOver;

    public Minesweeper() {
        board = new char[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE]; // Initialize flagged array
        gameOver = false;
        initializeBoard();
        placeMines();
        calculateHints();
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = '0';
            }
        }
    }

    private void placeMines() {
        Random rand = new Random();
        int minesPlaced = 0;
        while (minesPlaced < MINES) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (board[row][col] != 'M') {
                board[row][col] = 'M';
                minesPlaced++;
            }
        }
    }

    private void calculateHints() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 'M') {
                    int mineCount = 0;
                    for (int r = row - 1; r <= row + 1; r++) {
                        for (int c = col - 1; c <= col + 1; c++) {
                            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == 'M') {
                                mineCount++;
                            }
                        }
                    }
                    board[row][col] = (char) ('0' + mineCount);
                }
            }
        }
    }

    public void reveal(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col] || flagged[row][col]) {
            return;
        }
        revealed[row][col] = true;

        if (board[row][col] == 'M') {
            gameOver = true;
            System.out.println("Game Over! You hit a mine.");
            revealAllMines(); // Reveal all mines when a mine is hit
        } else if (board[row][col] == '0') {
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    reveal(r, c);
                }
            }
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 'M') {
                    revealed[row][col] = true; // Reveal all mines
                }
            }
        }
    }

    public void flag(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return; // Can't flag revealed cells
        }
        flagged[row][col] = true; // Set flag
    }

    public void unflag(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return; // Can't unflag revealed cells
        }
        flagged[row][col] = false; // Remove flag
    }

    public void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int row = 0; row < SIZE; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < SIZE; col++) {
                if (revealed[row][col]) {
                    System.out.print(board[row][col] + " ");
                } else if (flagged[row][col]) {
                    System.out.print("F "); // Show flag
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 'M' && !revealed[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Minesweeper game = new Minesweeper();
        Scanner scanner = new Scanner(System.in);

        while (!game.isGameOver() && !game.isGameWon()) {
            game.printBoard();
            System.out.print("Enter 'r' to reveal, 'f' to flag, or 'u' to unflag, followed by row and column (e.g., r 1 2, f 1 2, or u 1 2): ");
            String action = scanner.next();
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                if (action.equals("r")) {
                    game.reveal(row, col);
                } else if (action.equals("f")) {
                    game.flag(row, col);
                } else if (action.equals("u")) {
                    game.unflag(row, col);
                } else {
                    System.out.println("Invalid action. Please enter 'r' to reveal, 'f' to flag, or 'u' to unflag.");
                }
            } else {
                System.out.println("Invalid input. Please enter values within the range 0 to " + (SIZE - 1) + ".");
            }
        }

        scanner.close();
        game.printBoard(); // Show the final board with all mines revealed

        if (game.isGameWon()) {
            System.out.println("Congratulations! You've won the game!");
        } else {
            System.out.println("Game Over! You hit a mine.");
        }
    }
}