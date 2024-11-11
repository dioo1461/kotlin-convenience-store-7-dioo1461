package store.model.domain

data class Receipt(
    val purchasedItems: List<PurchasedItem>,
    val giftedItems: List<GiveawayItem>,
    val totalQuantity: Int,
    val totalPrice: Int,
    val promotionDiscountAmount: Int,
    val membershipDiscountAmount: Int,
    val finalPrice: Int
)

data class PurchasedItem(
    val name: String,
    val quantity: Int,
    val price: Int
)

data class GiveawayItem(
    val name: String,
    val quantity: Int,
    val price: Int
)