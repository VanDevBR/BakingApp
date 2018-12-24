package br.com.vanilson.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.vanilson.bakingapp.model.IngredientModel;
import br.com.vanilson.bakingapp.model.RecipeModel;
import br.com.vanilson.bakingapp.model.StepAdapter;

import static br.com.vanilson.bakingapp.DetailActivity.RECIPES_DETAIL_KEY;

public class MasterDetailListFragment extends Fragment {

    public MasterDetailListFragment(){}

    OnStepClickListener mCallback;

    private RecipeModel mRecipe;
    private RecyclerView mRecyclerView;
    private LinearLayout mIngredientsLl;
    private StepAdapter mStepAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mCallback = (OnStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detail_list, container, false);

        mIngredientsLl = rootView.findViewById(R.id.ll_ingredients);
        mRecyclerView = rootView.findViewById(R.id.rv_detail_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        mStepAdapter = new StepAdapter();

        Bundle bundle = getArguments();
        if(bundle == null){
            System.out.println("NO ARGUMENT FOUND!!!");
            return rootView;
        }

        mRecipe = bundle.getParcelable(RECIPES_DETAIL_KEY);

        int ingredientLayout = R.layout.ingredient_item;
        for(IngredientModel ingredient : mRecipe.getIngredients()){
            String ingredientText = ingredient.getQuantity() + " (" + ingredient.getMeasure().toLowerCase() + ") - " + ingredient.getIngredient();
            View ingredientView = inflater.inflate(ingredientLayout, null);
            ((TextView)ingredientView.findViewById(R.id.ingredient_item_tv)).setText(ingredientText);
            mIngredientsLl.addView(ingredientView);
        }

        mStepAdapter.setSteps(mRecipe.getSteps());
        mStepAdapter.setCallback(new OnStepClickListener() {
            @Override
            public void onStepClicked(int position) {
                mCallback.onStepClicked(position);
            }
        });
        mRecyclerView.setAdapter(mStepAdapter);

        return rootView;


    }


    public interface OnStepClickListener {
        void onStepClicked(int position);
    }

}
