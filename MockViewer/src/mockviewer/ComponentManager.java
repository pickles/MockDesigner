package mockviewer;

import java.io.IOException;
import java.io.InputStream;

import mockviewer.component.Box;
import mockviewer.component.Color;
import mockviewer.component.Line;
import mockviewer.component.Picture;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ComponentManager {

    public Page currentPage;
	
    public void parseConfigurationFile(InputStream in) throws IOException {
	KXmlParser parser = new KXmlParser();
	try {
	    parser.setInput(in, "SJIS");
	    parser.nextTag();
	    parser.require(XmlPullParser.START_TAG, null, "page");
	    currentPage = new Page();
	    while (parser.nextTag() == XmlPullParser.START_TAG) {
		String tagName = parser.getName();
		System.out.println(tagName);
		if (tagName.equalsIgnoreCase("line"))
		    currentPage.add(createLine(parser));
		else if (tagName.equalsIgnoreCase("box"))
		    currentPage.add(createBox(parser));
		else if (tagName.equalsIgnoreCase("image"))
		    currentPage.add(createPicture(parser));
		// âΩåÃÇ©Ç±Ç±Ç≈Ç‡nextTagÇµÇ»Ç¢Ç∆Ç§Ç‹Ç≠ìÆÇ©Ç»Ç¢ÅB
		parser.nextTag();
	    }
	} catch (XmlPullParserException e) {
	    e.printStackTrace();
	}
    }

    public Line createLine(XmlPullParser parser) {
	Line line = new Line();
	for (int i = 0; i < parser.getAttributeCount(); i++) {
	    String name = parser.getAttributeName(i);
	    String value = parser.getAttributeValue(i);
	    System.out.println(name + ":" + value);
	    if ("x".equalsIgnoreCase(name)) {
		line.x = Integer.parseInt(value);
	    } else if ("y".equalsIgnoreCase(name)) {
		line.y = Integer.parseInt(value);
	    } else if ("z".equalsIgnoreCase(name)) {
		line.z = Integer.parseInt(value);
	    } else if ("Width".equalsIgnoreCase(name)) {
		line.width = Integer.parseInt(value);
	    } else if ("Height".equalsIgnoreCase(name)) {
		line.height = Integer.parseInt(value);
	    } else if ("BorderColor".equalsIgnoreCase(name)) {
		if (value.length() != 6)
		    line.borderColor = null;
		else
		    line.borderColor = new Color(value);
	    } else if ("BackgroundColor".equalsIgnoreCase(name)) {
		if (value.length() != 6)
		    line.backgroundColor = null;
		else
		    line.backgroundColor = new Color(value);
	    }
	}
	return line;
    }

    public Box createBox(XmlPullParser parser) {
	Box box = new Box();
	for (int i = 0; i < parser.getAttributeCount(); i++) {
	    String name = parser.getAttributeName(i);
	    String value = parser.getAttributeValue(i);
	    System.out.println(name + ":" + value);
	    if ("x".equalsIgnoreCase(name)) {
		box.x = Integer.parseInt(value);
	    } else if ("y".equalsIgnoreCase(name)) {
		box.y = Integer.parseInt(value);
	    } else if ("z".equalsIgnoreCase(name)) {
		box.z = Integer.parseInt(value);
	    } else if ("Width".equalsIgnoreCase(name)) {
		box.width = Integer.parseInt(value);
	    } else if ("Height".equalsIgnoreCase(name)) {
		box.height = Integer.parseInt(value);
	    } else if ("BorderColor".equalsIgnoreCase(name)) {
		if (value.length() != 6)
		    box.borderColor = null;
		else
		    box.borderColor = new Color(value);
	    } else if ("BackgroundColor".equalsIgnoreCase(name)) {
		if (value.length() != 6)
		    box.backgroundColor = null;
		else
		    box.backgroundColor = new Color(value);
	    } else if ("Text".equalsIgnoreCase(name)) {
		box.text = value;
	    } else if ("TextColor".equalsIgnoreCase(name)) {
		box.textColor = new Color(value);
	    } else if ("FontType".equalsIgnoreCase(name)) {
		box.fontType = value;
	    } else if ("FontSize".equalsIgnoreCase(name)) {
		box.fontSize = Integer.parseInt(value);
	    } else if ("hAlign".equalsIgnoreCase(name)) {
		box.hAlign = value;
	    } else if ("vAlign".equalsIgnoreCase(name)) {
		box.vAlign = value;
	    } else if ("textColor".equalsIgnoreCase(name)) {
		if (value.length() != 6)
		    box.textColor = null;
		else
		    box.textColor = new Color(value);
	    }
	}
	return box;
    }

    public Picture createPicture(XmlPullParser parser) {
	Picture picture = new Picture();
	for (int i = 0; i < parser.getAttributeCount(); i++) {
	    String name = parser.getAttributeName(i);
	    String value = parser.getAttributeValue(i);
	    System.out.println(name + ":" + value);
	    if ("x".equalsIgnoreCase(name)) {
		picture.x = Integer.parseInt(value);
	    } else if ("y".equalsIgnoreCase(name)) {
		picture.y = Integer.parseInt(value);
	    } else if ("z".equalsIgnoreCase(name)) {
		picture.z = Integer.parseInt(value);
	    } else if ("Width".equalsIgnoreCase(name)) {
		picture.width = Integer.parseInt(value);
	    } else if ("Height".equalsIgnoreCase(name)) {
		picture.height = Integer.parseInt(value);
	    } else if ("ImagePath".equalsIgnoreCase(name)) {
		picture.imagePath = value;
	    }
	}
	picture.init();
	return picture;
    }

}
