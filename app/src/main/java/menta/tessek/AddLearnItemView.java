package menta.tessek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddLearnItemView extends AppCompatActivity {

    AppData appData = null;
    String sheetId = "";

    public static void start(Context context, String sheetId, AppData appData) {
        Intent intent = new Intent(context, AddLearnItemView.class);
        intent.putExtra(AppData.SHEET_ID, sheetId);
        intent.putExtra(AppData.APP_DATA, appData);
        ((Activity)context).startActivityForResult(intent, AppData.REQUEST_CODE_ADD_LEARN_ITEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_learn_item_view);
        Button buttonOk = (Button)findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtV1 = (TextView)findViewById(R.id.addLItem_EditText1);
                String txt1 = txtV1.getText().toString();
                TextView txtV2 = (TextView)findViewById(R.id.addLItem_EditText2);
                String txt2 = txtV2.getText().toString();
                appData.insertLearnItem(sheetId, txt1, txt2);
                AddLearnItemView.this.finish();
            }
        });

        Intent intent = this.getIntent();
        sheetId = intent.getStringExtra(AppData.SHEET_ID);
        appData = (AppData)intent.getSerializableExtra(AppData.APP_DATA);

        setTitle("Add to " + sheetId);
    }
}
