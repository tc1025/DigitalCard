package digitalcard.digitalcard.Module;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import digitalcard.digitalcard.R;

public class EditDialog extends Dialog {
    private Context context;

    private TextView tvTitle;
    private EditText etEdit;
    private Button btnDone, btnCancel;

    public EditDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.module_edit_dialog);

        tvTitle = findViewById(R.id.dialog_title);
        etEdit = findViewById(R.id.dialog_edit);
        btnDone = findViewById(R.id.btn_done);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public EditText getEtEdit() {
        return etEdit;
    }

    public Button getBtnDone() {
        return btnDone;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }
}
