package cells;

import java.awt.*;

public class Fluid extends Cell
{
    public float mass;

    public Fluid(int x, int y, CellType type, float m)
    {
        super(x, y, type);
        this.mass = m;
    }

    public void update(CellMap m) {}

    void setColor() {}
}
