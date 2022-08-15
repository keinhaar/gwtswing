package de.exware.gwtswing.awt;

import java.util.HashMap;
import java.util.Map;

import de.exware.gwtswing.swing.GComponent;

public class GGridBagLayout implements GLayoutManager
{
    private Map<GComponent,GGridBagConstraints> constraints;

    public GGridBagLayout()
    {
        constraints = new HashMap<>();
    }

    @Override
    public void addLayoutComponent(GComponent component, Object constraints)
    {
        GGridBagConstraints gbc = (GGridBagConstraints) constraints;
        GGridBagConstraints con = gbc.clone();
        this.constraints.put(component,con);
    }

    public void setConstraints(GComponent comp, GGridBagConstraints constraints) 
    {
        GGridBagConstraints con = constraints.clone();
        this.constraints.put(comp, con);
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
        LayoutManagerInfo linfo = getLayoutInfo(parent);
        GDimension dim = new GDimension();
        dim.width = linfo.totalWidth;
        dim.height = linfo.totalHeight;
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
     * Get LayoutInfo
     */
    protected LayoutManagerInfo getLayoutInfo(GComponent parent)
    {
        int i =0;
        LayoutManagerInfo linfo = new LayoutManagerInfo();
        //get grid size.
        for(;i<parent.getComponentCount();i++)
        {
            GComponent comp = parent.getComponent(i);
            if(comp.isVisible() == false) continue;
            GGridBagConstraints constr = constraints.get(comp);
            if(constr.gridx+1 > linfo.columns)
            {
                linfo.columns = constr.gridx+1;
            }
            if(constr.gridy+1 > linfo.rows)
            {
                linfo.rows = constr.gridy+1;
            }
        }
        linfo.rowheight = new int[linfo.rows];
        linfo.columnwidth = new int[linfo.columns];
        linfo.rowWeight = new double[linfo.rows];
        linfo.columnWeight = new double[linfo.columns];
        int largestMultiColumnWidth = -1;
        for(i=0;i<parent.getComponentCount();i++)
        {
            GComponent comp = parent.getComponent(i);
            if(comp.isVisible() == false) continue;
            GDimension size = comp.getPreferredSize();
            GGridBagConstraints constr = constraints.get(comp);
            if(constr.gridwidth == 1)
            {
                int colWidth = size.width + constr.insets.left + constr.insets.right;
                int width = linfo.columnwidth[constr.gridx];
                if(width < colWidth)
                {
                    linfo.columnwidth[constr.gridx] = colWidth;
                    linfo.totalWidth += colWidth - width;
                }
            }
            if(constr.gridheight == 1)
            {
                int rowHeight = size.height + constr.insets.top + constr.insets.bottom;
                int height = linfo.rowheight[constr.gridy];
                if(height < rowHeight)
                {
                    linfo.rowheight[constr.gridy] = rowHeight;
                    linfo.totalHeight += rowHeight - height;
                }
            }
            if(constr.weightx > linfo.columnWeight[constr.gridx + constr.gridwidth - 1])
            {   //weight auf letzte Spalte von mehrspaltigen elementen.
                linfo.columnWeight[constr.gridx + constr.gridwidth - 1] = constr.weightx;
            }
            if(constr.weighty > linfo.rowWeight[constr.gridy + constr.gridheight - 1])
            {
                linfo.rowWeight[constr.gridy + constr.gridheight - 1] = constr.weighty;
            }
        }
        
        GDimension contSize = parent.getSize();
        double totalWeightX = linfo.getTotalWeightX();
        if(totalWeightX > 0 && contSize.width > 0)
        {
            int extraSpaceX = contSize.width - linfo.totalWidth;
            if(extraSpaceX > 0)
            {
                int xx = 0;
                for(i=0;i<linfo.columns-1;i++)
                {
                    double weight = linfo.columnWeight[i];
                    int ex = (int) (extraSpaceX / totalWeightX * weight);
                    linfo.columnwidth[i] += ex;
                    xx += linfo.columnwidth[i];
                }
                linfo.columnwidth[linfo.columns-1] = contSize.width - xx;
            }
        }
        double totalWeightY = linfo.getTotalWeightY();
        if(totalWeightY > 0 && contSize.height > 0)
        {
            int extraSpaceY = contSize.height - linfo.totalHeight;
            if(extraSpaceY > 0)
            {
                int xx = 0;
                for(i=0;i<linfo.rows-1;i++)
                {
                    double weight = linfo.rowWeight[i];
                    int ex = (int) (extraSpaceY / totalWeightY * weight);
                    linfo.rowheight[i] += ex;
                    xx += linfo.rowheight[i];
                }
                linfo.rowheight[linfo.rows-1] = contSize.height - xx;
            }
        }
        
        //multi column calculation
        for(i=0;i<parent.getComponentCount();i++)
        {
            GComponent comp = parent.getComponent(i);
            if(comp.isVisible() == false) continue;
            GDimension size = comp.getPreferredSize();
            GGridBagConstraints constr = constraints.get(comp);
            if(constr.gridwidth > 1)
            {
                int colWidth = size.width + constr.insets.left + constr.insets.right;
                int width = linfo.columnwidth[constr.gridx];
                for(int x = constr.gridx + 1; x < constr.gridx + constr.gridwidth && x < linfo.columnwidth.length;x++)
                {
                    width += linfo.columnwidth[x];
                }
                if(width < colWidth)
                {
                    int col = linfo.columnwidth.length - 1;
                    if(linfo.columnwidth.length > (constr.gridx + constr.gridwidth - 1))
                    {
                        col = constr.gridx + constr.gridwidth - 1;
                    }
                    linfo.columnwidth[col] += colWidth - width;
                    linfo.totalWidth += colWidth - width;
                }
            }
            if(constr.gridheight > 1)
            {
//                int rowHeight = size.height + constr.insets.top + constr.insets.bottom;
//                int height = linfo.rowheight[constr.gridy];
//                if(height < rowHeight)
//                {
//                    linfo.rowheight[constr.gridy] = rowHeight;
//                    linfo.totalHeight += rowHeight - height;
//                }
            }
        }

        
        return linfo;
    }

    /**
     * Lays out the container in the specified panel.
     * @param parent the component which needs to be laid out
     */
    @Override
    public void layoutContainer(GComponent parent)
    {
        int i =0;
        LayoutManagerInfo linfo = getLayoutInfo(parent);
        GDimension contSize = parent.getSize();
        linfo.totalWidth = contSize.width;
        int xOffset = 0;
        int yOffset = 0;
        
        for(;i<parent.getComponentCount();i++)
        {
            GComponent comp = parent.getComponent(i);
            if(comp.isVisible() == false) continue;
            GDimension size = comp.getPreferredSize();
            GGridBagConstraints constr = constraints.get(comp);
            int columnX = linfo.getColumnPosition(constr.gridx) + xOffset;
            int rowY = linfo.getRowPosition(constr.gridy) + yOffset;
            int width = size.width;
            int height = size.height;
            if(constr.fill == GGridBagConstraints.HORIZONTAL || constr.fill == GGridBagConstraints.BOTH)
            {
                width = 0;
                for(int x=0;x<constr.gridwidth;x++)
                {
                    int nextColWidth = 0;
                    if(linfo.columnwidth.length > constr.gridx+x)
                    {
                        nextColWidth = linfo.columnwidth[constr.gridx+x];
                    }
                    width = width + nextColWidth;
                }
                width = width - constr.insets.left - constr.insets.right;
            }
            if(constr.fill == GGridBagConstraints.VERTICAL || constr.fill == GGridBagConstraints.BOTH)
            {
                height = 0;
                for(int y=0;y<constr.gridheight;y++)
                {
                    int nextColHeight = 0;
                    if(linfo.rowheight.length > constr.gridy+y)
                    {
                        nextColHeight = linfo.rowheight[constr.gridy+y];
                    }
                    height = height + nextColHeight;
                }
                height = height - constr.insets.top - constr.insets.bottom;
            }
            int y = rowY + constr.insets.top;
            int x = columnX + constr.insets.left;
            if(constr.anchor == constr.CENTER)
            {
                int colwidth = linfo.columnwidth[constr.gridx];
                int xo = (colwidth - width - constr.insets.left - constr.insets.right) / 2;
                x += xo;
            }
            if(constr.anchor == constr.CENTER || constr.anchor == constr.WEST)
            {
                int rowheight = linfo.rowheight[constr.gridy];
                int yo = (rowheight - height - constr.insets.top - constr.insets.bottom) / 2;
                y += yo;
            }
            if(constr.anchor == constr.SOUTHWEST || constr.anchor == constr.SOUTH || constr.anchor == constr.SOUTHEAST)
            {
                int rowheight = linfo.rowheight[constr.gridy];
                int yo = (rowheight - height - constr.insets.top - constr.insets.bottom);
                y += yo;
            }
            comp.setBounds(x, y ,width, height);
        }
    }

    public class LayoutManagerInfo
    {
        int rows = 0;
        int columns = 0;
        int[] rowheight;
        int[] columnwidth;
        double[] columnWeight; 
        double[] rowWeight; 
        int totalWidth;
        int totalHeight;

        public int getColumnPosition(int col)
        {
            int pos = 0;
            int i=0;
            for(;i<col;i++)
            {
                pos = pos + columnwidth[i];
            }
            return pos;
        }


        public int getRowPosition(int row)
        {
            int pos = 0;
            int i=0;
            for(;i<row;i++)
            {
                pos = pos + rowheight[i];
            }
            return pos;
        }
        
        public double getTotalWeightX()
        {
            double weight = 0;
            int i=0;
            for(;i<columns;i++)
            {
                weight = weight + columnWeight[i];
            }
            return weight;
        }
        
        public double getTotalWeightY()
        {
            double weight = 0;
            int i=0;
            for(;i<rows;i++)
            {
                weight = weight + rowWeight[i];
            }
            return weight;
        }
        
        public int getColumnWidth(int col)
        {
            return columnwidth[col];
        }
        
        public int[] getColumnWidths()
        {
            return columnwidth;
        }
        
        public int getRowHeight(int row)
        {
            if(rowheight.length <= row)
            {
                return 0;
            }
            return rowheight[row];
        }


        public int getTotalWidth()
        {
            return totalWidth;
        }


        public void setTotalWidth(int totalWidth)
        {
            this.totalWidth = totalWidth;
        }


        public int getTotalHeight()
        {
            return totalHeight;
        }


        public void setTotalHeight(int totalHeight)
        {
            this.totalHeight = totalHeight;
        }
    }

    @Override
    public void addLayoutComponent(String name, GComponent comp)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeLayoutComponent(GComponent comp)
    {
        GGridBagConstraints cons = constraints.remove(comp);
        return;
    }
}