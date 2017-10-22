package menta.tessek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(SheetsView.this).create();
                alertDialog.setTitle("New sheet");
                alertDialog.setMessage("Inert the name of the new sheet");
                final EditText input = new EditText(SheetsView.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String newSheetId = input.getText().toString();
                                appData.insertSheet(newSheetId);
                                refreshView();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

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
        refreshView();
    }

    private void refreshView(){
        ArrayList<String> sheets = appData.getSheetsList();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.ts_text_view,sheets);
        GridView gvSheets = (GridView)findViewById(R.id.gridViewSheets);
        gvSheets.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sheets_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
            startActivityForResult(intent, AppData.REQUEST_CODE_SET_DBPATH);
            return true;
        }
        else if (id == R.id.action_info) {
            AlertDialog alertDialog = new AlertDialog.Builder(SheetsView.this).create();
            alertDialog.setTitle("");
            alertDialog.setMessage("menta.tessek v 0.01");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", (DialogInterface.OnClickListener)null);
            alertDialog.show();
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
            if (!(new File(dbPth)).exists()){
                dbPth = System.getenv("SECONDARY_STORAGE") + "/" + pth[1];
            }

            try {
                doRefreshContent(dbPth);
                SharedPreferences settings = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor e = settings.edit();
                e.putString(AppData.SETTING_DBPATH, dbPth);
                e.commit();
            } catch (Exception exc) {
                //resetting previous situation
                AlertDialog alertDialog = new AlertDialog.Builder(SheetsView.this).create();
                alertDialog.setTitle("error setting new db");
                alertDialog.setMessage("file " + dbPth + " cannot be accessed.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", (DialogInterface.OnClickListener)null);
                alertDialog.show();

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
