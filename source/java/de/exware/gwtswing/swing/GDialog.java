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
    
    public GDialog()
    {
    }

    public GDialog(GDialogCallback callback)
    {
        this.callback = callback;
    }

    public void setModal(boolean modal)
    {
        
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
        if(visible == false)
        {
            GToolkit.getDefaultToolkit().removeAWTEventListener(listener);            
        }
        else
        {
            listener = new GAWTEventListener()
            {
                @Override
                public void eventDispatched(GAWTEvent event)
                {
                    if(event instanceof GKeyEvent && defaultButton != null)
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
            GToolkit.getDefaultToolkit().addAWTEventListener(listener, 0);
            GUtilities.focusFirstField(this);
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
