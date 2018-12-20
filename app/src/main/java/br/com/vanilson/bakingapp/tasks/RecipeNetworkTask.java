package br.com.vanilson.bakingapp.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.vanilson.bakingapp.model.RecipeModel;
import br.com.vanilson.bakingapp.utils.NetworkUtils;

public class RecipeNetworkTask extends AsyncTask<Void, Void, List<RecipeModel>> {

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
            //todo error message
        } else {

        }
    }
}
