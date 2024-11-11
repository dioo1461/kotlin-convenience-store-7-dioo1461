package store.model.service

import camp.nextstep.edu.missionutils.DateTimes
import store.model.domain.Promotion
import store.model.domain.Purchase

class PurchaseService(
    private val productService: ProductService,
    private val promotionService: PromotionService,
    private val inputService: InputService
) {
    fun getPromotionIfNotExpired(purchase: Purchase): Promotion? {
        val promotionName = productService.getPromotionName(purchase.product.name) ?: return null
        if (!promotionService.checkIsNotExpired(promotionName, DateTimes.now())) return null
        return promotionService.findPromotion(promotionName)
    }

    data class ApplyPromotionResult(
        val totalAmount: Int,
        val giveawayAmount: Int = 0,
        val purchasedQuantityReduced: Int
    )

    fun applyPromotions(purchase: Purchase): ApplyPromotionResult {
        val promotion = getPromotionIfNotExpired(purchase) ?: run {
            productService.decreaseStock(purchase.product.name, purchase.quantity)
            return ApplyPromotionResult(purchase.quantity, 0, 0)
        }
        val promotionStock = productService.getPromotionStock(purchase.product.name)

        val divider = promotion.requiredQuantity + promotion.giveawayQuantity
        val quotient = purchase.quantity / divider
        val remainder = purchase.quantity % divider

        if (promotionStock < purchase.quantity - remainder) {
            println("${promotion.name} 프로모션을 적용할 수 없습니다.")
            val promotionStockConsumption = promotionStock - remainder
            val giveawayAmount = promotionStockConsumption / divider * promotion.giveawayQuantity
            val nonBenefitedAmount = purchase.quantity - promotionStockConsumption
            if (inputService.askForPurchasingNonBenefitedProducts(purchase.product.name, nonBenefitedAmount)) {
                productService.decreaseStock(purchase.product.name, nonBenefitedAmount)
                productService.decreasePromotionStock(purchase.product.name, promotionStockConsumption)
                println("${promotion.name} 프로모션을 적용하고 나머지 ${nonBenefitedAmount}개를 구매합니다.")
                return ApplyPromotionResult(purchase.quantity, giveawayAmount, 0)
            }
            productService.decreasePromotionStock(purchase.product.name, promotionStockConsumption)
            println("${promotion.name} 프로모션을 적용하지 않습니다.")
            println("promotionStockConsumption: $promotionStockConsumption")
            return ApplyPromotionResult(promotionStockConsumption, giveawayAmount, nonBenefitedAmount)
        }

        productService.decreasePromotionStock(purchase.product.name, 1)
        return ApplyPromotionResult(1, 1, 1)
    }


    fun applyMembershipDiscount(totalPrice: Int): Int {
        if (!inputService.askForMembershipDiscount()) return 0
        return (totalPrice * 0.3).toInt().coerceAtMost(8000)
    }

}