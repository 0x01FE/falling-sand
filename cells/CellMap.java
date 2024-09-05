package cells;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CellMap
{
    public boolean PRESSURE_DEBUG = true;

    public int height;
    public int width;

    private Cell[][] map;
    public Cell[][] buffer;

    public Point mouse;
    public boolean mouse_pressed;
    public CellType cursor_type;

    public int cell_size;

    public CellMap(int h, int w, int cell_size)
    {
        this.height = h;
        this.width = w;
        this.cell_size = cell_size;

        this.map = new Cell[h][w];
        this.buffer = new Cell[h][w];

        this.mouse_pressed = false;
        this.cursor_type = CellType.SAND;

        wipeMap();
    }

    public void wipeMap()
    {
        int y = 0;
        for (Cell[] row : this.map)
        {
            int x = 0;
            for (Cell cell : row)
            {
                setCell(new Air(x, y));
                x++;
            }
            y++;
        }
    }

    public Cell getCell(int x, int y)
    {
        return this.map[y][x];
    }

    public void wipeCell(Cell c)
    {
        this.buffer[c.y][c.x] = new Air(c.x, c.y);
    }

    public void setCell(Cell c)
    {
        if (c.x >= this.width || c.y >= this.height || c.y < 0 || c.x < 0)
            return;

        this.buffer[c.y][c.x] = c;
    }

    public void swapCells(Cell c1, Cell c2)
    {
        if (c1.locked || c2.locked)
            return;

        int temp_x = c1.x;
        int temp_y = c1.y;

        c1.x = c2.x;
        c1.y = c2.y;

        c2.x = temp_x;
        c2.y = temp_y;

        setCell(c1);
        setCell(c2);
    }

    public void print()
    {
        System.out.println("Printing Map...");
        for (Cell[] row : this.map)
        {
            for (Cell c : row)
            {
                if (c == null)
                    System.out.print(" ");
                else if (c.type == CellType.SAND)
                    System.out.print("1");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public void placeCell()
    {
        if (this.cursor_type == CellType.SAND)
            this.setCell(new Sand(this.mouse.x, this.mouse.y));
        else if (this.cursor_type == CellType.WATER)
            this.setCell(new Water(this.mouse.x, this.mouse.y));
        else if (this.cursor_type == CellType.STONE)
            this.setCell(new Stone(this.mouse.x, this.mouse.y));
        else if (this.cursor_type == CellType.FIRE)
            this.setCell(new Fire(this.mouse.x, this.mouse.y));
        else if (this.cursor_type == CellType.OIL)
            this.setCell(new Oil(this.mouse.x, this.mouse.y));
    }

    public ArrayList<Cell> getNeighbors(Cell c)
    {
        boolean negative_y = c.y - 1 >= 0;
        boolean positive_y = c.y + 1 < this.height;

        ArrayList<Cell> cells = new ArrayList<Cell>();

        // x checks
        int x = c.x;

        if (negative_y)
            cells.add(this.map[c.y - 1][x]);

        if (positive_y)
            cells.add(this.map[c.y + 1][x]);

        // x - 1 checks
        if (c.x - 1 >= 0)
        {
            x = c.x - 1;

            if (negative_y)
                cells.add(this.map[c.y - 1][x]);

            cells.add(this.map[c.y][x]);

            if (positive_y)
                cells.add(this.map[c.y + 1][x]);
        }

        // x + 1 checks
        if (c.x + 1 < this.width)
        {
            x = c.x + 1;

            if (negative_y)
                cells.add(this.map[c.y - 1][x]);

            cells.add(this.map[c.y][x]);

            if (positive_y)
                cells.add(this.map[c.y + 1][x]);
        }

        return cells;
    }

    public void update()
    {
        if (this.mouse_pressed)
            this.placeCell();
//        System.out.println("Updating map...");
        for (Cell[] row : this.map)
        {
            for (Cell cell : row)
            {
                if (cell == null)
                    continue;

                cell.update(this);
            }
        }

        this.map = this.buffer;
    }

    public Cell getUnder(Cell c)
    {
        return this.map[c.y - 1][c.x];
    }

    public void draw(Graphics g2, JPanel renderingPanel, boolean debug_mode)
    {
        // System.out.println("cw " + this.width);
        // System.out.println("w " + renderingPanel.getWidth());
//        System.out.println(CELL_SIZE);
    

        for (Cell[] row : this.map)
        {
            for (Cell cell : row)
            {
                if (cell != null)
                {
                    // System.out.println("Drawing sand!");

                    // System.out.println("Filling rect with " + (cell.x * CELL_SIZE) + ", " + (renderingPanel.getHeight() - (cell.y * CELL_SIZE)));
//                    System.out.println(cell.x + " * " + CELL_SIZE);
                    int draw_x = cell.x * this.cell_size;
                    int draw_y = (renderingPanel.getHeight() - (cell.y * this.cell_size)) - this.cell_size;


                    if (cell instanceof Water)
                    {
                        if (debug_mode)
                        {
                            g2.setColor(new Color(255, 255, 255));
                            g2.drawString(String.valueOf(((Water) cell).mass), draw_x, draw_y);
                        }

                        if (PRESSURE_DEBUG)
                        {
                            g2.setColor(new Color(255, 255, 255));
                            g2.drawString(String.valueOf(((Water) cell).pressure), draw_x, draw_y);
                        }

                        if (!debug_mode && !PRESSURE_DEBUG)
                        {
                            g2.setColor(cell.color);
                            g2.fillRect(draw_x, draw_y, this.cell_size, this.cell_size);
                        }

                    }
                    else
                    {
                        g2.setColor(cell.color);
                        g2.fillRect(draw_x, draw_y, this.cell_size, this.cell_size);
                    }

                }
            }
        }

    }

}
