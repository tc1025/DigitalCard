package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by viks on 17/03/2018.
 */

public class DetailCardFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;

    TextView txtTitle, txtCardName, txtBarcodeNumber;
    LinearLayout btnBack, btnCardFrontView, btnCardBackView;
    ImageView imgBarcode;
    Button btnOk;

    String cardName, barcodeNumber, title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail_card, container, false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
        }

        toolbar = rootView.findViewById(R.id.toolbar);

        cardName = getArguments().getString(Utilities.BUNNDLE_CARD_NAME);
        barcodeNumber = getArguments().getString(Utilities.BUNNDLE_BARCODE_NUMBER);
        title = getArguments().getString(Utilities.BUNNDLE_CARD_CATEGORY);

        txtTitle = toolbar.getTxtTitle();
        txtCardName = rootView.findViewById(R.id.card_name);
        txtBarcodeNumber = rootView.findViewById(R.id.barcode_number);
        imgBarcode = rootView.findViewById(R.id.barcode_image);
        btnBack = toolbar.getBtnBack();
        btnCardFrontView = rootView.findViewById(R.id.card_front_view);
        btnCardBackView = rootView.findViewById(R.id.card_back_view);
        btnOk = rootView.findViewById(R.id.btn_ok);

        txtTitle.setText(title);
        txtCardName.setText(cardName);
        txtBarcodeNumber.setText(barcodeNumber);

        btnBack.setOnClickListener(this);
        btnCardFrontView.setOnClickListener(this);
        btnCardBackView.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        convertNumberToBarcode(barcodeNumber);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.card_front_view:
                Toast.makeText(getContext(), "You choose front view", Toast.LENGTH_SHORT).show();
                break;

            case R.id.card_back_view:
                Toast.makeText(getContext(), "You choose back view", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_ok:
                Toast.makeText(getContext(), "You choose OK", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    public void convertNumberToBarcode(String barcodeNumber) {
        Bitmap bitmap = null;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        width = (4 * width) / 5;

        try {
            bitmap = encodeAsBitmap(barcodeNumber, BarcodeFormat.CODE_128, width, convertDpToPx(87));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        imgBarcode.setImageBitmap(bitmap);
    }

    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        if (contents == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
