package sand;

import javax.swing.*;
import java.awt.*;

public class CellMap
{

    public int height;
    public int width;

    Cell[][] cells;

    public CellMap(int h, int w)
    {
        this.height = h;
        this.width = w;
        this.cells = new Cell[h][w];

        wipeMap();
    }

    public void wipeMap()
    {
        int y = 0;
        for (Cell[] row : this.cells)
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
    public void wipeCell(int x, int y)
    {
        this.cells[y][x] = new Air(x, y);
    }

    public void setCell(Cell c)
    {
        this.cells[c.y][c.x] = c;
    }

    public void swapCells(Cell c1, Cell c2)
    {
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
        for (Cell[] row : this.cells)
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

    public void update()
    {
        System.out.println("Updating map...");
        for (Cell[] row : this.cells)
        {
            for (Cell cell : row)
            {
                if (cell == null)
                    continue;

                cell.update(this);
            }
        }
    }


    public Cell getUnder(Cell c)
    {
        return this.cells[c.y - 1][c.x];
    }

    public void draw(Graphics g2, JPanel renderingPanel)
    {
        // System.out.println("cw " + this.width);
        // System.out.println("w " + renderingPanel.getWidth());
        int cell_size = renderingPanel.getWidth() / this.width;
        System.out.println(cell_size);
    

        for (Cell[] row : this.cells)
        {
            for (Cell cell : row)
            {
                if (cell != null)
                {
                    // System.out.println("Drawing sand!");
                    g2.setColor(cell.color);

                    // System.out.println("Filling rect with " + (cell.x * cell_size) + ", " + (renderingPanel.getHeight() - (cell.y * cell_size)));
                    g2.fillRect(cell.x * cell_size, (renderingPanel.getHeight() - (cell.y * cell_size)) - cell_size, cell_size, cell_size);
                }
            }
        }

    }

}
