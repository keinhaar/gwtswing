package de.exware.gwtswing.animation;

import de.exware.gwtswing.swing.GComponent;

/**
 * Interface for Animations of GComponents
 * @author martin
 *
 */
public interface Animation
{
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

    /**
     * Used internally to trigger animation.
     * Do not call manually!
     * @param comp
     */
    public void enable(GComponent comp);

    /**
     * Used internally to trigger animation
     * Do not call manually!
     * @param comp
     */
    public void disable(GComponent comp);
}
