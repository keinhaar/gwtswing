package de.exware.gwtswing.awt;

import java.util.HashMap;
import java.util.Map;

import de.exware.gplatform.element.GPContext2d;
import de.exware.gwtswing.awt.geom.Ellipse2D;

/**
 * A Swing like GGraphics2D Context.
 * @author martin
 *
 */
public class GGraphics2D
{
    private GPContext2d graphics;
    private GFont font;
    private static Map<GFont, GFontMetrics> metricsCache = new HashMap<>();

    public GGraphics2D(GPContext2d graphics)
    {
        this.graphics = graphics;
        graphics.setTransform(1,0,0,1,0,0);
    }
    
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        graphics.save();
        graphics.translate(0.5, 0.5);
        graphics.moveTo(x1, y1);
        graphics.lineTo( x2, y2);
        graphics.stroke();
        graphics.restore();
    }

    public void drawRect(int x, int y, int width, int height)
    {
        graphics.save();
        graphics.translate(0.5, 0.5);
        graphics.rect(x, y, width, height);
        graphics.stroke();
        graphics.restore();
    }
    
    public void draw(GShape shape)
    {
        if(shape instanceof Ellipse2D)
        {
            Ellipse2D ell = (Ellipse2D) shape;
            drawEllipseHelper(graphics, false, ell.getX(), ell.getY(), ell.getWidth(), ell.getHeight());
        }
        else
        {
            throw new UnsupportedOperationException("This type of Shape is not supported: " + shape.getClass().getName());
        }
    }

    public void fill(GShape shape)
    {
        if(shape instanceof Ellipse2D)
        {
            Ellipse2D ell = (Ellipse2D) shape;
            drawEllipseHelper(graphics, true, ell.getX(), ell.getY(), ell.getWidth(), ell.getHeight());
        }
        else
        {
            throw new UnsupportedOperationException("This type of Shape is not supported: " + shape.getClass().getName());
        }
    }

    private static void drawEllipseHelper(GPContext2d ctx, boolean fill, double x, double y, double w, double h) 
    {
        double kappa = .5522848f;
        double ox = w / 2 * kappa; // control point offset horizontal
        double oy = h / 2 * kappa; // control point offset vertical
        double xe = x + w; // x-end
        double ye = y + h; // y-end
        double xm = x + w / 2; // x-middle
        double ym = y + h / 2; // y-middle

        ctx.beginPath();
        ctx.moveTo(x, ym);
        ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
        ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
        ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
        ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
        ctx.stroke();
        if (fill) 
        {
            ctx.fill();
        }
    }
    
    public void fillRect(int x, int y, int width, int height)
    {
        graphics.fillRect(x, y, width, height);
    }

    public void drawString(String text, int x, int y)
    {
        graphics.fillText(text, x, y);
    }

    public void setColor(GColor color)
    {
        graphics.setStrokeColor(color.toHex());
        graphics.setFillColor(color.toHex());
    }

    public void drawImage(GImage image, int x, int y)
    {
        graphics.drawImage(image.getImageElement(), x, y);
    }

    public void drawImage(GImage image, int x, int y, int w, int h)
    {
        graphics.drawImage(image.getImageElement(), x, y, w, h);
    }

    public void drawImage(GImage image, int destX, int destY, int destX2, int destY2, int srcX, int srcY, int srcX2, int srcY2)
    {
        graphics.drawImage(image.getImageElement()
            , srcX, srcY, srcX2-srcX, srcY2-srcY
            , destX, destY, destX2-destX, destY2-destY);
    }
    
    public void setTransform()
    {
        graphics.rotate(90);
    }

    /**
     * Rotation in Radians.
     * @param rotate
     */
    public void setRotate(double rotate)
    {
        graphics.rotate(rotate);
    }

    /**
     * Rotation in Radians.
     * @param rotate
     */
    public void setTranslate(double x, double y)
    {
        graphics.translate(x, y);
    }

    /**
     * Scaling.
     * @param 
     */
    public void setScale(double x, double y)
    {
        graphics.scale(x, y);
    }

    public void setFont(GFont gFont)
    {
        graphics.setFont(gFont.toCSS());
        this.font = gFont;
    }
    
    public GFontMetrics getFontMetrics()
    {
        GFontMetrics fm = metricsCache.get(font);
        if(fm == null)
        {
            fm = new GFontMetrics(font);
            metricsCache.put(font, fm);
        }
        return fm;
    }
}
