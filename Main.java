import cells.CellMap;
import cells.CellType;
import cells.Sand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main
{
    static final int HEIGHT = 480 * 2;
    static final int WIDTH = 640 * 2;
    public static final int CELL_SIZE = 12; // IN PIXELS

    static final int CELL_MAP_HEIGHT = HEIGHT / CELL_SIZE;
    static final int CELL_MAP_WIDTH = WIDTH / CELL_SIZE;

    // FPS
    static final int TARGET_FPS = 60;


    public static void main(String[] args)
    {
        CellMap m = new CellMap(CELL_MAP_HEIGHT, CELL_MAP_WIDTH, CELL_SIZE);
        int TARGET_TPS = 90;
        m.setCell(new Sand(50, 40));

        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();

        JPanel renderingPanel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;

                // Paint everything black
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                m.draw(g2, this);
            }
        };

        renderingPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        renderingPanel.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX();
                int y = e.getY();

//                System.out.println(x + ", " + y);

                m.mouse = viewToCellSpace(new Point(x, y));
                m.mouse_pressed = true;

//                System.out.println("t " + mouse.x + ", " + mouse.y);

//                m.setCell(new Sand(mouse.x, mouse.y));

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                m.mouse_pressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        renderingPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                m.mouse = viewToCellSpace(new Point(e.getX(), e.getY()));
                m.placeCell();
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
          }
        );

        renderingPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}

            @Override
            public void keyPressed(KeyEvent keyEvent)
            {
                switch (keyEvent.getKeyCode())
                {
                    case KeyEvent.VK_1 -> m.cursor_type = CellType.SAND;
                    case KeyEvent.VK_2 -> m.cursor_type = CellType.WATER;
                    case KeyEvent.VK_3 -> m.cursor_type = CellType.STONE;
                    case KeyEvent.VK_4 -> m.cursor_type = CellType.FIRE;
                    case KeyEvent.VK_5 -> m.cursor_type = CellType.OIL;
                    case KeyEvent.VK_ESCAPE -> {
                        frame.dispose();
                        System.exit(1);
                    }
                    case KeyEvent.VK_C -> m.wipeMap();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });

        renderingPanel.setFocusable(true);

        pane.add(renderingPanel, BorderLayout.CENTER);

//        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        long current_time;

        long frameStartTime = System.currentTimeMillis();
        long frameEndTime;
        double frameDelta;

        long tickStartTime = System.currentTimeMillis();
        long tickEndTime;
        double tickDelta;

        long sleep_time = (long)Math.floor(1000.0 / TARGET_FPS);

        int frames = 0;
        double fps = 0;
        boolean text = true;

        while (true)
        {
            renderingPanel.repaint();
            frames++;

            current_time = System.currentTimeMillis();

            frameEndTime = current_time;
            tickEndTime = current_time;

            frameDelta = (double) frameEndTime - frameStartTime;

            if (frameDelta > 1000)
            {
                if (text)
                {
                    clearConsole();
                    text = false;
                }

                fps = frames / (frameDelta/1000);

                System.out.println("FPS: " + fps);
                text = true;
                frames = 0;
                frameStartTime = System.currentTimeMillis();
            }

            if (fps != 0)
            {
                if (fps < TARGET_FPS * 0.8 && fps > TARGET_FPS * 0.4)
                {
                    sleep_time = (long) ((sleep_time * TARGET_FPS) / fps);
                } else if (fps < TARGET_FPS * 0.4)
                {
                    sleep_time = 0;
                }
            }

            tickDelta = (double) (tickEndTime - tickStartTime);
            if (tickDelta >= (1000.0 / TARGET_TPS))
            {
                m.update();
                tickStartTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(sleep_time);
            } catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }

        }
    }

    static Point viewToCellSpace(Point p)
    {
//        System.out.println("Mouse clicked at (" + p.x + ", " + p.y + ").");
        float x_translate = (float) WIDTH / (CELL_MAP_WIDTH - 1);
        float y_translate = (float) HEIGHT / (CELL_MAP_HEIGHT - 1);

        return new Point(Math.round(p.x / x_translate), CELL_MAP_HEIGHT - Math.round(p.y / y_translate));
    }

    public static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }

            System.out.println("PROGRAM STATS:");
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
