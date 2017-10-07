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
    public static final int REQUEST_CODE_SET_DBPATH = 1000;
    public static final int REQUEST_CODE_ADD_LEARN_ITEM = 1001;
    public static final int REQUEST_CODE_UPDATE_LEARN_ITEM = 1002;

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

    public ArrayList<String> getOneSheetList(String sheetId){
        ArrayList<String> l = new ArrayList();

        Cursor crs = sqlConnectionManager.getOneSheetQuery(sheetId);

        if (crs.moveToFirst()) {
            do {
                l.add(crs.getString(2));
                l.add(crs.getString(3));
            } while (crs.moveToNext());
            crs.close();
        }

        return l;
    }

    public void insertLearnItem(String sheetId, String txt1, String txt2){
        sqlConnectionManager.insertLearnItem(sheetId, txt1, txt2);
    }

    public void deleteLearnItem(String sheetId, String txt1, String txt2){
        sqlConnectionManager.deleteLearnItem(sheetId, txt1, txt2);
    }

    public void updateLearnItem(String oldSheetId, String oldTxt1, String oldTxt2,
                                String newSheetId, String newTxt1, String newTxt2){
        sqlConnectionManager.updateLearnItem(oldSheetId, oldTxt1, oldTxt2,
                newSheetId, newTxt1, newTxt2);
    }

}
