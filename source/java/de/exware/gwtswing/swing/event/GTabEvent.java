/**
 * Copyright (c) 2006 eXware
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * eXware ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with eXware.
 */
package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.util.GEventObject;

/**
 *
 */
public class GTabEvent extends GEventObject
{
    private GComponent tab;
    private GComponent tabbedComponent;
    /**
     * This is used to check that every Listener has voted for closing or against it.
     */
    private int tabListenerCount;
    private int votes;
    private CloseState closeState = CloseState.IN_PROGRESS;
    
    public enum CloseState
    {
        CANCEL
        , IN_PROGRESS
        , CLOSE
    }

    /**
     * @param source
     * @param id
     */
    public GTabEvent(Object source, GComponent tab, GComponent tabbedComponent)
    {
        super(source);
        this.tab = tab;
        this.tabbedComponent = tabbedComponent;
    }

    public GComponent getTab()
    {
        return this.tab;
    }

    public GComponent getTabbedComponent()
    {
        return this.tabbedComponent;
    }

    public void setListenerCount(int size)
    {
        tabListenerCount = size;
    }

    public CloseState getCloseState()
    {
        return closeState;
    }
    
    public void allowClose(boolean allow)
    {
        votes++;
        if(false == allow)
        {
            closeState = CloseState.CANCEL;
        }
        if(votes == tabListenerCount && closeState == CloseState.IN_PROGRESS)
        {
            closeState = CloseState.CLOSE;
        }
    }
}
