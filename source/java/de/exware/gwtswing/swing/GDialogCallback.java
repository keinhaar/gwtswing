package de.exware.gwtswing.swing;

public interface GDialogCallback<T extends GDialog>
{
    public void execute(T dlg);
}