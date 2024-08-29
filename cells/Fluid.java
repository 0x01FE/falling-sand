package cells;

import java.awt.*;

public class Fluid extends Cell
{
    int mass;

    public Fluid(int x, int y, CellType type, int m)
    {
        super(x, y, type);
        this.mass = m;
    }

    public void update(CellMap m)
    {
        if (this.y <= m.height || this.y - 1 > 0)
        {
            Cell under = m.getUnder(this);

            if (under instanceof Fluid)
            {
                
            }
        }

    }

    void setColor()
    {
        if (this.mass < 0.3)
        {
            this.color = new Color(163, 208, 255);
        }
        else if (this.mass < 0.6)
        {
            this.color = new Color(94, 173, 255);
        }
        else
        {
            this.color = new Color(0, 126, 255);
        }
    }
}
