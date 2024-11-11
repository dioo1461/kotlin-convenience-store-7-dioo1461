package store.view

import camp.nextstep.edu.missionutils.Console
import store.values.Messages


object InputView {
    fun requestPurchaseItemAndAmount(): String {
        println(Messages.PROMPT_PURCHASE_ITEM)
        return getInput()
    }

    fun requestYesOrNo(prompt: String): String {
        println(prompt)
        return getInput().trim().uppercase()
    }

    private fun getInput(): String {
        return Console.readLine()
    }
}