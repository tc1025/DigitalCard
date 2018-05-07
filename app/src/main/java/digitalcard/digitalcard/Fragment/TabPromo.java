package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Adapter.PromoListAdapter;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Model.PromoList;
import digitalcard.digitalcard.R;

public class TabPromo extends Fragment {
    View rootView;

    RecyclerView rvPromo;

    List<PromoList> promoLists;
    PromoListAdapter promoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_promo, container, false);

        promoLists = new ArrayList<>();
        promoListAdapter = new PromoListAdapter(getContext(), promoLists);

        rvPromo = rootView.findViewById(R.id.rv_promo);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.offsetChildrenVertical(LinearLayout.VERTICAL);
        rvPromo.setLayoutManager(mLayoutManager);
        rvPromo.setItemAnimator(new DefaultItemAnimator());
        rvPromo.setAdapter(promoListAdapter);

        getData();

        return rootView;
    }

    public void getData(){
        String[] promoMerchant = new String[]{
                "Alfamart / Alfamidi",
                "Indomaret",
                "Starbucks",
                "Lottemart",
                "ACE Hardware",
        };

        String[] promoTitle = new String[]{
                "Promo special di tanggal 21 April",
                "Hari Kartini untuk ibu di Indomaret",
                "Disc. 50% for every coffee",
                "Monthly catalog",
                "Great sale on ACE",
        };

        String[] promoLink = new String[]{
                "http://alfamartku.com/promo",
                "http://www.indomaret.com/utama/promosi/super-hemat.html",
                "http://www.starbucks.co.id/",
                "http://www.lottemart.co.id/",
                "http://www.acehardware.com/home/index.jsp",
        };

        String[] promoDescription = new String[]{
                "Alfamart / Alfamidi description",
                "Indomaret description",
                "Starbucks description",
                "Lottemart description",
                "ACE Hardware description",
        };

        int countPromo = promoMerchant.length;
        PromoList promoList;

        for (int i = 0; i < countPromo; i++) {
            promoList = new PromoList(promoMerchant[i], promoTitle[i], promoLink[i], promoDescription[i]);
            promoLists.add(promoList);
        }

        promoListAdapter.notifyDataSetChanged();
    }
}
