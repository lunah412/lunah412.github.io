package com.example.searchevent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SetFravourite {
    private static final String LIST_KEY = "list_key";

    public static void writeList(Context context, List<Favourite> list){
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = context.getSharedPreferences(LIST_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
    }

    public static  List<Favourite> readlist(Context context){
        SharedPreferences pref = context.getSharedPreferences(LIST_KEY,Context.MODE_PRIVATE);
        String jsonString = pref.getString(LIST_KEY,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Favourite>>(){}.getType();
        List<Favourite> list = gson.fromJson(jsonString, type);
        return list;
    }
}
