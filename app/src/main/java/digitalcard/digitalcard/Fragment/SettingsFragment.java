package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
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

public class SettingsFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;
    TextView tvTitle;

    LinearLayout btnBack, addPasswordButton;

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

        addPasswordButton = rootView.findViewById(R.id.setting_password_button);

        btnBack.setOnClickListener(this);
        addPasswordButton.setOnClickListener(this);

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
        }
    }
}
