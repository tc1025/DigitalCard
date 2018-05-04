package digitalcard.digitalcard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import digitalcard.digitalcard.Fragment.AddCardFragment;
import digitalcard.digitalcard.Fragment.CategoryCardFragment;
import digitalcard.digitalcard.PopUpPanel.PopUpPanelLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout mainLayout;
    SlidingUpPanelLayout slidingUpPanelLayout;
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

        slidingUpPanelLayout = findViewById(R.id.popup_layout);
        slidingUpPanelLayout.addPanelSlideListener(new PanelSlidingListener());
        slidingUpPanelLayout.setFadeOnClickListener(new FadeOnClickListener());

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
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("CardOverview")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        } else {
            super.onBackPressed();
        }
    }

    public SlidingUpPanelLayout getSlidingPanel() {
        return slidingUpPanelLayout;
    }

    private class PanelSlidingListener implements SlidingUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            //Log.v(TAG, "onPanelSlide, offset: " + slideOffset);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
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
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
}
