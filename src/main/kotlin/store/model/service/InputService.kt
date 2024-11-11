package store.model.service

import store.model.domain.Purchase
import store.values.ErrorMessages
import store.values.Messages
import store.view.InputView

class InputService(private val productService: ProductService) {

    fun requestPurchases(): List<Purchase> {
        val input = InputView.requestPurchaseItemAndAmount()
        val purchaseList = parsePurchaseInput(input)
        purchaseList.forEach {
            require(
                productService.checkWholeStockAvailability(
                    it.product.name,
                    it.quantity
                )
            ) { ErrorMessages.QUANTITY_EXCEEDS_STOCK }
        }
        return purchaseList
    }

    fun askForPurchasingNonBenefitedProducts(itemName: String, quantity: Int): Boolean {
        val input = InputView.requestYesOrNo(Messages.LACK_OF_PROMOTION_STOCK.format(itemName, quantity))
        return parseYesOrNo(input)
    }

    fun askForExtraGiveaways(itemName: String, extraQuantity: Int): Boolean {
        val input = InputView.requestYesOrNo(Messages.ASK_FOR_EXTRA_GIVEAWAYS.format(itemName, extraQuantity))
        return parseYesOrNo(input)
    }

    fun askForMembershipDiscount(): Boolean {
        val input = InputView.requestYesOrNo(Messages.ASK_MEMBERSHIP_DISCOUNT)
        return parseYesOrNo(input)
    }

    fun askForPurchaseAgain(): Boolean {
        val input = InputView.requestYesOrNo(Messages.ASK_PURCHASE_AGAIN)
        return parseYesOrNo(input)
    }

    //  (예: [사이다-2],[감자칩-1])
    private fun parsePurchaseInput(input: String): List<Purchase> {
        require(input.matches(Regex("\\[.*]"))) { ErrorMessages.INVALID_FORMAT }
        val inputList = input.split(",")
        return inputList.map {
            val item = it.removeSurrounding("[", "]").split("-")
            val product = productService.findProduct(item[0])
            require(product != null) { ErrorMessages.PRODUCT_NOT_FOUND }
            Purchase(product, item[1].toInt())
        }
    }

    private fun parseYesOrNo(input: String): Boolean {
        require(input == "Y" || input == "N") { ErrorMessages.INVALID_INPUT }
        return input == "Y"
    }
}