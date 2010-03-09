package mockviewer.component;

import com.docomostar.ui.Font;
import com.docomostar.ui.Graphics;

public class Box extends Component{

    public String text;

    public int fontSize = 12;
    public String fontType = "PLAIN";
    
    public String vAlign = "LEFT";
    public String hAlign = "TOP";

    public Color textColor;
	
    public Font font = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN, 12); 
    
    private void updateFont() {
	if ("BOLD".equalsIgnoreCase(fontType))
	    font = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_BOLD, fontSize);
	else if ("ITALIC".equalsIgnoreCase(fontType))
	    font = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_ITALIC, fontSize);
	else
	    font = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN, fontSize);
    }

    public void paint(Graphics g) {
	if (backgroundColor != null) {
	    g.setColor(backgroundColor.getValue());
	    g.fillRect(x, y, width, height);
	}
		
	if (borderColor != null) {
	    g.setColor(borderColor.getValue());
	    g.drawRect(x, y, width, height);
	}
		
	if (textColor != null && text != null) {
	    updateFont();
	    g.setFont(font);
	    g.setColor(textColor.getValue());
	    Point p = calcTextOffset(g);
	    g.drawString(text, p.x, p.y);
	}
    }
	
    public Point calcTextOffset(Graphics g) {
	int strWidth = font.stringWidth(text);
	Point p = new Point(x, y);
	
	if (hAlign.equalsIgnoreCase("Center")) {
	    p.x = (x + x + width - strWidth) / 2;
	} else if (hAlign.equalsIgnoreCase("Right")) {
	    p.x = x + width - strWidth;
	    if (borderColor != null) p.x -= 2;
	} else {
	    if (borderColor != null) p.x += 3;
	}

	if (vAlign.equalsIgnoreCase("Center")) {
	    p.y = (height - font.getHeight()) / 2 + y + font.getAscent();
	} else if (vAlign.equalsIgnoreCase("Bottom")) {
	    p.y = (y + height - font.getDescent());
	    if (borderColor != null) p.y -=1;
	} else {
	    p.y += font.getAscent();
	    if (borderColor != null) p.y += 2;
	}
	return p;
    }

}
