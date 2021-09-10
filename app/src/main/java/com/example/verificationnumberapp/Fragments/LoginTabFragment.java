package com.example.verificationnumberapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.verificationnumberapp.Activities.HomeActivity;
import com.example.verificationnumberapp.Models.UserModel;
import com.example.verificationnumberapp.R;
import com.example.verificationnumberapp.SendOTPActivity;
import com.example.verificationnumberapp.Utils.Constants;
import com.example.verificationnumberapp.Utils.SharedPrefs;
import com.example.verificationnumberapp.Utils.URLConnector;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginTabFragment extends Fragment {

    private EditText loginMobileNumberEditText, getLoginPasswordEditText;
    private ProgressBar loginProgressBar;
    private Button loginButton;
    private TextView loginRegisterText;
    private UserModel userModel;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_login, container, false);
        loginMobileNumberEditText = view.findViewById(R.id.login_mobile_phone);
        getLoginPasswordEditText = view.findViewById(R.id.login_password);
        loginProgressBar = view.findViewById(R.id.login_progressbar);
        loginButton = view.findViewById(R.id.login_btn);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = loginMobileNumberEditText.getText().toString().trim();
                String password = getLoginPasswordEditText.getText().toString().trim();

                if (!mobileNumber.isEmpty()) {
                  if(!password.isEmpty()){
                      if(checkValidateMobileNumber(mobileNumber)) {
                          logIn(mobileNumber, password);}
                      else{
                          loginMobileNumberEditText.setError("Please enter a valid mobile number");
                      }
                  }else {
                      getLoginPasswordEditText.setError("Please enter password!!");
                  }
                } else {
                    loginMobileNumberEditText.setError("Please enter your mobile number!!");
                }
            }
        });
    }

    private boolean checkValidateMobileNumber(String phoneNumber){
        //check length of number and if it starts with "09"
        if (phoneNumber.length() != 10 || phoneNumber.charAt(0)!= '0' || phoneNumber.charAt(1) != '9') {
            return false;
        }
        //matcher.find() will return true if there were any non numeric characters
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(phoneNumber);

        return !matcher.find();
    }
    // This function will be called in VerifyOTP Activity
    private void logIn(final String mobile, final String password) {
        loginProgressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        userModel = new UserModel();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.LOG_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if(code.equals("200")) {
                                JSONObject data =  jsonObject.getJSONObject("data");
                                userModel.setUser_id(data.getInt("id"));
                                userModel.setFirstName(data.getString("first_name").trim());
                                userModel.setMiddleName(data.getString("middle_name").trim());
                                userModel.setLastName(data.getString("last_name").trim());
                                userModel.setMobileNumber(data.getString("mobile").trim());
                                userModel.setPhoneNumber(data.getString("phone").trim());
                                userModel.setNationalNumber(data.getString("national_num").trim());
                                userModel.setAddress(data.getString("address").trim());
                                userModel.setBalance(data.getInt("balance"));
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                loginButton.setVisibility(View.VISIBLE);
                                loginProgressBar.setVisibility(View.GONE);

                                //for the first login
                                saveUsersLogIn(userModel.getUser_id(),
                                        userModel.getMobileNumber(),
                                        userModel.getFirstName()+" "+userModel.getMiddleName()+" " +userModel.getLastName(),
                                        userModel.getBalance());

                                SharedPrefs.save(getContext(), SharedPrefs.KEY_LOG_IN_STATE, true);
                                SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_WHO_AM_I, Constants.LOG_IN_I_AM_KEY);
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }else if(code.equals("400")){
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                loginButton.setVisibility(View.VISIBLE);
                                loginProgressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            loginButton.setVisibility(View.VISIBLE);
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "LogIn Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        loginButton.setVisibility(View.VISIBLE);
                        loginProgressBar.setVisibility(View.GONE);

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("password", password);

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

    // This function will be called in VerifyOTP Activity
    private void saveUsersLogIn(int user_id, String user_mobile, String user_name,int user_balance) {
        Log.e("userLogIn",
                "Balance :" + userModel.getBalance() + " "
                + userModel.getLastName());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_ID, user_id);
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_MOBILE, user_mobile);
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_NAME, user_name);
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_BALANCE, user_balance);
    }

}
