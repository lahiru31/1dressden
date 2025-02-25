package com.khadar3344.myshop.data.network.repository

import com.khadar3344.myshop.data.mock.MockDataProvider
import com.khadar3344.myshop.data.network.dto.Product
import com.khadar3344.myshop.data.network.dto.Products
import com.khadar3344.myshop.util.Resource
import javax.inject.Inject

class MockNetworkRepositoryImpl @Inject constructor() : NetworkRepository {
    override suspend fun getProductsListFromApi(): Resource<List<Product>> {
        return try {
            Resource.Success(MockDataProvider.getMockProducts().products)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getSingleProductByIdFromApi(productId: Int): Resource<Product> {
        return try {
            val product = MockDataProvider.getMockProducts().products.find { it.id == productId }
            if (product != null) {
                Resource.Success(product)
            } else {
                Resource.Failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getProductsListBySearchFromApi(query: String): Resource<List<Product>> {
        return try {
            val filteredProducts = MockDataProvider.getMockProducts().products.filter { product ->
                product.title.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true) ||
                product.brand.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true)
            }
            Resource.Success(filteredProducts)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAllCategoriesListFromApi(): Resource<List<String>> {
        return try {
            Resource.Success(MockDataProvider.getMockCategories())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getProductsListByCategoryNameFromApi(categoryName: String): Resource<List<Product>> {
        return try {
            val filteredProducts = MockDataProvider.getMockProducts().products.filter { product ->
                product.category.equals(categoryName, ignoreCase = true)
            }
            Resource.Success(filteredProducts)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
