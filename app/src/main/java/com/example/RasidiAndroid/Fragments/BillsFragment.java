package com.example.RasidiAndroid.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RasidiAndroid.Adapters.BillsListAdapter;
import com.example.RasidiAndroid.Models.BillModel;
import com.example.RasidiAndroid.R;
import com.example.RasidiAndroid.Utils.SharedPrefs;
import com.example.RasidiAndroid.Utils.URLConnector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private ArrayList<BillModel> billsModelArrayList;
    private BillsListAdapter billsListAdapter;
    private RecyclerView billsRV;
    private NestedScrollView nestedSVBills;
    private int user_id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout note;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);
        // creating a new array list.
        note = view.findViewById(R.id.note);

        billsModelArrayList = new ArrayList<>();

        billsRV = view.findViewById(R.id.idRVBills);
        nestedSVBills = view.findViewById(R.id.idNestedSVBills);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                getBillsData();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onRefresh() {
        getBillsData();
    }
    private void getBillsData() {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.URL_USER_BILLS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        billsModelArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            JSONArray bills =  jsonObject.getJSONArray("data");
                            if(code.equals("200")) {
                                for (int i = 0; i < bills.length(); i++) {
                                    JSONObject object = bills.getJSONObject(i);
                                    boolean isPaid = object.getString("paid").equals("1");
                                    // on below line we are extracting data from our json object.
                                    billsModelArrayList.add(new BillModel(
                                            object.getInt("id"),
                                            object.getString("bill_type"),
                                            object.getInt("value"),
                                            object.getString("payment_at"),
                                            isPaid));
                                    System.out.println("jsonObject" + object.toString());

                                }
                                if(billsModelArrayList.size()>0){
                                    note.setVisibility(View.GONE);
                                }
                            }else{
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);

                            // passing array list to our adapter class.
                            billsListAdapter = new BillsListAdapter(billsModelArrayList, getContext(), user_id);

                            // setting layout manager to our recycler view.
                            billsRV.setLayoutManager(new LinearLayoutManager(getContext()));

                            // setting adapter to our recycler view.
                            billsRV.setAdapter(billsListAdapter);

                        } catch (JSONException e) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), "Fail to get data.." + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            mSwipeRefreshLayout.setRefreshing(false);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                // handling on error listener method.
                Toast.makeText(getContext(), "Fail to get data.." + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", String.valueOf(user_id));
                return parameters;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our string request to queue
        queue.add(stringRequest);
    }
}

