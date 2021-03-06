package digitalcard.digitalcard.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import digitalcard.digitalcard.Fragment.TabKartu;
import digitalcard.digitalcard.Fragment.TabPromo;
import digitalcard.digitalcard.MainActivity;

/**
 * Created by andrewtanowijono on 09/04/18.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CustomPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabKartu tab1 = new TabKartu();
                return tab1;
            case 1:
                TabPromo tab2 = new TabPromo();
                return tab2;
//            case 2:
//                TabFragment3 tab3 = new TabFragment3();
//                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
