import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Loads and stores images to prevent multiple instances of the same image
 * Created by needa_000 on 1/12/2015.
 */
public class ImageLoader
{
    private HashMap<String, Image> tiles;
    private HashMap<String, Image> actors;
    private Image errorImage;

    /**
     * Creates storage for different image types
     * Creates error image for when unable to load image
     */
    public ImageLoader()
    {
        tiles = new HashMap<String, Image>();
        actors = new HashMap<String, Image>();
        errorImage = new BufferedImage(50,50,Constants.IMAGE_TYPE);
        Graphics2D g = (Graphics2D) errorImage.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0,0,errorImage.getWidth(null),errorImage.getHeight(null));
        g.setFont(new Font("Times New Roman",Font.BOLD,50));
        g.setColor(Color.BLACK);
        g.drawString("??",0,42);
    }

    /**
     * Loads images from actor image storage location
     * @param name File name
     * @param width Requested width of image
     * @param height Requested height of image
     * @return Image of the given width and height from the file name
     */
    public Image loadActor(String name, int width, int height)
    {
        Image image = null;
        if(actors.containsKey(name))
            image =  actors.get(name);
        else
            image = loadImage(Constants.IMAGES_CHARACTERS_PATH + name);
        actors.put(name,image);
        if(width != -1 && height != -1)
            return image.getScaledInstance(width, height,Image.SCALE_SMOOTH);
        return image;
    }

    /**
     * Loads images from tile image storage location
     * @param name File name
     * @param width Requested width of image
     * @param height Requested height of image
     * @return Image of the given width and height from the file name
     */
    public Image loadTile(String name, int width, int height)
    {
        Image image = null;
        if(tiles.containsKey(name))
            image = tiles.get(name);
        else
            image = loadImage(Constants.IMAGES_TILES_PATH + name);
        tiles.put(name, image);
        if(width != -1 && height != -1)
            return image.getScaledInstance(width, height,Image.SCALE_SMOOTH);
        return image;
    }

    /**
     * Loads the image from the given file name/path.
     * Returns the error image if unable to load image
     * @param fileName Path or relative path of image
     * @return The loaded image or the error image
     */
    public Image loadImage(String fileName)
    {
        BufferedImage image = null;
        File imageFile = new File("JohnTheBandit/" + fileName);
        System.out.println(imageFile.getAbsoluteFile());
        try
        {
            image = ImageIO.read(imageFile);
        } catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Unable to load image at: " + imageFile.getAbsoluteFile());
            return getErrorImage();
        }
        return image;
    }

    /**
     * @return The error image, red square with question marks
     */
    public Image getErrorImage()
    {
        return errorImage;
    }

}
