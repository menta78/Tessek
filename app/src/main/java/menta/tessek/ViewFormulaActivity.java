package menta.tessek;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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

    float mTextSize = 16;

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
        setContentView(R.layout.activity_view_formula);
        AjLatexMath.init(this);

        Intent intent = this.getIntent();
        sheetId = intent.getStringExtra(AppData.SHEET_ID);
        txt1 = intent.getStringExtra(AppData.TXT1);
        txt2 = intent.getStringExtra(AppData.TXT2);
        formula = intent.getStringExtra(AppData.FORMULA);
        appData = (AppData)intent.getSerializableExtra(AppData.APP_DATA);

        TextView tvTxt1 = (TextView)findViewById(R.id.viewFormula_textViewTxt1);
        tvTxt1.setText(txt1);

        ImageView ivFrml = (ImageView)findViewById(R.id.viewFormula_imageViewFormula);
        ViewTreeObserver vto = ivFrml.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw(){
                setFormula();
                return true;
            }
        });

    }

    private Bitmap scaleBitmapAndKeepRation(Bitmap targetBmp,
                                           int reqHeightInPixels, int reqWidthInPixels) {
        Bitmap bitmap = Bitmap.createBitmap(reqWidthInPixels,
                reqHeightInPixels, Bitmap.Config.ARGB_8888);
        Canvas g = new Canvas(bitmap);
        g.drawBitmap(targetBmp, 0, 0, null);
        targetBmp.recycle();
        return bitmap;
    }

    private void setFormula(){
        ImageView ivFrml = (ImageView)findViewById(R.id.viewFormula_imageViewFormula);
        int h = ivFrml.getMeasuredHeight();
        int w = ivFrml.getMeasuredWidth();

        TeXFormula teXFormula = new TeXFormula(formula);
        TeXIcon icon = teXFormula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(mTextSize)
                .setWidth(TeXConstants.UNIT_PIXEL, w, TeXConstants.ALIGN_LEFT)
                .setIsMaxWidth(true)
                .setInterLineSpacing(TeXConstants.UNIT_PIXEL,
                        AjLatexMath.getLeading(mTextSize)).build();

        icon.setInsets(new Insets(5, 5, 5, 5));

        Bitmap image = Bitmap.createBitmap(icon.getIconWidth(), icon.getIconHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas g2 = new Canvas(image);
        g2.drawColor(Color.WHITE);
        icon.paintIcon(g2, 0, 0);

        Bitmap scaleimage = scaleBitmapAndKeepRation(image, h, w);
        ivFrml.setImageBitmap(scaleimage);
    }

}
