import sand.CellMap;
import sand.CellType;
import sand.Sand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main
{
    static final int HEIGHT = 480;
    static final int WIDTH = 620;

    static final int CELL_MAP_HEIGHT = 100;//HEIGHT / 2;
    static final int CELL_MAP_WIDTH = 100;//WIDTH / 2;

    static final int


    public static void main(String[] args)
    {
        CellMap m = new CellMap(CELL_MAP_HEIGHT, CELL_MAP_WIDTH);
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
            public void keyTyped(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_1 -> m.cursor_type = CellType.SAND;
                    case KeyEvent.VK_2 -> m.cursor_type = CellType.WATER;
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {}

            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });

        renderingPanel.setFocusable(true);

        pane.add(renderingPanel, BorderLayout.CENTER);

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        float x_translate = (float) WIDTH / CELL_MAP_WIDTH;
        float y_translate = (float) HEIGHT / CELL_MAP_HEIGHT;

        return new Point(Math.round(p.x / x_translate), CELL_MAP_HEIGHT - Math.round(p.y / y_translate));
    }

}
