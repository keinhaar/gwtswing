/**
 * Copyright (c) 2006 eXware All rights reserved.
 * 
 * This software is the confidential and proprietary information of eXware ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement
 * you entered into with eXware.
 */
package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.Constants;
import de.exware.gwtswing.awt.GBorderLayout;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.swing.border.GBorder;
import de.exware.gwtswing.swing.border.GBorderFactory;
import de.exware.gwtswing.swing.border.GEmptyBorder;
import de.exware.gwtswing.swing.border.SelectiveLineBorder;
import de.exware.gwtswing.swing.event.GTabEvent;
import de.exware.gwtswing.swing.event.GTabEvent.CloseState;
import de.exware.gwtswing.swing.event.GTabListener;

/**

 */
public class GTabbedPane extends GComponent
{
    public static final int TOP = 0;
    public static final int LEFT = 1;    
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    private GPanel tabs;
    private CardPanel tabbedComponents;
    private TabMouseAdapter tabMouseAdapter = new TabMouseAdapter();
    private int componentCounter = 0;
    private int selectedTab = -1;
    private int closeButtonGap = 3;
    private List<GTabListener> tabListeners = new ArrayList<>();
    private GBorder selectedTabBorder;
    private GBorder tabBorder;
    private static GIcon closeIcon = new GImageIcon(GUtilities.getResource(Constants.PLUGIN_ID, "/icons/cancel.png"), 16, 16);
    private GGridBagConstraints gbc = new GGridBagConstraints();
    private int position;

    /**
     * Creates a new Empty GTabbedPane.
     */
    public GTabbedPane()
    {
        this(TOP);
    }

    public GTabbedPane(int position)
    {
        this.position = position;
        setLayout(new GBorderLayout());
        tabs = new GPanel();
        tabs.getPeer().addClassName("gwts-GTabbedPane-Tabs");
        tabs.setLayout(new GGridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GGridBagConstraints.WEST;
        tabbedComponents = new CardPanel();
        tabbedComponents.getPeer().addClassName("gwts-GTabbedPane-TabbedComponents");
        add(tabbedComponents, GBorderLayout.CENTER);
        if(position == RIGHT)
        {
            selectedTabBorder = new SelectiveLineBorder(GColor.decode("#7a7463"), 1, 0, 1, 3);
            tabBorder = new GEmptyBorder(1, 0, 1, 3);
            add(tabs, GBorderLayout.EAST);
            gbc.fill = GGridBagConstraints.HORIZONTAL;
            gbc.insets.top = 3;
            gbc.weightx = 1;
        } 
        else if(position == LEFT) 
        {
            selectedTabBorder = new SelectiveLineBorder(GColor.decode("#7a7463"), 1, 3, 1, 0);
            tabBorder = new GEmptyBorder(1, 0, 1, 3);
            add(tabs, GBorderLayout.WEST);
            gbc.fill = GGridBagConstraints.HORIZONTAL;
            gbc.insets.top = 3;
            gbc.weightx = 1;
        }
        else if(position == TOP)
        {
            selectedTabBorder = new SelectiveLineBorder(GColor.decode("#6a6453"), 3, 1, 0, 1);
            tabBorder = new SelectiveLineBorder(GColor.decode("#8a8473"), 3, 1, 0, 1);
            add(tabs, GBorderLayout.NORTH);
            gbc.fill = GGridBagConstraints.HORIZONTAL;
            gbc.insets.left = 3;
        }
        else if(position == BOTTOM)
        {
            selectedTabBorder = new SelectiveLineBorder(GColor.decode("#6a6453"), 0, 1, 3, 1);
            tabBorder = new SelectiveLineBorder(GColor.decode("#8a8473"), 0, 1, 3, 1);
            add(tabs, GBorderLayout.SOUTH);
            gbc.fill = GGridBagConstraints.HORIZONTAL;
            gbc.insets.left = 3;
        }
    }

    /**
     * Add a new Tab with the given name. Clicking the tab will bring the component to the front of the tabbed
     * Components. This is equal to calling addTab(new JLabel(string),comp,closeable).
     * 
     * @param string
     *            The Label that should be displayed on the Tab.
     * @param comp
     *            The Component, that should be shown if the Tab is activated.
     * @param closeable
     *            if true, the tab may be closed by clicking on the close icon.
     */
    public void addTab(String string, GComponent comp, boolean closeable)
    {
        GLabel label = new GLabel(string);
        label.setBorder(GBorderFactory.createEmptyBorder(3, 0, 0, 0));
        addTab(label, comp, closeable);
    }

    /**
     * Add a new Tab with the given tabComp as TabRenderer. Clicking the tab will bring the component to the front of
     * the tabbed Components.
     * 
     * @param tabComp
     *            The Component that should be used as Tab
     * @param comp
     *            The Component, that should be shown if the Tab is activated.
     * @param closeable
     *            if true, the tab may be closed by clicking on the close icon.
     */
    public void addTab(GComponent tabComp, GComponent comp, boolean closeable)
    {
        GPanel tab = new GPanel();
        tab.setLayout(new GBorderLayout());
        tab.add(tabComp, GBorderLayout.CENTER);
        String tabIdentifier = "" + componentCounter++;
        if (closeable)
        {
            GLabel close = new GLabel(closeIcon)            
            {
                @Override
                public GInsets getPadding()
                {
                    return new GInsets();
                }
            };
            tab.add(close, GBorderLayout.EAST);
            close.getPeer().getStyle().setPadding(0);
            close.putClientProperty("closeAction", "true");
            close.putClientProperty("tabIdentifier", tabIdentifier);
            close.setOpaque(false);
            close.addMouseListener(tabMouseAdapter);
            close.setBorder(GBorderFactory.createEmptyBorder(2, closeButtonGap, 0, 2));
        }
        tab.getPeer().addClassName("gwts-GTabbedPane-Tab");
        tabComp.getPeer().addClassName("gwts-GTabbedPane-Tab");
        tabComp.addMouseListener(tabMouseAdapter);
        tab.setBorder(tabBorder);
        if(tabs.getComponentCount() > 0)
        {
            gbc.insets.top = 0;
        }
        tabs.add(tab, gbc);
        if(position == TOP || position == BOTTOM)
        {
            gbc.gridx++;
        }
        else
        {
            gbc.gridy++;
        }
  //      tab.setBackground(tabs.getBackground());
        tabComp.putClientProperty("tabIdentifier", tabIdentifier);
//        tabComp.putClientProperty(SwingUtils.CLIENT_PROPERTY_IGNORE_ENABLE_RECURSIVE, true);
        tab.putClientProperty("tabIdentifier", tabIdentifier);
        comp.putClientProperty("tabIdentifier", tabIdentifier);
        tabbedComponents.add(comp, tabIdentifier);
        if (selectedTab < 0)
        {
            setSelectedTab(0);
        }
//        else
//        {
////            setSelectedTab(tabIdentifier);
//        }
        GTabEvent evt = new GTabEvent(this, tab, comp);
        for (int i = 0; i < tabListeners.size(); i++)
        {
            GTabListener l = tabListeners.get(i);
            l.tabAdded(evt);
        }
    }

    @Override
    public GDimension getPreferredSize()
    {
        return super.getPreferredSize();
    }
    
    public void setSelectedTabBorder(GBorder border)
    {
        if(selectedTab >= 0)
        {
            getTabComponent(selectedTab).setBorder(border);
        }
        selectedTabBorder = border;
    }

    public void setTabEnabled(GComponent tabbedComponent, boolean enabled, String tooltip)
    {
        int index = getTabIndex(getTabIdentifier(tabbedComponent));
        GComponent comp = tabs.getComponent(index);
        GUtilities.setEnabledRecursive(comp, enabled, true);
        GUtilities.setTooltipRecursive(comp, tooltip);
    }

    public void setTabVisible(GComponent tabbedComponent, boolean visible)
    {
        int index = getTabIndex(getTabIdentifier(tabbedComponent));
        GComponent comp = tabs.getComponent(index);
        comp.setVisible(false);
        GComponent[] comps = tabs.getComponents();
        for(int i=0;i<comps.length;i++)
        {
            if(comps[i].isVisible())
            {
                setSelectedTab(i);
                break;
            }
        }
    }

    protected String getTabIdentifier(GComponent comp)
    {
        String ti = (String) comp.getClientProperty("tabIdentifier");
        while(comp.getParent() != null && ti == null)
        {
            comp = comp.getParent();
            ti = (String) comp.getClientProperty("tabIdentifier");
        }
        return ti;
    }
    
    public int getTabIndex(GComponent tabbedComponent)
    {
        return getTabIndex(getTabIdentifier(tabbedComponent));
    }
    
    /**
     * Selects the Tab on the given index.
     * 
     * @param i
     */
    public void setSelectedTab(int i)
    {
        if (i >= 0 && i < tabs.getComponentCount())
        {
            if (selectedTab >= 0)
            {
                GComponent lastActiveTab = tabs.getComponent(selectedTab);
                lastActiveTab.setBorder(tabBorder);
//                lastActiveTab.setOpaque(false);
                lastActiveTab.getPeer().removeClassName("gwts-GTabbedPane-selectedTab");
            }
            selectedTab = i;
            GComponent tabcomp = tabs.getComponent(i);
            tabcomp.setBorder(selectedTabBorder);
            String tabIdentifier = (String) tabcomp.getClientProperty("tabIdentifier");
            tabbedComponents.showCard(tabIdentifier);
            tabcomp.getPeer().addClassName("gwts-GTabbedPane-selectedTab");
            GComponent comp = tabbedComponents.getComponent(i);
            GUtilities.focusFirstField(comp);
            GTabEvent evt = new GTabEvent(this, tabcomp, comp);
            for (int j = 0; j < tabListeners.size(); j++)
            {
                GTabListener l = tabListeners.get(j);
                l.tabActivated(evt);
            }
        }
    }

    /**
     * @param tabIdentifier
     */
    private void setSelectedTab(String tabIdentifier)
    {
        setSelectedTab(getTabIndex(tabIdentifier));
    }

    /**
     * Selects the Tab, that is associated with this component.
     * 
     * @param comp
     */
    public void setSelectedTab(GComponent comp)
    {
        String tabIdent = (String) comp.getClientProperty("tabIdentifier");
        setSelectedTab(tabIdent);
    }

    /**
     * @param tabIdentifier
     */
    public void removeTab(String tabIdentifier)
    {
        removeTab(getTabIndex(tabIdentifier));
    }

    /**
     * Returns the tabIndex of the Tab with the given tabIdentifier. Be aware, that the tabIdentifier is only internally
     * used, and should therefore never be exposed to clients.
     * 
     * @param tabIdentifier
     * @return
     */
    private int getTabIndex(String tabIdentifier)
    {
        for (int i = 0; i < tabs.getComponentCount(); i++)
        {
            GComponent comp = tabs.getComponent(i);
            String tabIdent = (String) comp.getClientProperty("tabIdentifier");
            if (tabIdent.equals(tabIdentifier))
            {
                return i;
            }
        }
        return -1;
    }

    public void removeTab(GComponent tabbedComponent)
    {
        removeTab(getTabIdentifier(tabbedComponent));
    }
    
    /**
     * @param i
     */
    public void removeTab(int i)
    {
        if(i>=0 && i < tabs.getComponentCount())
        {
            GComponent tab = tabs.getComponent(i);
            GComponent comp = tabbedComponents.getComponent(i);
            tabs.remove(i);
            tabbedComponents.remove(i);
            if (i < selectedTab)
            {
                selectedTab -= 1;
            }
            if (i == selectedTab)
            {
                selectedTab = -1;
                setSelectedTab(0);
            }
            revalidate();
            GTabEvent evt = new GTabEvent(this, tab, comp);
            for (int j = 0; j < tabListeners.size(); j++)
            {
                GTabListener l = tabListeners.get(j);
                l.tabRemoved(evt);
            }
        }
    }

    /**
     * Adds an GTabListener to the listener list. Listeners will be notified if a Tab is about to be closed.
     * 
     * @param l
     */
    public void addTabListener(GTabListener l)
    {
        tabListeners.add(l);
    }

    /**
     * Remove a listener from this Panes listener list.
     * 
     * @param l
     */
    public void removGTabListener(GTabListener l)
    {
        tabListeners.remove(l);
    }

    /**
     * Used internally to handle activating and closing Tabs by Mouseclicks
     */
    class TabMouseAdapter extends GMouseAdapter
    {
        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(GMouseEvent evt)
        {
            GComponent comp = (GComponent) evt.getSource();
            String tabIdentifier = (String) comp.getClientProperty("tabIdentifier");
            String sclose = (String) comp.getClientProperty("closeAction");
            if ("true".equalsIgnoreCase(sclose))
            {
                int tabIndex = getTabIndex(tabIdentifier);
                GTabEvent tevt = new GTabEvent(GTabbedPane.this, tabs.getComponent(tabIndex),
                    tabbedComponents.getComponent(tabIndex));
                tevt.setListenerCount(tabListeners.size());
                for (int i = 0; i < tabListeners.size(); i++)
                {
                    GTabListener l = tabListeners.get(i);
                    l.checkTabClosingAllowed(tevt);
                }
                GSwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                        CloseState cstate = tevt.getCloseState();
                        if (cstate == CloseState.CLOSE)
                        {
                            removeTab(tabIdentifier);
                        }
                        if(cstate == CloseState.IN_PROGRESS)
                        {
                            GSwingUtilities.invokeLater(this);
                        }
                    }
                });
            }
            else if(comp.isEnabled())
            {
                setSelectedTab(tabIdentifier);
            }
        }
    }

    /**
     * Returns the number of Tabs in this ETabbedPane
     * 
     * @return
     */
    public int getTabCount()
    {
        return tabs.getComponentCount();
    }

    public GComponent getTabComponent(int i)
    {
        return tabs.getComponent(i).getComponent(0);
    }
    
    /**
     * Returns the Component that is associated with the Tab at the given index.
     * 
     * @param i
     * @return
     */
    public GComponent getTabbedComponent(int i)
    {
        return tabbedComponents.getComponent(i);
    }

    public int getSelectedIndex()
    {
        return selectedTab;
    }

    public void setTabText(int index, String title)
    {
        GComponent comp = getTabComponent(index);
        if(comp instanceof GLabel)
        {
            ((GLabel)comp).setText(title);
        }
    }
}
