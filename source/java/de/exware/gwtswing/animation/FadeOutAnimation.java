package de.exware.gwtswing.animation;

import de.exware.gwtswing.animation.trigger.VisibilityOnTrigger;
import de.exware.gwtswing.swing.GComponent;

/**
 * Changes the Opacity of the Component from 100% to 0%
 * @author martin
 */
public class FadeOutAnimation extends AbstractAnimation
{
    private static final String animationClass = "gwts-animation-fadeOut";

    public FadeOutAnimation()
    {
        this(VisibilityOnTrigger.INSTANCE, 0.5f);
    }
    
    public FadeOutAnimation(TriggerEvent evt, float duration)
    {
        super(evt, duration);
    }
    
    @Override
    public
    void enable(GComponent comp)
    {
        if(comp.isVisible() == false)
        {
            comp.getPeer().addClassName(animationClass);
        }
    }
    
    @Override
    public
    void disable(GComponent comp)
    {
        comp.getPeer().removeClassName(animationClass);
    }
}
