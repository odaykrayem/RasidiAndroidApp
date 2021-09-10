package com.example.verificationnumberapp.Models;

import java.util.ArrayList;

public class CategoriesModel {
    public int category_id;
    public String amount;
    public int price;

    private ArrayList<CategoriesModel> categoriesList;

    public CategoriesModel(int category_id, String amount) {
        this.category_id = category_id;
        this.amount = amount;
        categoriesList = new ArrayList<CategoriesModel>();

    }

    public int getCategory_id() {
        return category_id;
    }

    public String getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }


    public ArrayList<CategoriesModel> getCategoriesList(){
        if(this.categoriesList != null){
            return categoriesList;
        }else{
            this.categoriesList = new ArrayList<>();
        }
        return categoriesList;
    }
}

