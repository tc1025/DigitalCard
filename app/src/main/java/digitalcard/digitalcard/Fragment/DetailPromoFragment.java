package digitalcard.digitalcard.Fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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

import com.squareup.picasso.Picasso;

import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

public class DetailPromoFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;

    LinearLayout btnBack;
    ImageView ivPromoImage;
    TextView tvTitle, tvPromoTitle, tvPromoOriginalPrice, tvPromoDiscountedPrice, tvPromoDescription, tvPromoLink;

    String title, promoTitle, promoBanner, promoOriginalPrice, promoDiscountedPrice, promoDescription, promoLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail_promo, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(true);
        }

        toolbar = rootView.findViewById(R.id.toolbar);
        btnBack = toolbar.getBtnBack();
        tvTitle = toolbar.getTxtTitle();
        ivPromoImage = rootView.findViewById(R.id.promo_image);
        tvPromoTitle = rootView.findViewById(R.id.promo_title);
        tvPromoOriginalPrice = rootView.findViewById(R.id.promo_original_price);
        tvPromoOriginalPrice.setPaintFlags(tvPromoOriginalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvPromoDiscountedPrice = rootView.findViewById(R.id.promo_discounted_price);
        tvPromoDescription = rootView.findViewById(R.id.promo_description);
        tvPromoLink = rootView.findViewById(R.id.promo_link);

        assert getArguments() != null;
        title = getArguments().getString(Utilities.BUNDLE_PROMO_MERCHANT);
        promoTitle = getArguments().getString(Utilities.BUNDLE_PROMO_TITLE);
        promoBanner = getArguments().getString(Utilities.BUNDLE_PROMO_BANNER);
        promoOriginalPrice = getArguments().getString(Utilities.BUNDLE_PROMO_ORIGINAL_PRICE);
        promoDiscountedPrice = getArguments().getString(Utilities.BUNDLE_PROMO_DISCOUNTED_PRICE);
        promoDescription = getArguments().getString(Utilities.BUNDLE_PROMO_DESCRIPTION);
        promoLink = getArguments().getString(Utilities.BUNDLE_PROMO_LINK);

        tvTitle.setText(title);
        tvPromoTitle.setText(promoTitle);
        Picasso.get().load(promoBanner).into(ivPromoImage);
        tvPromoOriginalPrice.setText(promoOriginalPrice);
        tvPromoDiscountedPrice.setText(promoDiscountedPrice);
        tvPromoDescription.setText(promoDescription);
        tvPromoLink.setText(promoLink);

        btnBack.setOnClickListener(this);
        tvPromoLink.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;

            case R.id.promo_link:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(promoLink));
                startActivity(intent);
                break;
        }
    }
}
