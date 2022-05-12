package com.example.RasidiAndroid.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class URLConnector {

//    public static final String BASE_URL =  "http://192.168.43.78:8000";
//    public static final String BASE_URL =  "http://192.168.1.110:8000";
//    public static final String BASE_URL =  "http://192.168.43.130:8000";
//      public static final String BASE_URL =  "http://192.168.137.1:8000";
//    public static final String BASE_URL =  "http://192.168.100.146:8000";
   public static final String BASE_URL =  "https://rasidi.jableh.net";

    public static final String BASE_API_FOLDER = "api";
    public static final String BASE_IDENTIFIER = "user";
    public static final String REGISTER = "register";
    public static final String BEFORE_REGISTER = "before_register";
    public static final String LOGIN = "login";
    public static final String BEFORE_LOGIN = "before_login";

    public static final String CATEGORIES = "categories";
    public static final String TRANSFER_OPERATIONS_URL = "operations";
    public static final String BEFORE_TRANSFER_URL = "before_transfer";
    public static final String TRANSFER_URL = "transfer";
    public static final String BILLS_URL = "bills";
    public static final String PAY_BILL_URL = "pay_bill";
    public static final String GET_BALANCE_URL = "get_balance";
    public static final String SEND_FCM = "send_notification";

    public static final String REGISTER_NEW_TRANSFER_OPERATION_URL = "registerNewTransferOperation.php";

    // URL FOR REGISTER
    public static final String REGISTER_URL =
          URLConnector.BASE_URL + "/" +
          URLConnector.BASE_API_FOLDER + "/" +
          URLConnector.REGISTER;
    // URL FOR REGISTER
    public static final String BEFORE_REGISTER_URL =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BEFORE_REGISTER;

    // URL FOR LOG IN
    public static final String LOG_IN_URL =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.LOGIN;

    // URL FOR GETTING CATEGORIES IN MAIN FRAGMENT
    public static final String CATEGORIES_URL =
            URLConnector.BASE_URL + "/" +
            URLConnector.BASE_API_FOLDER + "/" +
            URLConnector.BASE_IDENTIFIER+ "/" +
            URLConnector.CATEGORIES;
    // URL FOR GETTING CATEGORIES IN MAIN FRAGMENT
    public static final String SEND_FCM_URL =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BASE_IDENTIFIER+ "/" +
                    URLConnector.SEND_FCM;
    // URL FOR GETTING USER'S OPERATIONS IN OPERATION FRAGMENT
    public static final String URL_USER_OPERATIONS =
            URLConnector.BASE_URL + "/" +
            URLConnector.BASE_API_FOLDER + "/" +
            URLConnector.BASE_IDENTIFIER+ "/" +
            URLConnector.TRANSFER_OPERATIONS_URL;

    public static final String URL_USER_BALANCE =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BASE_IDENTIFIER+ "/" +
                    URLConnector.GET_BALANCE_URL;
    public static final String URL_USER_BEFORE_TRANSFER =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BASE_IDENTIFIER+ "/" +
                    URLConnector.BEFORE_TRANSFER_URL;
    public static final String URL_USER_TRANSFER =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BASE_IDENTIFIER+ "/" +
                    URLConnector.TRANSFER_URL;
    public static final String URL_USER_BILLS =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BASE_IDENTIFIER+ "/" +
                    URLConnector.BILLS_URL;
    public static final String URL_USER_PAY_BILL =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.BASE_IDENTIFIER+ "/" +
                    URLConnector.PAY_BILL_URL;

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
    }
}
