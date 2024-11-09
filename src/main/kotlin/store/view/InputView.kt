package store.view

import camp.nextstep.edu.missionutils.Console


object InputView {
    fun requestPurchaseItemAndAmount(): String {
        println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])")
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