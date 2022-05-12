package com.example.RasidiAndroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RasidiAndroid.Activities.HomeActivity;
import com.example.RasidiAndroid.Models.UserModel;
import com.example.RasidiAndroid.Utils.Constants;
import com.example.RasidiAndroid.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * https://www.youtube.com/watch?v=dI8mMpL8dmo
 */
public class VerifyOTPActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String verificationId;

    private FirebaseAuth mAuth;
    private UserModel userModel;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+963-%s", getIntent().getStringExtra(Constants.USER_MOBILE_KEY)
        ));
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        setUpOTPInputs();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button buttonVerify = findViewById(R.id.buttonVerify);

        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra(Constants.VERIFICATION_ID);

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()) {

                    Toast.makeText(VerifyOTPActivity.this, "Please Enter Valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputCode1.getText().toString() +
                        inputCode2.getText().toString() +
                        inputCode3.getText().toString() +
                        inputCode4.getText().toString() +
                        inputCode5.getText().toString() +
                        inputCode6.getText().toString();
                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code

                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonVerify.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        SharedPrefs.save(getApplicationContext(), SharedPrefs.KEY_LOG_IN_STATE, true);
                                        SharedPrefs.save(getApplicationContext(), SharedPrefs.KEY_USER_WHO_AM_I, Constants.REGISTER_I_AM_KEY);
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(VerifyOTPActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }


        });
        findViewById(R.id.textResendCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+963" + getIntent().getStringExtra(Constants.USER_MOBILE_KEY))       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(VerifyOTPActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(VerifyOTPActivity.this, "Please Open VPN, your country is restricted", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        verificationId = newVerificationId;
                                        Toast.makeText(VerifyOTPActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();

                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }

    private void setUpOTPInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //todo:  not sure about !
                //here when the user fill the first editText the cursor "Focus" will auto move to next one
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //todo:  not sure about !
                //here when the user fill the first editText the cursor "Focus" will auto move to next one
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here when the user fill the first editText the cursor "Focus" will auto move to next one
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //todo:  not sure about !
                //here when the user fill the first editText the cursor "Focus" will auto move to next one
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //todo:  not sure about !
                //here when the user fill the first editText the cursor "Focus" will auto move to next one
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    private void logIn(String mobileNumber) {
//        userModel = new UserModel();
//        userModel.setMobileNumber("0" + getIntent().getStringExtra(Constants.USER_MOBILE_KEY));
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.LOG_IN_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                    try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String message = jsonObject.getString("message");
//                            JSONObject data =  jsonObject.getJSONObject("data");
//                            String code = jsonObject.getString("code");
//
//                            if(code.equals("200")) {
//                                userModel.setUser_id(data.getInt("id"));
//                                userModel.setFirstName(data.getString("first_name").trim());
//                                userModel.setMiddleName(data.getString("middle_name").trim());
//                                userModel.setLastName(data.getString("last_name").trim());
//                                userModel.setMobileNumber(data.getString("mobile").trim());
//                                userModel.setPhoneNumber(data.getString("phone").trim());
//                                userModel.setNationalNumber(data.getString("national_num").trim());
//                                userModel.setAddress(data.getString("address").trim());
//
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//                                Log.e("userLogIn", message +
//                                        "Your Name:" + userModel.getFirstName() + " "
//                                        + userModel.getLastName());
//                                //for the first login
//
//                                saveUsersLogIn(userModel.getUser_id(),
//                                        userModel.getMobileNumber(),
//                                        userModel.getFirstName()+" "+userModel.getMiddleName()+" " +userModel.getLastName(),
//                                        userModel.getBalance());
//
//                                Log.e("SharedPref",  ""+ SharedPrefs.getInt(getApplicationContext(), SharedPrefs.LOGGED_IN_KEY));
//
//                                System.out.println( SharedPrefs.getInt(getApplicationContext(), SharedPrefs.LOGGED_IN_KEY));
//
//
//                            }else{
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                            }
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "try-catch error : " + e.toString()
//                                    + "\nCause " + e.getCause()
//                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "LogIn Error2! " + error.toString()
//                                + "\nCause " + error.getCause()
//                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("mobile", userModel.getMobileNumber());
//
//                return params;
//            }
//        };
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//
//    }
//    private void saveUsersLogIn(int user_id, String user_mobile, String user_name,int user_balance) {
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_KEY, user_id);
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_MOBILE, user_mobile);
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_KEY, user_name);
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_USER_BALANCE, user_balance);
//    }

//    private  void registerUser() {
//        String firstName = getIntent().getStringExtra(Constants.USER_F_NAME_KEY);
//        String middleName  = getIntent().getStringExtra(Constants.USER_M_NAME_KEY);
//        String lastName = getIntent().getStringExtra(Constants.USER_L_NAME_KEY);
//        String mobileNumber = getIntent().getStringExtra(Constants.USER_MOBILE_KEY);
//        String phoneNumber = getIntent().getStringExtra(Constants.USER_PHONE_KEY);
//        String nationalNumber = getIntent().getStringExtra(Constants.USER_N_NUMBER_KEY);
//        String address = getIntent().getStringExtra(Constants.USER_ADDRESS_KEY);
//        String password = getIntent().getStringExtra(Constants.USER_PASSWORD_KEY);
//
//        userModel = new UserModel(firstName,
//                middleName, lastName, mobileNumber, phoneNumber, nationalNumber, address, password);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.REGISTER_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
//                        System.out.println(response);
//
//                        Log.e("response", response);
//
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            String code = jsonObject.getString("code");
//
//                            if(code.equals("200")){
//                                String message = jsonObject.getString("message");
//                                int uid = jsonObject.getInt("data");
//
//                                System.out.println(message + " "+ uid);
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                startActivity(intent);
//                                finish();
//                                saveUsersRegister(uid,  userModel.getMobileNumber(),
//                                        userModel.getFirstName()+" "+userModel.getMiddleName()+" " +userModel.getLastName(),
//                                        0);
//                            }else{
//                                String message = jsonObject.getString("message");
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                            Log.e("error1" ,"Register Error1! " + e.toString()
//                                    + "\nCause " + e.getCause()
//                                    + "\nmessage" + e.getMessage());
//                            Toast.makeText(getApplicationContext(), "Register Error1! " + e.toString()
//                                    + "\nCause " + e.getCause()
//                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error", "Register Error2! " + error.toString()
//                                + "\nCause: " + error.getCause()
//                                + "\nmessage: " + error.getMessage()
//                        );
//                        Toast.makeText(getApplicationContext(), "Register Error2! " + error.toString()
//                                + "\nCause " + error.getCause()
//                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//                })
//        {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("mobile", userModel.getMobileNumber());
//                params.put("first_name", userModel.getFirstName());
//                params.put("middle_name",userModel.getMiddleName());
//                params.put("last_name", userModel.getLastName());
//                params.put("mobile", userModel.getMobileNumber());
//                params.put("phone", userModel.getPhoneNumber());
//                params.put("national_num", userModel.getNationalNumber());
//                params.put("address", userModel.getAddress());
//                params.put("password", userModel.getPassword());
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//    }
//    private void saveUsersRegister(int user_id, String user_mobile, String user_name,int user_balance) {
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_KEY, user_id);
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_MOBILE, user_mobile);
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_KEY, user_name);
//        SharedPrefs.save(getApplicationContext(), SharedPrefs.LOGGED_IN_USER_BALANCE, user_balance);
//
//    }

}