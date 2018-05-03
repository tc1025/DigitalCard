package digitalcard.digitalcard;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Database.CardDB;
import digitalcard.digitalcard.Fragment.AddCardFragment;
import digitalcard.digitalcard.Fragment.CategoryCardFragment;
import digitalcard.digitalcard.Fragment.RegistrationCardFragment;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.PopUpPanel.PopUpPanelLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    CardDB cardDB;
    List<CardList> cardListArrayList = new ArrayList<>();

    LinearLayout mainLayout;
    PopUpPanelLayout popUpPanelLayout;
    ImageButton btnAdd, btnMenu;
    RecyclerView rvCard;

    AddCardFragment fAddCard;
    CategoryCardFragment fcategoryCardFragment;
    FragmentTransaction ft;
    private int flag_fragment = -1;
    public static int OPEN_FRAGMENT_ADD_CARDS = 1;
    public static int OPEN_FRAGMENT_RECORDS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popUpPanelLayout = findViewById(R.id.popup_layout);
        popUpPanelLayout.addPanelSlideListener(new PanelSlidingListener());
        popUpPanelLayout.setFadeOnClickListener(new FadeOnClickListener());

        fAddCard = new AddCardFragment();
        fcategoryCardFragment = new CategoryCardFragment();

        rvCard = findViewById(R.id.rv_card);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvCard.setLayoutManager(mLayoutManager);
        rvCard.setItemAnimator(new DefaultItemAnimator());

        btnAdd = findViewById(R.id.add_button);
        btnMenu = findViewById(R.id.menu_button);

        btnAdd.setOnClickListener(this);
        btnMenu.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.card_text));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.promo_text));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final digitalcard.digitalcard.Adapter.PagerAdapter adapter = new digitalcard.digitalcard.Adapter.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()) {
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        loadCard();
        fillCardList();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_button:
                Toast.makeText(this, "Add button", Toast.LENGTH_SHORT).show();
                ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.drag_view, fcategoryCardFragment, "AddNewCard").commit();
                popUpPanelLayout.setPanelState(PopUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.menu_button:
                Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drag_view);
        Log.e("frags" , String.valueOf(fragment));
        if (fragment == null) {
            this.finish();
            System.exit(0);
        } else if (fragment.getTag().equals("AddNewCard")) {
            super.onBackPressed();
            popUpPanelLayout.setPanelState(PopUpPanelLayout.PanelState.COLLAPSED);
        } else if (popUpPanelLayout.getPanelState() == PopUpPanelLayout.PanelState.EXPANDED) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                popUpPanelLayout.setPanelState(PopUpPanelLayout.PanelState.COLLAPSED);
            }
        } else {
            super.onBackPressed();
        }
    }

    public PopUpPanelLayout getSlidingPanel() {
        return popUpPanelLayout;
    }

    private class PanelSlidingListener implements PopUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            //Log.v(TAG, "onPanelSlide, offset: " + slideOffset);
        }

        @Override
        public void onPanelStateChanged(View panel, PopUpPanelLayout.PanelState previousState, PopUpPanelLayout.PanelState newState) {
            if (newState == PopUpPanelLayout.PanelState.COLLAPSED) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (flag_fragment == OPEN_FRAGMENT_ADD_CARDS) {
                    ft.remove(getSupportFragmentManager().findFragmentByTag("AddNewCard")).commit();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
//            InputMethodManager inputManager = (InputMethodManager) getSystemService(
//                    Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private class FadeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            popUpPanelLayout.setPanelState(PopUpPanelLayout.PanelState.COLLAPSED);
        }
    }


    public void loadCard(){
        cardDB = new CardDB(this);
        List<CardList> cardLists =  cardDB.getAllCardList();
        if (!cardLists.isEmpty()) {
            for (CardList data : cardLists) {
//                int cardId = data.getId();
                cardListArrayList.add(new CardList(data.getCardName(), data.getCardType(), data.getBarcodeNumber()));
            }
        }
    }

    private void fillCardList(){
        ListCardAdapter adapter = new ListCardAdapter(this , cardListArrayList);
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
                card = (LinearLayout) view.findViewById(R.id.card_card);
                cardName = (TextView) view.findViewById(R.id.card_name);
                cardIcon = (ImageView) view.findViewById(R.id.card_icon);
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
