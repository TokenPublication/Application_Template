package com.tokeninc.sardis.application_template.Helpers.PrintHelpers;

import com.token.printerlib.PrinterDefinitions;
import com.token.printerlib.StyledString;

public class BasePrintHelper {

    static void addText(StyledString styledText, String text, PrinterDefinitions.Alignment alignment) {
        addText(styledText, text, alignment, 11, 0);
    }

    static void addText(StyledString styledText, String text, PrinterDefinitions.Alignment alignment, int fontSize, float lineSpacing) {
        styledText.setFontFace(PrinterDefinitions.Font_E.SourceCodePro);
        styledText.setFontSize(fontSize);
        styledText.setLineSpacing(lineSpacing);
        styledText.addTextToLine(text, alignment);
    }

    static void addTextToNewLine(StyledString styledText, String text, PrinterDefinitions.Alignment alignment) {
        styledText.newLine();
        addText(styledText, text, alignment);
    }

    static void addTextToNewLine(StyledString styledText, String text, PrinterDefinitions.Alignment alignment, int fontSize) {
        styledText.newLine();
        addText(styledText, text, alignment, fontSize, 0);
    }

}
