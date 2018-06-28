package digitalcard.digitalcard.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import digitalcard.digitalcard.Adapter.PromoListAdapter;
import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Model.PromoList;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static com.google.android.gms.wearable.DataMap.TAG;

public class TabPromo extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    View rootView;

    RecyclerView rvPromo;
    LinearLayout llNoPromo;
    SwipeRefreshLayout swipeRefreshLayout;

    List<PromoList> promoLists;
    PromoListAdapter promoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_promo, container, false);

        promoLists = new ArrayList<>();
        promoListAdapter = new PromoListAdapter(getContext(), promoLists);

        rvPromo = rootView.findViewById(R.id.rv_promo);
        llNoPromo = rootView.findViewById(R.id.ll_no_promo);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.offsetChildrenVertical(LinearLayout.VERTICAL);
        rvPromo.setLayoutManager(mLayoutManager);
        rvPromo.setItemAnimator(new DefaultItemAnimator());
        rvPromo.setAdapter(promoListAdapter);

//        loadCardServer();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadCardServer();
            }
        });

        return rootView;
    }

    public void loadCardServer() {
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utilities.URL_PROMO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object1 = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = object1.getJSONArray("promo");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Log.e("data", "card : " + object.getString("merchant") + ", logo : " + object.getString("banner"));
                                promoLists.add(new PromoList(object.getString("merchant"),
                                        object.getString("title"),
                                        object.getString("banner"),
                                        object.getString("originalPrice"),
                                        object.getString("discountedPrice"),
                                        object.getString("description"),
                                        object.getString("link")));
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                        finally {
                            promoListAdapter.notifyDataSetChanged();

                            if (promoListAdapter.getItemCount() == 0) {
                                llNoPromo.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                            } else {
                                llNoPromo.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e(TAG, error.toString()); }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        promoLists.clear();
        promoListAdapter.notifyDataSetChanged();
        loadCardServer();
    }
}
