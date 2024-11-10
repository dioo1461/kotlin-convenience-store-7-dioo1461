package store.model.service

import store.model.domain.Purchase
import store.values.Messages
import store.view.InputView

class InputService {
    fun requestPurchases(): List<Pair<String, Int>> {
        val input = InputView.requestPurchaseItemAndAmount()
        return parsePurchaseInput(input)
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
    private fun parsePurchaseInput(input: String): List<Pair<String, Int>> {
        val inputList = input.split(",")
        return inputList.map {
            val item = it.removeSurrounding("[", "]").split("-")
            Pair(item[0], item[1].toInt())
        }
    }

    private fun parseYesOrNo(input: String): Boolean {
        return input == "Y"
    }
}