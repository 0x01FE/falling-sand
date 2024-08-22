package sand;

import java.awt.*;

public class Stone extends Cell
{
    public Stone(int x, int y) { super(x, y, CellType.STONE); }

    public void update(CellMap m) {};

    void setColor() { this.color = new Color(135, 135, 135); }
}
