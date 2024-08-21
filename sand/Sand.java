package sand;

import java.awt.*;
import java.util.Random;

public class Sand extends Cell
{

    public Sand(int x, int y)
    {
        super(x, y, CellType.SAND);
    }

    public Sand(Point p)
    {
        super(p.x, p.y, CellType.SAND);
    }

    public void update(CellMap m) {
        // Check if at bottom
        if (this.y >= m.height || this.y - 1 < 0)
            return;

        Cell under = m.getUnder(this);

        // Basically just swap them
        if (under.type == CellType.AIR) {
            m.swapCells(this, under);
        }

        // If there is sand under the cell check to the sides
        else if (under.type == CellType.SAND)
        {
            int side = 1;
            Random rand = new Random();

            int r = rand.nextInt(2);

            if (r == 1)
                side *= -1;

            Cell side_cell = m.cells[this.y - 1][this.x + side];

            if (side_cell.type == CellType.AIR)
            {
                m.swapCells(this, side_cell);
                return;
            }

            side *= -1;
            side_cell = m.cells[this.y - 1][this.x + side];
            if (side_cell.type == CellType.AIR)
            {
                m.swapCells(this, side_cell);
                return;
            }
        }
    }

    void setColor()
    {
        this.color = new Color(255, 185, 0);
    }
}
