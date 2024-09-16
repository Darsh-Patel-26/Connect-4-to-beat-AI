// Main.java
import javax.swing.*;
import gamepack.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConnectFourGUI game = new ConnectFourGUI();
            game.setVisible(true);
        });
    }
}
