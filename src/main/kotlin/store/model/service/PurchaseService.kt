package store.model.service

import camp.nextstep.edu.missionutils.DateTimes
import store.model.domain.Promotion
import store.model.domain.Purchase
import kotlin.math.min

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
        val giveawayAmount: Int,
        val purchasedQuantityReduced: Int,
        val additionalGiveawayAmount: Int
    )

    fun applyPromotions(purchase: Purchase): ApplyPromotionResult {
        val promotion = getPromotionIfNotExpired(purchase) ?: return handlePromotionNotAvailable(purchase)
        val promotionStock = productService.getPromotionStock(purchase.product.name)
        val divider = promotion.requiredQuantity + promotion.giveawayQuantity
        val quotient = purchase.quantity / divider
        val remainder = purchase.quantity % divider
        if (promotionStock < purchase.quantity - remainder)
            return handlePromotionStockShortage(purchase, promotion, promotionStock, divider, remainder)
        if (0 < remainder && remainder <= divider - promotion.giveawayQuantity)
            return handleExtraGiveaways(purchase, promotion, quotient, remainder)
        return handleStandardPromotion(purchase, promotion, quotient)
    }

    private fun handlePromotionNotAvailable(purchase: Purchase): ApplyPromotionResult {
        productService.decreaseStock(purchase.product.name, purchase.quantity)
        return ApplyPromotionResult(purchase.quantity, 0, 0, 0)
    }

    private fun handlePromotionStockShortage(
        purchase: Purchase,
        promotion: Promotion,
        promotionStock: Int,
        divider: Int,
        remainder: Int
    ): ApplyPromotionResult {
        val promotionStockConsumption = promotionStock / divider * divider
        val giveawayAmount = promotionStockConsumption / divider * promotion.giveawayQuantity
        val nonBenefitedAmount = purchase.quantity - promotionStockConsumption
        if (inputService.askForPurchasingNonBenefitedProducts(purchase.product.name, nonBenefitedAmount)) {
            productService.decreaseStock(purchase.product.name, nonBenefitedAmount)
            productService.decreasePromotionStock(purchase.product.name, promotionStockConsumption)
            return ApplyPromotionResult(purchase.quantity, giveawayAmount, 0, 0)
        }
        productService.decreaseStock(purchase.product.name, remainder)
        productService.decreasePromotionStock(purchase.product.name, promotionStockConsumption)
        return ApplyPromotionResult(promotionStockConsumption, giveawayAmount, nonBenefitedAmount, 0)
    }

    private fun handleExtraGiveaways(
        purchase: Purchase,
        promotion: Promotion,
        quotient: Int,
        remainder: Int
    ): ApplyPromotionResult {
        val baseGiveaways = quotient * promotion.giveawayQuantity
        val additionalGiveaways = min(remainder, productService.getPromotionStock(purchase.product.name))
        val promotionStockConsumption = purchase.quantity - remainder
        productService.decreasePromotionStock(purchase.product.name, promotionStockConsumption)
        if (inputService.askForExtraGiveaways(purchase.product.name, remainder)) {
            productService.decreasePromotionStock(purchase.product.name, remainder)
            return ApplyPromotionResult(
                purchase.quantity + additionalGiveaways,
                baseGiveaways + additionalGiveaways,
                0,
                additionalGiveaways
            )
        }
        return ApplyPromotionResult(purchase.quantity, baseGiveaways, 0, 0)
    }

    private fun handleStandardPromotion(purchase: Purchase, promotion: Promotion, quotient: Int): ApplyPromotionResult {
        productService.decreasePromotionStock(purchase.product.name, purchase.quantity)
        return ApplyPromotionResult(purchase.quantity, quotient * promotion.giveawayQuantity, 0, 0)
    }

    fun applyMembershipDiscount(totalPrice: Int): Int {
        if (!inputService.askForMembershipDiscount()) return 0
        return (totalPrice * 0.3).toInt().coerceAtMost(8000)
    }
}