import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by needa_000 on 12/23/2014.
 */
public class Engine extends JPanel implements ImageChangeListener,Runnable, ActionListener
{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;

    private BufferedImage backBuffer;
    public Engine()
    {
        super();
        setBackground(Color.RED);
        objectLayers = new ArrayList<ArrayList<OnScreenObject>>();
        backBuffer = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
        generatedPermaLayers = new HashMap<Integer, BufferedImage>();
        animatedObjectsByFrameGap = new HashMap<Integer, ArrayList<AnimatedObject>>();

    }

    private ArrayList<ArrayList<OnScreenObject>> objectLayers;
    private HashMap<Integer,BufferedImage> generatedPermaLayers;
    private HashMap<Integer,ArrayList<AnimatedObject>> animatedObjectsByFrameGap;

    @Override
    public void paint(Graphics g)
    {
        Graphics2D backBufferGraphics = (Graphics2D) backBuffer.getGraphics();
        backBufferGraphics.clearRect(0,0,backBuffer.getWidth(), backBuffer.getHeight());
        for (int i = 0; i < objectLayers.size(); i++)
        {
            if(generatedPermaLayers.containsKey(i))
            {
                backBufferGraphics.drawImage(generatedPermaLayers.get(i), 0, 0, null);
                continue;
            }
            for(OnScreenObject o:objectLayers.get(i))
                backBufferGraphics.drawImage(o.getImageToDisplay(),o.getX(),o.getY(),null);

        }
        g.drawImage(backBuffer,0,0,null);
    }

    @Override
    public void onChangeLocation(OnScreenObject changedObject, int oldX, int oldY)
    {
        int zLayer = changedObject.getZLayer();
        if(generatedPermaLayers.containsKey(zLayer))
            saveLayer(zLayer);
    }

    @Override
    public void onChangeImage(OnScreenObject changedObject)
    {
        onChangeLocation(changedObject,-1,-1);
    }

    public void addObject(OnScreenObject objectToAdd)
    {
        addObject(objectToAdd,true,false);
    }

    public void addObject(OnScreenObject objectToAdd, boolean repaint)
    {
        addObject(objectToAdd, repaint,false);
    }

    public void addObject(OnScreenObject objectToAdd,boolean repaint, boolean resave)
    {
        int zLayer = objectToAdd.getZLayer();
        while(objectLayers.size() <= zLayer)
            objectLayers.add(new ArrayList<OnScreenObject>());
        objectLayers.get(zLayer).add(objectToAdd);
        if(objectToAdd instanceof AnimatedObject)
        {
            int framesBetween = ((AnimatedObject) objectToAdd).getNumFramesBetweenFrames();
            if (!animatedObjectsByFrameGap.containsKey(framesBetween))
                animatedObjectsByFrameGap.put(framesBetween, (new ArrayList<AnimatedObject>()));
            animatedObjectsByFrameGap.get(framesBetween).add((AnimatedObject) objectToAdd);
        }
        if(resave && generatedPermaLayers.containsKey(zLayer))
            saveLayer(zLayer);
        if(repaint)
            repaint();
    }

    public void addManyObjects(ArrayList<OnScreenObject> objectsToAdd)
    {
        addManyObjects(objectsToAdd,true);
    }

    public void addManyObjects(ArrayList<OnScreenObject> objectsToAdd, boolean repaint)
    {
        Set<Integer> permaLayersChanged = new HashSet<Integer>();
        for(OnScreenObject o: objectsToAdd)
        {
            addObject(o, false, false);
            if(generatedPermaLayers.containsKey(o.getZLayer()))
                permaLayersChanged.add(o.getZLayer());
        }
        for(Integer z:permaLayersChanged)
            saveLayer(z);
        if(repaint)
            repaint();
    }

    private void saveLayer(int zLayer)
    {
        BufferedImage layer = new BufferedImage(WIDTH,HEIGHT,IMAGE_TYPE);
        for(OnScreenObject o:objectLayers.get(zLayer))
            layer.getGraphics().drawImage(o.getImageToDisplay(),o.getX(),o.getY(),null);
        generatedPermaLayers.put(zLayer,layer);
    }


    long timeElapsed;
    long frameCount;
    @Override
    public void run()
    {
        timeElapsed = 0;
        Timer timer = new Timer(40, this);
        timer.start();
    }

    private BlockingQueue objectsToAdd = new ArrayBlockingQueue(100);

    @Override
    public void actionPerformed(ActionEvent e)
    {
        timeElapsed += 100;
        frameCount++;
        boolean somethingChanged = false;
        for(Integer frameDifference: animatedObjectsByFrameGap.keySet())
            if(frameCount % frameDifference == 0)
                 for(AnimatedObject ao:animatedObjectsByFrameGap.get(frameDifference))
                 {
                     ao.cycle();
                     somethingChanged = true;
                 }
        if(somethingChanged)
            repaint();

    }
}


