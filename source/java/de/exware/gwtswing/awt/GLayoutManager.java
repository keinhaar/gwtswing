package de.exware.gwtswing.awt;

import de.exware.gwtswing.swing.GComponent;

public interface GLayoutManager
{
    void addLayoutComponent(GComponent comp, Object constraints);
    void addLayoutComponent(String name,GComponent comp);
    void layoutContainer(GComponent parent);
    GDimension minimumLayoutSize(GComponent parent);
    GDimension preferredLayoutSize(GComponent parent);
    void removeLayoutComponent(GComponent comp);

}
