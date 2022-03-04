/*
 * CardPanel.java
 *
 * Copyright (c) 2001 eXware
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * eXware ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with eXware.
 */
package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.Style.Visibility;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.GLayoutManager;

/**
* a simple alternative to the usage of cardlayout!
* because cardlayout was made for heavyweight components, it calls
* validate directly! to avoid this, and to avoid the casting of the
* layoutmanager, we use a simple Gridlayout, and call remove,add,revalidate and
* repaint to show the new component.
*
* @author Martin Schmitz 07.02.2000
*/
public class CardPanel extends GComponent
{
    private int current;

    /**
     * Creates a CardPanel.  Children, called "cards" in this API, should be added
     * with add().  The first child we be made visible, subsequent children will
     * be hidden.  To show a card, use one of the show*Card methods.
     */
    public CardPanel()
    {
        super();
        this.setLayout(new CardPanelLayout());
    }

    @Override
    public GComponent add(GComponent comp)
    {
        return add("",comp);
    }

    @Override
    public GComponent add(String name,GComponent comp)
    {
        addImpl(comp, name, getComponentCount());
        comp.setVisible(false);
        comp.setName(name);
        showFirst();
        return comp;
    }

    public GComponent add(GComponent comp,int index)
    {
        super.add(comp,index);
        comp.setVisible(false);
        showFirst();
        return comp;
    }

    @Override
    public void removeAll()
    {
        super.removeAll();
        current = -1;
    }

    @Override
    public void add(GComponent comp,Object constraints)
    {
        add((String)constraints,comp);
    }

    
    @Override
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        super.addImpl(comp, constraints, index);
    }

    @Override
    public void setVisible(boolean visible)
    {
        getPeer().getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
        GComponent comp = getCurrentComponent();
        if(comp != null)
        {
            comp.setVisible(visible);
        }
    }
    
/**
 * Gibt die Komponente zurueck deren Karte mit name gekennzeichnet ist.
 */
    public GComponent getComponent(String name)
    {
        int nChildren = this.getComponentCount();
        for (int i = 0; i < nChildren; i++)
        {
            GComponent child = this.getComponent(i);
            if (name.equals(child.getName()))
            {
                return child;
            }
        }
        return null;
    }

    public void remove(String name)
    {
        GComponent current = getCurrentComponent();
        int nChildren = this.getComponentCount();
        for (int i = 0; i < nChildren; i++)
        {
            GComponent child = this.getComponent(i);
            if (child.getName().equals(name))
            {
//                current.setVisible(false);
                remove(child);
                if(current == child)
                {
                    this.current = -1;
                    showFirstCard();
                }
                else
                {
                    showCard(current);
                }
                break;
            }
        }        
    }
    
/**
 * Gibt den Index der Komponente zurück.
 */
    public int indexOf(GComponent comp)
    {
        int nChildren = this.getComponentCount();
        for (int i = 0; i < nChildren; i++)
        {
            GComponent child = this.getComponent(i);
            if (child == comp)
            {
                return i;
            }
        }
        return -1;
    }

    public GComponent getCurrentComponent()
    {
        return this.getComponent(current);
    }

    private void showFirst()
    {
        if(getComponentCount() >= 1)
        {
            showFirstCard();
        }
    }

/**
* Hide the currently visible child  "card" and show the
* specified card.
*/
    public void showCard(GComponent card)
    {
        if(card == null) return;
        if (current != -1 && current < getComponentCount())
        {
            GComponent c = getComponent(current);
            if(card == c && card.isVisible())
            {//card has not changed, and is already visible
                return;
            }
            c.getPeer().removeFromParent();
            c.setVisible(false);
        }
        current = indexOf(card);
        getPeer().appendChild(card.getPeer());
        card.setVisible(true);
        revalidate();
    }


/**
* Show the card with the specified name.
* @see java.awt.Component#getName
*/
    public void showCard(String name)
    {
        GComponent comp = getComponent(name);
        showCard(comp);
    }


    /**
     * Show the card that was added to this CardPanel after the currently
     * visible card.  If the currently visible card was added last, then
     * show the first card.
     */
    public void showNextCard()
    {
        if (getComponentCount() <= 0)
        {
            return;
        }
        if (current == -1)
        {
            showCard(getComponent(0));
        }
        else if (current == (getComponentCount() - 1))
        {
            showCard(getComponent(0));
        }
        else
        {
            showCard(getComponent(current + 1));
        }
   }


    /**
     * Show the card that was added to this CardPanel before the currently
     * visible card.  If the currently visible card was added first, then
     * show the last card.
     */
    public void showPreviousCard()
    {
        if (getComponentCount() <= 0)
        {
            return;
        }
        if (current == -1)
        {
            showCard(getComponent(0));
        }
        else if (current == 0)
        {
            showCard(getComponent(getComponentCount() - 1));
        }
        else
        {
            showCard(getComponent(current - 1));
        }
    }


    /**
     * Show the first card that was added to this CardPanel.
     */
    public void showFirstCard()
    {
        if (getComponentCount() <= 0)
        {
            return;
        }
        showCard(getComponent(0));
    }


    /**
     * Show the last card that was added to this CardPanel.
     */
    public void showLastCard()
    {
        if (getComponentCount() < 0)
        {
            return;
        }
        showCard(getComponent(getComponentCount()-1));
    }

    class CardPanelLayout implements GLayoutManager
    {
        /**
         * Adds the specified component with the specified name to
         * the layout.
         * @param name the component name
         * @param comp the component to be added
         */
        @Override
        public void addLayoutComponent(String name, GComponent comp)
        {
        }

        /**
         * Removes the specified component from the layout.
         * @param comp the component to be removed
         */
        @Override
        public void removeLayoutComponent(GComponent comp)
        {
        }

        /**
         * Calculates the preferred size dimensions for the specified
         * panel given the components in the specified parent container.
         * @param parent the component to be laid out
         *
         * @see #minimumLayoutSize
         */
        @Override
        public GDimension preferredLayoutSize(GComponent parent)
        {
            int count = parent.getComponentCount();
            GDimension dim = new GDimension();
            GInsets insets = parent.getInsets();
            for(int i=0;i<count;i++)
            {
                GDimension pref = parent.getComponent(i).getPreferredSize();
                dim.width = dim.width > pref.width ? dim.width : pref.width;
                dim.height = dim.height > pref.height ? dim.height : pref.height;
            }
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
            return dim;
        }

        /**
         * Calculates the minimum size dimensions for the specified
         * panel given the components in the specified parent container.
         * @param parent the component to be laid out
         * @see #preferredLayoutSize
         */
        @Override
        public GDimension minimumLayoutSize(GComponent parent)
        {
            int count = parent.getComponentCount();
            GDimension dim = new GDimension();
            for(int i=0;i<count;i++)
            {
                GDimension pref = parent.getComponent(i).getMinimumSize();
                dim.width = dim.width > pref.width ? dim.width : pref.width;
                dim.height = dim.height > pref.height ? dim.height : pref.height;
            }
            return dim;
        }

        /**
         * Lays out the container in the specified panel.
         * @param parent the component which needs to be laid out
         */
        @Override
        public void layoutContainer(GComponent parent)
        {
            if(current >= 0 && getComponentCount() > 0  && getComponentCount() > current)
            {
                GComponent comp = getComponent(current);
                GInsets insets = parent.getInsets();
                GDimension dim = parent.getSize();

                comp.setBounds(0,0,dim.width-insets.left-insets.right,
                    dim.height-insets.top-insets.bottom);
            }
        }

        @Override
        public void addLayoutComponent(GComponent comp, Object constraints)
        {
        }
    }
}
