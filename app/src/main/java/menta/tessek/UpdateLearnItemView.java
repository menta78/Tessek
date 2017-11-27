package menta.tessek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class UpdateLearnItemView extends AppCompatActivity {

    AppData appData = null;
    String sheetId = "";
    String txt1 = "";
    String txt2 = "";
    String formula = "";

    public static void start(Context context, String sheetId, String txt1, String txt2, String formula, AppData appData) {
        Intent intent = new Intent(context, UpdateLearnItemView.class);
        intent.putExtra(AppData.APP_DATA, appData);
        intent.putExtra(AppData.SHEET_ID, sheetId);
        intent.putExtra(AppData.TXT1, txt1);
        intent.putExtra(AppData.TXT2, txt2);
        intent.putExtra(AppData.FORMULA, formula);
        ((Activity)context).startActivityForResult(intent, AppData.REQUEST_CODE_UPDATE_LEARN_ITEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_learn_item_view);

        Button buttonOk = (Button)findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldSheet = UpdateLearnItemView.this.sheetId;
                String oldTxt1 = UpdateLearnItemView.this.txt1;
                String oldTxt2 = UpdateLearnItemView.this.txt2;
                Spinner ss = (Spinner)findViewById(R.id.spinnerSheet);
                String newSheet = ((String)ss.getSelectedItem());
                TextView txtV1 = (TextView)findViewById(R.id.editTextUpdateText1);
                String newTxt1 = txtV1.getText().toString();
                TextView txtV2 = (TextView)findViewById(R.id.editTextUpdateText2);
                String newTxt2 = txtV2.getText().toString();
                TextView frmlV = (TextView)findViewById(R.id.editTextUpdateFormula);
                String newFrml = frmlV.getText().toString();
                appData.updateLearnItem(oldSheet, oldTxt1, oldTxt2, newSheet, newTxt1, newTxt2, newFrml);
                setResult(Activity.RESULT_OK, null);
                UpdateLearnItemView.this.finish();
            }
        });

        setTitle("Update ... ");
        populate();
    }

    private void populate(){
        Intent intent = this.getIntent();
        appData = (AppData)intent.getSerializableExtra(AppData.APP_DATA);
        sheetId = intent.getStringExtra(AppData.SHEET_ID);
        txt1 = intent.getStringExtra(AppData.TXT1);
        txt2 = intent.getStringExtra(AppData.TXT2);
        formula = intent.getStringExtra(AppData.FORMULA);

        ArrayList<String> sheets = appData.getSheetsList();
        ArrayAdapter<String> sheetsAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_sheets_view,sheets);
        Spinner sp = (Spinner)findViewById(R.id.spinnerSheet);
        sp.setAdapter(sheetsAdapter);
        int selIndex = sheets.indexOf(sheetId);
        sp.setSelection(selIndex);

        EditText eTxt1 = (EditText)findViewById(R.id.editTextUpdateText1);
        eTxt1.setText(txt1);
        EditText eTxt2 = (EditText)findViewById(R.id.editTextUpdateText2);
        eTxt2.setText(txt2);
        EditText eFrml = (EditText)findViewById(R.id.editTextUpdateFormula);
        eFrml.setText(formula);
    }

}
