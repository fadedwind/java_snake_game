import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600; // 游戏屏幕宽度
    static final int SCREEN_HEIGHT = 600; // 游戏屏幕高度
    static final int UNIT_SIZE = 25; // 游戏单位大小
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // 游戏单位总数
    static final int DELAY = 75; // 游戏延迟时间
    final int x[] = new int[GAME_UNITS]; // 蛇的x坐标
    final int y[] = new int[GAME_UNITS]; // 蛇的y坐标
    int bodyParts = 6; // 蛇的初始长度
    int applesEaten; // 吃掉的苹果数量
    int appleX; // 苹果的x坐标
    int appleY; // 苹果的y坐标
    char direction = 'R'; // 蛇的初始方向
    boolean running = false; // 游戏是否正在运行
    Timer timer; // 游戏计时器
    Random random; // 随机数生成器

    private int goldenAppleX; // 金苹果的x坐标
    private int goldenAppleY; // 金苹果的y坐标
    private boolean goldenAppleExists = false; // 金苹果是否存在
    private long goldenAppleStartTime; // 金苹果出现的开始时间

    GamePanel(){
        random = new Random(); // 初始化随机数生成器
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT)); // 设置游戏面板大小
        this.setBackground(Color.black); // 设置游戏背景颜色
        this.setFocusable(true); // 设置游戏面板可以获取焦点
        this.addKeyListener(new MyKeyAdapter()); // 添加键盘监听器
        startGame(); // 开始游戏
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) { //绘制我们的蛇蛇

        if(running) {
            // for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            //     g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            //     g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            // }  如想要显示网格，使用这串代码

                g.setColor(Color.red);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
                if (goldenAppleExists) {
                    g.setColor(Color.yellow);
                    g.fillOval(goldenAppleX, goldenAppleY, UNIT_SIZE, UNIT_SIZE);
                }

            for(int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,190,0));
                    // g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); random color optional
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Flexure",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    
        int chance = random.nextInt(100);
        if (chance < 6 && !goldenAppleExists) {
            do {
                goldenAppleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
                goldenAppleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
            } while (goldenAppleX == appleX && goldenAppleY == appleY);
            goldenAppleExists = true;
            goldenAppleStartTime = System.currentTimeMillis();
        }
    }

    public void move() {
        for(int i = bodyParts; i>0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
        if (goldenAppleExists && System.currentTimeMillis() - goldenAppleStartTime > 7000) {
            goldenAppleExists = false;  
        }
    }

    private void playSoundEffect() {
        try {
            URL url = this.getClass().getClassLoader().getResource("split.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void checkApple() { //吃苹果检测
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            playSoundEffect();
        }
        if (goldenAppleExists && x[0] == goldenAppleX && y[0] == goldenAppleY) {
            bodyParts += 10;
            applesEaten += 10;
            goldenAppleExists = false;
            playSoundEffect();
        }
        
    }

    public void checkCollisions() {  //自身及边界碰撞检测
        // check collision: head and body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // check collision: head and left border
        if(x[0] < 0) {
            running = false;
        }

        // check collision: head and right border
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }

        // check collision: head and top border
        if(y[0] < 0) {
            running = false;
        }

        // check collision: head and bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {    //游戏结束
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Flexure",Font.BOLD, 40));
        FontMetrics metrics_score = getFontMetrics(g.getFont());
        g.drawString("score: "+applesEaten, (SCREEN_WIDTH - metrics_score.stringWidth("score: "+applesEaten))/2, g.getFont().getSize());

        //text
        g.setColor(Color.red);
        g.setFont(new Font("consolas",Font.BOLD, 75));
        FontMetrics metrics_over = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!",(SCREEN_WIDTH - metrics_over.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override 
        public void keyPressed(KeyEvent e) { // 键盘转向判断
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
