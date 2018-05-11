package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Adapter.CardListAdapter;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;

/**
 * Created by viks on 17/03/2018.
 */

public class CategoryCardFragment extends Fragment implements View.OnClickListener {
    View rootview;
    Toolbar toolbar;

    TextView txtTitle;
    LinearLayout btnBack, searchPanel;
    ListView lvTop5Card;
    RecyclerView rvAllCard;

    List<CardList> cardLists;
    CardListAdapter cardListAdapter;

    Boolean mKeyboardStatus = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_category_card, container, false);
        toolbar = rootview.findViewById(R.id.toolbar);

        txtTitle = toolbar.getTxtTitle();
        btnBack = toolbar.getBtnBack();     btnBack.setVisibility(View.GONE);
        searchPanel = rootview.findViewById(R.id.search_panel);
        lvTop5Card = rootview.findViewById(R.id.top_5_cards);
        rvAllCard = rootview.findViewById(R.id.all_card_list);

        cardLists = new ArrayList<>();
        cardListAdapter = new CardListAdapter(getContext(), cardLists);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAllCard.setLayoutManager(layoutManager);
        rvAllCard.setItemAnimator(new DefaultItemAnimator());
        rvAllCard.setAdapter(cardListAdapter);

        getData();

        return rootview;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    /*buat hide keyboard*/
    public void dismissKeyboard(){
        InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        mKeyboardStatus = false;
    }

    public void showKeyboard(){
        InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mKeyboardStatus = true;
    }

    private boolean isKeyboardActive(){
        return mKeyboardStatus;
    }

    @Override
    public void onClick(View v) {

    }

    public void getData(){
        int logo = 0;
        String[] cardCategory = new String[]{
            "Alfamart / Alfamidi",
                "Indomaret",
                "Starbucks",
                "Lottemart",
                "ACE Hardware",
        };

        CardList cardList;
        for (String aCardCategory : cardCategory) {
            cardList = new CardList(aCardCategory, logo);
            cardLists.add(cardList);
        }

        cardListAdapter.notifyDataSetChanged();
    }
}
