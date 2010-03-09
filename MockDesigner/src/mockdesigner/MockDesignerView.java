/*
 * MockDesignerView.java
 */

package mockdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import mockdesigner.component.Box;
import mockdesigner.component.Component;
import mockdesigner.component.Line;
import mockdesigner.component.Picture;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * The application's main frame.
 */
public class MockDesignerView extends FrameView  {

    public Color color1 = Color.black;
    public Color color2 = Color.white;

    private CanvasPanel canvasPanel;

    private DefaultTableModel model;

    private File file;
    public static String APP_NAME = "MockDesigner";
    private boolean isDirty = false;

    public MockDesignerView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        // 独自設定
        color1Box.setBackground(color1);
        color2Box.setBackground(color2);
        canvasPanel = (CanvasPanel) canvas;
        canvasPanel.setView(this);

        model = (DefaultTableModel) propertyTable.getModel();
        model.addTableModelListener(canvasPanel);

        openFileChooser.setFileFilter(new FileNameExtensionFilter("XMLファイル", "xml"));
        saveFileChooser.setFileFilter(new FileNameExtensionFilter("XMLファイル", "xml"));

    }

    private void updateCoordinate(Point p) {
        if (!isOutOfBounds(p))
            coordinateLabel.setText(p.x + ":" + p.y);
    }

    private boolean isOutOfBounds(Point p) {
        return p.x < 0 || canvas.getWidth() < p.x || p.y < 0 || canvas.getHeight() < p.y;
    }

    public void updatePropertiesView(Component component) {
        Map<String, Object> properties = null;
        if (component != null)
            properties = component.getProperties();
        
        model.removeTableModelListener(canvasPanel);
        while (model.getRowCount() != 0) {
            model.removeRow(0);
        }
        if (properties == null) return;

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            Object[] data = new Object[2];
            data[0] = entry.getKey();
            data[1] = entry.getValue();
            model.addRow(data);
        }
        model.addTableModelListener(canvasPanel);
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MockDesignerApp.getApplication().getMainFrame();
            aboutBox = new MockDesignerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MockDesignerApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        canvas = new CanvasPanel();
        colorPanel = new javax.swing.JPanel();
        color1Box = new javax.swing.JPanel();
        color2Box = new javax.swing.JPanel();
        toolPanel = new javax.swing.JPanel();
        selectButton = new javax.swing.JToggleButton();
        lineButton = new javax.swing.JToggleButton();
        borderButton = new javax.swing.JToggleButton();
        boxButton = new javax.swing.JToggleButton();
        fillButton = new javax.swing.JToggleButton();
        imageButton = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        propertyTable = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        coordinateLabel = new javax.swing.JLabel();
        toolGroup = new javax.swing.ButtonGroup();
        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        colorChooser = new javax.swing.JColorChooser();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mockdesigner.MockDesignerApp.class).getContext().getResourceMap(MockDesignerView.class);
        canvas.setBackground(resourceMap.getColor("canvas.background")); // NOI18N
        canvas.setMinimumSize(new java.awt.Dimension(240, 240));
        canvas.setName("canvas"); // NOI18N
        canvas.setPreferredSize(new java.awt.Dimension(240, 240));
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                canvasMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                canvasMouseReleased(evt);
            }
        });
        canvas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                canvasMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                canvasMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        colorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        colorPanel.setName("colorPanel"); // NOI18N

        color1Box.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        color1Box.setName("color1Box"); // NOI18N
        color1Box.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                color1BoxMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout color1BoxLayout = new javax.swing.GroupLayout(color1Box);
        color1Box.setLayout(color1BoxLayout);
        color1BoxLayout.setHorizontalGroup(
            color1BoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );
        color1BoxLayout.setVerticalGroup(
            color1BoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        color2Box.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        color2Box.setName("color2Box"); // NOI18N
        color2Box.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                color2BoxMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout color2BoxLayout = new javax.swing.GroupLayout(color2Box);
        color2Box.setLayout(color2BoxLayout);
        color2BoxLayout.setHorizontalGroup(
            color2BoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );
        color2BoxLayout.setVerticalGroup(
            color2BoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPanelLayout = new javax.swing.GroupLayout(colorPanel);
        colorPanel.setLayout(colorPanelLayout);
        colorPanelLayout.setHorizontalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(color1Box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(color2Box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        colorPanelLayout.setVerticalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, colorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(color2Box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(color1Box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        toolPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        toolPanel.setName("toolPanel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(mockdesigner.MockDesignerApp.class).getContext().getActionMap(MockDesignerView.class, this);
        selectButton.setAction(actionMap.get("selectSelect")); // NOI18N
        toolGroup.add(selectButton);
        selectButton.setIcon(resourceMap.getIcon("selectButton.icon")); // NOI18N
        selectButton.setText(resourceMap.getString("selectButton.text")); // NOI18N
        selectButton.setMaximumSize(new java.awt.Dimension(25, 25));
        selectButton.setMinimumSize(new java.awt.Dimension(25, 25));
        selectButton.setName("selectButton"); // NOI18N
        selectButton.setPreferredSize(new java.awt.Dimension(25, 25));

        lineButton.setAction(actionMap.get("selectLine")); // NOI18N
        toolGroup.add(lineButton);
        lineButton.setIcon(resourceMap.getIcon("lineButton.icon")); // NOI18N
        lineButton.setText(resourceMap.getString("lineButton.text")); // NOI18N
        lineButton.setMaximumSize(new java.awt.Dimension(25, 25));
        lineButton.setMinimumSize(new java.awt.Dimension(25, 25));
        lineButton.setName("lineButton"); // NOI18N
        lineButton.setPreferredSize(new java.awt.Dimension(25, 25));

        borderButton.setAction(actionMap.get("selectBorder")); // NOI18N
        toolGroup.add(borderButton);
        borderButton.setIcon(resourceMap.getIcon("borderButton.icon")); // NOI18N
        borderButton.setText(resourceMap.getString("borderButton.text")); // NOI18N
        borderButton.setMaximumSize(new java.awt.Dimension(25, 25));
        borderButton.setMinimumSize(new java.awt.Dimension(25, 25));
        borderButton.setName("borderButton"); // NOI18N
        borderButton.setPreferredSize(new java.awt.Dimension(25, 25));

        boxButton.setAction(actionMap.get("selectBox")); // NOI18N
        toolGroup.add(boxButton);
        boxButton.setIcon(resourceMap.getIcon("boxButton.icon")); // NOI18N
        boxButton.setText(resourceMap.getString("boxButton.text")); // NOI18N
        boxButton.setMaximumSize(new java.awt.Dimension(25, 25));
        boxButton.setMinimumSize(new java.awt.Dimension(25, 25));
        boxButton.setName("boxButton"); // NOI18N
        boxButton.setPreferredSize(new java.awt.Dimension(25, 25));

        fillButton.setAction(actionMap.get("selectFill")); // NOI18N
        toolGroup.add(fillButton);
        fillButton.setIcon(resourceMap.getIcon("fillButton.icon")); // NOI18N
        fillButton.setText(resourceMap.getString("fillButton.text")); // NOI18N
        fillButton.setMaximumSize(new java.awt.Dimension(25, 25));
        fillButton.setMinimumSize(new java.awt.Dimension(25, 25));
        fillButton.setName("fillButton"); // NOI18N
        fillButton.setPreferredSize(new java.awt.Dimension(25, 25));

        imageButton.setAction(actionMap.get("selectImage")); // NOI18N
        toolGroup.add(imageButton);
        imageButton.setIcon(resourceMap.getIcon("imageButton.icon")); // NOI18N
        imageButton.setText(resourceMap.getString("imageButton.text")); // NOI18N
        imageButton.setMaximumSize(new java.awt.Dimension(25, 25));
        imageButton.setMinimumSize(new java.awt.Dimension(25, 25));
        imageButton.setName("imageButton"); // NOI18N
        imageButton.setPreferredSize(new java.awt.Dimension(25, 25));

        javax.swing.GroupLayout toolPanelLayout = new javax.swing.GroupLayout(toolPanel);
        toolPanel.setLayout(toolPanelLayout);
        toolPanelLayout.setHorizontalGroup(
            toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fillButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borderButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toolPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(boxButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lineButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, toolPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        toolPanelLayout.setVerticalGroup(
            toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lineButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(borderButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fillButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        propertyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "プロパティ", "値"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        propertyTable.setName("propertyTable"); // NOI18N
        propertyTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(propertyTable);
        propertyTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("propertyTable.columnModel.title0")); // NOI18N
        propertyTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("propertyTable.columnModel.title1")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(colorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(canvas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(toolPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(canvas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenuItem.setAction(actionMap.get("newAction")); // NOI18N
        newMenuItem.setText(resourceMap.getString("newMenuItem.text")); // NOI18N
        newMenuItem.setName("newMenuItem"); // NOI18N
        fileMenu.add(newMenuItem);

        openMenuItem.setAction(actionMap.get("openFileAction")); // NOI18N
        openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        fileMenu.add(openMenuItem);

        saveMenuItem.setAction(actionMap.get("saveFileAction")); // NOI18N
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("saveAsFileAction")); // NOI18N
        saveAsMenuItem.setText(resourceMap.getString("saveAsMenuItem.text")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        fileMenu.add(saveAsMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        coordinateLabel.setText(resourceMap.getString("coordinateLabel.text")); // NOI18N
        coordinateLabel.setName("coordinateLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coordinateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 377, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coordinateLabel)
                        .addContainerGap())))
        );

        openFileChooser.setName("openFileChooser"); // NOI18N

        saveFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveFileChooser.setName("saveFileChooser"); // NOI18N

        colorChooser.setName("colorChooser"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void canvasMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvasMouseMoved
        updateCoordinate(evt.getPoint());
    }//GEN-LAST:event_canvasMouseMoved

    private void canvasMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvasMouseDragged
        updateCoordinate(evt.getPoint());
    }//GEN-LAST:event_canvasMouseDragged

    private void canvasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvasMousePressed
        
    }//GEN-LAST:event_canvasMousePressed

    private void canvasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvasMouseReleased
        
    }//GEN-LAST:event_canvasMouseReleased
    
    private void color1BoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_color1BoxMouseClicked
        Color c = JColorChooser.showDialog(null, null, color1);
        if (c!=null) {
            color1 = c;
            color1Box.setBackground(color1);
        }
    }//GEN-LAST:event_color1BoxMouseClicked

    private void color2BoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_color2BoxMouseClicked
        Color c = JColorChooser.showDialog(null, null, color2);
        if (c != null) {
            color2 = c;
            color2Box.setBackground(color2);
        }
    }//GEN-LAST:event_color2BoxMouseClicked

    @Action
    public void selectSelect() {
        canvasPanel.selectedTool = Tool.SELECT;
    }

    @Action
    public void selectLine() {
        canvasPanel.selectedTool = Tool.LINE;
    }

    @Action
    public void selectBox() {
        canvasPanel.selectedTool = Tool.BOX;
    }

    @Action
    public void selectBorder() {
        canvasPanel.selectedTool = Tool.BORDER;
    }

    @Action
    public void selectFill() {
        canvasPanel.selectedTool = Tool.FILL;
    }

    @Action
    public void openFileAction() {
        if (isDirty) {
            int opt = confirmAndSave();
            if (opt == JOptionPane.CANCEL_OPTION) return;
        }

        int status = openFileChooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            try {
                file = openFileChooser.getSelectedFile();
                load(file);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ファイルを読み込めませんでした。", "エラー", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                file = null;
            }
        }
    }

    public void isDirty(boolean dirty) {
        this.isDirty = dirty;
        updateTitle();
    }

    public void updateTitle() {
        String newTitle = APP_NAME;
        if (file != null) newTitle += " - " + file.getName();
        if (isDirty) newTitle += " *";
        ((MockDesignerApp) getApplication()).getMainFrame().setTitle(newTitle);
    }

    private void load(File file) throws Exception {
        Document doc = new SAXBuilder().build(file);
        Element page = doc.getRootElement();
        int newW = Integer.parseInt(page.getAttributeValue("width"));
        int newH = Integer.parseInt(page.getAttributeValue("height"));

        Iterator it = page.getChildren().iterator();
        updatePropertiesView(null);
        canvasPanel.componentManager.clearAll();
        while(it.hasNext()) {
            Element elem = (Element) it.next();
            Component compnent = loadComponent(elem);
            canvasPanel.componentManager.addComponent(compnent);
        }
        canvasPanel.setPreferredSize(new Dimension(newW, newH));
        canvasPanel.setSize(newW, newH);
        canvasPanel.repaint();
        isDirty = false;
        updateTitle();
    }
    
    private Component loadComponent(Element elem) {
        if (elem.getName().equalsIgnoreCase("line")) {
            Line line = new Line();
            line.build(elem);
            return line;
        } else if (elem.getName().equalsIgnoreCase("box")) {
            Box box = new Box();
            box.build(elem);
            return box;
        } else if (elem.getName().equalsIgnoreCase("image")) {
            Picture pic = new Picture();
            pic.build(elem);
            return pic;
        }
        throw new IllegalArgumentException("Unknown element " + elem.getName());
    }

    @Action
    public boolean saveFileAction() {
        boolean isSaved = false;
        if (file == null)
            isSaved = saveAsFileAction();
        else 
            isSaved = save();
        return isSaved;
    }

    public int confirmAndSave() {
        int opt = JOptionPane.showConfirmDialog(null, "現在のファイルを保存しますか？");
        if (opt == JOptionPane.YES_OPTION) {
            if (saveFileAction()) return JOptionPane.YES_OPTION;
            else return JOptionPane.CANCEL_OPTION;
        }
        return opt;
    }
    
    @Action
    public boolean saveAsFileAction() {
        int status = saveFileChooser.showSaveDialog(null);
        boolean isSaved = false;
        if (status == JFileChooser.APPROVE_OPTION) {
            file = saveFileChooser.getSelectedFile();
            isSaved = save();
        }
        return isSaved;
    }

    private boolean save() {
        Writer out = null;
        try {
            Element page = new Element("page");
            page.setAttribute("width", Integer.toString(canvasPanel.getWidth()));
            page.setAttribute("height", Integer.toString(canvasPanel.getHeight()));
            Document document = new Document(page);
            for (Component component : canvasPanel.componentManager.getComponents()) {
               page.addContent(component.toXML());
            }
            out = new OutputStreamWriter(new FileOutputStream(file), "Windows-31J");
            Format format = Format.getPrettyFormat();
            format.setEncoding("Windows-31J");
            XMLOutputter xmlout = new XMLOutputter(format);

            xmlout.output(document, out);
            isDirty = false;
            updateTitle();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "ファイルを保存できませんでした。", "エラー", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (out != null) {
                try { 
                    out.close();
                } catch(Exception e) {}
            }
        }
        return false;
    }

    @Action
    public void newAction() {
        if (isDirty) {
            int opt = confirmAndSave();
            if (opt == JOptionPane.CANCEL_OPTION) return;
        }
        
        String newSize = JOptionPane.showInputDialog(null, "新しいキャンバスのサイズを入力してください（例:480x480）");
        if (newSize == null || newSize.isEmpty()) return;
        String[] data = newSize.split("x");
        if (data.length != 2) return;
        try {
            int newW = Integer.parseInt(data[0]);
            int newH = Integer.parseInt(data[1]);
            canvasPanel.setPreferredSize(new Dimension(newW, newH));
            canvasPanel.setSize(newW, newH);
            
            file = null;
            updatePropertiesView(null);
            canvasPanel.componentManager.clearAll();
            isDirty = false;
        } catch(Exception e) {
            return;
        }

        updateTitle();
        canvasPanel.repaint();
    }

    @Action
    public void selectImage() {
        canvasPanel.selectedTool = Tool.IMAGE;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton borderButton;
    private javax.swing.JToggleButton boxButton;
    private javax.swing.JPanel canvas;
    private javax.swing.JPanel color1Box;
    private javax.swing.JPanel color2Box;
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JLabel coordinateLabel;
    private javax.swing.JToggleButton fillButton;
    private javax.swing.JToggleButton imageButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToggleButton lineButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTable propertyTable;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JToggleButton selectButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.ButtonGroup toolGroup;
    private javax.swing.JPanel toolPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
