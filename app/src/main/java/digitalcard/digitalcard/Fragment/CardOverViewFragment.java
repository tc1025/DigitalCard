package digitalcard.digitalcard.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.Objects;

import digitalcard.digitalcard.Module.PopupDialog;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CardOverViewFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;
    PopupDialog popupDialog;

    TextView tvTitle, tvCardName, tvBarcodeNumber;
    ImageButton btnLocation;
    ImageView imgBarcode, dialogImgBarcode;
    LinearLayout dialogActivity;

    String title, cardName, barcodeNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_card_overview, container, false);
        toolbar = rootView.findViewById(R.id.toolbar);
        popupDialog = new PopupDialog(getActivity());

        tvTitle = toolbar.getTxtTitle();
        btnLocation = toolbar.getBtnLocation();
        tvCardName = rootView.findViewById(R.id.card_name);
        tvBarcodeNumber = rootView.findViewById(R.id.barcode_number);
        imgBarcode = rootView.findViewById(R.id.barcode_image);

        title = getArguments().getString(Utilities.BUNNDLE_CARD_CATEGORY);
        cardName = getArguments().getString(Utilities.BUNNDLE_CARD_NAME);
        barcodeNumber = getArguments().getString(Utilities.BUNNDLE_BARCODE_NUMBER);

        tvTitle.setText(title);
        tvCardName.setText(cardName);
        tvBarcodeNumber.setText(barcodeNumber);

        btnLocation.setOnClickListener(this);
        imgBarcode.setOnClickListener(this);

        convertNumberToBarcode(barcodeNumber, false);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.location_button:
                Toast.makeText(getActivity(), "You choose get location", Toast.LENGTH_SHORT).show();
                break;

            case R.id.barcode_image:
                Toast.makeText(getContext(), "You choose Barcode Image", Toast.LENGTH_SHORT).show();
                popupDialog.show();
                if (popupDialog.isShowing()) {
                    TextView dialogBarcodeNumber = popupDialog.getTvBarcodeNumber();
                    dialogImgBarcode = popupDialog.getImgBarcode();
                    dialogActivity = popupDialog.getLlDialogActivity();

                    dialogBarcodeNumber.setText(barcodeNumber);
                    convertNumberToBarcode(barcodeNumber, true);

                    dialogActivity.setOnClickListener(this);
                }
                break;

            case R.id.dialog_activity:
                popupDialog.dismiss();
                break;
        }
    }

    public void convertNumberToBarcode(String barcodeNumber, Boolean dialogBarcode) {
        Bitmap bitmap = null;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        width = (4 * width) / 5;

        if (Objects.equals(title, Utilities.MERCHANT_STARBUCKS)) {
            try {
                bitmap = encodeAsBitmap(barcodeNumber, BarcodeFormat.PDF_417, width, convertDpToPx(87));
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else
            try {
                bitmap = encodeAsBitmap(barcodeNumber, BarcodeFormat.CODE_128, width, convertDpToPx(87));
            } catch (WriterException e) {
                e.printStackTrace();
            }

        if (!dialogBarcode)
            imgBarcode.setImageBitmap(bitmap);
        else
            dialogImgBarcode.setImageBitmap(bitmap);
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
