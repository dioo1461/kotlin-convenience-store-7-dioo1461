package store.view

import store.model.domain.Product
import store.model.domain.Promotion
import store.model.domain.PromotionType
import store.values.Messages

object OutputView {
    fun printWelcomeMessage() {
        println(Messages.WELCOME_MESSAGE)
    }

    fun printProductList(products: List<Product>) {
        products.forEach { product ->
            val stockInfo = getStockInfo(product.stock)
            val promotionInfo = product.promotionName ?: ""
            println("${product.name} ${"%,d".format(product.price)}원 $stockInfo $promotionInfo")
        }
    }

    private fun getStockInfo(stock: Int): String {
        return if (stock > 0) "${stock}개" else "품절"
    }

}