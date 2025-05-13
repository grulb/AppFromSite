package com.example.appfromsite.Adapters

import com.example.appfromsite.Entity.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class PhotoRepository {
    private val targetImageUrls = listOf(
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-9-scaled.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-8.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-7-scaled.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-6.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-5.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-4.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-3-scaled.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-2.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/09/primer-1-scaled.jpg"
    )

    suspend fun parseSpecificPhotos(): List<PhotoEntity> = withContext(Dispatchers.IO) {
        val photoList = mutableListOf<PhotoEntity>()

        try {
            val doc = Jsoup.connect("https://vrgrad.ru/").get()
            val imgElements = doc.select("img")

            imgElements.forEach { img ->
                val imgUrl = img.absUrl("src")
                if (imgUrl in targetImageUrls) {
                    photoList.add(
                        PhotoEntity(
                            id = 0,
                            url = imgUrl,
                            title = img.attr("alt").takeIf { it.isNotEmpty() }
                                ?: "Пример объекта ${photoList.size + 1}"
                        )
                    )
                }
            }

            if (photoList.isEmpty()) {
                photoList.addAll(createDefaultPhotos())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Если парсинг не удался, возвращаем дефолтные фото
            photoList.addAll(createDefaultPhotos())
        }

        return@withContext photoList
    }

    private fun createDefaultPhotos(): List<PhotoEntity> {
        return targetImageUrls.mapIndexed { index, url ->
            PhotoEntity(
                id = 0,
                url = url,
                title = "Пример объекта ${index + 1}"
            )
        }
    }
}