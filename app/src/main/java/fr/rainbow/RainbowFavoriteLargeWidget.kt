package fr.rainbow

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.RemoteViews
import fr.rainbow.MainActivity.Companion.favorites
import fr.rainbow.functions.Functions

/**
 * Implementation of App Widget functionality.
 */
class RainbowFavoriteLargeWidget : AppWidgetProvider() {

    private val handler = Handler()
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        handler.postDelayed(
            { onUpdate(context, appWidgetManager, appWidgetIds) }, 30000
        )
    }

    override fun onDisabled(context: Context) {
        handler.removeCallbacksAndMessages(null)
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

    val views = RemoteViews(context.packageName, R.layout.rainbow_favorite_large_widget)
    views.setTextViewText(R.id.widgetText,favorites[0].name)
    views.setTextViewText(R.id.widgetTemp,favorites[0].weatherData!!.hourly.temperature_2m.get(
        Functions.findCurrentSlotHourly(favorites[0])
    ).toString())

    Functions.updatingWeatherWidgetIc(
        views,
        favorites[0].weatherData!!.hourly.weathercode[Functions.findCurrentSlotHourly(favorites[0])])
    appWidgetManager.updateAppWidget(appWidgetId, views)
}