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
public interface GTabListener
{
   /**
    * Signals the activation of a Tab.
    * @param evt
    */
   public void tabActivated(GTabEvent evt);

   /**
    * Called if a new Tab is added.
    * @param evt
    */
   public void tabAdded(GTabEvent evt);

   /**
    * Called if a has been removed.
    * @param evt
    */
   public void tabRemoved(GTabEvent evt);

   /**
    * Is called if an Tab is about to be closed. Listeners may avoid closing
    * by calling tevt.allowClosed(boolean alowed);
    * @param evt
    * @return
    */
   public void checkTabClosingAllowed(GTabEvent tevt);
}
