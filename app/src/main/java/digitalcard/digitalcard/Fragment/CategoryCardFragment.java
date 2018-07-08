package digitalcard.digitalcard.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import digitalcard.digitalcard.Adapter.CardListAdapter;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static com.google.android.gms.wearable.DataMap.TAG;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_category_card, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(true);
        }

        toolbar = rootview.findViewById(R.id.toolbar);

        txtTitle = toolbar.getTxtTitle();
        txtTitle.setText(getResources().getString(R.string.add_new_card_text));
        btnBack = toolbar.getBtnBack();
        btnBack.setVisibility(View.GONE);
        searchPanel = rootview.findViewById(R.id.search_panel);
        lvTop5Card = rootview.findViewById(R.id.top_5_cards);
        rvAllCard = rootview.findViewById(R.id.all_card_list);

        cardLists = new ArrayList<>();
        cardListAdapter = new CardListAdapter(getContext(), cardLists);

        txtTitle.setText(getResources().getString(R.string.add_new_card_text));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAllCard.setLayoutManager(layoutManager);
        rvAllCard.setItemAnimator(new DefaultItemAnimator());
        rvAllCard.setAdapter(cardListAdapter);

        loadCardServer();
        return rootview;
    }

    public void loadCardServer() {
        cardLists.add(new CardList("Other", null, 0xFFFFFFFF));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utilities.URL_CARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object1 = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = object1.getJSONArray("merchant");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CardList cardList = new CardList();
                                cardList.getCardName();

                                JSONObject object = jsonArray.getJSONObject(i);
                                Log.e("data", "card : " + object.getString("name") + ", logo : " + object.getString("logo"));
                                cardLists.add(new CardList(object.getString("name"), object.getString("logo"), object.getInt("background")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            cardListAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootview.getWindowToken(), 0);
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {

    }
}
