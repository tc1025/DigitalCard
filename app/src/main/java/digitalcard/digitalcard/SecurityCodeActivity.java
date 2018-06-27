package digitalcard.digitalcard;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import digitalcard.digitalcard.Util.KeyUtilities;

public class SecurityCodeActivity extends Activity {

    private LinearLayout mFingerprintLayout;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private String decryptResult;

    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_code);

        mFingerprintLayout = findViewById(R.id.use_fingerprint_layout);
        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.pin_indicator);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(6);

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                final KeyUtilities keyUtilities = new KeyUtilities();
                keyUtilities.initKeystore();
                decryptResult = keyUtilities.decryptString(SecurityCodeActivity.this);

                if (pin.equals(decryptResult)){
                    Intent i = new Intent(SecurityCodeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                 mPinLockView.resetPinLockView();
                    Toast.makeText(SecurityCodeActivity.this, R.string.wrong_security_code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if (!fingerprintManager.isHardwareDetected()){
            mFingerprintLayout.setVisibility(View.GONE);
        }

    }
}
