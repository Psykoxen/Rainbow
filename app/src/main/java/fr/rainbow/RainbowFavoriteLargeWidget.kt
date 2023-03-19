package fr.rainbow

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import fr.rainbow.MainActivity.Companion.favorites
import fr.rainbow.functions.Functions
import kotlinx.android.synthetic.main.item_favorite.view.*

/**
 * Implementation of App Widget functionality.
 */
class RainbowFavoriteLargeWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.rainbow_favorite_large_widget)
    views.setTextViewText(R.id.widgetText,favorites[0].name)
    views.setTextViewText(R.id.widgetTemp,favorites[0].weatherData!!.hourly.temperature_2m.get(
        Functions.findCurrentSlotHourly(favorites[0].weatherData!!)
    ).toString())

    /*Functions.updatingWeatherIc(
        R.id.weather_icon,
        favorites[0].weatherData!!.hourly.weathercode[Functions.findCurrentSlotHourly(favorites[0].weatherData)],
        favorites[0].weatherData!!.daily.sunrise[2]
    )*/

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)


    Log.d("test", favorites.toString())
}