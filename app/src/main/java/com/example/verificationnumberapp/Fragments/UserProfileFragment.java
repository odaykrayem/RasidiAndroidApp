package com.example.verificationnumberapp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.verificationnumberapp.Activities.EnterScreenActivity;
import com.example.verificationnumberapp.R;
import com.example.verificationnumberapp.Utils.SharedPrefs;
import com.example.verificationnumberapp.Utils.URLConnector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UserProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    MaterialTextView mNameTxt;
    MaterialTextView mMobileNumberTxt;
    MaterialTextView mBalanceTxt;
    MaterialButton mLogoutBtn;
    int user_id, balance;
    String mobile,name;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID);
        name = SharedPrefs.getString(getContext(), SharedPrefs.KEY_USER_NAME);
        mobile = SharedPrefs.getString(getContext(), SharedPrefs.KEY_USER_MOBILE);
        balance = SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_BALANCE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mNameTxt = view.findViewById(R.id.user_name_txt);
        mMobileNumberTxt = view.findViewById(R.id.user_mobile_txt);
        mBalanceTxt = view.findViewById(R.id.user_balance_txt);
        mLogoutBtn = view.findViewById(R.id.logout_btn);
        // SwipeRefreshLayout
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
                getUserBalance();
                setUserData();
            }
        });

        return  view;
    }

    private void setUserData() {
        user_id = SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID);
        name = SharedPrefs.getString(getContext(), SharedPrefs.KEY_USER_NAME);
        mobile = SharedPrefs.getString(getContext(), SharedPrefs.KEY_USER_MOBILE);
        balance = SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_BALANCE);
        String  userName = name;
        mNameTxt.setText(userName);
        String  userMobile = mobile;
        mMobileNumberTxt.setText(userMobile);
        int userBalance = balance;
        mBalanceTxt.setText(String.valueOf(userBalance));
        if(userBalance < 100){
            mBalanceTxt.setTextColor(Color.parseColor("#ff0000"));
        }else{
            mBalanceTxt.setTextColor(Color.parseColor("#00ff00"));
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.save(getContext(), SharedPrefs.KEY_LOG_IN_STATE, false);
                Intent intent = new Intent(getContext(), EnterScreenActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        getUserBalance();
        setUserData();
    }
    private void getUserBalance() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.URL_USER_BALANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Log.e("ddd",jsonObject.toString());
                            if(code.equals("200")) {
                                int data =  jsonObject.getInt("data");
                                SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_BALANCE, data );
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }else if(code.equals("400")){
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "LogIn Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID)));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1500,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}