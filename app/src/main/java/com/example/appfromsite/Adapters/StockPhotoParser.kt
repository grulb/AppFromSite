package com.example.appfromsite.Adapters

class StockPhotoParser {
    private val imageUrls = listOf(
        "https://vrgrad.ru/wp-content/uploads/2025/04/vrgrad-1.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/12/vrgrad-1.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/10/vrgrad.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/09/1-1024x676.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/07/august.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/04/skidka-20.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/03/coffee.jpg",
        "https://vrgrad.ru/wp-content/uploads/2024/02/podarok.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/12/newyear.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/11/remont.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/10/pol.jpg",
        "https://vrgrad.ru/wp-content/uploads/2023/08/remont-sent.jpg"
    )

    fun getImageUrls(): List<String> {
        return imageUrls.filter { url ->
            url.startsWith("http") && (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png"))
        }
    }
}