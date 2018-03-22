package digitalcard.digitalcard.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import digitalcard.digitalcard.Fragment.AddCardFragment;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

/**
 * Created by viks on 17/03/2018.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private Context context;
    private List<CardList> cardLists;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView logo;
        public TextView name;
        LinearLayout panel;
        ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.card_logo);
            name = itemView.findViewById(R.id.card_category);
            panel = itemView.findViewById(R.id.panel);
        }
    }

    public CardListAdapter(Context context, List<CardList> cardLists) {
        this.context = context;
        this.cardLists = cardLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CardList data = cardLists.get(position);

        if (data.getThumbnail() != 0)
            Glide.with(context).load(data.getThumbnail()).into(holder.logo);

        holder.name.setText(data.getCardCategory());
        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You choose " + data.getCardCategory(), Toast.LENGTH_SHORT).show();

                AddCardFragment addCardFragment = new AddCardFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, data.getCardCategory());
                addCardFragment.setArguments(bundle);

                ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drag_view, addCardFragment, "AddCard")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardLists.size();
    }
}
