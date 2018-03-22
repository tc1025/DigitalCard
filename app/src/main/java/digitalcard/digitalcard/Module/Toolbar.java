package digitalcard.digitalcard.Module;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import digitalcard.digitalcard.R;

/**
 * Created by viks on 16/03/2018.
 */

public class Toolbar extends LinearLayout {
    TextView txtTitle;
    LinearLayout btnBack;
    RelativeLayout panel;
    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.module_toolbar_popup, this, true);
        }
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        panel = (RelativeLayout) getChildAt(0);
        btnBack = (LinearLayout) panel.getChildAt(0);
        txtTitle = (TextView) panel.getChildAt(1);
    }

    public TextView getTxtTitle(){
        return txtTitle;
    }

    public LinearLayout getBtnBack(){
        return btnBack;
    }
}
