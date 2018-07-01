package digitalcard.digitalcard.Module;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import digitalcard.digitalcard.R;

/**
 * Created by viks on 16/03/2018.
 */

public class DoubleButton extends LinearLayout {
    LinearLayout panel, btnLeft, btnRight;

    public DoubleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.module_double_button, this, true);
        }
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        panel = (LinearLayout) getChildAt(0);
        btnLeft = (LinearLayout) panel.getChildAt(0);
        btnRight = (LinearLayout) panel.getChildAt(2);

    }

    public LinearLayout getBtnLeft(){
        return btnLeft;
    }

    public LinearLayout getBtnRight(){
        return  btnRight;
    }
}
