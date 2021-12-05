package de.exware.gwtswing.upload;

import com.google.gwt.dom.client.Document;

import de.exware.gwtswing.swing.GComponent;;

public class FileInputButton extends GComponent
{
    public FileInputButton()
    {
        super(Document.get().createFileInputElement());
    }
}
