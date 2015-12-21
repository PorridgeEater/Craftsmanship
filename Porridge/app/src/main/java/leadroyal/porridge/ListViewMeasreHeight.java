package leadroyal.porridge;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by LeadroyaL on 2015/12/6.
 */
public class ListViewMeasreHeight extends ListView {


    public ListViewMeasreHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ListViewMeasreHeight(Context context) {
        super(context);
    }


    public ListViewMeasreHeight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //根据模式计算每个child的高度和宽度
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);


    }


}
