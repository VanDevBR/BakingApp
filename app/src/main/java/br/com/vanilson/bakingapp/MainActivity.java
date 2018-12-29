package br.com.vanilson.bakingapp;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.vanilson.bakingapp.model.RecipeModel;
import br.com.vanilson.bakingapp.model.RecipesAdapter;
import br.com.vanilson.bakingapp.utils.NetworkUtils;
import br.com.vanilson.bakingapp.widget.PreferenceUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecipesAdapter mRecipesAdapter;

    public static String RECIPES_KEY = "recipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_recipes);
        mRecipesAdapter = new RecipesAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        if(getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            layoutManager = new GridLayoutManager(this, 3);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipesAdapter);

        if (savedInstanceState != null) {
            List<RecipeModel> recipes = savedInstanceState.getParcelableArrayList(RECIPES_KEY);
            if(recipes == null){
                loadRecipes();
            }else {
                mRecipesAdapter.setRecipes(recipes);
            }
        } else {
            loadRecipes();
        }


    }

    private void loadRecipes() {
        if(NetworkUtils.isNetworkAvailable(this)){
            new RecipeNetworkTasks().execute();
        } else {
            List<RecipeModel> recipes = PreferenceUtil.loadRecipes(this);
            if(recipes != null && recipes.size() > 0){
                mRecipesAdapter.setRecipes(recipes);
            }else{
                Toast.makeText(this, R.string.offline_toast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRecipesAdapter != null && mRecipesAdapter.getRecipes() != null){
            outState.putParcelableArrayList(RECIPES_KEY, (ArrayList<? extends Parcelable>) mRecipesAdapter.getRecipes());
        }
    }

    public class RecipeNetworkTasks extends AsyncTask<Void, Void, List<RecipeModel>> {

        @Override
        protected List<RecipeModel> doInBackground(Void... voids) {

            List<RecipeModel> recipes = new ArrayList<>();

            try {

                String resp = NetworkUtils.requestHttpUrl(new URL(NetworkUtils.RECIPES_URL));

                if(resp == null || resp.isEmpty()){
                    return null;
                }

                recipes.addAll(
                        (List<RecipeModel>) new Gson().fromJson(resp, new TypeToken<List<RecipeModel>>() {}.getType())
                );

                SharedPreferences preferences = getSharedPreferences(PreferenceUtil.PREF_KEY, getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PreferenceUtil.PREF_NAME, resp);
                editor.apply();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return recipes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<RecipeModel> recipeModels) {
            super.onPostExecute(recipeModels);
            if(recipeModels.isEmpty()){
                Toast.makeText(getApplicationContext(), R.string.notfound_toast, Toast.LENGTH_SHORT).show();
            } else {
                mRecipesAdapter.setRecipes(recipeModels);
            }
        }
    }

}
