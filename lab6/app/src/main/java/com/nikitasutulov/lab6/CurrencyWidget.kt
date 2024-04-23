package com.nikitasutulov.lab6

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

/**
 * Implementation of App Widget functionality.
 */
class CurrencyWidget : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val combinedRates = HashMap<String, Double>()

        val fetchCallback = object : Callback<List<Currency>> {
            override fun onResponse(call: Call<List<Currency>>, response: Response<List<Currency>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val currency = response.body()!![0]
                    val rate = currency.rate

                    combinedRates[currency.cc] = rate

                    if (combinedRates.size == 2) { // Both requests completed
                        val combinedRate = "USD: ${combinedRates["USD"]}\nEUR: ${combinedRates["EUR"]}"

                        val views = RemoteViews(context.packageName, R.layout.currency_widget)
                        views.setTextViewText(R.id.currencyRateTextView, combinedRate)

                        for (appWidgetId in appWidgetIds) {
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                val views = RemoteViews(context.packageName, R.layout.currency_widget)
                views.setTextViewText(R.id.currencyRateTextView, "Failed to fetch currency rates")

                for (appWidgetId in appWidgetIds) {
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }

        fetchCurrencyData("USD", fetchCallback)
        fetchCurrencyData("EUR", fetchCallback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchCurrencyData(currencyCode: String, callback: Callback<List<Currency>>) {
        RetrofitInstance.currencyApi.getCurrency(currencyCode, formatCurrentDate()).enqueue(callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatCurrentDate(): String {
        val currentDate = LocalDate.now()
        return "${currentDate.year}${String.format("%02d", currentDate.monthValue)}${String.format("%02d", currentDate.dayOfMonth)}"
    }
}
