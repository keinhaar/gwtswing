/**
 * Copyright (c) 2006 eXware
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * eXware ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with eXware.
 */
package de.exware.gwtswing;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GLayoutManager;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GLabel;

/**
 * A PartitionedPanel displays its components in columns. You might
 * or might not add Separators between rows. A
 * Seperator is build of a label and an horizontal line.
 */
public class PartitionedPanel extends GComponent
{
    private int columns;
    private int indentSize = 20;
    private GGridBagConstraints gbc;

    public PartitionedPanel()
    {
    	this(2);
    }
    
    /**
     * Create a PartionedPanel with the given number of columns.
     * 
     * @param columns
     */
    public PartitionedPanel(int columns)
    {
        super();
        getPeer().addClassName("gwts-PartitionedPanel");
        this.columns = columns;
        super.setLayout(new GGridBagLayout());
        gbc = new GGridBagConstraints();
        gbc.insets.left = 5;
        gbc.insets.top = 2;
        gbc.insets.bottom = 2;
        gbc.fill = GGridBagConstraints.NONE;
        gbc.anchor = GGridBagConstraints.WEST;
    }

    /**
     * Add a new Component to the actual column and row.
     * 
     * @param comp
     */
    @Override
    public GComponent add(GComponent comp)
    {
        add(comp, gbc.fill, 1, 1);
        return comp;
    }

    /**
     * Add an separator below the current row and break to the next row.
     * 
     * @param label
     */
    public void addSeparator(String label)
    {
        GGridBagConstraints old = gbc.clone();
//        gbc.weightx = 1;
//        gbc.fill = gbc.HORIZONTAL;
        breakRow();
        gbc.gridwidth = columns +1;
        gbc.insets.left = 5;
        gbc.insets.right = 5;
        SeparatorLabel lab = new SeparatorLabel(label);
        super.addImpl(lab, gbc, getComponentCount());
        gbc = old;
        breakRow();
        breakRow();
    }

    @Override
    public void removeAll()
    {
        super.removeAll();
        gbc.gridx = 0;
        gbc.gridy = 0;
    }
    
    /**
     * Break the current row. The next component added to this Panel will be added on the next row even if the last row
     * wasn't completely filled.
     */
    public void breakRow()
    {
        gbc.gridx = 0;
        gbc.gridy++;
    }

    /**
     * Leave the current column empty.
     */
    public void nextColumn()
    {
        gbc.gridx++;
        if(gbc.gridx >= columns)
        {
            breakRow();
        }
    }
    
    /**
     * Overridden to throw Exception on illegal usage of add methods.
     */
    @Override
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        throw new RuntimeException("Can't add Component. Only add(GComponent) is supported.");
    }

    /**
     * Overridden to throw Exception, because this Panel ha a fixed layoutmanager.
     */
    @Override
    public void setLayout(GLayoutManager layout)
    {
        if (layout != null)
        {
            throw new RuntimeException("Can't set Layout on PartitionedPanel. It's Layout is fixed.");
        }
    }

    /**
     * A Label with an additional horizontal line.
     */
    class SeparatorLabel extends GComponent
    {
        private GLabel linelabel;

        public SeparatorLabel(String text)
        {
            getPeer().addClassName("gwts-PartitionedPanel-Separator");
            setLayout(new GGridBagLayout());
            GGridBagConstraints gbc = new GGridBagConstraints();
            GLabel label = new GLabel(text);
            label.getPeer().addClassName("gwts-PartitionedPanel-Separator-text");
            GFont font = label.getFont();
            font = new GFont(font.getFamily(), font.BOLD, (int)font.getSize2D());
            label.setFont(font);
            add(label, gbc);
            linelabel = new GLabel()
            {
                @Override
                protected String getStylename()
                {
                    return ".gwts-PartitionedPanel-Separator-line";
                }
            };
            linelabel.setOpaque(true);
            linelabel.getPeer().addClassName("gwts-PartitionedPanel-Separator-line");
            gbc.gridx++;
            gbc.anchor = GGridBagConstraints.CENTER;
            gbc.fill = gbc.HORIZONTAL;
            gbc.weightx = 1;
            add(linelabel, gbc);
            linelabel.setPreferredSize(new GDimension(10,2));            
        }
        
        @Override
        public void setBounds(int x, int y, int width, int height)
        {
            GDimension dim = PartitionedPanel.this.getSize();
            super.setBounds(x, y, dim.width - indentSize - 10, height);
//            linelabel.setSize(linelabel.getSize().width-5, linelabel.getSize().height);
        }
        
        @Override
        public GDimension getPreferredSize()
        {
            setCachedPreferredSize(null);
            GDimension dim2 = super.getPreferredSize();
            return dim2;
        }
    }

    public void setCurrentColumnCount(int i)
    {
        columns = i;
        breakRow();
    }

    public void addMultiColumn(GComponent comp, int columnSpan)
    {
        add(comp, gbc.fill, columnSpan, 1);
    }
    
    public void add(GComponent comp, int fillmode, int columnSpan, int rowSpan)
    {
        add(comp,fillmode,columnSpan,rowSpan,0,0);
    }
    
    public void setAnchor(int anchor)
    {
        gbc.anchor = anchor;
    }
    
    public void add(GComponent comp, int fillmode, int columnSpan, int rowSpan,double weightX,double weightY)
    {
        GGridBagConstraints clone = gbc.clone();
        if (gbc.gridx + columnSpan >= columns ) // last column in row?
        {
            gbc.insets.right = indentSize;
        }
        if (gbc.gridx == 0) // the first column?
        {
            gbc.insets.left = indentSize;
        }
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.gridwidth = columnSpan;
        gbc.gridheight = rowSpan;
        gbc.fill = fillmode;
        super.addImpl(comp, gbc, getComponentCount());
        gbc.gridx += columnSpan; // next column
        if (gbc.gridx >= columns) // to much columns?
        {
            breakRow();
        }
        gbc.insets.right = clone.insets.right;
        gbc.insets.left = clone.insets.left;
        gbc.gridwidth = clone.gridwidth;
        gbc.gridheight = clone.gridheight;
        gbc.weightx = clone.weightx;
        gbc.weighty = clone.weighty;
        gbc.fill = clone.fill;
    }

    public int getIndentSize()
    {
        return indentSize;
    }

    public void setIndentSize(int indentSize)
    {
        this.indentSize = indentSize;
    }

    public GGridBagConstraints getConstraints()
    {
        return gbc;
    }
}
