package com.tokeninc.sardis.application_template.Printer;

/* Order of enumerations should not be changed, it has to match the enumarations on the native side  */

import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.example.printertest.IPrinterService;

public class PrinterDefinitions {

    // Order of following enumeration has to be same as the one on the native side

    enum Font_E
    {
        SourceCodePro("Source Code Pro"),
        Code_Bold("Source Code Pro (Bold)"),
        SourceSansPro("Source Sans Pro"),
        Sans_Semi_Bold("Source Sans Pro(Semi Bold)"),
        Sans_Bold("Source Sans Pro (Bold)"),
        Monaco_Linux("Monaco Linux"),
        Go("Go"),
        Go_Bold("Go Bold"),
        Tahoma("Tahoma"), // if you add those fonts again, update getIntCode function
        Tahoma_Bold("Tahoma Bold"),
        basicFont_8x8("Basic 8x8 Font"),
        openGlFont_8x13("OpenGl 8x13 Font"),
        vcrOcdMono_21x16("VCR OCD Mono 21x16"),
        Go_Mono("Go Mono"),
        Go_Mono_Bold("Go Mono Bold");

        private String friendlyName;
        private int intCode;

        private Font_E(String friendlyName){
            this.friendlyName = friendlyName;
        }

        @Override public String toString(){
            return friendlyName;
        }

    }

    static final public Font_E defaultFont = Font_E.SourceSansPro;
    static final public int defaultFontSize = 12;
    static public Integer[] fontSizes = {8, 9, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 64, 72, 96, 144};

    enum PrinterErrorCode
    {
        NO_ERROR (0),
        VP_VOLTAGE_ERROR (-1),
        VP_VOLTAGE_INITIALIZATION_ERROR (-2),
        HEAD_TEMPERATURE_ERROR (-3),
        FUSE_BLOWN_ERROR (-4),
        OUT_OF_PAPER_ERROR (-5),
        PAPER_SENSOR_ERROR (-6),
        UNEXPECTED_FIELD_IDENTIFIER_ERROR (-7),
        UNEXPECTED_RESPONSE_SIZE_ERROR (-8),
        READ_TIMEOUT_ERROR (-9),
        READ_WRITE_ERROR (-10),
        CANNOT_OPEN_DEVICE_ERROR (-11),
        OTHER_ERROR (-99),
        DEAD_SERVICE (-101),
        COULD_NOT_GET_STATUS (-102),
        NO_SERVICE (-103);

        private int errCode;

        PrinterErrorCode(int numVal) {
            this.errCode = numVal;
        }

        public int getNumVal() {
            return errCode;
        }

        public boolean Compare(int i){return errCode == i;}

        public static PrinterErrorCode GetValue(int _id)
        {
            PrinterErrorCode[] codes = PrinterErrorCode.values();
            for(int i = 0; i < codes.length; i++)
            {
                if(codes[i].Compare(_id))
                    return codes[i];
            }
            return COULD_NOT_GET_STATUS;
        }


    };

    enum Alignment { Left, Center, Right };



}

enum StyleFunctionCode
{
    ADD_TEXT_TO_LINE ('a'),
    PRINT_BITMAP ('b'),
    ADD_SPACE ('c'),
    DRAW_LINE ('d'),
    ADD_EMPTY_LINES ('e'),
    SET_FONT_FACE ('f'),
    SET_CURSOR_POSITION ('p'),
    SET_FONT_SIZE ('s'),
    SET_LINE_SPACING ('t');


    private char code;

    private StyleFunctionCode(char friendlyName){
        this.code = friendlyName;
    }

    @Override public String toString(){
        return String.valueOf(code);
    }
};

class StyledString {
    private final String openingTag = "<s>";
    private String bufferStr = openingTag;

    /* Clears the text buffer to its initial state*/
    public void clear() {
        bufferStr = openingTag;
    }

    /* Prints styled text*/
    public void print(IPrinterService ps) {
        try {
            ps.printText(bufferStr);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    @NonNull
    @Override
    public String toString() {
        return bufferStr;
    }

    /*
        Appends a new line to character to text, use this function can be avoided by manually adding newline character in other functions, like addText
        equivalent to addText("\n");
     */
    public void newLine() {
        bufferStr += "\n";
    }

    /* Add text to styled text, can be multiline and can contain tabs */
    public void addText(String text) {
        bufferStr += text;
    }

    /*
        Adds Text to current line starting from cursor position
        after adding all context of a line, newLine() should be called
    */
    public void addTextToLine(String text) {
        bufferStr += String.format("<%s,'%s',%d>", StyleFunctionCode.ADD_TEXT_TO_LINE, text, PrinterDefinitions.Alignment.Left.ordinal());
    }

    /*
        Adds Text to current line.
        If alignment is left, text is adding beginning from the cursor position.
        If alignment is right or center, text is placed accordingly (cursor position is ignored and not incremented).
        after adding all context of a line, newLine() should be called
     */
    public void addTextToLine(String text, PrinterDefinitions.Alignment alignment) {
        bufferStr += String.format("<%s,'%s',%d>", StyleFunctionCode.ADD_TEXT_TO_LINE, text, alignment.ordinal());
    }

    /* Prints a preloaded monochrome bitmap */
    public void printBitmap(String filename) {
        bufferStr += String.format("<%s,'%s',0>", StyleFunctionCode.PRINT_BITMAP, filename);
    }

    /* Prints a preloaded monochrome bitmap */
    public void printBitmap(String filename, int verticalMargin) {
        bufferStr += String.format("<%s,'%s',%d>", StyleFunctionCode.PRINT_BITMAP, filename, verticalMargin);
    }

    /* Leaves a blank space of given height in pixels */
    public void addSpace(int height) {
        bufferStr += String.format("<%s,%d>", StyleFunctionCode.ADD_SPACE, height);
    }

    /* Draws a horizontal line with given thickness and margins */
    public void drawLine(int thickness, int verticalMargin, int horizontalMargin) {
        bufferStr += String.format("<%s,%d,%d,%d>", StyleFunctionCode.DRAW_LINE, thickness, verticalMargin, horizontalMargin);
    }

    /* Leaves a blank space of given height in line heights (1.5 Lines, for example) */
    public void addEmptyLines(float lineCount) {
        bufferStr += String.format("<%s,%f>", StyleFunctionCode.ADD_EMPTY_LINES, lineCount);
    }

    /*Sets the font face. Selection will be effective until a new selection is made*/
    public void setFontFace(PrinterDefinitions.Font_E font) {
        bufferStr += String.format("<%s,%d>", StyleFunctionCode.SET_FONT_FACE, font.ordinal());
    }

    /*Sets the font size. Selection will be effective until a new selection is made*/
    public void setFontSize(int size) {
        bufferStr += String.format("<%s,%d>", StyleFunctionCode.SET_FONT_SIZE, size);
    }

    /* Sets cursor position to given value */
    public void setCursorPosition(int cursorPosition) {
        bufferStr += String.format("<%s,%d>", StyleFunctionCode.SET_CURSOR_POSITION, cursorPosition);
    }

    /* Sets line spacing to given value */
    public void setLineSpacing(float lineSpacing) {
        bufferStr += String.format("<%s,%f>", StyleFunctionCode.SET_LINE_SPACING, lineSpacing);
    }
}

