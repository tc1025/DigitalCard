package digitalcard.digitalcard;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import digitalcard.digitalcard.Util.KeyUtilities;

public class SecurityCodeActivity extends Activity {
    private static final String KEY_NAME = "DigiCardFingerprint";

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
                    startActivity(new Intent(SecurityCodeActivity.this, MainActivity.class));
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

        final KeyUtilities keyUtilities = new KeyUtilities();
        keyUtilities.initKeystore();

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if (!fingerprintManager.isHardwareDetected()
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED
                && !fingerprintManager.hasEnrolledFingerprints()
                && !keyguardManager.isKeyguardSecure()){
            mFingerprintLayout.setVisibility(View.GONE);
        } else{
            try {
                keyUtilities.generateKeyFingerprint();

                if (keyUtilities.initCipher()) {
                    keyUtilities.startAuth(this, fingerprintManager);
                }
            } catch (KeyUtilities.FingerprintException e) {
                e.printStackTrace();
            }
        }

    }
}
