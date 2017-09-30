package menta.tessek;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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

        File dbFile = new File(getExternalFilesDir(null), "tessek.db");
        String dbFilePath = dbFile.getAbsolutePath();

        appData = new AppData();
        appData.setup(dbFilePath);

        ArrayList<String> sheets = appData.getSheetsList();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,sheets);
        GridView gvSheets = (GridView)findViewById(R.id.gridViewSheets);
        gvSheets.setAdapter(dataAdapter);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
