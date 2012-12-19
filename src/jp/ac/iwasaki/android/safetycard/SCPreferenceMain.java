package jp.ac.iwasaki.android.safetycard;

import android.preference.*;
import android.view.WindowManager.LayoutParams;
import android.os.*;
import android.content.*;

public class SCPreferenceMain extends PreferenceActivity {  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        addPreferencesFromResource(R.xml.main_pref);  
    }  

    
    public static String getName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("key_name", "");  
    }  
    
    public static String getTel(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("key_tel", "");  
    }  

    public static String getBlood(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("key_blood", "A");  
    }  

    public static String getOther(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("key_other", "");  
    }  
}