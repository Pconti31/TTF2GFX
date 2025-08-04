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

The first 127 characters will be automatically loaded into slots 0-255. 

Note that the first 32 slots are control characters that should not be over-written.

You will notice any non-displayable characters will shown as a red box. 

Start choosing any additional characters from the available character set and dragging them over one by one slots
in the choosen character set and dropping them in place.

Continue selectiong characters until you are happy with the choosen character set.

After you press export you will be asked for the size of the font from 8 to 72 and for a name.  
A default name of your input font will be provided but you can override it.

Press the "Exit" button to exit.

Other features are present. 

Rather than drag and drop each character you can copy entire code blocks.

You can search the available character set for a particular glyph if you know its unicode value. 
This value can be entered as either a hexadecimal number of the form 0xnnnn or as a decimal ten based number.

It's also not necessary to use all 255 slots. You can enter a smaller number inside the "Ending GFX Position" text box.

## Enhancement
Renamed Search to Go To Unicode.

The needed version of Java is now custom built and distributed with 
the release so you no longer need to deal with its installation.

A windows installer has also been added.

### Enhancements for 2.05
Dragging and Dropping characters from a true type font over to a GFX font works fine but can be hard to do when you 
have a large number characters to move. So a batch copy of code blocks has been added. So say you were creating 
a Japanese font using Hiragana and Katakana.  You could first copy over numbers 0-9 using code block starting 30 
and ending 30 to gfx codes 33-42, then copy over Hiragana code block starting 3040 to 309f to GFX code 43,
then ending with Katakana code block 30a0 to 30ff to gfx code 151. Change the text field "Ending GFX Position" 
from 255 to 246 and export.

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