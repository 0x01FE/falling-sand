package sand;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/*

Water and other liquids tend to skew to the left for some reason. At first I thought it was the Java
random number generator being skewed but that doesn't seem to be the case. In (r == 1) 1 can be changed to 0 and get the same results.
I think the issue is actually with how the map updates from left to right.

Solution: Update the map to use a sort of frame buffer, where the new updated map is written to a seperated array that won't
interfere with other cells.

*/


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

        this.updateColor(m);
    }

    void setColor() { this.color = new Color(101, 101, 210); }

    void updateColor(CellMap m)
    {
        this.setColor();

        ArrayList<Cell> cells = m.getNeighbors(this);
        int water_neighbors = 0;

        for (Cell c : cells)
            if (c.type == CellType.WATER)
                water_neighbors++;

        for (int i = 0; i < water_neighbors / 2; i++)
            this.color = this.color.darker();


    }
}
