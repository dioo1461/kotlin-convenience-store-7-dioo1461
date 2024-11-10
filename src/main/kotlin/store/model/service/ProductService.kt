package store.model.service

import store.model.repository.ProductRepository
import store.values.ErrorMessages

class ProductService(private val productRepository: ProductRepository) {
    fun decreaseStock(productName: String, quantity: Int) {
        val remainingStock = productRepository.getStock(productName) - quantity
        require(remainingStock >= 0) { ErrorMessages.NORMAL_STOCK_CANNOT_BE_NEGATIVE }
        productRepository.setStock(productName, remainingStock)
    }

    fun decreasePromotionStock(productName: String, quantity: Int) {
        val remainingStock = productRepository.getPromotionStock(productName) - quantity
        require(remainingStock >= 0) { ErrorMessages.PROMOTION_STOCK_CANNOT_BE_NEGATIVE }
        productRepository.setPromotionStock(productName, remainingStock)

    }

    fun getPromotionName(productName: String): String? {
        return productRepository.findProduct(productName)?.promotionName
    }

    fun getPromotionProductStock(productName: String): Int {
        return productRepository.getPromotionStock(productName)
    }

    fun getStock(productName: String): Int {
        return productRepository.getStock(productName)
    }

    fun checkStockAvailability(productName: String, quantity: Int): Boolean {
        return productRepository.getStock(productName) >= quantity
    }

    fun checkPromotionStockAvailability(productName: String, quantity: Int): Boolean {
        return productRepository.getPromotionStock(productName) >= quantity
    }


}