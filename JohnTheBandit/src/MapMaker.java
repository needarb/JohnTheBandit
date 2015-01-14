import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by needa_000 on 1/13/2015.
 */
public class MapMaker extends JFrame implements DropTargetListener, MouseListener
{
    public static final int TILE_SIZE = 50;
    public static void main(String[] args)
    {
      MapMaker m = new MapMaker();
    }


    private ImageDisplay imageDisplay;
    private GridDisplay gridDisplay;
    private List<Image> images;
    private List<String> imageNames;
    private Image[][] grid;
    private CodeSetter codeSetter;
    public MapMaker()
    {
        images =  new ArrayList<Image>();
        imageNames = new ArrayList<String>();
        imageDisplay = new ImageDisplay(images);
        grid = new Image[20][20];
        gridDisplay = new GridDisplay(TILE_SIZE,grid);
        gridDisplay.addMouseListener(this);
        setLayout(new BorderLayout());
        add(imageDisplay, BorderLayout.WEST);
        add(gridDisplay, BorderLayout.CENTER);
        ButtonBar buttonBar = new ButtonBar(this);
        add(buttonBar,BorderLayout.NORTH);
        setDropTarget(new DropTarget(this, this));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dragExit(DropTargetEvent dte)
    {

    }

    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        Transferable transfer = dtde.getTransferable();
        if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
        {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            List objects = null;
            try
            {
                objects = (List) transfer.getTransferData(DataFlavor.javaFileListFlavor);
            } catch (UnsupportedFlavorException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            for (Object object : objects)
            {
                if (object instanceof File)
                {
                    File source = (File) object;
                    System.out.println(source.getAbsolutePath());

                    try
                    {
                        images.add(ImageIO.read(source).getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH));
                        imageNames.add(source.getAbsolutePath().substring(source.getAbsolutePath().lastIndexOf('\\')));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        System.err.println("Unable to read image");
                    }
                }
            }

        }
        repaint();
    }

    public void saveMap(List<Character> codes,String name)
    {
        System.out.println("Saving map");
        String out = "";
        HashMap<Image,Character> charMap = new HashMap<Image, Character>();
        for (int i = 0; i < imageNames.size(); i++)
        {
            out = out + ("# " + imageNames.get(i) + " : " + codes.get(i)) + System.lineSeparator();
            charMap.put(images.get(i),codes.get(i));
            charMap.put(null,' ');
        }
        for (int x = 0; x < grid.length; x++)
        {
            for (int y = 0; y < grid[0].length; y++)
            {
                out += charMap.get(grid[x][y]);
            }
            out += System.lineSeparator();
        }
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("name"), "utf-8"));
            writer.write(out);


        } catch (IOException ex) {
            System.err.println("Unable to save map");
        } finally {
            try {writer.close();} catch (Exception ex) {}
        }
        System.out.println("Map saved");
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(imageDisplay.getSelectedImage() == null)
            return;

        int convertedX = e.getX() / TILE_SIZE;
        int convertedY = e.getY() / TILE_SIZE;

        if(convertedX >= grid.length || convertedY >= grid[0].length)
            return;
        grid[convertedX][convertedY] = imageDisplay.getSelectedImage();
        repaint();
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


    public List<Image> getImages()
    {
        return images;
    }
}
