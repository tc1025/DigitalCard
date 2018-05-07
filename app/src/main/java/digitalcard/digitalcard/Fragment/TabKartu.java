package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.net.Uri;
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
import digitalcard.digitalcard.PopUpPanel.PopUpPanelLayout;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;


public class TabKartu extends Fragment {
    View rootView;
    CardDB cardDB;
    List<CardList> cardListArrayList = new ArrayList<>();
    FragmentTransaction ft;

    RecyclerView rvCard;
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
                cardListArrayList.add(new CardList(data.getId(), data.getCardName(), data.getCardType(), data.getBarcodeNumber()));
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

        public ListCardAdapter(Context mContext, List<CardList> cardLists) {
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

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Card name : " + data.cardName
                            + ", card type : " + data.cardType
                            + ", card barcode : " + data.barcodeNumber, Toast.LENGTH_SHORT).show();

                    CardOverViewFragment cardOverViewFragment = new CardOverViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Utilities.BUNNDLE_CARD_ID, position);
                    bundle.putString(Utilities.BUNNDLE_CARD_NAME, data.cardName);
                    bundle.putString(Utilities.BUNNDLE_CARD_CATEGORY, data.cardType);
                    bundle.putString(Utilities.BUNNDLE_BARCODE_NUMBER, data.barcodeNumber);
                    cardOverViewFragment.setArguments(bundle);

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.drag_view, cardOverViewFragment, "CardOverview").commit();

                    if (getContext() instanceof MainActivity) {
                        ((MainActivity) getContext()).getSlidingPanel().setPanelState(PopUpPanelLayout.PanelState.EXPANDED);
                    }
//                    CardOverviewFragments cardOverviewFragments = new CardOverviewFragments();
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("textNama" , data.getName());
//                    bundle.putInt("background" , data.getBackground());
//                    bundle.putInt("logo" , data.getThumbnail());
//                    bundle.putInt("id" , position);
//                    bundle.putString("textNamaOwner" , data.getOwnername());
//                    bundle.putString("serialNumber" , data.getSerialNumber());
//                    bundle.putString("note" , data.getNote());
//                    bundle.putString("depan" , data.getFotoDepan());
//                    bundle.putString("belakang" , data.getFotoBelakang());
//                    cardOverviewFragments.setArguments(bundle);
//
//                    ft = getSupportFragmentManager().beginTransaction();
//                    ft.addToBackStack(null);
//                    ft.replace(R.id.drag_view, cardOverviewFragments, "AddCards").commit();
//
//                    //Expand sliding panel and set status
//                    mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                    mSlidingOpen = true;
                }
            });

        }

        @Override
        public int getItemCount() {
//            RelativeLayout kosong = (RelativeLayout) findViewById(R.id.kosong);
//            LinearLayout isi = (LinearLayout) findViewById(R.id.isi);
//
//            if(cardListArrayList.size() == 0){
//                kosong.setVisibility(View.VISIBLE);
//                isi.setVisibility(View.GONE);
//                testadd.setVisibility(View.GONE);
//            }else {
//                isi.setVisibility(View.VISIBLE);
//                kosong.setVisibility(View.GONE);
//                testadd.setVisibility(View.VISIBLE);
//            }
            return cardListArrayList.size();
        }
    }
}
