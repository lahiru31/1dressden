package com.khadar3344.myshop.data.mock

import com.khadar3344.myshop.data.network.dto.Product
import com.khadar3344.myshop.data.network.dto.Products

object MockDataProvider {
    fun getMockProducts(): Products {
        return Products(
            limit = 10,
            products = listOf(
                Product(
                    id = 1,
                    title = "Men's T-Shirt",
                    description = "Comfortable cotton t-shirt for men.",
                    price = 19,
                    discountPercentage = 10.0,
                    rating = 4.5,
                    stock = 100,
                    brand = "Brand A",
                    category = "clothing",
                    thumbnail = "https://i.dummyjson.com/data/products/51/thumbnail.jpg",
                    images = listOf(
                        "https://i.dummyjson.com/data/products/51/1.jpg",
                        "https://i.dummyjson.com/data/products/51/2.jpg"
                    )
                ),
                Product(
                    id = 2,
                    title = "Women's Dress",
                    description = "Stylish summer dress for women.",
                    price = 49,
                    discountPercentage = 15.0,
                    rating = 4.7,
                    stock = 50,
                    brand = "Brand B",
                    category = "clothing",
                    thumbnail = "https://i.dummyjson.com/data/products/41/thumbnail.jpg",
                    images = listOf(
                        "https://i.dummyjson.com/data/products/41/1.jpg",
                        "https://i.dummyjson.com/data/products/41/2.jpg"
                    )
                ),
                Product(
                    id = 3,
                    title = "Men's Jeans",
                    description = "Classic fit jeans for men.",
                    price = 39,
                    discountPercentage = 5.0,
                    rating = 4.3,
                    stock = 75,
                    brand = "Brand C",
                    category = "clothing",
                    thumbnail = "https://i.dummyjson.com/data/products/31/thumbnail.jpg",
                    images = listOf(
                        "https://i.dummyjson.com/data/products/31/1.jpg",
                        "https://i.dummyjson.com/data/products/31/2.jpg"
                    )
                )
            ),
            skip = 0,
            total = 3
        )
    }

    fun getMockCategories(): List<String> {
        return listOf(
            "clothing",
            "accessories",
            "footwear",
            "bags",
            "jewelry"
        )
    }
}
