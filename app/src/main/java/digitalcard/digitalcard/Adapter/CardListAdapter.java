package digitalcard.digitalcard.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import digitalcard.digitalcard.Fragment.ExistingMemberFragment;
import digitalcard.digitalcard.Fragment.RegistrationCardFragment;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_card_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CardList data = cardLists.get(position);
        int cardBackground = 0;
//        if (data.getThumbnail() != 0)
//            Glide.with(context).load(data.getThumbnail()).into(holder.logo);

        Picasso.get().load(data.cardIcon).into(holder.logo, new Callback() {
            @Override
            public void onSuccess() {
                holder.logo.setBackgroundColor(data.cardBackground);
                holder.name.setText(data.cardCategory);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.register_dialog_layout, null);

                TextView dialogMessage = alertLayout.findViewById(R.id.dialog_message);
                dialogMessage.setText("Do you have this member card");

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(alertLayout);
                alert.setPositiveButton("Yes, add existing member card", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExistingMemberFragment existingMemberFragment = new ExistingMemberFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Utilities.BUNDLE_CARD_CATEGORY, data.cardCategory);
                        bundle.putString(Utilities.BUNDLE_CARD_LOGO, data.cardIcon);
                        bundle.putInt(Utilities.BUNDLE_CARD_BACKGROUND, data.cardBackground);
                        existingMemberFragment.setArguments(bundle);

                        ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.drag_view, existingMemberFragment, "AddCard")
                                .addToBackStack(null)
                                .commit();
                    }
                });

                alert.setNegativeButton("No, register now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegistrationCardFragment registrationCardFragment = new RegistrationCardFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Utilities.BUNDLE_CARD_CATEGORY, data.cardCategory);
                        bundle.putString(Utilities.BUNDLE_CARD_LOGO, data.cardIcon);
                        bundle.putInt(Utilities.BUNDLE_CARD_BACKGROUND, data.cardBackground);
                        registrationCardFragment.setArguments(bundle);

                        ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.drag_view, registrationCardFragment, "AddCard")
                                .addToBackStack(null)
                                .commit();
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardLists.size();
    }
}
