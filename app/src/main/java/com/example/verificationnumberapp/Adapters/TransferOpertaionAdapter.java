package com.example.verificationnumberapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.verificationnumberapp.Models.OperationModel;
import com.example.verificationnumberapp.R;
import java.util.ArrayList;

public class TransferOpertaionAdapter extends RecyclerView.Adapter<TransferOpertaionAdapter.ViewHolder> {

    // variable for our array list and context.
    private ArrayList<OperationModel> operationModelArrayList;
    private Context context;

    // creating a constructor.
    public TransferOpertaionAdapter(ArrayList<OperationModel> userModalArrayList, Context context) {
        this.operationModelArrayList = userModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.operation_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        OperationModel operationModel = operationModelArrayList.get(position);

        // on below line we are setting data to our text view.
        holder.mobileNumberTV.setText(operationModel.getMobile_number().trim());
        holder.categoryTV.setText(operationModel.getCategory().trim());
        holder.dateTV.setText(operationModel.getDate().trim().substring(0,10));

        // on below line we are loading our image
        if(operationModel.getSim_type().equals(OperationModel.MTN_STRING)){
            holder.providerImg.setImageResource(R.drawable.ic_mtn);
        }else if(operationModel.getSim_type().equals(OperationModel.SYRIATEL_STRING)){
            holder.providerImg.setImageResource(R.drawable.ic_syriatel_logo);
        }

        if(operationModel.isStatus()){
            holder.status.setText("DONE");
            holder.status.setBackgroundColor(Color.GREEN);
        }else{
            holder.status.setBackgroundColor(Color.RED);
            holder.status.setText("NOT DONE");

        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return operationModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView mobileNumberTV, categoryTV, dateTV;
//        private View status;
        private TextView  status;
        private ImageView providerImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            mobileNumberTV = itemView.findViewById(R.id.idTVMobileNumber);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
            dateTV = itemView.findViewById(R.id.idTVDate);
            status = itemView.findViewById(R.id.status_txt);
            providerImg = itemView.findViewById(R.id.idIVSim);
        }
    }
}