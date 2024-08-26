package cells;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fire extends Cell
{
    long last_move;
    long last_smoke;
    int moves;

    int burn_time = 1500; // in ms
    long spawn_time;

    static final int SMOKE_SPAWN_COOLDOWN = 200; // in ms

    public Fire(int x, int y)
    {
        super(x, y, CellType.FIRE);
        this.last_move = System.currentTimeMillis();
        this.spawn_time = this.last_move;
        this.last_smoke = this.last_move;
        this.moves = 0;
    }

    public Fire(int x, int y, int burn_time)
    {
        super(x, y, CellType.FIRE);
        this.last_move = System.currentTimeMillis();
        this.spawn_time = this.last_move;
        this.last_smoke = this.last_move;
        this.moves = 0;
        this.burn_time = burn_time;
    }

    public void update(CellMap m)
    {
        long current_time = System.currentTimeMillis();
        long timeDelta = current_time - this.last_move;

        // Set Things on Fire or die if no air
        ArrayList<Cell> neighbors = m.getNeighbors(this);
        boolean is_air = false;

        for (Cell target : neighbors)
        {
                if (target instanceof Flammable)
                {
                    ArrayList<Cell> target_neighbors = m.getNeighbors(target);
                    boolean is_air_for_spread = false;

                    for (Cell target_neighbor : target_neighbors)
                    {
                        if (target_neighbor.type == CellType.AIR || target_neighbor.type == CellType.SMOKE)
                        {
                            is_air_for_spread = true;
                            break;
                        }
                    }

                    if (is_air_for_spread)
                        m.setCell(new Fire(target.x, target.y, ((Flammable) target).burn_time));
                }

                if (target.type == CellType.AIR)
                    is_air = true;
        }

        // Flame dies out if no oxygen (air)
        if (!is_air)
        {
            m.wipeCell(this);
            return;
        }

        // Check if can rise
        if (this.y + 1 < m.height && timeDelta >= 300)
        {
            Cell above = m.cells[this.y + 1][this.x];

//            if (above.type == CellType.AIR)
//                m.swapCells(this, above);

            if (current_time - this.spawn_time >= this.burn_time)
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

        this.updateColor();
    }

    void setColor()
    {
        this.color = new Color(203, 59, 27);
    }

    void updateColor()
    {
        this.setColor();

        int max = this.moves;

        if (max > 2)
            max = 2;

        for (int i = 0; i < max; i++)
            this.color = this.color.darker();
    }
}
