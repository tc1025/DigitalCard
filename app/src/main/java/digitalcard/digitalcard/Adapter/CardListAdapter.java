package digitalcard.digitalcard.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import digitalcard.digitalcard.Fragment.ExistingCardFragment;
import digitalcard.digitalcard.Fragment.RegistrationCardFragment;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Module.RegisterDialog;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

/**
 * Created by viks on 17/03/2018.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private Context context;
    private List<CardList> cardLists;
    RegisterDialog registerDialog;
    Button btn_yes, btn_no;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CardList data = cardLists.get(position);

//        if (data.getThumbnail() != 0)
//            Glide.with(context).load(data.getThumbnail()).into(holder.logo);

        holder.name.setText(data.cardCategory);
        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You choose " + data.cardCategory, Toast.LENGTH_SHORT).show();

                registerDialog = new RegisterDialog(context);

                registerDialog.show();
                if (registerDialog.isShowing()){
                    btn_no = registerDialog.getNoBtn();
                    btn_yes = registerDialog.getYesBtn();

                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RegistrationCardFragment registrationCardFragment = new RegistrationCardFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, data.cardCategory);
                            registrationCardFragment.setArguments(bundle);

                            ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.drag_view, registrationCardFragment, "AddCard")
                                    .addToBackStack(null)
                                    .commit();
                            registerDialog.dismiss();
                        }
                    });

                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ExistingCardFragment existingCardFragment = new ExistingCardFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, data.cardCategory);
                            existingCardFragment.setArguments(bundle);

                            ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.drag_view, existingCardFragment, "AddCard")
                                    .addToBackStack(null)
                                    .commit();
                            registerDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardLists.size();
    }
}
