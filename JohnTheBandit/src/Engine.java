import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * JPanel able to display moving and non-moving objects
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

    private ArrayList<ArrayList<OnScreenObject>> objectLayers;
    private HashMap<Integer, BufferedImage> generatedPermaLayers;
    private HashMap<Integer, ArrayList<AnimatedObject>> animatedObjectsByFrameGap;

    private BufferedImage backBuffer;

    public Engine()
    {
        super();
        setBackground(Color.RED);
        objectLayers = new ArrayList<ArrayList<OnScreenObject>>();
        backBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        generatedPermaLayers = new HashMap<Integer, BufferedImage>();
        animatedObjectsByFrameGap = new HashMap<Integer, ArrayList<AnimatedObject>>();

    }

    @Override
    public void paint(Graphics g)
    {
        long before = System.currentTimeMillis();
        Graphics2D backBufferGraphics = (Graphics2D) backBuffer.getGraphics();
        backBufferGraphics.clearRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());
        for (int i = 0; i < objectLayers.size(); i++)
        {
            if (generatedPermaLayers.containsKey(i))
            {
                backBufferGraphics.drawImage(generatedPermaLayers.get(i), 0, 0, null);
                continue;
            }
            for (OnScreenObject o : objectLayers.get(i))
                backBufferGraphics.drawImage(o.getImageToDisplay(), o.getX(), o.getY(), null);

        }
        g.drawImage(backBuffer, 0, 0, null);
        System.out.println(System.currentTimeMillis() - before);
    }

    /**
     * Adds the object to the screen and proper layer list
     * @param objectToAdd The object to be added
     */
    public void addObject(OnScreenObject objectToAdd)
    {
        int zLayer = objectToAdd.getZLayer();
        while (objectLayers.size() <= zLayer)
            objectLayers.add(new ArrayList<OnScreenObject>());
        objectLayers.get(zLayer).add(objectToAdd);
        if (objectToAdd instanceof AnimatedObject)
        {
            int framesBetween = ((AnimatedObject) objectToAdd).getNumFramesBetweenFrames();
            if (!animatedObjectsByFrameGap.containsKey(framesBetween))
                animatedObjectsByFrameGap.put(framesBetween, (new ArrayList<AnimatedObject>()));
            animatedObjectsByFrameGap.get(framesBetween).add((AnimatedObject) objectToAdd);
        }
    }

    /**
     * Draws all of the objects in the layer to a new BufferedImage and then saves that Image
     * Prevents redrawing every object on unchanged layers
     * @param zLayer
     */
    private void saveLayer(int zLayer)
    {
        BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, IMAGE_TYPE);
        for (OnScreenObject o : objectLayers.get(zLayer))
            layer.getGraphics().drawImage(o.getImageToDisplay(), o.getX(), o.getY(), null);
        generatedPermaLayers.put(zLayer, layer);
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

    /**
     * Queues the given object to add to the screen
     *
     * @param object Object to add
     */
    public void queueAddObject(OnScreenObject object)
    {
        objectsToAdd.add(object);
    }

    /**
     * Queues all of the given objects to add
     *
     * @param toAdd List of objects to add
     */
    public void queueAddManyObjects(List<NonAnimatedObject> toAdd)
    {
        objectsToAdd.addAll(toAdd);
    }

    /**
     * Queues the first object found at the given coordinates
     *
     * @param x      x cord
     * @param y      y cord
     * @param zLayer The objects layer
     */
    public void queueRemoveObject(int x, int y, int zLayer)
    {
        for (OnScreenObject o : objectLayers.get(zLayer))
        {
            if (o.getX() == x && o.getY() == y)
            {
                objectsToRemove.add(o);
                return;
            }
        }
    }

    /**
     * Queues the given object to be removed
     *
     * @param o Object to remove
     */
    public void queueRemoveObject(OnScreenObject o)
    {
        objectsToRemove.add(o);
    }

    /**
     * Queues all of the objects to be removed
     *
     * @param toRemove A list of objects to be removed
     */
    public void queueRemoveManyObjects(List<OnScreenObject> toRemove)
    {
        objectsToRemove.addAll(toRemove);
    }


    /**
     * Queues the given layer to be cleared (All objects removed)
     *
     * @param zLayer The layer to clear
     */
    public void queueClearLayer(int zLayer)
    {
        layersToClear.add(zLayer);
    }

    /**
     * Queues the given object to be moved. Creates a MoveObject which stores where it will go
     *
     * @param o    The object to be moved
     * @param newX The x grid location to move to
     * @param newY The y grid location to move to
     */
    public void queueMoveObject(OnScreenObject o, int newX, int newY)
    {
        objectsToMove.add(new MoveObject(o, newX, newY));
    }


    private boolean somethingChanged = false;
    private HashSet<Integer> changedLayers = new HashSet<Integer>();

    /**
     * Updates moving images, clears layers, removes objects, adds objects, and moves objects in that order
     * Saves the changed layers
     * Repaints if needed
     */
    public void calculateNextFrame()
    {
        timeElapsed += 100;
        frameCount++;
        somethingChanged = false;
        changedLayers.clear();

        updateAnimatedObjects();
        clearLayers();
        removeObjects();
        addObjects();
        moveObjects();

        for (Integer i : changedLayers)
            saveLayer(i);
        if (somethingChanged)
            repaint();
    }

    /**
     * Cycles animated objects to their next frame if necessary.
     */
    public void updateAnimatedObjects()
    {
        for (Integer frameDifference : animatedObjectsByFrameGap.keySet())
            if (frameCount % frameDifference == 0)
                for (AnimatedObject ao : animatedObjectsByFrameGap.get(frameDifference))
                {
                    ao.cycle();
                    changedLayers.add(ao.getZLayer());
                    somethingChanged = true;
                }
    }

    /**
     * Clears all layers in the layersToClear queue
     */
    public void clearLayers()
    {
        for (Integer z : layersToClear)
        {
            if (generatedPermaLayers.containsKey(z))
                generatedPermaLayers.remove(z);
            if (objectLayers.size() > z)
                objectLayers.get(z).clear();
        }
        layersToClear.clear();
    }

    /**
     * Removes all of the objects in the objectsToRemove queue from the screen
     */
    public void removeObjects()
    {
        for (OnScreenObject o : objectsToRemove)
        {
            objectLayers.get(o.getZLayer()).remove(o);
            changedLayers.add(o.getZLayer());
            somethingChanged = true;
        }
        objectsToRemove.clear();
    }

    /**
     * Adds of the objects in the objectsToAdd queue to the screen
     */
    public void addObjects()
    {
        for (OnScreenObject o : objectsToAdd)
        {
            addObject(o);
            changedLayers.add(o.getZLayer());
            somethingChanged = true;
        }
        objectsToAdd.clear();
    }

    /**
     * Moves all of the objects in the objects to move queue to their new location
     */
    public void moveObjects()
    {
        for (MoveObject mo : objectsToMove)
        {
            mo.applyMove();
            changedLayers.add(mo.getOnScreenObject().getZLayer());
            somethingChanged = true;
        }
        objectsToMove.clear();
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof Timer)
            calculateNextFrame();
    }

    /**
     * Stores the object that needs to be moved and where it needs to be moved to
     */
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


