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

/**
 * The Class FontGFXGlyph.
 */
public class FontGFXGlyph {
  
  /** The b valid. */
  public boolean bValid;        ///< Indicates either a control character or unassigned space.
  
  /** The bitmap offset. */
  public int     bitmapOffset;  ///< Index into our giant GFXfont->bitmap
  
  /** The width. */
  public int     width;         ///< Bitmap dimensions in pixels
  
  /** The height. */
  public int     height;        ///< Bitmap dimensions in pixels - more of base line position
  
  /** The x advance. */
  public int     xAdvance;      ///< Distance to advance cursor (x axis)
  
  /** The x offset. */
  public int     xOffset;       ///< X dist from cursor pos to UL corner
  
  /** The y offset. */
  public int     yOffset;       ///< Y dist from cursor pos to UL corner
  
  /** The bitmap. */
  public int[]   bitmap;        ///< Character bitmap
  
  /** The ch. */
  public char    ch;            ///< The character
  
  /**
   * To hex string.
   *
   * @param bFirst
   *          the b first
   * @return the <code>string</code> object
   */
  public String toHexString(boolean bFirst) {
    if (bitmap.length <= 0) {
        return "";
    }
    StringBuilder builder = new StringBuilder();
    if (bFirst)
      builder.append("    ");
    else 
      builder.append("   ");
    int i;
    for (i = 0; i < bitmap.length; i++) {
      if (i % 15 == 0 && i > 0) {
        builder.append("\n   ");
      }
      if (!bFirst) {
          builder.append(",");
      }
      builder.append(String.format("0x%02X", (byte) bitmap[i]));
    }
    if (i % 15 > 0)
      for (int j = i%15; j<15; j++) 
        builder.append("     ");
    if (bValid)
      builder.append(" // '" + String.valueOf(ch) + "'\n");
    else
      builder.append(" // " + "\n");
    return builder.toString();

  }

}
