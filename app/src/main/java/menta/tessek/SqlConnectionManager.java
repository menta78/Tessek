package menta.tessek; /**
 * Created by lmentaschi on 25/09/17.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

public class SqlConnectionManager {

    SQLiteDatabase db;

    public SqlConnectionManager(String dbFilePath){
        db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public Cursor getSheetsQuery(){
        Cursor cr=db.rawQuery("SELECT * FROM sheet_headers",null);
        return cr;
    }

}
