package cells;

import java.awt.*;
import java.util.ArrayList;
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
    static final int MAX_COMPRESS = 2;
    static final int MIN_MASS = 1;
    static final int MIN_FLOW = 1;
    static final int MAX_SPEED = 100;

    public Water(int x, int y)
    {
        super(x, y, CellType.WATER, 100);
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

    int get_stable_state_b(int total_mass)
    {
        if (total_mass <= 1)
        {
            return 1;
        }
        else if (total_mass < 2 * MAX_MASS + MAX_COMPRESS)
        {
            return (MAX_MASS * MAX_MASS + total_mass * MAX_COMPRESS)/(MAX_MASS / MAX_COMPRESS);
        }
        else
        {
            return (total_mass + MAX_COMPRESS) / 2;
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

        if (flow > MIN_FLOW)
            flow /= 2;

        if (vertical)
            flow = constrain(flow, 0, Math.min(MAX_SPEED, this.mass));
        else
            flow = constrain(flow, 0, this.mass);


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
            return this.mass - get_stable_state_b(this.mass + ((Fluid) other).mass);
        return this.mass - get_stable_state_b(this.mass);
    }

    int flowIntoBellow(Cell other)
    {
        if (other instanceof Fluid)
            return get_stable_state_b(this.mass + ((Fluid) other).mass);
        return get_stable_state_b(this.mass);
    }

    int flowIntoSide(Cell other)
    {
        if (other instanceof Fluid)
            return (this.mass - ((Fluid) other).mass) / 4;
        return (this.mass) / 4;
    }

}
