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

import java.awt.Point;
import java.awt.Rectangle;

/**
 * The Class CharacterHelper.
 */
public class CharacterHelper {
  
  /** The ch. */
  public String ch;
  
  /** Replacement character */
  public int replace_code;
  
  /** The r. */
  public Rectangle r;
  
  /** The row this character appears on */
  public int row;
  
  /** The n code. */
  public int nCode;
  
  /** The s code. */
  public String sCode;
  
  /** The b valid. */
  public boolean bValid;
  
  /**
   * Instantiates a new character helper.
   *
   * @param r
   *          the r
   * @param ch
   *          the ch
   * @param nCode
   *          the n code
   * @param sCode
   *          the s code
   * @param bValid
   *          the b valid
   */
  public CharacterHelper(int row, Rectangle r, String ch, int nCode, String sCode, boolean bValid) {
    this.row = row;
    this.ch = ch;
    this.replace_code = -1;
    this.r = r;
    this.nCode = nCode;
    this.sCode = sCode;
    this.bValid = bValid;
  }
  
  /**
   * Return true if this node contains p.
   *
   * @param p
   *          the <code>Point</code> object
   * @return <code>true</code>, if successful
   */
  public boolean contains(Point p) {

    return r.contains(p);
  }

  /**
   * toString.
   *
   * @return the <code>string</code> object
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("CharacterHelper: %s code: %s [%d] rect: %s", ch, sCode, nCode, r.toString());
  }
}
