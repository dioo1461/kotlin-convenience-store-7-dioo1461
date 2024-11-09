package store.model.service

import store.model.repository.ProductRepository
import store.values.ErrorMessages

class ProductService(private val productRepository: ProductRepository) {
    fun checkStockAvailability(productName: String, quantity: Int): Boolean {
        return productRepository.getStock(productName) >= quantity
    }

    fun decreaseStock(productName: String, quantity: Int): Boolean {
        val remainingStock = productRepository.getStock(productName) - quantity
        if (remainingStock < quantity) {
            return false
        }
        productRepository.setStock(productName, remainingStock - quantity)
        return true
    }

    fun getProductPrice(productName: String): Int {
        return productRepository.findByName(productName).price
    }
}