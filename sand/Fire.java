package sand;

import java.awt.*;
import java.util.Random;

public class Fire extends Cell
{
    long last_move;
    int moves;
    public Fire(int x, int y)
    {
        super(x, y, CellType.FIRE);
        this.last_move = System.currentTimeMillis();
        this.moves = 0;
    }

    public void update(CellMap m)
    {
        long current_time = System.currentTimeMillis();
        long timeDelta = current_time - this.last_move;

        // Check if can rise
        if (this.y + 1 < m.height && timeDelta >= 100)
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
