package store.model.service

import store.model.repository.PromotionRepository
import java.time.LocalDate

class PromotionService(private val promotionRepository: PromotionRepository) {
    fun getRequiredQuantity(promotionName: String): Int? {
        return promotionRepository.findByName(promotionName)?.requiredQuantity
    }

    fun getRewardQuantity(promotionName: String): Int? {
        return promotionRepository.findByName(promotionName)?.rewardQuantity
    }
    
    fun checkIsExpired(promotionName: String, date: LocalDate): Boolean {
        val promotion = promotionRepository.findByName(promotionName) ?: return false
        return date.isBefore(promotion.startDate) || date.isAfter(promotion.endDate)
    }

}