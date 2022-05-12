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
import com.example.RasidiAndroid.Adapters.TransferOpertaionAdapter;
import com.example.RasidiAndroid.Models.OperationModel;
import com.example.RasidiAndroid.R;
import com.example.RasidiAndroid.Utils.Constants;
import com.example.RasidiAndroid.Utils.SharedPrefs;
import com.example.RasidiAndroid.Utils.URLConnector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OperationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private ArrayList<OperationModel> operationModelArrayList;
    private TransferOpertaionAdapter transferOpertaionAdapter;
    private RecyclerView transferOperationRV;
    private NestedScrollView nestedSV;
    int user_id;
    String whoAmI;
    String isUserVerified;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID);
        whoAmI = SharedPrefs.getString(getContext(), SharedPrefs.KEY_USER_WHO_AM_I);
        isUserVerified  = "";
        if(whoAmI.equals(Constants.REGISTER_I_AM_KEY)){
            isUserVerified = "true";
        }else if(whoAmI.equals(Constants.LOG_IN_I_AM_KEY)){
            isUserVerified = "false";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_operations, container, false);
        // creating a new array list.
        operationModelArrayList = new ArrayList<>();
        note = view.findViewById(R.id.note);
        transferOperationRV = view.findViewById(R.id.idRVTransferOperation);
        nestedSV = view.findViewById(R.id.idNestedSV);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                getOperations();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getOperations() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.URL_USER_OPERATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        operationModelArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            JSONArray operations =  jsonObject.getJSONArray("data");
                            if(code.equals("200")) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                for (int i = 0; i < operations.length(); i++) {
                                JSONObject object = operations.getJSONObject(i);
                                boolean status = object.getString("status").equals("1");
                                // on below line we are extracting data from our json object.
                                operationModelArrayList.add(new OperationModel(
                                        object.getString("mobile"),
                                        object.getString("category_id"),
                                        status,
                                        object.getString("sim_type"),
                                        object.getString("created_at")));
                                        System.out.println("jsonObject" + object.toString());
                            }
                            if(operationModelArrayList.size()>0){
                                note.setVisibility(View.GONE);
                            }
                        }else{
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);

                            // passing array list to our adapter class.
                            transferOpertaionAdapter = new TransferOpertaionAdapter(operationModelArrayList, getContext());

                            // setting layout manager to our recycler view.
                            transferOperationRV.setLayoutManager(new LinearLayoutManager(getContext()));

                            // setting adapter to our recycler view.
                            transferOperationRV.setAdapter(transferOpertaionAdapter);

                        } catch (JSONException e) {
                            mSwipeRefreshLayout.setRefreshing(false);

                            Toast.makeText(getContext(), "Fail to get data.." + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("error1:::" + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
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
                System.out.println("error2" + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", String.valueOf(user_id));
                parameters.put("verify", isUserVerified);
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

    @Override
    public void onRefresh() {
        getOperations();
    }
}
