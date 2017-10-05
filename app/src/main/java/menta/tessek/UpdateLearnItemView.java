package menta.tessek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UpdateLearnItemView extends AppCompatActivity {

    AppData appData = null;
    String sheetId = "";
    String txt1 = "";
    String txt2 = "";

    public static void start(Context context, String sheetId, String txt1, String txt2, AppData appData) {
        Intent intent = new Intent(context, UpdateLearnItemView.class);
        intent.putExtra(AppData.APP_DATA, appData);
        intent.putExtra(AppData.SHEET_ID, sheetId);
        intent.putExtra(AppData.TXT1, txt1);
        intent.putExtra(AppData.TXT2, txt2);
        ((Activity)context).startActivityForResult(intent, AppData.REQUEST_CODE_UPDATE_LEARN_ITEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_learn_item_view);
    }
}
