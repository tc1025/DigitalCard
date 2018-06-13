package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

public class DetailPromoFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;

    LinearLayout btnBack;
    ImageView ivPromoImage;
    TextView tvTitle, tvPromoTitle, tvPromoDescription;

    String title, promoTitle, promoDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail_promo, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        btnBack = toolbar.getBtnBack();
        tvTitle = toolbar.getTxtTitle();
        ivPromoImage = rootView.findViewById(R.id.promo_image);
        tvPromoTitle = rootView.findViewById(R.id.promo_title);
        tvPromoDescription = rootView.findViewById(R.id.promo_description);

        assert getArguments() != null;
        title = getArguments().getString(Utilities.BUNDLE_PROMO_MERCHANT);
        promoTitle = getArguments().getString(Utilities.BUNDLE_PROMO_TITLE);
        promoDescription = getArguments().getString(Utilities.BUNDLE_PROMO_DESCRIPTION);

        tvTitle.setText(title);
        tvPromoTitle.setText(promoTitle);
        tvPromoDescription.setText(promoDescription);

        btnBack.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }
}
