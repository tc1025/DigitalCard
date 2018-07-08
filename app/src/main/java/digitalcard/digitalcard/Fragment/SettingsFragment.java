package digitalcard.digitalcard.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.KeyUtilities;

public class SettingsFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;
    TextView tvTitle;

    LinearLayout btnBack, addSecurityCodeButton, deleteSecurityCodeButton, aboutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
        }

        toolbar = rootView.findViewById(R.id.toolbar);

        tvTitle = toolbar.getTxtTitle();
        tvTitle.setText("Settings");
        btnBack = toolbar.getBtnBack();

        addSecurityCodeButton = rootView.findViewById(R.id.setting_password_button);
        deleteSecurityCodeButton = rootView.findViewById(R.id.delete_security_code_button);
        aboutButton = rootView.findViewById(R.id.about_button);

        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!mySPrefs.contains("Key")){
            deleteSecurityCodeButton.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(this);
        addSecurityCodeButton.setOnClickListener(this);
        deleteSecurityCodeButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.setting_password_button:
                AddSecurityCodeFragment addSecurityCodeFragment = new AddSecurityCodeFragment();

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drag_view, addSecurityCodeFragment, "SettingsAddPassword")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.delete_security_code_button:
                final KeyUtilities keyUtilities = new KeyUtilities();
                keyUtilities.initKeystore();
                keyUtilities.removeKey(getActivity());

                deleteSecurityCodeButton.setVisibility(View.GONE);
                break;
            case R.id.about_button:
                AboutFragment aboutFragment= new AboutFragment();

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drag_view, aboutFragment, "SettingsAbout")
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
