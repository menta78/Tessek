package menta.tessek; /**
 * Created by lmentaschi on 25/09/17.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.lang.reflect.Array;
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

    public void generateNewDb(){
        SQLiteDatabase db = getDb();
        try {
            db.beginTransaction();
            try {
                String createSql = "CREATE TABLE sheet_data " +
                        "(sheet_id text, mod_date date, sentence1 text, sentence2 text, " +
                        "learnt boolean, image text, item_formula text)";
                db.execSQL(createSql);
            } catch (Exception e) {
            }

            try {
                String createSql = "CREATE TABLE sheet_headers (sheet_id text, creation_date date)";
                db.execSQL(createSql);
            } catch (Exception e) {
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    public Cursor getSheetsQuery(){
        SQLiteDatabase db = getDb();
        Cursor cr=db.rawQuery("SELECT * FROM sheet_headers",null);
        return cr;
    }

    public Cursor getOneSheetQuery(String sheetId, String filterPattern){
        SQLiteDatabase db = getDb();
        Cursor cr;
        if (filterPattern.isEmpty()) {
            String queryTxt = "SELECT sheet_id, sentence1, sentence2 FROM sheet_data WHERE sheet_id = ? ORDER BY rowid DESC";
            cr = db.rawQuery(queryTxt, new String [] {sheetId});
        } else {
            filterPattern = "%" + filterPattern + "%";
            String queryTxt = "SELECT sheet_id, sentence1, sentence2 FROM sheet_data WHERE sheet_id = ? "
                    + " AND ((sentence1 LIKE ?) OR (sentence2 LIKE ?))"
                    + " ORDER BY rowid DESC";
            cr = db.rawQuery(queryTxt, new String [] {sheetId, filterPattern, filterPattern});
        }
        return cr;
    }

    public String getFormula(String sheetId, String txt1, String txt2){
        SQLiteDatabase db = getDb();
        String queryTxt = "SELECT item_formula FROM sheet_data WHERE " +
                          "sheet_id = ? and sentence1 = ? and sentence2 = ?";
        Cursor cr = db.rawQuery(queryTxt, new String [] {sheetId, txt1, txt2});
        if (cr.moveToFirst()) {
            return cr.getString(0);
        } else {
            return "";
        }
    }

    public void insertSheet(String newSheetId){
        int tmstmp = round(System.currentTimeMillis() / 1000);
        String insertQ = "INSERT INTO sheet_headers (sheet_id, creation_date) VALUES (?, ?)";
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            db.execSQL(insertQ, new String[]{newSheetId, String.valueOf(tmstmp)});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertLearnItem(String sheetId, String txt1, String txt2, String formula){
        int tmstmp = round(System.currentTimeMillis() / 1000);
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            String deleteQ = "DELETE FROM sheet_data WHERE " +
                    "sheet_id=? AND sentence1=? AND sentence2=?";
            db.execSQL(deleteQ, new String[]{sheetId, txt1, txt2});
            String insertQ = "INSERT INTO sheet_data " +
                    "(sheet_id, sentence1, sentence2, mod_date, learnt, image, item_formula) " +
                    "VALUES (?,?,?,?,0,'',?)";
            db.execSQL(insertQ, new String[]{sheetId, txt1, txt2, String.valueOf(tmstmp), formula});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public void deleteLearnItem(String sheetId, String txt1, String txt2){
        int tmstmp = round(System.currentTimeMillis() / 1000);
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            String deleteQ = "DELETE FROM sheet_data WHERE " +
                    "sheet_id=? AND sentence1=? AND sentence2=?";
            db.execSQL(deleteQ, new String[]{sheetId, txt1, txt2});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateLearnItem(String oldSheetId, String oldTxt1, String oldTxt2,
                                String newSheetId, String newTxt1, String newTxt2, String newFormula){
        int tmstmp = round(System.currentTimeMillis() / 1000);
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            String updateQ = "UPDATE sheet_data SET " +
                    "sheet_id=?, sentence1=?, sentence2=?, item_formula=?, mod_date=? WHERE " +
                    "sheet_id=? AND sentence1=? AND sentence2=?";
            db.execSQL(updateQ, new String[]{newSheetId, newTxt1, newTxt2, newFormula,
                    String.valueOf(tmstmp), oldSheetId, oldTxt1, oldTxt2});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public String[] getSentences(String sheetId, String aSentenceText){
        SQLiteDatabase db = getDb();
        String queryTxt = "SELECT sentence1, sentence2 FROM sheet_data WHERE " +
                "sheet_id = ? AND (sentence1 = ?) OR (sentence2 = ?)";
        Cursor cr = db.rawQuery(queryTxt, new String [] {sheetId, aSentenceText, aSentenceText});
        if (cr.moveToFirst()) {
            String sentence1 = cr.getString(0);
            String sentence2 = cr.getString(1);
            return new String[] {sentence1, sentence2};
        } else {
            return new String[] {"", ""};
        }
    }

}
