package com.tokeninc.sardis.application_template.Helpers.PrintHelpers;

import com.token.printerlib.PrinterDefinitions;
import com.token.printerlib.PrinterService;
import com.token.printerlib.StyledString;

public class PrintHelper extends BasePrintHelper {

    public static void PrintSuccess()
    {   // Print the success message
        StyledString styledText = new StyledString();

        // Strings must be max 29 digits
        addTextToNewLine(styledText, "Banka uygulaması", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "başarıyla kurulmuştur", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "kullanımı için", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "https://www.tokeninc.com/", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "adresini ziyaret edin", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "-----------------------------", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Banka uygulaması sorularınız", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "için, Banka Pos", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Destek Hattı 0850 000 0 000", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "-----------------------------", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "YazarkasaPos sorularınız için", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Beko YazarkasaPos", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Çözüm Merkezi", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "0850 250 0 767", PrinterDefinitions.Alignment.Center);

        addTextToNewLine(styledText, DateUtil.getDate("dd-MM-yy"), PrinterDefinitions.Alignment.Left);
        addText(styledText, DateUtil.getTime("HH:mm"), PrinterDefinitions.Alignment.Right);

        styledText.newLine();
        styledText.addSpace(100);

        styledText.print(PrinterService.getService());
    }

    public static void PrintError()
    {   // Print the error message if necessary
        StyledString styledText = new StyledString();

        addTextToNewLine(styledText, "Uygulama kurulumunda hata", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "ile karşılaşılmıştır", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Lütfen Beko YazarkasaPos", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Çözüm Merkezi'ni arayın", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "0850 250 0 767", PrinterDefinitions.Alignment.Center);

        addTextToNewLine(styledText, DateUtil.getDate("dd-MM-yy"), PrinterDefinitions.Alignment.Left);
        addText(styledText, DateUtil.getTime("HH:mm"), PrinterDefinitions.Alignment.Right);

        styledText.newLine();
        styledText.addSpace(100);

        styledText.print(PrinterService.getService());
    }

    public static void PrintBatchClose(String batch_no, String tx_no, String MID, String TID)
    {
        StyledString styledText = new StyledString();

        addTextToNewLine(styledText, "TOKEN", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "FINTECH", PrinterDefinitions.Alignment.Center);

        addTextToNewLine(styledText, "İŞYERİ NO: ", PrinterDefinitions.Alignment.Left);
        addText(styledText, MID, PrinterDefinitions.Alignment.Right);

        addTextToNewLine(styledText, "TERMİNAL NO: ", PrinterDefinitions.Alignment.Left);
        addText(styledText, TID, PrinterDefinitions.Alignment.Right);

        addTextToNewLine(styledText, " ", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, "Grup Kapama Başarılı", PrinterDefinitions.Alignment.Center);
        addTextToNewLine(styledText, " ", PrinterDefinitions.Alignment.Center);

        if (batch_no.equals("")){
            addTextToNewLine(styledText, "Grup Yok", PrinterDefinitions.Alignment.Center);
        }
        else{
            addTextToNewLine(styledText, "Grup No: ", PrinterDefinitions.Alignment.Left);
            addText(styledText, batch_no, PrinterDefinitions.Alignment.Right);
        }
        if (tx_no.equals("") || tx_no.equals("null") || tx_no.equals("0")){
            addTextToNewLine(styledText, "İşlem Yok", PrinterDefinitions.Alignment.Center);
        }
        else{
            addTextToNewLine(styledText, "İşlem Sayısı: ", PrinterDefinitions.Alignment.Left);
            addText(styledText, tx_no, PrinterDefinitions.Alignment.Right);
        }

        addTextToNewLine(styledText, DateUtil.getDate("dd-MM-yy"), PrinterDefinitions.Alignment.Left);
        addText(styledText, DateUtil.getTime("HH:mm"), PrinterDefinitions.Alignment.Right);

        styledText.newLine();
        styledText.addSpace(100);

        styledText.print(PrinterService.getService());
    }

    public static void PrintContactless(boolean is32)
    {
        StyledString styledText = new StyledString();
        styledText.printBitmap(is32 ? "contactless32" : "contactless64", 0);
        styledText.newLine();
        styledText.addSpace(100);
        styledText.print(PrinterService.getService());
    }

    public static void PrintVisa()
    {
        StyledString styledText = new StyledString();
        styledText.printBitmap("visa-contactless", 0);
        styledText.newLine();
        styledText.addSpace(100);
        styledText.print(PrinterService.getService());
    }
}
