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
    private static final String DayDate = "10-10-2019";
    private static final String IsAttendance = "attendance";
    private static final String IsDeparture = "departure";
    ////******************************////////////////////
    private static final String IsAttendanceNotify = "attendanceNotify";
    private static final String IsDepartureNotify = "departureNotify";
    private static final String WelcomeNotify = "welcomeNotify";
    ///////////////////////////////////////////////////
    private static final String NOneTime= "n_one_time";
    private static final String NOneTxt = "n_one_txt";

    private static final String NTwoTime= "n_two_time";
    private static final String NTwoTxt = "n_two_txt";


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
    public void Remove() {
        editor.remove(User_name);
        editor.remove(UserCode);
        editor.remove(IS_LOGGED);

        editor.remove(FullName);
        editor.remove(Mobile);
        editor.remove(Address);


        editor.remove(Password);
        editor.remove(NOneTxt);
        editor.remove(NOneTime);

        editor.remove(NTwoTxt);
        editor.remove(NTwoTime);
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



    public void setDayDate(String dayDate) {
        editor.putString(DayDate, dayDate);
        editor.commit();
    }
    public String getDayDate() {
        return pref.getString(DayDate, "");
    }


    public void NotAttendance() {
        editor.putBoolean(IsAttendance, false);
        editor.commit();
    }
    public void Attendance() {
        editor.putBoolean(IsAttendance, true);
        editor.commit();
    }
    public boolean IsAttendance() {
        return pref.getBoolean(IsAttendance, false);
    }


    public void NotDeparture() {
        editor.putBoolean(IsDeparture, false);
        editor.commit();
    }
    public void Departure() {
        editor.putBoolean(IsDeparture, true);
        editor.commit();
    }
    public boolean IsDeparture() {
        return pref.getBoolean(IsDeparture, false);
    }


    public void NotDepartureNotify() {
        editor.putBoolean(IsDepartureNotify, false);
        editor.commit();
    }
    public void DepartureNotify() {
        editor.putBoolean(IsDepartureNotify, true);
        editor.commit();
    }
    public boolean IsDepartureNotify() {
        return pref.getBoolean(IsDepartureNotify, false);
    }



    public void NotAttendanceNotify() {
        editor.putBoolean(IsAttendanceNotify, false);
        editor.commit();
    }
    public void AttendanceNotify() {
        editor.putBoolean(IsAttendanceNotify, true);
        editor.commit();
    }
    public boolean IsAttendanceNotify() {
        return pref.getBoolean(IsAttendanceNotify, false);
    }


    public void NotWelcomeNotify() {
        editor.putBoolean(WelcomeNotify, false);
        editor.commit();
    }
    public void WelcomeNotify() {
        editor.putBoolean(WelcomeNotify, true);
        editor.commit();
    }
    public boolean IsWelcomeNotify() {
        return pref.getBoolean(WelcomeNotify, false);
    }


    public void setNOneTime(String nOneTime) {
        editor.putString(NOneTime, nOneTime);
        editor.commit();
    }
    public String getNOneTime() {
        return pref.getString(NOneTime, "");
    }

    public void setNOneTxt(String nOneTxt) {
        editor.putString(NOneTxt, nOneTxt);
        editor.commit();
    }
    public String getNOneTxt() {
        return pref.getString(NOneTxt, "");
    }


    public void setNTwoTime(String nTwoTime) {
        editor.putString(NTwoTime, nTwoTime);
        editor.commit();
    }
    public String getNTwoTime() {
        return pref.getString(NTwoTime, "");
    }

    public void setNTwoTxt(String nTwoTxt) {
        editor.putString(NTwoTxt, nTwoTxt);
        editor.commit();
    }
    public String getNTwoTxt() {
        return pref.getString(NTwoTxt, "");
    }


}
