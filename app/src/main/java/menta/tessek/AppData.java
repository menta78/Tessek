package menta.tessek;

import android.database.Cursor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lmentaschi on 25/09/17.
 */

public class AppData implements Serializable {

    public static final String SHEET_ID = "sheet_id";
    public static final String APP_DATA = "app_data";
    public static final int REQUEST_CODE_ADD_LEARN_ITEM = 1000;

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

}
