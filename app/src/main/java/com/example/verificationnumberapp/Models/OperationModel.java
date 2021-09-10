package com.example.verificationnumberapp.Models;

public class OperationModel {


    private int user_id;
    private String mobile_number;
    private String category;
    private boolean status;
    private  String sim_type;
    private   String date;

    public final static String MTN_STRING = "MTN";
    public final static String SYRIATEL_STRING = "Syriatel";


    public OperationModel(int user_id, String mobile_number, String category, boolean status, String sim_type, String date) {
        this.user_id = user_id;
        this.mobile_number = mobile_number;
        this.category = category;
        this.status = status;
        this.sim_type = sim_type;
        this.date = date;
    }
    public OperationModel(String mobile_number, String category, boolean status, String sim_type, String date) {
        this.mobile_number = mobile_number;
        this.category = category;
        this.status = status;
        this.sim_type = sim_type;
        this.date = date;
    }

    public OperationModel(int user_id, String mobile_number, String category, String sim_type) {
        this.user_id = user_id;
        this.mobile_number = mobile_number;
        this.category = category;
        this.sim_type = sim_type;
    }
    public int getUser_id() {
        return user_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getCategory() {
        return category;
    }

    public boolean isStatus() {
        return status;
    }

    public String getSim_type() {
        return sim_type;
    }

    public String getDate() {
        return date;
    }


}
