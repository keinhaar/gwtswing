package de.exware.gwtswing.swing.tree;

import de.exware.gwtswing.Constants;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GIcon;
import de.exware.gwtswing.swing.GImageIcon;
import de.exware.gwtswing.swing.GLabel;
import de.exware.gwtswing.swing.GTree;
import de.exware.gwtswing.swing.GUIManager;
import de.exware.gwtswing.swing.GUtilities;

public class GDefaultTreeCellRenderer implements GTreeCellRenderer
{
    private static GIcon folderIcon = new GImageIcon(GUtilities.getResource(Constants.PLUGIN_ID, "/icons/folder.svg"),16,16);
    private static GIcon fileIcon = new GImageIcon(GUtilities.getResource(Constants.PLUGIN_ID, "/icons/file.svg"),16,16);
    
    @Override
    public GComponent getTreeCellRendererComponent(GTree tree, Object value, boolean selected, boolean expanded,
        boolean leaf, int row, boolean hasFocus)
    {
        GLabel label = new GLabel(leaf ? fileIcon : folderIcon);
        //This line is a hack, because the GWT Compiler removes the toString method if this is not there.
        //But only in Production. Development mode is OK???
        label.setText(value == null ? "" : "" + value.getClass());
        label.setText(value == null ? "" : value.toString());
        label.setBackground(null);
        if(selected)
        {
            label.setBackground(GUIManager.getColor(".gwts-GTree-selected/background-color"));
        }
        return label;
    }
}
