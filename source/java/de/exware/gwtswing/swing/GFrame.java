package de.exware.gwtswing.swing;

import java.util.Objects;

import de.exware.gwtswing.awt.GBorderLayout;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GWindow;

public class GFrame extends GWindow
{
    private GPanel toolbarMenuContainer;
    private GMenuBar menubar;
    private GToolBar toolbar;
    
    public GFrame()
    {
    }
    
    public void setGToolBar(GToolBar tbar)
    {
        this.toolbar = tbar;
        ensureToolBarMenuContainer();
        GGridBagConstraints gbc = new GGridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = gbc.HORIZONTAL;
        toolbarMenuContainer.add(tbar, gbc);
    }
    
    public void setToolbarLogo(GComponent logo)
    {
        GGridBagConstraints gbc = new GGridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = gbc.BOTH;
        Objects.nonNull(toolbar);
        toolbarMenuContainer.add(logo, gbc);
    }
    
    public GToolBar getGToolBar()
    {
        return toolbar;
    }

    public GMenuBar getGMenuBar()
    {
        return menubar;
    }

    public void setGMenuBar(GMenuBar mbar)
    {
        ensureToolBarMenuContainer();
        GGridBagConstraints gbc = new GGridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        toolbarMenuContainer.add(mbar, gbc);
        menubar = mbar;
    }

    private void ensureToolBarMenuContainer()
    {
        if(toolbarMenuContainer == null)
        {
            GComponent cp = getContentPane();
            remove(cp);
            toolbarMenuContainer = new GPanel();
            toolbarMenuContainer.setLayout(new GGridBagLayout());
            GUtilities.insertClassNameBefore(toolbarMenuContainer.getPeer(), "toolbarMenuContainer");
            _add(toolbarMenuContainer, GBorderLayout.CENTER);
            GGridBagConstraints gbc = new GGridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.weightx = 0;
            gbc.weighty = 1;
            gbc.fill = gbc.BOTH;
            toolbarMenuContainer.add(cp, gbc);
        }
    }
}
