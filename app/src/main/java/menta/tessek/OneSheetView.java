package menta.tessek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class OneSheetView extends AppCompatActivity {

    private static final String SHEET_ID = "sheet_id";
    private static final String APP_DATA = "app_data";
    AppData appData = null;
    String sheetId = "";

    public static void start(Context context, String sheetId, AppData appData) {
        Intent intent = new Intent(context, OneSheetView.class);
        intent.putExtra(SHEET_ID, sheetId);
        intent.putExtra(APP_DATA, appData);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_sheet_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = this.getIntent();
        sheetId = intent.getStringExtra(SHEET_ID);
        appData = (AppData)intent.getSerializableExtra(APP_DATA);

        setTitle(sheetId);
        refreshViewNoFilter();
    }

    private void refreshViewNoFilter(){
        ArrayList<String> sheet = appData.getOneSheetList(sheetId);
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,sheet);
        GridView gvOneSheet = (GridView)findViewById(R.id.gridViewOneSheet);
        gvOneSheet.setAdapter(dataAdapter);
    }

}
