package sand;

import java.awt.*;
import java.util.Random;

public class Smoke extends Cell {
    long last_move;
    int moves;
    static final int LIFETIME_AVG = 10;

    public Smoke(int x, int y)
    {
        super(x, y, CellType.SMOKE);

        this.last_move = System.currentTimeMillis();
        this.moves = 0;
    }

    public void update(CellMap m)
    {
        long current_time = System.currentTimeMillis();
        long timeDelta = current_time - this.last_move;

        Random rand = new Random();

        // Check if can rise
        if (this.y + 1 < m.height && timeDelta >= 100)
        {
            Cell above = m.cells[this.y + 1][this.x];
            int r = rand.nextInt(7);

            if (above.type == CellType.AIR && r != 0)
                m.swapCells(this, above);
            else
            {
                boolean moved = false;
                r = rand.nextInt(2);

                int side = 1;
                if (r == 0)
                    side *= -1;

                for (int i = 0; i < 2; i++)
                {
                    if (!(this.x + side >= m.width || this.x + side < 0) && !moved)
                    {
                        Cell side_cell = m.cells[this.y][this.x + side];

                        if (side_cell.type == CellType.AIR)
                        {
                            m.swapCells(this, side_cell);
                            moved = true;
                        }
                    }
                    side *= -1;
                }
            }

            r = rand.nextInt(LIFETIME_AVG + 1);

            if (r == 0)
                m.wipeCell(this);

            this.last_move = System.currentTimeMillis();
            moves++;
        }

        this.updateColor();
    }

    void setColor()
    {
        this.color = new Color(203, 59, 27).darker();
    }

    void updateColor()
    {
        this.setColor();

        int max = this.moves;

        if (max > 3)
            max = 3;


        for (int i = 0; i < max; i++)
            this.color = this.color.darker();
    }
}
