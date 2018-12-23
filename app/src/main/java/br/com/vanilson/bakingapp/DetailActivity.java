package br.com.vanilson.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import br.com.vanilson.bakingapp.model.RecipeModel;

public class DetailActivity extends AppCompatActivity{

    public static String RECIPES_DETAIL_KEY = "recipesDetail";
    RecipeModel mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            mRecipe = getIntent().getParcelableExtra(RECIPES_DETAIL_KEY);

            getSupportActionBar().setTitle(mRecipe.getName());

            MasterDetailListFragment detailListFragment = new MasterDetailListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPES_DETAIL_KEY, mRecipe);
            detailListFragment.setArguments(bundle);

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.master_list_fragment, detailListFragment)
                    .commit();
        }


    }
}
