package menta.tessek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class OneSheetView extends AppCompatActivity {

    AppData appData = null;
    String sheetId = "";
    ArrayList<String> sheetList;
    String selectedTxt1 = "";
    String selectedTxt2 = "";
    String filterPattern = "";

    Menu menu;

    public static void start(Context context, String sheetId, AppData appData) {
        Intent intent = new Intent(context, OneSheetView.class);
        intent.putExtra(AppData.SHEET_ID, sheetId);
        intent.putExtra(AppData.APP_DATA, appData);
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
                AddLearnItemView.start(OneSheetView.this,
                        OneSheetView.this.sheetId, OneSheetView.this.appData);
            }
        });

        GridView gvOneSheet = (GridView)findViewById(R.id.gridViewOneSheet);

        gvOneSheet.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                int position = info.position;
                int[] selRow = getRowIndices(position);
                OneSheetView.this.selectedTxt1 = OneSheetView.this.sheetList.get(selRow[0]);
                OneSheetView.this.selectedTxt2 = OneSheetView.this.sheetList.get(selRow[1]);

                MenuItem mi = contextMenu.add("Modify");
                mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        final String sheetId = OneSheetView.this.sheetId;
                        final String txt1 = OneSheetView.this.selectedTxt1;
                        final String txt2 = OneSheetView.this.selectedTxt2;
                        final String formula = appData.getFormula(sheetId, txt1, txt2);
                        UpdateLearnItemView.start(OneSheetView.this,
                                sheetId, txt1, txt2, formula, OneSheetView.this.appData);
                        return false;
                    }
                });

                mi = contextMenu.add("Delete");
                mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog alertDialog = new AlertDialog.Builder(OneSheetView.this).create();
                        alertDialog.setTitle("");
                        final String sheetId = OneSheetView.this.sheetId;
                        final String txt1 = OneSheetView.this.selectedTxt1;
                        final String txt2 = OneSheetView.this.selectedTxt2;
                        alertDialog.setMessage("Delete item " + txt1 + " ... ?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        OneSheetView.this.appData.deleteLearnItem(sheetId, txt1, txt2);
                                        OneSheetView.this.refreshView();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return true;
                    }
                });
            }
        });

        gvOneSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int[] selRow = getRowIndices(position);
                String txt1 = OneSheetView.this.sheetList.get(selRow[0]);
                String txt2 = OneSheetView.this.sheetList.get(selRow[1]);
                String sheetId = OneSheetView.this.sheetId;
                AppData appData = OneSheetView.this.appData;
                String frml = appData.getFormula(sheetId, txt1, txt2);
                if (frml != "") {
                    ViewFormulaActivity.start(OneSheetView.this, sheetId, txt1, txt2, frml, appData);
                }
            }
        });

        Intent intent = this.getIntent();
        sheetId = intent.getStringExtra(AppData.SHEET_ID);
        appData = (AppData)intent.getSerializableExtra(AppData.APP_DATA);

        refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one_sheet_view, menu);
        MenuItem menuItemRemoveFilter = menu.findItem(R.id.action_one_sheet_remove_filter);
        menuItemRemoveFilter.setVisible(!filterPattern.isEmpty());
        this.menu = menu;
        return true;
    }

    private int[] getRowIndices(int selectedIndex){
        int[] result = new int[2];
        if (selectedIndex % 2 == 0){
            result[0] = selectedIndex;
            result[1] = selectedIndex + 1;
        } else {
            result[0] = selectedIndex - 1;
            result[1] = selectedIndex;
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == AppData.REQUEST_CODE_FILTER_SHEET) {
                filterPattern = data.getStringExtra(AppData.FILTER_PATTERN);
            }
            refreshView();
        }
    }

    private void refreshView(){

        if (filterPattern.isEmpty()) {
            setTitle(sheetId);
        } else {
            setTitle(sheetId + " (filter " + filterPattern + ")");
        }

        if (menu != null) {
            MenuItem menuItemRemoveFilter = menu.findItem(R.id.action_one_sheet_remove_filter);
            menuItemRemoveFilter.setVisible(!filterPattern.isEmpty());
        }

        sheetList = appData.getOneSheetList(sheetId, filterPattern);
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>
                (getApplicationContext(),R.layout.ts_text_view,sheetList);
        GridView gvOneSheet = (GridView)findViewById(R.id.gridViewOneSheet);
        gvOneSheet.setAdapter(dataAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_one_sheet_filter) {
            FilterSheetView.start(OneSheetView.this, sheetId, filterPattern);
        }
        if (id == R.id.action_one_sheet_remove_filter) {
            filterPattern = "";
            refreshView();
        }

        return super.onOptionsItemSelected(item);
    }

}
