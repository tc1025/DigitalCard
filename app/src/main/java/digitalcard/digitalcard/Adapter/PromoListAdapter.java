package digitalcard.digitalcard.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Model.PromoList;
import digitalcard.digitalcard.R;

public class PromoListAdapter extends RecyclerView.Adapter<PromoListAdapter.ViewHolder> {
    Context context;
    List<PromoList> promoLists;

    public PromoListAdapter(Context context, List<PromoList> promoLists) {
        this.context = context;
        this.promoLists = promoLists;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
    public void onBindViewHolder(@NonNull PromoListAdapter.ViewHolder holder, int position) {
        final PromoList data = promoLists.get(position);

        holder.tvPromoTitle.setText(data.getTitle());
        holder.llPromoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You choose " + data.getMerchant() + " promo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return promoLists.size();
    }
}
