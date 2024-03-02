package de.exware.gwtswing.animation.trigger;

import de.exware.gwtswing.animation.Animation;
import de.exware.gwtswing.animation.TriggerEvent;
import de.exware.gwtswing.awt.event.GComponentAdapter;
import de.exware.gwtswing.awt.event.GComponentEvent;
import de.exware.gwtswing.swing.GComponent;

/**
 * Triggers the Animation if visibility is set too true
 * @author martin
 */
public class VisibilityOnTrigger extends TriggerEvent
{
    public static final VisibilityOnTrigger INSTANCE = new VisibilityOnTrigger();
    
    private VisibilityOnTrigger()
    {
    }
    
    @Override
    protected void install(Animation animation, GComponent comp)
    {
        GComponentAdapter adapter = new GComponentAdapter()
        {
            @Override
            public void componentShown(GComponentEvent e)
            {
                animation.enable(comp);
            }

            @Override
            public void componentHidden(GComponentEvent e)
            {
                animation.disable(comp);
            }
        };
        comp.addComponentListener(adapter);
        comp.putClientProperty("AnimationAdapter-" + getClass().getName(), adapter);
    }
    
    @Override
    protected void uninstall(Animation animation, GComponent comp)
    {
        GComponentAdapter adapter = (GComponentAdapter) comp.getClientProperty("AnimationAdapter-" + getClass().getName());
        comp.removeComponentListener(adapter);
    }
}
