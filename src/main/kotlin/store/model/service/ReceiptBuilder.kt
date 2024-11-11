package store.model.service

import store.model.domain.GiveawayItem
import store.model.domain.Purchase
import store.model.domain.PurchasedItem
import store.model.domain.Receipt

class ReceiptBuilder(
    private var purchasedList: List<Purchase>,
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
            this.totalQuantity += purchase.quantity
            this.totalPrice += purchase.product.price * purchase.quantity
        }
        this.finalPrice = totalPrice
    }

    fun applyPromotions(): ReceiptBuilder {
        purchasedList = purchasedList.map { purchase ->
            val result = purchaseService.applyPromotions(purchase)
            purchase.quantity = result.totalAmount
            this.promotionDiscountAmount += (purchase.product.price * result.giveawayAmount)
            this.totalPrice -= result.purchasedQuantityReduced * purchase.product.price
            this.finalPrice -= result.purchasedQuantityReduced * purchase.product.price
            this.totalQuantity -= result.purchasedQuantityReduced
            this.finalPrice -= promotionDiscountAmount
            this.giveawayItems.add(GiveawayItem(purchase.product.name, result.giveawayAmount))
            purchase
        }
        return this
    }

    fun applyMembershipDiscount(): ReceiptBuilder {
        this.membershipDiscountAmount = purchaseService.applyMembershipDiscount(totalPrice)
        this.finalPrice -= membershipDiscountAmount
        return this
    }

    fun build(): Receipt {
        purchasedList.forEach { purchase ->
            val purchasedItem = PurchasedItem(purchase.product.name, purchase.quantity, purchase.product.price)
            this.purchasedItems.add(purchasedItem)
        }
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