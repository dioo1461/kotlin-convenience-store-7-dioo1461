package store.controller

import store.model.repository.ProductRepository
import store.model.repository.PromotionRepository
import store.model.service.InputService
import store.model.service.ProductService
import store.model.service.PromotionService
import store.values.ErrorMessages
import store.view.InputView
import store.view.OutputView

class PurchaseController(productFilePath: String, promotionFilePath: String) {
    private val productRepository = ProductRepository(productFilePath)
    private val promotionRepository = PromotionRepository(promotionFilePath)
    private val productService = ProductService(productRepository)
    private val promotionService = PromotionService(promotionRepository)
    private val inputService = InputService()

    fun run() {
        while (true) try {
            OutputView.printWelcomeMessage()
            OutputView.printProductList(productRepository.getAll())

            val purchasePairs = inputService.parseInput(InputView.requestPurchaseItemAndAmount())
            purchasePairs.forEach {
                val (productName, quantity) = it

                require(productService.checkStockAvailability(productName, quantity)) {
                    ErrorMessages.QUANTITY_EXCEEDS_STOCK
                }
                

            }

        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

}