package digitalcard.digitalcard.Module;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    RelativeLayout panel, panelMiddle;
    ImageButton btnLocation, btnDelete;
    ImageView imgDropdown;

    Boolean backButton, cardOverviewStyle;

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.module_toolbar_popup, this, true);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Toolbar);
        backButton = ta.getBoolean(R.styleable.Toolbar_backButton, true);
        cardOverviewStyle = ta.getBoolean(R.styleable.Toolbar_cardOverviewStyle, false);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        panel = (RelativeLayout) getChildAt(0);
        btnBack = (LinearLayout) panel.getChildAt(0);
        panelMiddle = (RelativeLayout) panel.getChildAt(1);
        txtTitle = (TextView) panelMiddle.getChildAt(0);
        imgDropdown = (ImageView) panelMiddle.getChildAt(1);
        btnLocation = (ImageButton) panel.getChildAt(2);
        btnDelete = (ImageButton) panel.getChildAt(3);

        if (!backButton) {
            btnBack.setVisibility(GONE);
            imgDropdown.setVisibility(VISIBLE);
        } else {
            btnBack.setVisibility(VISIBLE);
            imgDropdown.setVisibility(GONE);
        }

        if (!cardOverviewStyle){
            btnLocation.setVisibility(GONE);
            btnDelete.setVisibility(GONE);
        } else {
            btnLocation.setVisibility(VISIBLE);
            btnDelete.setVisibility(VISIBLE);
        }
    }

    public TextView getTxtTitle(){
        return txtTitle;
    }

    public LinearLayout getBtnBack(){
        return btnBack;
    }

    public ImageButton getBtnLocation(){
        return btnLocation;
    }

    public ImageButton getBtnDelete(){
        return btnDelete;
    }

    public void backButtonView(Boolean setVisibility) {
        if (!setVisibility) {
            btnBack.setVisibility(GONE);
            imgDropdown.setVisibility(VISIBLE);
        } else {
            btnBack.setVisibility(VISIBLE);
            imgDropdown.setVisibility(GONE);
        }
    }
}
