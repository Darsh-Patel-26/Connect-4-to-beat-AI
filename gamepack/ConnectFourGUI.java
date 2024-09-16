package gamepack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConnectFourGUI extends JFrame {
    private ConnectFourGame game;
    private JButton[] dropButtons;
    private JPanel boardPanel;
    private JLabel statusLabel;

    public ConnectFourGUI() {
        game = new ConnectFourGame();
        setTitle("Connect Four");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create drop buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, ConnectFourGame.COLS));
        dropButtons = new JButton[ConnectFourGame.COLS];
        for (int i = 0; i < ConnectFourGame.COLS; i++) {
            final int col = i;
            dropButtons[i] = new JButton("Drop");
            dropButtons[i].addActionListener(e -> makeMove(col));
            buttonPanel.add(dropButtons[i]);
        }
        add(buttonPanel, BorderLayout.NORTH);

        // Create board panel
        boardPanel = new JPanel(new GridLayout(ConnectFourGame.ROWS, ConnectFourGame.COLS));
        for (int i = 0; i < ConnectFourGame.ROWS * ConnectFourGame.COLS; i++) {
            boardPanel.add(new JPanel());
        }
        add(boardPanel, BorderLayout.CENTER);

        // Create status label
        statusLabel = new JLabel("Player 1's turn");
        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateBoard();
    }

    private void makeMove(int col) {
        if (game.makeMove(col)) {
            updateBoard();
            if (game.checkWin()) {
                JOptionPane.showMessageDialog(this, (game.isPlayerTurn() ? "Player 2" : "Player 1") + " wins!");
                resetGame();
            } else if (game.isBoardFull()) {
                JOptionPane.showMessageDialog(this, "It's a draw!");
                resetGame();
            } else if (!game.isPlayerTurn()) {
                // AI's turn
                int aiCol = game.aiMove();
                updateBoard();
                if (game.checkWin()) {
                    JOptionPane.showMessageDialog(this, "AI wins!");
                    resetGame();
                } else if (game.isBoardFull()) {
                    JOptionPane.showMessageDialog(this, "It's a draw!");
                    resetGame();
                }
            }
        }
    }

    private void updateBoard() {
        int[][] board = game.getBoard();
        for (int row = 0; row < ConnectFourGame.ROWS; row++) {
            for (int col = 0; col < ConnectFourGame.COLS; col++) {
                JPanel cell = (JPanel) boardPanel.getComponent(row * ConnectFourGame.COLS + col);
                cell.removeAll();
                if (board[row][col] != 0) {
                    JPanel disc = new JPanel();
                    disc.setPreferredSize(new Dimension(50, 50));
                    disc.setBackground(board[row][col] == 1 ? Color.RED : Color.YELLOW);
                    cell.add(disc);
                }
                cell.revalidate();
                cell.repaint();
            }
        }
        statusLabel.setText(game.isPlayerTurn() ? "Player 1's turn" : "AI's turn");
    }

    private void resetGame() {
        game = new ConnectFourGame();
        updateBoard();
    }
}