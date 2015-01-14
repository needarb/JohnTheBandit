import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
/**
 * Created by needa_000 on 1/13/2015.
 */
public class ImageDisplay extends JPanel implements MouseListener
{
    private List<Image> images;
    private int selectedImage;

    public ImageDisplay(List<Image> images)
    {
        this.images = images;
        setPreferredSize(new Dimension(50,700));

        addMouseListener(this);

    }


    @Override
    public void paint(Graphics g)
    {
        BufferedImage backBuffer = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < images.size(); i++)
        {
            Image scaled = images.get(i).getScaledInstance(50,50,Image.SCALE_SMOOTH);
            g.drawImage(scaled,0,i*50,null);
            if(i == selectedImage)
            {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(4));
                g.setColor(Color.BLUE);
                g.drawRect(0, i * 50, 49, i * 50 + 49);
            }

        }
        g.drawImage(backBuffer,0,0,null);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int convertedY = e.getY() / 50;
        if(convertedY < images.size() && convertedY >= 0)
            selectedImage = convertedY;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }



    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    public Image getSelectedImage()
    {
        return images.get(selectedImage);
    }
}
