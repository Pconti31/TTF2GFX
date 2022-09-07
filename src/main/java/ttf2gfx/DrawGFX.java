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

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import java.awt.Font;

/**
 * The Class DrawGFX.
 */
public class DrawGFX extends JPanel {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant dashed. */
  final static public  BasicStroke dashed = new BasicStroke(3.0f, 
      BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 5.0f, new float[]{5.0f}, 0);
  
  /** The Constant dotted. */
  final static public  BasicStroke dotted = new BasicStroke(1.0f, 
      BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1.0f,2.0f}, 0);

  /** The Constant PANEL_WIDTH. */
  private static final int PANEL_WIDTH = 490;
  
  /** The scroll max row. */
  private int SCROLL_MAX_ROW = 0;
  
  /** The grid height. */
  private int GRID_HEIGHT = 560;
  
  /** The Constant NUM_COLS. */
  private static final int NUM_COLS = 14;
  
  /** The Constant BOX_WIDTH. */
  private static final int BOX_WIDTH = 35;
  
  /** The Constant BOX_HEIGHT. */
  private static final int BOX_HEIGHT = 35;
  
  /** The Constant MARGIN. */
  private static final int MARGIN = 0;
  
  /** The font. */
  private Font font;
  
  /** The font code. */
  private Font fontCode;
  
  /** The n scroll pos. */
  protected int   nScrollPos=0;

  /** The current character. */
  private CharacterHelper currentCharacter = null;
  
  /** The current character. */
  private CharacterHelper trackCharacter = null;
  
  /** The character list. */
  public static List<CharacterHelper> characterList;

  /**
   * Create the dialog.
   */
  public DrawGFX() {
    this.addMouseListener(new MouseHandler());
    characterList = new ArrayList<CharacterHelper>();
  }
  
  /**
   * Delete char.
   */
  public void deleteChar() {
    if (currentCharacter == null) {
      JOptionPane.showMessageDialog(null, 
          "You need to select a character to be deleted inside choosen character set", 
          "WARNING",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    currentCharacter.ch = " ";
    currentCharacter.replace_code = -1;
    currentCharacter.bValid = true;
    clearCurChar();
    repaint();
  }
  
  /**
   * Replace char.
   *
   * @param availChars
   *          the avail chars
   */
  public void replaceChar(DrawTTF availChars) {
    CharacterHelper selected = availChars.getSelectedChar();
    if (selected == null) {
      JOptionPane.showMessageDialog(null, 
          "Please select a character in the available character set first", 
          "WARNING",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (currentCharacter == null) {
      JOptionPane.showMessageDialog(null, 
          "You need to select a character to be replaced inside choosen character set", 
          "WARNING",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    currentCharacter.ch = selected.ch;
    currentCharacter.replace_code = selected.nCode;
    currentCharacter.bValid = false;
    clearCurChar();
    availChars.clearCurChar();
  }
  
  /**
   * Drop char.
   *
   * @param availChars
   *          the avail chars
   */
  public void dropChar(CharacterHelper selected, Point p) {
    trackCharacter = null;
    if (selected == null) {
      JOptionPane.showMessageDialog(null, 
          "Please select a character in the available character set first", 
          "WARNING",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    currentCharacter = findChar(p);
    if (currentCharacter != null) {
      currentCharacter.ch = selected.ch;
      currentCharacter.replace_code = selected.nCode;
      currentCharacter.bValid = true;
      System.out.println("Drop loc: "+ currentCharacter.r);
      clearCurChar();
      FontBuilder.availableCharPane.clearCurChar();
      repaint();
    }
  }
  
  /**
   * Clear cur char.
   */
  public void clearCurChar() {
    currentCharacter = null;
  }
  
  /**
   * Update font.
   *
   * @param ttfFile
   *          the ttf file
   */
  public void updateFont(File ttfFile) {
    fontCode = new Font("Dialog", Font.PLAIN, 12);

    //create the font to use.
    try {
    font = Font.createFont(Font.TRUETYPE_FONT, ttfFile).deriveFont(20f);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //register the font
    ge.registerFont(font);
    } catch (Exception e) {
      return;
    }
    addToCharacterList();
  }
  
  /**
   * Adds the to character list.
   *
   * @param nMaxRows
   *          the n max rows
   */
  public void addToCharacterList() {
    currentCharacter = null;
    characterList.clear();
    SCROLL_MAX_ROW = FontBuilder.END_CODE / NUM_COLS;
    if (SCROLL_MAX_ROW/2*2 != SCROLL_MAX_ROW) SCROLL_MAX_ROW++;
    String testCh = null;
    Point p;
    Rectangle rect=null;
    int x = MARGIN;
    int y = 0;
    int code_point = FontBuilder.START_CODE;
    for (int row=0; row<=SCROLL_MAX_ROW; row++) {
      for (int col=0; col<NUM_COLS; col++) {
        if (code_point > FontBuilder.END_CODE) break;
        testCh =  String.format("%c", code_point);
        p = new Point(x, y);
        rect = new Rectangle(p.x,p.y, BOX_WIDTH,BOX_HEIGHT);
        if (canDisplay(code_point)) {
            String sCode = String.format("%03d", code_point);
            characterList.add(new CharacterHelper(row,rect,testCh,code_point,sCode,true));
        } else {
            String sCode = String.format("%03d", code_point);
            characterList.add(new CharacterHelper(row,rect,testCh,code_point,sCode,false));
        }
//        int idx = row * NUM_COLS + col;
//        System.out.println(String.format("list[%d] starts code %d char[%s]",idx,code_point,testCh));
        x += BOX_WIDTH;
        code_point++;
      }
      x = MARGIN;
      y += BOX_HEIGHT;
    }
//    System.out.println(String.format("DrawGFX->GRID_HEIGHT: %d SCROLL_MAX_ROW: %d",GRID_HEIGHT,SCROLL_MAX_ROW));
  }

  /**
   * Find char.
   *
   * @param p
   *          the p
   * @return <code>CharacterHelper</code>, if successful or null
   */
  public CharacterHelper findChar(Point p) {
//    System.out.println(String.format("findChar point=%s",p.toString()));
    for (CharacterHelper h : characterList) {
      if (h.contains(p)) {
//      System.out.println(String.format("***findChar MATCH->%s",h.toString()));
        return h;
      }
    }
    return null;
  }
  
  /**
   * Draw selection rect.
   *
   * @param g2d
   *          the graphics object
   * @param b
   *          the bounded <code>Rectangle</code> object
   */
  public void drawSelRect(Graphics2D g2d, Rectangle b) {
    Stroke defaultStroke = g2d.getStroke();
    g2d.setColor(Color.BLUE);
//    g2d.setStroke(dashed);
    g2d.setStroke(new BasicStroke(6f));
    g2d.drawRect(b.x-2, b.y-2, b.width+4, b.height+4);
    g2d.setStroke(defaultStroke);  
  }
  
  /**
   * Highlight char for a drag operation.
   *
   * @param p
   *          the p
   */
  public void highlightChar(Point p) {
    currentCharacter = null;
    CharacterHelper h = findChar(p);
    if (h != null) {
      if (trackCharacter == null) {
        trackCharacter = h;
        repaint();
      } else if (trackCharacter.nCode != h.nCode) {
         trackCharacter = h;
         repaint();
      }
    }
  }
  
  /**
   * Draw dragging rect.
   *
   * @param g2d
   *          the graphics object
   * @param b
   *          the bounded <code>Rectangle</code> object
   */
  public void drawTrackRect(Graphics2D g2d) {
    CharacterHelper h;
    Stroke defaultStroke = g2d.getStroke();
    if (trackCharacter != null) {
      h = trackCharacter;
      g2d.setColor(Color.BLACK);
      g2d.setStroke(dashed);
      g2d.drawRect(h.r.x-2, h.r.y-2, BOX_WIDTH, BOX_WIDTH);
      g2d.setStroke(defaultStroke);  
    }
  }

  /**
   * The Class MouseHandler for characterPane.
   */
  private class MouseHandler extends MouseAdapter {

    /**
     * mouseClicked.
     *
     * @param e
     *          the e
     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      Point mousePt = e.getPoint();
      currentCharacter = findChar(mousePt);
      if (currentCharacter != null) {
        repaint();
      }
    }
  }
  
    
  /**
   * paintComponent.
   *
   * @param g
   *          the g
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (GRID_HEIGHT == 0) return;
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(Color.WHITE);
    g2d.fillRect(MARGIN, 0, PANEL_WIDTH, GRID_HEIGHT);
    drawCoordinates(g2d, PANEL_WIDTH+MARGIN, GRID_HEIGHT);
    g2d.setColor(Color.BLACK);
    int yOffset = BOX_HEIGHT/2+2;
    for (CharacterHelper h : characterList) {
//      System.out.println(String.format("x=%d y=%d code %d char[%s]",h.r.x,h.r.y,h.nCode,h.ch));
      g2d.setFont(font);
      if (h.bValid) {
        g2d.setColor(Color.BLACK);
      } else {
        g2d.setColor(Color.RED);
      }
      g2d.drawString(h.ch,h.r.x+10,h.r.y+yOffset);
      g2d.setFont(fontCode);
      g2d.drawString(h.sCode,h.r.x+5,h.r.y+BOX_HEIGHT);

    }
    if (trackCharacter != null) {
      drawTrackRect(g2d);
    } else if (currentCharacter != null) 
      drawSelRect(g2d, currentCharacter.r);
    g2d.dispose();
  }
  
  /**
   * Draw grid coordinates.
   *
   * @param g2d
   *          the graphics object
   * @param w
   *          the width of simulated TFT screen
   * @param h
   *          the height of simulated TFT screen
   */
  private void drawCoordinates(Graphics2D g2d, int w, int h) {
    int x, y, dx, dy, dw, dh;
    // draw X axis
    dy = 0;
    dh = h;
    g2d.setColor(Color.GRAY);
    for (x=MARGIN; x<=(w-BOX_WIDTH)+MARGIN; x+=BOX_WIDTH) {
      dx = x;
      g2d.drawLine(dx, dy, dx, dh);
    }
    // draw Y axis  
    dx = MARGIN;
    dw = w;
    for (y=0; y<h; y+=BOX_HEIGHT) {
      dy = y;
      g2d.drawLine(dx, dy, dw, dy);
    }
    g2d.drawRect(MARGIN, 0, w-MARGIN, h);
  }
  
  /**
   * canDisplay
   */
  public boolean canDisplay(int codePoint) {
    if (codePoint == (int)'\n' || codePoint == (int)'\r') { // ignore newlines
      return false;
    }
    boolean bCanDisplay = false;
    try {
      if (font.canDisplay(codePoint)) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( codePoint );
        bCanDisplay = (!Character.isISOControl(codePoint)) &&
          (codePoint != KeyEvent.CHAR_UNDEFINED) &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
      }
      } catch(IllegalArgumentException e) {
        ;
      }
    return bCanDisplay;
  }

}

