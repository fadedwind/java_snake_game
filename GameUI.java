import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI {
    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameUI window = new GameUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GameUI() {
        initialize();
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        JButton btnStartGame = new JButton("Start Game");
        btnStartGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GameFrame();
                // Start game logic here
            }
        });
        frame.getContentPane().add(btnStartGame);

        JButton btnGameInstructions = new JButton("Game Instructions");
        btnGameInstructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show game instructions here
                String gameInstructions = "Game Instructions: This is a snake game! Use \"↑ ↓ ← →\" to move! Earn as much as score possible! 这是一个贪吃蛇游戏！使用\"↑ ↓ ← →\" 来移动！尽可能赚取更多的分数吧！";
                JOptionPane.showMessageDialog(null, gameInstructions);
            }
        });
        frame.getContentPane().add(btnGameInstructions);

        JButton btnExitGame = new JButton("Exit Game");
        btnExitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(btnExitGame);
    }
}