import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * Created by needa_000 on 12/19/2014.
 */
public class MainWindow extends JFrame
{
    public MainWindow()
    {
        super("John The Bandit");
        JLabel label = new JLabel("It's working!");
        label.setFont(new Font("Times New Roman",Font.BOLD,48));
        add(label);
        startMusic();
        pack();
        setVisible(true);
    }

    private void startMusic()
    {
        String path = "JohnTheBandit/" + Constants.MUSIC_PATH + "a_song.mp3";
        File file = new File(path);
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