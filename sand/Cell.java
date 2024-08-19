package sand;

import java.awt.Color;

public class Cell
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

    void setColor()
    {
        if (this.type == CellType.SAND)
        {
            this.color = new Color(255, 185, 0);
        }
    }
}
