# ttf2gfx utility

### Brief Overview
The ttf2gfx is a standalone desktop application to allow conversion of TrueType fonts to Adafruit's GFX Font format.

The main advantage of the ttf2gfx utility is that it supports a simply way for users to pluck individual glyphs out of a larger font and assign them slots between 0 and 255. 

Now even if a conversion program supports character 128-255 IDE's like Arduino IDE use utf8 
encoding not 8 bit ISO8859 so your characters could be a problem when defining C string literals. 
The GUIslice Builder has been modified to detect this and output such literals as hexadecimal escape 
strings so you don't have to do the mapping yourself.  

The GUIslice Builder also makes to easy to enter characters that would otherwise be difficult to find and enter into text fields. With release 0.16.b003 you simply need to right click on a "Text" property and a mimi popup will appear allowing you to invoke a CharacterMap dialog that will show you all of the characters in a font and allow you to select and then copy them to your Text property.

See: <https://github.com/ImpulseAdventure/GUIslice-Builder>

### Usage

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

### Acknowledgements

The Non-Built in Java Themes are supported by the FlatLaf project. 

The FlatLaf project is on GitHub: 
<https://github.com/JFormDesigner/FlatLaf>

FontConverterGFX.java by SQUIX78 greatly increased the speed of implemenation of this utility.

See: <https://github.com/squix78/esp8266-oled-ssd1306-font-converter> 