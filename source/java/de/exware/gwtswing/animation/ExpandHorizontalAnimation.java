package de.exware.gwtswing.animation;

import de.exware.gwtswing.swing.GComponent;

public class ExpandHorizontalAnimation extends AbstractAnimation
{
    private static final String animationClass = "gwts-animation-expandHorizontal";

    public ExpandHorizontalAnimation()
    {
        this(TriggerEvent.VISIBILITY, 0.5f);
    }
    
    public ExpandHorizontalAnimation(TriggerEvent evt, float duration)
    {
        super(evt, duration);
    }
    
    @Override
    void enable(GComponent comp)
    {
        comp.getPeer().addClassName(animationClass);
    }
    
    @Override
    void disable(GComponent comp)
    {
        comp.getPeer().removeClassName(animationClass);
    }
}
