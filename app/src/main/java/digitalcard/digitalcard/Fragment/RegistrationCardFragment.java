package digitalcard.digitalcard.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import digitalcard.digitalcard.Database.CardDB;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
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

    TextView tvTitle, tvCaptchaGenerator;
    LinearLayout btnBack, btnCancel, btnDone;
    EditText regName, regIdNumber, regAddress, regDOB, regCaptcha, regCardName;
    RadioGroup regGender;
    RadioButton rbGender;

    String title, logo, cardName, barcodeNumber, frontView, backView, notes, captcha = "";
    int backgroundColor;

    Calendar calendar = Calendar.getInstance();
    String dateFormat = "dd/MM/yyyy";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_card, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
        }

        toolbar = rootView.findViewById(R.id.toolbar);
        action = rootView.findViewById(R.id.btn_action);

        regName = rootView.findViewById(R.id.register_name);
        regIdNumber = rootView.findViewById(R.id.register_id_number);
        regGender = rootView.findViewById(R.id.radio_gender);
        regAddress = rootView.findViewById(R.id.register_address);
        regDOB = rootView.findViewById(R.id.register_DOB);

        assert getArguments() != null;
        title = getArguments().getString(Utilities.BUNDLE_CARD_CATEGORY);
        logo = getArguments().getString(Utilities.BUNDLE_CARD_LOGO);
        backgroundColor = getArguments().getInt(Utilities.BUNDLE_CARD_BACKGROUND);

        tvTitle = toolbar.getTxtTitle();
        btnBack = toolbar.getBtnBack();
        btnCancel = action.getBtnLeft();
        btnDone = action.getBtnRight();
        tvCaptchaGenerator = rootView.findViewById(R.id.register_captcha_generator);
        regCaptcha = rootView.findViewById(R.id.register_captcha);
        regCardName = rootView.findViewById(R.id.register_card_name);

        Random r = new Random();
        String code = "";
        for (int i = 0; i < 5; i++) {
            if (i < 2)
                code = code + (char) (r.nextInt(26) + 'a');
            else
                code = code + r.nextInt(10);
        }
        captcha = code.toUpperCase();
        tvCaptchaGenerator.setText(captcha);

        tvTitle.setText(title);
        regIdNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

        regDOB.setOnClickListener(this);
        regGender.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int genderId = regGender.getCheckedRadioButtonId();
        rbGender = rootView.findViewById(genderId);

        switch (v.getId()){
            case R.id.register_DOB:
                new DatePickerDialog(getContext(), date, 1990, 1, 1).show();
                break;

            case R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.button_cancel:
                getActivity().onBackPressed();
                break;

            case R.id.button_done:
                if (regName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Name field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regIdNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Id number field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regIdNumber.getText().toString().length() != 16) {
                    Toast.makeText(getActivity(), "Id number length must 16 digits", Toast.LENGTH_SHORT).show();
                } else if (genderId == -1) {
                    Toast.makeText(getActivity(), "Gender field must be choose", Toast.LENGTH_SHORT).show();
                } else if (regAddress.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Address field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regDOB.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Date of birth field must be filled", Toast.LENGTH_SHORT).show();
                } else if (regCardName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Card name field must be filled", Toast.LENGTH_SHORT).show();
                } else if (!regCaptcha.getText().toString().equals(captcha)) {
                    Toast.makeText(getActivity(), "Captcha not matched", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();

                    cardName = regCardName.getText().toString();
                    barcodeNumber = randomBarcode;
                    notes = "";
                    frontView = "";
                    backView = "";

                    CardOverViewFragment cardOverViewFragment = new CardOverViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Utilities.BUNDLE_CARD_NAME, cardName);
                    bundle.putString(Utilities.BUNDLE_BARCODE_NUMBER, barcodeNumber);
                    bundle.putString(Utilities.BUNDLE_CARD_CATEGORY, title);
                    bundle.putString(Utilities.BUNDLE_CARD_LOGO, logo);
                    bundle.putInt(Utilities.BUNDLE_CARD_BACKGROUND, backgroundColor);
                    bundle.putString(Utilities.BUNDLE_CARD_NOTES, notes);
                    bundle.putString(Utilities.BUNDLE_CARD_FRONT_VIEW, frontView);
                    bundle.putString(Utilities.BUNDLE_CARD_BACK_VIEW, backView);
                    cardOverViewFragment.setArguments(bundle);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }

                    addCard();

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.drag_view, cardOverViewFragment, "CardOverview").commit();
                }
                break;
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_YEAR, dayOfMonth);
            updateDate();
        }
    };

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        regDOB.setText(sdf.format(calendar.getTime()));
    }

    public void addCard () {
        CardList cardList = new CardList();
        cardList.cardName       = cardName;
        cardList.cardType       = title;
        cardList.barcodeNumber  = barcodeNumber;
        cardList.cardIcon       = logo;
        cardList.cardBackground = backgroundColor;
        cardList.cardNote       = notes;
        cardList.cardFrontView  = frontView;
        cardList.cardBackView   = backView;

        CardDB cardDB = new CardDB(getContext());
        cardDB.addCard(new CardList(cardList.cardName, cardList.cardType ,cardList.barcodeNumber, cardList.cardIcon, cardList.cardBackground, cardList.cardNote, cardList.cardFrontView, cardList.cardBackView));

        Intent intent = new Intent(TabKartu.RADIO_DATASET_CHANGED);
        getActivity().sendBroadcast(intent);
    }
}
