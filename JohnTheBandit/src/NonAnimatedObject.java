import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Not moving OnScreenObject. Stores single image.
 * Created by needa_000 on 12/23/2014.
 */
public class NonAnimatedObject extends OnScreenObject
{
    private Image image;

    public NonAnimatedObject(Image image,int x, int y, int z)
    {
        super(x,y,z);
        this.image = image;
    }

    public void setImageToDisplay(Image image)
    {
        this.image = image;
    }
    @Override
    public Image getImageToDisplay()
    {
        return image;
    }

}
