package digitalcard.digitalcard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import digitalcard.digitalcard.Fragment.ExistingCardFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import digitalcard.digitalcard.Fragment.CategoryCardFragment;
import digitalcard.digitalcard.Util.Utilities;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout mainLayout;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ImageButton btnMenu;
    FloatingActionButton btnAdd;

    ExistingCardFragment fAddCard;
    CategoryCardFragment fcategoryCardFragment;
    FragmentTransaction ft;
    private int flag_fragment = -1;
    public static int OPEN_FRAGMENT_ADD_CARDS = 1;
    public static int OPEN_FRAGMENT_RECORDS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingUpPanelLayout = findViewById(R.id.popup_layout);
        slidingUpPanelLayout.addPanelSlideListener(new PanelSlidingListener());
        slidingUpPanelLayout.setFadeOnClickListener(new FadeOnClickListener());

        fAddCard = new ExistingCardFragment();
        fcategoryCardFragment = new CategoryCardFragment();

        btnAdd = findViewById(R.id.add_button);
//        btnMenu = findViewById(R.id.menu_button);

        btnAdd.setOnClickListener(this);
//        btnMenu.setOnClickListener(this);

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
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
                if (tabLayout.getSelectedTabPosition() == 1)
                    btnAdd.setVisibility(View.GONE);
                else btnAdd.setVisibility(View.VISIBLE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button_menu, menu);

        if(menu instanceof MenuBuilder){

            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_account:
                break;
            case R.id.item_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
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
//            case R.id.menu_button:
//                Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
//                break;
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
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("CardOverview")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("DetailPromoFragment")) {
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

//    private void showMenu(View view){
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        popupMenu.setOnMenuItemClickListener();
//    }

    private void validateAllPermissions() {
        String[] permissions = Utilities.ALL_PERMISSIONS;

        boolean allPermissionGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                allPermissionGranted = false;
            }
        }

        if (!allPermissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, Utilities.VP_ALL);
            }
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
