package menta.tessek;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by lmentaschi on 30/09/17.
 */

public class TsGridView extends GridView {

    public TsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TsGridView(Context context) {
        super(context);
    }

    public TsGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
