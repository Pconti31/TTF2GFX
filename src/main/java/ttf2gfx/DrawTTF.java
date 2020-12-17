package ttf2gfx;

//import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
//import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Scrollable;

import java.awt.Font;

public class DrawTTF extends JPanel implements Scrollable {
  private static final long serialVersionUID = 1L;

  private static final int PANEL_WIDTH = 490;
  private int MAXCODEPOINT = 0;
  private int SCROLL_TOP_ROW = 0;
  private int SCROLL_MAX_ROW = 0;
//  private int Y_AXIS = 0;
  private int GRID_HEIGHT = 560;
  private static final int NUM_ROWS = 16;
  private static final int NUM_COLS = 16;
  private static final int BOX_WIDTH = 35;
  public static final int BOX_HEIGHT = 35;
  private static final int MARGIN = 0;
  
  public  Font font;
  private Font fontCode;

  protected int   nScrollPos=0;
  private CharacterHelper currentCharacter = null;
  private List<CharacterHelper> characterList;
  private List<Integer> rowList;

  /**
   * Create the dialog.
   */
  public DrawTTF()  {
    this.addMouseListener(new MouseHandler());
    characterList = new ArrayList<CharacterHelper>();
    rowList = new ArrayList<Integer>();
  }
  
  public CharacterHelper getSelectedChar() {
    return currentCharacter;
  }
  
  public void clearCurChar() {
    currentCharacter = null;
  }
  
  public void updateFont(File ttfFile) {
    currentCharacter = null;
    characterList.clear();
    rowList.clear();
    
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

    MAXCODEPOINT = Short.MAX_VALUE;
    String testCh = null;
    Point p;
    Rectangle rect=null;
    int x = MARGIN;
    int y = 0;
    int row = 0;
    int col = 0;
    for (int i=32; i<=MAXCODEPOINT; i++) {
      if (col % NUM_COLS == 0 && col > NUM_COLS-1) {
        x = MARGIN;
        y += BOX_HEIGHT;
        row++; 
        col = 0;
      }
      // as we scroll we may already have this char in out list
      testCh =  String.format("%c", i);
      p = new Point(x, y);
      rect = new Rectangle(p.x,p.y, BOX_WIDTH,BOX_HEIGHT);
      if (canDisplay(i)) {
        String sCode = String.format("x%04x", i);
        characterList.add(new CharacterHelper(row, rect,testCh,i,sCode,true));
        x += BOX_WIDTH;
        col++;
      }
    }
    SCROLL_MAX_ROW = row;
    GRID_HEIGHT = y + BOX_HEIGHT;
  }

  public void searchCharCode() {
    String sCodePoint = (String) JOptionPane.showInputDialog(null,
        "Enter Code Point:",
        "Enter width", JOptionPane.PLAIN_MESSAGE, null, null, null);
    int code_point;
    try {
      if (sCodePoint.isEmpty() || sCodePoint == null)
        return;
      if (sCodePoint.toLowerCase().startsWith("0x") ||
          sCodePoint.toLowerCase().startsWith("x")) {
        code_point = Integer.decode(sCodePoint);
      } else {
        code_point = Integer.parseInt(sCodePoint);
      }
    } catch (NumberFormatException nfExc) {
      JOptionPane.showConfirmDialog(null, "Input must be number! Decimal or Hexidecimal", "Error!",
          JOptionPane.PLAIN_MESSAGE);
      return;
    }
    for (CharacterHelper h : characterList) {
      if (h.nCode == code_point) {
        scrollRectToVisible(h.r);
        currentCharacter = h;
        repaint();
        return;
      }
    }
  }
  public boolean findChar(Point p) {
//    System.out.println(String.format("findChar point=%s",p.toString()));
    for (CharacterHelper h : characterList) {
      if (h.contains(p)) {
        currentCharacter = h;
//        System.out.println(String.format("***findChar MATCH->%s",h.toString()));
        return true;
      }
    }
    return false;
  }
  
  public CharacterHelper dragChar(Point p) {
//  System.out.println(String.format("findChar point=%s",p.toString()));
  for (CharacterHelper h : characterList) {
    if (h.contains(p)) {
      currentCharacter = h;
//      System.out.println(String.format("***findChar MATCH->%s",h.toString()));
//      repaint();
      return currentCharacter;
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
/*
  public void drawSelRect(Graphics2D g2d, Rectangle b) {
    Stroke defaultStroke = g2d.getStroke();
    g2d.setColor(Color.BLUE);
//    g2d.setStroke(dashed);
    g2d.setStroke(new BasicStroke(6f));
    g2d.drawRect(b.x-2, b.y-2, b.width+4, b.height+4);
    g2d.setStroke(defaultStroke);  
  }
*/ 
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
    int idx = 0;
    g2d.setColor(Color.BLACK);
//    System.out.println(String.format("row=%d nMaxRow=%d",SCROLL_TOP_ROW,nMaxRow));
    int yOffset = BOX_HEIGHT/2+2;
    for (int row=0; row<=SCROLL_MAX_ROW; row++) {
      for (int col=0; col<NUM_COLS; col++) {
        idx = row * NUM_COLS + col;
        if (idx < characterList.size()) {
          CharacterHelper h = characterList.get(idx);
//            System.out.println(String.format("idx=%d [%d][%d] code %d char[%s]",idx,h.r.x,h.r.y,h.nCode,h.ch));
          if (h.bValid) {
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(h.ch,h.r.x+10,h.r.y+yOffset);
            g2d.setFont(fontCode);
            g2d.drawString(h.sCode,h.r.x,h.r.y+BOX_HEIGHT);
          }
        }
      }
    }

//    if (currentCharacter != null) 
//      drawSelRect(g2d, currentCharacter.r);
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

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    //Get the current position.
    int currentPosition = visibleRect.y;
    //Return the number of pixels between currentPosition
    //and the nearest tick mark in the indicated direction.
    if (direction < 0) {
      int newPosition = currentPosition - (currentPosition / BOX_HEIGHT) * BOX_HEIGHT;
        nScrollPos = (newPosition == 0) ? BOX_HEIGHT : newPosition;
        if (SCROLL_TOP_ROW > 0) {
          SCROLL_TOP_ROW--;
        } else {
          nScrollPos = 0;
        }
    } else {
      if (SCROLL_TOP_ROW+NUM_ROWS < SCROLL_MAX_ROW-1) {
        nScrollPos = ((currentPosition / BOX_HEIGHT) + 1) * BOX_HEIGHT - currentPosition;
        SCROLL_TOP_ROW++;
      } else {
        nScrollPos = 0;
      }

    }
//    System.out.println(String.format("SCROLL_TOP_ROW=%d nScrollPos=%d",SCROLL_TOP_ROW,nScrollPos));
    clearCurChar();
    return nScrollPos;
  }

  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
//    System.out.println(String.format("getScrollableBlockIncrement SCROLL_TOP_ROW=%d nScrollPos=%d",SCROLL_TOP_ROW,nScrollPos));
    //Get the current position.
    int currentPosition = visibleRect.y;
//    System.out.println("BlockIncrement: y=" + currentPosition);

    //Return the number of pixels between currentPosition
    //and the nearest tick mark in the indicated direction.
    if (direction < 0) {
      int newPosition = currentPosition - (currentPosition / BOX_HEIGHT) * BOX_HEIGHT;
        nScrollPos = (newPosition == 0) ? BOX_HEIGHT : newPosition;
        if (SCROLL_TOP_ROW > 0) {
          SCROLL_TOP_ROW--;
        } else {
          nScrollPos = 0;
        }
    } else {
      if (SCROLL_TOP_ROW+NUM_ROWS < SCROLL_MAX_ROW-1) {
        nScrollPos = ((currentPosition / BOX_HEIGHT) + 1) * BOX_HEIGHT - currentPosition;
        SCROLL_TOP_ROW++;
      } else {
        nScrollPos = 0;
      }

    }
//    System.out.println(String.format("SCROLL_TOP_ROW=%d nScrollPos=%d",SCROLL_TOP_ROW,nScrollPos));
    clearCurChar();
    return nScrollPos;
  }

  @Override
  public boolean getScrollableTracksViewportWidth() {
    return false;
  }

  @Override
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }

  /**
   * The Class MouseHandler
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
      if (findChar(mousePt)) {
        repaint();
      }
    }
    
    /**
     * mouseReleased
     *
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }
  }

}

