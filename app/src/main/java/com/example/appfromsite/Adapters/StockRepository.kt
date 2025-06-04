package com.example.appfromsite.Adapters

import android.util.Log
import com.example.appfromsite.Entity.PhotoEntity
import com.example.appfromsite.Entity.StockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class StockRepository(
    private val webParser: WebParser,
    private val imageParser: StockPhotoParser
) {
    private val stockUrls = listOf(
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/skidka-na-majskie-prazdniki-aktivirovana/",
        "https://vrgrad.ru/novosti-kompanii/uspejte-zakazat-remont-po-staroj-czene/",
        "https://vrgrad.ru/novosti-kompanii/nashim-podpischikam-15-skidka/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/skidka-na-remont-detskoj-v-den-znanij-i-koe-chto-eshhyo/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/podarim-vam-horoshij-kondiczioner/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/uspej-zakazat-remont-do-nachala-leta-so-skidkoj-20/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/podarite-uyut-i-kofe-svoim-lyubimym/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/chto-podarit-na-prazdniki/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/novogodnyaya-akcziya-i-podarki-uzhe-zhdut-pod-elochkoj/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/tolko-v-noyabre-skidka-10-i-gadzhet-v-podarok/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/uyuta-i-tepla-vam-v-dom-v-oktyabre/",
        "https://vrgrad.ru/novosti-kompanii/akczii-i-skidki/zakazhi-lyuboj-remont-v-sentyabre-i-poluchi-podarok/"
    )

    suspend fun getStocks(): List<StockEntity> = withContext(Dispatchers.IO) {
        val textResults = mutableListOf<StockEntity>()
        val imageUrls = imageParser.getImageUrls()

        stockUrls.forEachIndexed { index, url ->
            try {
                val stock = webParser.parseStockData(url)
                if (stock != null) {
                    val imageUrl = if (index < imageUrls.size) imageUrls[index] else ""
                    textResults.add(stock.copy(url = imageUrl))
                    Log.d("StockRepository", "Successfully parsed: $url with image: $imageUrl")
                }
                delay(500)
            } catch (e: Exception) {
                Log.e("StockRepository", "Error parsing $url", e)
            }
        }

        Log.d("StockRepository", "Total parsed items: ${textResults.size}")
        return@withContext textResults
    }
}