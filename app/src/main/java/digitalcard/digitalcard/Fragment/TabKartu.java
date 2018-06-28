package digitalcard.digitalcard.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import digitalcard.digitalcard.Database.CardDB;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static com.google.android.gms.wearable.DataMap.TAG;


public class TabKartu extends Fragment {
    View rootView;
    CardDB cardDB;
    List<CardList> cardListArrayList = new ArrayList<>();
    FragmentTransaction ft;

    RecyclerView rvCard;
    LinearLayout llNoCard;
    ListCardAdapter adapter;

    int cardCount = 0;

    //    Radio
    public final static String RADIO_DATASET_CHANGED = "DATASET_CHANGED";
    Radio radio;

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RADIO_DATASET_CHANGED);
        getActivity().getApplicationContext().registerReceiver(radio, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().getApplicationContext().unregisterReceiver(radio);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "not changed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radio = new Radio();
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

    private class Radio extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RADIO_DATASET_CHANGED)){
                //Notify dataset changed here
                cardListArrayList.clear();
                loadCard();
                fillCardList();
            }
        }
    }

    public void loadCard(){
        cardDB = new CardDB(getContext());
        List<CardList> cardLists =  cardDB.getAllCardList();
        if (!cardLists.isEmpty()) {
            for (CardList data : cardLists) {
                cardListArrayList.add(new CardList(data.getId(), data.getCardType(), data.getCardName(), data.getBarcodeNumber(), data.getCardIcon(), data.getCardBackground(), data.getCardNote(), data.getCardFrontView(), data.getCardBackView()));
            }
        }
        cardCount = cardListArrayList.size();
    }

    private void fillCardList(){
        adapter = new ListCardAdapter(getContext(), cardListArrayList);
        rvCard.setAdapter(adapter);
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
            Picasso.get().load(data.cardIcon).into(holder.cardIcon);
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
                    bundle.putString(Utilities.BUNDLE_CARD_LOGO, data.cardIcon);
                    bundle.putInt(Utilities.BUNDLE_CARD_BACKGROUND, data.cardBackground);
                    bundle.putString(Utilities.BUNDLE_CARD_NOTES, data.cardNote);
                    bundle.putString(Utilities.BUNDLE_CARD_FRONT_VIEW, data.cardFrontView);
                    bundle.putString(Utilities.BUNDLE_CARD_BACK_VIEW, data.cardBackView);
                    bundle.putInt("aaa", data.id);
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
