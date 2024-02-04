package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GToolkit;
import de.exware.gwtswing.awt.GWindow;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GKeyEvent;

public class GDialog extends GWindow
{
    private GDialogCallback callback;
    private GAbstractButton defaultButton;
    private GAWTEventListener listener;
    private boolean isModal = false;
    
    public GDialog()
    {
        this((GFrame)null, (GDialogCallback)null);
    }

    public GDialog(GDialogCallback callback)
    {
        this((GWindow)null, callback);
    }

    public GDialog(GWindow parent, GDialogCallback callback)
    {
        this(parent, null, callback);
    }

    public GDialog(GWindow parent, String title, GDialogCallback callback)
    {
        this(parent, title, false, callback);
    }

    public GDialog(GWindow parent, String title)
    {
        this(parent, title, false, null);
    }

    public GDialog(GWindow parent, String title, boolean modal)
    {
        this(parent, title, modal, null);
    }

    public GDialog(GWindow parent, String title, boolean modal, GDialogCallback callback)
    {
        this.callback = callback;
        setTitle(title);
        setModal(modal);
    }

    public void setModal(boolean modal)
    {
        if(modal != isModal)
        {
            isModal = modal;
        }
    }
    
    protected void callback()
    {
        callback.execute(this);
    }

    public void dispose()
    {
        setVisible(false);
    }
    
    public void hide()
    {
        setVisible(false);
    }
    
    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if(visible == false && callback != null)
        {
            callback();
        }
        if(visible)
        {
            setModalComponent(this);
            listener = new GAWTEventListener()
            {
                @Override
                public void eventDispatched(GAWTEvent event)
                {
                    if(defaultButton != null)
                    {
                        GKeyEvent evt = (GKeyEvent) event;
                        if(evt.getId() == GKeyEvent.KEY_RELEASED)
                        {
                            int kc1 = evt.getKeyCode();
                            int kc2 = GKeyEvent.VK_ENTER;
                            if(kc1 == kc2)
                            {
                                defaultButton.doClick();
                            }
                        }
                    }
                }
            };
            GToolkit.getDefaultToolkit().addAWTEventListener(listener, GAWTEvent.KEY_EVENT_MASK);
            GUtilities.focusFirstField(this);
        }
        else
        {
            GToolkit.getDefaultToolkit().removeAWTEventListener(listener);            
            removeModalComponent();
        }
    }
 
    public GAbstractButton getDefaultButton()
    {
        return defaultButton;
    }

    public void setDefaultButton(GAbstractButton defaultButton)
    {
        defaultButton.getPeer().addClassName("gwts-GDialog-defaultButton");
        this.defaultButton = defaultButton;
    }

    public void setCallback(GDialogCallback callback)
    {
        this.callback = callback;
    }
    
}
