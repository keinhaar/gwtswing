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

/**
 * GTabListeners will be notified on any changes made to the Tabs of an ETabbedPane.
 */
public interface GChangeListener
{
    public void stateChanged(GChangeEvent evt);
}
