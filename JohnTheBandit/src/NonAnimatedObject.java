import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by needa_000 on 12/23/2014.
 */
public class NonAnimatedObject extends OnScreenObject
{
    private Image image;

    public NonAnimatedObject(BufferedImage image,int x, int y, int z)
    {
        super(x,y,z);
        this.image = image;
    }

    public void setImageToDisplay(Image imge)
    {
        this.image = image;
    }
    @Override
    public Image getImageToDisplay()
    {
        return image;
    }

}
