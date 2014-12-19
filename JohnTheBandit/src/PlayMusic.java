import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by needa_000 on 12/19/2014.
 */
public class PlayMusic implements Runnable
{
    private String filePath;
    public PlayMusic(String filePath)
    {
        this.filePath = filePath;
    }
    @Override
    public void run()
    {
        File file = new File(filePath);
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            Player player = new Player(fis);
            player.play();
        } catch (JavaLayerException e)
        {
            e.printStackTrace();
        }
    }
}
