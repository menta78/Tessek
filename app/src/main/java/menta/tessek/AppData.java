package menta.tessek;

import android.database.Cursor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lmentaschi on 25/09/17.
 */

public class AppData implements Serializable {

    public static final String APP_DATA = "app_data";
    public static final String SHEET_ID = "sheet_id";
    public static final String SETTING_DBPATH = "dbpath";
    public static final String TXT1 = "txt1";
    public static final String TXT2 = "txt2";
    public static final String FORMULA = "formula";
    public static final String FILTER_PATTERN = "filterPattern";
    public static final int REQUEST_CODE_SET_DBPATH = 1000;
    public static final int REQUEST_CODE_ADD_LEARN_ITEM = 1001;
    public static final int REQUEST_CODE_UPDATE_LEARN_ITEM = 1002;
    public static final int REQUEST_CODE_FILTER_SHEET = 1003;

    SqlConnectionManager sqlConnectionManager;

    public void setup(String dbFilePath){
        this.sqlConnectionManager = new SqlConnectionManager(dbFilePath);
    }

    public ArrayList<String> getSheetsList(){
        ArrayList<String> l = new ArrayList<>();
        Cursor crs = this.sqlConnectionManager.getSheetsQuery();
        if (crs.moveToFirst()) {
            do {
                l.add(crs.getString(0));
            } while (crs.moveToNext());
        }
        crs.close();
        return l;
    }

    public void generateNewDb(){
        sqlConnectionManager.generateNewDb();
    }

    public ArrayList<String> getOneSheetList(String sheetId, String filterPattern){
        ArrayList<String> l = new ArrayList();

        Cursor crs = sqlConnectionManager.getOneSheetQuery(sheetId, filterPattern);

        if (crs.moveToFirst()) {
            do {
                l.add(crs.getString(1));
                l.add(crs.getString(2));
            } while (crs.moveToNext());
            crs.close();
        }

        return l;
    }

    public void insertSheet(String newSheetId){
        sqlConnectionManager.insertSheet(newSheetId);
    }

    public void insertLearnItem(String sheetId, String txt1, String txt2, String formula){
        sqlConnectionManager.insertLearnItem(sheetId, txt1, txt2, formula);
    }

    public void deleteLearnItem(String sheetId, String txt1, String txt2){
        sqlConnectionManager.deleteLearnItem(sheetId, txt1, txt2);
    }

    public void updateLearnItem(String oldSheetId, String oldTxt1, String oldTxt2,
                                String newSheetId, String newTxt1, String newTxt2, String newFormula){
        sqlConnectionManager.updateLearnItem(oldSheetId, oldTxt1, oldTxt2,
                newSheetId, newTxt1, newTxt2, newFormula);
    }

    public String getFormula(String sheetId, String txt1, String txt2){
        return sqlConnectionManager.getFormula(sheetId, txt1, txt2);
    }

    public String[] getSentences(String sheetId, String aSentenceText) {
        return sqlConnectionManager.getSentences(sheetId, aSentenceText);
    }

}
