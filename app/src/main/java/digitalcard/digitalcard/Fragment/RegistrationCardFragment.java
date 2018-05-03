package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import digitalcard.digitalcard.Module.DoubleButton;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

/**
 * Created by viks on 24/03/2018.
 */

public class RegistrationCardFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;
    DoubleButton action;

    TextView tvTitle;
    LinearLayout btnBack, btnCancel, btnDone;

    String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_card, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        action = rootView.findViewById(R.id.btn_action);

        title = getArguments().getString(Utilities.BUNNDLE_CARD_CATEGORY);

        tvTitle = toolbar.getTxtTitle();
        btnBack = toolbar.getBtnBack();
        btnCancel = action.getBtnLeft();
        btnDone = action.getBtnRight();

        tvTitle.setText(title);

        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.button_cancel:
                Toast.makeText(getActivity(), "You choose cancel", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;

            case R.id.button_done:
                Random random = new Random();
                String dsa = "";
                for (int i = 0; i < 4; i++) {
                    int q = random.nextInt(9999 + 1);
                    String test;
                    if (q < 1000) {
                        test = "0" + String.valueOf(q);
                        dsa = dsa + test + " ";
                    } else {
                        dsa = dsa + q + " ";
                    }
                }

                Toast.makeText(getActivity(), "You choose done, card id is " + dsa, Toast.LENGTH_SHORT).show();

                DetailCardFragment detailCardFragment = new DetailCardFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Utilities.BUNNDLE_CARD_NAME, title);
                bundle.putString(Utilities.BUNNDLE_BARCODE_NUMBER, dsa);
                bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, title);
                detailCardFragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.drag_view, detailCardFragment, "DetailCard").commit();
                break;
        }
    }
}
