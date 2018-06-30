package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import digitalcard.digitalcard.Database.CardDB;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Module.DoubleButton;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Scan.CameraActivity;
import digitalcard.digitalcard.Util.Utilities;

/**
 * Created by viks on 16/03/2018.
 */

public class ExistingMemberFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;
    DoubleButton btnAction;

    LinearLayout btnBack, btnCancel, btnDone;
    TextView txtTitle;
    EditText etBarcodeNumber, etCardName;
    Button btnScan;

    String title, logo, cardName, barcodeNumber, frontView, backView, notes;
    int backgroundColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_card, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
        }

        toolbar = rootView.findViewById(R.id.toolbar);
        btnAction = rootView.findViewById(R.id.btn_action);

        txtTitle = toolbar.getTxtTitle();
        btnBack = toolbar.getBtnBack();
        btnCancel = btnAction.getBtnLeft();
        btnDone = btnAction.getBtnRight();
        etBarcodeNumber = rootView.findViewById(R.id.barcode_number);
        etCardName = rootView.findViewById(R.id.card_name);
        btnScan = rootView.findViewById(R.id.btn_scan);

        title = getArguments().getString(Utilities.BUNDLE_CARD_CATEGORY);
        logo = getArguments().getString(Utilities.BUNDLE_CARD_LOGO);
        backgroundColor = getArguments().getInt(Utilities.BUNDLE_CARD_BACKGROUND);

        txtTitle.setText(title);
        etBarcodeNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnScan.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        switch (view.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.button_cancel:
                getActivity().onBackPressed();
                break;

            case R.id.button_done:
                Log.e("edittext", "card name : " + etCardName.getText() + ", barcode number : " + etBarcodeNumber.getText());
                if (etCardName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Card alias must be filled", Toast.LENGTH_SHORT).show();
                    etCardName.requestFocus();
                } else if (etBarcodeNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Barcode number must be filled", Toast.LENGTH_SHORT).show();
                    etBarcodeNumber.requestFocus();
                } else {
                    Toast.makeText(getActivity(), "Card Added", Toast.LENGTH_SHORT).show();

                    cardName = etCardName.getText().toString();
                    barcodeNumber = etBarcodeNumber.getText().toString();
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

            case R.id.btn_scan:
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 1);
                break;
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }

        if (requestCode == 1) {
            if (data != null) {
                String serialNumber = data.getStringExtra("CODE_DATA");
                etBarcodeNumber.setText(serialNumber);
                Toast.makeText(getActivity(), data.getStringExtra("CODE_TYPE"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}