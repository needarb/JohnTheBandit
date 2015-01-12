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
    public static void main(String[] args)
    {
        MainWindow m = new MainWindow();
    }
    public MainWindow()
    {
        super("John The Bandit");
        PlayingField pf = new PlayingField();
        addKeyListener(pf);
        add(pf.getDisplay());
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startMusic()
    {
        String path = "JohnTheBandit/" + Constants.SOUNDS_MUSIC_PATH + "a_song.mp3";
        PlayMusic player = new PlayMusic(path);
        Thread t = new Thread(player);
        t.start();

    }
}
