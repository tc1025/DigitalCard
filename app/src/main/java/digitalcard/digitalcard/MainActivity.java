package digitalcard.digitalcard;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import digitalcard.digitalcard.Adapter.CustomPagerAdapter;
import digitalcard.digitalcard.Fragment.AccountFragment;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import digitalcard.digitalcard.Fragment.CategoryCardFragment;
import digitalcard.digitalcard.Fragment.MenuFragment;
import digitalcard.digitalcard.Fragment.SettingsFragment;
import digitalcard.digitalcard.Util.Utilities;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mainLayout, btnAccount, btnSettings, btnInfo;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ImageButton btnMenu;
    FloatingActionButton btnAdd;

    AccountFragment accountFragment;
    SettingsFragment settingsFragment;
    CategoryCardFragment categoryCardFragment;
    MenuFragment menuFragment;
    FragmentTransaction ft;
    private int flag_fragment = -1;
    public static int OPEN_FRAGMENT_ADD_CARDS = 1;
    public static int OPEN_FRAGMENT_RECORDS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingUpPanelLayout = findViewById(R.id.popup_layout);
        slidingUpPanelLayout.addPanelSlideListener(new PanelSlidingListener());
        slidingUpPanelLayout.setFadeOnClickListener(new FadeOnClickListener());

        accountFragment = new AccountFragment();
        settingsFragment = new SettingsFragment();
        categoryCardFragment = new CategoryCardFragment();
        menuFragment = new MenuFragment();

        btnAdd = findViewById(R.id.add_button);
//        btnMenu = findViewById(R.id.menu_button);

        btnAdd.setOnClickListener(this);
//        btnMenu.setOnClickListener(this);

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.card_text));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.promo_text));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final CustomPagerAdapter adapter = new CustomPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()) {
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        btnAdd.show();
                        break;

                    default:
                        btnAdd.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                Log.e("KEVIN", "Backstack Count = " + getSupportFragmentManager().getBackStackEntryCount());
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_account:
                ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.drag_view, accountFragment, "Account").commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.item_settings:
                ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.drag_view, settingsFragment, "Settings").commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.drag_view, categoryCardFragment, "AddNewCard").commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;

//            case R.id.menu_button:
//                ft = getSupportFragmentManager().beginTransaction();
//                ft.addToBackStack(null);
//                ft.replace(R.id.drag_view, menuFragment, "Menu").commit();
//                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                break;
//            case R.id.menu_button:
//                Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drag_view);
        if (fragment == null) {
            Log.e("KEVIN", "FRAGMENT NULL AND FINISH ACTIVITY");
            this.finish();
            System.exit(0);
        } else if (fragment.getTag().equals("AddNewCard")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("Menu")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("CardOverview")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("DetailPromoFragment")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("Account")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragment.getTag().equals("Settings")) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

        getSupportFragmentManager().popBackStack();

    }

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
                getSupportFragmentManager().popBackStack();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                if (flag_fragment == OPEN_FRAGMENT_ADD_CARDS) {
//                    ft.remove(getSupportFragmentManager().findFragmentByTag("AddNewCard")).commit();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }
            }
        }
    }

    private class FadeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
}
