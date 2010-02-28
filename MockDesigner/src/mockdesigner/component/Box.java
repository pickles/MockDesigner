/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mockdesigner.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;
import org.jdom.Element;

/**
 *
 * @author Manabu Shibata
 */
public class Box extends Component {

    public Font font = new Font("ＭＳ ゴシック", Font.PLAIN, 12);

    public String text = "";
    public Color textColor = Color.black;
    public String textAlign = "Left";

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = super.getProperties();
        properties.put("Text", text);
        properties.put("Text align", textAlign);
        properties.put("Text color", colorToString(textColor));
        return properties;
    }

    @Override
    public void paint(Graphics g) {
        Font orgFont = g.getFont();
        Color orgColor = g.getColor();

        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(x, y, width, height);
        }
        
        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawRect(x, y, width, height);
        }

        if (textColor != null && text != null) {
            g.setFont(font);
            g.setColor(textColor);
            Point p = calcTextOffset(g);
            g.drawString(text, p.x, p.y);
        }

        g.setFont(orgFont);
        g.setColor(orgColor);
    }

    public Point calcTextOffset(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int strWidth = fm.stringWidth(text);
        Point p = new Point(x, y);
        p.y += fm.getAscent();
        
        if (textAlign.equalsIgnoreCase("Left")) {
            if (borderColor != null) {
                p.x += 3; //罫線1pxとその間に2pxの余白
                p.y += 2; //罫線1pxとその間に1pxの余白
            }
        } else if (textAlign.equalsIgnoreCase("Center")) {
            p.x = (x + x + width) / 2 - strWidth / 2;
            if (borderColor != null) {
                p.y += 2;
            }
        } else if (textAlign.equalsIgnoreCase("Right")) {
            p.x = x + width - strWidth;
            if (borderColor != null) {
                p.x -= 2; //罫線1pxとその間に1pxの余白
                p.y += 2; //罫線1pxとその間に1pxの余白
            }
        }
        return p;
    }
    
    public boolean isOn(Point p) {
        if (x <= p.x && p.x <= x + width && y <= p.y && p.y <= y + height)
            return true;

        return false;
    }

    @Override
    public void startMove(Point clickPoint) {
       super.startMove(clickPoint);
       if (distance(clickPoint, new Point(x + width, y + height)) < RADIUS) {
           moveType = MoveType.RESIZE;
       } else {
           moveType = MoveType.ALL;
       }
    }

    @Override
    public void move(Point dist) {
        if (moveType == MoveType.RESIZE) {
            width += dist.x;
            height += dist.y;
        } else {
            x += dist.x;
            y += dist.y;
        }
    }

    @Override
    public void updateProperty(String name, String value) {
        super.updateProperty(name, value);
        if ("Text".equals(name)) {
            text = value;
        } else if ("Text align".equals(name)) {
            if (value.equalsIgnoreCase("Left") ||
                    value.equalsIgnoreCase("Center") ||
                    value.equalsIgnoreCase("Right")) {
                textAlign = value;
            } else {
                throw new IllegalArgumentException("Invalid text align [" + value +"]");
            }
        } else if ("Text color".equals(name)) {
            textColor = stringToColor(value);
        }
    }

    @Override
    public Component copy() {
        Box copy = new Box();
        migrate(copy);
        copy.text = text;
        copy.textAlign = textAlign;
        if (textColor != null)
            copy.textColor = new Color(textColor.getRGB());
        
        return copy;
    }

    public Element toXML() {
        Element element = toXML("box");
        element.setAttribute("text", text);
        element.setAttribute("textColor", colorToString(textColor));
        element.setAttribute("textAlign", textAlign);
        return element;
    }

    public void build(Element elem) {
        x = getIntProperty(elem.getAttributeValue("x"), true);
        y = getIntProperty(elem.getAttributeValue("y"), true);
        z = getIntProperty(elem.getAttributeValue("z"), true);
        width = getIntProperty(elem.getAttributeValue("width"), true);
        height = getIntProperty(elem.getAttributeValue("height"), true);
        borderColor = getColorProperty(elem.getAttributeValue("borderColor"), false);
        backgroundColor = getColorProperty(elem.getAttributeValue("backgroundColor"), false);
        text =  elem.getAttributeValue("text");
        textAlign = elem.getAttributeValue("textAlign");
        textColor = getColorProperty(elem.getAttributeValue("textColor"), false);
    }
}
