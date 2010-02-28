package mockviewer.component;

import com.docomostar.ui.Graphics;

public class Color {

	public int red;
	public int green;
	public int blue;
	
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public Color(String colorCode) {
		this.red   = Integer.parseInt(colorCode.substring(0,2), 16);
		this.green = Integer.parseInt(colorCode.substring(2,4), 16);
		this.blue  = Integer.parseInt(colorCode.substring(4,6), 16);
	}
	
	public int getValue() {
		return Graphics.getColorOfRGB(red, green, blue);
	}
}
