package digitalcard.digitalcard.Module;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import digitalcard.digitalcard.R;

public class MenuPopupDialog extends Dialog {
    Context context;

    private LinearLayout btnAccount, btnSettings, btnInfo;
    private RelativeLayout panel;

    public MenuPopupDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.module_menu_popup);

        btnAccount = findViewById(R.id.menu_account);
        btnSettings = findViewById(R.id.menu_settings);
        btnInfo = findViewById(R.id.menu_info);
    }

    public RelativeLayout getPanel(){
        return panel;
    }

    public LinearLayout getBtnAccount() {
        return btnAccount;
    }

    public LinearLayout getBtnSettings() {
        return btnSettings;
    }

    public LinearLayout getBtnInfo() {
        return btnInfo;
    }
}
