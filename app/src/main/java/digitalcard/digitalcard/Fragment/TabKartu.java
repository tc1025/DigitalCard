package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Database.CardDB;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;


public class TabKartu extends Fragment {
    View rootView;
    CardDB cardDB;
    List<CardList> cardListArrayList = new ArrayList<>();
    FragmentTransaction ft;

    RecyclerView rvCard;
    LinearLayout llNoCard;
    ListCardAdapter adapter;

    int cardCount = 0;

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

        cardListArrayList.clear();
        loadCard();
        fillCardList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_kartu, container, false);

        rvCard = rootView.findViewById(R.id.rv_card);
        llNoCard = rootView.findViewById(R.id.ll_no_card);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rvCard.setLayoutManager(mLayoutManager);
        rvCard.setItemAnimator(new DefaultItemAnimator());

        loadCard();
        fillCardList();

        return rootView;
    }

    public void loadCard(){
        cardDB = new CardDB(getContext());
        List<CardList> cardLists =  cardDB.getAllCardList();
        if (!cardLists.isEmpty()) {
            for (CardList data : cardLists) {
//                int cardId = data.getId();
                cardListArrayList.add(new CardList(data.getId(), data.getCardType(), data.getCardName(), data.getBarcodeNumber(), data.getCardIcon(), data.getCardBackground()));
            }
        }
        cardCount = cardListArrayList.size();
    }

    private void fillCardList(){
        adapter = new ListCardAdapter(getContext(), cardListArrayList);
        rvCard.setAdapter(adapter);
        Log.e("asd", cardListArrayList.size() + " size");
    }

    private class ListCardAdapter extends RecyclerView.Adapter<ListCardAdapter.MyViewHolder>{
        private Context mContext;
        private List<CardList> cardLists;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView cardName;
            ImageView cardIcon;
            LinearLayout card;

            MyViewHolder(View view) {
                super(view);
                card = view.findViewById(R.id.card_card);
                cardName = view.findViewById(R.id.card_name);
                cardIcon = view.findViewById(R.id.card_icon);
            }
        }

        ListCardAdapter(Context mContext, List<CardList> cardLists) {
            this.mContext = mContext;
            this.cardLists = cardLists;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.itemview_card_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final CardList data = cardLists.get(position);

            holder.cardName.setText(data.getCardName());
            holder.cardName.setSingleLine(true);
            holder.cardIcon.setImageResource(data.cardIcon);
            holder.cardIcon.setBackgroundColor(data.cardBackground);
            holder.card.setBackgroundColor(data.cardBackground);

            switch (data.cardType) {
                case "Starbucks":
                    holder.cardName.setTextColor(0xFFFFFFFF);
                    break;
                case "Lottemart":
                    holder.cardName.setTextColor(0xFFFFFFFF);
                    break;
            }

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CardOverViewFragment cardOverViewFragment = new CardOverViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Utilities.BUNDLE_CARD_ID, position);
                    bundle.putString(Utilities.BUNDLE_CARD_NAME, data.cardName);
                    bundle.putString(Utilities.BUNDLE_CARD_CATEGORY, data.cardType);
                    bundle.putString(Utilities.BUNDLE_BARCODE_NUMBER, data.barcodeNumber);
                    bundle.putInt(Utilities.BUNDLE_CARD_LOGO, data.cardIcon);
                    bundle.putInt(Utilities.BUNDLE_CARD_BACKGROUND, data.cardBackground);
                    cardOverViewFragment.setArguments(bundle);

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.drag_view, cardOverViewFragment, "CardOverview").commit();

                    if (getContext() instanceof MainActivity) {
                        ((MainActivity) getContext()).getSlidingPanel().setPanelState(com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if (cardListArrayList.size() == 0) {
                rvCard.setVisibility(View.GONE);
                llNoCard.setVisibility(View.VISIBLE);
            } else {
                rvCard.setVisibility(View.VISIBLE);
                llNoCard.setVisibility(View.GONE);
            }
            return cardListArrayList.size();
        }
    }
}
