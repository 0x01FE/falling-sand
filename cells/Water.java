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
    static final int MAX_MASS = 100;
    static final int MAX_COMPRESS = 150;
    static final int MIN_MASS = 1;
    static final int MIN_FLOW = 1;
    static final int MAX_SPEED = 100;
    static final int DEFAULT_MASS = 100;

    public Water(int x, int y)
    {
        super(x, y, CellType.WATER, DEFAULT_MASS);
    }

    public Water(int x, int y, int m)
    {
        super(x, y, CellType.WATER, m);
    }

    public void update(CellMap m)
    {
        if (this.mass <= MIN_MASS)
        {
            m.wipeCell(this);
            return;
        }

        if (this.y - 1 >= 0)
        {
            Cell under = m.getUnder(this);
            flowInto(m, under);
        }

        int r = rand.nextInt(2);
        if (r == 1)
        {
            // Check Left
            if (this.x - 1 >= 0 && this.mass > 0)
            {
                Cell left = m.getCell(this.x - 1, this.y);
                flowInto(m, left);
            }
            // Check Right
            if (this.x + 1 < m.width && this.mass > 0)
            {
                Cell right = m.getCell(this.x + 1, this.y);
                flowInto(m, right);
            }
        }
        else
        {
            // Check Right
            if (this.x + 1 < m.width && this.mass > 0)
            {
                Cell right = m.getCell(this.x + 1, this.y);
                flowInto(m, right);
            }
            // Check Left
            if (this.x - 1 >= 0 && this.mass > 0)
            {
                Cell left = m.getCell(this.x - 1, this.y);
                flowInto(m, left);
            }
        }

        // Up, if pressure (compressed)
        if (this.y + 1 < m.height && this.mass > 0)
        {
            Cell above = m.getCell(this.x, this.y + 1);
            flowInto(m, above);
        }

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

    /*
    * The method will return an integer between -something and MAX_MASS
    *
    * Where negative integers represent flowing upwards do to mass being over max compression.
    *
    * The method is passed the mass of a top and bottom cell.
    */
    int get_stable_vertical_state(int top_mass, int bottom_mass)
    {
        // Typically we want to flow down, if we can
        if (bottom_mass < MAX_COMPRESS)
        {
            // If we can fit everything from the top in the bottom cell, have it flow!
            if (bottom_mass + top_mass <= MAX_COMPRESS)
            {
                return top_mass;
            }
            // If it doesn't all fit, return the difference as flow
            else
            {
                return (bottom_mass + top_mass) - MAX_COMPRESS;
            }
        }
        // If bottom mass is full, don't flow into it.
        else if (bottom_mass == MAX_COMPRESS)
        {
            return 0;
        }
        // If the bottom block is over max compression, try to flow up.
        else
        {
            return MAX_COMPRESS - bottom_mass;
        }
    }

    int constrain(int amt, int low, int high)
    {
        return Math.max(Math.min(amt, high), low);
    }

    void flowInto(CellMap m, Cell other)
    {
        if (this.mass <= MIN_MASS)
        {
            m.wipeCell(this);
            return;
        }

        int flow;
        boolean vertical = false;

        if (!(other instanceof Fluid || other instanceof Air))
            return;

        if(this.y - other.y > 0)
        {
            flow = flowIntoBellow(other);
            vertical = true;
        }
        else if (this.y - other.y < 0)
        {
            flow = flowIntoAbove(other);
//            System.out.println("Flowing up FLOW: " + flow);
            vertical = true;
        }
        else
            flow = flowIntoSide(other);

//        if (flow > MIN_FLOW)
//            flow /= 2;

//        if (vertical)
//            flow = constrain(flow, 0, Math.min(MAX_SPEED, this.mass));
//        else
//            flow = constrain(flow, 0, this.mass);

        if (flow == 0)
            return;


        ((Fluid) m.buffer[this.y][this.x]).mass -= flow;
        if (other instanceof Fluid)
            ((Fluid) m.buffer[other.y][other.x]).mass += flow;
        else if (other instanceof Air)
            m.setCell(new Water(other.x, other.y, flow));

        this.mass -= flow;

        if (this.mass <= MIN_MASS)
            m.wipeCell(this);
    }

    int flowIntoAbove(Cell other)
    {
        if (other instanceof Fluid)
            return this.mass - get_stable_vertical_state(((Fluid) other).mass, this.mass);
        return this.mass - get_stable_vertical_state(0, this.mass);
    }

    int flowIntoBellow(Cell other)
    {
        if (other instanceof Fluid)
            return get_stable_vertical_state(this.mass, ((Fluid) other).mass);
        return get_stable_vertical_state(this.mass, 0);
    }

    int flowIntoSide(Cell other)
    {
        if (other instanceof Fluid)
            return (this.mass - ((Fluid) other).mass) / 2;
        return (this.mass) / 2;
    }

}
