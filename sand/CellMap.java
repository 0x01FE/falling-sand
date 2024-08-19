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
    }

    public void setCell(int x, int y, CellType c)
    {
        this.cells[y][x] = new Cell(x, y, c);
    }

    public void wipeCell(int x, int y)
    {
        this.cells[y][x] = null;
    }

    public void setCell(Cell c)
    {
        this.cells[c.y][c.x] = c;
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

                else if (cell.type == CellType.SAND)
                {
                    // No clue what this was doing
                    // if (y == CellType.AIR)
                    //     continue;
                        
                    // Falling
                    // Check if at bottom
                    if (cell.y >= this.height || cell.y - 1 < 0)
                        continue;

                    Cell under = getUnder(cell);
                    boolean fall = (under == null);

                    if (!fall)
                    {
                        fall = (under.type == CellType.WATER);
                    }

                    // Basically just swap them
                    if (fall)
                    {
                        int old_x = cell.x;
                        int old_y = cell.y;

                        // System.out.println("sand is falling, new coords" + cell.x + ", " + (cell.y - 1));
                        setCell(cell.x, cell.y - 1, cell.type);

                        // Determine CellType of where we were
                        CellType prevType = null;
                        if (under != null)
                        {
                            prevType = under.type;
                        }

                        // System.out.println("new air coords, " + cell.x + ", " + cell.y + ", " + prevType);
                        this.wipeCell(old_x, old_y);
                        // System.out.println("setting " + old_x + ", " + old_y + " to null");
                    }
                }
            }
        }
    }


    private Cell getUnder(Cell c)
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
