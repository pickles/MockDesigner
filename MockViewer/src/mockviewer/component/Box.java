package mockviewer.component;

import com.docomostar.ui.Font;
import com.docomostar.ui.Graphics;

public class Box extends Component{

	public String text;
	public String textAlign;
	public Color textColor;
	
	static final Font font = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN, 12); 
	
	public void paint(Graphics g) {
		g.setFont(font);
		if (backgroundColor != null) {
			g.setColor(backgroundColor.getValue());
			g.fillRect(x, y, width, height);
		}
		
		if (borderColor != null) {
			g.setColor(borderColor.getValue());
			g.drawRect(x, y, width, height);
		}
		
		if (textColor != null && text != null) {
			g.setColor(textColor.getValue());
			Point p = calcTextOffset(g);
			g.drawString(text, p.x, p.y);
		}
	}
	
	public Point calcTextOffset(Graphics g) {
		int strWidth = font.stringWidth(text);
		Point p = new Point(x, y);
		p.y += font.getAscent();
		
		if (textAlign.equalsIgnoreCase("LEFT")) {
			if (borderColor != null) {
				 p.x += 3; //�r��1px�Ƃ��̊Ԃ�2px�̗]��
	             p.y += 2; //�r��1px�Ƃ��̊Ԃ�1px�̗]��
			}
		} else if (textAlign.equalsIgnoreCase("CENTER")) {
			p.x = (x + x + width) / 2 - strWidth / 2;
			if (borderColor != null) {
                p.y += 2;
            }
        } else if (textAlign.equalsIgnoreCase("Right")) {
            p.x = x + width - strWidth;
            if (borderColor != null) {
                p.x -= 2; //�r��1px�Ƃ��̊Ԃ�1px�̗]��
                p.y += 2; //�r��1px�Ƃ��̊Ԃ�1px�̗]��
            }
        }
        return p;

	}

}
