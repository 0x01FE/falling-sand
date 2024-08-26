package cells;

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

        if (under.type == CellType.AIR || under.type == CellType.WATER) {
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

            for (int i = 0; i < 2; i++)
            {
                if (!(this.x + side >= m.width || this.x + side < 0))
                {
                    Cell side_cell = m.cells[this.y - 1][this.x + side];

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
        Random rand = new Random();
        int mod = rand.nextInt(11);
        float hue = (float) ((36 + mod) / 359.0);
        int saturation = 1;
        float blackness = (float) (75 / 100.0);

//        this.color = new Color(255, 185, 0);
        this.color = Color.getHSBColor(hue, saturation, blackness);
    }
}
