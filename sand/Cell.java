package sand;

import java.awt.Color;

public abstract class Cell
{
    int x;
    int y;
    Color color;

    Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract void update(CellMap m);
    public abstract void generateColor();
}
