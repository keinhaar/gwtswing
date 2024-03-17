package de.exware.gwtswing.animation;

import de.exware.gwtswing.animation.trigger.VisibilityOnTrigger;
import de.exware.gwtswing.swing.GComponent;

/**
 * Changes the Opacity of the Component from 0% to 100%
 * @author martin
 */
public class FadeInAnimation extends AbstractAnimation
{
    private static final String animationClass = "gwts-animation-fadeIn";

    public FadeInAnimation()
    {
        this(VisibilityOnTrigger.INSTANCE, 0.5f);
    }
    
    public FadeInAnimation(TriggerEvent evt, float duration)
    {
        super(evt, duration);
    }
    
    public FadeInAnimation(float duration)
    {
        this(VisibilityOnTrigger.INSTANCE, duration);
    }

    @Override
    public
    void enable(GComponent comp)
    {
        comp.getPeer().addClassName(animationClass);
    }
    
    @Override
    public
    void disable(GComponent comp)
    {
        comp.getPeer().removeClassName(animationClass);
    }
}
