package de.exware.gwtswing.swing.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.exware.gplatform.GPStorage;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GComponentAdapter;
import de.exware.gwtswing.awt.event.GComponentEvent;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.swing.GAbstractAction;
import de.exware.gwtswing.swing.GAction;
import de.exware.gwtswing.swing.GPopupMenu;
import de.exware.gwtswing.swing.GSwingUtilities;
import de.exware.gwtswing.swing.GTable;
import de.exware.gwtswing.swing.GUIManager;
import de.exware.gwtswing.swing.TableFilter;
import de.exware.gwtswing.swing.event.GColumnEvent;
import de.exware.gwtswing.swing.event.GColumnListener;
import de.exware.gwtswing.swing.event.GTableModelEvent;
import de.exware.gwtswing.swing.event.GTableModelListener;

/**
 * A class, which can track the size of columns to store and recreate on next use. 
 * @author martin
 */
public class GTableManager
{
    private GTable<?> table;
    private String storageKeyBase;
    private Map<Integer, GAction> filterActions = new HashMap<>();
    
    /**
     * @param table
     * @param storageKeyBase The key that will be used to store information in the browsers storage.
     * Will be prefixed by "TableManager." and extended by the type of value that is stored. For example ".columnSizes"
     */
    public GTableManager(GTable<?> table, String storageKeyBase)
    {
        this.table = table;
        this.storageKeyBase = "GTableManager." + storageKeyBase;
        table.getModel().addTableModelListener(new GTableModelListener()
        {
            @Override
            public void tableChanged(GTableModelEvent evt)
            {
                GSwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        resetColumnSizes();
                    }
                });
            }
        });
        table.addColumnListener(new GColumnListener()
        {
            @Override
            public void columnResized(GColumnEvent evt)
            {
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<table.getColumnCount();i++)
                {
                    if(i>0) sb.append(';');
                    sb.append(table.getColumnWidth(i));
                }
                GPStorage storage = GPlatform.getInstance().getLocalStorage();
                storage.setItem(GTableManager.this.storageKeyBase + ".columnSizes", sb.toString());
            }
        });
        resetColumnSizes();
        table.getTableHeader().addMouseListener(new InternalMouseListener());
    }

    class InternalMouseListener extends GMouseAdapter
    {
        @Override
        public void mouseClicked(GMouseEvent evt)
        {
            super.mouseClicked(evt);
            if(GSwingUtilities.isRightMouseButton(evt))
            {
                int col = table.columnAtPoint(evt.getPoint());
                if(col >= 0)
                {
                    showFilterMenu(col, evt.getX(), evt.getY());
                }
            }
        }
    }
    
    public void resetColumnSizes()
    {
        GPStorage storage = GPlatform.getInstance().getLocalStorage();
        String sizes = storage.getItem(storageKeyBase + ".columnSizes");
        if(sizes != null)
        {
            doSize(sizes);
        }
        else
        {
            if(table.getWidth() > 0)
            {
                int width = table.getWidth();
                int cols = table.getColumnCount();
                sizes = "";
                for(int i=0;i<cols;i++)
                {
                    sizes += width / cols + ";";
                }
                doSize(sizes);
            }
            table.addComponentListener(new GComponentAdapter()
            {
                @Override
                public void componentResized(GComponentEvent e)
                {
                    int width = table.getWidth();
                    int cols = table.getColumnCount();
                    String sizes = "";
                    for(int i=0;i<cols;i++)
                    {
                        sizes += width / cols + ";";
                    }
                    doSize(sizes);
                    table.removeComponentListener(this);
                }
            });
        }
    }
    
    private void doSize(String sizes)
    {
        String[] tokens = sizes.split(";");
        for(int i=0;i<tokens.length && i < table.getColumnCount();i++)
        {
            int width = Integer.parseInt(tokens[i]);
            table.setColumnWidth(i, width);
        }
    }
    
    public void showFilterMenu(int column, int x, int y)
    {
        GPopupMenu menu = new GPopupMenu();
        if(table.isFiltered(column))
        {
            GAction action = new GAbstractAction(GUIManager.getString("resetFilter.text"))
            {
                @Override
                public void actionPerformed(GActionEvent evt)
                {
                    table.setFilter(column, null);
                }
            };
            menu.add(action);
        }
        GAction action = filterActions.get(column);
        if(action != null)
        {
            menu.add(action);
        }
        else if(filterActions.containsKey(column))
        {
            addAllPossibleValues(column, menu);
        }
        if(menu.getComponentCount() > 0)
        {
            menu.show(table.getTableHeader(), x + 3, y + 3);
        }
    }

    private void addAllPossibleValues(int column, GPopupMenu menu)
    {
        GTableModel model = table.getModel();
        int rowCount = model.getRowCount();
        Set possibleValues = new HashSet<>();
        for(int i=0;i<rowCount;i++)
        {
            Object value = model.getValueAt(i, column);
            possibleValues.add(value);
        }
        for(Object obj : possibleValues)
        {
            if(obj == null) continue;
            String name = obj.toString();
            GAction action = new GAbstractAction(name)
            {
                @Override
                public void actionPerformed(GActionEvent evt)
                {
                    table.setFilter(column, new TableFilter()
                    {
                        @Override
                        public boolean accept(Object value)
                        {
                            return obj.equals(value);
                        }
                    });
                }
            };
            menu.add(action);
        }
    }

    public void setFilterAction(int column, GAction action)
    {
        filterActions.put(column, action);
    }

    public void setAllPossibleValuesFilter(int column)
    {
        filterActions.put(column, null);
    }
}
