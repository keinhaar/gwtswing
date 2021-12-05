# GWTSwing
This is an UI API similar to Java's Swing API, but on top of GWT. 

There are 2 main differences between Swing an GWTSwing.
1. There are no modal dialogs. Instead of that, callback objects can be used, to get notified if a dialog was closed.
2. All Classnames beginn with "G" instead of "J" (GTextField instead of JTextField). This was done to avoid license problems, because the API is now different from Oracles Swing and to avoid confusion while developing with GWTSwing.

There are no server calls after starting the application. Everything is handled locally on the client.

## How to use
GWTSwing is used like other gwt libraries. Just add

```
  <inherits name='de.exware.gwtswing'/>
```
to your projects ```abc.gwt.xml```.
Additionally you should add the css file to your html pages header
```
   <link type="text/css" rel="stylesheet" href="de.exware.gwtswing.sample/de/exware/gwtswing/gwtswing.css">
```

After that, you can use the GWTSwing classes in your project.
```
GPanel panel = new GPanel();
panel.setLayout(new GGridLayout(1, 1));
GLabel label = new GLabel("Hello World");
panel.add(label);
GUtilities.addToWidget(Document.get().getBody(), panel);
```
Looks like Swing? Thats the goal! :-) 

The call of 'GUtilities.addToWidget' appends your GWTSwing UI to the Web Page.

Porting an existing Swing application is now nearly as easy as replacing all "J" with "G".

## Demo
A small demo application, which shows some of the main UI components can be found here:

http://www.exware.de/gwtswing/demo.html

## Real live usage
Where is GWTSwing used

### keedo
An archive system for digital documents which allows to set markers on documents without modifying the original documents. Includes an automatic text recognition system (OCR) for scanned documents. A demo can be found on the projects homepage. https://keeitsi.com/

### Keeitsi homepage
The companies homepage was made with GWTSwing just to prove, that it is possible.

### FFManager
FFManager is an application to manage volunteer fire brigades. In it's newest release 4.0 the developers began to port the Swing UI to GWTSwing.
