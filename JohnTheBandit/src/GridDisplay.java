import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Created by needa_000 on 1/13/2015.
 */
public class GridDisplay extends JPanel
{
    private int tileSize;
    private Image[][] grid;
    private BufferedImage backBuffer;

    public GridDisplay(int tileSize, Image[][] grid)
    {
        this.tileSize = tileSize;
        this.grid = grid;
        backBuffer = new BufferedImage(grid.length * tileSize, grid.length * tileSize, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paint(Graphics g)
    {
        backBuffer.getGraphics().clearRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());
        for (int x = 0; x < grid.length; x++)
        {
            for (int y = 0; y < grid[0].length; y++)
            {
                if (grid[x][y] != null)
                    backBuffer.getGraphics().drawImage(grid[x][y], x * tileSize, y * tileSize, null);
            }
        }
        g.drawImage(backBuffer, 0, 0, null);
    }

}

