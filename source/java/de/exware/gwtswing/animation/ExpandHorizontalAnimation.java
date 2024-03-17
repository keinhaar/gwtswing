package de.exware.gwtswing.animation;

import de.exware.gwtswing.animation.trigger.VisibilityOnTrigger;
import de.exware.gwtswing.swing.GComponent;

/**
 * Expands the Component from zero width to normal width
 * @author martin
 */
public class ExpandHorizontalAnimation extends AbstractAnimation
{
    private static final String animationClass = "gwts-animation-expandHorizontal";

    public ExpandHorizontalAnimation()
    {
        this(VisibilityOnTrigger.INSTANCE, 0.5f);
    }
    
    public ExpandHorizontalAnimation(TriggerEvent evt, float duration)
    {
        super(evt, duration);
    }
    
    public ExpandHorizontalAnimation(float duration)
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
