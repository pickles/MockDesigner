/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mockdesigner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import mockdesigner.component.Box;
import mockdesigner.component.Component;
import mockdesigner.component.Line;

/**
 *
 * @author Manabu Shibata
 */
public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener, TableModelListener, KeyListener {

    public Tool selectedTool = Tool.SELECT;

    MockDesignerView view;
    ComponentManager componentManager = new ComponentManager();

    boolean isDragging = false;
    boolean canceled = false;
    Point dragStartPoint;
    private int maxZ = 0;

    Tool candidate;
    Point currentPoint;
    Component selectedComponent;

    public CanvasPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        addKeyListener(this);
    }
    
    @Override
    public void paint(Graphics g) {
        System.out.println("Canvas paint isDragging="+isDragging);
        Color orgColor = g.getColor();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(orgColor);

        System.out.println(componentManager.getComponents().size());
        for (Component c : componentManager.getComponents()) {
            c.paint(g);
        }

        if (isDragging) {
            drawCandidate(g);
        }
    }

    private void drawCandidate(Graphics g) {
        if (candidate == Tool.LINE) {
            g.drawLine(dragStartPoint.x, dragStartPoint.y, currentPoint.x, currentPoint.y);
        } else if (candidate == Tool.BORDER || candidate == Tool.BOX || candidate == Tool.FILL) {
            g.drawRect(dragStartPoint.x, dragStartPoint.y, currentPoint.x - dragStartPoint.x, currentPoint.y - dragStartPoint.y);
        } else if (candidate == Tool.SELECT) {
            g.drawOval(currentPoint.x - 4, currentPoint.y - 4 , 8, 8);
        }
    }

    public void setView(MockDesignerView view) {
        this.view = view;
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isDragging = true;
            canceled = false;
            dragStartPoint = e.getPoint();
            currentPoint = e.getPoint();
            if (selectedTool == Tool.SELECT) {
                if (selectedComponent != null) {
                    selectedComponent.isSelect = false;
                }
                selectedComponent = componentManager.getComponent(dragStartPoint);
                if (selectedComponent != null) {
                    selectedComponent.isSelect = true;
                    selectedComponent.startMove(currentPoint);
                    view.updatePropertiesView(selectedComponent.getProperties());
                } else {
                    view.updatePropertiesView(null);
                }

            }
            candidate = selectedTool;
            System.out.println("Mouse pressed at " + dragStartPoint.x + ":" + dragStartPoint.y);
        } else if (e.getButton() == MouseEvent.BUTTON3) {

        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        requestFocusInWindow();
        if (e.getButton() == MouseEvent.BUTTON3) {
            cancelAction();
            return;
        }
        
        currentPoint = e.getPoint();
        System.out.println("Mouse released at " + currentPoint.x + ":" + currentPoint.y);

        isDragging = false;
        candidate = null;
        if (!canceled && selectedTool == Tool.LINE) {
            Line line = createLine();
            selectedComponent = line;
            componentManager.addComponent(line);
            view.updatePropertiesView(selectedComponent.getProperties());
        } else if (!canceled && (selectedTool == Tool.BORDER || selectedTool == Tool.BOX || selectedTool == Tool.FILL)) {
            Box box = createBox();
            selectedComponent = box;
            componentManager.addComponent(box);
            view.updatePropertiesView(selectedComponent.getProperties());
        } else if (!canceled && selectedTool == Tool.SELECT) {
            if (selectedComponent != null && selectedComponent.isMoving()) {
                selectedComponent.endMove();
            }
        }
        repaint();
    }

    protected void cancelAction() {
        canceled = true;
        dragStartPoint = null;
        candidate = null;
        isDragging = false;
        if (selectedComponent != null && selectedComponent.isMoving()) {
            selectedComponent.cancelMove();
        }
    }

    protected Line createLine() {
        Line line = new Line();
        line.x = dragStartPoint.x;
        line.y = dragStartPoint.y;
        line.width = currentPoint.x;
        line.height = currentPoint.y;
        line.z = maxZ++;
        line.borderColor = view.color1;
        return line;
    }

    protected Box createBox() {
        Box box = new Box();
        if (currentPoint.x < dragStartPoint.x) {
            box.x = currentPoint.x;
            box.width = dragStartPoint.x - currentPoint.x;
        } else {
            box.x = dragStartPoint.x;
            box.width = currentPoint.x - dragStartPoint.x;
        }

        if (currentPoint.y < dragStartPoint.y) {
            box.y = currentPoint.y;
            box.height = dragStartPoint.y - currentPoint.y;
        } else {
            box.y = dragStartPoint.y;
            box.height = currentPoint.y - dragStartPoint.y;
        }

        box.z = maxZ++;
        
        if (selectedTool == Tool.BORDER) {
            box.borderColor = view.color1;
            box.backgroundColor = null;
        } else if (selectedTool == Tool.BOX) {
            box.borderColor = view.color1;
            box.backgroundColor = view.color2;
        } else if (selectedTool == Tool.FILL) {
            box.borderColor = null;
            box.backgroundColor = view.color2;
        }

        return box;
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        Point prevPoint = new Point(currentPoint);
        currentPoint = e.getPoint();
        if (selectedComponent != null && selectedComponent.isMoving()) {
            Point dist = new Point(currentPoint.x - prevPoint.x, currentPoint.y - prevPoint.y);
            selectedComponent.move(dist);
            view.updatePropertiesView(selectedComponent.getProperties());
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e) {}

    public void tableChanged(TableModelEvent e) {
        TableModel m = (TableModel) e.getSource();
        String name = (String) m.getValueAt(e.getFirstRow(), 0);
        String value = (String) m.getValueAt(e.getFirstRow(), 1);
        try {
            selectedComponent.updateProperty(name, value);
            componentManager.updateOrder();
        } catch (Exception ex) {
            ex.printStackTrace();
            view.updatePropertiesView(selectedComponent.getProperties());
        }
        repaint();
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            if (selectedComponent != null && selectedComponent.y > 0) {
                selectedComponent.move(new Point(0, -1));
                view.updatePropertiesView(selectedComponent.getProperties());
                repaint();
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (selectedComponent != null && selectedComponent.y < 240) {
                selectedComponent.move(new Point(0, 1));
                view.updatePropertiesView(selectedComponent.getProperties());
                repaint();
            }
        } else if (code == KeyEvent.VK_LEFT) {
            if (selectedComponent != null && selectedComponent.x > 0) {
                selectedComponent.move(new Point(-1, 0));
                view.updatePropertiesView(selectedComponent.getProperties());
                repaint();
            }
        } else if (code == KeyEvent.VK_RIGHT) {
            if (selectedComponent != null && selectedComponent.x < 240) {
                selectedComponent.move(new Point(1, 0));
                view.updatePropertiesView(selectedComponent.getProperties());
                repaint();
            }
        }
    }

    Component copiedComponent = null;
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        int mod = e.getModifiers();
        System.out.println("KeyRelease "+code+":"+mod);
        
        if (code == KeyEvent.VK_DELETE) {
            if (selectedComponent != null) {
                cancelAction();
                componentManager.removeComponent(selectedComponent);
                selectedComponent = null;
                view.updatePropertiesView(null);
                repaint();
            }
        } else if (code == KeyEvent.VK_C && mod == KeyEvent.CTRL_MASK) {
            System.out.println("Copy");
            if (selectedComponent != null) {
                copiedComponent = selectedComponent.copy();
            }
        } else if (code == KeyEvent.VK_V && mod == KeyEvent.CTRL_MASK) {
            System.out.println("Past");
            if (copiedComponent != null) {
                Component copy = copiedComponent.copy();
                copy.x = currentPoint.x;
                copy.y = currentPoint.y;
                componentManager.addComponent(copy);
                selectedComponent = copy;
                view.updatePropertiesView(copy.getProperties());
                repaint();
            }
        }
    }
}
