package menta.tessek;

/**
 * Created by lmentaschi on 25/09/17.
 */

public class AppData {

    SqlConnectionManager sqlConnectionManager;

    public void setup(String dbFilePath){
        this.sqlConnectionManager = new SqlConnectionManager(dbFilePath);
    }

}
