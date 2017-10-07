package menta.tessek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class SheetsView extends AppCompatActivity {

    AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        GridView gvSheets = (GridView)findViewById(R.id.gridViewSheets);
        gvSheets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final String selectedSheet = ((TextView)v).getText().toString();
                AlertDialog alertDialog = new AlertDialog.Builder(SheetsView.this).create();
                alertDialog.setTitle("");
                alertDialog.setMessage("View sheet " + selectedSheet + "?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                OneSheetView.start(SheetsView.this, selectedSheet, appData);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        File dbFile = new File(getExternalFilesDir(null), "tessek.db");
        String dbDefaultFilePath = dbFile.getAbsolutePath();

        try {
            SharedPreferences settings = getPreferences(MODE_PRIVATE);
            String dbFilePath = settings.getString(AppData.SETTING_DBPATH, dbDefaultFilePath);
            doRefreshContent(dbFilePath);
        } catch (Exception e) {
            String dbFilePath = dbDefaultFilePath;
            doRefreshContent(dbFilePath);
        }
    }

    private void doRefreshContent(String dbFilePath){
        appData = new AppData();
        appData.setup(dbFilePath);
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()){
            appData.generateNewDb();
        }
        appData.generateNewDb();

        ArrayList<String> sheets = appData.getSheetsList();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,sheets);
        GridView gvSheets = (GridView)findViewById(R.id.gridViewSheets);
        gvSheets.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sheets_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //SharedPreferences settings = getPreferences(MODE_PRIVATE);
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
            startActivityForResult(intent, AppData.REQUEST_CODE_SET_DBPATH);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == AppData.REQUEST_CODE_SET_DBPATH
                && resultCode == Activity.RESULT_OK
                && data != null){
            String oldPth = appData.sqlConnectionManager.dbFilePath;
            Uri uri = data.getData();
            String[] pth = uri.getPath().split(":");
            String dbPth = Environment.getExternalStorageDirectory() + "/" + pth[1];

            try {
                doRefreshContent(dbPth);
                SharedPreferences settings = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor e = settings.edit();
                e.putString(AppData.SETTING_DBPATH, dbPth);
                e.commit();
            } catch (Exception exc) {
                //resetting previous situation
                dbPth = oldPth;
                doRefreshContent(dbPth);
                SharedPreferences settings = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor e = settings.edit();
                e.putString(AppData.SETTING_DBPATH, dbPth);
                e.commit();
            }
        }
    }


}
