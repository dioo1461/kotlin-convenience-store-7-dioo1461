package store.view

import store.model.domain.Product
import store.model.domain.Promotion
import store.model.domain.PromotionType

object OutputView {
    fun printWelcomeMessage(
        products: List<Product>,
        promotions: List<Promotion>
    ) {
        println("안녕하세요. w편의점입니다.")
        println("현재 보유하고 있는 상품입니다.")
        printProductList(products, promotions)
    }

    private fun printProductList(products: List<Product>, promotions: List<Promotion>) {
        products.forEach { product ->
            val stockInfo = getStockInfo(product.stock)
            val promotionInfo = getPromotionInfo(product.name, promotions)
            println("${product.name} ${"%,d".format(product.price)}원 $stockInfo $promotionInfo")
        }
    }

    private fun getStockInfo(stock: Int): String {
        return if (stock > 0) "${stock}개" else "품절"
    }

    private fun getPromotionInfo(
        productName: String,
        promotions: List<Promotion>
    ): String {
        val promotion = promotions.find { it.productName == productName }
        return when (promotion?.type) {
            PromotionType.ONE_PLUS_ONE -> "${promotion.productName}1+1"
            PromotionType.TWO_PLUS_ONE -> "${promotion.productName}2+1"
            else -> ""
        }
    }
}