/**
 *
 * The MIT License
 *
 * Copyright 2020 Paul Conti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package ttf2gfx;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JSplitPane;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.awt.event.ActionEvent;

/**
 * The Class FontBuilder.
 * 
 * @author Paul Conti
 */
public class FontBuilder extends JPanel {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /** The txt font. */
  private JTextField txtFont;
  
  /** The chosen char pane. */
  private DrawGFX chosenCharPane;
  
  /** The available char pane. */
  public static DrawTTF availableCharPane;
  
  /** The ttf file. */
  public static File ttfFile = null;
  
  /** The scroll pane. */
  public JScrollPane scrollPane;

  /** The Constant START_CODE. */
  public static int START_CODE = 32;
  
  /** The end code. */
  public static int END_CODE = 255;
  
  public JFormattedTextField txtStartCode;

  /** The txt end code. */
  public JFormattedTextField txtEndCode;
  
  /** The lbl status message. */
  static JLabel lblStatusMessage;
  private JLabel lblBusyWait;
  
  /**
   * Create the panel.
   */
  public FontBuilder() {
    setLayout(new BorderLayout());
    setBounds(100,100,1015, 577);
    
    JPanel infoPane = new JPanel();
    infoPane.setPreferredSize(new Dimension(1015,50));
    add(infoPane, BorderLayout.NORTH);
    
    txtFont = new JTextField();
    txtFont.setColumns(32);
    txtFont.setEditable(false);
    
    JButton btnBrowse = new JButton("Browse...");
    btnBrowse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new PostWorker().execute();
      }
    });

    Icon icon = new ImageIcon(Ttf2GfxApp.class.getResource("/resources/export.png"));
    JButton btnExport = new JButton("Export");
    btnExport.setToolTipText("Convert to GFX Font File");
    btnExport.setIcon(icon);
    btnExport.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        FontGenerator.generateFontSet(ttfFile);
      }
    });
    
    JButton btnSearchCode = new JButton("Search");
    btnSearchCode.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        availableCharPane.searchCharCode();
      }
    });
    btnSearchCode.setToolTipText("Enter a character's code.  Either as a decimal or as hexadecimal value.");
    
    JLabel lblStartCode = new JLabel("Start Code:");
    txtStartCode = new JFormattedTextField(); 
    // TAB Key Support
    txtStartCode.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        SwingUtilities.invokeLater(new Runnable() {  
          @Override  
          public void run() {
            txtStartCode.selectAll();  
          }   
        });  
      }

      @Override
      public void focusLost(FocusEvent e) {
        try {
          txtStartCode.commitEdit();
        } catch (ParseException ep) {
        }
        START_CODE = Integer.valueOf(txtStartCode.getText());
        chosenCharPane.addToCharacterList();
        repaint();
      }
    });
    txtStartCode.setColumns(4);
    txtStartCode.setValue(String.valueOf(START_CODE));
    txtStartCode.setToolTipText("Enter starting character code value in decimal. Do not use hexadecimal.");
    // ENTER Key Support
    txtStartCode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        try {
          txtStartCode.commitEdit();
        } catch (ParseException e) {
        }
        START_CODE = Integer.valueOf(txtStartCode.getText());
        chosenCharPane.addToCharacterList();
        repaint();
      }
    });
    JLabel lblEndCode = new JLabel("End Code:");
    txtEndCode = new JFormattedTextField(); 
    // TAB Key Support
    txtEndCode.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        SwingUtilities.invokeLater(new Runnable() {  
          @Override  
          public void run() {
            txtEndCode.selectAll();  
          }   
        });  
      }

      @Override
      public void focusLost(FocusEvent e) {
        try {
          txtEndCode.commitEdit();
        } catch (ParseException ep) {
        }
        END_CODE = Integer.valueOf(txtEndCode.getText());
        chosenCharPane.addToCharacterList();
        repaint();
      }
    });
    txtEndCode.setColumns(4);
    txtEndCode.setValue(String.valueOf(END_CODE));
    txtEndCode.setToolTipText("Enter ending character code value in decimal. Do not use hexadecimal.");
    // ENTER Key Support
    txtEndCode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        try {
          txtEndCode.commitEdit();
        } catch (ParseException e) {
        }
        END_CODE = Integer.valueOf(txtEndCode.getText());
        chosenCharPane.addToCharacterList();
        repaint();
      }
    });
    JButton btnExit = new JButton("Exit");
    Icon iconExit = new ImageIcon(Ttf2GfxApp.class.getResource("/resources/logout.png"));
    btnExit.setIcon(iconExit);
    btnExit.setActionCommand("exit");
    btnExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        System.exit(0);
      }
    });
    btnExit.setToolTipText("Exit Application");
    
    GroupLayout gl_infoPane = new GroupLayout(infoPane);
    gl_infoPane.setHorizontalGroup(
      gl_infoPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_infoPane.createSequentialGroup()
          .addContainerGap()
          .addComponent(txtFont, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
          .addGap(18)
          .addComponent(btnBrowse)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(btnSearchCode)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(lblStartCode)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(txtStartCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(lblEndCode)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(txtEndCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(btnExport)
          .addGap(18)
          .addComponent(btnExit)
          .addContainerGap(81, Short.MAX_VALUE))
    );
    gl_infoPane.setVerticalGroup(
      gl_infoPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_infoPane.createSequentialGroup()
          .addGap(11)
          .addGroup(gl_infoPane.createParallelGroup(Alignment.BASELINE)
            .addComponent(txtFont, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(btnBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSearchCode)
            .addComponent(lblStartCode)
            .addComponent(txtStartCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(lblEndCode)
            .addComponent(txtEndCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(btnExport)
            .addComponent(btnExit))
          .addGap(10))
    );
    infoPane.setLayout(gl_infoPane);
    
    JPanel statusBar = new JPanel();
    add(statusBar, BorderLayout.SOUTH);
    
    JPanel buttonPane = new JPanel();
    
    
    lblStatusMessage = new JLabel("Begin by selecting a Font");
    
    GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
    gl_buttonPane.setHorizontalGroup(
      gl_buttonPane.createParallelGroup(Alignment.TRAILING)
        .addGroup(Alignment.LEADING, gl_buttonPane.createSequentialGroup()
          .addGap(53)
          .addComponent(lblStatusMessage)
          .addContainerGap(735, Short.MAX_VALUE))
    );
    gl_buttonPane.setVerticalGroup(
      gl_buttonPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_buttonPane.createSequentialGroup()
          .addContainerGap()
          .addComponent(lblStatusMessage)
          .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    buttonPane.setLayout(gl_buttonPane);
    
    JLabel lblNewLabel = new JLabel("Drag characters from this Panel");
    GroupLayout gl_statusBar = new GroupLayout(statusBar);
    gl_statusBar.setHorizontalGroup(
      gl_statusBar.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_statusBar.createSequentialGroup()
          .addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 497, GroupLayout.PREFERRED_SIZE)
          .addGap(152)
          .addComponent(lblNewLabel)
          .addContainerGap(206, Short.MAX_VALUE))
    );
    gl_statusBar.setVerticalGroup(
      gl_statusBar.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_statusBar.createSequentialGroup()
          .addGroup(gl_statusBar.createParallelGroup(Alignment.LEADING)
            .addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGroup(gl_statusBar.createSequentialGroup()
              .addContainerGap()
              .addComponent(lblNewLabel)))
          .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    statusBar.setLayout(gl_statusBar);
    
    JPanel mainPane = new JPanel(new BorderLayout());
    JPanel titlesPane = new JPanel();
    titlesPane.setPreferredSize(new Dimension(1015,50));
    JLabel lblChoosenChars = new JLabel("Choosen Characters");
    mainPane.add(titlesPane,BorderLayout.NORTH);
    JLabel lblAvailableChars = new JLabel("Available Characters");
    
    JButton btnDelete = new JButton("Delete Character");
    btnDelete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chosenCharPane.deleteChar();
      }
    });
    
    GroupLayout gl_titlesPane = new GroupLayout(titlesPane);
    gl_titlesPane.setHorizontalGroup(
      gl_titlesPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_titlesPane.createSequentialGroup()
          .addGap(28)
          .addComponent(lblChoosenChars)
          .addGap(90)
          .addComponent(btnDelete)
          .addPreferredGap(ComponentPlacement.RELATED, 316, Short.MAX_VALUE)
          .addComponent(lblAvailableChars)
          .addGap(267))
    );
    gl_titlesPane.setVerticalGroup(
      gl_titlesPane.createParallelGroup(Alignment.TRAILING)
        .addGroup(Alignment.LEADING, gl_titlesPane.createSequentialGroup()
          .addContainerGap()
          .addGroup(gl_titlesPane.createParallelGroup(Alignment.BASELINE)
            .addComponent(btnDelete)
            .addComponent(lblChoosenChars)
            .addComponent(lblAvailableChars))
          .addContainerGap(16, Short.MAX_VALUE))
    );
    titlesPane.setLayout(gl_titlesPane);
    
    JSplitPane splitPane = new JSplitPane();
    
    chosenCharPane = new DrawGFX();
    chosenCharPane.setPreferredSize(new Dimension(490,565));
    splitPane.setLeftComponent(chosenCharPane);
    chosenCharPane.setLayout(null);
    // now setup for drag and drop from availableCharPanel
    new MyDropTargetListener(chosenCharPane);
    TransferHandler dnd = new TransferHandler() {
      private static final long serialVersionUID = 1L;
      @Override
      public boolean canImport(TransferSupport support) {
          if (!support.isDrop()) {
              return false;
          }
          //only Strings
          if (!support.isDataFlavorSupported(CharacterSelection.characterFlavor)) {
              return false;
          }
          return true;
      }
      @Override
      public boolean importData(TransferSupport support) {
          if (!canImport(support)) {
              return false;
          }

          Transferable transferable = support.getTransferable();
          DropLocation dropLocation = support.getDropLocation();
          Point dropPoint = dropLocation.getDropPoint();
          CharacterHelper h;
          try {
              h = (CharacterHelper) transferable.getTransferData(CharacterSelection.characterFlavor);
          } catch (Exception e) {
              e.printStackTrace();
              return false;
          }
          chosenCharPane.dropChar(h, dropPoint);
          return true;
      }
    };
    chosenCharPane.setTransferHandler(dnd);
    
    lblBusyWait = new JLabel();
    lblBusyWait.setIcon(new ImageIcon(Ttf2GfxApp.class.getResource("/resources/busywait.gif")));
    lblBusyWait.setBounds(230, 204, 48, 48);
    lblBusyWait.setVisible(false);
    chosenCharPane.add(lblBusyWait);
    
    availableCharPane = new DrawTTF();
    availableCharPane.setPreferredSize(new Dimension(490,7500));
    // setup for drag operations
    MyDragGestureListener dlistener = new MyDragGestureListener();
    DragSource dragSource = new DragSource();
    DragSourceMotionListener dsml = new DragSourceMotionListener() {
      @Override
      public void dragMouseMoved(DragSourceDragEvent dsde) {
        Point pts = new Point(dsde.getX(),dsde.getY());
        SwingUtilities.convertPointFromScreen(pts, chosenCharPane);
        chosenCharPane.highlightChar(pts);
//        System.out.println("x/y [" + dsde.getX() + "," + dsde.getY() + "] pts: " + pts.toString());
      }
    };
    dragSource.addDragSourceMotionListener(dsml);
    dragSource.createDefaultDragGestureRecognizer(availableCharPane, DnDConstants.ACTION_COPY, dlistener);
    
    scrollPane = new JScrollPane(availableCharPane,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setAutoscrolls(false);
    splitPane.setRightComponent(scrollPane);
    
    mainPane.add(splitPane, BorderLayout.CENTER);
    add(mainPane, BorderLayout.CENTER);
  }

  /**
   * Post status msg.
   *
   * @param message
   *          the message
   * @param timeout
   *          the timeout
   */
  public static void postStatusMsg(String message, int timeout) {
    lblStatusMessage.setText(message);
    Timer timer = new Timer(timeout, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        lblStatusMessage.setText("");
      }
    });
    timer.setRepeats(false); // Only execute once
    timer.start(); // Go go go!
  }

  /**
   * Choose font.
   */
  public int ChooseFont() {
    String workingDir = getWorkingDir();
    File currentDirectory = new File(workingDir);
    JFileChooser chooser = new JFileChooser(currentDirectory);
    chooser.addChoosableFileFilter(new FileFilter() {
      public String getDescription() {
        String descr = new String("TrueTypeFont File *.ttf");
        return descr;
      }

      public boolean accept(File f) {
        if (f.getName().toLowerCase().endsWith(".ttf"))
          return true;
        return false;
      }
    });
    chooser.setDialogTitle("Choose your TrueType Font");
    File file = null;
    int option = chooser.showDialog(null, "Select Font");
    if (option == JFileChooser.APPROVE_OPTION) {
      file = chooser.getSelectedFile();
    }
    if (file != null) {
      ttfFile = file;
      // create the font
      try {
        // create the font to use. Specify the size!
        Font font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(12f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // register the font
        ge.registerFont(font);
        String family = font.getFamily();
        txtFont.setText(family);
        chosenCharPane.updateFont(ttfFile);
        availableCharPane.updateFont(ttfFile);
      } catch (IOException e) {
        postStatusMsg(e.toString(), 10000);
        option = JFileChooser.ERROR_OPTION;
      } catch (FontFormatException e) {
        postStatusMsg(e.toString(), 10000);
        option = JFileChooser.ERROR_OPTION;
      }
    }
    return option;
  }

  /**
   * getWorkingDir - attempts to find the directory where our executable is
   * running.
   *
   * @return workingDir - our working directory
   */
  public static String getWorkingDir() {
    // The code checking for "lib" is to take care of the case 
    // where we are running not inside eclipse IDE
    String workingDir;
    String strUserDir = System.getProperty("user.dir");
    int n = strUserDir.indexOf("lib");
    if (n > 0) {
      strUserDir = strUserDir.substring(0,n-1);  // remove "/bin"
    }
    workingDir = strUserDir + System.getProperty("file.separator"); 
    return workingDir;
  }
  
  public void openFont() {
    new PostWorker().execute();
  }
  
  // just some logging
  protected void debug(DragSourceEvent dsde) {
  }

  class MyDropTargetListener extends DropTargetAdapter {

//    private DropTarget dropTarget;
    private DrawGFX p;

    public MyDropTargetListener(DrawGFX panel) {
      p = panel;
//      dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
    }

    @Override
    public void drop(DropTargetDropEvent event) {
      try {
        DropTarget test = (DropTarget) event.getSource();
        Component ca = (Component) test.getComponent();
        Point dropPoint = ca.getMousePosition();
        Transferable tr = event.getTransferable();

        if (event.isDataFlavorSupported(CharacterSelection.characterFlavor)) {
            CharacterHelper h = (CharacterHelper) tr.getTransferData(CharacterSelection.characterFlavor);

            if (h != null) {
                p.dropChar(h,dropPoint);
                p.revalidate();
                p.repaint();
                availableCharPane.clearCurChar();
                event.dropComplete(true);
            }
        } else {
            event.rejectDrop();
        }
        setCursor(Cursor.getDefaultCursor());
          
      } catch (Exception e) {
          e.printStackTrace();
          event.rejectDrop();
      }
    }
  }

  class MyDragGestureListener implements DragGestureListener {

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
      DrawTTF panel = (DrawTTF) event.getComponent();
      Point p = event.getDragOrigin();
      CharacterHelper h = panel.dragChar(p);
      CharacterSelection selection = new CharacterSelection(h);
      if (selection != null) {
        Cursor ghost = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        Stroke defaultStroke = g2d.getStroke();
        g2d.setFont(panel.font);
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawString(h.ch,10,20);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawRect(0, 0, 30, 30);
        g2d.setStroke(defaultStroke);  
        g2d.dispose();
        try {
          ghost = Toolkit.getDefaultToolkit().createCustomCursor(
             img, 
             new Point(0,0),
             "ghost");
        }catch(Exception e) {}
        event.startDrag(ghost, selection);
      }
    }
  }

  class PostWorker extends SwingWorker<Integer, Integer> {
    @Override
    protected Integer doInBackground() throws Exception {
      // Do a time-consuming task.
      lblBusyWait.setVisible(true);
      repaint();
      int status = ChooseFont();
      lblBusyWait.setVisible(false);
      return status;
    }


    @Override
    protected void done() {
      try {
        if (get()== JFileChooser.APPROVE_OPTION) {
          postStatusMsg("Font Loaded! Now either export your font or begin replacing characters", 10000);
          repaint();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
