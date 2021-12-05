package de.exware.gwtswing.upload;

import static de.exware.gwtswing.swing.GUIManager.getString;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;

import de.exware.gwtswing.Constants;
import de.exware.gwtswing.PartitionedPanel;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridLayout;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;
import de.exware.gwtswing.swing.GButton;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GIcon;
import de.exware.gwtswing.swing.GImageIcon;
import de.exware.gwtswing.swing.GLabel;
import de.exware.gwtswing.swing.GOptionPane;
import de.exware.gwtswing.swing.GUtilities;
import de.exware.gwtswing.swing.border.GBorderFactory;
import de.exware.gwtswing.swing.border.GLineBorder;;

/**
 * A Upload Button that supports multiple File Upload as well as Styling like
 * other GComponents.
 */
public class UploadButton extends GComponent
{
    public static final String ENCODING_MULTIPART = "multipart/form-data";
    private List<BrowserFile> files;
    private ProgressLabel progress;
    private GButton submit;
    private FormElement form;
    private boolean simple;
    private InputLabel ilabel;
    private List<UploadListener> uploadListeners;

    public UploadButton(String url)
    {
        this(url, null, false);
    }

    public UploadButton(String url, boolean simple)
    {
        this(url, null, simple);
    }

    public UploadButton(String url, String uploadText, boolean simple)
    {
        super(Document.get().createFormElement());
        this.simple = simple;
        setLayout(new GGridLayout(1, 1));
        form = (FormElement) getPeer();
        form.setAction(url);
        form.setEnctype(ENCODING_MULTIPART);
        form.setMethod("POST");
        PartitionedPanel panel = new PartitionedPanel(1);
        panel.getPeer().addClassName("gwts-UploadButton-box");
        if(simple)
        {
            
        }
        else
        {
            setBorder(new GLineBorder(GColor.BLACK, 2, BorderStyle.DASHED));
            panel.setBorder(GBorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        panel.setAnchor(GGridBagConstraints.CENTER);
        panel.setIndentSize(0);
        add(panel);
        if(simple == false)
        {
            GIcon icon = new GImageIcon(GUtilities.getResource(Constants.PLUGIN_ID, "/icons/upload.svg"), 24, 24);
            GLabel iconLabel = new GLabel(icon);
            iconLabel.getPeer().addClassName("gwts-UploadButton-icon");
            iconLabel.setOpaque(false);
            panel.add(iconLabel);//, GGridBagConstraints.HORIZONTAL, 1, 1);
        }
        final InputElement fileInput = Document.get().createFileInputElement();
        String id = "" + Math.random();
        fileInput.setAttribute("id", id);
        fileInput.setAttribute("multiple", "");
        fileInput.addClassName("gwts-UploadButton-input");
        panel.getPeer().appendChild(fileInput);
        Event.sinkEvents(fileInput, Event.getEventsSunk(fileInput) | Event.ONCHANGE);
        Event.setEventListener(getPeer(), new EventListener()
        {
            @Override
            public void onBrowserEvent(Event event)
            {
                if (event.getTypeInt() == event.ONCHANGE)
                {
                    JavaScriptObject obj = (JavaScriptObject) fileInput.getPropertyObject("files");
                    BrowserFileList list = obj.cast();
                    files = list.getFiles();
                    if(files.size() > 0)
                    {
                        submit.setEnabled(true);
                        if(simple)
                        {
                            submit.setVisible(true);
                            ilabel.setVisible(false);
                            UploadButton.this.revalidate();
                        }
                    }
                }
            }
        });
        if (uploadText == null)
        {
            uploadText = getString("upload.text");
        }
        
        ilabel = new InputLabel(getString("upload.file.select.text"));
        ilabel.setTarget(id);
        panel.add(ilabel);
        
        submit = new GButton(uploadText);
        if(simple)
        {
            ilabel.getPeer().addClassName("gwts-GButton");
//            ilabel.setBackground(GUIManager.getColor(".gwts-GButton/background-color"));
            ilabel.setBorder(submit.getBorder());
            submit.setVisible(false);
        }
        else
        {
            submit.setEnabled(false);
        }
        submit.addActionListener(new GActionListener()
        {
            @Override
            public void actionPerformed(GActionEvent evt)
            {
                submit();
            }
        });
        panel.add(submit, GGridBagConstraints.HORIZONTAL, 1, 1);
        progress = new ProgressLabel();
        panel.add(progress, GGridBagConstraints.HORIZONTAL, 1, 1);
    }

    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = super.getPreferredSize();
        dim.width += 6;
        return dim;
    }
    
    @Override
    public void setVisible(boolean visible)
    {
        boolean submitVisibility = submit.isVisible();
        super.setVisible(visible);
        submit.setVisible(submitVisibility);
    }
    
    public void setURL(String url)
    {
        form.setAction(url);
    }
    
    public void submit()
    {
        if(files != null)
        {
            submit.setEnabled(false);
            progress.setProgress(0);
            doUpload(form.getAction(), form, files.toArray());
        }
    }

    class ProgressLabel extends GComponent
    {
        int progress = 0;
        
        public ProgressLabel()
        {
            setBackground(GColor.BLUE);
            setPreferredSize(new GDimension(1, 5));
        }
        
        @Override
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, width, height);
            getPeer().getStyle().setWidth(progress, Unit.PCT);
        }
        
        void setProgress(int p)
        {
            progress = p;
            getPeer().getStyle().setWidth(p, Unit.PCT);
        }
    }
    
    /**
     * Erzeugt die Daten für den Upload Request.
     * 
     * @param objects
     * @param element
     * @return
     */
    private native void doUpload(String action, Element form, Object[] files)
    /*-{
		var that = this;
		var data = new FormData(form);
		for (var i = 0; i < files.length; i++) 
		{
            data.append(files[i].name, files[i], "" + files[i].lastModified);
		}
		var request = new XMLHttpRequest();
		request.addEventListener("error", function(event) 
		{
			that.@de.exware.gwtswing.upload.UploadButton::uploadError(I)(request.status);
		});
		request.addEventListener("load", function(event) 
		{
		    if(request.status == 200)
		    {
			    that.@de.exware.gwtswing.upload.UploadButton::uploadComplete()();
			}
			else
			{
                that.@de.exware.gwtswing.upload.UploadButton::uploadError(I)(request.status);
			}
		});
		request.open("POST", action);
		request.upload.onprogress = function(event) 
		{
			that.@de.exware.gwtswing.upload.UploadButton::progress(II)(event.loaded, event.total);
		};
		request.send(data);
    }-*/;

    private void uploadComplete()
    {
        GOptionPane.showMessageDialog(UploadButton.this, "Upload Success");
        finished();
        for(int i=0;i<uploadListeners.size();i++)
        {
            uploadListeners.get(i).uploadSuccessful();
        }
    }

    private void uploadError(int status)
    {
        GOptionPane.showMessageDialog(this, "Upload failed. Status: " + status);
        finished();
        for(int i=0;i<uploadListeners.size();i++)
        {
            uploadListeners.get(i).uploadFailed();
        }
    }

    private void finished()
    {
        Timer t = new Timer()
        {
            @Override
            public void run()
            {
                submit.setVisible(false);
                ilabel.setVisible(true);
                submit.setEnabled(true);
                UploadButton.this.revalidate();
                progress.getPeer().getStyle().setWidth(0, Unit.PCT);
            }
        };
        t.schedule(1000);
    }

    /**
     * Wird aufgerufen um den Fortschritt auf dem Widget anzuzeigen.
     */
    private void progress(int loaded, int total)
    {
        int p = loaded * 100 / total;
        progress.getPeer().getStyle().setWidth(p, Unit.PCT);
    }

    class InputLabel extends GComponent
    {
        public InputLabel(String string)
        {
            super(Document.get().createLabelElement());
            setText(string);
            getPeer().addClassName("gwts-UploadButton-label");
            if(simple == false)
            {
                setOpaque(false);
            }
        }
    
        public String getText()
        {
            return getPeer().getInnerText();
        }
    
        public void setText(String text)
        {
            getPeer().setInnerText((text == null) ? "" : text);
            setCachedPreferredSize(null);
        }
    
        public void setTarget(String target)
        {
            getPeer().setAttribute("for", target);
        }
        
        @Override
        public GDimension getPreferredSize()
        {
            GDimension dim = getCachedPreferredSize();
            if(dim == null)
            {
                dim = super.getPreferredSize();
                dim.width ++;
            }
            return dim;
        }
    }

    public void addUploadListener(UploadListener uploadListener)
    {
        if(uploadListeners == null)
        {
            uploadListeners = new ArrayList<>();
        }
        uploadListeners.add(uploadListener);
    }
}