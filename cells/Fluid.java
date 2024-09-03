package cells;

import java.awt.*;

public class Fluid extends Cell
{
    public int mass;

    public Fluid(int x, int y, CellType type, int m)
    {
        super(x, y, type);
        this.mass = m;
    }

    public void update(CellMap m) {}

    void setColor() {}
}
