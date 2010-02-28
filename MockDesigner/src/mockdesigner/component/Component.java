/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mockdesigner.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;
import org.jdom.Element;

/**
 *
 * @author Manabu Shibata
 */
public abstract class Component {

    public int x;
    public int y;
    public int z;

    public int height;
    public int width;
    
    public Color backgroundColor = Color.WHITE;
    public Color borderColor = Color.BLACK;

    public int originalX;
    public int originalY;
    public int originalW;
    public int originalH;
    public boolean isMoving = false;

    public boolean isSelect = false;

    protected static final int RADIUS = 4;

    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new TreeMap<String, Object>();
        properties.put("x", x);
        properties.put("y", y);
        properties.put("z", z);
        properties.put("width", width);
        properties.put("height", height);
        properties.put("Border color", colorToString(borderColor));
        properties.put("Background color", colorToString(backgroundColor));
        return properties;
    }
        
    protected enum MoveType {
        START, ALL, END, RESIZE
    };
    
    protected MoveType moveType = null;

    protected int dotProduct(Point p1, Point p2) {
        return p1.x * p2.x + p1.y * p2.y;
    }

    protected double distance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y));
    }
    
    public abstract void paint(Graphics g);
    public abstract boolean isOn(Point p);
    public void startMove(Point p) {
        originalX = x;
        originalY = y;
        originalW = width;
        originalH = height;
        isMoving = true;
    }
    
    public void cancelMove() {
        x = originalX;
        y = originalY;
        width = originalW;
        height = originalH;
        moveType = null;
        isMoving = false;
    }
    
    public abstract void move(Point p);
    
    public void endMove() {
        moveType = null;
        isMoving = false;
    }
    public boolean isMoving() {
        return isMoving;
    }

    public void updateProperty(String name, String value) {
        if ("x".equals(name)) {
            x = Integer.parseInt(value);
        } else if ("y".equals(name)) {
            y = Integer.parseInt(value);
        } else if ("z".equals(name)) {
            z = Integer.parseInt(value);
        } else if ("width".equals(name)) {
            width = Integer.parseInt(value);
        } else if ("height".equals(name)) {
            height = Integer.parseInt(value);
        } else if ("Border color".equals(name)) {
            borderColor = stringToColor(value);
        } else if ("Background color".equals(name)) {
            backgroundColor = stringToColor(value);
        }
    }

    public String colorToString(Color c) {
        if (c == null) return "";
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        return String.format("%02x%02x%02x", r, g, b);
    }

    public Color stringToColor(String s) {
        if (s.length() == 0) return null;
        
        if (s.length() != 6) throw new IllegalArgumentException("It should have at least 6 chars["+s+"]");
        int r = Integer.parseInt(s.substring(0,2), 16);
        int g = Integer.parseInt(s.substring(2,4), 16);
        int b = Integer.parseInt(s.substring(4,6), 16);
        return new Color(r, g, b);
    }

    public abstract Component copy();
    protected void migrate(Component c) {
        c.x = x;
        c.y = y;
        c.z = z;
        c.width = width;
        c.height = height;
        if (borderColor != null)
            c.borderColor = new Color(borderColor.getRGB());
        if (backgroundColor != null)
            c.backgroundColor = new Color(backgroundColor.getRGB());
    }

    public abstract Element toXML();
    
    public Element toXML(String name) {
        Element component = new Element(name);
        component.setAttribute("x", Integer.toString(x));
        component.setAttribute("y", Integer.toString(y));
        component.setAttribute("z", Integer.toString(z));
        component.setAttribute("width", Integer.toString(width));
        component.setAttribute("height", Integer.toString(height));
        component.setAttribute("borderColor", colorToString(borderColor));
        component.setAttribute("backgroundColor", colorToString(backgroundColor));
        return component;
    }

        public int getIntProperty(String s, boolean require) {
        if (s == null || s.isEmpty()) {
            if (require)
                throw new IllegalArgumentException("null");
            else
                return 0;
        }

        return Integer.parseInt(s);
    }

    public Color getColorProperty(String s, boolean require) {
        if (s == null || s.isEmpty()) {
            if (require)
                throw new IllegalArgumentException("null");
            else
                return null;
        }

        return stringToColor(s);
    }
}
