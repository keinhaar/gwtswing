/**
 * Copyright (c) 2006 eXware
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * eXware ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with eXware.
 */
package de.exware.gwtswing.swing;


import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;

/**
 * This Toolbar allows to add groups. Toolbar elements can than be added to a
 * group, which allows better automated sorting of the toolbar elements.
 */
public class GToolBar extends GComponent
{
    private GGridBagConstraints gbc;
    
    public GToolBar()
    {
        gbc = new GGridBagConstraints();
        setLayout(new GGridBagLayout());
    }
    
    public GGridBagConstraints getConstraints()
    {
        return gbc;
    }
    
    @Override
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        super.addImpl(comp, gbc, index);
        gbc.gridx++;
        if(index < getComponentCount())
        {
            GGridBagLayout gbl = (GGridBagLayout) getLayout();
            gbc = new GGridBagConstraints();
            for(int i=0;i<getComponentCount();i++)
            {
                gbl.setConstraints(getComponent(i), gbc);
                gbc.gridx++;
            }
        }
    }
    
    /**
     * @param attribute
     */
    public void addGroup(String groupname)
    {
        GLabel group = new GLabel();
        group.putClientProperty("id", groupname);
        group.setVisible(false);
        add(group);
    }

    /**
     * Add an action to this Toolbar. The action will be added after the group,
     * or at the end of this toolbar if the group is null or does not exist.
     * @param group
     * @param action
     */
    public void add(String group, GAction action)
    {
        GToolBarButton item = new GToolBarButton(action);
        GIcon icon = (GIcon) action.getValue(GAction.ICON);
        if(icon != null)
        {
            item.setIcon(icon);
        }
        else
        {
            item.setText((String) action.getValue(GAction.NAME));
        }
        if (group == null) // no group defined, so we just add to the end
        {
            add(item);
        }
        else
        // try to append to the group
        {
            int index = getItemIndex(group);
            if (index < 0) // group not found or last element?
            {
                add(item);
            }
            else
            {
                addImpl(item, null, index + 1);
            }
        }
        item.putClientProperty("id",action.getValue("id"));
    }

    private int getItemIndex(String id)
    {
        int index = -1;
        for (int i = 0; i < getComponentCount(); i++)
        {
            GComponent comp = getComponent(i);
            if (id.equals(comp.getClientProperty("id")))
            {
                index = i;
                break;
            }
        }
        return index;
    }

    class GToolBarButton extends GButton
    {

        public GToolBarButton(GAction action)
        {
            super(action);
            setBackground(GUIManager.getColor(".gwts-GToolBarButton/background-color"));
            setText(null);
        }

        @Override
        public void setAction(GAction action)
        {
            super.setAction(action);
        }
        
    }
}
