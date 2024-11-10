package store.controller

import store.model.repository.ProductRepository
import store.model.repository.PromotionRepository
import store.model.service.InputService
import store.model.service.ProductService
import store.model.service.PromotionService
import store.values.ErrorMessages
import store.view.InputView
import store.view.OutputView
import camp.nextstep.edu.missionutils.DateTimes
import store.model.service.ReceiptBuilder

class PurchaseController(productFilePath: String, promotionFilePath: String) {
    private val productRepository = ProductRepository(productFilePath)
    private val promotionRepository = PromotionRepository(promotionFilePath)
    private val productService = ProductService(productRepository)
    private val promotionService = PromotionService(promotionRepository)
    private val inputService = InputService()

    fun run() {
        while (true) try {
            OutputView.printWelcomeMessage()
            OutputView.printProductList(productRepository.getAllNormalProducts())
            val purchases = inputService.requestPurchases()
            ReceiptBuilder().printReceipt()

        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }


}