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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The Class Ttf2GfxApp.
 * Allows a user to select characters from a TrueType font and 
 * carry them over to a font created for adafruit's GFX graphics package.
 * 
 * @author Paul Conti
 */
public class Ttf2GfxApp implements ActionListener {
  
  /** The content pane. */
  private FontBuilder contentPane;
  
  /** version number for our appliaction */
  public static final String VERSION = "2.00";

  /** version number for user preferences */
  public static final String VERSION_NO = "-1";
  
  /** our top frame */
  JFrame frame;

  /**
   * Launch the application.
   *
   * @param args
   *          the arguments
   */
  public static void main(String[] args) {
    Ttf2GfxApp prog = new Ttf2GfxApp();
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ThemeManager.loadThemes();
          ThemeManager.getDefaultLookAndFeel();
          prog.initUI();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public Ttf2GfxApp() {
  }
  
  /**
   * Initializes the UI.
   */
  public void initUI() {
    frame = new JFrame();
    frame.setTitle("Ttf2GfxApp Font Converter");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);
    
    JMenu mbFile = new JMenu("File");
    menuBar.add(mbFile);
    
    JMenuItem openMenuItem = new JMenuItem("Open",
        new ImageIcon(Ttf2GfxApp.class.getResource("/resources/open.png")));
    openMenuItem.setToolTipText("Open image file");
    openMenuItem.addActionListener(this);
    openMenuItem.setActionCommand("open");
    mbFile.add(openMenuItem);
    
    JMenuItem exportMenuItem = new JMenuItem("Export",
        new ImageIcon(Ttf2GfxApp.class.getResource("/resources/export.png")));
    exportMenuItem.setToolTipText("Export file with image stored as C Array");
    exportMenuItem.setActionCommand("export");
    exportMenuItem.addActionListener(this);
    mbFile.add(exportMenuItem);
    
    JMenuItem exitMenuItem = new JMenuItem("Exit",
        new ImageIcon(Ttf2GfxApp.class.getResource("/resources/logout.png")));
    exitMenuItem.setToolTipText("Exit Program");
    exitMenuItem.setActionCommand("exit");
    exitMenuItem.addActionListener(this);
    mbFile.add(exitMenuItem);
    
    JMenu mnOptions = new JMenu("Options");
    menuBar.add(mnOptions);
    
    JMenuItem meniItemThemes = new JMenuItem("Themes");
    meniItemThemes.setActionCommand("themes");
    meniItemThemes.addActionListener(this);
    mnOptions.add(meniItemThemes);
    menuBar.add(mnOptions);
    
    JMenu mnHelp = new JMenu("Help");
    menuBar.add(mnHelp);
    
    JMenuItem meniItemAbout = new JMenuItem("About");
    meniItemAbout.setIcon(new ImageIcon(Ttf2GfxApp.class.getResource("/resources/about.png")));
    meniItemAbout.setActionCommand("about");
    meniItemAbout.addActionListener(this);
    mnHelp.add(meniItemAbout);
    
    contentPane = new FontBuilder();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    frame.setBounds(100,100,870, 565);
    frame.getContentPane().add(contentPane);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  /**
   * actionPerformed.
   *
   * @param e
   *          the e
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String command = ((AbstractButton) e.getSource()).getActionCommand();
//    System.out.println("command: " + command);
    switch(command) {
      case "about":
        JOptionPane.showMessageDialog(null, "Program to convert TrueType Fonts\n" 
        + "to Adafruit's GFX font format.\n\n"
        + " ver " + VERSION + "\n" 
        + "Copyright (c) 2020 Paul Conti\n"
        , "About Image2C", JOptionPane.INFORMATION_MESSAGE);
        break;
    
      case "open":
        contentPane.openFont();
        break;

      case "exit":
        System.exit(0);
        break;
        
      case "export":
       FontGenerator.generateFontSet(FontBuilder.ttfFile);
       break;

      case "themes":
        String[] themes = ThemeManager.getOptions();
        JComboBox<String> comboBox = new JComboBox<String>(themes); 
        comboBox.setSelectedIndex(ThemeManager.defLafIndex);
        Object[] message = {
            "Choose Theme:", comboBox,
          };
        int option = JOptionPane.showConfirmDialog(null, message, "Theme", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
          return;
        }
        String name = (String)comboBox.getSelectedItem();
        ThemeManager.saveLookAndFeel(name);
        ThemeManager.setLookAndFeel(name);
        SwingUtilities.updateComponentTreeUI(frame);
        break;
        
      default:
        throw new IllegalArgumentException("Invalid menu item: " + command);
    }

  }

}
