package com.cncoding.teazer.model.application;

import com.cncoding.teazer.model.base.Category;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class CategoriesList {
    private ArrayList<Category> categoriesList;

    public CategoriesList(ArrayList<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public ArrayList<Category> getCategoriesList() {
        return categoriesList;
    }
}