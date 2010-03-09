/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mockdesigner.component;

import java.awt.Color;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pickles
 */
public class ComponentTest {

    public ComponentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetProperties() {
        Line l = new Line();
        l.x = 10;
        l.y = 11;
        l.width = 20;
        l.height = 21;

        Map<String, Object> properties = l.getProperties();
        if (properties.size() == 0) {
            fail("no properies.");
        }

        for (Map.Entry<String, Object> p : properties.entrySet()) {
            System.out.println(p.getKey() + ":" + p.getValue());
            if (p.getKey().equals("x")) {
                //assertEquals(new Integer(10), p.getValue());
            }
        }
    }

    @Test
    public void testUpdateProperty() {
        Box box = new Box();
        box.x = 10;
        box.updateProperty("x", "100");
        box.updateProperty("text", "Hello");
        box.updateProperty("Border color", "00ff00");
        Color expected = new Color(0x00, 0xff, 0x00);
        
        assertEquals(100, box.x);
        assertEquals("Hello", box.text);
        assertEquals(expected.getRGB(), box.borderColor.getRGB());

        box.updateProperty("text", "");
        assertEquals("", box.text);

        box.updateProperty("Border color", "");
        assertEquals(null, box.borderColor);
    }

}