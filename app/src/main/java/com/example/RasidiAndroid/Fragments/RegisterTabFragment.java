package com.example.RasidiAndroid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RasidiAndroid.Models.UserModel;
import com.example.RasidiAndroid.R;
import com.example.RasidiAndroid.SendOTPActivity;
import com.example.RasidiAndroid.Utils.Constants;
import com.example.RasidiAndroid.Utils.SharedPrefs;
import com.example.RasidiAndroid.Utils.URLConnector;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterTabFragment extends Fragment {
    private EditText reg_first_name, reg_middle_name,
            reg_last_name, reg_mobile_number,
            reg_phone_number, reg_national_number, reg_address,
            reg_password, reg_password_confirm;

    private Button register_btn;
    private TextInputLayout txtInLayoutRegPassword;
    private ProgressBar progressBar;
    private UserModel userModel;

    @Nullable
    @Override
    //Hint :: Phone Number for Land phone //
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_register, container, false);
        reg_first_name = view.findViewById(R.id.reg_first_name);
        reg_middle_name = view.findViewById(R.id.reg_middle_name);
        reg_last_name = view.findViewById(R.id.reg_last_name);
        reg_mobile_number = view.findViewById(R.id.reg_mobile_number);
        reg_phone_number = view.findViewById(R.id.reg_phone_number);
        reg_national_number = view.findViewById(R.id.reg_national_number);
        reg_address = view.findViewById(R.id.reg_address);
        reg_password = view.findViewById(R.id.reg_password);
        reg_password_confirm = view.findViewById(R.id.reg_password_confirm);
        register_btn = view.findViewById(R.id.reg_register_btn);
        txtInLayoutRegPassword = view.findViewById(R.id.txtInLayoutRegPassword);
        progressBar = view.findViewById(R.id.register_progressbar);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (validateInput()) {
                    Toast.makeText(getContext(), "check done", Toast.LENGTH_SHORT).show();
                    checkIfNumberExisted();

                } else {
                    progressBar.setVisibility(View.GONE);
                    register_btn.setVisibility(View.VISIBLE);
                    return;
                }

            }
        });
    }

    private void checkIfNumberExisted() {

        String firstName = reg_first_name.getText().toString().trim();
        String middleName = reg_middle_name.getText().toString().trim();
        String lastName = reg_last_name.getText().toString().trim();
        String mobileNumber = reg_mobile_number.getText().toString().trim();
        String phoneNumber = reg_phone_number.getText().toString().trim();
        String nationalNumber = reg_national_number.getText().toString().trim();
        String address = reg_address.getText().toString().trim();
        String password = reg_password.getText().toString().trim();

        userModel = new UserModel(firstName,
                middleName, lastName, mobileNumber, phoneNumber, nationalNumber, address, password);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.BEFORE_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            progressBar.setVisibility(View.GONE);
                            register_btn.setVisibility(View.VISIBLE);
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
                                registerUser();
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                            } else if (code.equals("400")) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Try Register Error", "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                            Toast.makeText(getContext(), "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            register_btn.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Response", "Error Response ! " + error.toString()
                                + "\nCause: " + error.getCause()
                                + "\nmessage: " + error.getMessage());
                        Toast.makeText(getContext(), "Register Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        register_btn.setVisibility(View.VISIBLE);
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", userModel.getMobileNumber());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void registerUser() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            progressBar.setVisibility(View.GONE);
                            register_btn.setVisibility(View.VISIBLE);
                            if (code.equals("200")) {
                                String message = jsonObject.getString("message");
                                int uid = jsonObject.getInt("data");
                                Log.e("id", uid + "");
                                System.out.println(message + " " + uid);
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                                Log.e("register info", userModel.getUser_id() + "");
                                saveUsersRegister(jsonObject.getInt("data"), userModel.getMobileNumber(),
                                        userModel.getFirstName() + " " + userModel.getMiddleName() + " " + userModel.getLastName(),
                                        userModel.getBalance());

                                Intent intent = new Intent(getActivity(), SendOTPActivity.class);
                                intent.putExtra(Constants.USER_MOBILE_KEY, userModel.getMobileNumber().substring(1));

                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error1", "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                            Toast.makeText(getContext(), "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            register_btn.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Register Error2! " + error.toString()
                                + "\nCause: " + error.getCause()
                                + "\nmessage: " + error.getMessage()
                        );
                        Toast.makeText(getContext(), "Register Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        register_btn.setVisibility(View.VISIBLE);
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", userModel.getMobileNumber());
                params.put("first_name", userModel.getFirstName());
                params.put("middle_name", userModel.getMiddleName());
                params.put("last_name", userModel.getLastName());
                params.put("phone", userModel.getPhoneNumber());
                params.put("national_num", userModel.getNationalNumber());
                params.put("address", userModel.getAddress());
                params.put("password", userModel.getPassword());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void saveUsersRegister(int user_id, String user_mobile, String user_name, int user_balance) {
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_ID, user_id);
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_MOBILE, user_mobile);
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_NAME, user_name);
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_BALANCE, user_balance);

    }

    private boolean validateInput() {
        progressBar.setVisibility(View.VISIBLE);
        register_btn.setVisibility(View.GONE);
        if (reg_first_name.getText().toString().trim().isEmpty()) {

            reg_first_name.setError("Please fill out this field");
            progressBar.setVisibility(View.VISIBLE);
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (!reg_first_name.getText().toString().trim().matches(regex) || reg_first_name.getText().toString().trim().length() < 3) {
                reg_first_name.setError("Please fill out this field with valid first name > 3 letters");
                return false;
            }
        }
        if (reg_middle_name.getText().toString().trim().isEmpty()) {

            reg_middle_name.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (!reg_middle_name.getText().toString().trim().matches(regex) || reg_middle_name.getText().toString().trim().length() < 3) {
                reg_middle_name.setError("Please fill out this field with a valid mid name > 3 letters");
                return false;
            }
        }
        if (reg_last_name.getText().toString().trim().isEmpty()) {

            reg_last_name.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (!reg_last_name.getText().toString().trim().matches(regex) || reg_last_name.getText().toString().trim().length() < 3) {
                reg_last_name.setError("Please fill out this field with a valid last name > 3 letters");
                return false;
            }
        }
        if (reg_mobile_number.getText().toString().trim().isEmpty()) {

            reg_mobile_number.setError("Please fill out this field");
            return false;
        } else {
            if (!checkPhoneNumber(reg_mobile_number.getText().toString().trim())) {
                reg_mobile_number.setError("Please fill out this field with valid mobile number");
                return false;
            }
        }
        if (reg_phone_number.getText().toString().trim().isEmpty()) {

            reg_phone_number.setError("Please fill out this field");
            return false;
        } else {
            if (!checkLandPhoneNumber(reg_phone_number.getText().toString().trim())) {
                reg_phone_number.setError("Please fill out this field with valid phone number");
                return false;
            }
        }
        if (reg_national_number.getText().toString().trim().isEmpty()) {
            reg_national_number.setError("Please fill out this field");
            return false;
        } else {
            if (reg_national_number.getText().toString().trim().length() != 11) {
                reg_national_number.setError("Please fill out this field with valid national number");
                return false;
            }
        }
        if (reg_address.getText().toString().trim().isEmpty()) {
            reg_address.setError("Please fill out this field");
            return false;
        } else {

        }
        if (reg_password.getText().toString().trim().isEmpty()) {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
            reg_password.setError("Please fill out this field");
            return false;
        } else if (reg_password.getText().toString().trim().length() < 4) {
                reg_password.setError("password should be more than 3 characters");
                return false;
            }
        else{txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
            //Here you can write the codes for checking password
        }
        if (reg_password_confirm.getText().toString().trim().isEmpty()) {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
            reg_password_confirm.setError("Please fill out this field");
            return false;
        } else {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
        }
        if (!reg_password.getText().toString().trim().isEmpty()) {
            if (!reg_password.getText().toString().trim().matches(reg_password_confirm.getText().toString().trim())) {
                reg_password_confirm.setError("Please fill out this  with the same password");
                reg_password_confirm.setText("");
                return false;
            }
        }
        return true;

    }

    private boolean checkPhoneNumber(String phoneNumber) {
        //check length of number and if it starts with "09"
        if (phoneNumber.length() != 10 || phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '9') {
            return false;
        }
        //matcher.find() will return true if there were any non numeric characters
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.find()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkLandPhoneNumber(String landPhoneNumber) {
        if (landPhoneNumber.length() != 7) {
            return false;
        }
        //matcher.find() will return true if there were any non numeric characters
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(landPhoneNumber);
        if (matcher.find()) {
            return false;
        } else {
            return true;
        }
    }

}
