package menta.tessek;

import android.database.Cursor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lmentaschi on 25/09/17.
 */

public class AppData implements Serializable {

    SqlConnectionManager sqlConnectionManager;

    public void setup(String dbFilePath){
        this.sqlConnectionManager = new SqlConnectionManager(dbFilePath);
    }

    public ArrayList<String> getSheetsList(){
        ArrayList<String> l = new ArrayList<>();
        Cursor crs = this.sqlConnectionManager.getSheetsQuery();
        crs.moveToFirst();
        do {
          l.add(crs.getString(0));
        } while (crs.moveToNext());
        crs.close();
        return l;
    }

}
