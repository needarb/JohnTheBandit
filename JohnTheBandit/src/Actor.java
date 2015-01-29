import java.awt.*;

/**
 * The Onscreen characters. Creates an OnScreenObject for this character
 * Created by needa_000 on 1/10/2015.
 */
public class Actor
{
    private PlayingField playingField;

    private int x; //Grid based x and y.
    private int y;

    private NonAnimatedObject onScreenObject;

    public Actor(PlayingField playingField, int x, int y, Image image)
    {
        this.playingField = playingField;
        this.x = x;
        this.y = y;
        onScreenObject = new NonAnimatedObject(image, x * playingField.getTileWidth(), y * playingField.getTileHeight(), PlayingField.CHARACTERS);
    }

    /**
     * Sets the grid based x and y and queues the OnScreenObject to move next update
     * @param newX new grid x
     * @param newY new grid y
     */
    public void setLocation(int newX, int newY)
    {

        this.x = newX;
        this.y = newY;
        playingField.getEngine().queueMoveObject(onScreenObject,newX*playingField.getTileWidth(),newY*playingField.getTileHeight());
    }

    public NonAnimatedObject getOnScreenObject()
    {
        return onScreenObject;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
