package cells;

public class Flammable extends Cell
{
    int burn_time; // in ms

    public Flammable(int x, int y, CellType type, int burn_time)
    {
        super(x, y, type);
        this.burn_time = burn_time;
    }

    public void update(CellMap m) {}

    void setColor() {}
}
