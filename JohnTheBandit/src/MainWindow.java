import javax.swing.*;
import java.awt.*;

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
        pack();
        setVisible(true);
    }
}
