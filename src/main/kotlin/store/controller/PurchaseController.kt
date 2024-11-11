package store.controller

import store.model.repository.ProductRepository
import store.model.repository.PromotionRepository
import store.model.service.InputService
import store.model.service.ProductService
import store.model.service.PromotionService
import store.model.service.PurchaseService
import store.model.service.ReceiptBuilder
import store.view.InputView
import store.view.OutputView

class PurchaseController(productFilePath: String, promotionFilePath: String) {
    private val productRepository = ProductRepository(productFilePath)
    private val promotionRepository = PromotionRepository(promotionFilePath)
    private val productService = ProductService(productRepository)
    private val promotionService = PromotionService(promotionRepository)
    private val inputService = InputService(productService)
    private val purchaseService = PurchaseService(productService, promotionService, inputService)

    fun run() {
        while (true) try {
            OutputView.printWelcomeMessage()
            OutputView.printProductList(productRepository.getAllProducts())
            val purchases = inputService.requestPurchases()
            val receipt = ReceiptBuilder(purchases, purchaseService).applyPromotions().build()
            OutputView.printReceipt(receipt)
            if (!inputService.askForPurchaseAgain()) break
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }
}