package menta.tessek; /**
 * Created by lmentaschi on 25/09/17.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class SqlConnectionManager implements Serializable {

    String dbFilePath = "";

    public SqlConnectionManager(String dbFilePath){
        this.dbFilePath = dbFilePath;
    }

    public SQLiteDatabase getDb(){
        return SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public Cursor getSheetsQuery(){
        SQLiteDatabase db = getDb();
        Cursor cr=db.rawQuery("SELECT * FROM sheet_headers",null);
        return cr;
    }

    public Cursor getOneSheetQuery(String sheetId){
        SQLiteDatabase db = getDb();
        String queryTxt = "SELECT * FROM sheet_data WHERE sheet_id = ? ORDER BY rowid DESC";
        Cursor cr = db.rawQuery(queryTxt, new String [] {sheetId});
        return cr;
    }

    public void insertLearnItem(String sheetId, String txt1, String txt2){
        int tmstmp = round(System.currentTimeMillis() / 1000);
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            String deleteQ = "DELETE FROM sheet_data WHERE " +
                    "sheet_id=? AND sentence1=? AND sentence2=?";
            db.execSQL(deleteQ, new String[]{sheetId, txt1, txt2});
            String insertQ = "INSERT INTO sheet_data " +
                    "(sheet_id, sentence1, sentence2, mod_date, learnt, image) " +
                    "VALUES (?,?,?,?, 0, '')";
            db.execSQL(insertQ, new String[]{sheetId, txt1, txt2, String.valueOf(tmstmp)});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        db.close();
    }

}
