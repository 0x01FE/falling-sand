package sand;

import java.awt.*;
import java.util.Random;

public class Sand extends Cell
{

    public Sand(int x, int y)
    {
        super(x, y);
        generateColor();
    }

    public Sand(Point p)
    {
        super(p.x, p.y);
        generateColor();
    }

    public void update(CellMap m)
    {
        if (this.y == 0)
            return;

        // If there's nothing bellow the sand, fall
        if (m.cells[this.y][this.x] == null)
        {
            this.y += 1;
        }

        // Pick random side to check if there's a pixel bellow
        Random rand = new Random();

        int r = rand.nextInt(2);

        int dx = 1;
        if (r == 1 && this.x != 0)
            dx *= -1;

        if (m.cells[this.y - 1][this.x + dx] == null)
        {
            this.y -= 1;
            this.x += dx;
        }

        // If at edge don't check other side
        if (this.x == 0 || this.x == m.width)
            return;

        dx *= -1;

        if (m.cells[this.y - 1][this.x + dx] == null)
        {
            this.y -= 1;
            this.x += dx;
        }

    }

    public void generateColor()
    {
        this.color = new Color(255, 185, 0);
    };
}
