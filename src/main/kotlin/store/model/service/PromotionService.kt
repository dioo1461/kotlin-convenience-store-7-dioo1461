package store.model.service

import store.model.domain.Promotion
import store.model.repository.PromotionRepository
import java.time.LocalDateTime

class PromotionService(private val promotionRepository: PromotionRepository) {
    fun checkIsNotExpired(promotionName: String, date: LocalDateTime): Boolean {
        val promotion = promotionRepository.findByName(promotionName) ?: return false
        return promotion.endDate.isBefore(date.toLocalDate())
    }

    fun findPromotion(promotionName: String): Promotion? {
        return promotionRepository.findByName(promotionName)
    }
}