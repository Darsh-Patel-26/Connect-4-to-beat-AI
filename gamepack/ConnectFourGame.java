package gamepack;

public class ConnectFourGame {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private int[][] board;
    private boolean playerTurn; // true for player 1, false for player 2 or AI
    private static final int MAX_DEPTH = 6;

    public ConnectFourGame() {
        board = new int[ROWS][COLS];
        playerTurn = true;
    }

    public boolean makeMove(int col) {
        if (col < 0 || col >= COLS || board[0][col] != 0) {
            return false;
        }

        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = playerTurn ? 1 : 2;
                playerTurn = !playerTurn;
                return true;
            }
        }

        return false;
    }

    public boolean checkWin() {
        return checkWin(board);
    }

    private boolean checkWin(int[][] board) {
        // Check horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] != 0 &&
                    board[row][col] == board[row][col+1] &&
                    board[row][col] == board[row][col+2] &&
                    board[row][col] == board[row][col+3]) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != 0 &&
                    board[row][col] == board[row+1][col] &&
                    board[row][col] == board[row+2][col] &&
                    board[row][col] == board[row+3][col]) {
                    return true;
                }
            }
        }

        // Check diagonal (top-left to bottom-right)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] != 0 &&
                    board[row][col] == board[row+1][col+1] &&
                    board[row][col] == board[row+2][col+2] &&
                    board[row][col] == board[row+3][col+3]) {
                    return true;
                }
            }
        }

        // Check diagonal (top-right to bottom-left)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 3; col < COLS; col++) {
                if (board[row][col] != 0 &&
                    board[row][col] == board[row+1][col-1] &&
                    board[row][col] == board[row+2][col-2] &&
                    board[row][col] == board[row+3][col-3]) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == 0) {
                return false;
            }
        }
        return true;
    }

    public int aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == 0) {
                int row = getLowestEmptyRow(col);
                board[row][col] = 2;
                int score = minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board[row][col] = 0;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }

        makeMove(bestMove);
        return bestMove;
    }

    private int minimax(int[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || checkWin(board) || isBoardFull(board)) {
            return evaluateBoard(board);
        }

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (board[0][col] == 0) {
                    int row = getLowestEmptyRow(board, col);
                    board[row][col] = 2;
                    int score = minimax(board, depth - 1, alpha, beta, false);
                    board[row][col] = 0;
                    maxScore = Math.max(maxScore, score);
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (board[0][col] == 0) {
                    int row = getLowestEmptyRow(board, col);
                    board[row][col] = 1;
                    int score = minimax(board, depth - 1, alpha, beta, true);
                    board[row][col] = 0;
                    minScore = Math.min(minScore, score);
                    beta = Math.min(beta, score);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minScore;
        }
    }

    private int evaluateBoard(int[][] board) {
        int score = 0;
        score += evaluateLine(board, 2); // Evaluate for AI (player 2)
        score -= evaluateLine(board, 1); // Evaluate for human (player 1)
        return score;
    }

    private int evaluateLine(int[][] board, int player) {
        int score = 0;

        // Horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board[row][col], board[row][col+1], board[row][col+2], board[row][col+3], player);
            }
        }

        // Vertical
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                score += evaluateWindow(board[row][col], board[row+1][col], board[row+2][col], board[row+3][col], player);
            }
        }

        // Diagonal (top-left to bottom-right)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board[row][col], board[row+1][col+1], board[row+2][col+2], board[row+3][col+3], player);
            }
        }

        // Diagonal (top-right to bottom-left)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 3; col < COLS; col++) {
                score += evaluateWindow(board[row][col], board[row+1][col-1], board[row+2][col-2], board[row+3][col-3], player);
            }
        }

        return score;
    }

    private int evaluateWindow(int a, int b, int c, int d, int player) {
        int score = 0;
        int count = 0;
        int empty = 0;

        for (int piece : new int[]{a, b, c, d}) {
            if (piece == player) {
                count++;
            } else if (piece == 0) {
                empty++;
            }
        }

        if (count == 4) {
            score += 100;
        } else if (count == 3 && empty == 1) {
            score += 5;
        } else if (count == 2 && empty == 2) {
            score += 2;
        }

        return score;
    }

    private int getLowestEmptyRow(int col) {
        return getLowestEmptyRow(board, col);
    }

    private int getLowestEmptyRow(int[][] board, int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return -1; // Column is full
    }

    private boolean isBoardFull(int[][] board) {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == 0) {
                return false;
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }
}