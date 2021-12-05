package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;

public class GButtonGroup
{
    private static int groupCounter;
    private String group;
    private List<GAbstractButton> buttons = new ArrayList<>();
    private ButtonGroupListener listener = new ButtonGroupListener();
    
    public GButtonGroup()
    {
        group = "" + groupCounter++;
    }
    
    public void add(GAbstractButton bt)
    {
        buttons.add(bt);
        if(bt instanceof GRadioButton)
        {
            ((GRadioButton)bt).setButtonGroup(group);
        }
        else if(bt instanceof GToggleButton)
        {
            bt.addActionListener(listener);
        }
    }
    
    class ButtonGroupListener implements GActionListener
    {
        @Override
        public void actionPerformed(GActionEvent evt)
        {
            GAbstractButton source = (GAbstractButton) evt.getSource();
            for(int i=0;i<buttons.size();i++)
            {
                GAbstractButton b = buttons.get(i);
                if(b != source)
                {
                    b.setSelected(false);
                }
            }
        }
    }
}
