package com.tokeninc.sardis.application_template.Helpers.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.DateUtil;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.PrintServiceBinding;

import java.util.ArrayList;
import java.util.List;

import static com.tokeninc.sardis.application_template.Helpers.PrintHelpers.PrintHelper.PrintBatchClose;
import static java.lang.String.format;

public class DatabaseHelper extends SQLiteOpenHelper {

    public PrintServiceBinding printService;

    public static String DATABASE = "database.db";

    public static String TABLE ="sale_table";
    public static String CARD_NO ="card_no";
    public static String AMOUNT ="sale_amount";
    public static String PROCESS_TIME ="process_time";
    public static String APPROVAL_CODE ="approval_code";
    public static String SERIAL_NO ="serial_no";

    public static String ACT_TABLE ="act_table";
    public static String MERCHANT_ID ="merchant_id";
    public static String TERMINAL_ID ="terminal_id";

    public static String BATCH_TABLE ="batch_table";
    public static String BATCH_NO = "batch_no";

    public static String Tx_TABLE ="tx_table";
    public static String TxNO = "tx_no";

    public static String SALE_COUNTER ="sale_counter";
    public static String SALE_NO = "sale_no";

    String sale_db; String act_db; String batch_table;
    String tx_table, merchant_id_element, sale_counter_table;

    String process_time, approval_code, serial_no, batch_no;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        sale_db = "CREATE TABLE "+TABLE+"("+CARD_NO + " Text, "+AMOUNT + " Text, "+PROCESS_TIME + " Text, "+APPROVAL_CODE+ " Text, "+SERIAL_NO+ " Text, "+BATCH_NO+ " Text);";
        db.execSQL(sale_db);

        act_db = "CREATE TABLE "+ACT_TABLE+"("+MERCHANT_ID + " Text, "+TERMINAL_ID + " Text);";
        db.execSQL(act_db);

        batch_table = "CREATE TABLE "+BATCH_TABLE+"("+BATCH_NO + " Text);";
        db.execSQL(batch_table);

        tx_table = "CREATE TABLE "+Tx_TABLE+"("+TxNO + " Text);";
        db.execSQL(tx_table);

        sale_counter_table = "CREATE TABLE "+SALE_COUNTER+"("+SALE_NO + " Text);";
        db.execSQL(sale_counter_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE+" ;");
    }

    public void insertSaleData(String card_no, String sale_amount, String process_time , String approval_code, String serial_no, String batch_no){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();

            contentValues.put(CARD_NO, card_no);
            contentValues.put(AMOUNT,sale_amount);
            contentValues.put(PROCESS_TIME, process_time);
            contentValues.put(APPROVAL_CODE,approval_code);
            contentValues.put(SERIAL_NO,serial_no);
            contentValues.put(BATCH_NO,batch_no);
            db.insert(TABLE,null,contentValues);
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertDataTidMid(String merchant_id, String terminal_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();

            contentValues.put(MERCHANT_ID, merchant_id);
            contentValues.put(TERMINAL_ID,terminal_id);
            db.insert(ACT_TABLE,null,contentValues);

            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void dropDataTidMid(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS act_table");
            act_db = "CREATE TABLE "+ACT_TABLE+"("+MERCHANT_ID + " Text, "+TERMINAL_ID + " Text);";
            db.execSQL(act_db);
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String readDataTidMid(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(false, "act_table", new String[] {"merchant_id"}, null, null, null, null, "merchant_id", "1");
         merchant_id_element = c.moveToFirst() ? c.getString(0) : "";
        c.close();

        Cursor c2 = db.query(false, "act_table", new String[] {"merchant_id"}, null, null, null, null, "merchant_id", "1");
        String terminal_id_element = c2.moveToFirst() ? c2.getString(0) : "";
        c2.close();

        db.close();

        return terminal_id_element;
    }

    public void insertBatch(String batch_no){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();

            contentValues.put(BATCH_NO, batch_no);
            db.insert(BATCH_TABLE,null,contentValues);
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertTx(String tx_no){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();

            contentValues.put(TxNO,tx_no);
            db.insert(Tx_TABLE,null,contentValues);
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertSaleCount(String sale_no){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();

            contentValues.put(SALE_NO,sale_no);
            db.insert(SALE_COUNTER,null,contentValues);
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<com.tokeninc.sardis.application_template.Helpers.DataBase.DataModel> getData(){
        List<com.tokeninc.sardis.application_template.Helpers.DataBase.DataModel> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();
        com.tokeninc.sardis.application_template.Helpers.DataBase.DataModel dataModel = null;
        while (cursor.moveToNext()) {
            dataModel= new com.tokeninc.sardis.application_template.Helpers.DataBase.DataModel();
            String card_no = cursor.getString(cursor.getColumnIndexOrThrow("card_no"));
            String sale_amount = cursor.getString(cursor.getColumnIndexOrThrow("sale_amount"));
            String process_time = cursor.getString(cursor.getColumnIndexOrThrow("process_time"));
            String approval_code = cursor.getString(cursor.getColumnIndexOrThrow("approval_code"));
            String serial_no = cursor.getString(cursor.getColumnIndexOrThrow("serial_no"));
            dataModel.setCard_no(card_no);
            dataModel.setSale_amount(sale_amount);
            dataModel.setProcess_time(process_time);
            dataModel.setApproval_code(approval_code);
            dataModel.setSerial_no(serial_no);
            stringBuffer.append(dataModel);
            data.add(dataModel);
        }
        return data;
    }

    public void batchClose(){
        printService = new PrintServiceBinding();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS sale_table");
        String  sale_db = "CREATE TABLE "+TABLE+"("+CARD_NO + " Text, "+AMOUNT + " Text, "+PROCESS_TIME + " Text, "+APPROVAL_CODE+ " Text, "+SERIAL_NO+ " Text, "+BATCH_NO+ " Text);";
        db.execSQL(sale_db);

        Cursor c = db.query(false, "batch_table", new String[] {"batch_no"}, null, null, null, null, "batch_no", "1");
        String batch_element_one = c.moveToFirst() ? c.getString(0) : "";
        c.close();

        Cursor c2 = db.query(false, "tx_table", new String[] {"tx_no"}, null, null, null, null, "tx_no", "1");
        String tx_element_one = c2.moveToFirst() ? c2.getString(0) : "";
        c2.close();

        if (batch_element_one == ""){
            insertBatch("1");
        }
        else {
            int batch_element_one_int = 0;

            try {
                batch_element_one_int = Integer.parseInt(batch_element_one);
            } catch(NumberFormatException nfe) {
                Log.i("Could not parse ", "ERR"+ nfe);
            }

            if (batch_element_one_int >= 1){
                db.execSQL("DROP TABLE IF EXISTS batch_table");
                db.execSQL("CREATE TABLE IF NOT EXISTS batch_table (batch_no Text);");

                db.execSQL("DROP TABLE IF EXISTS tx_table");
                db.execSQL("CREATE TABLE IF NOT EXISTS tx_table (tx_no Text);");

                batch_element_one_int ++;
                String batch_element_one_string_plus = String.valueOf(batch_element_one_int);
                insertBatch(batch_element_one_string_plus);
            }
        }
        printService.print(PrintBatchClose(batch_element_one, tx_element_one));
        db.close();
    }

    public void getSaleData(String myCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sale_table WHERE approval_code = " + myCode, null);

        if (cursor.moveToFirst()) {

        String card_no = cursor.getString(cursor.getColumnIndex("card_no"));
        String sale_amount = cursor.getString(cursor.getColumnIndexOrThrow("sale_amount"));
        String process_time = cursor.getString(cursor.getColumnIndexOrThrow("process_time"));
        String approval_code = cursor.getString(cursor.getColumnIndexOrThrow("approval_code"));
        String serial_no = cursor.getString(cursor.getColumnIndexOrThrow("serial_no"));
        String batch_no = cursor.getString(cursor.getColumnIndexOrThrow("batch_no"));

        cursor.close();
        }

        db.close();
    }

    public void SaveSaleToDB(String card_no, String sale_amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(false, "batch_table", new String[] {"batch_no"}, null, null, null, null, "batch_no", "1");
        String batch_element_one = c.moveToFirst() ? c.getString(0) : "";
        c.close();

        Cursor c2 = db.query(false, "tx_table", new String[] {"tx_no"}, null, null, null, null, "tx_no", "1");
        String tx_element_one = c2.moveToFirst() ? c2.getString(0) : "";
        c2.close();

        Cursor c3 = db.query(false, "sale_counter", new String[] {"sale_no"}, null, null, null, null, "sale_no", "1");
        String sale_element_one = c3.moveToFirst() ? c3.getString(0) : "";
        c3.close();

        // Create a Sale Counter, Auto increment it, write to DB as approval_code
        if (sale_element_one.equals("")){
            insertSaleCount("1");
            approval_code = "1";
        }
        else {
            int sale_element_one_int = 0;

            try {
                sale_element_one_int = Integer.parseInt(sale_element_one);
            } catch (NumberFormatException nfe) {
                Log.i("Could not parse ", "ERR"+ nfe);
            }
            if (sale_element_one_int >= 1) {
                sale_element_one_int++;
                String sale_element_one_string_plus = String.valueOf(sale_element_one_int);
                db.execSQL("DROP TABLE IF EXISTS sale_counter");
                db.execSQL("CREATE TABLE IF NOT EXISTS sale_counter (sale_no Text);");
                insertSaleCount(sale_element_one_string_plus);
                approval_code = sale_element_one_string_plus;
            }
        }

        // Transaction Counter, Transaction Counter resets when batch is closed
        if (tx_element_one.equals("")){
            insertTx("1");
        }
        else {
            int tx_element_one_int = 0;

            try {
                tx_element_one_int = Integer.parseInt(tx_element_one);
            } catch (NumberFormatException nfe) {
                Log.i("Could not parse ", "ERR"+ nfe);
            }

            if (tx_element_one_int >= 1) {
                tx_element_one_int++;
                String tx_element_one_string_plus = String.valueOf(tx_element_one_int);
                db.execSQL("DROP TABLE IF EXISTS tx_table");
                db.execSQL("CREATE TABLE IF NOT EXISTS tx_table (tx_no Text);");
                insertTx(tx_element_one_string_plus);
            }
        }

        // Creating a unique Serial Number with Batch and Transaction counters
        if (batch_element_one.equals("") && tx_element_one.equals("")){
            serial_no ="1-1";
        }
        if (batch_element_one.equals("") && !tx_element_one.equals("") ){
            int tx_element_one_int = 0;
            try {
                tx_element_one_int = Integer.parseInt(tx_element_one);
            } catch (NumberFormatException nfe) {
                Log.i("Could not parse ", "ERR"+ nfe);
            }
            tx_element_one_int++;
            String tx_element_one_string_plus = String.valueOf(tx_element_one_int);
            serial_no = "1" + "-" + tx_element_one_string_plus;
        }
        if (!batch_element_one.equals("") && tx_element_one.equals("")){
            serial_no = batch_element_one + "-" + "1";
        }
        if (!batch_element_one.equals("") && !tx_element_one.equals("")){
            int tx_element_one_int = 0;
            try {
                tx_element_one_int = Integer.parseInt(tx_element_one);
            } catch (NumberFormatException nfe) {
                Log.i("Could not parse ", "ERR"+ nfe);
            }
            tx_element_one_int++;
            String tx_element_one_string_plus = String.valueOf(tx_element_one_int);

            serial_no = batch_element_one + "-" + tx_element_one_string_plus;
        }

        // If this sale is for the empty batch(first load of the app) Batch Number starts from "1"
        if (batch_element_one.equals("")){
            insertBatch("1");
            batch_no = "1";
        }
        else{
            batch_no = batch_element_one;
        }

        // WRITE SALE DATA TO DATABASE
        process_time = DateUtil.getDate("dd-MM-yy");
        insertSaleData(card_no, sale_amount, process_time,  approval_code, serial_no, batch_no);
        db.close();
    }
}
