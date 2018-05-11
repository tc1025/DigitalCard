package digitalcard.digitalcard.Module;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import digitalcard.digitalcard.R;

public class PopupBarcodeDialog extends Dialog{
    private Context context;

    private ImageView imgBarcode;
    private TextView tvBarcodeNumber;
    private LinearLayout llDialogActivity;

    private int brightness;

    public PopupBarcodeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.module_popup_dialog);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(context))
                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }

            brightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        }
        catch(Settings.SettingNotFoundException e){
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context))
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
        WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
        layoutpars.screenBrightness = brightness / (float)255;
        Log.e("tommy", "normal " + brightness + ", changes " + brightness / (float)255);
        getWindow().setAttributes(layoutpars);

        imgBarcode = findViewById(R.id.barcode_image_dialog);
        tvBarcodeNumber = findViewById(R.id.barcode_number_dialog);
        llDialogActivity = findViewById(R.id.dialog_activity);
    }

    public ImageView getImgBarcode() {
        return imgBarcode;
    }

    public TextView getTvBarcodeNumber() {
        return tvBarcodeNumber;
    }

    public LinearLayout getLlDialogActivity() {
        return llDialogActivity;
    }
}
