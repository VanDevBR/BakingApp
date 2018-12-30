package br.com.vanilson.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import br.com.vanilson.bakingapp.model.RecipeModel;

public class IngredientsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new IngredientsWidgetProvider(getApplicationContext());
    }

    public static void updateWidget(Context context, RecipeModel recipe) {
        PreferenceUtil.saveWidgedRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeAppWidget.class));
        RecipeAppWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }
}
