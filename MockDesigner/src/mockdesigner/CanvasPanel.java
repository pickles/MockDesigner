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
import mockdesigner.component.Memento;
import mockdesigner.component.Picture;

/**
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

    MementoStack mementoStack = new MementoStack(10);

    public CanvasPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        addKeyListener(this);
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
                    mementoStack.push(selectedComponent.createMemento("update"));
                    view.updatePropertiesView(selectedComponent);
                } else {
                    view.updatePropertiesView(null);
                }

            }
            candidate = selectedTool;
            System.out.println("Mouse pressed at " + dragStartPoint.x + ":" + dragStartPoint.y);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            cancelAction();
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
        
        if (currentPoint.x == dragStartPoint.x && currentPoint.y == dragStartPoint.y) {
            if (canceled || selectedComponent != null)
                mementoStack.pop();
        }

        isDragging = false;
        candidate = null;
        if (!canceled && selectedTool == Tool.LINE) {
            Line line = createLine();
            mementoStack.push(line.createMemento("create"));
            componentManager.addComponent(line);
            updateState(line);
        } else if (!canceled && (selectedTool == Tool.BORDER || selectedTool == Tool.BOX || selectedTool == Tool.FILL)) {
            Box box = createBox();
            mementoStack.push(box.createMemento("create"));
            componentManager.addComponent(box);
            updateState(box);
        } else if (!canceled && selectedTool == Tool.IMAGE) {
            Picture pic = createPicture();
            mementoStack.push(pic.createMemento("create"));
            componentManager.addComponent(pic);
            updateState(pic);
        } else if (!canceled && selectedTool == Tool.SELECT) {
            if (selectedComponent != null && selectedComponent.isMoving()) {
                selectedComponent.endMove();
                view.isDirty(true);
            }
        }
        repaint();
    }

    private Picture createPicture() {
        Picture picture = new Picture();
        picture.x = currentPoint.x;
        picture.y = currentPoint.y;
        picture.z = maxZ++;
        return picture;
    }

    protected void cancelAction() {
        canceled = true;
        dragStartPoint = null;
        candidate = null;
        isDragging = false;
        if (selectedComponent != null && selectedComponent.isMoving()) {
            selectedComponent.cancelMove();
            mementoStack.pop();
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
        if (currentPoint == null) currentPoint = e.getPoint();
        Point prevPoint = new Point(currentPoint);
        currentPoint = e.getPoint();
        if (selectedComponent != null && selectedComponent.isMoving()) {
            Point dist = new Point(currentPoint.x - prevPoint.x, currentPoint.y - prevPoint.y);
            selectedComponent.move(dist);
            view.updatePropertiesView(selectedComponent);
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e) {}

    public void tableChanged(TableModelEvent e) {
        TableModel m = (TableModel) e.getSource();
        String name = (String) m.getValueAt(e.getFirstRow(), 0);
        String value = (String) m.getValueAt(e.getFirstRow(), 1);
        try {
            mementoStack.push(selectedComponent.createMemento("update"));
            selectedComponent.updateProperty(name, value);
            componentManager.updateOrder();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        view.updatePropertiesView(selectedComponent);
        repaint();
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (selectedComponent != null
                && code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN
                || code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_LEFT) {
            if (code == KeyEvent.VK_UP && selectedComponent.y > 0) {
                selectedComponent.move(new Point(0, -1));
            } else if (code == KeyEvent.VK_DOWN && selectedComponent.y < this.getHeight()) {
                selectedComponent.move(new Point(0, 1));
            } else if (code == KeyEvent.VK_LEFT && selectedComponent.x > 0) {
                selectedComponent.move(new Point(-1, 0));
            } else if (code == KeyEvent.VK_RIGHT && selectedComponent.x < this.getWidth()) {
                selectedComponent.move(new Point(1, 0));
            }
            updateState(selectedComponent);
        }
    }

    Component copiedComponent = null;
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        int mod = e.getModifiers();
        
        if (code == KeyEvent.VK_DELETE) {
            delete();
        } else if (code == KeyEvent.VK_C && mod == KeyEvent.CTRL_MASK) {
            copy();
        } else if (code == KeyEvent.VK_V && mod == KeyEvent.CTRL_MASK) {
            paste();
        } else if (code == KeyEvent.VK_Z && mod == KeyEvent.CTRL_MASK) {
            undo();
        }
    }

    public void delete() {
        if (selectedComponent != null) {
            cancelAction();
            mementoStack.push(selectedComponent.createMemento("delete"));
            componentManager.removeComponent(selectedComponent);
            selectedComponent = null;
            updateState(null);
        }
    }

    public void copy() {
        if (selectedComponent != null) {
            copiedComponent = selectedComponent.copy();
        }
    }

    public void paste() {
        if (copiedComponent != null) {
            Component copy = copiedComponent.copy();
            copy.x = currentPoint.x;
            copy.y = currentPoint.y;
            componentManager.addComponent(copy);
            updateState(copy);
        }
    }

    public void undo() {
        Memento memento = mementoStack.pop();
        if (memento == null) {
            return;
        }

        if (memento.getCommand().equalsIgnoreCase("delete")) {
            Component component = memento.getRestoredComponent();
            componentManager.addComponent(component);
        } else if (memento.getCommand().equalsIgnoreCase("update")) {
            Component component = memento.getComponent();
            component.restore(memento);
        } else if (memento.getCommand().equalsIgnoreCase("create")) {
            Component component = memento.getComponent();
            componentManager.removeComponent(component);
            updateState(null);
        }
        repaint();
    }

    private void updateState(Component c) {
        selectedComponent = c;
        view.updatePropertiesView(c);
        view.isDirty(true);
        repaint();
    }
}
