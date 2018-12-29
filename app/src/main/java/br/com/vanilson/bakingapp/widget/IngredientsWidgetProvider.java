package br.com.vanilson.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import br.com.vanilson.bakingapp.R;
import br.com.vanilson.bakingapp.model.IngredientModel;
import br.com.vanilson.bakingapp.model.RecipeModel;

public class IngredientsWidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<IngredientModel> ingredients;

    public IngredientsWidgetProvider(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        List<RecipeModel> recipes = PreferenceUtil.loadRecipes(mContext);
        ingredients = recipes.get(0).getIngredients(); //todo
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);
        IngredientModel ingredient = ingredients.get(i);
        String ingredientText = ingredient.getQuantity() + " (" + ingredient.getMeasure().toLowerCase() + ") - " + ingredient.getIngredient();
        row.setTextViewText(R.id.ingredient_item_tv, ingredientText);
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
