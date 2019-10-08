package com.hexamples.hader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by webuser1 on 6/14/2015.
 */
public class SessionManager {
    private static final String PREF_NAME = "app.Montej.com.pref";
    private static final String IS_LOGGED = "isLogged";
    private static final String UserCode = "UserCode";
    private static final String FullName = "FullName";
    private static final String Mobile = "Mobile";
    private static final String Password = "Password";
    private static final String User_name = "user_name";
    private static final String Address = "address";




    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void LoginSession() {
        editor.putBoolean(IS_LOGGED, true);
        editor.commit();
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGGED, false);
    }

    public void logout() {
        editor.putBoolean(IS_LOGGED, false);
        editor.commit();
    }

    public void setUserId(String code) {
        editor.putString(UserCode, code);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(UserCode, "");
    }

    public void setFullName(String name) {
        editor.putString(FullName, name);
        editor.commit();
    }
    public String getFullName() {
        return pref.getString(FullName, "");
    }

    public void setMobile(String mobile) {
        editor.putString(Mobile, mobile);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(Mobile, "");
    }



    public void setPassword(String password) {
        editor.putString(Password, password);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(Password, "");
    }




    public void ClearAllData() {
        editor.clear();
        editor.commit();
    }


    public void setUser_name(String user_name) {
        editor.putString(User_name, user_name);
        editor.commit();
    }
    public String getUser_name() {
        return pref.getString(User_name, "");
    }


    public void setaddress(String address) {
        editor.putString(Address, address);
        editor.commit();
    }
    public String getAddress() {
        return pref.getString(Address, "");
    }





}
