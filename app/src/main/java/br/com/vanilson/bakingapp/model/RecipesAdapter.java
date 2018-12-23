package br.com.vanilson.bakingapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.vanilson.bakingapp.DetailActivity;
import br.com.vanilson.bakingapp.R;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    List<RecipeModel> recipes;
    Context mContext;

    @NonNull
    @Override
    public RecipesAdapter.RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipes_list_item, parent, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapter.RecipesAdapterViewHolder holder, int position) {
        if (!recipes.get(position).getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load(recipes.get(position).getImage())
                    .error(R.drawable.no_picture)
                    .centerCrop()
                    .into(holder.mRecipeImageView);
        } else {
            holder.mRecipeImageView.setImageResource(R.drawable.nutella_pie);
        }
        holder.mRecipeTextView.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (null == recipes) return 0;
        return recipes.size();
    }

    public void setRecipes(List<RecipeModel> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public List<RecipeModel> getRecipes() {
        return recipes;
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mRecipeImageView;
        public final TextView mRecipeTextView;

        public RecipesAdapterViewHolder(View view) {
            super(view);
            mRecipeImageView = view.findViewById(R.id.recipe_iv);
            mRecipeTextView = view.findViewById(R.id.recipe_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DetailActivity.RECIPES_DETAIL_KEY, recipes.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
