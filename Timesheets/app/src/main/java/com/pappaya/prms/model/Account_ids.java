package com.pappaya.prms.model;

import java.util.ArrayList;

/**
 * Created by yasar on 10/12/16.
 */
public class Account_ids {

    private String id;

    private String name;

    private ArrayList<Account_ids> account_idsArrayList;

    public ArrayList<Account_ids> getAccount_idsArrayList() {
        return account_idsArrayList;
    }

    public void setAccount_idsArrayList(ArrayList<Account_ids> account_idsArrayList) {
        this.account_idsArrayList = account_idsArrayList;
    }

    public Account_ids() {

    }

    public Account_ids(ArrayList<Account_ids> account_idsArrayList) {
        this.account_idsArrayList = account_idsArrayList;
    }

    public Account_ids(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
