package de.exware.gwtswing.swing;

import java.util.List;

import de.exware.gwtswing.PartitionedPanel;
import de.exware.gwtswing.awt.GBorderLayout;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;
import de.exware.gwtswing.swing.border.GBorderFactory;

public class GOptionPane extends GComponent
{
    //Message Types
    public static final int INFORMATION_MESSAGE = 1;
    public static final int ERROR_MESSAGE = 2;
    public static final int QUESTION_MESSAGE = 3;
    
    //OptionTypes
    public static final int OK_CANCEL_OPTION = 1;
    public static final int YES_NO_OPTION = 2;
    public static final int YES_NO_CANCEL_OPTION = 3;
    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    public static final int NO_OPTION = 2;
    public static final int YES_OPTION = OK_OPTION;
    
    private String title = "Message";
    private Object message;
    private int messageType;
    private int optionType;
    private boolean input;
    private GComponent inputField;
    private GOptionCallback callback;
    private GDialog dlg;
    List<String> selectableOptions;
    
    public GOptionPane(Object message, int messageType)
    {
        this(message, messageType, OK_OPTION);
    }
    
    private GOptionPane(Object message, int messageType, int optionType)
    {
        setLayout(new GBorderLayout(10, 10));
        this.message = message;
        this.messageType = messageType;
        this.optionType = optionType;
    }
    
    public static void showMessageDialog(GComponent parent, Object message)
    {
        GOptionPane pane = new GOptionPane(message, INFORMATION_MESSAGE);
        pane.createAndShow(parent);
    }
    
    public static void showMessageDialog(GComponent parent, Object message, String title,
        int messageType)
    {
        GOptionPane pane = new GOptionPane(message, messageType);
        pane.title = title;
        pane.createAndShow(parent);
    }
    
    public static void showInputDialog(GComponent parent, Object message, GOptionCallback callback)
    {
        GOptionPane pane = new GOptionPane(message, QUESTION_MESSAGE);
        pane.setCallback(callback);
        pane.setInput(true);
        pane.createAndShow(parent);
    }
    
    public static void showInputDialog(GComponent parent, Object message, String title, int optionType, GOptionCallback callback)
    {
        GOptionPane pane = new GOptionPane(message, QUESTION_MESSAGE, optionType);
        pane.setCallback(callback);
        pane.setInput(true);
        pane.title = title;
        pane.createAndShow(parent);
    }
    
    public static void showInputDialog(GComponent parent, Object message, String title, int optionType,
        List<String> selectableOptions, GOptionCallback callback)
    {
        GOptionPane pane = new GOptionPane(message, QUESTION_MESSAGE, optionType);
        pane.setCallback(callback);
        pane.setInput(true);
        pane.title = title;
        pane.selectableOptions = selectableOptions;
        pane.createAndShow(parent);
    }

    public static void showConfirmDialog(GComponent parent,
        Object message, String title, int optionType, GOptionCallback callback)
    {
        GOptionPane pane = new GOptionPane(message, QUESTION_MESSAGE, optionType);
        pane.setCallback(callback);
        pane.title = title;
        pane.createAndShow(parent);
    }
    
    private void setCallback(GOptionCallback callback)
    {
        this.callback = callback;
    }

    private void setInput(boolean b)
    {
        this.input = b;
    }

    private void createAndShow(GComponent parent)
    {
        init();
        GDialog dlg = createDialog(title);
        dlg.getContentPane().add(this);
        dlg.getContentPane().setBorder(GBorderFactory.createEmptyBorder(10, 10, 10, 10));
        dlg.pack();
        dlg.setLocationRelativeTo(parent);
        GPoint loc = dlg.getLocation();
        if(loc.x < 0)
        {
            loc.x = 0;
        }
        if(loc.y < 0)
        {
            loc.y = 0;
        }
        GDimension size = dlg.getSize();
        dlg.setBounds(loc.x, loc.y, size.width, size.height);
        dlg.show();
    }
    
    private void init()
    {
        GComponent messageComp;
        if(message instanceof GComponent)
        {
            messageComp = (GComponent) message;
        }
        else
        {
            messageComp = new GLabel("" + message);
        }
        GPanel panel = new GPanel();
        panel.setLayout(new GGridBagLayout());
        GGridBagConstraints gbc = new GGridBagConstraints();
        add(panel, GBorderLayout.CENTER);
        panel.add(messageComp, gbc);
        if(input)
        {
            if(selectableOptions == null)
            {
                inputField = new GTextField(20); 
            }
            else
            {
                GComboBox<String> combo = new GComboBox<>();
                for(int i=0;i<selectableOptions.size();i++)
                {
                    combo.addItem(selectableOptions.get(i));
                }
                inputField = combo;
            }
            gbc.gridy++;
            panel.add(inputField, gbc);
        }
        PartitionedPanel buttons = new PartitionedPanel(4);
        buttons.setIndentSize(0);
        String okText = GUIManager.getString("OK.text");
        if(optionType == YES_NO_CANCEL_OPTION || optionType == YES_NO_OPTION)
        {
            okText = GUIManager.getString("YES.text");;
        }
        GButton ok = new GButton(okText);
        buttons.add(ok);
        ok.addActionListener(new GActionListener()
        {
            @Override
            public void actionPerformed(GActionEvent evt)
            {
                String input = null;
                if(GOptionPane.this.input)
                {
                    if(selectableOptions == null)
                    {
                        input = ((GTextField)inputField).getText();
                    }
                    else
                    {
                        input = (String) ((GComboBox)inputField).getSelectedItem();
                    }
                }
                dlg.setVisible(false);
                if(callback != null)
                {
                    callback.execute(OK_OPTION, input);
                }
            }
        });
        if(optionType == YES_NO_OPTION 
            || optionType == YES_NO_CANCEL_OPTION)
        {
            String noText = GUIManager.getString("NO.text");
            GButton no = new GButton(noText);
            buttons.add(no);
            no.addActionListener(new GActionListener()
            {
                @Override
                public void actionPerformed(GActionEvent evt)
                {
                    dlg.setVisible(false);
                    if(callback != null)
                    {
                        callback.execute(NO_OPTION, null);
                    }
                }
            });
        }
        if(optionType == YES_NO_CANCEL_OPTION 
            || optionType == OK_CANCEL_OPTION)
        {
            String cancelText = GUIManager.getString("CANCEL.text");
            GButton cancel = new GButton(cancelText);
            buttons.add(cancel);
            cancel.addActionListener(new GActionListener()
            {
                @Override
                public void actionPerformed(GActionEvent evt)
                {
                    dlg.setVisible(false);
                    if(callback != null)
                    {
                        callback.execute(CANCEL_OPTION, null);
                    }
                }
            });
        }
        add(buttons, GBorderLayout.SOUTH);
    }

    public GDialog createDialog(String title)
    {
        dlg = new GDialog();
        dlg.setModal(true);
        dlg.setTitle(title);
        return dlg;
    }
    
    public interface GOptionCallback
    {
        public void execute(int option, String input);
    }

}
