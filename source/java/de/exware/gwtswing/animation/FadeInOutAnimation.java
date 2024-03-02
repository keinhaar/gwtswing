package de.exware.gwtswing.animation;

import de.exware.gwtswing.animation.trigger.VisibilityTrigger;
import de.exware.gwtswing.swing.GComponent;

/**
 * Changes the Opacity of the Component from 0% to 100% if visibility is set too true, and 
 * from 100% to 0% if visibility is set to false
 * @author martin
 */
public class FadeInOutAnimation extends AbstractAnimation
{
    private static final String animationClass = "gwts-animation-fade";

    public FadeInOutAnimation()
    {
        this(VisibilityTrigger.INSTANCE, 0.5f);
    }
    
    public FadeInOutAnimation(TriggerEvent evt, float duration)
    {
        super(evt, duration);
    }
    
    @Override
    public
    void enable(GComponent comp)
    {
        comp.getPeer().addClassName(animationClass + (comp.isVisible() ? "In" : "Out"));
    }
    
    @Override
    public
    void disable(GComponent comp)
    {
        comp.getPeer().removeClassName(animationClass + (!comp.isVisible() ? "In" : "Out"));
    }
}
