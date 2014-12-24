import java.awt.*;
import java.util.ArrayList;

/**
 * Created by needa_000 on 12/23/2014.
 */
public class AnimatedObject extends OnScreenObject
{
    private ArrayList<Image> images;
    private int frame;
    private int framesBetweenFrames;

    public AnimatedObject(ArrayList<Image> images)
    {
        super(0,0,0);
        this.images = images;
        framesBetweenFrames = 1;
    }

    @Override
    public Image getImageToDisplay()
    {
        return images.get(frame);
    }

    public int getNumFrames()
    {
        return images.size();
    }

    public int getNumFramesBetweenFrames()
    {
        return framesBetweenFrames;
    }

    public void cycle()
    {
        frame++;
        if(frame >= images.size())
            frame = 0;
    }
}
