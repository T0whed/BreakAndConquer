package BrickBreaker;

import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int TotalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballX = 120;
    private int ballY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // draw
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // scores
        g.setColor(Color.PINK);
        g.setFont(new Font("serif", Font.BOLD, 25));
        if (ballY > 570)
            g.drawString("Game Over, Scores: " + score, 190, 300);
        else
            g.drawString("" + score, 650, 30);

        // the paddle
        g.setColor(Color.BLUE);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.ORANGE);
        g.fillOval(ballX, ballY, 20, 20);

        if (TotalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.PINK);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Congrats! You Conquered!", 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
        if (ballY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.PINK);
            g.setFont(new Font("serif", Font.BOLD, 30));
            // g.drawString("Game Over, Scores: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {

            if (new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
                ballYdir = -ballYdir;

            A: for (int i = 0; i < map.map.length; ++i) {
                for (int j = 0; j < map.map[0].length; ++j) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickVAlue(0, i, j);
                            --TotalBricks;
                            score += 5;

                            if (ballX + 10 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else
                                ballYdir = -ballYdir;
                            break A;
                        }

                    }
                }
            }
            ballX += ballXdir;
            ballY += ballYdir;
            if (ballX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600)
                playerX = 600;
            else
                moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10)
                playerX = 10;
            else
                moveLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballX = 120;
                ballY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                TotalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

}
