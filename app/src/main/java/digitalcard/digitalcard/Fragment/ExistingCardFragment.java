package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import digitalcard.digitalcard.Module.DoubleButton;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Scan.CameraActivity;
import digitalcard.digitalcard.Util.Utilities;

/**
 * Created by viks on 16/03/2018.
 */

public class ExistingCardFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;
    DoubleButton btnAction;

    LinearLayout btnBack, btnCancel, btnDone;
    TextView txtTitle;
    EditText etBarcodeNumber, etCardName;
    Button btnScan;

    String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_card, container, false);
        toolbar = rootView.findViewById(R.id.toolbar);
        btnAction = rootView.findViewById(R.id.btn_action);

        txtTitle = toolbar.getTxtTitle();
        btnBack = toolbar.getBtnBack();
        btnCancel = btnAction.getBtnLeft();
        btnDone = btnAction.getBtnRight();
        etBarcodeNumber = rootView.findViewById(R.id.barcode_number);
        etCardName = rootView.findViewById(R.id.card_name);
        btnScan = rootView.findViewById(R.id.btn_scan);

        title = getArguments().getString(Utilities.BUNNDLE_CARD_CATEGORY);
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
        switch (view.getId()){
            case R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.button_cancel:
                Toast.makeText(getActivity(), "You choose cancel", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;

            case R.id.button_done:
                Log.e("edittext", "card name : " + etCardName.getText() + ", barcode number : " + etBarcodeNumber.getText());
                if (etCardName.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Card name must be filled", Toast.LENGTH_SHORT).show();
                else if (etBarcodeNumber.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Barcode number must be filled", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getActivity(), "You add a new card", Toast.LENGTH_SHORT).show();

                    DetailCardFragment detailCardFragment = new DetailCardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Utilities.BUNNDLE_CARD_NAME, etCardName.getText().toString());
                    bundle.putString(Utilities.BUNNDLE_BARCODE_NUMBER, etBarcodeNumber.getText().toString());
                    bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, title);
                    detailCardFragment.setArguments(bundle);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.drag_view, detailCardFragment, "DetailCard").commit();
                }
                break;

            case R.id.btn_scan:
                Toast.makeText(getActivity(), "You choose scan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 1);
                break;
        }
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
            }
        }
    }
}