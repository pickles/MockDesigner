package mockdesigner.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import mockdesigner.anotation.Property;

/**
 *
 * @author Manabu Shibata
 */
public class Box extends Component {

    public Font font = new Font("ＭＳ ゴシック", Font.PLAIN, 12);

    @Property("Text")
    public String text = "";

    @Property("FontSize")
    public int fontSize = 12;

    @Property("FontType")
    public String fontType = "plain";

    @Property("TextColor")
    public Color textColor = Color.black;

    @Property("vAlign")
    public String vAlign = "Top";

    @Property("hAlign")
    public String hAlign = "Left";

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

    protected Point calcTextOffset(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int strWidth = fm.stringWidth(text);
        Point p = new Point(x, y);
        
        if (hAlign.equalsIgnoreCase("Center")) {
            p.x = (x + x + width - strWidth) / 2;
        } else if (hAlign.equalsIgnoreCase("Right")) {
            p.x = x + width - strWidth;
            if (borderColor != null) {
                p.x -= 2; //罫線1pxとその間に1pxの余白
            }
        } else { // Left
            if (borderColor != null) {
                p.x += 3; //罫線1pxとその間に2pxの余白
            }
        }

        if (vAlign.equalsIgnoreCase("Center")) {
            p.y = (height - fm.getHeight()) / 2 + y + fm.getAscent();
        } else if (vAlign.equalsIgnoreCase("Bottom")) {
            p.y = (y + height - fm.getLeading());
            if (borderColor != null) {
                p.y -= 1;
            }
        } else {
            p.y += fm.getAscent();
            if (borderColor != null) {
                p.y += 2; //罫線1pxとその間に1pxの余白
            }
        }
        
        return p;
    }

    @Override
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
        updateFont();
    }

    @Override
    public void afterBuild() {
        updateFont();
    }

    protected void updateFont() {
        int type = Font.PLAIN;
        if (fontType.equalsIgnoreCase("bold")) {
            type = Font.BOLD;
        } else if (fontType.equalsIgnoreCase("italic")) {
            type = Font.ITALIC;
        }
        
        font = new Font("ＭＳ ゴシック", type, fontSize);
    }

    @Override
    public String getComponentName() {
        return "box";
    }
}
