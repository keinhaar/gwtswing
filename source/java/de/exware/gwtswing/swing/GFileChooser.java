package de.exware.gwtswing.swing;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.exware.gwtswing.awt.GBorderLayout;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GGridLayout;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.io.GFile;
import de.exware.gwtswing.io.GFileSystemAsync;
import de.exware.gwtswing.swing.table.GAbstractTableModel;
import de.exware.gwtswing.swing.table.GTableManager;

public class GFileChooser extends GComponent
{
    private GFileSystemAsync filesystem;
    private GFile start;
    private GButton ok;
    private GTable<GFile> table;
    private GDialog dlg;
    private GFile[] actualFileList;
    private GTableManager tableManager;
    private GFile selectedFile;
    
    public GFileChooser(GFileSystemAsync filesystem)
    {
        this(filesystem, null);
    }
    
    public GFileChooser(GFileSystemAsync filesystem, GFile start)
    {
        this.filesystem = filesystem;
        this.start = start;
        setLayout(new GBorderLayout());
        
        table = new GTable();
        table.addMouseListener(new GMouseAdapter()
        {
            @Override
            public void mouseClicked(GMouseEvent e)
            {
                if(e.getClickCount() > 1)
                {
                    int index = table.getSelectedRows()[0];
                    GFile file = actualFileList[index];
                    filesystem.listFiles(file, new FileSystemCallBack());
                }
            }
        });
        GScrollPane spane = new GScrollPane(table);
        spane.setPreferredSize(new GDimension(500,300));
        add(spane, GBorderLayout.CENTER);
        
        filesystem.getRoots(new FileSystemCallBack());
        
        GPanel buttons = new GPanel();
        buttons.setLayout(new GGridLayout(1, 2));
        add(buttons, GBorderLayout.SOUTH);
        ok = new GButton("");
        buttons.add(ok);
        String cancelText = GUIManager.getString("CANCEL.text");;
        GButton cancel = new GButton(cancelText);
        buttons.add(cancel);
        ok.addActionListener(new GActionListener()
        {
            @Override
            public void actionPerformed(GActionEvent evt)
            {
                int[] rows = table.getSelectedRows();
                if(rows.length > 0)
                {
                    int row = table.convertRowIndexToModel(rows[0]);
                    selectedFile = actualFileList[row];
                }
                dlg.hide();
                dlg.callback();
            }
        });
        cancel.addActionListener(new GActionListener()
        {
            @Override
            public void actionPerformed(GActionEvent evt)
            {
                dlg.hide();
            }
        });
        table.setSortable(true);
        tableManager = new GTableManager(table, "fileChooser");
    }
    
    public void showOpenDialog(GDialogCallback callback)
    {
        String okText = GUIManager.getString("OPEN.text");
        ok.setText(okText);
        dlg = new GDialog(callback);
        dlg.getContentPane().add(this);
        dlg.pack();
        dlg.setLocationRelativeTo(null);
        dlg.show();
    }
    
    class FileSystemCallBack implements AsyncCallback<GFile[]>
    {
        @Override
        public void onFailure(Throwable caught)
        {
            caught.printStackTrace();
        }

        @Override
        public void onSuccess(GFile[] result)
        {
            if(result != null)
            {
                actualFileList = result;
                table.setModel(new GFileTableModel(result));
                tableManager.resetColumnSizes();
                GScrollPane spane = (GScrollPane) table.getParent();
                spane.refitContent();
                table.revalidate();
            }
        }
    }
    
    class GFileTableModel extends GAbstractTableModel
    {
        private List<GFile> files;
        
        public GFileTableModel(GFile[] result)
        {
            files = Arrays.asList(result);
        }
        
        @Override
        public String getColumnName(int col)
        {
            String value = null;
            switch(col)
            {
                case 0:
                    value = "Name";
                break;
                case 1:
                    value = "Size";
                break;
                case 2:
                    value = "Modified";
                break;
            }
            return value;
        }
        
        @Override
        public int getColumnCount()
        {
            return 3;
        }

        @Override
        public int getRowCount()
        {
            return files.size();
        }

        @Override
        public Object getValueAt(int row, int column)
        {
            GFile file = files.get(row);
            Object value = null;
            switch(column)
            {
                case 0:
                    value = file.getName();
                break;
                case 1:
                    value = file.getSize();
                break;
                case 2:
                    value = file.getModificationTime();
                break;
            }
            return value;
        }
        
    }

    public GFile getSelectedFile()
    {
        return selectedFile;
    }

    public void setSelectedFile(GFile file)
    {
        filesystem.listFiles(file, new FileSystemCallBack());
    }
}
