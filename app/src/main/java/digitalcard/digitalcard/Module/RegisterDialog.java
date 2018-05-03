package digitalcard.digitalcard.Module;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;

import digitalcard.digitalcard.R;

public class RegisterDialog extends Dialog {
    Context activity;
    Button yesBtn, noBtn;

    public RegisterDialog(@NonNull Context context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.module_register_dialog);

//        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yesBtn = findViewById(R.id.btn_yes);
        noBtn = findViewById(R.id.btn_no);
    }

    public Button getYesBtn() {
        return yesBtn;
    }

    public Button getNoBtn() {
        return noBtn;
    }
}
