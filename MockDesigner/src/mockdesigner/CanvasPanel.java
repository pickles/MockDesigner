package mockdesigner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import mockdesigner.component.Component;

/**
 * @author Manabu Shibata
 */
public class CanvasPanel extends JPanel implements MouseListener {

    private mockdesigner.tool.Tool selectedTool;

    private Point lastClickedPoint;

    ComponentManager componentManager = ComponentManager.getInstance();
    
    public CanvasPanel() {
        setFocusable(true);
        // クリック時にフォーカスを要求しないとキーイベントが発生しない。
        addMouseListener(this);
    }
    
    @Override
    public void paint(Graphics g) {
        Color orgColor = g.getColor();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(orgColor);

        for (Component c : componentManager.getComponents()) {
            c.paint(g);
        }

        drawCandidate(g);
    }

    private void drawCandidate(Graphics g) {
        selectedTool.drawCandidate(g);
    }

    public void setTool(mockdesigner.tool.Tool tool) {
        removeMouseListener(selectedTool);
        removeMouseMotionListener(selectedTool);
        removeKeyListener(selectedTool);
        
        addMouseListener(tool);
        addMouseMotionListener(tool);
        addKeyListener(tool);
        selectedTool = tool;
    }

    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
        lastClickedPoint = new Point(e.getPoint());
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        requestFocusInWindow();
        lastClickedPoint = new Point(e.getPoint());
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public Point getLastClickedPoint() {
        return lastClickedPoint;
    }

}
