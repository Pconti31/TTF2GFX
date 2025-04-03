# ttf2gfx utility

## Brief Overview
The ttf2gfx is a standalone desktop application to allow conversion of TrueType fonts to Adafruit's GFX Font format. 

The main advantage of the ttf2gfx utility is that it supports a simply way for users to pluck individual glyphs out of a larger font and assign them slots between 0 and 255. 

Now even if a conversion program supports character 128-255 IDE's like Arduino IDE use UTF8 
encoding not 8 bit ISO8859 so your characters could be a problem when defining C string literals.
 
The GUIslice Builder has been modified to detect this and output such literals as hexadecimal escape 
strings so you don't have to do the mapping yourself.  

The GUIslice Builder also makes to easy to enter characters that would otherwise be difficult 
to find and enter into text fields. With release 0.16.b003 and higher you simply need to right 
click on a "Text" property and a mini popup will appear allowing you to invoke a CharacterMap 
dialog that will show you all of the characters in a font and allow you to select and then copy 
them to your Text property.

See: <https://github.com/ImpulseAdventure/GUIslice-Builder>

## Install instructions
<p>
The needed version of Java is now custom built and distributed with 
the release so you no longer need to deal with its installation.
</p>

<p>
Download ttf2gfx-{opsys}-{arch}.{release}.{build}.{zip,tgz} using 
operating system and cpu architecture (ie., win-x64, linux-x64) from github.
Then untar or unzip into your folder of choice and use either ttf2gfx.bat or 
ttf2gfx.sh to run the program.
</p>

### LINUX and MACOS install Example

Open a terminal
```
cd $HOME
tar xvzf ttf2gfx-linux-x64-1.0.tar.gz
```
Then enter the new TTFGFX folder and run
```
cd TTFGFX
./ttf2gfx.sh
```

## Usage

After opening the application you begin by pressing the "Browse" button and selecting a TrueType font to use as input.

The first 255 characters will be automatically loaded into slots 0-255. 

Note that most fonts have 32 control characters in slots 128 to 159. You will notice such characters displayed as a red box.  These slots can be assigned other character codes. 

If after loading your font you are satisfied with the resulting character set simply press export button. 

Otherwise you can start choosing characters from the available character set and selecting a slot to place the choosen character into the choosen character set. 

Once both characters have been selected press "`<<<<Replace`" button to make the substitution. Continue selectiong characters until you are happy with the choosen character set.

After you press export you will be asked for the size of the font from 8 to 72 and for a name.  A default name of your input font will be provided but you can override it.

Press the "x" in the top frame of the ttf2gfx app to exit.

Two other minor features are present. 

You can search the available character set for a particular glyph if you know its unicode value. This value can be entered as either a hexadecimal number of the form 0xnnnn or as a decimal ten based number.

It's also not necessary to use all 255 slots. You can enter a smaller number inside the "End Code" text box.

## Enhancement
Renamed Search to Go To Unicode.

The needed version of Java is now custom built and distributed with 
the release so you no longer need to deal with its installation.

A windows installer has also been added.

### Bug fixes for 2.04
Issue 14 - Error: Unable to access jarfile ttf2gfx-1.00.jar using linux or mac
Turns out the new ttf2gfx.sh wasn't checkin to github. No jar file is needed now that a custom built Java is included.
The correct ttf2gfx.sh script has been added.

### Bug fixes for 2.03
Issue 12 - Missing code points per row.

### Bug fixes for 2.02
Issue 10 - Manual code point entry for Chinese fonts.

### Bug fixes for 2.01
Export didn't always inform you of problems and simply looked like it was hung.

### Bug fixes for 2.00

Added execute permissions to Java for Linux and MacOS.

## Bug Fixes 1.03
TrueType Fonts with more than 32767 code points did not display characters 32768 and higher.

## Acknowledgements

The Non-Built in Java Themes are supported by the FlatLaf project. 

The FlatLaf project is on GitHub: 
<https://github.com/JFormDesigner/FlatLaf>

FontConverterGFX.java by SQUIX78 greatly increased the speed of implemenation of this utility.

See: <https://github.com/squix78/esp8266-oled-ssd1306-font-converter> 