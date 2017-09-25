package menta.tessek; /**
 * Created by lmentaschi on 25/09/17.
 */

import android.database.sqlite.SQLiteDatabase;

public class SqlConnectionManager {

    SQLiteDatabase db;

    public SqlConnectionManager(String dbFilePath){
        db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

}
