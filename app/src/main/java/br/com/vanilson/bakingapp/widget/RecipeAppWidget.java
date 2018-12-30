package br.com.vanilson.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;

import br.com.vanilson.bakingapp.MainActivity;
import br.com.vanilson.bakingapp.R;
import br.com.vanilson.bakingapp.model.RecipeModel;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RecipeModel recipeModel = PreferenceUtil.loadWidgedRecipe(context);

        if (recipeModel == null){
            List<RecipeModel> recipes = PreferenceUtil.loadRecipes(context);
            recipeModel = recipes != null && recipes.size() > 0 ? recipes.get(0) : null;
        }

        if(recipeModel != null){
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);

            views.setTextViewText(R.id.recipe_name_tv, recipeModel.getName());
            views.setOnClickPendingIntent(R.id.recipe_name_tv, pendingIntent);

            Intent intent = new Intent(context, IngredientsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            views.setRemoteAdapter(R.id.ingredients_lv, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_lv);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

