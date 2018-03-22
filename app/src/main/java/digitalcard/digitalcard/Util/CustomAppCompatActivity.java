package digitalcard.digitalcard.Util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by viks on 16/03/2018.
 */

@SuppressLint("Registered")
public class CustomAppCompatActivity extends AppCompatActivity {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupTransparentSystemBarsForLmp(getWindow());
    }

    public static boolean isLmpOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Sets up transparent navigation and status bars in LMP. This method is a
     * no-op for other platform versions.
     */
    @TargetApi(19)
    public static void setupTransparentSystemBarsForLmp(Window window) {
        // Currently we use reflection to access the flags and the API to set
        // the transparency
        // on the System bars.
        if (isLmpOrAbove()) {
            try {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                Field drawsSysBackgroundsField = WindowManager.LayoutParams.class.getField("FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS");
                window.addFlags(drawsSysBackgroundsField.getInt(null));

                Method setStatusBarColorMethod = Window.class.getDeclaredMethod("setStatusBarColor", int.class);
                Method setNavigationBarColorMethod = Window.class.getDeclaredMethod("setNavigationBarColor", int.class);
                setStatusBarColorMethod.invoke(window, Color.TRANSPARENT);
                setNavigationBarColorMethod.invoke(window, Color.RED);
                //setNavigationBarColorMethod.invoke(window, R.drawable.gradient_background);	// Navigation color
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}