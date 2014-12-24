import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by needa_000 on 12/23/2014.
 */
public class TestWindow extends JFrame implements MouseListener
{
    public static void main(String[] args)
    {
        TestWindow window = new TestWindow();
    }
    private Engine engine;
    public TestWindow()
    {
       super();
        addMouseListener(this);
        engine = new Engine();
        OnScreenObject o1 = new NonAnimatedObject(createImageWithSquare(100,100,Color.BLUE),100,100,0);
        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 1; i < 6; i++)
        {
            images.add(createImageWithSquare(50*i,50*i,i%2==0?Color.BLUE:Color.RED));
        }

        engine.addObject(new AnimatedObject(loadImages()));
        engine.addObject(o1);
        add(engine);
        Thread t = new Thread(engine);
        repaint();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        t.start();
    }

    public BufferedImage createImageWithSquare(int width, int height, Color c)
    {
        BufferedImage image = new BufferedImage(width,height,Engine.IMAGE_TYPE);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(c);
        graphics.fillRect((int)(width*.1),(int)(height*.1),(int)(width*.8),(int)(height*.8));
        return image;
    }

    public ArrayList<Image> loadImages()
    {
        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i < 181; i++)
        {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("JohnTheBandit\\res\\images\\animations\\sickaf\\" + i + ".gif"));
            } catch (IOException e) {
            }
            images.add(img);
        }
     return  images;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        int red = (int)(Math.random()*256);
        int green = (int)(Math.random()*256);
        int blue = (int)(Math.random()*256);
        Color randomColor = new Color(red,green,blue,150);
        engine.queueObject(new NonAnimatedObject(createImageWithSquare(200,200,randomColor),e.getX(),e.getY(),1));
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
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
}
