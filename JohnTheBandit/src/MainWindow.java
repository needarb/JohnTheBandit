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
        System.out.println("Started");
        pack();
        setVisible(true);
    }

    private void startMusic()
    {
        String path = "JohnTheBandit/" + Constants.MUSIC_PATH + "a_song.mp3";
        PlayMusic player = new PlayMusic(path);
        Thread t = new Thread(player);
        t.start();

    }
}
