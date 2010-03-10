package mockdesigner.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import mockdesigner.component.Component;

/**
 * @author Manabu Shibata
 */
public class DrawingTool extends Tool {

    protected List<DrawingListener> listeners = new ArrayList<DrawingListener>();
    
    protected boolean isDragging;

    protected Color color1;
    protected Color color2;

    protected int currentZ = 0;

    public void drawCandidate(Graphics g){}

    @Override
    public void mouseMoved(MouseEvent e) {
        currentPoint = new Point(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPoint = new Point(e.getPoint());
        notifyShouldRepaint();
    }

    public void addDrawingListener(DrawingListener listener) {
        this.listeners.add(listener);
    }

    public void removeDrawingListener(DrawingListener listener) {
        this.listeners.remove(listener);
    }

    public void notifyComponentCreated(Component component) {
        for(DrawingListener listener : listeners) {
            listener.componentCreated(component);
        }
    }

    public void notifyShouldRepaint() {
        for (DrawingListener listener : listeners) {
            listener.shouldRepaint();
        }
    }
    
    public int getCurrentZ() {
        return currentZ;
    }

    public void setCurrentZ(int currentZ) {
        this.currentZ = currentZ;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }
}
