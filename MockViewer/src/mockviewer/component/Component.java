package mockviewer.component;

import com.docomostar.ui.Graphics;

public abstract class Component {

	public int x;
	public int y;
	public int z;
	
	public int width;
	public int height;
	
	public Color borderColor;
	public Color backgroundColor;
	
	public abstract void paint(Graphics g);
}
