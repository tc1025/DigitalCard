package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

public class LocationFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;

    LinearLayout btnBack;
    TextView tvTitle;
    MapView merchantLocation;
    GoogleMap mGoogleMap;

    Location location;
    String title;
    Double latitude, longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location, container, false);
        toolbar = rootView.findViewById(R.id.toolbar);

        merchantLocation = rootView.findViewById(R.id.merchant_location);
        assert getArguments() != null;
        latitude = getArguments().getDouble(Utilities.BUNNDLE_LOCATION_LATITUDE);
        longitude = getArguments().getDouble(Utilities.BUNNDLE_LOCATION_LONGITUDE);
        title = getArguments().getString(Utilities.BUNNDLE_CARD_CATEGORY);

        btnBack = toolbar.getBtnBack();
        tvTitle = toolbar.getTxtTitle();

        tvTitle.setText(title);
        btnBack.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        merchantLocation.onCreate(savedInstanceState);
        merchantLocation.onResume();

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int statusCode = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (statusCode == ConnectionResult.SUCCESS) {
            merchantLocation.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    mGoogleMap.setBuildingsEnabled(true);
                    mGoogleMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
                    mGoogleMap.setOnMapLoadedCallback(onMapLoadedCallback);
                    try {
                        mGoogleMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            switch (statusCode) {
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                    int childCount = merchantLocation.getChildCount();
                    Log.e("tommy", "Map childCount: " + childCount);
                    Drawable bgGradient = getResources().getDrawable(R.drawable.gradient_background, null);
                    merchantLocation.setBackground(bgGradient);

                    /// Add child view
                    TextView valueTV = new TextView(getActivity());
                    valueTV.setText("Unable to display map. Please update your google play service to see map");
                    valueTV.setTextColor(0xffffffff);
//					valueTV.setId(5);
                    valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    valueTV.setGravity(Gravity.CENTER);
                    merchantLocation.addView(valueTV);
                    merchantLocation.getChildAt(0).setVisibility(View.GONE);

                    merchantLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String appPackageName = "com.google.android.gms";
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });
                    break;
                default:
                    Log.e("tommy", "Load map error. Google Play Service status number " + statusCode);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener myLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            Location location = mGoogleMap.getMyLocation();

            if (location == null) {
                // Something's wrong, internet connection or gps is not active
                // if so, inform the user with a dialog
                showDialogWarningMyLocation(getView());

                return false;
            }

            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition =
                    new CameraPosition.Builder().target(latlng).zoom(16).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            return false;
        }
    };

    GoogleMap.OnMapLoadedCallback onMapLoadedCallback = new GoogleMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            if (latitude == 0 || longitude== 0) {
                return;
            }

            LatLng latlng = new LatLng(latitude, longitude);

            CameraPosition cameraPosition =
                    new CameraPosition.Builder().target(latlng).zoom(14).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    };

    private void showDialogWarningMyLocation(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_information, null);
        popupView.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);

        TextView popupHeader = (TextView) popupView.findViewById(R.id.popup_header);
        TextView popupContent = (TextView) popupView.findViewById(R.id.popup_content);
        View btnNet = popupView.findViewById(R.id.btn_neutral);

        popupHeader.setText(R.string.dialog_header_location_null);
        popupContent.setText(R.string.dialog_content_location_null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        btnNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
}
