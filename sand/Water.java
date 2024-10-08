package sand;

import java.awt.*;
import java.util.Random;

public class Water extends Cell
{
    public Water(int x, int y) { super(x, y, CellType.WATER); }

    public void update(CellMap m)
    {
        if (this.y >= m.height || this.y - 1 < 0)
            return;

        Cell under = m.getUnder(this);

        if (under.type == CellType.AIR) {
            m.swapCells(this, under);
        }

        // If cant fall, move to random side
        else
        {
            int side = 1;
            Random rand = new Random();

            int r = rand.nextInt(2);

            if (r == 1)
                side *= -1;

            for (int i = 0; i < 2; i++)
            {
                if (!(this.x + side >= m.width || this.x + side < 0))
                {
                    Cell side_cell = m.cells[this.y][this.x + side];

                    if (side_cell.type == CellType.AIR)
                    {
                        m.swapCells(this, side_cell);
                        return;
                    }
                }
                side *= -1;
            }
        }
    }

    void setColor()
    {
        this.color = new Color(45, 45, 200);
    }
}
