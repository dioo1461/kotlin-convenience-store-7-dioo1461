package store.model.service

import store.model.domain.Promotion
import store.model.repository.PromotionRepository
import store.values.ErrorMessages
import java.time.LocalDateTime

class PromotionService(private val promotionRepository: PromotionRepository) {
    fun checkIsNotExpired(promotionName: String, date: LocalDateTime): Boolean {
        val promotion = promotionRepository.findByName(promotionName)
        require(promotion != null) { ErrorMessages.PROMOTION_NOT_FOUND }
        return promotion.startDate.isBefore(date.toLocalDate()) && promotion.endDate.isAfter(date.toLocalDate())
    }

    fun findPromotion(promotionName: String): Promotion? {
        return promotionRepository.findByName(promotionName)
    }
}