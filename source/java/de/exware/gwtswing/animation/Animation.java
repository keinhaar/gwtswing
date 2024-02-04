package de.exware.gwtswing.animation;

import de.exware.gwtswing.swing.GComponent;

public interface Animation
{
    public enum TriggerEvent
    {
        NONE
        , VISIBILITY
    }
    
    /**
     * Liefert eine eindeutige ID.
     * @return
     */
    public String getId();

    /**
     * Animation Duration
     * @return
     */
    public float getAnimationDuration();

    /**
     * Installiert die Animation auf der GComponent.
     * @param comp
     */
    public void install(GComponent comp);
    
    /**
     * Removes the Animation from the GComponent.
     * @param comp
     */
    public void uninstall(GComponent comp);
    
    public TriggerEvent getTriggerEvent();
}
