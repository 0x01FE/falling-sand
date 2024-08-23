package sand;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fire extends Cell
{
    long last_move;
    long last_smoke;
    int moves;

    static final int SMOKE_SPAWN_COOLDOWN = 200; // in ms

    public static final CellType[] FLAMMABLE_MATERIALS = { CellType.OIL };

    public Fire(int x, int y)
    {
        super(x, y, CellType.FIRE);
        this.last_move = System.currentTimeMillis();
        this.last_smoke = this.last_move;
        this.moves = 0;
    }

    public void update(CellMap m)
    {
        long current_time = System.currentTimeMillis();
        long timeDelta = current_time - this.last_move;

        // Check if can rise
        if (this.y + 1 < m.height && timeDelta >= 300)
        {
            Cell above = m.cells[this.y + 1][this.x];

            if (above.type == CellType.AIR)
                m.swapCells(this, above);

            Random rand = new Random();
            int r = rand.nextInt(5);

            if (r == 0)
                m.wipeCell(this);

            this.last_move = System.currentTimeMillis();
            moves++;
        }

        // Produce Smoke
        timeDelta = current_time - this.last_smoke;

        if (this.y + 1 < m.height && timeDelta >= SMOKE_SPAWN_COOLDOWN)
        {
            Cell above = m.cells[this.y + 1][this.x];

            if (above.type == CellType.AIR)
                m.setCell(new Smoke(above.x, above.y));

            this.last_smoke = System.currentTimeMillis();
        }

        // Set Things on Fire
        ArrayList<Cell> neighbors = m.getNeighbors(this);

        for (Cell c : neighbors)
        {
            for (CellType material : FLAMMABLE_MATERIALS)
            {
                if (c.type == material)
                {
                    m.setCell(new Fire(c.x, c.y));
                }
            }
        }


        this.updateColor();
    }

    void setColor()
    {
        this.color = new Color(203, 59, 27);
    }

    void updateColor()
    {
        this.setColor();

        for (int i = 0; i < moves; i++)
            this.color = this.color.darker();
    }
}
