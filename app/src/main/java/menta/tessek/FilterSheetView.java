package menta.tessek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FilterSheetView extends AppCompatActivity {

    String sheetId;

    public static void start(Context context, String sheetId, String filterPattern) {
        Intent intent = new Intent(context, FilterSheetView.class);
        intent.putExtra(AppData.SHEET_ID, sheetId);
        intent.putExtra(AppData.FILTER_PATTERN, filterPattern);
        ((Activity)context).startActivityForResult(intent, AppData.REQUEST_CODE_FILTER_SHEET);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_sheet_view);

        Intent intent = getIntent();
        sheetId = intent.getStringExtra(AppData.SHEET_ID);
        String filterPattern = intent.getStringExtra(AppData.FILTER_PATTERN);

        setTitle("Filter " + sheetId);
        EditText editTextFilter = (EditText)findViewById(R.id.editTextFilter);
        editTextFilter.setText(filterPattern);
        Button buttonOk = (Button)findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextFilter = (EditText) findViewById(R.id.editTextFilter);
                String filterPattern = editTextFilter.getText().toString();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(AppData.FILTER_PATTERN, filterPattern);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
