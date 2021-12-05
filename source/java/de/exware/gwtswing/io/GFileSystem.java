package de.exware.gwtswing.io;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(GFileSystem.RELATIVE_SERVICE_PATH)
public interface GFileSystem extends RemoteService
{
    public static final String RELATIVE_SERVICE_PATH = "GFileSystem";

    /**
     * Get an Array of FileSystem Roots. Implementation must make sure, that no files outside of this roots 
     * are accessible.
     * @return
     */
    public GFile[] getRoots();
    
    /**
     * Provides a List of Files in the given Directory.
     * @param parent
     * @return
     * @throws IOException 
     */
    public GFile[] listFiles(GFile parent) throws IOException;
}
