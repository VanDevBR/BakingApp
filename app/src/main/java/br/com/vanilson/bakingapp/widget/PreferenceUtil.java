package br.com.vanilson.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import br.com.vanilson.bakingapp.model.RecipeModel;

public class PreferenceUtil {
    public static final String PREF_KEY = "pref_key";
    public static final String PREF_NAME = "pref_name";
    public static final String PREF_WIDGET = "pref_widget";

    public static List<RecipeModel> loadRecipes(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        String resp = prefs.getString(PREF_NAME, "");
        if(resp == null){
            Log.e("PreferenceUtil", "Not recipe found on preference");
            return null;
        }

        List<RecipeModel> recipes = new ArrayList<>();
        recipes.addAll(
                (List<RecipeModel>) new Gson().fromJson(resp, new TypeToken<List<RecipeModel>>() {}.getType())
        );

        return recipes;
    }

    public static void saveRecipe(Context context, String resp) {
        SharedPreferences preferences = context.getSharedPreferences(PreferenceUtil.PREF_KEY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceUtil.PREF_NAME, resp);
        editor.apply();
    }

    public static RecipeModel loadWidgedRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        String resp = prefs.getString(PREF_WIDGET, "");
        if(resp == null){
            Log.e("PreferenceUtil", "Not recipe widget found on preference");
            return null;
        }

        RecipeModel recipe = new Gson().fromJson(resp, new TypeToken<RecipeModel>() {}.getType());

        return recipe;
    }

    public static void saveWidgedRecipe(Context context, RecipeModel recipe) {
        SharedPreferences preferences = context.getSharedPreferences(PreferenceUtil.PREF_KEY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceUtil.PREF_WIDGET, new Gson().toJson(recipe));
        editor.apply();
    }
}
