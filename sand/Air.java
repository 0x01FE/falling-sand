package sand;

import java.awt.*;

public class Air extends Cell {
    public Air(int x, int y)
    {
        super(x, y, CellType.AIR);
    }

    public void update(CellMap m) {};

    void setColor() { this.color =  new Color(0, 0, 0); }
}
