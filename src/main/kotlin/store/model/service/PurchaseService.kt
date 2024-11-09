package store.model.service

import store.model.repository.ProductRepository
import store.model.repository.PromotionRepository

class PurchaseService(
    private val productService: ProductService,
    private val promotionService: PromotionService
) {


}