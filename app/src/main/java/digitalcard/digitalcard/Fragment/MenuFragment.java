package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;

public class MenuFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;

    LinearLayout btnAccount, btnSettings, btnInfo;
    TextView tvTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);

        tvTitle = toolbar.getTxtTitle();
        btnAccount = rootView.findViewById(R.id.btn_account);
        btnSettings = rootView.findViewById(R.id.btn_settings);
        btnInfo = rootView.findViewById(R.id.btn_info);

        tvTitle.setText("Menu");
        btnAccount.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account:
                AccountFragment accountFragment = new AccountFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.drag_view, accountFragment, "Account");
                ft.commit();
                break;

            case R.id.btn_settings:
                Toast.makeText(getActivity(), "You choose Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_info:
                Toast.makeText(getActivity(), "You choose Info", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
