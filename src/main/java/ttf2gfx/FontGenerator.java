/**
 *
 * The MIT License
 *
 * Copyright 2020-2022 Paul Conti
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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * The Class FontGenerator.
 * Based upon Adafruit fontconvert.c and FontConverterGFX.java by SQUIX78
 * 
 * https://learn.adafruit.com/adafruit-gfx-graphics-library/using-fonts
 * https://github.com/adafruit/Adafruit-GFX-Library/blob/master/fontconvert/fontconvert.c
 * https://github.com/squix78/esp8266-oled-ssd1306-font-converter
 */
public class FontGenerator {

  /** The Font sizes. */
  private static Integer[] FontSizes= {8,10,12,14,16,18,20,22,24,26,28,36,48,72};
  
  /** The g 2 d. */
  private static Graphics2D g2d;
  
  /** The font metrics. */
  private static FontMetrics fontMetrics;
  
  /** The image. */
  private static BufferedImage image;
  
  /** The baseline Y. */
//  private static int baselineY;
  
  /** The font. */
  private static Font font = null;
  
  private static int nTotalBytes;
  
  /* Java Fonts are in Points with 72 points per inch (DPI)
   * Windows used to be 96 DPI
   * while Adafruit GFX converter uses a DPI of 141
   * so our scaling factor is 141 / 72.
   */
  private static float scaleFactor = 1.95833f;

  /**
   * Instantiates a new font generator.
   */
  public FontGenerator() {
  }

  /**
   * Generate font set.
   *
   * @param ttfFile
   *          the ttf font file
   */
  public static void generateFontSet(File ttfFile) {
    try {
      JComboBox<Integer> cbSizes = new JComboBox<Integer>(FontSizes);
      JTextField txtOutputName = new JTextField();
      String outputFileName = ttfFile.getName().toLowerCase();
      int idx = outputFileName.indexOf(".ttf");
      if (idx != -1) {
        outputFileName = outputFileName.substring(0,idx);
      }
      outputFileName = outputFileName.substring(0,idx);
      outputFileName = outputFileName.replaceAll("[\\s\\-\\.]", "_");
      txtOutputName.setText(outputFileName);
      nTotalBytes = 0;
      Object[] message = {
        "Font Size:", cbSizes,
        "Output file:", txtOutputName
      };
      int option = JOptionPane.showConfirmDialog(null, message, "Export", JOptionPane.OK_CANCEL_OPTION);
      if (option != JOptionPane.OK_OPTION) {
        return;
      }
      Integer iSize = (Integer) cbSizes.getSelectedItem();
      String userFileName = txtOutputName.getText();
      userFileName = userFileName.replaceAll("[\\s\\-\\.]", "_");
      
      outputFileName = FontBuilder.getWorkingDir() + userFileName + "_" + iSize.toString() + "pt" + ".h";
      //create the font to use.
      /* Use scaling factor to reset size of font,
       * If we don't account for this the font will appear half the size
       * of Adafruit's Free Fonts.
       */
      float fSize = iSize.floatValue() * scaleFactor;
      font = Font.createFont(Font.TRUETYPE_FONT, ttfFile).deriveFont(fSize);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      //register the font
      ge.registerFont(font);

      image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
      g2d = (Graphics2D) image.getGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      g2d.setFont(font);
      fontMetrics = g2d.getFontMetrics();
//    baselineY = fontMetrics.getMaxAscent();
      ArrayList<FontGFXGlyph> fontList = new ArrayList<FontGFXGlyph>();
      for (CharacterHelper h : DrawGFX.characterList) {
        fontList.add(decodeChar(h));
      }
      StringBuilder sBd = new StringBuilder();
      File fontSet = new File(outputFileName);
      String fontName = fontSet.getName();
      int n = fontName.indexOf(".h");
      fontName = fontName.substring(0,n);
      buildFontSet(sBd, fontName, fontList);
      BufferedWriter bw = null;

      bw = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(fontSet), "UTF-8"));
      bw.write(sBd.toString());
      bw.flush();
      bw.close();
      FontBuilder.postStatusMsg(String.format("created->%s",outputFileName),10000);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, 
          e.toString(), 
          "ERROR",
          JOptionPane.ERROR_MESSAGE);
    }
  }
  
  /**
   * Builds the font set.
   *
   * @param sBd
   *          the s bd
   * @param fontName
   *          the font name
   * @param fontList
   *          the font list
   */
  private static void buildFontSet(StringBuilder sBd, String fontName, ArrayList<FontGFXGlyph> fontList) {
    sBd.append("// Created by ttf2gfx utility\n");
    sBd.append("const uint8_t " + fontName + "Bitmaps[] PROGMEM = {\n");
    
    sBd.append("    // Bitmap Data:\n");
    
    int counter = 0;
    int jumpPointer = 0;
    boolean bFirst = true;
    for (FontGFXGlyph letter : fontList) {
        letter.bitmapOffset = jumpPointer;
        sBd.append(letter.toHexString(bFirst));
        bFirst = false;
        jumpPointer += letter.bitmap.length;
        counter++;
        nTotalBytes += letter.bitmap.length;
    }
    nTotalBytes += DrawGFX.characterList.size() * 7; // now account for Glyphs array
    sBd.append("};\n");
    sBd.append("const GFXglyph " + fontName + "Glyphs[] PROGMEM = {\n");
    sBd.append("// bitmapOffset, width, height, xAdvance, xOffset, yOffset\n");
    counter = 0;
    for (FontGFXGlyph letter : fontList) {
    
        sBd.append("\t");
        sBd.append(String.format("  { %5d, %3d, %3d, %3d, %4d, %4d }",
                letter.bitmapOffset, letter.width, letter.height,
                letter.xAdvance, letter.xOffset, letter.yOffset));
    
        if (counter < fontList.size() - 1) {
            sBd.append(",");
        }
        if (letter.bValid)
          sBd.append(" // '" + String.valueOf(letter.ch) + "'\n");
        else 
          sBd.append(" // " + "\n");
        counter++;
    }
  
    sBd.append("};\n");
    
    sBd.append("const GFXfont " + fontName + " PROGMEM = {\n");
    sBd.append("    (uint8_t  *)" + fontName + "Bitmaps,\n");
    sBd.append("    (GFXglyph *)" + fontName + "Glyphs,\n");
    sBd.append(String.format("    0x%02X, \n", (byte) FontBuilder.START_CODE));
    sBd.append(String.format("    0x%02X, \n", (byte) FontBuilder.END_CODE));
    sBd.append(String.format("    %d\n};\n", (byte) fontMetrics.getHeight()));
    sBd.append(String.format("// Approx. %d bytes", nTotalBytes));
  }
  
  /**
   * Decode char.
   *
   * @param code_point
   *          the code point
   * @return the <code>font GFX glyph</code> object
   */
  private static FontGFXGlyph decodeChar(CharacterHelper h) {
    char ch;
    if (h.replace_code == -1)
      ch = (char)h.nCode;
    else
      ch = (char)h.replace_code;
    
    Rectangle r = getTextBounds(ch);
    copyChar(r,ch);

    int height = Math.max(1, r.height);
    int width = Math.max(1, r.width);

    FontGFXGlyph glyph = new FontGFXGlyph();
    glyph.height = height;
    glyph.width = width;
    glyph.xOffset = r.x;
    glyph.yOffset = r.y;
    glyph.xAdvance = fontMetrics.charWidth(ch) + 1;
    glyph.ch = ch;
    glyph.bValid = h.bValid; 

    int arraySize = (int) Math.ceil(width * height / 8.0);
    int character[] = new int[arraySize];

    int bitNum = 0;
// System.out.println("** ch[" + ch + "] **");
    for (int y = 0; y < height; y++) {

// System.out.println();
        for (int x = 0; x < width; x++) {
            int byteNum = bitNum / 8;
            int bitPos = 7 - (bitNum % 8);
            int currentByte = character[byteNum];
            if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
// System.out.print("X");
                currentByte = currentByte | (1 << bitPos);
            } else {
// System.out.print("_");
                currentByte = currentByte & ~(1 << bitPos);
            }
            character[byteNum] = (byte) currentByte;
            bitNum++;
        }
    }
// System.out.println();
// System.out.println("** end **");
    
    glyph.bitmap = character;

    return glyph;
  }

  /**
   * Copy char.
   *
   * @param r
   *          the r
   * @param c
   *          the c
   */
  private static void copyChar(Rectangle r, char c) {
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, 450, 250);
    g2d.setColor(Color.BLACK);
    g2d.drawString(String.valueOf(c),  - r.x, - r.y);
  }
  
  /**
   * Gets the text bounds.
   *
   * @param c
   *          the c
   * @return the text bounds
   */
  private static Rectangle getTextBounds(char c) {
    FontRenderContext frc = g2d.getFontRenderContext();
    GlyphVector gv = g2d.getFont().createGlyphVector(frc, String.valueOf(c));
    return gv.getPixelBounds(null, 0, 0);
  }

}
