package de.exware.gwtswing.animation;

import java.util.HashMap;
import java.util.Map;

import de.exware.gwtswing.PartitionedPanel;
import de.exware.gwtswing.awt.GToolkit;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GComponentAdapter;
import de.exware.gwtswing.awt.event.GComponentEvent;
import de.exware.gwtswing.awt.event.GContainerEvent;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GUtilities;

abstract public class AbstractAnimation implements Animation
{
    private static int maxId;
    private static Map<String, GAWTEventListener> listeners = new HashMap<>();
    private int id = maxId++;
    private TriggerEvent trigger = TriggerEvent.NONE;
    private float duration = 1;
    
    public AbstractAnimation()
    {
    }

    public AbstractAnimation(TriggerEvent trigger)
    {
        this.trigger = trigger;
    }

    public AbstractAnimation(TriggerEvent trigger, float duration)
    {
        this(trigger);
        this.duration = duration;
    }

    @Override
    public String getId()
    {
        return "" + id;
    }
    
    /**
     * Installs the Animation to any newly created instance of clazz.
     * @param clazz
     * @param animation
     */
    public static void installGlobally(Class<? extends GComponent> clazz, Animation animation)
    {
        String key = clazz.getName() + animation.getId();
        GAWTEventListener listener = listeners.get(key);
        if(listener == null)
        {
            listener = new GAWTEventListener()
            {
                @Override
                public void eventDispatched(GAWTEvent event)
                {
                    if(event instanceof GContainerEvent)
                    {
                        GComponent comp = (GComponent) event.getSource();
                        if(GUtilities.instanceOf(comp, clazz))
                        {
                            animation.install(comp);
                        }
                    }
                }
            };
            listeners.put(key, listener);
            GToolkit.getDefaultToolkit().addAWTEventListener(listener, GAWTEvent.CONTAINER_EVENT_MASK);
        }
    }

    /**
     * Stops installing the Animation to any newly created instance of clazz.
     * Be aware, that this DOES NOT remove the animation from already created instances.
     * @param clazz
     * @param animation
     */
    public static void stopInstallGlobally(Class<PartitionedPanel> clazz, Animation animation)
    {
        String key = clazz.getName() + animation.getId();
        GAWTEventListener listener = listeners.get(key);
        if(listener == null)
        {
            GToolkit.getDefaultToolkit().removeAWTEventListener(listener);
        }
    }
    
    @Override
    public TriggerEvent getTriggerEvent()
    {
        return trigger;
    }
    
    @Override
    public void install(GComponent comp)
    {
        TriggerEvent trigger = getTriggerEvent();
        if(TriggerEvent.VISIBILITY.equals(trigger))
        {
            GComponentAdapter adapter = new GComponentAdapter()
            {
                @Override
                public void componentShown(GComponentEvent e)
                {
                    enable(comp);
                }

                @Override
                public void componentHidden(GComponentEvent e)
                {
                    disable(comp);
                }
            };
            comp.addComponentListener(adapter);
            comp.putClientProperty("AnimationAdapter-" + getId(), adapter);
        }
        float duration = getAnimationDuration();
        comp.getPeer().getStyle().setProperty("animationDuration", duration + "s");
        enable(comp);
    }
    
    @Override
    public void uninstall(GComponent comp)
    {
        TriggerEvent trigger = getTriggerEvent();
        if(TriggerEvent.VISIBILITY.equals(trigger))
        {
            GComponentAdapter adapter = (GComponentAdapter) comp.getClientProperty("AnimationAdapter-" + getId());
            comp.removeComponentListener(adapter);
        }
    }
    
    abstract void enable(GComponent comp);
    abstract void disable(GComponent comp);

    @Override
    public float getAnimationDuration()
    {
        return duration;
    }

    public void setAnimationDuration(float seconds)
    {
        duration = seconds;
    }
}
