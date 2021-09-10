package com.example.verificationnumberapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.verificationnumberapp.Fragments.BillsFragment;
import com.example.verificationnumberapp.Models.BillModel;
import com.example.verificationnumberapp.Models.OperationModel;
import com.example.verificationnumberapp.R;
import com.example.verificationnumberapp.Utils.SharedPrefs;
import com.example.verificationnumberapp.Utils.URLConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillsListAdapter extends RecyclerView.Adapter<BillsListAdapter.ViewHolder> {

    // variable for our array list and context.
    private ArrayList<BillModel> billModelArrayList;
    private Context context;
    int user_id;


    // creating a constructor.
    public BillsListAdapter(ArrayList<BillModel> billModelArrayList, Context context, int user_id) {
        this.billModelArrayList = billModelArrayList;
        this.context = context;
        this.user_id = user_id;


    }

    @NonNull
    @Override
    public BillsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.bill_rv_item, parent, false);
        return new BillsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsListAdapter.ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        final BillModel billModel = billModelArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.billTypeTV.setText(billModel.getBill_type().trim());
        holder.billValueTV.setText(billModel.getValue()+"");
        if(billModel.getPayment_date() != "null"){
            holder.billPaymentDateTV.setText(billModel.getPayment_date().trim());

        }else{
            holder.billPaymentDateTV.setText("--");
        }


        if(billModel.isPaid()){
            holder.isPaidTV.setText("Paid");
            holder.isPaidTV.setTextColor(Color.GREEN);
            holder.payBtn.setVisibility(View.INVISIBLE);

        }else{
            holder.isPaidTV.setText("NOT PAID YET");
            holder.isPaidTV.setTextColor(Color.RED);
        }

        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater= LayoutInflater.from(context);

                final View view=inflater.inflate(R.layout.enter_password_dialog,null);
                final AlertDialog.Builder alertDialogueBuilder= new AlertDialog.Builder(context);

                alertDialogueBuilder.setView(view);
                alertDialogueBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           sendPayBillRequest(billModel);
                    }

                });
                alertDialogueBuilder.setCancelable(true).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }

                });
                alertDialogueBuilder.create();
                alertDialogueBuilder.show();
                    }

        });
    }
    private void sendPayBillRequest(final BillModel billModel) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConnector.URL_USER_PAY_BILL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");

                            if(code.equals("200")){
                                String message = jsonObject.getString("message");
                                int balance = jsonObject.getInt("data");
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                SharedPrefs.save(context, SharedPrefs.KEY_USER_BALANCE, balance);


                            }else if(code.equals("400")){
                                String message = jsonObject.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();

                            Toast.makeText(context, " Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Error2! " + error.toString()
                                + "\nCause: " + error.getCause()
                                + "\nmessage: " + error.getMessage()
                        );
                        Toast.makeText(context, "Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(user_id));
                params.put("id", billModel.getBill_id()+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return billModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView billTypeTV, billValueTV, billPaymentDateTV;
        private TextView  isPaidTV;
        private Button payBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our variables.
            billTypeTV = itemView.findViewById(R.id.idTVBillType);
            billValueTV = itemView.findViewById(R.id.idTVBillValue);
            billPaymentDateTV = itemView.findViewById(R.id.idTVBillPaymentDate);
            isPaidTV = itemView.findViewById(R.id.isPaid_txt);
            payBtn = itemView.findViewById(R.id.pay_btn);
        }
    }
}