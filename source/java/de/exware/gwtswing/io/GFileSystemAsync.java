package de.exware.gwtswing.io;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GFileSystemAsync
{
    void getRoots(AsyncCallback<GFile[]> callback);

    void listFiles(GFile parent, AsyncCallback<GFile[]> callback);
}
