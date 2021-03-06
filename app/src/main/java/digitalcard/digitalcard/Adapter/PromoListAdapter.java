package digitalcard.digitalcard.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import digitalcard.digitalcard.Fragment.DetailPromoFragment;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.PromoList;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

public class PromoListAdapter extends RecyclerView.Adapter<PromoListAdapter.ViewHolder> {
    private Context context;
    private List<PromoList> promoLists;

    public PromoListAdapter(Context context, List<PromoList> promoLists) {
        this.context = context;
        this.promoLists = promoLists;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llPromoCard;
        ImageView ivPromoImage;
        TextView tvPromoTitle;
        ViewHolder(View itemView) {
            super(itemView);
            llPromoCard = itemView.findViewById(R.id.promo_card);
            ivPromoImage = itemView.findViewById(R.id.promo_image);
            tvPromoTitle = itemView.findViewById(R.id.promo_title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_promo_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PromoListAdapter.ViewHolder holder, int position) {
        final PromoList data = promoLists.get(position);

        Picasso.get().load(data.getBanner()).into(holder.ivPromoImage, new Callback() {
            @Override
            public void onSuccess() {
                holder.tvPromoTitle.setText(data.getTitle());
                holder.tvPromoTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                holder.tvPromoTitle.setSingleLine();
                holder.tvPromoTitle.setMarqueeRepeatLimit(10);
                holder.tvPromoTitle.setSelected(true);

                holder.llPromoCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailPromoFragment detailPromoFragment = new DetailPromoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Utilities.BUNDLE_PROMO_MERCHANT, data.getMerchant());
                        bundle.putString(Utilities.BUNDLE_PROMO_TITLE, data.getTitle());
                        bundle.putString(Utilities.BUNDLE_PROMO_BANNER, data.getBanner());
                        bundle.putString(Utilities.BUNDLE_PROMO_ORIGINAL_PRICE, data.getOriginalPrice());
                        bundle.putString(Utilities.BUNDLE_PROMO_DISCOUNTED_PRICE, data.getDiscountedPrice());
                        bundle.putString(Utilities.BUNDLE_PROMO_DESCRIPTION, data.getDescription());
                        bundle.putString(Utilities.BUNDLE_PROMO_LINK, data.getLink());
                        detailPromoFragment.setArguments(bundle);

                        ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.drag_view, detailPromoFragment, "DetailPromoFragment")
                                .commit();

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).getSlidingPanel().setPanelState(com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return promoLists.size();
    }
}
