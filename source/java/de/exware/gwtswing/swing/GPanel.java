package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GLayoutManager;

public class GPanel extends GComponent
{
    public GPanel()
    {
    }

    public GPanel(GLayoutManager layout) {
        setLayout(layout);
    }
}
