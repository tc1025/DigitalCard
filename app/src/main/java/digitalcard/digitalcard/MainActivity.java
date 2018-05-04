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
import digitalcard.digitalcard.Fragment.CardOverViewFragment;
import digitalcard.digitalcard.Fragment.CategoryCardFragment;
import digitalcard.digitalcard.Fragment.RegistrationCardFragment;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.PopUpPanel.PopUpPanelLayout;
import digitalcard.digitalcard.Util.Utilities;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout mainLayout;
    PopUpPanelLayout popUpPanelLayout;
    ImageButton btnAdd, btnMenu;

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
        } else if (fragment.getTag().equals("CardOverview")) {
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
}
