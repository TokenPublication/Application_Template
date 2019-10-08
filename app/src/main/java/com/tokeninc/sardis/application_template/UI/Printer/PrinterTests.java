package com.tokeninc.sardis.application_template.UI.Printer;

import android.content.Context;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import com.example.printertest.IPrinterService;
import com.tokeninc.sardis.application_template.UI.Printer.PrinterDefinitions.Alignment;
import com.tokeninc.sardis.application_template.UI.Printer.PrinterDefinitions.Font_E;


public class PrinterTests {

    public static final int bottomMargin = 120; // margin that is left after each print

    static void TestFonts(IPrinterService ps) {
        try {
            for (Font_E font : Font_E.values()) {
                ps.setFontFace(font.ordinal());
                for (int fontSize : PrinterDefinitions.fontSizes) {
                    if (fontSize > 20)
                        continue;

                    ps.setFontSize(fontSize);
                    ps.addTextToLine(font.toString() + " " + fontSize + "pt ", PrinterDefinitions.Alignment.Left.ordinal());
                    ps.printLine();
                    ps.addTextToLine("SOME TEXT String 08", PrinterDefinitions.Alignment.Left.ordinal());
                    ps.printLine();
                }

            }
            ps.addSpace(bottomMargin);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static void PrintLoremIpsum(IPrinterService ps, boolean uppercase)
    {
        try {
            String loremIpsumStr = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.\nProin sollicitudin a dolor sit amet porttitor.\nCras sed felis at erat tempus\nmalesuada accumsan vel augue.\nNunc sed lorem elementum,\npretium metus sit amet, ornare urna.\nProin a massa a arcu placerat lobortis.\nPraesent mattis, lacus a consequat pellentesque,\narcu dolor fringilla lorem,\nat malesuada nunc arcu vel felis.\nAliquam facilisis erat tempor diam maximus euismod a a arcu. ";
            if(uppercase)
                loremIpsumStr= loremIpsumStr.toUpperCase();
            ps.setLineSpacing(1.0f);
            ps.printText(loremIpsumStr);
            ps.addSpace(bottomMargin);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static void PrintAllCharacters(IPrinterService ps, boolean includeTurkishCharacters)
    {
        String text = "";

        for (int i=32; i<128; i++)
        {
            text += (char)i;
            if (i % 16 == 0)
                text += '\n';
        }

        if (includeTurkishCharacters)
            text += "\nçÇğĞıİöÖşŞüÜ" + "\u20BA"; // turkish characters and turkish lira symbol

        try
        {
            ps.printText(text);
            ps.addSpace(bottomMargin);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }

    public static void PrintSampleReceipt(IPrinterService ps)
    {
        final int lineThickness = 2;
        try
        {
            ps.setFontFace(Font_E.Sans_Semi_Bold.ordinal());
            ps.setFontSize(16);
            ps.setLineSpacing(1.4f);
            ps.printText("Sales Report");

            ps.setFontFace(Font_E.SourceSansPro.ordinal());
            ps.setFontSize(14);
            ps.setLineSpacing(1.1f);
            ps.printText("February 1, 2019 12::00AM-\n"
                    + "February 1, 2019 12::00AM");

            ps.setFontFace(Font_E.Sans_Semi_Bold.ordinal());
            ps.drawLine(lineThickness,10,0);

            ps.setLineSpacing(1.0f);
            ps.printText("SALES SUMMARY");
            ps.drawLine(lineThickness,10,0);

            ps.setLineSpacing(1.3f);
            ps.printText("Gross Sales\t\t 14.00 ₺");

            ps.setFontFace(Font_E.SourceSansPro.ordinal());

            ps.printText("Refunds\t\t\t 0.00 ₺\n"
                    + "Discounts\t\t\t 0.00 ₺");

            ps.setFontFace(Font_E.Sans_Semi_Bold.ordinal());
            ps.printText("Net Sales\t\t\t 14.00 ₺");
            ps.setFontFace(Font_E.SourceSansPro.ordinal());
            ps.printText("Tax\t\t\t\t $0.00\n"
                    +"Tip\t\t\t\t $0.00\n"
                    +"Gift Card Sales\t\t 0.00 ₺\n"
                    +"Partial Refunds\t\t 0.00 ₺");

            ps.setFontFace(Font_E.Sans_Semi_Bold.ordinal());
            ps.printText("Total Collected\t\t 0.00 ₺");
            ps.setFontFace(Font_E.SourceSansPro.ordinal());
            ps.printText( "Cash\t\t\t\t 14.00 ₺\n"
                    + "Card\t\t\t\t 0.00 ₺\n"
                    + "Gift Card\t\t\t 0.00 ₺\n"
                    + "Other\t\t\t 0.00 ₺\n"
                    + "Fees\t\t\t\t 0.00 ₺");
            ps.setFontFace(Font_E.Sans_Semi_Bold.ordinal());
            ps.printText("Net Total\t\t\t 14.00 ₺");

            ps.drawLine(lineThickness,10,0);
            ps.setLineSpacing(1.0f);
            ps.printText("CATEGORY SALES");
            ps.drawLine(lineThickness,10,0);
            ps.setLineSpacing(1.3f);

            ps.setFontFace(Font_E.SourceSansPro.ordinal());
            ps.printText(  "Drinks x 1\t\t\t 2.00 ₺\n"
                    + "Maels x 1\t\t\t 2.00 ₺");

            ps.setFontFace(Font_E.Sans_Semi_Bold.ordinal());
            ps.drawLine(lineThickness,10,0);
            ps.setLineSpacing(1.0f);
            ps.printText("ITEM SALES");
            ps.drawLine(lineThickness,10,0);
            ps.setLineSpacing(1.3f);

            ps.setFontFace(Font_E.SourceSansPro.ordinal());
            ps.printText("Icetea(Regular)x1\t 2.00 ₺\n"
                    + "Pizza(Regular)x1\t 2.00 ₺");
            ps.printBitmap("ykb", 0);
            ps.addSpace(bottomMargin);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    static void PrintSampleReceipt2(IPrinterService ps)
    {
        StyledString styledText = new StyledString();

        styledText.setLineSpacing(1.0f);
        styledText.setFontFace(Font_E.Sans_Semi_Bold);
        styledText.setFontSize(16);
        styledText.addTextToLine("ACME INC.", PrinterDefinitions.Alignment.Center);
        styledText.newLine(); // equivalent to styledText.addText("\n");

        styledText.setFontFace(Font_E.SourceSansPro);
        styledText.setFontSize(12);
        styledText.addTextToLine("Afiyet Olsun", Alignment.Center);
        styledText.newLine();

        styledText.addTextToLine("AT0000150552", Alignment.Center);
        styledText.newLine();

        styledText.addTextToLine("12-03-2019");
        styledText.addTextToLine("FİŞ NO: 12", Alignment.Right);
        styledText.newLine();

        styledText.addText("SAAT: 15:15\n");

        styledText.addSpace(10);

        styledText.addTextToLine("YİYECEK");
        styledText.addTextToLine("%8", Alignment.Center);
        styledText.addTextToLine("150,00 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addTextToLine("İÇECEK");
        styledText.addTextToLine("%18", Alignment.Center);
        styledText.addTextToLine("48,15 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addTextToLine("YİYECEK");
        styledText.addTextToLine("%8", Alignment.Center);
        styledText.addTextToLine("150,00 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addTextToLine("YİYECEK");
        styledText.addTextToLine("%8", Alignment.Center);
        styledText.addTextToLine("150,00 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addTextToLine("İÇECEK");
        styledText.addTextToLine("%18", Alignment.Center);
        styledText.addTextToLine("10,00 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addTextToLine("YİYECEK");
        styledText.addTextToLine("%8", Alignment.Center);
        styledText.addTextToLine("150,00 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addSpace(10);

        styledText.setFontFace(Font_E.Sans_Semi_Bold);
        styledText.setFontSize(14);
        styledText.addTextToLine("TOPKDV");
        styledText.addTextToLine("35,51 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addTextToLine("TOPLAM");
        styledText.addTextToLine("305,51 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addSpace(10);

        styledText.setFontFace(Font_E.SourceSansPro);
        styledText.setFontSize(12);
        styledText.addTextToLine("NAKİT");
        styledText.addTextToLine("305,51 ₺", Alignment.Right);
        styledText.newLine();

        styledText.addSpace(10);

        styledText.addTextToLine("EKÜ NO: 1");
        styledText.addTextToLine("Z NO: 258", Alignment.Right);
        styledText.newLine();

        styledText.addSpace(10);

        styledText.addTextToLine("uF AT 0000150258", Alignment.Center);
        styledText.newLine();


        // you can print bitmap files by
        // styledText.printBitmap("ykb");
        // styledText.printBitmap("ykb", 20)

        // you can add multiline text
        //styledText.addText("this is a multiline\ntext that can have \t\t tabs");

        styledText.addSpace(100);

        styledText.print(ps);
    }

    static void changingSizesTest(IPrinterService ps)
    {
        try
        {
            ps.setFontSize(12);
            ps.addTextToLine("MIDDLE ", Alignment.Left.ordinal());
            ps.setFontSize(8);
            ps.addTextToLine("SMALL ", Alignment.Left.ordinal());
            ps.setFontSize(16);
            ps.addTextToLine("LARGE ", Alignment.Left.ordinal());
            ps.printLine();
            ps.addSpace(bottomMargin);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static PrinterDefinitions.PrinterErrorCode checkStatus(IPrinterService ps)
    {
        try
        {
            return PrinterDefinitions.PrinterErrorCode.GetValue(ps.printerStatus());
        }
        catch (RemoteException e)
        {
            return PrinterDefinitions.PrinterErrorCode.DEAD_SERVICE;
        }
        catch (Exception e)
        {
            return PrinterDefinitions.PrinterErrorCode.DEAD_SERVICE;
        }
    }

    static void ToastStatus(Context context, PrinterDefinitions.PrinterErrorCode errCode)
    {
        String errorString;
        int textColor;
        if(errCode == PrinterDefinitions.PrinterErrorCode.NO_ERROR)
        {
            errorString = "Printer Status: Ready";
            textColor = 0xff5cb85c; // success green
        }
        else
        {
            errorString = "Printer Status: " + errCode.toString();
            textColor = 0xffd9534f; // error green
        }

        Toast toast = Toast.makeText(context, errorString, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(textColor);
        toast.show();
    }
}
