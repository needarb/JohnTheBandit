import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by needa_000 on 1/13/2015.
 */
public class CodeSetter extends JFrame implements FocusListener
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

    private JPanel selectionPanel;
    private List<JTextField> imageCodes;
    private int numSelectors = 0;
    private List<Image> images;
    private List<JPanel> selectors;
    public CodeSetter(List<Image> images)
    {
        this.images = images;
        selectionPanel = new JPanel();
        imageCodes = new ArrayList<JTextField>();
        selectors = new ArrayList<JPanel>();
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        addFocusListener(this);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
    }

    public void addSelector()
    {
        JPanel selector = new JPanel();
        selector.add(new JLabel("" + ++numSelectors));
        JTextField tf = new JTextField("A");
        tf.setInputVerifier(VERIFIER);
        tf.setPreferredSize(new Dimension(20, 20));
        selector.add(tf);
        imageCodes.add(tf);
        add(selector);
        selectors.add(selector);
    }

    public void removeSelector()
    {
        selectors.remove(selectors.size()-1);
        numSelectors--;
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        System.out.println("Focus gained" + images.size());
        while(numSelectors < images.size())
            addSelector();
        while(numSelectors < images.size())
        repaint();
    }


    @Override
    public void focusLost(FocusEvent e)
    {

    }

    public List<Character> getImageCodes()
    {
        List<Character> codes = new ArrayList<Character>();
        for(JTextField tf: imageCodes)
            codes.add(tf.getText().charAt(0));
        return codes;

    }
}
