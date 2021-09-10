package com.example.verificationnumberapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.verificationnumberapp.Models.CategoriesModel;
import com.example.verificationnumberapp.Models.OperationModel;
import com.example.verificationnumberapp.R;
import com.example.verificationnumberapp.SpinnerCustom.CustomSpinnerAdapter;
import com.example.verificationnumberapp.SpinnerCustom.CustomSpinnerItem;
import com.example.verificationnumberapp.Utils.SharedPrefs;
import com.example.verificationnumberapp.Utils.URLConnector;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddOperationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText phoneBox;
    EditText passwordET;
    private TextInputLayout txtInLayoutPassword;
    Button submitButton;
    Spinner providerSpinner;
    Spinner categoriesSpinner;
    String spinnerSelectedCategory;
    String selectedProvider;
    OperationModel operationModel;
    ProgressBar progressBar;
    private ArrayList<CustomSpinnerItem> mCustomSpinnerItems;
    private CustomSpinnerAdapter mCustomSpinnerAdapter;
    ArrayList<CategoriesModel> categoriesList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;
    private int user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_operation);

        phoneBox = findViewById(R.id.phoneBox);
        passwordET = findViewById(R.id.add_op_password);
        txtInLayoutPassword = findViewById(R.id.txtInLayoutPassword);
        progressBar = findViewById(R.id.add_op_PB);
        submitButton = findViewById(R.id.sendButton);
        categoriesSpinner = (Spinner) findViewById(R.id.spinner);
        providerSpinner = (Spinner) findViewById(R.id.custom_spinner);
        user_id = SharedPrefs.getInt(this, SharedPrefs.KEY_USER_ID);


        categoriesSpinner.setOnItemSelectedListener(this);
        initiateCategories();

        //Provider spinner
        initProviderSpinnerList();
        mCustomSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), mCustomSpinnerItems);
        providerSpinner.setAdapter(mCustomSpinnerAdapter);
        providerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CustomSpinnerItem customSpinnerItem = (CustomSpinnerItem) parent.getItemAtPosition(position);
                if (position == 0) {
                    selectedProvider = OperationModel.MTN_STRING;
                } else if (position == 1) {
                    selectedProvider = OperationModel.SYRIATEL_STRING;
                }
                //  selectedProvider = customSpinnerItem.getProviderName();
                //     Toast.makeText(getContext(), selectedProvider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProvider = OperationModel.MTN_STRING;
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoriesSpinner != null && categoriesSpinner.getSelectedItem() !=null ) {
                    spinnerSelectedCategory = (String) categoriesSpinner.getSelectedItem();
                } else  {
                    spinnerSelectedCategory = "100";
                }
                Log.e("spinner category","spinner "+spinnerSelectedCategory);

                Toast.makeText(AddOperationActivity.this, spinnerSelectedCategory, Toast.LENGTH_LONG).show();
                if(spinnerSelectedCategory == null){
                    Log.e("null category","there is no category");
                }else{
                    if(validateInput()){
                        checkPasswordCorrection();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }


    //Initializing of custom spinner
    private void initProviderSpinnerList() {
        mCustomSpinnerItems = new ArrayList<>();
        mCustomSpinnerItems.add(new CustomSpinnerItem("MTN", R.drawable.ic_mtn_spinner));
        mCustomSpinnerItems.add(new CustomSpinnerItem("", R.drawable.ic_syriatel_logo));
    }

    //Categories spinner functions
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelectedCategory = parent.getItemAtPosition(position).toString().trim();
    }

    //Categories spinner functions
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinnerSelectedCategory = "100";
    }

    private boolean checkPhoneNumber(String phoneNumber) {

        //check length of number and if it starts with "09"
        if (phoneNumber.length() != 10 || phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '9') {
            return false;
        }
        //matcher.find() will return true if there were any non numeric characters
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(phoneNumber);
        return !matcher.find();

    }

    private boolean validateInput(){
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
        if (phoneBox.getText().toString().trim().isEmpty()) {
            phoneBox.setError("Please fill out this field");
            return false;
        } else {
            if(!checkPhoneNumber(phoneBox.getText().toString().trim())){
                phoneBox.setError("Please fill out this field with valid mobile number");
                return false;
            }
        }
        if (passwordET.getText().toString().trim().isEmpty()) {
            txtInLayoutPassword.setPasswordVisibilityToggleEnabled(false);
            passwordET.setError("Please fill out this field");
            return false;
        } else {
            txtInLayoutPassword.setPasswordVisibilityToggleEnabled(true);
        }
        return true;
    }
    public void initiateCategories() {
        Log.e("init", "init");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLConnector.CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            JSONArray categories = jsonObject.getJSONArray("data");
                            String code = jsonObject.getString("code");

                            if (code.equals("200")) {
                                Toast.makeText(AddOperationActivity.this, message, Toast.LENGTH_SHORT).show();
                                //traversing through all the object
                                for (int i = 0; i < categories.length(); i++) {
                                    //getting product object from json array
                                    JSONObject object = categories.getJSONObject(i);

                                    categoriesList.add(new CategoriesModel(
                                            object.getInt("id"),
                                            object.getInt("amount") + ""
                                    ));
                                }
                                ArrayList<String> spinnerCategories = new ArrayList<>();

                                for (int j = 0; j < categoriesList.size(); j++) {
                                    spinnerCategories.add(categoriesList.get(j).getAmount().trim());
                                }
                                // Creating adapter for spinner
                                spinnerAdapter =
                                        new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerCategories);

                            } else {


                            }

                            if (categoriesList != null) {
                                ArrayList<String> spinnerCategories = new ArrayList<>();

                                for (int i = 0; i < categoriesList.size(); i++) {
                                    spinnerCategories.add(categoriesList.get(i).getAmount().trim());
                                }
                                // Creating adapter for spinner
                                spinnerAdapter =
                                        new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerCategories);
                            }
                            // Drop down layout style - list view with radio button
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            categoriesSpinner.setAdapter(spinnerAdapter);
                            // Spinner click listener


                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Login Error1! " + e.toString()
//                                    + "\nCause " + e.getCause()
//                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("response", "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
//                            Toast.makeText(getApplicationContext(), "LogIn Error2! " + error.toString()
//                                    + "\nStatus Code " + "\nCause " + error.getCause()
//                                    + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("response", "LogIn Error2! " + error.toString()
                                    + "\nStatus Code " + "\nCause " + error.getCause()
                                    + "\nmessage" + error.getMessage());
                        }

                    }
                });

        //adding our stringrequest to queue
        queue.add(stringRequest);
    }

    private void checkPasswordCorrection() {
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
        final String password = passwordET.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.URL_USER_BEFORE_TRANSFER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");

                            if(code.equals("200")){
                                addOperationRequest();

                            }else{
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                submitButton.setVisibility(View.VISIBLE);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.e("error1" ,"Check Password Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
//                            Toast.makeText(getApplicationContext(), "Check Password Error1! " + e.toString()
//                                    + "\nCause " + e.getCause()
//                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            submitButton.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Check Password Error2! " + error.toString()
                                + "\nCause: " + error.getCause()
                                + "\nmessage: " + error.getMessage()
                        );
//                        Toast.makeText(getApplicationContext(), "Check Password Error2! " + error.toString()
//                                + "\nCause " + error.getCause()
//                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(user_id));
                params.put("password", password);
                Log.e("info", String.valueOf(user_id) + " " + password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private  void addOperationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
             String mobile_number = phoneBox.getText().toString().trim();
             String category = spinnerSelectedCategory;
             Log.e("spinner category", spinnerSelectedCategory);
              String sim_type = selectedProvider;

            operationModel = new OperationModel(user_id,
                    mobile_number, category, sim_type);

           StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.URL_USER_TRANSFER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");

                            progressBar.setVisibility(View.GONE);
                            submitButton.setVisibility(View.VISIBLE);

                            if(code.equals("200")){
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }else{
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.e("error1" ,"Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
//                            Toast.makeText(getApplicationContext(), "Register Error1! " + e.toString()
//                                    + "\nCause " + e.getCause()
//                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            submitButton.setVisibility(View.VISIBLE);

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
//                        Toast.makeText(getApplicationContext(), "Register Error2! " + error.toString()
//                                + "\nCause " + error.getCause()
//                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                    }
                })
                {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(user_id));
                params.put("sim_type", operationModel.getSim_type());
                params.put("category_amount",operationModel.getCategory());
                params.put("mobile", operationModel.getMobile_number());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
