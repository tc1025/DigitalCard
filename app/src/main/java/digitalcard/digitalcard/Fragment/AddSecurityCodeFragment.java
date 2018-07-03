package digitalcard.digitalcard.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.KeyUtilities;

public class AddSecurityCodeFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;
    TextView tvTitle, mEnterPinPrompt;

    LinearLayout btnBack;

    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;

    String codeFirstAttempt = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_security_code, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
        }

        toolbar = rootView.findViewById(R.id.toolbar);

        tvTitle = toolbar.getTxtTitle();
        tvTitle.setText("Password");
        btnBack = toolbar.getBtnBack();

        mEnterPinPrompt = rootView.findViewById(R.id.enter_pin_prompt);

        mPinLockView = rootView.findViewById(R.id.pin_lock_view);
        mIndicatorDots = rootView.findViewById(R.id.pin_indicator);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(6);

        final KeyUtilities keyUtilities = new KeyUtilities();
        keyUtilities.initKeystore();

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (codeFirstAttempt.isEmpty()){
                    codeFirstAttempt = pin;
                    mPinLockView.resetPinLockView();
                    mEnterPinPrompt.setText("Re-enter Your Security Code");
                } else {
                    if (pin.equals(codeFirstAttempt)){
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                                keyUtilities.checkForKey(getActivity());
                                keyUtilities.encryptString(getActivity(), pin);
//                            }
//                        }).start();
                        getActivity().onBackPressed();
                    } else {
                        codeFirstAttempt = "";
                        mPinLockView.resetPinLockView();
                        mEnterPinPrompt.setText("Enter Your Security Code");
                    }
                }
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });

        btnBack.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }
}
