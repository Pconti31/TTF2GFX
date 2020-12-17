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

import java.io.File;

/**
 * The Class ThemeInfo.
 */
public class ThemeInfo {
  
  /** The name. */
  public final String name;
  
  /** The theme file. */
  public final File themeFile;
  
  /** The laf class name. */
  public final String lafClassName;
  
  /**
   * Instantiates a new theme info.
   *
   * @param name
   *          the name
   * @param themeFile
   *          the theme file
   * @param lafClassName
   *          the laf class name
   */
  public ThemeInfo( String name, File themeFile, String lafClassName ) {
    this.name = name;
    this.themeFile = themeFile;
    this.lafClassName = lafClassName;
  }
  
}
