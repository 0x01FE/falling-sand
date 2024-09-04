package cells;

import java.awt.*;
import java.util.Random;

/*

Water and other liquids tend to skew to the left for some reason. At first I thought it was the Java
random number generator being skewed but that doesn't seem to be the case. In (r == 1) 1 can be changed to 0 and get the same results.
I think the issue is actually with how the map updates from left to right.

Solution: Update the map to use a sort of frame buffer, where the new updated map is written to a seperated array that won't
interfere with other cells.

*/


public class Water extends Fluid
{
    static Random rand = new Random();
    static final int MAX_MASS = 9;
//    static final int MAX_COMPRESS = 150;
    static final int MIN_MASS = 1;
    static final int MIN_FLOW = 1;
    static final int MAX_SPEED = 100;
    static final int DEFAULT_MASS = 9;

    int x_pressure;
    int y_pressure;

    public Water(int x, int y)
    {
        super(x, y, CellType.WATER, DEFAULT_MASS);
        this.x_pressure = 0;
        this.y_pressure = 0;
    }

    public Water(int x, int y, int m)
    {
        super(x, y, CellType.WATER, m);
        this.x_pressure = 0;
        this.y_pressure = 0;
    }

    public void update(CellMap m)
    {
        if (this.mass < MIN_MASS)
        {
            m.wipeCell(this);
            return;
        }

        // Flow Down
        flowIntoBellow(m);

        if (this.mass < MIN_MASS)
        {
            m.wipeCell(this);
            return;
        }

        // Flow to sides
        int r = rand.nextInt(2);
        int side = 1;

        if (r == 1)
            side *= -1;

        flowIntoSide(m, side);

        if (this.mass < MIN_MASS)
        {
            m.wipeCell(this);
            return;
        }

        side *= -1;
        flowIntoSide(m ,side);

        if (this.mass < MIN_MASS)
        {
            m.wipeCell(this);
        }

        // Should we flow up?

//        this.updateColor();
    }

    void setColor() { this.color = new Color(101, 101, 210); }

    void updateColor()
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


    void flowInto(CellMap m, Cell other, int flow)
    {
        if (other instanceof Fluid)
            ((Fluid) m.buffer[other.y][other.x]).mass += flow;
        else
            m.setCell(new Water(other.x, other.y, flow));

//        ((Fluid) m.buffer[this.x][this.y]).mass -= flow;
        m.setCell(new Water(this.x, this.y, this.mass - flow));
        this.mass -= flow;
    }

//    int flowIntoAbove(Cell other)
//    {
//        if (other instanceof Fluid)
//            return this.mass - get_stable_vertical_state(((Fluid) other).mass, this.mass);
//        return this.mass - get_stable_vertical_state(0, this.mass);
//    }

    void flowIntoBellow(CellMap m)
    {
        // Don't flow out of bounds
        if (this.y - 1 < 0)
            return;

        int flow = 0;

        Cell under = m.getUnder(this);
        if (under instanceof Fluid)
        {
            int under_mass = ((Fluid) under).mass;
            if (under_mass + this.mass > MAX_MASS)
            {
                flow = MAX_MASS - under_mass;
            }
            else
            {
                flow = this.mass;
            }
        }
        // TODO @0x01FE : make that density system you keep thinking about
        else if (under instanceof Air)
        {
            flow = this.mass;
        }


        if (flow != 0)
            flowInto(m, under, flow);
    }

    void flowIntoSide(CellMap m, int side)
    {
        int flow = 0;

        // If out of bounds do nothing
        if (this.x + side < 0 || this.x + side >= m.width)
            return;

        Cell side_cell = m.getCell(this.x + side, this.y);

        if (side_cell instanceof Fluid)
        {
            int side_cell_mass = ((Fluid) side_cell).mass;
            flow = this.mass - ((side_cell_mass + this.mass) / 2);
        }
        else if (side_cell instanceof Air)
        {
            flow = this.mass - (this.mass / 2);
        }

        if (flow != 0)
            flowInto(m, side_cell, flow);

    }

}
