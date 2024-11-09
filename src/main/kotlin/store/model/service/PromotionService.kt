package store.model.service

import store.model.repository.PromotionRepository
import store.values.ErrorMessages

class PromotionService(private val promotionRepository: PromotionRepository) {

    fun checkStockAvailability(productName: String, quantity: Int): Boolean {
        return promotionRepository.getStock(productName) >= quantity
    }

    fun decreaseStock(productName: String, quantity: Int): Boolean {
        val remainingStock = promotionRepository.getStock(productName) - quantity
        if (remainingStock < quantity) {
            return false
        }
        promotionRepository.setStock(productName, quantity)
        return true
    }

}