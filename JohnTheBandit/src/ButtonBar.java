import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by needa_000 on 1/13/2015.
 */
public class ButtonBar extends JPanel implements ActionListener
{
    static
    {
        VERIFIER = new InputVerifier()
        {
            @Override
            public boolean verify(JComponent input)
            {
                JTextField tf = (JTextField) input;
                if(tf.getText().length() == 0)
                    tf.setText("A");
                else
                    tf.setText("" + tf.getText().charAt(0));
                return true;
            }
        };
    }
    private static InputVerifier VERIFIER;
    private MapMaker mapMaker;
    private JButton saveButton;
    private JButton newMapButton;

    public ButtonBar(MapMaker mapMaker)
    {
        this.mapMaker = mapMaker;
        this.saveButton = new JButton("Save Map");
        saveButton.addActionListener(this);
        this.newMapButton = new JButton("New Map");
        newMapButton.addActionListener(this);
        this.setLayout(new FlowLayout());
        this.add(newMapButton);
        this.add(saveButton);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == saveButton)
        {
            deploySaveWindow();
        }
        else if (e.getSource() == newMapButton)
        {

        }
        else
        {
            if(imageCodes == null)
                return;
            List<Character> codes = new ArrayList<Character>();
            for(JTextField tf: imageCodes)
                codes.add(tf.getText().charAt(0));
            mapMaker.saveMap(codes,name.getText().replace(" ","_") + ".map");
        }
    }

    private JTextField name;
    private List<JTextField> imageCodes;
    private JFrame saveWindow = new JFrame();
    private void deploySaveWindow()
    {
        saveWindow = new JFrame();
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel,BoxLayout.Y_AXIS));
        List<JPanel> selectors = new ArrayList<JPanel>();
        imageCodes = new ArrayList<JTextField>();
        int num =1;
        char currentchar = 'A';
        for(Image i: mapMaker.getImages())
        {
            JPanel selector = new JPanel();
            selector.add(new JLabel("" + ++num));
            JTextField tf = new JTextField("" + currentchar);
            tf.setInputVerifier(VERIFIER);
            tf.setPreferredSize(new Dimension(20, 20));
            selector.add(tf);
            imageCodes.add(tf);
            selectors.add(selector);
            num++;
            currentchar = (char) (currentchar + 1);
            savePanel.add(selector);
            System.out.println(currentchar);
        }
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name: "));
        name = new JTextField("newmap");
        namePanel.add(name);
        savePanel.add(namePanel);
        JButton finalSave = new JButton("Save");
        finalSave.addActionListener(this);
        savePanel.add(finalSave);
        saveWindow.add(savePanel);
        saveWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        saveWindow.pack();
        saveWindow.setVisible(true);


    }
}
