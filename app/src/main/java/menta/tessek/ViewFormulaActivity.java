package menta.tessek;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import maximsblog.blogspot.com.jlatexmath.core.Insets;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;
import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;

public class ViewFormulaActivity extends AppCompatActivity {

    String sheetId;
    String txt1;
    String txt2;
    String formula;
    AppData appData;

    public static void start(Context context, String sheetId, String txt1, String txt2, String frml, AppData appData) {
        Intent intent = new Intent(context, ViewFormulaActivity.class);
        intent.putExtra(AppData.SHEET_ID, sheetId);
        intent.putExtra(AppData.TXT1, txt1);
        intent.putExtra(AppData.TXT2, txt2);
        intent.putExtra(AppData.FORMULA, frml);
        intent.putExtra(AppData.APP_DATA, appData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        sheetId = intent.getStringExtra(AppData.SHEET_ID);
        txt1 = intent.getStringExtra(AppData.TXT1);
        txt2 = intent.getStringExtra(AppData.TXT2);
        formula = intent.getStringExtra(AppData.FORMULA);
        appData = (AppData)intent.getSerializableExtra(AppData.APP_DATA);

        setContentView(R.layout.activity_view_formula);

        TextView tvTxt1 = (TextView)findViewById(R.id.viewFormula_textViewTxt1);
        tvTxt1.setText(txt1);


    }

}
