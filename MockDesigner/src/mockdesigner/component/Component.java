package mockdesigner.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mockdesigner.anotation.Property;
import org.jdom.Element;

/**
 * @author Manabu Shibata
 */
public abstract class Component {

    @Property("x")
    public int x;

    @Property("y")
    public int y;

    @Property("z")
    public int z;

    @Property("Width")
    public int width;

    @Property("Height")
    public int height;

    @Property("BackgroundColor")
    public Color backgroundColor = Color.WHITE;

    @Property("BorderColor")
    public Color borderColor = Color.BLACK;

    private Memento mementoForCancel;
    
    public boolean isMoving = false;

    public boolean isSelect = false;

    protected static final int RADIUS = 4;

    protected List<Field> getProperyFields() {
        List<Field> fields = new ArrayList<Field>();
        for (Class clazz = this.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field candidate : clazz.getDeclaredFields()) {
                if (candidate.getAnnotation(Property.class) != null) {
                    fields.add(candidate);
                }
            }
        }
        return fields;
    }
    
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new TreeMap<String, Object>();

        for (Field field : getProperyFields()) {
            Property prop = field.getAnnotation(Property.class);
            try {
                Object value = field.get(this);
                if (value != null) {
                    if (value instanceof Color) {
                        properties.put(prop.value(), colorToString((Color) value));
                    } else {
                        properties.put(prop.value(), value.toString());
                    }
                } else {
                    properties.put(prop.value(), "");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

    public Memento createMemento(String command) {
        Memento memento = new Memento(this, command);
        for (Map.Entry<String, Object> entry : getProperties().entrySet()) {
            memento.addProperty(entry.getKey(), entry.getValue());
        }
        return memento;
    }

    public void restore(Memento memento) {
        for (Map.Entry<String, Object> entry : memento.getProperties().entrySet()) {
            this.updateProperty(entry.getKey(), (String) entry.getValue());
        }
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
        mementoForCancel = createMemento("update");
        isMoving = true;
    }
    
    public void cancelMove() {
        restore(mementoForCancel);
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
        for (Field field : getProperyFields()) {
            Property prop = field.getAnnotation(Property.class);
            if (prop.value().equalsIgnoreCase(name)) {
                try {
                    if (field.getType() == Integer.TYPE) {
                        field.setInt(this, Integer.parseInt((String) value));
                    } else if (field.getType() == Color.class) {
                        field.set(this, stringToColor((String) value));
                    } else {
                        field.set(this, value);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
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

    public Component copy() {
        try {
            Component copy = (Component) getClass().newInstance();
            copy.restore(createMemento("copy"));
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Element toXML() {
        Element component = new Element(getComponentName());
        for (Field field : getProperyFields()) {
            Property prop = field.getAnnotation(Property.class);
            try {
                if (field.getType() == Color.class) {
                    component.setAttribute(prop.value(), colorToString((Color) field.get(this)));
                } else {
                    Object value = field.get(this);
                    if (value == null)
                        component.setAttribute(prop.value(), "");
                    else
                        component.setAttribute(prop.value(), field.get(this).toString());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return component;
    }

    public void build(Element element) {
        for (Field field : getProperyFields()) {
            Property prop = field.getAnnotation(Property.class);
            updateProperty(prop.value(), element.getAttributeValue(prop.value()));
        }
        afterBuild();
    }

    protected void afterBuild() {}

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

    public abstract String getComponentName();
    
}
