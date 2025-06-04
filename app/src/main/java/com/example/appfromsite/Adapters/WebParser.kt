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
            val doc = Jsoup.connect(url)
                .timeout(15_000)
                .userAgent("Mozilla/5.0")
                .get()

            val title = doc.selectFirst("h1.elementor-heading-title")?.text()?.trim() ?: ""

            val textElements = doc.select("div.elementor-widget-container p")

            val filteredText = textElements
                .filterNot { element ->
                    // Исключаем элементы, содержащие пагинацию, соцсети, контакты
                    element.text().contains("Ранее") ||
                            element.text().contains("Страница") ||
                            element.text().contains("Далее") ||
                            element.text().contains("Whatsapp") ||
                            element.text().contains("Vk") ||
                            element.text().contains("Telegram") ||
                            element.text().contains("Odnoklassniki") ||
                            element.text().contains("Youtube") ||
                            element.text().matches(Regex("""\+7\s\(\d{3}\)\s\d{3}-\d{2}-\d{2}""")) ||
                            element.text().contains("Info@") ||
                            element.text().contains("г. Москва") ||

                            // Исключаем элементы, которые находятся в футере
                            element.parents().any { parent ->
                                parent.hasClass("footer") ||
                                        parent.tagName() == "footer" ||
                                        parent.hasClass("elementor-location-footer")
                            }
                }
                .joinToString("\n") { it.text().trim() }
                .replace(Regex("""<[^>]*>|[«»]"""), "") // Удаляем оставшиеся HTML-теги и кавычки

            StockEntity(0, "", title, filteredText)
        } catch (e: Exception) {
            Log.e("WebParser", "Error parsing URL: $url", e)
            null
        }
    }
}