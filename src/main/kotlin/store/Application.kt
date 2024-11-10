package store

import store.controller.PurchaseController

fun main() {
    val productFilePath = "src/main/resources/products.md"
    val promotionFilePath = "src/main/resources/promotions.md"
    val purchaseController = PurchaseController(productFilePath, promotionFilePath)
    purchaseController.run()
}
