package sand;

import javax.swing.*;
import java.awt.*;

public class CellMap
{
    public static final int CELL_SIZE = 10;

    public int height;
    public int width;

    Cell[][] cells;

    public CellMap(int h, int w)
    {
        this.height = h;
        this.width = w;
        this.cells = new Cell[h][w];
    }

    public void setCell(Cell c)
    {
        this.cells[c.x][c.y] = c;
    }

    public void update()
    {
        for (Cell[] row : this.cells)
        {
            for (Cell cell : row)
            {
                if (cell != null)
                    cell.update(this);
            }
        }
    }

    public void draw(Graphics g2, JPanel renderingPanel)
    {

        for (Cell[] row : this.cells)
        {
            for (Cell cell : row)
            {
                if (cell != null)
                {
                    g2.setColor(cell.color);

                    g2.fillRect(cell.x - renderingPanel.getWidth(), cell.y - renderingPanel.getHeight(), CELL_SIZE, CELL_SIZE);
                }
            }
        }

    }

}
