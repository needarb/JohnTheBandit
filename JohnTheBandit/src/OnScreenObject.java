import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Object with image to display on screen as well as image's location
 * Created by needa_000 on 12/23/2014.
 */
public abstract class OnScreenObject
{
    private int ZLayer;


    protected int x;
    protected int y;
    protected int zLayer;

    public OnScreenObject(int x, int y, int zLayer)
    {
        this.x = x;
        this.y = y;
        this.zLayer = zLayer;
    }

    public abstract Image getImageToDisplay();

    public Point getLocation()
    {
        return new Point(x, y);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setLocation(Point point)
    {
        this.x = (int) point.getX();
        this.y = (int) point.getY();
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getZLayer()
    {
        return ZLayer;
    }
}
