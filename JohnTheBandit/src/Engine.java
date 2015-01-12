import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by needa_000 on 12/23/2014.
 */
public class Engine extends JPanel implements Runnable, ActionListener
{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;

    private BlockingQueue<OnScreenObject> objectsToAdd = new ArrayBlockingQueue<OnScreenObject>(500);
    private BlockingQueue<OnScreenObject> objectsToRemove = new ArrayBlockingQueue<OnScreenObject>(500);
    private BlockingQueue<Integer> layersToClear = new ArrayBlockingQueue<Integer>(25);
    private BlockingQueue<MoveObject> objectsToMove = new ArrayBlockingQueue<MoveObject>(500);

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
        long before = System.currentTimeMillis();
        Graphics2D backBufferGraphics = (Graphics2D) backBuffer.getGraphics();
        backBufferGraphics.clearRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());
        for (int i = 0; i < objectLayers.size(); i++)
        {
            if(generatedPermaLayers.containsKey(i))
            {
                backBufferGraphics.drawImage(generatedPermaLayers.get(i), 0, 0, null);
                continue;
            }
            for(OnScreenObject o:objectLayers.get(i))
                backBufferGraphics.drawImage(o.getImageToDisplay(), o.getX(), o.getY(), null);

        }
        g.drawImage(backBuffer,0,0,null);
        System.out.println(System.currentTimeMillis() - before);
    }


    public void addObject(OnScreenObject objectToAdd)
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
        System.out.println("Starting engine");
        timer.start();
    }

    public void queueAddObject(OnScreenObject object)
    {
        System.out.println("Queuing object to layer " + object.getZLayer());
        objectsToAdd.add(object);
    }


    public void queueAddManyObjects(ArrayList<NonAnimatedObject> toAdd)
    {
        objectsToAdd.addAll(toAdd);
    }

    public void queueRemoveObject(int x, int y, int zLayer)
    {
        for(OnScreenObject o: objectLayers.get(zLayer))
        {
            if(o.getX() == x && o.getY() == y)
            {
                objectsToRemove.add(o);
                return;
            }
        }
    }

    public void queueClearLayer(int zLayer)
    {
        layersToClear.add(zLayer);
    }

    public void queueMoveObject(OnScreenObject o, int newX, int newY)
    {
        objectsToMove.add(new MoveObject(o,newX,newY));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        timeElapsed += 100;
        frameCount++;
        boolean somethingChanged = false;
        HashSet<Integer> changedLayers = new HashSet<Integer>();
        for(Integer frameDifference: animatedObjectsByFrameGap.keySet())
            if(frameCount % frameDifference == 0)
                 for(AnimatedObject ao:animatedObjectsByFrameGap.get(frameDifference))
                 {
                     ao.cycle();
                     changedLayers.add(ao.getZLayer());
                     somethingChanged = true;
                 }
        for(Integer z:layersToClear)
        {
            if(generatedPermaLayers.containsKey(z))
                generatedPermaLayers.remove(z);
            if(objectLayers.size() > z)
                objectLayers.get(z).clear();
        }
        layersToClear.clear();
        for(OnScreenObject o: objectsToRemove)
        {
            objectLayers.get(o.getZLayer()).remove(o);
            changedLayers.add(o.getZLayer());
            somethingChanged = true;
        }
        objectsToRemove.clear();
        for(OnScreenObject o: objectsToAdd)
        {
            System.out.println("Adding object to layer " + o.getZLayer());
            addObject(o);
            changedLayers.add(o.getZLayer());
            somethingChanged = true;
        }
        objectsToAdd.clear();
        for(MoveObject mo: objectsToMove)
        {
            mo.applyMove();
            changedLayers.add(mo.getOnScreenObject().getZLayer());
            somethingChanged = true;
        }
        objectsToMove.clear();
        for(Integer i: changedLayers)
            saveLayer(i);

        if(somethingChanged)
            repaint();

    }

    private class MoveObject
    {
        private OnScreenObject o;
        private int x;
        private int y;

        public MoveObject(OnScreenObject o, int x, int y)
        {
            this.o = o;
            this.x = x;
            this.y = y;
        }

        public OnScreenObject getOnScreenObject()
        {
            return o;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public void applyMove()
        {
            o.setX(x);
            o.setY(y);
        }
    }
}


