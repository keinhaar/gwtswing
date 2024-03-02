package de.exware.gwtswing.animation.trigger;

import de.exware.gwtswing.animation.Animation;
import de.exware.gwtswing.animation.TriggerEvent;
import de.exware.gwtswing.awt.event.GComponentAdapter;
import de.exware.gwtswing.awt.event.GComponentEvent;
import de.exware.gwtswing.swing.GComponent;

/**
 * Triggers the Animation if visibility is changed
 * @author martin
 */
public class VisibilityTrigger extends TriggerEvent
{
    public static final VisibilityTrigger INSTANCE = new VisibilityTrigger();
    
    private VisibilityTrigger()
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
                animation.disable(comp);
                animation.enable(comp);
            }

            @Override
            public void componentHidden(GComponentEvent e)
            {
                animation.disable(comp);
                animation.enable(comp);
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
