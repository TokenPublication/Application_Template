package com.tokeninc.sardis.application_template.UI.Printer;

import android.os.RemoteException;
import androidx.annotation.NonNull;

import com.example.printertest.IPrinterService;

public class StyledString {
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
