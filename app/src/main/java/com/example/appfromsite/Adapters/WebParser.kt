package com.example.appfromsite.Adapters

import android.util.Log
import com.example.appfromsite.Entity.StockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

class WebParser {
    fun parseStockData(url: String): StockEntity? {
        return try {
            val document = Jsoup.connect(url)
                .timeout(15_000)
                .ignoreHttpErrors(true)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            if (document == null) {
                Log.e("WebParser", "Document is null for URL: $url")
                return null
            }

            // Парсим данные
            val title = document.selectFirst("h1.elementor-heading-title")?.text() ?: ""
            val text = document.select("div.elementor-widget-container p").joinToString("\n") { it.text() }
            val imageUrl = document.selectFirst("div.elementor-widget-container img")?.attr("src") ?: ""

            StockEntity(0, imageUrl, title, text)

        } catch (e: IOException) {
            Log.e("WebParser", "Network error for URL: $url", e)
            null
        } catch (e: Exception) {
            Log.e("WebParser", "Unexpected error for URL: $url", e)
            null
        }
    }
}