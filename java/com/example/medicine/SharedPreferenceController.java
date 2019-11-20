package com.example.medicine;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceController {
    void setPassword(Context ctx, String pw) {
        SharedPreferences preferences = ctx.getSharedPreferences("password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PW", pw);
        editor.apply();
    }

    String getPassword(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("password", Context.MODE_PRIVATE);
        return preferences.getString("PW", "");
    }
}
