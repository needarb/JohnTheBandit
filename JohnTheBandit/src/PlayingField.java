import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * The playing field grid. Interfaces with the engine to display the game
 * Created by needa_000 on 1/7/2015.
 */
public class PlayingField implements KeyListener
{
    public static final int BACKGROUND = 0;
    public static final int CHARACTERS = 1;
    public static final int OVERLAY = 2;

    private Engine engine;
    private ImageLoader imageLoader;
    private int tileWidth = 30;
    private int tileHeight = 30;
    private NonAnimatedObject[][] backgroundTiles;
    private ArrayList<Actor> actors;
    private Map<Character, Image> images = new HashMap<Character, Image>();

    public PlayingField()
    {
        this.engine = new Engine();
        this.imageLoader = new ImageLoader();
        this.actors = new ArrayList<Actor>();
        loadMap("default.map");
        Thread t = new Thread(engine);
        t.start();
    }

    /**
     * Sets the image for the tile at the given grid location
     *
     * @param x Grid x
     * @param y Grid y
     * @param b The new image
     */
    public void setTile(int x, int y, BufferedImage b)
    {
        engine.queueRemoveObject(x, y, BACKGROUND);
        engine.queueAddObject(new NonAnimatedObject(b, x, y, BACKGROUND));
    }

    /**
     * Load the map with the given name from the map folder
     *
     * @param name Name of map with the .map file extension
     */
    public void loadMap(String name)
    {
        Map<String, Character> imageNames = new HashMap<String, Character>();
        List<String> rows = new LinkedList<String>();
        Scanner parser = null;
        try
        {
            parser = new Scanner(new FileInputStream("JohnThebandit/" + Constants.MAPS_PATH + name));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Map not found");
            return;
        }
        while (parser.hasNextLine())
        {
            String line = parser.nextLine().replace(" ", "");
            if (line.equals(""))
                continue;
            if (line.charAt(0) == '#')
            {
                int colon = line.indexOf(':');
                if (colon == -1)
                    continue;
                imageNames.put(line.substring(1, colon), line.charAt(colon + 1));
            }
            else
                rows.add(line);
        }
        images.clear();
        for (String imageName : imageNames.keySet())
        {
            images.put(imageNames.get(imageName), imageLoader.loadTile(imageName, tileWidth, tileHeight));
        }
        backgroundTiles = new NonAnimatedObject[rows.size()][rows.get(0).length()];
        ArrayList<NonAnimatedObject> toAdd = new ArrayList<NonAnimatedObject>(backgroundTiles.length * backgroundTiles[0].length);
        for (int row = 0; row < backgroundTiles.length; row++)
        {
            for (int col = 0; col < backgroundTiles[0].length; col++)
            {
                char c = rows.get(row).charAt(col);
                backgroundTiles[row][col] = new NonAnimatedObject(images.get(c), row * tileWidth, col * tileHeight, BACKGROUND);
                toAdd.add(backgroundTiles[row][col]);
            }
        }
        engine.queueClearLayer(BACKGROUND);
        engine.queueAddManyObjects(toAdd);
    }

    public void addActor(String imageName)
    {
        addActor(imageName, 0, 0);
    }

    /**
     * Creates an actor and adds it to the grid
     *
     * @param imageName Image for the actor
     * @param x         Actor's grid x
     * @param y         Actor's grid y
     */
    public void addActor(String imageName, int x, int y)
    {
        System.out.println("Adding actor");
        Actor a = new Actor(this, x, y, imageLoader.loadActor(imageName, tileWidth, tileHeight));
        System.out.println("Adding actor's image to screen");
        engine.queueAddObject(a.getOnScreenObject());
        actors.add(a);
    }

    public JPanel getDisplay()
    {
        return engine;
    }

    public Engine getEngine()
    {
        return engine;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public ImageLoader getImageLoader()
    {
        return imageLoader;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getKeyChar() == 'a')
            for (Actor a : actors)
                a.setLocation(a.getX() - 1, a.getY());
        if (e.getKeyChar() == 'd')
            for (Actor a : actors)
                a.setLocation(a.getX() + 1, a.getY());
        if (e.getKeyChar() == 'w')
            for (Actor a : actors)
                a.setLocation(a.getX(), a.getY() - 1);
        if (e.getKeyChar() == 's')
            for (Actor a : actors)
                a.setLocation(a.getX(), a.getY() + 1);
        if (e.getKeyChar() == 'o')
            addActor("jokhn.png", 4, 4);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
