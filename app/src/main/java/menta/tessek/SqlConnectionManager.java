package menta.tessek; /**
 * Created by lmentaschi on 25/09/17.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.io.Serializable;

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

}
