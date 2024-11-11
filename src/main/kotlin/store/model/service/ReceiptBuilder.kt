package store.model.service

import store.model.domain.GiveawayItem
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
            this.totalQuantity += purchase.quantity
            this.totalPrice += purchase.product.price * purchase.quantity
            val purchasedItem = PurchasedItem(purchase.product.name, purchase.quantity, purchase.product.price)
            this.purchasedItems.add(purchasedItem)
        }
        this.finalPrice = totalPrice
    }

    fun applyPromotions(): ReceiptBuilder {
        purchasedList.forEach { purchase ->
            val result = purchaseService.applyPromotions(purchase)
            println("${purchase.product.name} result: $result")
            purchase.quantity = result.totalAmount
            val purchasedItem = purchasedItems.find { it.name == purchase.product.name }!!
            purchasedItem.quantity = result.totalAmount

            this.promotionDiscountAmount += (purchase.product.price * result.giveawayAmount)
            this.totalQuantity += result.additionalGiveawayAmount
            this.totalQuantity -= result.purchasedQuantityReduced
            this.totalPrice -= result.purchasedQuantityReduced * purchase.product.price
            this.totalPrice += result.additionalGiveawayAmount * purchase.product.price
            this.giveawayItems.add(GiveawayItem(purchase.product.name, result.giveawayAmount, purchase.product.price))
        }
        this.finalPrice = this.totalPrice - this.promotionDiscountAmount
        return this
    }

    fun applyMembershipDiscount(): ReceiptBuilder {
        this.membershipDiscountAmount = purchaseService.applyMembershipDiscount(totalPrice)
        this.finalPrice -= membershipDiscountAmount
        println("membershipDiscountAmount: $membershipDiscountAmount")
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