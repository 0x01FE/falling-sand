import sand.CellMap;
import sand.CellType;
import sand.Sand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main
{
    static final int HEIGHT = 480 * 2;
    static final int WIDTH = 640 * 2;
    public static final int CELL_SIZE = 3; // IN PIXELS

    static final int CELL_MAP_HEIGHT = HEIGHT / CELL_SIZE;
    static final int CELL_MAP_WIDTH = WIDTH / CELL_SIZE;

//    static final int


    public static void main(String[] args)
    {
        CellMap m = new CellMap(CELL_MAP_HEIGHT, CELL_MAP_WIDTH, CELL_SIZE);
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

        while (true)
        {
            renderingPanel.repaint();

            m.update();
            // m.print();

            try {
                Thread.sleep(30);
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

        Point ans = new Point(Math.round(p.x / x_translate), CELL_MAP_HEIGHT - Math.round(p.y / y_translate));

//        System.out.println("Translated Point (" + ans.x + ", " + ans.y + ")");

        return ans;
    }

}
