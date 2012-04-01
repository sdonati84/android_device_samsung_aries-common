package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class TouchWake implements OnPreferenceChangeListener {

    private static final String TOUCHWAKE_ENABLE_FILE = "/sys/class/misc/touchwake/enabled";
    private static final String TOUCHWAKE_DELAY_FILE = "/sys/class/misc/touchwake/delay";
    
    public static boolean isSupported() {
        return Utils.fileExists(TOUCHWAKE_ENABLE_FILE);
    }

    public static void setEnabled(boolean value) {
    	if (value==true)
    		Utils.writeValue(TOUCHWAKE_ENABLE_FILE, "1");
    	else
    		Utils.writeValue(TOUCHWAKE_ENABLE_FILE, "0");
    }
    
    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_TOUCHWAKE_EN, false);
        if (enabled==true){
        	Utils.writeValue(TOUCHWAKE_ENABLE_FILE, "1");
        } else {
        	Utils.writeValue(TOUCHWAKE_ENABLE_FILE, "0");
        }
        
        Utils.writeValue(TOUCHWAKE_DELAY_FILE, sharedPrefs.getString(DeviceSettings.KEY_TOUCHWAKE_DEL, "6"));
    }
    

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    	String key=preference.getKey();
    	if (DeviceSettings.KEY_TOUCHWAKE_EN.equals(key)){
            boolean enable = (Boolean) newValue;
            TouchWake.setEnabled(enable);
            return true;
    	} else {
    		Utils.writeValue(TOUCHWAKE_DELAY_FILE, (String) newValue);
    		return true;
    	}
    }

}
