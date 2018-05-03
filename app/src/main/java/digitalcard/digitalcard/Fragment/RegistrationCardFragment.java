package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    EditText regName, regIdNumber, regGender, regAddress, regDOB;

    String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_card, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        action = rootView.findViewById(R.id.btn_action);

        regName = rootView.findViewById(R.id.register_name);
        regIdNumber = rootView.findViewById(R.id.register_id_number);
        regGender = rootView.findViewById(R.id.register_gender);
        regAddress = rootView.findViewById(R.id.register_address);
        regDOB = rootView.findViewById(R.id.register_DOB);

        title = getArguments().getString(Utilities.BUNNDLE_CARD_CATEGORY);

        tvTitle = toolbar.getTxtTitle();
        btnBack = toolbar.getBtnBack();
        btnCancel = action.getBtnLeft();
        btnDone = action.getBtnRight();

        tvTitle.setText(title);
        regIdNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

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
                if (regName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Name field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regIdNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Id number field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regGender.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Gender field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regAddress.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Address field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regDOB.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Date of birth field must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    Random random = new Random();
                    String randomBarcode = "";
                    for (int i = 0; i < 4; i++) {
                        int randomInt = random.nextInt(9999 + 1);
                        String test;
                        if (randomInt < 1000) {
                            test = "0" + String.valueOf(randomInt);
                            randomBarcode = randomBarcode + test;
                        } else {
                            randomBarcode = randomBarcode + randomInt;
                        }
                    }

                    DetailCardFragment detailCardFragment = new DetailCardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Utilities.BUNNDLE_CARD_NAME, title);
                    bundle.putString(Utilities.BUNNDLE_BARCODE_NUMBER, randomBarcode);
                    bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, title);
                    detailCardFragment.setArguments(bundle);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.drag_view, detailCardFragment, "DetailCard").commit();
                }
                break;
        }
    }
}
