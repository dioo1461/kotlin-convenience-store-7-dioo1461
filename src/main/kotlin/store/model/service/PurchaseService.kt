package store.model.service

import store.model.repository.ProductRepository
import store.model.repository.PromotionRepository
import store.values.ErrorMessages
import store.values.Messages

class PurchaseService(
    private val productService: ProductService,
    private val promotionService: PromotionService
) {
    fun purchaseProduct(productName: String, quantity: Int) {
        require(productService.checkStockAvailability(productName, quantity)) {
            ErrorMessages.QUANTITY_EXCEEDS_STOCK
        }
        productService.decreaseStock(productName, quantity)
    }
}