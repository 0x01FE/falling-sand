package sand;

import javax.swing.*;
import java.awt.*;

public class CellMap
{

    public int height;
    public int width;

    CellType[][] cells;

    public CellMap(int h, int w)
    {
        this.height = h;
        this.width = w;
        this.cells = new CellType[h][w];
    }

    public void setCell(int x, int y, CellType c)
    {
        this.cells[x][y] = c;
    }

    public void update()
    {
        int y = 0;
        for (int[] row : this.cells)
        {
            int x = 0;
            for (int cell : row)
            {
                switch (cell)
                {
                    case CellType.AIR:
                        break;
                    case CellType.SAND:
                        if (y == CellType.AIR)
                            break;
                        
                        // Falling
                        CellType under = getUnder(x, y);
                        
                        if (under == CellType.Air || under == CellType.WATER)
                        {
                            // Basically just swap them
                            setCell(x, y + 1, cell);
                            setCell(x, y, under);
                        }
                        break;
                }
                x++;    
            }
            y++;
        }
    }
    
    private CellType getUnder(int x, int y)
    {
        return this.cells[y + 1][x];
    }

    public void draw(Graphics g2, JPanel renderingPanel)
    {
        System.out.println("cw " + this.width);
        System.out.println("w " + renderingPanel.getWidth());
        int cell_size = renderingPanel.getWidth() / this.width;
        System.out.println(cell_size);
    

        for (Cell[] row : this.cells)
        {
            for (Cell cell : row)
            {
                if (cell != null)
                {
                    g2.setColor(cell.color);

                    g2.fillRect(cell.x * cell_size, renderingPanel.getHeight() - (cell.y * cell_size), cell_size, cell_size);
                }
            }
        }

    }

}
