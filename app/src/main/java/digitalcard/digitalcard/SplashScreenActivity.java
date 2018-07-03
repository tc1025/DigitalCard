package digitalcard.digitalcard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by Laptops on 03/05/2018.
 */

public class SplashScreenActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
                if (preferences.contains("Key")){
                    startActivity(new Intent(SplashScreenActivity.this, SecurityCodeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
