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
            this.purchasedItems.add(purchasedItem)
            this.totalQuantity += purchase.quantity
            this.totalPrice += purchase.product.price
        }
        this.finalPrice = totalPrice
    }

    fun applyPromotions(): ReceiptBuilder {
        purchasedList.forEach { purchase ->
            // 프로모션 혜택을 적용하고, 결과에 따라 구매 및 증정 물품 수량을 업데이트
            val result = purchaseService.applyPromotionIfAvailable(purchase)
            this.promotionDiscountAmount += (purchase.product.price * result.giveawayAmount)
            this.finalPrice -= promotionDiscountAmount
            this.giveawayItems.add(GiveawayItem(purchase.product.name, result.giveawayAmount))
        }
        return this
    }

    fun applyMembershipDiscount(): ReceiptBuilder {
        this.membershipDiscountAmount = purchaseService.applyMembershipDiscount(totalPrice)
        this.finalPrice -= membershipDiscountAmount
        return this
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