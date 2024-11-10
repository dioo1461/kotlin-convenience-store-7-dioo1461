package store.model.service

import store.model.domain.GiveawayItem
import store.model.domain.Promotion
import store.model.domain.Purchase
import store.model.domain.PurchasedItem
import store.model.domain.Receipt

class ReceiptBuilder(
    private val purchasedList: List<Purchase>,
    private val purchaseService: PurchaseService
) {

    private val purchasedItems = mutableListOf<PurchasedItem>()
    private val giveawayItems = mutableListOf<GiveawayItem>()
    private var totalQuantity = 0
    private var totalPrice = 0
    private var promotionDiscountAmount = 0
    private var membershipDiscountAmount = 0
    private var finalPrice = 0

    init {
        purchasedList.forEach { purchase ->
            val purchasedItem = PurchasedItem(purchase.product.name, purchase.quantity, purchase.product.price)
            purchasedItems.add(purchasedItem)
            totalQuantity += purchase.quantity
            totalPrice += purchase.product.price
        }
        finalPrice = totalPrice
    }

    fun applyPromotions() {
        purchasedList.forEach { purchase ->
            // 프로모션 혜택을 적용하고, 결과에 따라 구매 및 증정 물품 수량을 업데이트
            val result = purchaseService.applyPromotionIfAvailable(purchase)
            promotionDiscountAmount += result.giveawayAmount * purchase.product.price

            if (result.giveawayAmount > 0) {
                giveawayItems.add(GiveawayItem(purchase.product.name, result.giveawayAmount))
            }

            // 프로모션 할인 금액을 최종 가격에서 차감
            finalPrice -= promotionDiscountAmount
        }
    }

    fun applyMembershipDiscount(isMember: Boolean) {
        if (isMember) {
            membershipDiscountAmount = (finalPrice * 0.3).toInt().coerceAtMost(8000) // 최대 한도 8,000원
            finalPrice -= membershipDiscountAmount
        }
    }

    fun build(): Receipt {
        return Receipt(
            purchasedItems,
            giveawayItems,
            totalQuantity,
            totalPrice,
            promotionDiscountAmount,
            membershipDiscountAmount,
            finalPrice
        )
    }
}