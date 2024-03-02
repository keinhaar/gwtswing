package de.exware.gwtswing.animation;

import de.exware.gwtswing.animation.trigger.VisibilityOnTrigger;
import de.exware.gwtswing.swing.GComponent;

/**
 * Expands the Component from zero height to normal height
 * @author martin
 */
public class ExpandVerticalAnimation extends AbstractAnimation
{
    private static final String animationClass = "gwts-animation-expandVertical";

    public ExpandVerticalAnimation()
    {
        this(VisibilityOnTrigger.INSTANCE, 0.5f);
    }
    
    public ExpandVerticalAnimation(TriggerEvent evt, float duration)
    {
        super(evt, duration);
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
