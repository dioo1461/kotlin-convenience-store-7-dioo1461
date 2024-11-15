package store.model.service

import store.model.domain.Product
import store.model.repository.ProductRepository
import store.values.ErrorMessages

class ProductService(private val productRepository: ProductRepository) {
    fun decreaseStock(productName: String, quantity: Int) {
        val remainingStock = productRepository.getStock(productName) - quantity
        require(remainingStock >= 0) { ErrorMessages.STOCK_CANNOT_BE_NEGATIVE }
        productRepository.setStock(productName, remainingStock)
    }

    fun decreasePromotionStock(productName: String, quantity: Int) {
        val remainingStock = productRepository.getPromotionStock(productName) - quantity
        require(remainingStock >= 0) { ErrorMessages.PROMOTION_STOCK_CANNOT_BE_NEGATIVE }
        productRepository.setPromotionStock(productName, remainingStock)
    }

    fun findProduct(productName: String): Product? {
        return productRepository.findProduct(productName)
    }

    fun getPromotionName(productName: String): String? {
        return productRepository.findProduct(productName)?.promotionName
    }

    fun getPromotionStock(productName: String): Int {
        return productRepository.getPromotionStock(productName)
    }

    fun getStock(productName: String): Int {
        return productRepository.getStock(productName)
    }

    fun checkWholeStockAvailability(productName: String, quantity: Int): Boolean {
        val product = productRepository.findProduct(productName)
        if (product!!.promotionName == null) {
            return productRepository.getStock(productName) >= quantity
        }
        return productRepository.getStock(productName) + productRepository.getPromotionStock(productName) >= quantity
    }

    fun checkStockAvailability(productName: String, quantity: Int): Boolean {
        return productRepository.getStock(productName) >= quantity
    }

    fun checkPromotionStockAvailability(productName: String, quantity: Int): Boolean {
        return productRepository.getPromotionStock(productName) >= quantity
    }


}