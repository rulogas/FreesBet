package com.example.freesbet.intro;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String CARGA_ACTIVIDAD_CACHE = "cargaActividadCache";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setCargaCacheActividad(boolean isFirstTime) {
        editor.putBoolean(CARGA_ACTIVIDAD_CACHE, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeCargaActividad() {
        return pref.getBoolean(CARGA_ACTIVIDAD_CACHE, true);
    }
}
