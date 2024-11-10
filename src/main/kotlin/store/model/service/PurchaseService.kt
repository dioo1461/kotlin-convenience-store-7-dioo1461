package store.model.service

import camp.nextstep.edu.missionutils.DateTimes
import store.model.domain.Promotion
import store.model.domain.Purchase

class PurchaseService(
    private val productService: ProductService,
    private val promotionService: PromotionService,
    private val inputService: InputService
) {
    fun getPromotionIfAvailable(purchase: Purchase): Promotion? {
        val promotionName = productService.getPromotionName(purchase.product.name) ?: return null
        if (!promotionService.checkIsExpired(promotionName, DateTimes.now())) return null
        if (!productService.checkPromotionStockAvailability(promotionName, purchase.quantity)) return null
        return promotionService.findPromotion(promotionName)
    }

    data class ApplyPromotionResult(
        val purchaseAmount: Int,
        val giveawayAmount: Int = 0,
    )

    fun applyPromotionIfAvailable(purchase: Purchase): ApplyPromotionResult {
        val promotion = getPromotionIfAvailable(purchase) ?: return ApplyPromotionResult(purchaseAmount = purchase.quantity)
        val promotionStock = productService.getPromotionProductStock(purchase.product.name)

        // 증정 수량과 실제 구매할 수량 계산
        val giveawayAmount = purchase.quantity / promotion.requiredQuantity * promotion.giveawayQuantity
        var purchaseAmount = purchase.quantity - giveawayAmount

        // 프로모션 재고 부족 시
        if (promotionStock < giveawayAmount) {
            val availableGiveawayAmount = promotionStock / promotion.requiredQuantity * promotion.giveawayQuantity
            val nonBenefitedAmountDueToStock = purchaseAmount + (giveawayAmount - availableGiveawayAmount)

            // 정가 결제 여부를 확인하고, 동의하지 않으면 프로모션 재고만큼만 적용 후 반환
            if (!inputService.askForPurchasingNonBenefitedProducts(purchase.product.name, nonBenefitedAmountDueToStock)) {
                productService.decreasePromotionStock(purchase.product.name, availableGiveawayAmount)
                return ApplyPromotionResult(purchaseAmount, availableGiveawayAmount)
            }

            // 동의하면 프로모션 재고와 일반 재고에서 차감 후 반환
            productService.decreasePromotionStock(purchase.product.name, promotionStock)
            productService.decreaseStock(purchase.product.name, nonBenefitedAmountDueToStock)
            return ApplyPromotionResult(
                purchaseAmount + nonBenefitedAmountDueToStock,
                availableGiveawayAmount
            )
        }

        // 프로모션 혜택을 위한 추가 수량이 필요한 경우
        val remainingNeededForPromotion = promotion.requiredQuantity - purchase.quantity % promotion.requiredQuantity
        if (remainingNeededForPromotion > 0) {
            // 추가 혜택을 원하지 않으면, 현재 계산된 purchaseAmount와 giveawayAmount로 반환
            if (!inputService.askForExtraGiveaways(purchase.product.name, remainingNeededForPromotion)) {
                productService.decreasePromotionStock(purchase.product.name, giveawayAmount)
                return ApplyPromotionResult(purchaseAmount, giveawayAmount)
            }

            // 사용자가 추가 혜택을 원하면, 추가 수량을 구매 금액에 포함하고 재고 차감 후 반환
            purchaseAmount += remainingNeededForPromotion
            productService.decreasePromotionStock(purchase.product.name, giveawayAmount + promotion.giveawayQuantity)
        } else {
            // 프로모션 재고가 충분한 경우 차감 후 반환
            productService.decreasePromotionStock(purchase.product.name, giveawayAmount)
        }

        return ApplyPromotionResult(purchaseAmount, giveawayAmount)
    }


}