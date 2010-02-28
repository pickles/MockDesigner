package mockviewer.component;

import com.docomostar.ui.Graphics;

public class Line extends Component {

	public void paint(Graphics g) {
		g.setColor(borderColor.getValue());
		g.drawLine(x, y, width, height);
	}
}
