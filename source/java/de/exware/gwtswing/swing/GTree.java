package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.exware.gwtswing.awt.GBorderLayout;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseListener;
import de.exware.gwtswing.swing.event.GTreeModelEvent;
import de.exware.gwtswing.swing.event.GTreeModelListener;
import de.exware.gwtswing.swing.event.GTreeSelectionEvent;
import de.exware.gwtswing.swing.event.GTreeSelectionListener;
import de.exware.gwtswing.swing.tree.GDefaultMutableTreeNode;
import de.exware.gwtswing.swing.tree.GDefaultTreeCellRenderer;
import de.exware.gwtswing.swing.tree.GDefaultTreeModel;
import de.exware.gwtswing.swing.tree.GTreeCellRenderer;
import de.exware.gwtswing.swing.tree.GTreeModel;
import de.exware.gwtswing.swing.tree.GTreePath;

public class GTree<T> extends GComponent
{
    private GTreeCellRenderer renderer = new GDefaultTreeCellRenderer();
    private GTreeModel model = createDefaultTreeModel();
    private int preferredHeight;
    private int preferredWidth;
    private List<ItemWrapper> renderedItems = new ArrayList<>();
    private List<Boolean> expanded = new ArrayList<>();
    private Map<ItemWrapper, Object> map = new HashMap<>();
    private Map<Object, ItemWrapper> reverseMap = new HashMap<>();
    private Set<Integer> selectedRows = new HashSet<>();
    private List<GTreeSelectionListener> treeSelectionListeners;
    private boolean rootVisible = true;
    private ModelListener modelListener = new ModelListener();
    private int itemSpacing = 0;

    public GTree()
    {
        setModel(model);
    }

    private GTreeModel createDefaultTreeModel()
    {
        GDefaultMutableTreeNode root = new GDefaultMutableTreeNode("Colors");
        GDefaultMutableTreeNode white = new GDefaultMutableTreeNode("White");
        root.add(white);
        GDefaultMutableTreeNode gray = new GDefaultMutableTreeNode("Gray");
        root.add(gray);
        GDefaultMutableTreeNode lightgray = new GDefaultMutableTreeNode("LightGray");
        gray.add(lightgray);
        GDefaultMutableTreeNode darkgray = new GDefaultMutableTreeNode("DarkGray");
        gray.add(darkgray);
        GDefaultMutableTreeNode black = new GDefaultMutableTreeNode("Black");
        root.add(black);
        GDefaultMutableTreeNode red = new GDefaultMutableTreeNode("Red");
        root.add(red);
        GDefaultMutableTreeNode green = new GDefaultMutableTreeNode("Green");
        root.add(green);
        GDefaultMutableTreeNode blue = new GDefaultMutableTreeNode("Blue");
        root.add(blue);
        GDefaultTreeModel model = new GDefaultTreeModel(root);
        return model;
    }

    public void expandRow(int row) 
    {
        ItemWrapper item = renderedItems.get(row);
        setExpandedState(item);
        validate();
    }

    public List<Integer> getExpandedAbsoluteRows()
    {
        List<Integer> rows = new ArrayList<>();
        for(int i=0;i<expanded.size();i++)
        {
            if(expanded.get(i) == true)
            {
                rows.add(i);
            }
        }
        return rows;
    }
    
    public void expandAbsoluteRows(List<Integer> rows)
    {
        for(int i=0;i<rows.size();i++)
        {
            expanded.set(rows.get(i), true);
        }
        revalidate();
    }
    
    public void setRootVisible(boolean rootVisible) 
    {
        this.rootVisible = rootVisible;
        setModel(model);
    }
    
    public void setModel(GTreeModel model)
    {
        if(this.model != null)
        {
            model.removeTreeModelListener(modelListener);
        }
        this.model = model;
        model.addTreeModelListener(modelListener);
        getPeer().removeAllChildren();
        renderedItems.clear();
        expanded.clear();
        map.clear();
        reverseMap.clear();
        preferredHeight = 0;
        preferredWidth = 0;
        Object root = model.getRoot();
        if(rootVisible)
        {
            createTreeItems(root, 0);
        }
        else
        {
            for(int i=0;i<model.getChildCount(root);i++)
            {
                createTreeItems(model.getChild(root, i), 0);
            }
        }
        setCachedPreferredSize(null);
        revalidate();
    }
    
    private void createTreeItems(Object object, int indentation)
    {
        GComponent comp = renderer.getTreeCellRendererComponent(this, object, false, false, false, renderedItems.size(), false);
        comp = new ItemWrapper(comp, object, renderedItems.size(), indentation);
        ItemWrapper iw = (GTree<T>.ItemWrapper) comp;
        renderedItems.add(iw);
        expanded.add(false);
        if(indentation > 0)
        {
            comp.setVisible(false);
        }
        map.put((GTree<T>.ItemWrapper) comp, object);
        reverseMap.put(object, (GTree<T>.ItemWrapper) comp);
        getPeer().appendChild(comp.getPeer());
        int count = model.getChildCount(object);
        int newIndent = indentation + 1;
        for(int i=0;i<count;i++)
        {
            Object obj = model.getChild(object, i);
            createTreeItems(obj, newIndent);
        }
    }

    @Override
    public void validate()
    {
        super.validate();
        preferredHeight = 0;
        preferredWidth = 0;
        int y = 0;
        for(int i=0;i<renderedItems.size();i++)
        {
            ItemWrapper comp = renderedItems.get(i);
            GDimension dim = null; 
            dim = comp.getPreferredSize();
            int indent = comp.getIndentation();
            if(comp.isVisible())
            {
                comp.setBounds(indent * 10, y, dim.width+1, dim.height);
                boolean exp = expanded.get(i);
                Object obj = map.get(comp);
                Stack<Object> stack = new Stack<>();
                stack.push(obj);
                while(stack.isEmpty() == false)
                {
                    obj = stack.pop();
                    int childCount = model.getChildCount(obj);
                    for(int x=0;x<childCount;x++)
                    {
                        Object cobj = model.getChild(obj, x);
                        GComponent child = reverseMap.get(cobj);
                        child.setVisible(exp);
                        stack.push(cobj);
                    }
                }
                y += dim.height + itemSpacing;
                comp.validate();
                preferredHeight += dim.height + itemSpacing;
            }
            if(preferredWidth < (indent * 10 + dim.width + 2))
            {
                preferredWidth = (indent * 10 + dim.width +2);
            }
        }
    }
    
    public void forceRepaint()
    {
        
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getExplizitPreferredSize();
        if(dim == null)
        {
            dim = getCachedPreferredSize();
            if(dim == null)
            {
                int w = preferredWidth + getStyleExtraWidth();
                int h = preferredHeight + getStyleExtraHeight();
                dim = new GDimension(w, h);
                setCachedPreferredSize(dim);
            }
        }
        return dim;
    }

    public GTreeCellRenderer getCellRenderer()
    {
        return renderer;
    }

    public void setCellRenderer(GTreeCellRenderer renderer)
    {
        this.renderer = renderer;
    }
    
    private GMouseListener expandListener = new GMouseAdapter()
    {
        @Override
        public void mouseClicked(GMouseEvent evt) 
        {
            GComponent comp = (GComponent) evt.getSource();
            ItemWrapper iw = (GTree<T>.ItemWrapper) comp.getParent();
            setExpandedState(iw);
            getParent().validate();
        };
    };
    
    private void setExpandedState(ItemWrapper iw)
    {
        int index = (int) iw.getClientProperty("row");
        boolean exp = expanded.get(index);
        expanded.set(index, !exp);
        iw.updateHandle(!exp);
        setCachedPreferredSize(null);
    }
    
    public GTreePath getSelectionPath()
    {
        GTreePath path = null;
        if(selectedRows.size() > 0)
        {
            int row = selectedRows.iterator().next();
            ItemWrapper iw = renderedItems.get(row);
            Object value = iw.getValue();
            path = new GTreePath(value);
        }
        return path;
    }
    
    private GMouseListener selectionListener = new GMouseAdapter()
    {
        @Override
        public void mouseClicked(GMouseEvent evt) 
        {
            if(GSwingUtilities.isLeftMouseButton(evt))
            {
                GComponent comp = (GComponent) evt.getSource();
                ItemWrapper iw = (GTree<T>.ItemWrapper) comp.getParent();
                int index = (int) iw.getClientProperty("row");
                if(evt.isControlDown() == false)
                {
                    List<Integer> sel = new ArrayList<>(selectedRows);
                    selectedRows.clear();
                    for(int i : sel)
                    {
                        renderedItems.get(i).updateElement();
                    }
                }
                selectedRows.add(index);
                if(treeSelectionListeners != null)
                {
                    GTreeSelectionEvent tevt = new GTreeSelectionEvent(GTree.this, getSelectionPath());
                    for(int i=0;i<treeSelectionListeners.size();i++)
                    {
                        treeSelectionListeners.get(i).valueChanged(tevt);
                    }
                }
                GPoint point = GSwingUtilities.convertPoint((GComponent) evt.getSource(), evt.getX(), evt.getY(), GTree.this);
                GMouseEvent tevt = new GMouseEvent(GTree.this, point.x, point.y, evt.getClickCount(), evt.getButton());
                for(int i=0;GTree.this.mouseListeners != null && i < GTree.this.mouseListeners.size();i++)
                {
                    GMouseListener l = GTree.this.mouseListeners.get(i);
                    l.mouseClicked(tevt);
                }
                iw.updateElement();
            }
            else
            {
                GPoint point = GSwingUtilities.convertPoint((GComponent) evt.getSource(), evt.getX(), evt.getY(), GTree.this);
                GMouseEvent tevt = new GMouseEvent(GTree.this, point.x, point.y, evt.getClickCount(), evt.getButton());
                for(int i=0;GTree.this.mouseListeners != null && i < GTree.this.mouseListeners.size();i++)
                {
                    GMouseListener l = GTree.this.mouseListeners.get(i);
                    l.mouseClicked(tevt);
                }
            }
        }
        
        @Override
        public void mouseClickedLong(GMouseEvent evt) 
        {
            GPoint point = GSwingUtilities.convertPoint((GComponent) evt.getSource(), evt.getX(), evt.getY(), GTree.this);
            GMouseEvent tevt = new GMouseEvent(GTree.this, point.x, point.y, evt.getClickCount(), evt.getButton());
            for(int i=0;GTree.this.mouseListeners != null && i < GTree.this.mouseListeners.size();i++)
            {
                GMouseListener l = GTree.this.mouseListeners.get(i);
                l.mouseClickedLong(tevt);
            }
        }
    };

    private static GFont font;
    
    static class Handle extends GLabel
    {
        static GDimension prefSize;
        
        @Override
        public GDimension getPreferredSize()
        {
            if(prefSize == null)
            {
                prefSize = super.getPreferredSize();
            }
            return prefSize;
        }
    }
    
    class ItemWrapper extends GComponent
    {
        private GLabel handle = new Handle();
        private int row;
        private GComponent renderedItem;
        private int indentation;
        
        public ItemWrapper(GComponent renderedItem, Object value, int row, int indentation)
        {
            this.indentation = indentation;
            this.renderedItem = renderedItem;
            handle.setText("\u25b9");
            setOpaque(false);
            handle.setOpaque(false);
            this.row = row;
            setLayout(new GBorderLayout());
            add(renderedItem, GBorderLayout.CENTER);
            add(handle, GBorderLayout.WEST);
            handle.addMouseListener(expandListener);
            renderedItem.addMouseListener(selectionListener);
            putClientProperty("row", row);
            putClientProperty("value", value);
            if(font == null)
            {
                font = new GFont("Courier New", GFont.BOLD, (int) handle.getFont().getSize2D());
            }
            handle.setFont(font);
        }

        @Override
        public void setVisible(boolean visible)
        {
            super.setVisible(visible);
            if(visible == false)
            {
                if(selectedRows.remove(row))
                {
                    updateElement();
                }
            }
        }
        
        protected Object getValue()
        {
            return getClientProperty("value");
        }
        
        public void updateElement()
        {
            renderedItem.removeMouseListener(selectionListener);
            GComponent comp = renderer.getTreeCellRendererComponent(GTree.this, getClientProperty("value")
                , selectedRows.contains(row), expanded.get(row), false, row, false);
            comp.setBounds(renderedItem.getLocation().x, renderedItem.getLocation().y
                , renderedItem.getSize().width, renderedItem.getSize().height);
            replace(renderedItem, comp);
            renderedItem = comp;
            renderedItem.addMouseListener(selectionListener);
        }

        private void replace(GComponent renderedItem, GComponent comp)
        {
            remove(renderedItem);
            add(comp, GBorderLayout.CENTER);
        }

        public void updateHandle(boolean expanded)
        {            
            handle.setText(expanded ? "\u25bf" : "\u25b9");
        }

        protected int getRow()
        {
            return row;
        }

        public int getIndentation()
        {
            return indentation;
        }
    }

    public void addTreeSelectionListener(GTreeSelectionListener treeSelectionListener)
    {
        if(treeSelectionListeners == null)
        {
            treeSelectionListeners = new ArrayList<>();
        }
        treeSelectionListeners.add(treeSelectionListener);
    }

    public GTreeModel getModel()
    {
        return model;
    }
    
    class ModelListener implements GTreeModelListener
    {
        @Override
        public void treeStructureChanged(GTreeModelEvent e)
        {
            setModel(getModel());
        }

        @Override
        public void treeNodesChanged(GTreeModelEvent e)
        {
            Object val = e.getTreePath().getLastPathComponent();
            if(val == null)
            {
                ItemWrapper iw = renderedItems.get(0);
                iw.updateElement();
            }
            else
            {
                ItemWrapper ciw = reverseMap.get(val);
                if(ciw != null)
                {
                    ciw.updateElement();
                }
//                for(int i=0;i<model.getChildCount(val);i++)
//                {
//                    Object child = model.getChild(val, i);
//                    ItemWrapper ciw = reverseMap.get(child);
//                    ciw.updateElement();
//                }
            }
        }
    }

    /**
     * Returns the GTreePath for the given x-y Koordinates.
     * @param x
     * @param y
     * @return
     */
    public GTreePath getPathForLocation(int x, int y)
    {
        GTreePath path = null;
        for(int i=0;i<renderedItems.size();i++)
        {
            GTree<T>.ItemWrapper item = renderedItems.get(i);
            if(y < item.getLocation().y + item.getSize().height)
            {
                path = new GTreePath(map.get(item));
                break;
            }
        }
        return path;
    }

    public int getItemSpacing()
    {
        return itemSpacing;
    }

    public void setItemSpacing(int itemSpacing)
    {
        this.itemSpacing = itemSpacing;
    }
}
