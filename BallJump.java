import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class BallJump implements ActionListener, MouseListener, KeyListener
{
    public static BallJump ballJump;
    public final int WIDTH = 800, HEIGHT = 800;
    public Refresh refresh;
    public Rectangle ball;
    public ArrayList<Rectangle> columns;
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    public Random rand;
    int bg1 = 0;// 0
    int bl1 = 0;// 0
    int bs1 = 255;// 255
    int t1 = 255;// 255
    int br1 = 255;// 255
    int bg = 0;// 0
    int bl = 255;// 255
    int bs = 0;// 0
    int t = 255;// 255
    int br = 0;// 0
    int bg2 = 0; // 0
    int t2 = 255;// 255
    int br2 = 255;// 255

    public BallJump()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(100, this);
        refresh = new Refresh();
        rand = new Random();
        jframe.add(refresh);
        jframe.setTitle("Ball Jump");     
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);
        ball = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20); 
        columns = new ArrayList<Rectangle>();
        addColumn(true);     
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();  
    }

    public void addColumn(boolean start)
    {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);
        if (start)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space)); 
        }
        else
        {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));   
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column)
    {
        Color c1 = new Color(bg1,bg,bg2);
        Color c2 = new Color(br1,br,br2);
        g.setColor(c1);    
        g.fillRect(column.x, column.y, column.width, column.height);
        g.setColor(c2);
        g.fillRoundRect(column.x, column.y, column.width, column.height,20,20);
    }

    public void jump()
    {
        if (gameOver)
        {
            ball = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);    
            columns.clear();  
            yMotion = 0;    
            score = 0;
            addColumn(true);
            addColumn(true);
            addColumn(true); 
            addColumn(true);
            gameOver = false; 
        }

        if (!started)
        {
            started = true;
        }
        else if (!gameOver)
        {
            if (yMotion > 0)
            {
                yMotion = 0;
            }
            yMotion -= 10;    
        }
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        int speed = 10;
        ticks++;
        if (started)
        {
            for (int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);
                column.x -= speed; 
            }

            if (ticks % 2 == 0 && yMotion < 15)
            {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0)
                {
                    columns.remove(column);
                    if (column.y == 0)
                    {
                        addColumn(false); 
                    }
                }
            }

            ball.y += yMotion;
            for (Rectangle column : columns)
            {
                if (column.y == 0 && ball.x + ball.width / 2 > column.x + column.width / 2 - 10 && ball.x + ball.width / 2 < column.x + column.width / 2 + 10)
                {
                    score++;     
                }

                if (column.intersects(ball))
                {
                    gameOver = true;
                    if (ball.x <= column.x)
                    {
                        ball.x = column.x - ball.width;
                    }
                    else   
                    {
                        if (column.y != 0)
                        {
                            ball.y = column.y - ball.height; 
                        }
                        else if (ball.y < column.height)
                        {
                            ball.y = column.height;
                        }
                    }
                }
            }
            if (ball.y > HEIGHT - 120 || ball.y < 0)
            {
                gameOver = true;    
            }
            if (ball.y + yMotion >= HEIGHT - 120)
            {
                ball.y = HEIGHT - 120 - ball.height;
                gameOver = true;    
            }
        }
        refresh.repaint();  
    }

    public void repaint(Graphics g)
    {
        Color c1 = new Color(bg1,bg,0);
        Color c2 = new Color(bs1,bs,0);
        Color c3 = new Color(bl1,bl,0);
        Color c4 = new Color(t1,t,t2);
        g.setColor(c1);// Background
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(c2);// Base
        g.fillRect(0, HEIGHT - 120, WIDTH, 140);
        g.setColor(c1);// Ball
        g.fillRect(ball.x, ball.y, ball.width, ball.height);
        g.setColor(c3);// Ball
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
        for (Rectangle column : columns)
        {
            paintColumn(g,column); 
        }
        g.setColor(c4); 
        g.setFont(new Font("DIALOG",1,100));
        if (!started)
        {
            g.drawString("CLICK HERE", 75, HEIGHT / 2 - 50);     
        }
        if (gameOver)
        {
            g.drawString("GAME OVER", 100, HEIGHT / 2 - 50);    
        }
        if (!gameOver && started)
        {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100); 
        }
    }

    public static void main(String[] args)
    {
        ballJump = new BallJump();  
    }

    @Override    public void mouseClicked(MouseEvent e)
    {
        jump();    
    }

    @Override    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            jump();
        }
    }

    @Override    public void mousePressed(MouseEvent e)
    {
    }

    @Override    public void mouseReleased(MouseEvent e)
    {
    }

    @Override    public void mouseEntered(MouseEvent e)
    {
    }

    @Override    public void mouseExited(MouseEvent e)
    {
    }

    @Override    public void keyTyped(KeyEvent e)
    {
    }

    @Override    public void keyPressed(KeyEvent e)
    {
    }
}
