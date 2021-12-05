package de.exware.gwtswing.io;

import java.io.Serializable;

/**
 * This class is not really a replacement of java.io.File. It's main purpose is to allow File Selection
 * with GFileChooser.
 * <b>You can't select local Files with this class!!</b>
 * Instead you will need a Server Side Component that allows selection of Files and provides Directory Information.
 * @author martin
 *
 */
public class GFile implements Serializable
{
    private long size;
    private long modificationTime;
    private String[] path;
    private boolean isDirectory;
    
    public GFile()
    {
    }
    
    public GFile(String path, long size, long modificationTime)
    {
        this(path, size, modificationTime, false);
    }
    
    public GFile(String path, long size, long modificationTime, boolean isDirectory)
    {
        path = path.replace('\\', '/');
        if(path.endsWith("/"))
        {
            path = path.substring(0, path.length()-1);
        }
        this.path = path.split("/");
        this.size = size;
        this.modificationTime = modificationTime;
        this.isDirectory = isDirectory;
    }
    
    public GFile(String path, boolean isDirectory)
    {
        this(path, 0, 0, isDirectory);
    }
    
    @Override
    public String toString()
    {
        return getPath();
    }
    
    public long getSize()
    {
        return size;
    }
    
    public String getName()
    {
        return path[path.length-1];
    }
    
    public long getModificationTime()
    {
        return modificationTime;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    public String getPath()
    {
        String path = "";
        for(int i=0;i<this.path.length;i++)
        {
            path += "/";
            path += this.path[i];
        }
        return path;
    }
}
