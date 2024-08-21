package sand;

import java.awt.Color;

public abstract class Cell
{
    int x;
    int y;
    CellType type;
    Color color;

    Cell(int x, int y, CellType type)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.setColor();
    }

    public void print()
    {
        System.out.println(this.type + " at (" + this.x + ", " + this.y + ")");
    }

    abstract void setColor();
    abstract void update(CellMap m);
}
