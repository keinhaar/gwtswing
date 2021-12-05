package de.exware.gwtswing.swing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.exware.gwtswing.awt.GBorderLayout;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GCursor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GGridLayout;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseListener;
import de.exware.gwtswing.awt.event.GMouseMotionListener;
import de.exware.gwtswing.swing.border.GBorderFactory;
import de.exware.gwtswing.swing.border.SelectiveLineBorder;
import de.exware.gwtswing.swing.event.GColumnEvent;
import de.exware.gwtswing.swing.event.GColumnListener;
import de.exware.gwtswing.swing.event.GListSelectionEvent;
import de.exware.gwtswing.swing.event.GListSelectionListener;
import de.exware.gwtswing.swing.event.GTableModelEvent;
import de.exware.gwtswing.swing.event.GTableModelListener;
import de.exware.gwtswing.swing.table.GAbstractTableModel;
import de.exware.gwtswing.swing.table.GDefaultTableCellRenderer;
import de.exware.gwtswing.swing.table.GTableCellRenderer;
import de.exware.gwtswing.swing.table.GTableModel;
import de.exware.gwtswing.util.DoubleMap;

public class GTable<T> extends GComponent
{
    private GGridBagConstraints gbc = new GGridBagConstraints();
    private GTableModel model;
    private Map<Class,GTableCellRenderer> renderers = new HashMap<>();
    private GPanel content;
    private GPanel header;
    private List<GComponent> renderedItems = new ArrayList<>();
    private List<Integer> selectedItems = new ArrayList<>();
    private List<GColumnListener> columnListeners;
    private DoubleMap<Integer, Integer> sortMap;
    private Comparator<?>[] comparators;
    private int sortedColumn = -1; 
    private boolean ascending = true;
    private TableFilter[] filter;
    private List<Integer> nonFilteredRows;
    private GListSelectionModel selectionModel;
    private List<GListSelectionListener> selectionListeners;
    private int selectionMode;
    
    private GTableModelListener modelListener = new GTableModelListener()    
    {
        @Override
        public void tableChanged(GTableModelEvent evt)
        {
            if(evt.getFirstRow() == GTableModelEvent.HEADER_ROW)
            {
                setModel(model);
            }
            else
            {
                if(evt.getType() != GTableModelEvent.UPDATE)
                {
                    ((GTableLayout)content.getLayout()).rowHeight=0;
                }
                renderAll();
            }
        }
    };
    
    public GTable()
    {
        renderers.put(Object.class, new GDefaultTableCellRenderer());
        setLayout(new GBorderLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        content = new GPanel();
        content.setLayout(new GTableLayout());
        content.getPeer().addClassName("gwts-GTable-content");
        add(content, GBorderLayout.CENTER);
        header = new GPanel();
        header.getPeer().addClassName("gwts-GTable-header");
        GTable<T>.GTableLayout headerLayout = new GTableLayout();
        headerLayout.isHeader = true;
        header.setLayout(headerLayout);
        HeaderListener resize = new HeaderListener();
        header.addMouseListener(resize);
        header.addMouseMotionListener(resize);
        add(header, GBorderLayout.NORTH);
        header.setBorder(new SelectiveLineBorder(GColor.DARK_GRAY, 0, 0, 2, 0));
        setBorder(GBorderFactory.createLineBorder(GColor.DARK_GRAY, 1));
        setModel(new InnerDefaultTableModel());
        addMouseListener(selectionListener);
        setDefaultRenderer(Date.class, new GDefaultTableCellRenderer()
        {
            @Override
            public GComponent getTableCellRendererComponent(GTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
            {
                GLabel label = (GLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Date date = (Date) value;
                if(date != null)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    String str = sdf.format(date);
                    label.setText(str);
                }
                return label;
            }
        });
    }
    
    public GListSelectionModel getSelectionModel() 
    {
        if(selectionModel == null)
        {
            selectionModel = new GListSelectionModel()
            {
                @Override
                public void removeListSelectionListener(GListSelectionListener listener)
                {
                    if(selectionListeners != null)
                    {
                        selectionListeners.remove(listener);
                    }
                }
                
                @Override
                public void addListSelectionListener(GListSelectionListener listener)
                {
                    if(selectionListeners == null)
                    {
                        selectionListeners = new ArrayList<>();
                    }
                    selectionListeners.add(listener);
                }

                @Override
                public void setSelectionMode(int selectionMode)
                {
                    GTable.this.selectionMode = selectionMode;
                }
            };
        }
        return selectionModel;
    }
    
    public GTableCellRenderer getCellRenderer(int row, int column) 
    {
        Class clazz = getModel().getColumnClass(column);
        GTableCellRenderer renderer = renderers.get(clazz);
        if(renderer == null)
        {
            renderer = renderers.get(Object.class);
        }
        return renderer;
    }
    
    public void setSortable(boolean sortable)
    {
        if(sortable)
        {
            comparators = new Comparator[getColumnCount()];
            Comparator<?> comparator = new Comparator<Object>()
            {
                @Override
                public int compare(Object o1, Object o2)
                {
                    String s1 = o1 == null ? "" : o1.toString();
                    String s2 = o2 == null ? "" : o2.toString();
                    return s1.toString().compareTo(s2.toString());
                }
            };
            for(int i=0;i<comparators.length;i++)
            {
                comparators[i] = comparator;
            }
        }
        else
        {
            comparators = null;
        }
    }
    
    public void setComparator(int column, Comparator<?> comparator)
    {
        if(isSortable())
        {
            comparators[column] = comparator;
        }
        else
        {
            throw new IllegalStateException("Must be sortable");
        }
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = super.getPreferredSize();
        dim.width = 0;
        for(int i=0;i<getColumnCount();i++)
        {
            dim.width += getColumnWidth(i);
        }
        return dim;
    }
    
    private void updateHeader(int column)
    {
        if(column >= 0)
        {
            GLabel label = (GLabel) header.getComponent(column);
            String text = model.getColumnName(column);
            if(column == sortedColumn)
            {
                if(ascending)
                {
                    text = "\u25B2 " + text;
                }
                else
                {
                    text = "\u25BC " + text;
                }
            }
            if(isFiltered(column))
            {
                text += "&nbsp;&nbsp;<b><div style='color:#000099'>\u2A5B</b>";
            }
            label.setText(text);
        }
    }
    
    public boolean isFiltered(int column)
    {
        if(filter != null)
        {
            return filter[column] != null;
        }
        return false;
    }

    public void setFilter(int column, TableFilter filter)
    {
        if(this.filter == null)
        {
            this.filter = new TableFilter[getColumnCount()];
        }
        this.filter[column] = filter;
        applyFilter();
        if(isSortable() && sortedColumn >= 0)
        {
            sort(sortedColumn);
        }
        updateHeader(column);
        renderAll();
    }
    
    private void applyFilter()
    {
        if(nonFilteredRows == null)
        {
            nonFilteredRows = new ArrayList<>();
        }
        else
        {
            nonFilteredRows.clear();
        }
        int rowCount = model.getRowCount();
        for(int row=0; row<rowCount;row++)
        {
            boolean include = true;
            for(int col = 0;col<model.getColumnCount();col++)
            {
                if(filter[col] != null)
                {
                    Object value = model.getValueAt(row, col);
                    if(filter[col].accept(value) == false)
                    {
                        include = false;
                        break;
                    }
                }
            }
            if(include)
            {
                nonFilteredRows.add(row);
            }
        }
    }

    public void addColumnListener(GColumnListener listener)
    {
        if(columnListeners == null)
        {
            columnListeners = new ArrayList<>();
        }
        if(columnListeners.contains(listener) == false)
        {
            columnListeners.add(listener);
        }
    }
    
    class HeaderListener extends GMouseAdapter
        implements GMouseMotionListener
    {
        boolean isDragging;
        int column;
        
        @Override
        public void mouseMoved(GMouseEvent e)
        {
            int x = e.getX();
            if(isDragging)
            {
                header.setCursor(GCursor.COLUMN_RESIZE_CURSOR);
            }
            else
            {
                int col = getEdgeOfColumn(x);
                if(col >= 0)
                {
                    header.setCursor(GCursor.COLUMN_RESIZE_CURSOR);
                }
                else
                {
                    header.setCursor(GCursor.DEFAULT_CURSOR);
                }
            }
            if(isDragging)
            {
                GTableLayout layout = (GTable<T>.GTableLayout) header.getLayout();
                if(column < 0 && x > 0)
                {
                    column = getColumnCount() - 1; 
                }
                int total = 0;
                int newWidth = 10;
                int oldWidth = layout.columnWidths[column];
                for(int i=0;i<=column;i++)
                {
                    int w = layout.columnWidths[i];
                    total += w;                    
                }
                int diff = x - total;
                newWidth = layout.getColumnWidth(column) + diff;
                if(newWidth < 10) newWidth = 10;
                setColumnWidth(column, newWidth);
                GColumnEvent cevt = new GColumnEvent(GTable.this, column, oldWidth, newWidth);
                fireColumnResized(cevt);
            }
        }
        
        @Override
        public void mousePressed(GMouseEvent evt)
        {
            super.mousePressed(evt);
            int col = getEdgeOfColumn(evt.getX());
            if(col >= 0 && GSwingUtilities.isLeftMouseButton(evt))
            {
                isDragging = true;
                column = col;
            }
        }
        
        @Override
        public void mouseReleased(GMouseEvent evt)
        {
            super.mouseReleased(evt);
            if(isDragging == false && isSortable() && GSwingUtilities.isLeftMouseButton(evt))
            {
                int col = columnAtPoint(new GPoint(evt.getX(), evt.getY()));
                if(col == sortedColumn)
                {
                    ascending = !ascending;
                }
                sort(col);
            }
            isDragging = false;
        }
        
        @Override
        public void mouseExited(GMouseEvent evt)
        {
            super.mouseExited(evt);
        }
        
        private int getEdgeOfColumn(int x)
        {
            GTableLayout layout = (GTable<T>.GTableLayout) header.getLayout();
            int edgeOfColumn = -1;
            int pos = 0;
            for(int i=0;i<layout.columnWidths.length;i++)
            {
                int w = layout.columnWidths[i];
                pos += w;
                if(x > pos-10 && x < pos+10)
                {
                    edgeOfColumn = i;
                    break;
                }
            }
            return edgeOfColumn;
        }
    }
    
    public int getColumnWidth(int col)
    {
        GTableLayout layout = (GTable<T>.GTableLayout) header.getLayout();
        return layout.getColumnWidth(col);
    }
    
    private void sort(int column)
    {
        Comparator comp1 = comparators[column];
        Comparator<SortWrapper> swComparator = new Comparator<SortWrapper>()
        {
            @Override
            public int compare(SortWrapper o1, SortWrapper o2)
            {
                Object ob1 = o1.value;
                Object ob2 = o2.value;
                return comp1.compare(ob1, ob2);
            }
        };
        Comparator<SortWrapper> comp = swComparator;
        if(ascending == false)
        {
            comp = new Comparator<SortWrapper>()
            {
                @Override
                public int compare(SortWrapper o1, SortWrapper o2)
                {
                    return -swComparator.compare(o1, o2);
                }
            };
        }
        List<SortWrapper> values = new ArrayList<>();
        GTableModel model = getModel();
        int rowCount = getRowCount();
        for(int row = 0; row<rowCount; row++)
        {
            int r = row;
            if(nonFilteredRows != null)
            {
                r = nonFilteredRows.get(row);
            }
            Object value = model.getValueAt(r, column);
            SortWrapper sw = new SortWrapper();
            sw.modelrow = r;
            sw.value = value;
            values.add(sw);
        }
        Collections.sort(values, comp);
        sortMap = new DoubleMap<>();
        for(int row = 0; row<rowCount; row++)
        {
            SortWrapper sw = values.get(row);
            sortMap.put(sw.modelrow, row);
        }
        int oldSortedColumn = sortedColumn;
        sortedColumn = column;
        updateHeader(oldSortedColumn);
        updateHeader(sortedColumn);
        renderAll();
    }

    public int convertRowIndexToModel(int viewRowIndex)
    {
    	if(isSortable() && sortedColumn >= 0)
    	{
	    	int modelIndex = sortMap.reverseGet(viewRowIndex);
	    	return modelIndex;
    	}
    	else if(nonFilteredRows != null)
    	{
    	    return nonFilteredRows.get(viewRowIndex);
    	}
    	return viewRowIndex;
    }

    public int convertRowIndexToView(int viewRowIndex)
    {
    	int viewIndex = sortMap.get(viewRowIndex);
    	return viewIndex;
    }
    
    class SortWrapper
    {
        int modelrow;
        Object value;
    }
    
    public boolean isSortable()
    {
        return comparators != null;
    }

    private void fireColumnResized(GColumnEvent evt)
    {
        if(columnListeners != null)
        {
            for(int i=0;i<columnListeners.size();i++)
            {
                columnListeners.get(i).columnResized(evt);
            }
        }
    }
    
    class GTableLayout extends GGridBagLayout
    {
        LayoutManagerInfo info;
        int rowHeight = 10;
        int[] columnWidths;
        boolean initialCalculation = true;
        boolean isHeader;
        
        public void reset()
        {
            initialCalculation = true;
        }
        
        @Override
        protected LayoutManagerInfo getLayoutInfo(GComponent parent)
        {
            if(initialCalculation)
            {
                info = super.getLayoutInfo(parent);
                columnWidths = info.getColumnWidths();
                initialCalculation = false;
                rowHeight = info.getRowHeight(0);
                return info;
            }
            else
            {
                if(rowHeight == 0)
                {
                    info = super.getLayoutInfo(parent);
                    int[] ws = info.getColumnWidths();
                    for(int i=0;i<ws.length;i++)
                    {
                        ws[i] = columnWidths[i];
                    }
                    columnWidths = ws;
                    rowHeight = info.getRowHeight(0);
                }
                return info;
            }
        }
        
        @Override
        public GDimension preferredLayoutSize(GComponent parent)
        {
            if(initialCalculation)
            {
                return super.preferredLayoutSize(parent);
            }
            else
            {
                GDimension dim = new GDimension(info.getTotalWidth(), isHeader ? rowHeight : rowHeight * getModel().getRowCount());
                return dim;
            }
        }

        public int getColumnWidth(int col)
        {
            return columnWidths[col];
        }

        public void setColumnWidth(int col, int columnWidth)
        {
            columnWidths[col] = columnWidth;
        }
    }
    
    public void setColumnWidth(int col, int columnWidth)
    {        
        GTableLayout headerlayout = (GTable<T>.GTableLayout) header.getLayout();
        if(headerlayout.initialCalculation)
        {
            validate();
        }
        GTableLayout contentlayout = (GTable<T>.GTableLayout) content.getLayout();
        headerlayout.setColumnWidth(col, columnWidth);
        contentlayout.setColumnWidth(col, columnWidth);
        revalidate();
        GScrollPane parent = (GScrollPane) getParent();
        parent.refitContent();
    }
    
    @Override
    public void revalidate()
    {
        super.revalidate();
    }
    
    public void setModel(GTableModel model)
    {
        boolean sortable = isSortable();
        setSortable(false);
        ((GTableLayout)header.getLayout()).reset();
        ((GTableLayout)content.getLayout()).reset();
        header.removeAll();
        if(this.model != null)
        {
            this.model.removeTableModelListener(modelListener);
        }
        selectedItems.clear();
        this.model = model;
        model.addTableModelListener(modelListener);
        GGridBagConstraints headerGbc = new GGridBagConstraints();
        int colCount = model.getColumnCount();
        for(int x=0;x<colCount;x++)
        {
            String name = model.getColumnName(x);
            GLabel label = new GLabel(name);
            label.setBackground(null);
            if(x > 0)
            {
                if(x == colCount -1)
                {
                    label.setBorder(new SelectiveLineBorder(GColor.DARK_GRAY, 0, 2, 0, 2));
                }
                else
                {
                    label.setBorder(new SelectiveLineBorder(GColor.DARK_GRAY, 0, 2, 0, 0));
                }
            }
            headerGbc.gridx = x;
            headerGbc.fill = GGridBagConstraints.HORIZONTAL;
            header.add(label, headerGbc);
        }
        renderAll();
        setSortable(sortable);
    }
    
    private void renderAll()
    {
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.removeAll();
        renderedItems.clear();
        gbc.fill = gbc.BOTH;
        int colCount = model.getColumnCount();
        int rowCount = model.getRowCount();
        if(nonFilteredRows != null)
        {
            rowCount = nonFilteredRows.size();
        }
        for(int row=0;row<rowCount;row++)
        {
            int modelRow = convertRowIndexToModel(row);
            for(int col=0;col<colCount;col++)
            {
                Object value = model.getValueAt(modelRow, col);
                GTableCellRenderer renderer = getCellRenderer(row, col);
                GComponent comp = renderer.getTableCellRendererComponent(this, value, false, false, row, col);
                Cell cell = new Cell(comp);
                cell.update(row, col);
                comp = cell;
                gbc.gridx = col;
                gbc.gridy = row;
//                gbc.fill = GGridBagConstraints.HORIZONTAL;
                content.add(comp, gbc);
                comp.putClientProperty("value", value);
                comp.putClientProperty("gridx", col);
                comp.putClientProperty("gridy", row);
                renderedItems.add(comp);
            }
        }
        setCachedPreferredSize(null);
        validate();
    }
    
    class Cell extends GPanel
    {
        public Cell(GComponent comp)
        {
            setLayout(new GGridLayout(1, 1));
            setOpaque(false);
            add(comp);
        }
        
        @Override
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, width, height);
        }
        
        void update(int row, int column)
        {
            int rowCount = getRowCount();
            int colCount = getColumnCount();
            int top = 0;
            int bottom = 0;
            int right = 0;
            int left = 0;
            if(row > 0)
            {
                top = 2;
            }
            if(row == rowCount - 1)
            {
                bottom = 2;
            }
            if(column > 0)
            {
                left = 2;
            }
            if(column == colCount - 1)
            {
                right = 2;
            }
            setBorder(new SelectiveLineBorder(GColor.LIGHT_GRAY, top, left, bottom, right));
        }
    }
    
    @Override
    public void validate()
    {
        super.validate();
        if(model.getRowCount() > 0 && content.getComponentCount() > 0)
        {
            for(int x=0;x<model.getColumnCount();x++)
            {
                GTableLayout headerlayout = (GTable<T>.GTableLayout) header.getLayout();
                GTableLayout contentlayout = (GTable<T>.GTableLayout) content.getLayout();
                if(headerlayout.initialCalculation == true)
                {
                    headerlayout.setColumnWidth(x, contentlayout.getColumnWidth(x));
                }
                else
                {
                   contentlayout.setColumnWidth(x, headerlayout.getColumnWidth(x));
                }
            }
        }
        header.validate();
        content.validate();
    }

    public int getRowCount()
    {
        if(nonFilteredRows != null)
        {
            return nonFilteredRows.size();
        }
        return model.getRowCount();
    }
    
    public int getColumnCount()
    {
        return model.getColumnCount();
    }

    public int[] getSelectedRows()
    {
        List<Integer> selectedRows = new ArrayList<>();
        for(int i=0;i<selectedItems.size();i++)
        {
            int row = selectedItems.get(i) / model.getColumnCount();
            if(selectedRows.contains(row) == false)
            {
                selectedRows.add(row);
            }
        }
        int[] rows = new int[selectedRows.size()];
        for(int i=0;i<selectedRows.size();i++)
        {
            rows[i] = selectedRows.get(i);
        }
        return rows;
    }

    private GMouseListener selectionListener = new GMouseAdapter()
    {
        @Override
        public void mouseClicked(final GMouseEvent evt) 
        {
            GUtilities.clearSelection();
            if(evt.getClickCount() == 1)
            {
                GSwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                        GPoint point = new GPoint(evt.getX(), evt.getY());
                        int row = rowAtPoint(point);
                        int col = columnAtPoint(point);
                        if(row < 0 || col < 0) return;
                        int current = row * model.getColumnCount() + col;
                        boolean selected = selectedItems.contains(current);
                        if(evt.isControlDown())
                        {
                            if(selected)
                            {
                                selected = false;
                                selectedItems.remove(new Integer(current));
                            }
                            else
                            {
                                selected = true;
                                selectedItems.add(current);
                            }
                            fireSelectionEvent();
                        }
                        else
                        {
                            if(selectedItems.contains(current) == false)
                            {
                                ArrayList<Integer> oldList = new ArrayList<>(selectedItems);
                                selectedItems.clear();
                                for(int i=0;i<oldList.size();i++)
                                {
                                    revalidateRenderedItem(oldList.get(i));
                                }
                                if(!selected)
                                {
                                    selected = true;
                                    selectedItems.add(current);
                                }
                                fireSelectionEvent();
                            }
                        }
                        revalidateRenderedItem(current);
                        validate();
                    };
                });
            }
        };
    };

    private void fireSelectionEvent()
    {
        GListSelectionEvent evt = new GListSelectionEvent(this);
        for(int i=0;selectionListeners != null && i < selectionListeners.size();i++)
        {
            GListSelectionListener listener = selectionListeners.get(i);
            listener.valueChanged(evt);
        }
    }
    
    public int columnAtPoint(GPoint point) 
    {
        int column = -1;
        GDimension dim = header.getSize();
        if(point.x > 0 && point.x < dim.width)
        {
            int width = 0;
            for(int i=0;i<header.getComponentCount();i++)
            {
                width += header.getComponent(i).getSize().width;
                if(point.x < width)
                {
                    column = i;
                    break;
                }
            }
        }
        return column;
    }
    
    public int rowAtPoint(GPoint point) 
    {
        int row = -1;
        GDimension dim = getSize();
        int height = header.getSize().height;
        if(point.y > height && point.y < dim.height)
        {
            for(int i=0;i<content.getComponentCount();i+=model.getColumnCount())
            {
                height += content.getComponent(i).getSize().height;
                if(point.y < height)
                {
                    row = i / model.getColumnCount();
                    break;
                }
            }
        }
        return row;
    }
    
    private void revalidateRenderedItem(int index)
    {
        GComponent comp = renderedItems.get(index);
        Object value = comp.getClientProperty("value");
        Integer gridx = (Integer) comp.getClientProperty("gridx");
        Integer gridy = (Integer) comp.getClientProperty("gridy");
        int current = index;
        boolean selected = selectedItems.contains(current);
        GGridBagConstraints gbc = this.gbc.clone();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        content.remove(comp);
        comp.removeMouseListener(selectionListener);
        Class clazz = getModel().getColumnClass(gridx);
        GTableCellRenderer renderer = getCellRenderer(gridy, gridx);
        comp = renderer.getTableCellRendererComponent(GTable.this, value, selected, false, gridy, gridx);
        Cell cell = new Cell(comp);
        cell.update(gridy, gridx);
        comp = cell;
        comp.putClientProperty("value", value);
        comp.putClientProperty("gridx", gridx);
        comp.putClientProperty("gridy", gridy);
        renderedItems.set(current, comp);
        content.add(comp,gbc);
    }
    
    private int getRowFromIndex(int index)
    {
        int row = index / model.getColumnCount();
        return row;
    }
    
    private int getColumnFromIndex(int index)
    {
        int col = index % model.getColumnCount();
        return col;
    }

    public GTableModel getModel()
    {
        return model;
    }

    public GComponent getTableHeader()
    {
        return header;
    }

    public void resetLayout()
    {
        ((GTableLayout)header.getLayout()).reset();
        ((GTableLayout)content.getLayout()).reset();
    }

    public void setDefaultRenderer(Class<?> colClass, GTableCellRenderer renderer)
    {
        renderers.put(colClass, renderer);
    }
}

class InnerDefaultTableModel extends GAbstractTableModel
{

    @Override
    public int getColumnCount()
    {
        return 3;
    }

    @Override
    public int getRowCount()
    {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        return "AAA";
    }
}