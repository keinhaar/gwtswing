package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPStyle;
import de.exware.gplatform.GPlatform;
import de.exware.gplatform.event.GPEvent;
import de.exware.gplatform.event.GPEvent.Type;
import de.exware.gplatform.event.GPEventListener;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GCursor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.GFontMetrics;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.GLayoutManager;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.GRectangle;
import de.exware.gwtswing.awt.GToolkit;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GComponentEvent;
import de.exware.gwtswing.awt.event.GComponentListener;
import de.exware.gwtswing.awt.event.GContainerEvent;
import de.exware.gwtswing.awt.event.GContainerListener;
import de.exware.gwtswing.awt.event.GFocusEvent;
import de.exware.gwtswing.awt.event.GFocusListener;
import de.exware.gwtswing.awt.event.GKeyEvent;
import de.exware.gwtswing.awt.event.GKeyListener;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseListener;
import de.exware.gwtswing.awt.event.GMouseMotionListener;
import de.exware.gwtswing.awt.event.GMouseWheelEvent;
import de.exware.gwtswing.awt.event.GMouseWheelListener;
import de.exware.gwtswing.awt.event.GTouchEvent;
import de.exware.gwtswing.awt.event.GTouchListener;
import de.exware.gwtswing.swing.border.GBorder;
import de.exware.gwtswing.swing.plaf.ComponentUI;

public class GComponent
{
    private static final long LONG_CLICK_TIME = 500;
    private GPElement peer;
    private List<GComponent> components;
    private GLayoutManager layout;
    private GComponent parent;
    private GBorder border;
    private GFont font;
    private GDimension explizitPreferredSize;
    private GDimension preferredSize;
    private String tooltip;
    private SingleEventListener eventListener;
    protected List<GMouseListener> mouseListeners;
    private List<GMouseWheelListener> mouseWheelListeners;
    private List<GTouchListener> touchListeners;
    private List<GMouseMotionListener> mouseMotionListeners;
    private List<GKeyListener> keyListeners;
    private List<GComponentListener> componentListeners;
    private List<GContainerListener> containerListeners;
    private List<GFocusListener> focusListeners;
    private Map<Object, Object> clientProperties;
    private int maxWidthForPreferredSize = Integer.MAX_VALUE;
    private String name;
    private boolean enabled = true;
    private boolean visible = true;
    private GDimension size = new GDimension();
    private static boolean isLongClickEnabled = true;
    private static long longClickStart;
    private static boolean longClickPerformed;
    private static GPoint lastTouchStart;
    private static Map<Class, GInsets> cssBorderCache = new HashMap<>();
    /**
     * A Stack of modal components (Most of the time GDialogs). If the Stack is not empty, then only
     * Components inside the top most Stack element will process events.
     */
    private static Stack<GComponent> modalityStack = new Stack<>();
    private boolean processEvent = true;
    /**
     * If true, all mouseEvent Propagation is stopped on this Component.
     */
    private boolean stopMousePropagation = true;
    private ComponentUI componentUI;

    public GComponent()
    {
        this(GPlatform.getDoc().createDivElement());
    }

    protected GComponent(GPElement peer)
    {
        setPeer(peer);
        setClassNames(peer, "");
    }
    
    protected void setModalComponent(GComponent comp)
    {
        modalityStack.push(comp);
    }
    
    protected void removeModalComponent()
    {
        if(modalityStack.isEmpty() == false)
        {
            modalityStack.pop();
        }
    }
    
    /**
     * Used to determine if the component is able to handle long clicks.
     * @return
     */
    protected boolean supportsLongClick()
    {
        return true;
    }
    
    protected void setClassNames(GPElement el, String additionalName)
    {
        el.setClassName(additionalName);
        Class cl = getClass();
        while(cl != GComponent.class)
        {
            el.addClassName("gwts-" + cl.getSimpleName());
            cl = cl.getSuperclass();
        }
        el.addClassName("gwts-" + cl.getSimpleName());
    }

    public void addComponentListener(GComponentListener listener)
    {
        if(componentListeners == null)
        {
            componentListeners = new ArrayList<>();
        }
        if(componentListeners.contains(listener) == false)
        {
            componentListeners.add(listener);
        }
    }
    
    public void addContainerListener(GContainerListener listener)
    {
        if(containerListeners == null)
        {
            containerListeners = new ArrayList<>();
        }
        if(containerListeners.contains(listener) == false)
        {
            containerListeners.add(listener);
        }
    }
    
    public void removeComponentListener(GComponentListener listener)
    {
        if(componentListeners != null)
        {
            componentListeners.remove(listener);
        }
    }
    
    public void removeContainerListener(GContainerListener listener)
    {
        if(containerListeners != null)
        {
            containerListeners.remove(listener);
        }
    }
    
    public void addKeyListener(GKeyListener listener)
    {
        if(keyListeners == null)
        {
            keyListeners = new ArrayList<>();
            initEventListener(GPEvent.Type.ONKEYDOWN
                , GPEvent.Type.ONKEYPRESS
                , GPEvent.Type.ONKEYUP);
        }
        if(keyListeners.contains(listener) == false)
        {
            keyListeners.add(listener);
        }
    }
    
    public void addFocusListener(GFocusListener listener)
    {
        if(focusListeners == null)
        {
            focusListeners = new ArrayList<>();
            initEventListener(GPEvent.Type.ONFOCUS);
        }
        if(focusListeners.contains(listener) == false)
        {
            focusListeners.add(listener);
        }
    }
    
    public void removeKeyListener(GKeyListener listener)
    {
        if(keyListeners != null)
        {
            keyListeners.remove(listener);
        }
    }
    
    public void removeTouchListener(GTouchListener listener)
    {
        if(touchListeners != null)
        {
            touchListeners.remove(listener);
        }
    }
    
    public void addMouseListener(GMouseListener listener)
    {
        if(mouseListeners == null)
        {
            mouseListeners = new ArrayList<>();
            initEventListener(GPEvent.Type.ONMOUSEOVER
                , GPEvent.Type.ONMOUSEOUT
                , GPEvent.Type.ONCLICK
                , GPEvent.Type.ONMOUSEDOWN
                , GPEvent.Type.ONMOUSEUP 
                , GPEvent.Type.ONDBLCLICK 
                , GPEvent.Type.ONCONTEXTMENU);
        }
        if(mouseListeners.contains(listener) == false)
        {
            mouseListeners.add(listener);
        }
    }
    
    public void addMouseWheelListener(GMouseWheelListener listener)
    {
        if(mouseWheelListeners == null)
        {
            mouseWheelListeners = new ArrayList<>();
            initEventListener(GPEvent.Type.ONMOUSEWHEEL);
        }
        if(mouseWheelListeners.contains(listener) == false)
        {
            mouseWheelListeners.add(listener);
        }
    }
    
    public void addTouchListener(GTouchListener listener)
    {
        if(touchListeners == null)
        {
            touchListeners = new ArrayList<>();
            initEventListener(GPEvent.Type.ONTOUCHSTART
                , GPEvent.Type.ONTOUCHEND
                , GPEvent.Type.ONTOUCHMOVE);
        }
        if(touchListeners.contains(listener) == false)
        {
            touchListeners.add(listener);
        }
    }
    
    public void addMouseMotionListener(GMouseMotionListener listener)
    {
        if(mouseMotionListeners == null)
        {
            mouseMotionListeners = new ArrayList<>();
            initEventListener(GPEvent.Type.ONMOUSEMOVE);
        }
        if(mouseMotionListeners.contains(listener) == false)
        {
            mouseMotionListeners.add(listener);
        }
    }
    
    public void removeMouseMotionListener(GMouseMotionListener listener)
    {
        if(mouseMotionListeners != null)
        {
            mouseMotionListeners.remove(listener);
        }
    }
    
    public void removeMouseListener(GMouseListener listener)
    {
        if(mouseListeners != null)
        {
            mouseListeners.remove(listener);
        }
    }
    
    class SingleEventListener implements GPEventListener
    {
        @Override
        public void onBrowserEvent(GPEvent jsEvent)
        {
            processEvent = true;
            if(modalityStack.isEmpty() == false && isMouseOrTouchEvent(jsEvent))
            {
                GComponent modalComponent = modalityStack.peek();
                GPoint loc = modalComponent.getLocationOnScreen();
                GDimension size = modalComponent.getSize();                
                int x = jsEvent.getClientX();
                int y = jsEvent.getClientY();
                GRectangle rect = new GRectangle(loc.x, loc.y, size.width, size.height);
                processEvent = rect.contains(x, y);
            }
            GAWTEvent evt = _handleEvent(jsEvent);
            if(evt == null)
            {
//                System.out.println("");
            }
            else
            {
                GToolkit.getDefaultToolkit().handleAWTEvent(evt);
                if(evt.isConsumed())
                {
                    jsEvent.preventDefault();               
                    jsEvent.stopPropagation();
                }
            }
        }

        private boolean isMouseOrTouchEvent(GPEvent jsEvent)
        {
            Type t = jsEvent.getType();
            return t.ordinal() < 12; // Ordinal Number for Mouse and Touch Events in the Type are >=0 && < 12
        }
    }
    
    /**
     * Sollte in allen addXXXListener Methoden aufgerufen werden.
     */
    protected void initEventListener(GPEvent.Type ... eventTypes)
    {
        initEventListener(getInputElement(), eventTypes);
    }
    
    /**
     */
    protected void initEventListener(GPElement element, GPEvent.Type ... eventTypes)
    {
        if(eventListener == null)
        {
            eventListener = new SingleEventListener();
            element.setEventListener(eventListener);
        }
        element.enabledEvents( eventTypes );
    }
    
    private GMouseEvent handleMouseEnter(GPEvent event)
    {
        GMouseEvent evt = new GMouseEvent(this, event);
        for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
        {
            mouseListeners.get(i).mouseEntered(evt);
        }
        return evt;
    }
    
    private GMouseEvent handleMouseExited(GPEvent event)
    {
        GMouseEvent evt = new GMouseEvent(this, event);
        for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
        {
            GMouseListener lis = mouseListeners.get(i);
            lis.mouseExited(evt);
        }
        return evt;
    }
    
    private GMouseEvent handleMouseClicked(GPEvent event)
    {
        GMouseEvent evt = null;
        if(isEnabled())
        {
            if(isLongClickEnabled && longClickPerformed)
            {
                longClickPerformed = false;
            }
            else
            {
                evt = new GMouseEvent(this, event, 1);
                for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
                {
                    mouseListeners.get(i).mouseClicked(evt);
                }
            }
        }
        return evt;
    }
    
    private GMouseEvent handleDoubleClick(GPEvent event)
    {
        GMouseEvent evt = null;
        if(isEnabled())
        {
            evt = new GMouseEvent(this, event, 2);
            for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
            {
                mouseListeners.get(i).mouseClicked(evt);
            }
        }
        return evt;
    }
    
    private GMouseEvent handleContextMenu(GPEvent event)
    {
        GMouseEvent evt = null;
        if(isEnabled())
        {
            event.preventDefault();
            evt = new GMouseEvent(this, event, true, 1, GMouseEvent.BUTTON3);
            for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
            {
                mouseListeners.get(i).mouseClicked(evt);
            }
        }
        return evt;
    }
    
    private GMouseEvent handleMousePressed(GPEvent event)
    {
        GMouseEvent evt = null;
        if(isEnabled())
        {
            if(isLongClickEnabled && supportsLongClick())
            {
                longClickStart = System.currentTimeMillis();
            }
            evt = new GMouseEvent(this, event);
            for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
            {
                mouseListeners.get(i).mousePressed(evt);
            }
        }
        return evt;
    }
    
    private GMouseEvent handleMouseReleased(GPEvent event)
    {
        GMouseEvent evt = null;
        if(isEnabled())
        {
            if(isLongClickEnabled && supportsLongClick())
            {
                long diff = System.currentTimeMillis() - longClickStart;
                if(diff > LONG_CLICK_TIME)
                {
                    longClickPerformed = true;
                    evt = new GMouseEvent(this, event, 1);
                    evt.setId(GMouseEvent.MOUSE_LONG_CLICK);
                    for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
                    {
                        mouseListeners.get(i).mouseClickedLong(evt);
                    }
                    GToolkit.getDefaultToolkit().handleAWTEvent(evt);
                }
            }
            evt = new GMouseEvent(this, event);
            for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
            {
                mouseListeners.get(i).mouseReleased(evt);
            }
        }
        return evt;
    }
    
    private GMouseEvent handleMouseMoved(GPEvent event)
    {
        longClickPerformed = false;
        GMouseEvent evt = new GMouseEvent(this, event, isStopMousePropagation());
        for(int i=0;processEvent && mouseMotionListeners != null && i< mouseMotionListeners.size();i++)
        {
            mouseMotionListeners.get(i).mouseMoved(evt);
        }
        return evt;
    }
    
    private GMouseEvent handleMouseWheelMoved(GPEvent event)
    {
        GMouseWheelEvent evt = new GMouseWheelEvent(this, event);
        for(int i=0;processEvent && mouseMotionListeners != null && i< mouseMotionListeners.size();i++)
        {
            mouseWheelListeners.get(i).mouseWheelMoved(evt);
        }
        return evt;
    }
    
    private GTouchEvent handleTouchStart(GPEvent event)
    {
        GTouchEvent evt = new GTouchEvent(this, event);
        lastTouchStart = evt.getPoints()[0];
        for(int i=0;processEvent && touchListeners != null && i< touchListeners.size();i++)
        {
            touchListeners.get(i).touchStart(evt);
        }
        return evt;
    }
    
    private GTouchEvent handleTouchMove(GPEvent event)
    {
        GTouchEvent evt = new GTouchEvent(this, event);
        for(int i=0;processEvent && touchListeners != null && i< touchListeners.size();i++)
        {
            touchListeners.get(i).touchMove(evt);
        }
        return evt;
    }
    
    private GAWTEvent handleTouchEnd(GPEvent event)
    {
        GTouchEvent evt = new GTouchEvent(this, event);
        GAWTEvent aevt = evt;
        for(int i=0;processEvent && touchListeners != null && i< touchListeners.size();i++)
        {
            touchListeners.get(i).touchEnd(evt);
        }
        if(evt.isConsumed() == false)
        {
            GMouseEvent mevt = new GMouseEvent(this, lastTouchStart.x, lastTouchStart.y, 1, GMouseEvent.BUTTON1);
            aevt = mevt;
            for(int i=0;processEvent && mouseListeners != null && i< mouseListeners.size();i++)
            {
                mouseListeners.get(i).mouseClicked(mevt);
            }
        }
        return aevt;
    }
    
    private GKeyEvent handleKeyPressed(GPEvent event)
    {
        GKeyEvent evt = null;
        if(isEnabled())
        {
            evt = new GKeyEvent(this, event);
            for(int i=0;processEvent && keyListeners != null && i< keyListeners.size();i++)
            {
                keyListeners.get(i).keyPressed(evt);
                if(evt.isConsumed())
                {
                    break;
                }
            }
        }
        return evt;
    }
    
    private GKeyEvent handleKeyReleased(GPEvent event)
    {
        GKeyEvent evt = null;
        if(isEnabled())
        {
            evt = new GKeyEvent(this, event);
            for(int i=0;processEvent && keyListeners != null && i< keyListeners.size();i++)
            {
                keyListeners.get(i).keyReleased(evt);
                if(evt.isConsumed())
                {
                    break;
                }
            }
        }
        return evt;
    }
    
    private GKeyEvent handleKeyTyped(GPEvent event)
    {
        GKeyEvent evt = null;
        if(isEnabled())
        {
            evt = new GKeyEvent(this, event);
            GKeyEvent fevt = evt;
            if(processEvent)
            {
                //Dies ist asynchron, da ansonsten der Buchstabe noch nicht verarbeitet ist.
                GSwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for(int i=0;keyListeners != null && i< keyListeners.size();i++)
                        {
                            keyListeners.get(i).keyTyped(fevt);
                        }
                    }
                });
            }
        }
        return evt;
    }
    
    private GFocusEvent handleFocusGained(GPEvent event)
    {
        GFocusEvent evt = null;
        if(isEnabled())
        {
            evt = new GFocusEvent(this);
            for(int i=0;processEvent && focusListeners != null && i< focusListeners.size();i++)
            {
                focusListeners.get(i).focusGained(evt);
            }
        }
        return evt;
    }
    
    public void handleEvent(GAWTEvent event)
    {
    }
    
    private GAWTEvent _handleEvent(GPEvent event)
    {
        GAWTEvent bevent = null;
        switch(event.getType())
        {
            case ONMOUSEOVER:
                bevent = handleMouseEnter(event);
            break;
            case ONMOUSEOUT:
                bevent = handleMouseExited(event);
            break;
            case ONCLICK:
                bevent = handleMouseClicked(event);
            break;
            case ONCONTEXTMENU:
                bevent = handleContextMenu(event);
            break;
            case ONDBLCLICK:
                bevent = handleDoubleClick(event);
            break;
            case ONMOUSEDOWN:
                bevent = handleMousePressed(event);
            break;
            case ONMOUSEUP:
                bevent = handleMouseReleased(event);
            break;
            case ONMOUSEMOVE:
                bevent = handleMouseMoved(event);
            break;
            case ONMOUSEWHEEL:
                bevent = handleMouseWheelMoved(event);
            break;
            case ONTOUCHSTART:
                bevent = handleTouchStart(event);
            break;
            case ONTOUCHMOVE:
                bevent = handleTouchMove(event);
            break;
            case ONTOUCHEND:
                bevent = handleTouchEnd(event);
            break;
            case ONKEYDOWN:
                bevent = handleKeyPressed(event);
            break;
            case ONKEYUP:
                bevent = handleKeyReleased(event);
            break;
            case ONKEYPRESS:
                bevent = handleKeyTyped(event);
            break;
            case ONFOCUS:
                bevent = handleFocusGained(event);
            break;
            default:
                //Nothing
            break;
        }
        if(processEvent)
        {   //Give Components a chance to do their own stuff.
            handleEvent(bevent);
        }
        return bevent;
    }

    public void setOpaque(boolean opaque)
    {
        if(opaque)
        {
            getPeer().getStyle().clearProperty("backgroundColor");
        }
        else
        {
            getPeer().getStyle().setBackgroundColor("transparent");
        }
    }
    
    protected String getStylename()
    {
        return getStylename(getClass());
    }
    
    protected String getStylename(Class clazz)
    {
        return ".gwts-" + clazz.getSimpleName();
    }
    
    public void setForeground(GColor col)
    {
        getPeer().getStyle().setColor(col.toRGBA());
    }

    public void setBackground(GColor col)
    {
        if(col == null)
        {
            getPeer().getStyle().setBackgroundColor("transparent");
        }
        else
        {
            getPeer().getStyle().setBackgroundColor(col.toRGBA());
        }
    }
    
    public GColor getBackground()
    {
        String col = getPeer().getStyle().getBackgroundColor();
        GColor gcol = GColor.decode(col);
        if(gcol == null)
        {
            gcol = GUIManager.getColor(getStylename() + "/background-color");
        }
        if(gcol == null)
        {
            Class clazz = getClass().getSuperclass();
            while (clazz != null && gcol == null)
            {
                gcol = GUIManager.getColor(getStylename(clazz) + "/background-color");
                clazz = clazz.getSuperclass();
            }
        }
        return gcol;
    }
    
    public GColor getForeground()
    {
        String col = getPeer().getStyle().getColor();
        GColor gcol = GColor.decode(col);
        return gcol;
    }
    
    public void putClientProperty(Object key, Object value)
    {
        if(clientProperties == null)
        {
            clientProperties = new HashMap<>();
        }
        clientProperties.put(key, value);
    }
    
    public Object getClientProperty(Object key)
    {
        Object value = null;
        if(clientProperties != null)
        {
            value = clientProperties.get(key);
        }
        return value;
    }
    
    public String getToolTipText()
    {
        return tooltip;
    }
    
    public String getToolTipText(int x, int y)
    {
        return tooltip;
    }
    
    public void setToolTipText(String tooltip)
    {
        if(tooltip == null)
        {
            GToolTipManager.getInstance().unregisterComponent(this);
        }
        else
        {
            GToolTipManager.getInstance().registerComponent(this);
        }
        this.tooltip = tooltip;
    }
    
    public void setVisible(boolean visible)
    {
        if(this.visible != visible)
        {
            this.visible = visible;
            getPeer().getStyle().setVisibility(visible ? "visible" : "hidden");
            if(visible)
            {
                fireComponentShown();
            }
            else
            {
                fireComponentHidden();
            }
        }
//        int count = getComponentCount();
//        for(int i=0;i<count;i++)
//        {
//            getComponent(i).setVisible(visible);
//        }
    }
    
    protected void fireComponentShown()
    {
        GComponentEvent evt = new GComponentEvent(this);
        for(int i=0;componentListeners != null && i<componentListeners.size();i++)
        {
            componentListeners.get(i).componentShown(evt);
        }
    }
    
    protected void fireComponentHidden()
    {
        GComponentEvent evt = new GComponentEvent(this);
        for(int i=0;componentListeners != null && i<componentListeners.size();i++)
        {
            componentListeners.get(i).componentHidden(evt);
        }
    }
    
    public boolean isVisible()
    {
        return visible;
//        String vis = getPeer().getStyle().getVisibility();
//        return "".equals(vis) || Visibility.VISIBLE.getCssName().equals(vis);
    }
    
    public boolean isShowing()
    {
        boolean showing = true;
        GComponent comp = this;
        while(comp != null)
        {
            if(comp.isVisible() == false)
            {
                showing = false;
                break;
            }
            comp = comp.getParent();
        }
        return showing;
    }
    
    public GPElement getPeer()
    {
        return peer;
    }

    protected GPElement getInputElement()
    {
        return getPeer();
    }
    
    protected void setPeer(GPElement peer)
    {
        this.peer = peer;
    }
    
    protected void setCachedPreferredSize(GDimension dim)
    {
        preferredSize = dim;
    }
    
    protected GDimension getCachedPreferredSize()
    {
        return preferredSize;
    }
    
    public GComponent getTopLevelAncestor() 
    {
        GComponent parent = this;
        while(parent.getParent() != null)
        {
            parent = parent.getParent();
        }
        return parent;
    }
    
    public GDimension getPreferredSize()
    {
        GDimension dim = getExplizitPreferredSize();
        if(explizitPreferredSize == null)
        {
            dim = getCachedPreferredSize();
            if(layout != null)
            {
                dim = layout.preferredLayoutSize(this);
                dim.width += getStyleExtraWidth();
                dim.height += getStyleExtraHeight();
            }
            else if(dim == null)
            {
                GPElement div = GUtilities.getMeasureElement();
                GPElement clone = getPeer().cloneNode(true);
//                if(this instanceof GLabel && ((GLabel)this).getText().contains("This is an"))
//                {
////                    System.out.println("ABC");
//                }
                
                clone.getStyle().clearWidth();
                clone.getStyle().clearHeight();
                clone.getStyle().setPosition("relative");
                clone.getStyle().setDisplay("block");
                clone.getStyle().setVisibility("visible");
                int ew = getStyleExtraWidth();
                clone.getStyle().setProperty("maxWidth",(maxWidthForPreferredSize - ew) + "px");
                String family = getFont().getFamily();
                clone.getStyle().setProperty("fontFamily", family);
                div.appendChild(clone);
                int w = clone.getOffsetWidth();
                int h = clone.getOffsetHeight();
//                if(this instanceof GLabel && ((GLabel)this).getText().contains("This is an"))
//                {
////                    Window.alert("Height: " + h + "Width: " + w+ " MaxWidth: " + maxWidthForPreferredSize);
//                }
                div.removeChild(clone);
                dim = new GDimension(w, h);
                setCachedPreferredSize(dim);
            }
        }
        return dim;
    }

    protected int getMaxWidthForPreferredSize()
    {
        return maxWidthForPreferredSize;
    }
    
    public int getComponentCount()
    {
        if(components != null)
        {
            return components.size();
        }
        return 0;
    }

    public GComponent getComponent(int i)
    {
        if(components != null)
        {
            return components.get(i);
        }
        return null;
    }

    public GComponent[] getComponents()
    {
        if(components != null)
        {
            return components.toArray(new GComponent[components.size()]);
        }
        return new GComponent[0];
    }
    
    public GDimension getMinimumSize()
    {
        return new GDimension(20,20);
    }

    public GDimension getSize()
    {
        return new GDimension(size.width, size.height);
    }

    public int getHeight()
    {
        return size.height;
    }
    
    public int getWidth()
    {
        return size.width;
    }
    
    public GInsets getInsets()
    {
        if(border != null)
        {
            return border.getBorderInsets(this);
        }
        else
        {
            GInsets insets = cssBorderCache.get(getClass());
            if(insets == null)
            {
                insets = GUIManager.getBorderSize(getClass());
                cssBorderCache.put(getClass(), insets);
            }
            return insets;
        }
    }

    public void setCursor(GCursor cursor)
    {
        getPeer().getStyle().setProperty("cursor", cursor.getCursorName());
    }
    
    public GRectangle getBounds() 
    {
        GPoint loc = getLocation();
        return new GRectangle(loc.x, loc.y, getWidth(), getHeight());
    }
    
    public GRectangle getBounds(GRectangle rect) 
    {
        if(rect == null)
        {
            return getBounds();
        }
        else
        {
            GPoint loc = getLocation();
            return new GRectangle(loc.x, loc.y, getWidth(), getHeight());
        }
    }
    
    public void setBounds(int x, int y, int width, int height)
    {
        setSize(width, height);
        setLocation(x, y);
//        size.width = width;
//        size.height = height;
//        width -= getStyleExtraWidth();
//        height -= getStyleExtraHeight();
//        if(width < 0) width = 0;
//        if(height < 0) height = 0;
//        Style style = getPeer().getStyle();
//        style.setLeft(x, Unit.PX);
//        style.setTop(y, Unit.PX);
//        style.setWidth(width, Unit.PX);
//        style.setHeight(height, Unit.PX);
    }

    public void setLocation(int x, int y)
    {
        GPStyle style = getPeer().getStyle();
        style.setLeft(x);
        style.setTop(y);
    }
    
    public void setSize(GDimension dim)
    {
        setSize(dim.width, dim.height);
    }
    
    public void setSize(int width, int height)
    {
        if(size.width != width || size.height != height)
        {
            size.width = width;
            size.height = height;
            width -= getStyleExtraWidth();
            height -= getStyleExtraHeight();
            if(width < 0) width = 0;
            if(height < 0) height = 0;
            GPStyle style = getPeer().getStyle();
            style.setWidth(width);
            style.setHeight(height);
            GComponentEvent evt = new GComponentEvent(this);
            for(int i=0;componentListeners != null && i<componentListeners.size();i++)
            {
                componentListeners.get(i).componentResized(evt);
            }
        }
    }
    
    public GPoint getLocation()
    {
        GPStyle style = getPeer().getStyle();
        GPoint loc = new GPoint( style.getLeft(), style.getTop() );
        return loc;
    }
    
    public GPoint getLocationOnScreen()
    {
        int x = getPeer().getAbsoluteLeft();
        int y = getPeer().getAbsoluteTop();
//        GInsets insets = getInsets();
        GPoint loc = new GPoint(x, y);
        return loc;
    }
    
    public GLayoutManager getLayout()
    {
        return layout;
    }

    public void setLayout(GLayoutManager layout)
    {
        this.layout = layout;
    }
    
    public void add(GComponent comp, Object constraints)
    {
        addImpl(comp, constraints, -1);
    }

    public GComponent add(GComponent comp)
    {
        add(comp, null);
        return comp;
    }

    public GComponent add(String constraints, GComponent comp)
    {
        add(comp, constraints);
        return comp;
    }

    public void add(GComponent comp, Object constraints, int index)
    {
        addImpl(comp, constraints, index);
    }
    
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        getPeer().appendChild(comp.getPeer());
        if(components == null)
        {
            components = new ArrayList<>();
        }
        if(index == -1)
        {
            components.add(comp);
        }
        else
        {
            components.add(index, comp);
        }
        comp.setParent(this);
        if(layout != null && constraints != null)
        {
            layout.addLayoutComponent(comp, constraints);
        }
        GContainerEvent evt = new GContainerEvent(this, comp);
        if(containerListeners != null)
        {
            for(int i=0;i<containerListeners.size();i++)
            {
                containerListeners.get(i).componentAdded(evt);
            }
        }
        GToolkit.getDefaultToolkit().handleAWTEvent(evt);
        setCachedPreferredSize(null);
        revalidate();
    }

    public void removeAll()
    {
        if(components != null)
        {
            int i = components.size() - 1;
            for(;i>=0;i--)
            {
                remove(i);
            }
        }
    }
    
    public void remove(GComponent comp)
    {
        if(components != null)
        {
            int index = components.indexOf(comp);
            remove(index);
        }
    }
    
    public void remove(int index)
    {        
        if(components != null && index >= 0 && index < components.size())
        {
            GComponent comp = components.remove(index);
            comp.getPeer().removeFromParent();
            comp.setParent(null);
            if(layout != null)
            {
                layout.removeLayoutComponent(comp);
            }
            setCachedPreferredSize(null);
            GContainerEvent evt = new GContainerEvent(this, comp);
            if(containerListeners != null)
            {
                for(int i=0;i<containerListeners.size();i++)
                {
                    containerListeners.get(i).componentRemoved(evt);
                }
            }
            GToolkit.getDefaultToolkit().handleAWTEvent(evt);
        }
    }
    
    private void setParent(GComponent parent)
    {
        this.parent = parent;
        setCachedPreferredSize(null);
    }

    public void validate()
    {
        if(layout != null)
        {
            layout.layoutContainer(this);
        }
        validateChildren();
    }

    protected void validateChildren()
    {
        for(int i=0;components != null && i<components.size();i++)
        {
            GComponent c = components.get(i);
            if(c.isVisible())
            {
                c.validate();
            }
        }
        if(getBorder() != null)
        {
            border = getBorder();
            border.install(this);
        }
    }

    public void revalidate()
    {
        GRepaintManager.revalidate(this);
    }
    
    public GComponent getParent()
    {
        return parent;
    }

    public GBorder getBorder()
    {
        return border;
    }

    public void setBorder(GBorder border)
    {
        if(this.border != null)
        {
            this.border.uninstall(this);
        }
        if(border != null)
        {
            border.install(this);
        }
        this.border = border;
        setCachedPreferredSize(null);
    }

    protected int getStyleExtraWidth()
    {
        GInsets bi = getInsets();
        GInsets padding = getPadding();
        return padding.left + padding.right + bi.left + bi.right;
    }
    
    protected int getStyleExtraHeight()
    {
        GInsets bi = getInsets();
        GInsets padding = getPadding();
        return padding.top + padding.bottom + bi.top + bi.bottom;
    }

    public GFont getFont()
    {
        if(font == null)
        {
            font = GUIManager.getFont(".gwts-GComponent/font");
        }
        return font;
    }

    protected GInsets getPadding()
    {
        GInsets padding = null;
        Class clazz = this.getClass();
        while(padding == null && clazz != null)
        {
            padding = GUIManager.getPadding(getClass());
            clazz = clazz.getSuperclass();
        }
        return padding;
    }

    public void setFont(GFont font)
    {
        this.font = font;
        setFont(getPeer(), font);
        setCachedPreferredSize(null);
    }
    
    protected void setFont(GPElement peer, GFont font)
    {
        peer.getStyle().setFontSize(font.getSize2D());
        peer.getStyle().setProperty("fontFamily", font.getFamily());
        if(font.getStyle() == font.BOLD)
        {
            peer.getStyle().setFontWeight("bold");
        }
    }

    protected GDimension getExplizitPreferredSize()
    {
        return explizitPreferredSize;
    }

    public void setPreferredSize(GDimension preferredSize)
    {
        this.preferredSize = preferredSize;
        this.explizitPreferredSize = preferredSize;
    }

    /**
     * Internal Framework Method. Don't Use.
     * @param maxWidthForPreferredSize
     */
    public void setMaxWidthForPreferredSize(int maxWidthForPreferredSize)
    {
        if (this.maxWidthForPreferredSize != maxWidthForPreferredSize)
        {
            this.maxWidthForPreferredSize = maxWidthForPreferredSize;
            setCachedPreferredSize(null);
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        if(enabled)
        {
            getPeer().getStyle().clearColor();
        }
        else
        {
            setForeground(GColor.GRAY);
        }
    }

    public void requestFocus()
    {
        getPeer().focus();
    }

    public boolean hasFocus()
    {
        return getPeer().hasFocus();
    }
    
    public GFontMetrics getFontMetrics(GFont font)
    {
        return new GFontMetrics(font);
    }

    public int getX()
    {
        return getLocation().x;
    }

    public int getY()
    {
        return getLocation().y;
    }

    protected boolean isStopMousePropagation()
    {
        return stopMousePropagation;
    }

    protected void setStopMousePropagation(boolean stopMousePropagation)
    {
        this.stopMousePropagation = stopMousePropagation;
    }
    
    public ComponentUI getUI()
    {
    	return componentUI;
    }
    
    public void setUI(ComponentUI componentUI)
    {
    	if(this.componentUI != null)
    	{
    		this.componentUI.uninstallUI(this);
    	}
    	
    	if(componentUI != null)
    	{
    		componentUI.installUI(this);
    	}
    }
    
    public void updateUI()
    {
    	setUI(GUIManager.getUI(this));
    }
}
