package de.exware.gwtswing.animation;

import de.exware.gwtswing.swing.GComponent;

/**
 * Event that Triggers the Animation 
 * @author martin
 */
abstract public class TriggerEvent
{
    public static final TriggerEvent NONE = new TriggerEvent() 
    {
        @Override
        protected void install(Animation animation, GComponent comp)
        {
        }
        @Override
        protected void uninstall(Animation animation, GComponent comp)
        {
        }
    };

    protected abstract void install(Animation animation, GComponent comp);

    protected abstract void uninstall(Animation animation, GComponent comp);
}