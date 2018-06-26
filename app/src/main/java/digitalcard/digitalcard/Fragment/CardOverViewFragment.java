package digitalcard.digitalcard.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import digitalcard.digitalcard.Database.CardDB;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Module.EditDialog;
import digitalcard.digitalcard.Module.PopupBarcodeDialog;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CardOverViewFragment extends Fragment implements View.OnClickListener{
    View rootView;
    CardDB cardDB;
    Toolbar toolbar;
    PopupBarcodeDialog popupDialog;
    EditDialog editDialog;

    TextView tvTitle, tvCardName, tvBarcodeNumber, tvNotes;
    ImageButton btnLocation, btnDelete;
    ImageView imgBarcode, dialogImgBarcode, imgLogo, imgFrontView, imgBackView;
    LinearLayout dialogActivity, btnBack;
    Button btnOk;

    String title, cardName, barcodeNumber, logo, frontView, backView, notes;
    int cardID, backgroundColor, id;
    Bitmap bmFrontView, bmBackView;

    private GoogleApiClient mGoogleApiClient;
    Location location;

    private List<CardList> cardLists = new ArrayList<>();

    Intent intent = new Intent(TabKartu.RADIO_DATASET_CHANGED);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_card_overview, container, false);
        cardDB = new CardDB(getActivity());

        toolbar = rootView.findViewById(R.id.toolbar);
        popupDialog = new PopupBarcodeDialog(getActivity());
        editDialog = new EditDialog(getActivity());

        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initGoogleServices();
        }

        tvTitle = toolbar.getTxtTitle();
        btnLocation = toolbar.getBtnLocation();
        btnBack = toolbar.getBtnBack();
        tvCardName = rootView.findViewById(R.id.card_name);
        tvBarcodeNumber = rootView.findViewById(R.id.barcode_number);
        imgBarcode = rootView.findViewById(R.id.barcode_image);
        btnDelete = rootView.findViewById(R.id.delete_card);
        imgLogo = rootView.findViewById(R.id.merchant_logo);
        tvNotes = rootView.findViewById(R.id.notes);
        imgFrontView = rootView.findViewById(R.id.img_front_view);
        imgBackView = rootView.findViewById(R.id.img_back_view);
        btnOk = rootView.findViewById(R.id.btn_ok);

        Boolean toolbarView = getArguments().getBoolean(Utilities.BUNDLE_BACK_BUTTON_VISIBILITY, false);
        if (toolbarView) {
            toolbar.backButtonView(toolbarView);
            if (getContext() instanceof MainActivity) {
                ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
                btnDelete.setVisibility(View.GONE);
                btnLocation.setVisibility(View.GONE);
                btnOk.setVisibility(View.VISIBLE);
            }
        } else {
            toolbar.backButtonView(toolbarView);
        }

        assert getArguments() != null;
        cardID = getArguments().getInt(Utilities.BUNDLE_CARD_ID);
        title = getArguments().getString(Utilities.BUNDLE_CARD_CATEGORY);
        cardName = getArguments().getString(Utilities.BUNDLE_CARD_NAME);
        barcodeNumber = getArguments().getString(Utilities.BUNDLE_BARCODE_NUMBER);
        logo = getArguments().getString(Utilities.BUNDLE_CARD_LOGO);
        backgroundColor = getArguments().getInt(Utilities.BUNDLE_CARD_BACKGROUND);
        notes = getArguments().getString(Utilities.BUNDLE_CARD_NOTES);
        frontView = getArguments().getString(Utilities.BUNDLE_CARD_FRONT_VIEW);
        backView = getArguments().getString(Utilities.BUNDLE_CARD_BACK_VIEW);
        id = getArguments().getInt("aaa");

        tvTitle.setText(title);
        tvCardName.setText(cardName);
        tvBarcodeNumber.setText(barcodeNumber);
        Picasso.get().load(logo).into(imgLogo);
        imgLogo.setBackgroundColor(backgroundColor);
        if (!notes.equals(""))
            tvNotes.setText(notes);
        if (!frontView.equals("")) {
            byte[] decodeImage = Base64.decode(frontView, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
            imgFrontView.setImageBitmap(image);
        }
        if (!backView.equals("")) {
            byte[] decodeImage = Base64.decode(backView, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
            imgBackView.setImageBitmap(image);
        }

        btnLocation.setOnClickListener(this);
        imgBarcode.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        tvCardName.setOnClickListener(this);
        tvNotes.setOnClickListener(this);
        imgFrontView.setOnClickListener(this);
        imgBackView.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        convertNumberToBarcode(barcodeNumber, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                initGoogleServices();
            }
        } else {
            initGoogleServices();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (v.getId()) {
            case  R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.btn_ok:
                addCard();
                break;

            case R.id.card_name:
                popUpEdit("cardName");
                break;

            case R.id.notes:
                popUpEdit("cardNotes");
                break;

            case R.id.img_front_view:
                startActivityForResult(intentCamera, 5);
                break;

            case R.id.img_back_view:
                startActivityForResult(intentCamera, 6);
                break;

            case R.id.location_button:
                LocationFragment locationFragment = new LocationFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble(Utilities.BUNDLE_LOCATION_LATITUDE, location != null ? location.getLatitude() : 0.0);
                bundle.putDouble(Utilities.BUNDLE_LOCATION_LONGITUDE, location != null ? location.getLongitude() : 0.0);
                bundle.putString(Utilities.BUNDLE_CARD_CATEGORY, cardName);
                locationFragment.setArguments(bundle);
                ft.addToBackStack(null);
                ft.replace(R.id.drag_view, locationFragment, "LocationCard").commit();

//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .build(getActivity());
//                    startActivityForResult(intent, 1);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO: Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                }

                break;

            case R.id.barcode_image:
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

            case R.id.delete_card:
                List<CardList> tempCardLists = cardDB.getAllCardList();
                if (!tempCardLists.isEmpty())
                    for (CardList data : tempCardLists) {
                        cardLists.add(new CardList(data.getId(), data.getCardType(), data.getCardName(), data.getBarcodeNumber(), data.getCardIcon(), data.getCardBackground(), data.getCardNote(), data.getCardFrontView(), data.getCardBackView()));
                    }
                CardList deleteTarget = cardLists.get(cardID);
                cardDB.deleteCard(deleteTarget);

//                TabKartu tabKartu = new TabKartu();
//                ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
//                ft.detach(tabKartu);
//                ft.attach(tabKartu);
//                ft.commit();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                if (getContext() instanceof MainActivity) {
//                    ((MainActivity) getContext()).getSlidingPanel().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                }
                break;
        }
    }

    public void addCard () {
        CardList cardList = new CardList();
        cardList.cardName = cardName;
        cardList.cardType = title;
        cardList.barcodeNumber = barcodeNumber;
        cardList.cardIcon = logo;
        cardList.cardBackground = backgroundColor;
        cardList.cardNote = notes;
        cardList.cardFrontView = frontView;
        cardList.cardBackView = backView;

        CardDB cardDB = new CardDB(getContext());
        cardDB.addCard(new CardList(cardList.cardName, cardList.cardType ,cardList.barcodeNumber, cardList.cardIcon, cardList.cardBackground, cardList.cardNote, cardList.cardFrontView, cardList.cardBackView));

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void popUpEdit (final String editSection) {
        editDialog.show();
        if (editDialog.isShowing()) {
            TextView tvTitle = editDialog.getTvTitle();
            final EditText etEdit = editDialog.getEtEdit();
            Button btnDone = editDialog.getBtnDone();
            Button btnCancel = editDialog.getBtnCancel();

            switch (editSection) {
                case "cardName":
                    tvTitle.setText("Input new card name");
                    break;

                case "cardNotes":
                    tvTitle.setText("Input notes here");
                    break;
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDialog.dismiss();
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getEdited = etEdit.getText().toString();
                    CardList cardList = new CardList();
                    cardList.cardName = cardName;
                    cardList.cardType = title;
                    cardList.barcodeNumber = barcodeNumber;
                    cardList.cardIcon = logo;
                    cardList.cardBackground = backgroundColor;
                    cardList.cardNote = notes;
                    cardList.cardFrontView = frontView;
                    cardList.cardBackView = backView;

                    switch (editSection) {
                        case "cardName":
                            cardDB.updateCard(new CardList(id, cardList.cardType, getEdited, cardList.barcodeNumber, cardList.cardIcon, cardList.cardBackground, cardList.cardNote, cardList.cardFrontView, cardList.cardBackView));
                            tvCardName.setText(getEdited);
                            break;
                        case "cardNotes":
                            cardDB.updateCard(new CardList(id, getEdited, cardList.cardName, cardList.barcodeNumber, cardList.cardIcon, cardList.cardBackground, getEdited, cardList.cardFrontView, cardList.cardBackView));
                            tvNotes.setText(getEdited);
                            break;
                    }

                    editDialog.dismiss();
                }
            });
        }
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
            } else if (resultCode == 2) {
                // The user canceled the operation.
            }
        }

        if (requestCode == 5) {
            if (data!= null) {
                bmFrontView = (Bitmap) data.getExtras().get("data");
                imgFrontView.setImageBitmap(bmFrontView);

                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bmFrontView.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                frontView = Base64.encodeToString(b, Base64.DEFAULT);

                CardList cardList = new CardList();
                cardList.cardName = cardName;
                cardList.cardType = title;
                cardList.barcodeNumber = barcodeNumber;
                cardList.cardIcon = logo;
                cardList.cardBackground = backgroundColor;
                cardList.cardNote = notes;
                cardList.cardBackView = backView;
                cardDB.updateCard(new CardList(id, cardList.cardType, cardList.cardName, cardList.barcodeNumber, cardList.cardIcon, cardList.cardBackground, cardList.cardNote, frontView, cardList.cardBackView));

                getActivity().sendBroadcast(intent);
            }
        }

        if (requestCode == 6) {
            if (data != null) {
                bmBackView = (Bitmap) data.getExtras().get("data");
                imgBackView.setImageBitmap(bmBackView);

                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bmBackView.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                backView = Base64.encodeToString(b, Base64.DEFAULT);

                CardList cardList = new CardList();
                cardList.cardName = cardName;
                cardList.cardType = title;
                cardList.barcodeNumber = barcodeNumber;
                cardList.cardIcon = logo;
                cardList.cardBackground = backgroundColor;
                cardList.cardNote = notes;
                cardList.cardFrontView = frontView;
                cardDB.updateCard(new CardList(id, cardList.cardType, cardList.cardName, cardList.barcodeNumber, cardList.cardIcon, cardList.cardBackground, cardList.cardNote, cardList.cardFrontView, backView));

                getActivity().sendBroadcast(intent);
            }
        }
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void initGoogleServices() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(
                    getActivity(),
                    connectionCallbacks,
                    connectionFailedListener)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }
    }

    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnectionSuspended(int arg0) {
            //
        }

        @Override
        public void onConnected(Bundle arg0) {
            try {
                Location userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (userLocation != null) {
                    location = userLocation;
                } else {
                    LocationManager locationService =
                            (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location locationGPS = locationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (locationGPS != null) {
                        location = locationGPS;
                    } else {
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                        String provider = locationService.getBestProvider(criteria, true);
                        location = locationService.getLastKnownLocation(provider);
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    };

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
            //
        }
    };

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
