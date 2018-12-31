package br.com.vanilson.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.vanilson.bakingapp.model.RecipeModel;
import br.com.vanilson.bakingapp.widget.IngredientsWidgetService;

import static br.com.vanilson.bakingapp.StepActivity.STEP_DETAIL_KEY;

public class DetailActivity extends AppCompatActivity implements MasterDetailListFragment.OnStepClickListener{

    public static String RECIPES_DETAIL_KEY = "recipesDetail";
    RecipeModel mRecipe;
    Boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (savedInstanceState == null) {
            mRecipe = getIntent().getParcelableExtra(RECIPES_DETAIL_KEY);
        } else {
            if (savedInstanceState.getParcelable(STEP_DETAIL_KEY) != null){
                mRecipe = savedInstanceState.getParcelable(STEP_DETAIL_KEY);
            }
        }

        getSupportActionBar().setTitle(mRecipe.getName());

        MasterDetailListFragment detailListFragment = new MasterDetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPES_DETAIL_KEY, mRecipe);
        detailListFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.master_list_fragment, detailListFragment)
                .commit();

        if(isTablet){

            StepFragment stepFragment = new StepFragment();
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(STEP_DETAIL_KEY, mRecipe.getSteps().get(0));
            stepFragment.setArguments(stepBundle);

            FragmentManager stepManager = getSupportFragmentManager();
            stepManager.beginTransaction()
                    .replace(R.id.step_fragment_fl, stepFragment)
                    .commit();

        }


    }

    @Override
    public void onStepClicked(int position) {
        if(isTablet){
            StepFragment stepFragment = new StepFragment();
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(STEP_DETAIL_KEY, mRecipe.getSteps().get(position));
            stepFragment.setArguments(stepBundle);

            FragmentManager stepManager = getSupportFragmentManager();
            stepManager.beginTransaction()
                    .replace(R.id.step_fragment_fl, stepFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putParcelableArrayListExtra(StepActivity.STEP_DETAIL_KEY, mRecipe.getSteps());
            intent.putExtra(StepActivity.STEP_POSITION_KEY, position);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRecipe != null){
            outState.putParcelable(STEP_DETAIL_KEY, mRecipe);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_widget) {
            IngredientsWidgetService.updateWidget(this, mRecipe);
            Toast.makeText(this, String.format(getString(R.string.widget_added), mRecipe.getName()), Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
